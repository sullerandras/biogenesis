//! `java.util.Collection` and `Iterator`.
//!
//! A variable of type `Collection<T>` (or `Set<T>`) is a `JCollection<T>`:
//! a reference to any collection, here an `ArrayList` (`Vec<T>`) or a
//! `HashSet` (`JHashSet<T>`, iterating in Java's order). Iterators and for-each
//! loops walk a snapshot taken when they start; Java would throw
//! ConcurrentModificationException if the collection changed meanwhile, which
//! the translated code doesn't do.

use std::sync::Arc;

use super::color::Color;
use super::dump::{JDump, JDumpTarget};
use super::hashset::JHashSet;
use super::jref::{JArray, JList, JRef};
use super::jstr::JStr;
use super::throw;

/// What collections need of their elements: Java's `hashCode`, `equals` and
/// `==`. J2R implements it for the translated classes (their own `hashCode`
/// and `equals`, else identity).
pub trait JObject: Clone {
    fn hash_code(&self) -> i32;
    fn equals(&self, other: &Self) -> bool;
    /// Java `==`
    fn same(&self, other: &Self) -> bool;
    fn is_null(&self) -> bool {
        false
    }
    /// `compareTo`, for keys that are `Comparable` (HashMap's tree bins use it).
    fn compare_to(&self, _other: &Self) -> Option<i32> {
        None
    }
}

/// `System.identityHashCode`: differs from run to run in Java.
pub fn identity_hash_code() -> i32 {
    throw("J2R: identity hash code (Object.hashCode not overridden) is not reproducible")
}

impl JObject for f64 {
    fn hash_code(&self) -> i32 {
        let bits = if self.is_nan() { 0x7ff8000000000000u64 } else { self.to_bits() };
        (bits ^ (bits >> 32)) as i32
    }
    fn equals(&self, other: &Self) -> bool {
        // Double.equals: doubleToLongBits
        (self.is_nan() && other.is_nan()) || self.to_bits() == other.to_bits()
    }
    fn same(&self, other: &Self) -> bool {
        // == on two Double boxes is identity in Java: not reproducible.
        let _ = other;
        throw("J2R: == on boxed Double")
    }
    fn compare_to(&self, other: &Self) -> Option<i32> {
        Some(if self < other {
            -1
        } else if self > other {
            1
        } else {
            let (a, b) = (if self.is_nan() { 0x7ff8000000000000u64 as i64 } else { self.to_bits() as i64 }, if other.is_nan() { 0x7ff8000000000000u64 as i64 } else { other.to_bits() as i64 });
            a.cmp(&b) as i32
        })
    }
}

impl JObject for i32 {
    fn hash_code(&self) -> i32 {
        *self
    }
    fn equals(&self, other: &Self) -> bool {
        self == other
    }
    fn same(&self, other: &Self) -> bool {
        // Integer.valueOf caches -128..127; == on other boxes is identity.
        if (-128..=127).contains(self) && (-128..=127).contains(other) {
            self == other
        } else {
            throw("J2R: == on boxed Integer")
        }
    }
    fn compare_to(&self, other: &Self) -> Option<i32> {
        Some(self.cmp(other) as i32)
    }
}

impl JObject for JStr {
    fn hash_code(&self) -> i32 {
        match self.option() {
            None => throw("java.lang.NullPointerException"),
            Some(s) => s.encode_utf16().fold(0i32, |h, c| h.wrapping_mul(31).wrapping_add(c as i32)),
        }
    }
    fn equals(&self, other: &Self) -> bool {
        JStr::equals(self, other)
    }
    fn same(&self, other: &Self) -> bool {
        match (self.option(), other.option()) {
            (None, None) => true,
            (Some(a), Some(b)) => std::ptr::eq(a.as_ptr(), b.as_ptr()),
            _ => false,
        }
    }
    fn is_null(&self) -> bool {
        self.option().is_none()
    }
    fn compare_to(&self, other: &Self) -> Option<i32> {
        let (a, b) = (self.option()?, other.option()?);
        let (mut x, mut y) = (a.encode_utf16(), b.encode_utf16());
        let (la, lb) = (a.encode_utf16().count() as i32, b.encode_utf16().count() as i32);
        loop {
            match (x.next(), y.next()) {
                (Some(c), Some(d)) if c != d => return Some(c as i32 - d as i32),
                (Some(_), Some(_)) => {}
                _ => return Some(la - lb),
            }
        }
    }
}

impl JObject for Color {
    fn hash_code(&self) -> i32 {
        self.getRGB()
    }
    fn equals(&self, other: &Self) -> bool {
        Color::equals(*self, *other)
    }
    fn same(&self, _other: &Self) -> bool {
        throw("J2R: == on Color")
    }
    fn is_null(&self) -> bool {
        Color::is_null(*self)
    }
}

/// A `java.util.Collection` (or `Set`) of `T`.
pub trait JCollectionT<T>: JDumpTarget {
    fn size(&self) -> i32;
    fn add(&mut self, x: T) -> bool;
    fn remove(&mut self, x: &T) -> bool;
    fn contains(&self, x: &T) -> bool;
    fn clear(&mut self);
    /// The elements in iteration order.
    fn snapshot(&self) -> Vec<T>;
}

pub type JCollection<T> = JRef<dyn JCollectionT<T>>;

