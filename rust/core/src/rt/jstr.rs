//! `java.lang.String`: immutable and nullable.

use std::rc::Rc;

use super::throw;

#[derive(Clone, Default, Debug)]
pub struct JStr(Option<Rc<str>>);

impl JStr {
    pub const NULL: JStr = JStr(None);

    pub fn from(s: &str) -> JStr {
        JStr(Some(Rc::from(s)))
    }

    #[inline]
    pub fn is_null(&self) -> bool {
        self.0.is_none()
    }

    pub fn as_str(&self) -> &str {
        match &self.0 {
            Some(s) => s,
            None => throw("java.lang.NullPointerException (String)"),
        }
    }

    /// `a.equals(b)`
    pub fn equals(a: &JStr, b: &JStr) -> bool {
        let av = a.as_str();
        matches!(&b.0, Some(bv) if **bv == *av)
    }

    pub fn option(&self) -> Option<&str> {
        self.0.as_deref()
    }
}
