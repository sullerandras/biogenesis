//! Threads: what Java gives the simulation when `Utils.THREAD_COUNT` > 1.
//!
//! The parallel executor (`native/parallel_executor.rs`, Java's
//! `ParallelExecutor` scheme) moves organisms on several threads at once.
//! Java's code relies on three things there, and this module provides them:
//!
//! - `synchronized`: monitors. A `static final Object x_monitor = new
//!   Object()` is a `JMonitor`; other objects get theirs from a registry
//!   keyed by address. Reentrant, as Java's.
//! - `Collections.synchronizedSet`: every operation on a collection takes
//!   that collection's monitor (when it has one, i.e. code synchronizes on
//!   it), so `synchronized (_organisms)` excludes `_organisms.add` as in Java.
//! - The garbage collector: a thread may read a reference field another
//!   thread overwrites at the same moment (statistics' `lastBornBeing`, a
//!   neighbour's fields), and Java guarantees that what it reads is a live
//!   object. Here the old value of a reference field overwritten during the
//!   parallel phase isn't dropped but kept (`defer`) until all threads are
//!   done, so a racing `clone()` always finds the object alive, and the swap
//!   itself is under a (striped) lock so two writers can't both drop the
//!   old value.
//!
//! All of this only happens while `active()`: from the executor's start of
//! the parallel phase to its end. Otherwise (THREAD_COUNT 1, and between
//! the phases) the simulation runs on one thread and every lock here is a
//! branch on a flag, so the serial simulation keeps its speed and stays
//! bit-identical to Java's.
//!
//! What stays as in Java: plain fields (ints, doubles) written by one thread
//! and read by another race, Java-style (a read sees the old or the new
//! value; Rust formally calls that undefined, the generated code does plain
//! loads and stores, as the JVM does). Organisms near each other never move
//! at the same time: the executor's line locks ensure that, as in Java.

use std::cell::{Cell, RefCell};
use std::sync::Mutex;
use std::sync::atomic::{AtomicBool, AtomicUsize, Ordering};

/// A value on a cache line of its own (128 bytes on Apple Silicon): a line
/// that one thread writes and others read costs every reader a miss.
#[repr(align(128))]
pub struct CacheLine<T>(pub T);

/// Read by every thread all the time (every monitor, every reference
/// store), so nothing written may share its line.
static ACTIVE: CacheLine<AtomicBool> = CacheLine(AtomicBool::new(false));

/// Are organisms moving on several threads right now?
#[inline(always)]
pub fn active() -> bool {
    ACTIVE.0.load(Ordering::Relaxed)
}

/// Set by the executor around the parallel phase, while no worker runs
/// (starting and joining the workers orders it with their accesses).
pub fn set_active(on: bool) {
    ACTIVE.0.store(on, Ordering::SeqCst);
}

thread_local! {
    static TOKEN: u8 = const { 0 };
}

/// A nonzero number that identifies the current thread.
#[inline]
fn thread_token() -> usize {
    TOKEN.with(|t| t as *const u8 as usize)
}

/// Waiting for a lock that is held for a few instructions (the
/// atmosphere's): spin, twice as long each round, and only after some
/// 4,000 spins give the core away.
#[inline]
fn backoff(spins: &mut u32) {
    if *spins < 12 {
        for _ in 0..1u32 << *spins {
            std::hint::spin_loop();
        }
        *spins += 1;
    } else {
        std::thread::yield_now();
    }
}

/// A reentrant lock (a Java monitor). `lock` locks only while `active()`;
/// `lock_always` always does (lazy static initialization). On a cache line
/// of its own (128 bytes on Apple Silicon): the atmosphere's monitors are
/// statics side by side, and every organism takes them every frame.
#[repr(align(128))]
pub struct JMonitor {
    owner: AtomicUsize,
    /// Only the owner reads or writes it.
    depth: Cell<u32>,
}

// SAFETY: `depth` is only touched by the thread that owns the lock.
unsafe impl Sync for JMonitor {}
unsafe impl Send for JMonitor {}

impl Default for JMonitor {
    fn default() -> Self {
        JMonitor::new()
    }
}

impl JMonitor {
    pub const fn new() -> Self {
        JMonitor { owner: AtomicUsize::new(0), depth: Cell::new(0) }
    }

    /// `synchronized (x) { ... }`: the guard unlocks at the end of the block.
    #[inline]
    pub fn lock(&self) -> JMonitorGuard<'_> {
        if !active() {
            return JMonitorGuard(None);
        }
        self.lock_always()
    }

    pub fn lock_always(&self) -> JMonitorGuard<'_> {
        let me = thread_token();
        if self.owner.load(Ordering::Relaxed) == me {
            self.depth.set(self.depth.get() + 1);
        } else {
            let mut spins = 0;
            // Test and test-and-set: waiters read the line, only a free lock is written.
            while self.owner.compare_exchange_weak(0, me, Ordering::Acquire, Ordering::Relaxed).is_err() {
                while self.owner.load(Ordering::Relaxed) != 0 {
                    backoff(&mut spins);
                }
            }
            self.depth.set(1);
        }
        JMonitorGuard(Some(self))
    }
}

pub struct JMonitorGuard<'a>(Option<&'a JMonitor>);

impl Drop for JMonitorGuard<'_> {
    #[inline]
    fn drop(&mut self) {
        if let Some(m) = self.0 {
            let d = m.depth.get() - 1;
            m.depth.set(d);
            if d == 0 {
                m.owner.store(0, Ordering::Release);
            }
        }
    }
}

