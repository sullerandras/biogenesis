//! Rust port of the Biogenesis simulation core.
//!
//! `rt` is the hand-written Java runtime (references, arrays, java.util.Random,
//! java.lang.Math, java.awt.Color). `generated` is generated from the Java sources by
//! `rust/transpiler` and must not be edited by hand.

pub mod rt;
pub mod generated;
