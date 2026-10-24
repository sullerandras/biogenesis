//! Saved games: the world and the simulation's statics (parameters,
//! `Utils.random`) in the dump format, read back by rt::load. The Java side
//! (rust/verify/biogenesis/Save.java, Load.java) reads and writes the same
//! files, so a game saved by one continues identically in the other.
//!
//! The one case where a loaded game may not continue exactly as the saved one
//! would have: a HashSet bin that had become a tree (8+ organisms whose IDs
//! collide in the table). Loading adds the organisms in iteration order,
//! which can build that bin in a different order; `save` warns about it.

use crate::generated::{self, World};
use crate::rt::*;

pub const HEADER: &str = "biogenesis-save 1\n";

/// The save file text. Warns (stderr) if loading it may not continue exactly.
pub fn save(world: &JRef<World>) -> String {
    let (text, warnings) = text(world);
    for w in warnings {
        eprintln!("warning: {w}");
    }
    text
}

/// The save file text, and what a game loaded from it can't reproduce exactly.
pub fn text(world: &JRef<World>) -> (String, Vec<String>) {
    let mut d = Dumper::new();
    d.raw(HEADER);
    d.field("world", world);
    generated::dump_statics(&mut d);
    (d.out, d.warnings)
}

pub fn load(s: &str) -> JRef<World> {
    let mut l = Loader::new(s);
    l.expect(HEADER);
    let mut world = JRef::null();
    while !l.at_end() {
        let n = l.name();
        if n == "world" {
            world = JLoad::load(&mut l);
        } else if !generated::load_static(&mut l, n) {
            l.fail(&format!("unknown static {n}"));
        }
        l.end_line();
    }
    if world.is_null() {
        l.fail("no world");
    }
    world
}
