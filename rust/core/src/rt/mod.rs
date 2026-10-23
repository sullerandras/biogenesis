//! Java semantics the generated code relies on. Names follow Java (nextInt,
//! getRGB) so the generated calls read like the original.

#![allow(non_snake_case)]

pub mod color;
pub mod dump;
pub mod jmath;
pub mod jref;
pub mod jstr;
pub mod random;

pub use color::Color;
pub use dump::{Dumper, JDump, JDumpTarget};
pub use jref::{JArray, JList, JRef};
pub use jstr::JStr;
pub use random::Random;

/// `throw new X(...)` and runtime exceptions end the simulation: the Java
/// program would print a stack trace, so a panic is the closest equivalent.
#[cold]
#[inline(never)]
pub fn throw(what: &str) -> ! {
    panic!("{what}")
}
