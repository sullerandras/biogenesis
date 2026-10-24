//! Java static fields.
//!
//! A static is one global value, initialized on first use (Java initializes a
//! class's statics on first use of the class; nothing here depends on the
//! difference). The translated simulation runs on one thread, like the Java
//! one with THREAD_COUNT 1, so the value is a plain `UnsafeCell` and a read
//! is a load, not a `thread_local!` lookup.

use std::cell::UnsafeCell;

pub struct JStatic<T> {
    value: UnsafeCell<Option<T>>,
    init: fn() -> T,
}

// SAFETY: statics are only used from the simulation's thread (see the module doc).
unsafe impl<T> Sync for JStatic<T> {}

fn uninitialized<T>() -> T {
    unreachable!()
}

impl<T> JStatic<T> {
    /// A static whose initializer is a constant expression.
    pub const fn new(value: T) -> Self {
        JStatic { value: UnsafeCell::new(Some(value)), init: uninitialized::<T> }
    }

    /// A static initialized by running `init` on first use.
    pub const fn lazy(init: fn() -> T) -> Self {
        JStatic { value: UnsafeCell::new(None), init }
    }

    #[inline]
    fn slot(&self) -> &T {
        // SAFETY: one thread, and no reference into the slot outlives a get/set/with.
        unsafe {
            if (*self.value.get()).is_none() {
                Self::initialize(self);
            }
            (*self.value.get()).as_ref().unwrap_unchecked()
        }
    }

    #[cold]
    #[inline(never)]
    fn initialize(&self) {
        let v = (self.init)();
        // SAFETY: as in `slot`; `init` may have set the static itself, Java's value wins then too.
        unsafe {
            if (*self.value.get()).is_none() {
                *self.value.get() = Some(v);
            }
        }
    }

    /// Reads the value. The borrow ends when `f` returns.
    #[inline]
    pub fn with<R>(&self, f: impl FnOnce(&T) -> R) -> R {
        f(self.slot())
    }

    #[inline]
    pub fn set(&self, v: T) {
        // SAFETY: as in `slot`. The old value is dropped after the slot is updated.
        let old = unsafe { std::mem::replace(&mut *self.value.get(), Some(v)) };
        drop(old);
    }
}

impl<T: Clone> JStatic<T> {
    #[inline]
    pub fn get(&self) -> T {
        self.slot().clone()
    }
}
