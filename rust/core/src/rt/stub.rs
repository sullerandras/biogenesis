//! GUI and networking types the headless simulation doesn't have
//! (`VisibleWorldInterface`, `InfoToolbar`, corridors...), listed as `stub` in
//! J2R's config. A stub value only records whether it is null. Calls on it do
//! nothing (as the stubs in benchmark/Benchmark.java), return null for
//! objects and a non-null stub for stub types; a call that would have to
//! return a number or boolean throws, so reaching one is noticed.

use super::dump::{Dumper, JDump};

#[derive(Clone, Copy, Default, PartialEq, Eq, Debug)]
pub struct Stub(bool);

impl Stub {
    pub const NULL: Stub = Stub(false);
    pub const NONNULL: Stub = Stub(true);

    pub fn is_null(self) -> bool {
        !self.0
    }
}

impl JDump for Stub {
    fn dump(&self, d: &mut Dumper) {
        d.raw(if self.0 { "stub" } else { "null" });
    }
}

impl super::collections::JObject for Stub {
    fn hash_code(&self) -> i32 {
        super::collections::identity_hash_code()
    }
    fn equals(&self, other: &Self) -> bool {
        self.same(other)
    }
    fn same(&self, _other: &Self) -> bool {
        super::throw("J2R: identity of a stub object")
    }
    fn is_null(&self) -> bool {
        !self.0
    }
}
