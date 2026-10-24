//! Rust port of the Biogenesis simulation core.
//!
//! `rt` is the hand-written Java runtime (references, arrays, java.util.Random,
//! java.lang.Math, java.awt.Color). `generated` is generated from the Java sources by
//! `rust/transpiler` and must not be edited by hand.

pub mod rt;
pub mod generated;
/// Hand-written bodies of the methods J2R's config lists as `native`.
pub mod native;
/// Saved games.
pub mod save;
/// Translator tests: J2R output for rust/verify/j2rtest.
pub mod samples;
