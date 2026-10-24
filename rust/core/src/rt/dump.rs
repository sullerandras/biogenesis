//! A canonical text dump of object graphs, written identically by the Java
//! harness (rust/verify/Dump.java) so that the two programs' states can be
//! diffed. Fields are sorted by name; doubles are written as their raw bits;
//! an object seen before is written as `@id` (so aliasing is compared too).

use std::collections::HashMap;
use std::fmt::Write;

use super::color::Color;
use super::jref::JRef;
use super::jstr::JStr;

#[derive(Default)]
pub struct Dumper {
    pub out: String,
    ids: HashMap<*const (), usize>,
    /// Keeps every dumped object alive, so a freed object's address can't be
    /// reused by a new one (Java's IdentityHashMap holds its keys the same way).
    keep: Vec<Box<dyn std::any::Any>>,
    depth: usize,
    /// Things a save can't reproduce exactly (see rust/core/src/save.rs).
    pub warnings: Vec<String>,
}

impl Dumper {
    pub fn new() -> Dumper {
        Dumper::default()
    }

    fn indent(&mut self) {
        for _ in 0..self.depth {
            self.out.push(' ');
        }
    }

    /// One `name=value` line; `value` writes the value.
    pub fn field(&mut self, name: &str, value: &dyn JDump) {
        self.indent();
        self.out.push_str(name);
        self.out.push('=');
        value.dump(self);
        self.out.push('\n');
    }

    pub fn raw(&mut self, s: &str) {
        self.out.push_str(s);
    }

    /// Writes `Name#id{ ... }` with the object's fields inside.
    pub fn object(&mut self, class: &str, id: usize, body: impl FnOnce(&mut Dumper)) {
        let _ = write!(self.out, "{class}#{id}{{\n");
        self.depth += 1;
        body(self);
        self.depth -= 1;
        self.indent();
        self.out.push('}');
    }
}

pub trait JDump {
    fn dump(&self, d: &mut Dumper);
}

/// The contents of a referenced object (a generated class or an array).
pub trait JDumpTarget {
    fn dump_target(&self, d: &mut Dumper, id: usize);
}

impl<T: JDumpTarget + ?Sized + 'static> JDump for JRef<T> {
    fn dump(&self, d: &mut Dumper) {
        let Some(ptr) = self.identity() else {
            d.raw("null");
            return;
        };
        if let Some(id) = d.ids.get(&ptr) {
            let _ = write!(d.out, "@{id}");
            return;
        }
        let id = d.ids.len();
        d.ids.insert(ptr, id);
        d.keep.push(Box::new(self.clone()));
        self.get(|t| t.dump_target(d, id));
    }
}

impl Dumper {
    /// `[#id n<suffix>` + elements + `]`
    pub fn sequence(&mut self, id: usize, n: usize, suffix: &str, body: impl FnOnce(&mut Dumper)) {
        let _ = write!(self.out, "[#{id} {n}{suffix}\n");
        self.depth += 1;
        body(self);
        self.depth -= 1;
        self.indent();
        self.out.push(']');
    }

    pub fn element(&mut self, x: &dyn JDump) {
        self.indent();
        x.dump(self);
        self.out.push('\n');
    }
}

impl<T: JDump> JDumpTarget for Vec<T> {
    fn dump_target(&self, d: &mut Dumper, id: usize) {
        d.sequence(id, self.len(), "", |d| {
            for x in self {
                d.element(x);
            }
        });
    }
}

impl JDump for i32 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "{self}");
    }
}

impl JDump for i64 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "{self}L");
    }
}

impl JDump for i16 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "{self}");
    }
}

impl JDump for i8 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "{self}");
    }
}

impl JDump for u16 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "{self}");
    }
}

impl JDump for bool {
    fn dump(&self, d: &mut Dumper) {
        d.out.push_str(if *self { "true" } else { "false" });
    }
}

impl JDump for f64 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "d:{:016x}", self.to_bits());
    }
}

impl JDump for f32 {
    fn dump(&self, d: &mut Dumper) {
        let _ = write!(d.out, "f:{:08x}", self.to_bits());
    }
}

impl JDump for Color {
    fn dump(&self, d: &mut Dumper) {
        if self.is_null() {
            d.raw("null");
        } else {
            let _ = write!(d.out, "c:{:08x}", self.getRGB() as u32);
        }
    }
}

impl JDump for JStr {
    fn dump(&self, d: &mut Dumper) {
        match self.option() {
            None => d.raw("null"),
            Some(s) => {
                let _ = write!(d.out, "s{}:{s}", s.encode_utf16().count());
            }
        }
    }
}
