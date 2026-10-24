//! Java object references.
//!
//! A Java reference is shared, mutable and nullable. `JRef<T>` models it as an
//! optional `Arc<JCell<T>>`. The generated code never keeps a borrow across a
//! call: it reads a field with `obj.get(|o| o.f)` and writes it with
//! `obj.set(|o| o.f = v)` after `v` is computed, so a method may freely read
//! and write the object it was called on, like in Java.
//!
//! Objects are shared between threads as in Java (`Arc`, and `JCell` is
//! `Send + Sync`): with THREAD_COUNT > 1 organisms move on several threads,
//! under the rules of `rt/parallel.rs`.

use std::sync::Arc;

use super::throw;

/// The cell behind a `JRef`: an `UnsafeCell`, so a field access is a plain
/// load or store. That is sound because the generated code never holds a
/// borrow across a call (see above). The `checked` feature checks it like a
/// `RefCell` would (`verify.sh` runs a world with it), keeping the borrow
/// counts in a side table so that the layout stays the same: an array is
/// an `Arc<JCell<[T]>>` made from an `Arc<[T]>`.
#[repr(transparent)]
pub struct JCell<T: ?Sized>(std::cell::UnsafeCell<T>);

// SAFETY: Java's sharing model; see rt/parallel.rs for what keeps the
// parallel phase memory-safe (deferred drops, monitors, the line locks).
unsafe impl<T: ?Sized> Send for JCell<T> {}
unsafe impl<T: ?Sized> Sync for JCell<T> {}

impl<T> JCell<T> {
    #[inline]
    pub fn new(value: T) -> Self {
        JCell(std::cell::UnsafeCell::new(value))
    }

    /// The value, when nothing else can reach the cell.
    #[inline]
    pub fn get_mut(&mut self) -> &mut T {
        self.0.get_mut()
    }
}

#[cfg(not(feature = "checked"))]
impl<T: ?Sized> JCell<T> {
    #[inline]
    pub fn borrow(&self) -> &T {
        // SAFETY: borrows end before any other access to the object (see the module doc).
        unsafe { &*self.0.get() }
    }

    #[inline]
    #[allow(clippy::mut_from_ref)]
    pub fn borrow_mut(&self) -> &mut T {
        // SAFETY: as for `borrow`.
        unsafe { &mut *self.0.get() }
    }
}

#[cfg(feature = "checked")]
impl<T: ?Sized> JCell<T> {
    fn key(&self) -> usize {
        self as *const Self as *const () as usize
    }

    pub fn borrow(&self) -> checked::Ref<'_, T> {
        checked::acquire(self.key(), false);
        // SAFETY: no mutable borrow exists (checked above).
        checked::Ref(unsafe { &*self.0.get() }, self.key())
    }

    pub fn borrow_mut(&self) -> checked::RefMut<'_, T> {
        checked::acquire(self.key(), true);
        // SAFETY: no other borrow exists (checked above).
        checked::RefMut(unsafe { &mut *self.0.get() }, self.key())
    }
}

#[cfg(feature = "checked")]
pub mod checked {
    use std::cell::RefCell;
    use std::collections::HashMap;

    thread_local! {
        /// Borrows by cell address: n > 0 shared ones, -1 a mutable one.
        static BORROWS: RefCell<HashMap<usize, isize>> = RefCell::new(HashMap::new());
    }

    pub(super) fn acquire(key: usize, exclusive: bool) {
        BORROWS.with(|b| {
            let mut b = b.borrow_mut();
            let n = b.entry(key).or_insert(0);
            if *n < 0 || (exclusive && *n > 0) {
                panic!("J2R: object already borrowed (a borrow outlived a field access)");
            }
            *n = if exclusive { -1 } else { *n + 1 };
        })
    }

    fn release(key: usize) {
        BORROWS.with(|b| {
            let mut b = b.borrow_mut();
            let n = b.get_mut(&key).unwrap();
            if *n > 1 {
                *n -= 1;
            } else {
                b.remove(&key);
            }
        })
    }

    pub struct Ref<'a, T: ?Sized>(pub(super) &'a T, pub(super) usize);
    pub struct RefMut<'a, T: ?Sized>(pub(super) &'a mut T, pub(super) usize);

    impl<T: ?Sized> std::ops::Deref for Ref<'_, T> {
        type Target = T;
        fn deref(&self) -> &T {
            self.0
        }
    }
    impl<T: ?Sized> std::ops::Deref for RefMut<'_, T> {
        type Target = T;
        fn deref(&self) -> &T {
            self.0
        }
    }
    impl<T: ?Sized> std::ops::DerefMut for RefMut<'_, T> {
        fn deref_mut(&mut self) -> &mut T {
            self.0
        }
    }
    impl<T: ?Sized> Drop for Ref<'_, T> {
        fn drop(&mut self) {
            release(self.1)
        }
    }
    impl<T: ?Sized> Drop for RefMut<'_, T> {
        fn drop(&mut self) {
            release(self.1)
        }
    }
}

