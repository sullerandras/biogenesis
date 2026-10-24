//! Lambdas: a value of a functional interface type (one abstract method,
//! like `OrganismBuckets.OrganismMatcher`) is a nullable shared closure,
//! `JFn<dyn Fn(JRef<Organism>) -> bool>`. Captured variables are copied into
//! it, as Java captures (effectively final) values.

use std::rc::Rc;

use super::throw;

pub struct JFn<F: ?Sized>(Option<Rc<F>>);

impl<F: ?Sized> JFn<F> {
    pub fn new(f: Rc<F>) -> Self {
        JFn(Some(f))
    }

    pub const fn null() -> Self {
        JFn(None)
    }

    pub fn is_null(&self) -> bool {
        self.0.is_none()
    }

    /// The closure, to call it.
    #[inline]
    pub fn f(&self) -> &F {
        match &self.0 {
            Some(f) => f,
            None => throw("java.lang.NullPointerException"),
        }
    }
}

impl<F: ?Sized> Clone for JFn<F> {
    fn clone(&self) -> Self {
        JFn(self.0.clone())
    }
}

impl<F: ?Sized> Default for JFn<F> {
    fn default() -> Self {
        JFn(None)
    }
}