impl<T: JObject + JDump + 'static> JCollectionT<T> for Vec<T> {
    fn size(&self) -> i32 {
        self.len() as i32
    }
    fn add(&mut self, x: T) -> bool {
        self.push(x);
        true
    }
    fn remove(&mut self, x: &T) -> bool {
        match self.iter().position(|e| obj_equals(x, e)) {
            Some(i) => {
                Vec::remove(self, i);
                true
            }
            None => false,
        }
    }
    fn contains(&self, x: &T) -> bool {
        self.iter().any(|e| obj_equals(x, e))
    }
    fn clear(&mut self) {
        Vec::clear(self)
    }
    fn snapshot(&self) -> Vec<T> {
        self.clone()
    }
}

/// `Objects.equals(a, b)`, as ArrayList.indexOf compares.
fn obj_equals<T: JObject>(a: &T, b: &T) -> bool {
    if a.is_null() { b.is_null() } else { a.equals(b) }
}

impl<T: JObject + JDump + 'static> JRef<Vec<T>> {
    /// Use as a `Collection` (Java's implicit upcast).
    pub fn as_collection(self) -> JCollection<T> {
        self.map_rc(|rc| rc as Arc<super::jref::JCell<dyn JCollectionT<T>>>)
    }

    /// `list.remove(Object)`
    pub fn remove_obj(&self, x: &T) -> bool {
        self.set(|v| JCollectionT::remove(v, x))
    }

    /// `list.contains(Object)`
    pub fn contains_obj(&self, x: &T) -> bool {
        self.get(|v| JCollectionT::contains(v, x))
    }
}

impl<T: Clone> JRef<Vec<T>> {
    /// `list.remove(int)`
    pub fn remove_at(&self, i: i32) -> T {
        self.set(|v| {
            if i < 0 || i as usize >= v.len() {
                throw("java.lang.IndexOutOfBoundsException");
            }
            v.remove(i as usize)
        })
    }

    /// `list.clear()`
    pub fn clear(&self) {
        self.set(|v| v.clear())
    }
}

impl<T: JObject + JDump + 'static> JRef<JHashSet<T>> {
    /// `new HashSet<>()`
    pub fn new_set() -> Self {
        JRef::new(JHashSet::new())
    }

    /// Use as a `Collection`/`Set` (Java's implicit upcast).
    pub fn as_collection(self) -> JCollection<T> {
        self.map_rc(|rc| rc as Arc<super::jref::JCell<dyn JCollectionT<T>>>)
    }
}

/// Each operation holds the collection's monitor while organisms move on
/// several threads, if code synchronizes on the collection (`World._organisms`
/// is a `Collections.synchronizedSet` in Java), see rt/parallel.rs.
impl<T: Clone + 'static> JRef<dyn JCollectionT<T>> {
    #[inline]
    fn sync(&self) -> super::parallel::JMonitorGuard<'static> {
        super::parallel::collection_guard(self.identity())
    }
    pub fn size(&self) -> i32 {
        let _g = self.sync();
        self.get(|c| c.size())
    }
    pub fn isEmpty(&self) -> bool {
        self.size() == 0
    }
    pub fn add(&self, x: T) -> bool {
        let _g = self.sync();
        self.set(|c| c.add(x))
    }
    pub fn remove(&self, x: &T) -> bool {
        let _g = self.sync();
        self.set(|c| c.remove(x))
    }
    pub fn contains(&self, x: &T) -> bool {
        let _g = self.sync();
        self.get(|c| c.contains(x))
    }
    pub fn clear(&self) {
        let _g = self.sync();
        self.set(|c| c.clear())
    }
    /// The elements, for a for-each loop.
    pub fn snapshot_array(&self) -> JArray<T> {
        let _g = self.sync();
        JArray::from_vec(self.get(|c| c.snapshot()))
    }
    /// `toArray(a)`: as `List.toArray`.
    pub fn to_array(&self, a: &JArray<T>, null: T) -> JArray<T> {
        let items: JList<T> = {
            let _g = self.sync();
            JRef::new(self.get(|c| c.snapshot()))
        };
        items.to_array(a, null)
    }
    /// `iterator()`
    pub fn iterator(&self) -> JIter<T> {
        let _g = self.sync();
        JRef::new(IterState { items: self.get(|c| c.snapshot()), pos: 0, src: self.clone(), last: None })
    }
}

pub struct IterState<T: 'static> {
    items: Vec<T>,
    pos: usize,
    src: JCollection<T>,
    last: Option<T>,
}

/// A `java.util.Iterator`.
pub type JIter<T> = JRef<IterState<T>>;

impl<T: Clone + 'static> JRef<IterState<T>> {
    pub fn hasNext(&self) -> bool {
        self.get(|s| s.pos < s.items.len())
    }

    pub fn next(&self) -> T {
        self.set(|s| {
            let Some(x) = s.items.get(s.pos).cloned() else { throw("java.util.NoSuchElementException") };
            s.pos += 1;
            s.last = Some(x.clone());
            x
        })
    }

    pub fn remove(&self) {
        let (src, last) = self.set(|s| (s.src.clone(), s.last.take()));
        match last {
            Some(x) => {
                src.remove(&x);
            }
            None => throw("java.lang.IllegalStateException"),
        }
    }
}
