//! Java semantics the generated code relies on. Names follow Java (nextInt,
//! getRGB) so the generated calls read like the original.

#![allow(non_snake_case)]

pub mod collections;
pub mod color;
pub mod dump;
pub mod geom;
pub mod hashset;
pub mod jfn;
pub mod jmath;
pub mod jref;
pub mod jstr;
pub mod load;
pub mod random;
pub mod stub;

pub use collections::{JCollection, JCollectionT, JIter, JObject};
pub use color::Color;
pub use dump::{Dumper, JDump, JDumpTarget};
pub use geom::{Line2D, Line2DT, Point, Point2D, Rectangle, RectangleT};
pub use hashset::JHashSet;
pub use jfn::JFn;
pub use jref::{JArray, JList, JRef};
pub use jstr::{JStr, JToString};
pub use load::{JLoad, Loader};
pub use random::Random;
pub use stub::Stub;

/// `throw new X(...)` and runtime exceptions end the simulation: the Java
/// program would print a stack trace, so a panic is the closest equivalent.
#[cold]
#[inline(never)]
pub fn throw(what: &str) -> ! {
    panic!("{what}")
}
