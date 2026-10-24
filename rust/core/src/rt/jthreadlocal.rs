//! `java.lang.ThreadLocal`, for `static final ThreadLocal<T> x = new
//! ThreadLocal<>()` (J2R translates only that form, and `x.get()`,
//! `x.set(v)`).
//!
//! Every thread has a vector of slots; a JThreadLocal is a slot number,
//! taken on first use. A thread that never set the value reads the default
//! (null for references), as Java's `initialValue()` gives.

use std::any::Any;
use std::cell::RefCell;
use std::marker::PhantomData;
use std::sync::atomic::{AtomicUsize, Ordering};

pub struct JThreadLocal<T> {
    /// The slot number + 1; 0 until first used.
    slot: AtomicUsize,
    _t: PhantomData<fn() -> T>,
}

static NEXT_SLOT: AtomicUsize = AtomicUsize::new(0);

thread_local! {
    static SLOTS: RefCell<Vec<Option<Box<dyn Any>>>> = const { RefCell::new(Vec::new()) };
}

impl<T: Clone + Default + 'static> JThreadLocal<T> {
    pub const fn new() -> Self {
        JThreadLocal { slot: AtomicUsize::new(0), _t: PhantomData }
    }

    #[inline]
    fn slot(&self) -> usize {
        match self.slot.load(Ordering::Relaxed) {
            0 => self.take_slot(),
            s => s - 1,
        }
    }

    #[cold]
    fn take_slot(&self) -> usize {
        let s = NEXT_SLOT.fetch_add(1, Ordering::Relaxed) + 1;
        match self.slot.compare_exchange(0, s, Ordering::Relaxed, Ordering::Relaxed) {
            Ok(_) => s - 1,
            Err(other) => other - 1,
        }
    }

    /// `x.get()`
    #[inline]
    pub fn get(&self) -> T {
        let slot = self.slot();
        SLOTS.with(|s| match s.borrow().get(slot) {
            Some(Some(v)) => v.downcast_ref::<T>().expect("JThreadLocal: slot type").clone(),
            _ => T::default(),
        })
    }

    /// `x.set(v)`
    pub fn set(&self, v: T) {
        let slot = self.slot();
        // The old value is dropped after the borrow ends.
        let _old = SLOTS.with(|s| {
            let mut s = s.borrow_mut();
            if s.len() <= slot {
                s.resize_with(slot + 1, || None);
            }
            s[slot].replace(Box::new(v))
        });
    }
}

// SAFETY: the value lives in each thread's own slots; the struct itself is
// only an atomic slot number.
unsafe impl<T> Sync for JThreadLocal<T> {}
