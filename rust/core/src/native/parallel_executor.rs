#![allow(non_snake_case)]

//! ParallelExecutor's parallel path: Java's thread machinery (WorkerThread,
//! WorkerJob, LinesLocker, a ConcurrentHashMap set), so hand-written, with
//! Java's algorithm. ThreadStartIndexes is translated.
//!
//! The world is split into vertical lines of buckets (OrganismBuckets'
//! columns); each thread moves the organisms of a range of lines, from left
//! to right, holding locks on the lines up to RANGE_TO_CLEAR away from the
//! one it is in. So two threads are always at least 5 lines (5 * 70 pixels)
//! apart: organisms that touch each other never move at the same time. The
//! ranges are adjusted after each frame, from the slowest and the fastest
//! thread's times. While the threads run, `rt::parallel` is active (monitors,
//! deferred drops: see there).

use std::panic::{AssertUnwindSafe, catch_unwind};
use std::sync::mpsc::{Receiver, Sender, channel};
use std::sync::atomic::{AtomicUsize, Ordering};
use std::sync::{Arc, Condvar, Mutex};
use std::thread::JoinHandle;
use std::time::Instant;

use crate::generated::{Organism, OrganismBuckets, parallel_executor, thread_start_indexes};
use crate::rt::parallel;
use crate::rt::*;

/// WorkerJob.RANGE_TO_CLEAR
const RANGE_TO_CLEAR: i32 = thread_start_indexes::MIN_RANGE_SIZE - 1;

/// A worker's stack: the organisms' code runs on it, as on the main thread.
const STACK_SIZE: usize = 64 << 20;

/// ParallelExecutor.progressAllOrganismsInParallel(int organismCount, int threadCount)
pub fn progressAllOrganismsInParallel(organismCount: i32, threadCount: i32) {
    let lineCount = OrganismBuckets::getMaxWidth(&parallel_executor::organismBuckets()) + 1;
    if threadCount * 5 > lineCount {
        // not enough gap between threads
        parallel_executor::progressAllOrganismsInSerial(organismCount);
        return;
    }

    let mut pool = POOL.lock().unwrap();
    if pool.workers.len() != threadCount as usize {
        pool.resize(threadCount as usize);
    }
    // Java initializes the ranges only when the thread count changes; a new
    // world of another width keeps the same pool here, so this runs every
    // frame (it does nothing when neither count changed).
    thread_start_indexes::initialize(threadCount, lineCount);
    let lines: Arc<Vec<Semaphore>> = Arc::new((0..lineCount).map(|_| Semaphore::new()).collect());
    let mut checked = std::mem::replace(&mut pool.checked, Arc::new(Checked(Vec::new())));
    Arc::get_mut(&mut checked).expect("the workers are done").reset(organismCount as usize);

    parallel::set_active(true);
    for (i, w) in pool.workers.iter().enumerate() {
        let job = Job {
            thread: i,
            startIndex: thread_start_indexes::getStartIndex(i as i32),
            endIndex: thread_start_indexes::getEndIndex(i as i32),
            lines: lines.clone(),
            checked: checked.clone(),
        };
        w.jobs.send(job).unwrap();
    }

    // wait till all jobs finish
    let mut nanos = vec![0i64; pool.workers.len()];
    for _ in 0..pool.workers.len() {
        let (i, n) = pool.done.recv().unwrap();
        nanos[i] = n;
    }
    parallel::set_active(false);
    parallel::drop_deferred();
    pool.checked = checked;

    let (mut shortestNanos, mut shortestIndex) = (i64::MAX, -1);
    let (mut longestNanos, mut longestIndex) = (i64::MIN, -1);
    for (i, &n) in nanos.iter().enumerate() {
        if n < shortestNanos {
            shortestNanos = n;
            shortestIndex = i as i32;
        }
        if n > longestNanos {
            longestNanos = n;
            longestIndex = i as i32;
        }
    }
    thread_start_indexes::adjust(shortestIndex, shortestNanos, longestIndex, longestNanos);
}

/// ParallelExecutor.progressLineInBucket(int index): moves the organisms
/// of one line of buckets that no other thread has moved this frame.
fn progressLineInBucket(index: i32, checked: &Checked) {
    let buckets = parallel_executor::organismBuckets();
    let organisms = parallel_executor::organisms();
    let column = |y: i32| buckets.get(|b| b.buckets.at(y)).at(index);
    for y in 0..=OrganismBuckets::getMaxHeight(&buckets) {
        // Nothing changes the buckets while organisms move (OrganismBuckets.java).
        let bucket = column(y);
        for i in 0..bucket.len() {
            let o = bucket.at(i);
            if !checked.add(&o) {
                continue;
            }
            if !Organism::r#move(&o) {
                organisms.remove(&o);
                // Java also clears visibleWorld's selected organism if it was
                // this one: a stub here (its getSelectedOrganism() is null).
            }
        }
    }
}

// ---------------------------------------------------------------- checkedOrganisms

/// checkedOrganisms (a ConcurrentHashMap set in Java): the organisms moved
/// this frame (an organism is in every bucket its bounding box touches). A
/// lock-free open-addressing set of addresses, 2 to 4 times as big as the
/// organisms, so that probe sequences stay short.
struct Checked(Vec<AtomicUsize>);