/// `T` may be a trait object (`JRef<dyn RectangleT>`), for variables of a
/// class type that subclasses are assigned to.
pub struct JRef<T: ?Sized>(Option<Arc<JCell<T>>>);

/// The object of a field that is never reassigned after the constructor
/// (J2R checks that), borrowed for a call without updating its count: a
/// copy of the reference that is never dropped. Shared counts are what
/// threads contend on (every organism calls its `_world`).
pub struct JAlias<T: ?Sized>(std::mem::ManuallyDrop<JRef<T>>);

impl<T: ?Sized> std::ops::Deref for JAlias<T> {
    type Target = JRef<T>;
    #[inline]
    fn deref(&self) -> &JRef<T> {
        &self.0
    }
}

impl<T: ?Sized> JRef<T> {
    /// # Safety
    /// The alias must not outlive the object holding this reference, and
    /// the reference must not be overwritten meanwhile: J2R uses it for a
    /// call's receiver `this.f`, f never reassigned after the constructor.
    #[inline]
    pub unsafe fn alias(&self) -> JAlias<T> {
        // SAFETY: a bitwise copy that is never dropped, so the count stays right.
        JAlias(std::mem::ManuallyDrop::new(unsafe { std::ptr::read(self) }))
    }
}

/// A field's object for a call's receiver: borrowed (no count update)
/// while organisms move on several threads, a clone otherwise. Then a
/// field that is reassigned during the call keeps its old object alive
/// anyway: overwritten references are only dropped when the threads are
/// done (rt/parallel.rs). The world's `organismBuckets` and
/// `worldStatistics`, which all organisms use, are such fields.
pub struct JShared<T: ?Sized> {
    r: std::mem::ManuallyDrop<JRef<T>>,
    owned: bool,
}

impl<T: ?Sized> std::ops::Deref for JShared<T> {
    type Target = JRef<T>;
    #[inline]
    fn deref(&self) -> &JRef<T> {
        &self.r
    }
}

impl<T: ?Sized> Drop for JShared<T> {
    #[inline]
    fn drop(&mut self) {
        if self.owned {
            // SAFETY: a clone, dropped once.
            unsafe { std::mem::ManuallyDrop::drop(&mut self.r) }
        }
    }
}

impl<T: ?Sized> JRef<T> {
    /// # Safety
    /// For a call's receiver `this.f`: `this` lives through the call, and
    /// the field is only written with `setf` meanwhile.
    #[inline]
    pub unsafe fn share(&self) -> JShared<T> {
        if super::parallel::active() {
            // SAFETY: see JShared: an overwritten value is deferred, not dropped.
            JShared { r: std::mem::ManuallyDrop::new(unsafe { std::ptr::read(self) }), owned: false }
        } else {
            JShared { r: std::mem::ManuallyDrop::new(self.clone()), owned: true }
        }
    }
}

/// A Java array: one allocation holding the elements, like Java's.
pub type JArray<T> = JRef<[T]>;

/// A `java.util.List` (always an `ArrayList` in the code we translate).
pub type JList<T> = JRef<Vec<T>>;

impl<T: ?Sized> Clone for JRef<T> {
    #[inline]
    fn clone(&self) -> Self {
        JRef(self.0.clone())
    }
}

impl<T: ?Sized> Default for JRef<T> {
    #[inline]
    fn default() -> Self {
        JRef(None)
    }
}

impl<T> JRef<T> {
    #[inline]
    pub fn new(value: T) -> Self {
        JRef(Some(Arc::new(JCell::new(value))))
    }

    /// The same object (or null) as another type: used for upcasts to trait
    /// objects, `self.map_rc(|rc| rc as Arc<JCell<dyn Trait>>)`.
    #[inline]
    pub fn map_rc<U: ?Sized>(self, f: impl FnOnce(Arc<JCell<T>>) -> Arc<JCell<U>>) -> JRef<U> {
        JRef(self.0.map(f))
    }
}

