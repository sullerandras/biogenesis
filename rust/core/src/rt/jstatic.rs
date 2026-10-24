//! Java static fields.
//!
//! A static is one global value, initialized on first use (Java initializes a
//! class's statics on first use of the class; nothing here depends on the
//! difference). The value is a plain `UnsafeCell`, so a read is a load, not
//! a `thread_local!` lookup: like a Java static, it is shared by all
//! threads, and races as Java's do (see rt/parallel.rs). The first use may
//! happen on a worker thread, so lazy initialization is under a lock.

use std::cell::UnsafeCell;
use std::sync::atomic::{AtomicBool, Ordering};

use super::parallel::{self, JMonitor};

pub struct JStatic<T> {
    value: UnsafeCell<Option<T>>,
    ready: AtomicBool,
    init: fn() -> T,
}

// SAFETY: Java's semantics for statics (see the module doc).
unsafe impl<T> Sync for JStatic<T> {}

fn uninitialized<T>() -> T {
    unreachable!()
}

/// Lazy initializations run one at a time (reentrant: an initializer may
/// use another lazy static).
static INIT: JMonitor = JMonitor::new();

impl<T> JStatic<T> {
    /// A static whose initializer is a constant expression.
    pub const fn new(value: T) -> Self {
        JStatic { value: UnsafeCell::new(Some(value)), ready: AtomicBool::new(true), init: uninitialized::<T> }
    }

    /// A static initialized by running `init` on first use.
    pub const fn lazy(init: fn() -> T) -> Self {
        JStatic { value: UnsafeCell::new(None), ready: AtomicBool::new(false), init }
    }

    #[inline]
    fn slot(&self) -> &T {
        if !self.ready.load(Ordering::Acquire) {
            self.initialize();
        }
        // SAFETY: initialized (above), and no reference into the slot outlives a get/set/with.
        unsafe { (*self.value.get()).as_ref().unwrap_unchecked() }
    }

    #[cold]
    #[inline(never)]
    fn initialize(&self) {
        let _g = INIT.lock_always();
        // SAFETY: under INIT; `init` may have set the static itself, Java's value wins then too.
        unsafe {
            if (*self.value.get()).is_none() {
                let v = (self.init)();
                if (*self.value.get()).is_none() {
                    *self.value.get() = Some(v);
                }
            }
        }
        self.ready.store(true, Ordering::Release);
    }

    /// Reads the value. The borrow ends when `f` returns.
    #[inline]
    pub fn with<R>(&self, f: impl FnOnce(&T) -> R) -> R {
        f(self.slot())
    }

    /// The value in place, for a call on a static that is never assigned
    /// after its initialization (J2R checks that), so no `clone()`.
    #[inline]
    pub fn get_ref(&self) -> &T {
        self.slot()
    }

    #[inline]
    pub fn set(&self, v: T)
    where
        T: 'static,
    {
        if !self.ready.load(Ordering::Acquire) {
            self.initialize();
        }
        // SAFETY: as in `slot`. The old value is dropped after the slot is updated.
        let slot = self.value.get();
        if std::mem::needs_drop::<T>() && parallel::active() {
            // SAFETY: the static's slot, initialized above.
            unsafe { parallel::store_shared(slot, Some(v)) }
        } else {
            let old = unsafe { std::mem::replace(&mut *slot, Some(v)) };
            drop(old);
        }
    }
}

impl<T: Clone> JStatic<T> {
    #[inline]
    pub fn get(&self) -> T {
        self.slot().clone()
    }
}
