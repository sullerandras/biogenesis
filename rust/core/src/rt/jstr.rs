//! `java.lang.String`: immutable and nullable.

use std::rc::Rc;

use super::throw;
use super::JArray;

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

impl JStr {
    pub fn from_string(s: String) -> JStr {
        JStr(Some(Rc::from(s)))
    }

    /// `a + b + ...` on strings: each part converted like `String.valueOf`.
    pub fn cat(parts: &[&dyn JToString]) -> JStr {
        let mut s = String::new();
        for p in parts {
            p.push_java(&mut s);
        }
        JStr::from_string(s)
    }

    /// `s.toCharArray()`: UTF-16 code units, like Java's `char`.
    pub fn toCharArray(&self) -> JArray<u16> {
        JArray::from_vec(self.as_str().encode_utf16().collect())
    }

    /// `s.replace(char, char)`
    pub fn replace_char(&self, from: u16, to: u16) -> JStr {
        let units: Vec<u16> = self.as_str().encode_utf16().map(|c| if c == from { to } else { c }).collect();
        JStr::from_string(from_utf16(&units))
    }
}

/// Java strings may hold lone surrogates, Rust's can't: those end the run.
fn from_utf16(units: &[u16]) -> String {
    match String::from_utf16(units) {
        Ok(s) => s,
        Err(_) => throw("J2R: string with a lone surrogate"),
    }
}

/// `Integer.toHexString(int)`
pub fn to_hex_string(i: i32) -> JStr {
    JStr::from_string(format!("{:x}", i as u32))
}

/// `Integer.toString(int)`, `String.valueOf(int)`
pub fn int_to_string(i: i32) -> JStr {
    JStr::from_string(i.to_string())
}

/// How a value appears in a string concatenation (`String.valueOf`).
/// Floating point is left out on purpose: `Double.toString` has its own
/// format and isn't needed yet.
pub trait JToString {
    fn push_java(&self, out: &mut String);
}

impl JToString for JStr {
    fn push_java(&self, out: &mut String) {
        out.push_str(self.option().unwrap_or("null"));
    }
}

impl JToString for i32 {
    fn push_java(&self, out: &mut String) {
        out.push_str(&self.to_string());
    }
}

impl JToString for i64 {
    fn push_java(&self, out: &mut String) {
        out.push_str(&self.to_string());
    }
}

impl JToString for i16 {
    fn push_java(&self, out: &mut String) {
        out.push_str(&self.to_string());
    }
}

impl JToString for i8 {
    fn push_java(&self, out: &mut String) {
        out.push_str(&self.to_string());
    }
}

/// `char`
impl JToString for u16 {
    fn push_java(&self, out: &mut String) {
        out.push_str(&from_utf16(&[*self]));
    }
}

impl JToString for bool {
    fn push_java(&self, out: &mut String) {
        out.push_str(if *self { "true" } else { "false" });
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    // Expected values are what Java prints for the same expressions.
    #[test]
    fn java_string_conversions() {
        assert_eq!(to_hex_string(-1).as_str(), "ffffffff");
        assert_eq!(to_hex_string(0).as_str(), "0");
        assert_eq!(to_hex_string(0x7fff_ffff).as_str(), "7fffffff");
        assert_eq!(int_to_string(i32::MIN).as_str(), "-2147483648");
        let s = JStr::cat(&[&JStr::from("a"), &0xb0u16, &-5i32, &JStr::NULL, &true, &7i64]);
        assert_eq!(s.as_str(), "a°-5nulltrue7");
        assert_eq!(s.toCharArray().len(), 13);
        assert_eq!(s.toCharArray().at(1), 0xb0);
        assert_eq!(JStr::from("1213").replace_char('1' as u16, 'X' as u16).as_str(), "X2X3");
    }
}