/// Recent objects of one class, for `new` to reuse once nothing else refers
/// to them: a slot holding the only reference means nobody can tell the
/// reused object from a new one, if it is reset to what a new one is
/// (`T::default()`, then the constructor runs). Java can't do this: it can't
/// know that an object is garbage until it collects. `contact` makes two
/// `ExLine2DDouble`s each call; with this they're the same two every time.
/// Each thread has its own (J2R declares them `thread_local!`).
pub struct JRecycler<T, const N: usize>(std::cell::UnsafeCell<([Option<Arc<JCell<T>>>; N], usize)>);

impl<T: Default, const N: usize> JRecycler<T, N> {
    pub const fn new() -> Self {
        JRecycler(std::cell::UnsafeCell::new(([const { None }; N], 0)))
    }

    /// `JRef::new(T::default())`, but reusing an object in a slot if
    /// nothing else refers to it. A new object takes a slot (in turn, when
    /// all are in use: the newest objects are the likeliest to be dropped).
    #[inline]
    pub fn new_object(&self) -> JRef<T> {
        // SAFETY: thread-local, and the borrow ends before anything else runs
        // (resetting drops the old fields, which only drops Rcs).
        let (slots, next) = unsafe { &mut *self.0.get() };
        for s in slots.iter_mut() {
            if let Some(rc) = s {
                if let Some(cell) = Arc::get_mut(rc) {
                    *cell.get_mut() = T::default();
                    return JRef(Some(rc.clone()));
                }
            }
        }
        let rc = Arc::new(JCell::new(T::default()));
        slots[*next] = Some(rc.clone());
        *next = (*next + 1) % N;
        JRef(Some(rc))
    }
}

impl<T: ?Sized> JRef<T> {
    #[inline]
    pub const fn null() -> Self {
        JRef(None)
    }

    #[inline]
    pub fn is_null(&self) -> bool {
        self.0.is_none()
    }

    #[inline]
    fn rc(&self) -> &Arc<JCell<T>> {
        match &self.0 {
            Some(rc) => rc,
            None => throw("java.lang.NullPointerException"),
        }
    }

    /// Java `==` on references (the static types may differ: a subclass
    /// object and a base class variable).
    #[inline]
    pub fn ptr_eq<U: ?Sized>(a: &Self, b: &JRef<U>) -> bool {
        match (&a.0, &b.0) {
            (Some(x), Some(y)) => std::ptr::addr_eq(Arc::as_ptr(x), Arc::as_ptr(y)),
            (None, None) => true,
            _ => false,
        }
    }

    /// Identity of the referenced object (for dumps), or None for null.
    pub fn identity(&self) -> Option<*const ()> {
        self.0.as_ref().map(|rc| Arc::as_ptr(rc) as *const ())
    }

    /// Reads from the object. The borrow ends when `f` returns.
    #[inline]
    pub fn get<R>(&self, f: impl FnOnce(&T) -> R) -> R {
        f(&self.rc().borrow())
    }

    /// Writes to the object. The borrow ends when `f` returns.
    #[inline]
    pub fn set<R>(&self, f: impl FnOnce(&mut T) -> R) -> R {
        f(&mut self.rc().borrow_mut())
    }

    /// `obj.f = v`, written `obj.setf(|o| &mut o.f, v)`: `v` is evaluated
    /// before the object is borrowed, and after `obj` (Java's order).
    #[inline]
    pub fn setf<F: 'static>(&self, field: impl FnOnce(&mut T) -> &mut F, v: F) {
        let mut o = self.rc().borrow_mut();
        let slot = field(&mut o);
        if std::mem::needs_drop::<F>() && super::parallel::active() {
            // SAFETY: a field of a live object.
            unsafe { super::parallel::store_shared(slot, v) }
        } else {
            *slot = v;
        }
    }
}

impl<T: Clone> JRef<[T]> {
    /// `new T[len]`, every element set to `fill` (Java's default value).
    pub fn new_array(len: i32, fill: T) -> Self {
        if len < 0 {
            throw("java.lang.NegativeArraySizeException");
        }
        Self::from_rc(std::iter::repeat_n(fill, len as usize).collect())
    }

    pub fn from_vec(v: Vec<T>) -> Self {
        Self::from_rc(Arc::from(v))
    }

    fn from_rc(rc: Arc<[T]>) -> Self {
        // SAFETY: JCell<[T]> is a transparent UnsafeCell<[T]>, which has the layout of [T].
        JRef(Some(unsafe { Arc::from_raw(Arc::into_raw(rc) as *const JCell<[T]>) }))
    }

    /// `a.length`
    #[inline]
    pub fn len(&self) -> i32 {
        self.get(|v| v.len() as i32)
    }

    /// `a[i]`
    #[inline]
    pub fn at(&self, i: i32) -> T {
        self.get(|v| match v.get(i as usize) {
            Some(x) => x.clone(),
            None => throw("java.lang.ArrayIndexOutOfBoundsException"),
        })
    }