impl Checked {
    /// Empty, with room for `n` organisms.
    fn reset(&mut self, n: usize) {
        let size = (2 * n.max(16)).next_power_of_two();
        if self.0.len() < size {
            self.0 = (0..size).map(|_| AtomicUsize::new(0)).collect();
        } else {
            self.0.iter_mut().for_each(|k| *k.get_mut() = 0);
        }
    }

    /// `checkedOrganisms.add(o)`: false if it was there.
    fn add(&self, o: &JRef<Organism>) -> bool {
        let key = o.identity().unwrap() as usize;
        let mask = self.0.len() - 1;
        let mut i = ((key as u64).wrapping_mul(0x9E37_79B9_7F4A_7C15) >> 32) as usize & mask;
        loop {
            match self.0[i].load(Ordering::Relaxed) {
                0 => match self.0[i].compare_exchange(0, key, Ordering::Relaxed, Ordering::Relaxed) {
                    Ok(_) => return true,
                    Err(k) if k == key => return false,
                    Err(_) => i = (i + 1) & mask,
                },
                k if k == key => return false,
                _ => i = (i + 1) & mask,
            }
        }
    }
}

// ---------------------------------------------------------------- LinesLocker

/// A binary semaphore (`new Semaphore(1)`).
struct Semaphore {
    taken: Mutex<bool>,
    freed: Condvar,
}

impl Semaphore {
    fn new() -> Self {
        Semaphore { taken: Mutex::new(false), freed: Condvar::new() }
    }

    fn acquire(&self) {
        let mut taken = self.taken.lock().unwrap();
        while *taken {
            taken = self.freed.wait(taken).unwrap();
        }
        *taken = true;
    }

    fn release(&self) {
        *self.taken.lock().unwrap() = false;
        self.freed.notify_one();
    }
}

/// LinesLocker: lock/unlock ignore indexes outside the lines.
struct Lines<'a>(&'a [Semaphore]);

impl Lines<'_> {
    fn lock(&self, index: i32) {
        if index >= 0 && (index as usize) < self.0.len() {
            self.0[index as usize].acquire();
        }
    }
    fn unlock(&self, index: i32) {
        if index >= 0 && (index as usize) < self.0.len() {
            self.0[index as usize].release();
        }
    }
    fn lock_range(&self, from: i32, to: i32) {
        (from..=to).for_each(|i| self.lock(i));
    }
    fn unlock_range(&self, from: i32, to: i32) {
        (from..=to).for_each(|i| self.unlock(i));
    }
}

// ---------------------------------------------------------------- WorkerJob, WorkerThread

struct Job {
    thread: usize,
    startIndex: i32,
    endIndex: i32,
    lines: Arc<Vec<Semaphore>>,
    checked: Arc<Checked>,
}

impl Job {
    /// WorkerJob.run(): the lines from startIndex to endIndex, inclusive.
    /// Returns how long it took, in nanoseconds.
    fn run(&self) -> i64 {
        let start = Instant::now();
        let lines = Lines(&self.lines);
        lines.lock_range(self.startIndex - RANGE_TO_CLEAR, self.startIndex + RANGE_TO_CLEAR);
        let mut index = self.startIndex;
        while index <= self.endIndex {
            progressLineInBucket(index, &self.checked);
            lines.unlock(index - RANGE_TO_CLEAR);
            index += 1;
            lines.lock(index + RANGE_TO_CLEAR);
        }
        lines.unlock_range(index - RANGE_TO_CLEAR, index + RANGE_TO_CLEAR);
        start.elapsed().as_nanos() as i64
    }
}

/// (thread, nanos)
type Done = (usize, i64);

struct Worker {
    jobs: Sender<Job>,
    handle: Option<JoinHandle<()>>,
}

/// The worker threads, made once and reused (as Java's).
struct Pool {
    workers: Vec<Worker>,
    done: Receiver<Done>,
    done_tx: Sender<Done>,
    checked: Arc<Checked>,
}

static POOL: std::sync::LazyLock<Mutex<Pool>> = std::sync::LazyLock::new(|| {
    let (done_tx, done) = channel();
    Mutex::new(Pool { workers: Vec::new(), done, done_tx, checked: Arc::new(Checked(Vec::new())) })
});

impl Pool {
    fn resize(&mut self, n: usize) {
        // stop all worker threads
        for w in &mut self.workers {
            let (closed, _) = channel();
            drop(std::mem::replace(&mut w.jobs, closed));
        }
        for w in self.workers.drain(..) {
            w.handle.map(|h| h.join());
        }
        for i in 0..n {
            let (jobs, rx) = channel::<Job>();
            let done = self.done_tx.clone();
            let handle = std::thread::Builder::new()
                .name(format!("WorkerThread-{i}"))
                .stack_size(STACK_SIZE)
                .spawn(move || {
                    while let Ok(job) = rx.recv() {
                        // A panic is an exception in Java: the simulation is over. The
                        // other threads may wait for this one's lines, so end the process
                        // (the panic message is printed already).
                        let Ok(r) = catch_unwind(AssertUnwindSafe(|| job.run())) else { std::process::exit(101) };
                        parallel::hand_over_deferred();
                        let thread = job.thread;
                        drop(job);
                        if done.send((thread, r)).is_err() {
                            break;
                        }
                    }
                })
                .expect("J2R: can't start a worker thread");
            self.workers.push(Worker { jobs, handle: Some(handle) });
        }
    }
}