// ---------------------------------------------------------------- monitors of objects

/// The monitors of objects that code synchronizes on (a few: the world, its
/// `_organisms`): an open-addressing table keyed by the object's address,
/// read without locking. Entries are never removed; an address reused by a
/// later object just shares the monitor, which is harmless.
const MONITORS: usize = 1024;
static MONITOR_KEYS: [AtomicUsize; MONITORS] = [const { AtomicUsize::new(0) }; MONITORS];
static MONITOR_SLOTS: [JMonitor; MONITORS] = [const { JMonitor::new() }; MONITORS];
static MONITOR_INSERT: Mutex<()> = Mutex::new(());

#[inline]
fn monitor_hash(key: usize) -> usize {
    (key >> 4).wrapping_mul(0x9E37_79B9_7F4A_7C15) >> (usize::BITS - 10)
}

/// The monitor of the object at `key`, if it has one.
#[inline]
fn find_monitor(key: usize) -> Option<&'static JMonitor> {
    let mut i = monitor_hash(key);
    for _ in 0..MONITORS {
        match MONITOR_KEYS[i].load(Ordering::Acquire) {
            k if k == key => return Some(&MONITOR_SLOTS[i]),
            0 => return None,
            _ => i = (i + 1) % MONITORS,
        }
    }
    None
}

/// The monitor of the object at `key`, made on first use.
fn monitor(key: usize) -> &'static JMonitor {
    if let Some(m) = find_monitor(key) {
        return m;
    }
    let _g = MONITOR_INSERT.lock().unwrap();
    if let Some(m) = find_monitor(key) {
        return m;
    }
    let mut i = monitor_hash(key);
    for _ in 0..MONITORS {
        if MONITOR_KEYS[i].load(Ordering::Relaxed) == 0 {
            MONITOR_KEYS[i].store(key, Ordering::Release);
            return &MONITOR_SLOTS[i];
        }
        i = (i + 1) % MONITORS;
    }
    panic!("J2R: more than {MONITORS} objects used as monitors");
}

/// `synchronized (obj) { ... }` and synchronized methods (`this`'s monitor).
/// The monitor is registered even when not `active()`, so that the object's
/// collection operations lock it later (see `collection_guard`).
pub fn lock_object(key: Option<*const ()>) -> JMonitorGuard<'static> {
    let Some(key) = key else { super::throw("java.lang.NullPointerException") };
    monitor(key as usize).lock()
}

/// `Collections.synchronizedSet(x)` etc.: gives x a monitor.
pub fn make_synchronized(key: Option<*const ()>) {
    if let Some(key) = key {
        monitor(key as usize);
    }
}

/// For an operation on a collection: its monitor, if it has one.
#[inline]
pub fn collection_guard(key: Option<*const ()>) -> JMonitorGuard<'static> {
    if !active() {
        return JMonitorGuard(None);
    }
    match key.and_then(|k| find_monitor(k as usize)) {
        Some(m) => m.lock_always(),
        None => JMonitorGuard(None),
    }
}

// ---------------------------------------------------------------- deferred drops

trait Erased {}
impl<T> Erased for T {}

/// Values dropped later, on the executor's thread.
struct Deferred(#[allow(dead_code)] Vec<Box<dyn Erased>>);
// SAFETY: what is deferred are references (JRef, JStr...), which the
// parallel phase shares between threads anyway (see the module doc).
unsafe impl Send for Deferred {}

thread_local! {
    static GRAVEYARD: RefCell<Vec<Box<dyn Erased>>> = const { RefCell::new(Vec::new()) };
}

static GRAVEYARDS: Mutex<Vec<Deferred>> = Mutex::new(Vec::new());

/// Keeps `v` until the parallel phase ends (it's the old value of a field
/// that another thread may be reading).
#[cold]
pub fn defer<T: 'static>(v: T) {
    GRAVEYARD.with(|g| g.borrow_mut().push(Box::new(v)));
}

/// A worker hands its deferred values over at the end of its job.
pub fn hand_over_deferred() {
    let v = GRAVEYARD.with(|g| std::mem::take(&mut *g.borrow_mut()));
    if !v.is_empty() {
        GRAVEYARDS.lock().unwrap().push(Deferred(v));
    }
}

/// After the parallel phase: drops what was deferred.
pub fn drop_deferred() {
    hand_over_deferred();
    let all = std::mem::take(&mut *GRAVEYARDS.lock().unwrap());
    drop(all);
}

/// Striped locks for overwriting reference fields while `active()`.
const STRIPES: usize = 64;
static STRIPE: [CacheLine<AtomicBool>; STRIPES] = [const { CacheLine(AtomicBool::new(false)) }; STRIPES];

/// `*slot = v` while other threads may access the slot: the old value is
/// deferred (see the module doc).
///
/// # Safety
/// `slot` must be valid for writes.
#[cold]
pub unsafe fn store_shared<F: 'static>(slot: *mut F, v: F) {
    let s = &STRIPE[((slot as usize) >> 3) % STRIPES].0;
    let mut spins = 0;
    while s.compare_exchange_weak(false, true, Ordering::Acquire, Ordering::Relaxed).is_err() {
        while s.load(Ordering::Relaxed) {
            backoff(&mut spins);
        }
    }
    // SAFETY: valid (the caller), and writers of this slot are excluded by the stripe.
    let old = unsafe { std::ptr::replace(slot, v) };
    s.store(false, Ordering::Release);
    defer(old);
}