    /// `a[i] = x`
    #[inline]
    pub fn put(&self, i: i32, x: T)
    where
        T: 'static,
    {
        self.set(|v| match v.get_mut(i as usize) {
            // SAFETY: an element of a live array.
            Some(slot) if std::mem::needs_drop::<T>() && super::parallel::active() => unsafe {
                super::parallel::store_shared(slot, x)
            },
            Some(slot) => *slot = x,
            None => throw("java.lang.ArrayIndexOutOfBoundsException"),
        })
    }
}

impl<T: Clone> JRef<Vec<T>> {
    /// `new ArrayList<>()` and the like.
    pub fn from_vec(v: Vec<T>) -> Self {
        JRef::new(v)
    }

    /// `list.size()`
    #[inline]
    pub fn len(&self) -> i32 {
        self.get(|v| v.len() as i32)
    }

    /// `list.get(i)`
    #[inline]
    pub fn at(&self, i: i32) -> T {
        self.get(|v| match v.get(i as usize) {
            Some(x) => x.clone(),
            None => throw("java.lang.ArrayIndexOutOfBoundsException"),
        })
    }

    /// `list.set(i, x)`
    #[inline]
    pub fn put(&self, i: i32, x: T) {
        self.set(|v| match v.get_mut(i as usize) {
            Some(slot) => *slot = x,
            None => throw("java.lang.ArrayIndexOutOfBoundsException"),
        })
    }

    /// `list.add(x)`
    pub fn add(&self, x: T) -> bool {
        self.set(|v| v.push(x));
        true
    }

    /// `list.toArray(a)`: copies into `a` when it is big enough (setting the
    /// element after the last one to null, as Java does), else a new array.
    pub fn to_array(&self, a: &JArray<T>, null: T) -> JArray<T> {
        let items = self.get(|v| v.clone());
        if a.len() as usize >= items.len() {
            let n = items.len();
            a.set(|dst| {
                dst[..n].clone_from_slice(&items);
                if dst.len() > n {
                    dst[n] = null;
                }
            });
            a.clone()
        } else {
            JArray::from_vec(items)
        }
    }
}

/// What a method can do with an array parameter that doesn't escape it (J2R
/// checks that it is only indexed, measured and passed on to such
/// parameters): implemented by `JArray` and by `JStackArray`.
pub trait JArr<T: Copy> {
    fn at(&self, i: i32) -> T;
    fn put(&self, i: i32, x: T);
    fn len(&self) -> i32;
}

impl<T: Copy + 'static> JArr<T> for JRef<[T]> {
    #[inline]
    fn at(&self, i: i32) -> T {
        JRef::<[T]>::at(self, i)
    }
    #[inline]
    fn put(&self, i: i32, x: T) {
        JRef::<[T]>::put(self, i, x)
    }
    #[inline]
    fn len(&self) -> i32 {
        JRef::<[T]>::len(self)
    }
}

/// A local `new double[2]` that never escapes the method (or the
/// non-escaping parameters it is passed to): on the stack, as HotSpot's
/// escape analysis would put it.
pub struct JStackArray<T: Copy, const N: usize>([std::cell::Cell<T>; N]);

impl<T: Copy, const N: usize> JStackArray<T, N> {
    /// `new T[N]`, every element `fill`.
    #[inline]
    pub fn new(fill: T) -> Self {
        JStackArray(std::array::from_fn(|_| std::cell::Cell::new(fill)))
    }

    /// `{a, b, ...}`
    #[inline]
    pub fn from(values: [T; N]) -> Self {
        JStackArray(values.map(std::cell::Cell::new))
    }

    #[inline]
    pub fn at(&self, i: i32) -> T {
        match self.0.get(i as usize) {
            Some(x) => x.get(),
            None => throw("java.lang.ArrayIndexOutOfBoundsException"),
        }
    }

    #[inline]
    pub fn put(&self, i: i32, x: T) {
        match self.0.get(i as usize) {
            Some(slot) => slot.set(x),
            None => throw("java.lang.ArrayIndexOutOfBoundsException"),
        }
    }

    #[inline]
    pub fn len(&self) -> i32 {
        N as i32
    }
}

impl<T: Copy, const N: usize> JArr<T> for JStackArray<T, N> {
    #[inline]
    fn at(&self, i: i32) -> T {
        JStackArray::at(self, i)
    }
    #[inline]
    fn put(&self, i: i32, x: T) {
        JStackArray::put(self, i, x)
    }
    #[inline]
    fn len(&self) -> i32 {
        N as i32
    }
}
