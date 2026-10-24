//! Java object references.
//!
//! A Java reference is shared, mutable and nullable. `JRef<T>` models it as an
//! optional `Rc<RefCell<T>>`. The generated code never keeps a borrow across a
//! call: it reads a field with `obj.get(|o| o.f)` and writes it with
//! `obj.set(|o| o.f = v)` after `v` is computed, so a method may freely read
//! and write the object it was called on, like in Java.

use std::cell::RefCell;
use std::rc::Rc;

use super::throw;

/// `T` may be a trait object (`JRef<dyn RectangleT>`), for variables of a
/// class type that subclasses are assigned to.
pub struct JRef<T: ?Sized>(Option<Rc<RefCell<T>>>);

/// A Java array: a reference to a fixed-length vector.
pub type JArray<T> = JRef<Vec<T>>;

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
        JRef(Some(Rc::new(RefCell::new(value))))
    }

    /// The same object (or null) as another type: used for upcasts to trait
    /// objects, `self.map_rc(|rc| rc as Rc<RefCell<dyn Trait>>)`.
    #[inline]
    pub fn map_rc<U: ?Sized>(self, f: impl FnOnce(Rc<RefCell<T>>) -> Rc<RefCell<U>>) -> JRef<U> {
        JRef(self.0.map(f))
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
    fn rc(&self) -> &Rc<RefCell<T>> {
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
            (Some(x), Some(y)) => std::ptr::addr_eq(Rc::as_ptr(x), Rc::as_ptr(y)),
            (None, None) => true,
            _ => false,
        }
    }

    /// Identity of the referenced object (for dumps), or None for null.
    pub fn identity(&self) -> Option<*const ()> {
        self.0.as_ref().map(|rc| Rc::as_ptr(rc) as *const ())
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
    pub fn setf<F>(&self, field: impl FnOnce(&mut T) -> &mut F, v: F) {
        *field(&mut self.rc().borrow_mut()) = v;
    }
}

impl<T: Clone> JRef<Vec<T>> {
    /// `new T[len]`, every element set to `fill` (Java's default value).
    pub fn new_array(len: i32, fill: T) -> Self {
        if len < 0 {
            throw("java.lang.NegativeArraySizeException");
        }
        JRef::new(vec![fill; len as usize])
    }

    pub fn from_vec(v: Vec<T>) -> Self {
        JRef::new(v)
    }

    /// `a.length` for arrays, `a.size()` for lists.
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
            JRef::new(items)
        }
    }
}
