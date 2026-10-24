//! Reads the dump format (rt::dump) back into objects: the save file format.
//! `@id` refers to an object read earlier, so objects are registered before
//! their fields are read, and cycles (organism -> world -> organisms) work.
//! J2R generates `JLoad` for the translated classes; the rt types are here.

use std::any::Any;
use std::collections::HashMap;

use super::collections::{JCollection, JCollectionT, JObject};
use super::color::Color;
use super::dump::JDump;
use super::geom::{Line2D, Line2DT, Point, Point2D, Rectangle, RectangleT};
use super::hashset::JHashSet;
use super::jref::JRef;
use super::jstr::JStr;
use super::random::Random;
use super::stub::Stub;

pub struct Loader<'a> {
    s: &'a str,
    pos: usize,
    /// Objects by id, as every type they were registered as.
    objects: HashMap<usize, Vec<Box<dyn Any>>>,
}

pub trait JLoad: Sized {
    fn load(l: &mut Loader) -> Self;
}

impl<'a> Loader<'a> {
    pub fn new(s: &'a str) -> Loader<'a> {
        Loader { s, pos: 0, objects: HashMap::new() }
    }

    #[cold]
    pub fn fail(&self, what: &str) -> ! {
        let line = self.s[..self.pos].matches('\n').count() + 1;
        let rest: String = self.s[self.pos..].chars().take(40).collect();
        panic!("save file, line {line}: {what} at {rest:?}")
    }

    pub fn at_end(&self) -> bool {
        self.pos >= self.s.len()
    }

    pub fn peek(&self, p: &str) -> bool {
        self.s[self.pos..].starts_with(p)
    }

    /// Consumes `p` if it's next.
    pub fn eat(&mut self, p: &str) -> bool {
        if self.peek(p) {
            self.pos += p.len();
            true
        } else {
            false
        }
    }

    pub fn expect(&mut self, p: &str) {
        if !self.eat(p) {
            self.fail(&format!("expected {p:?}"));
        }
    }

    fn take_while(&mut self, f: impl Fn(char) -> bool) -> &'a str {
        let start = self.pos;
        let rest = &self.s[start..];
        let n = rest.find(|c: char| !f(c)).unwrap_or(rest.len());
        self.pos += n;
        &self.s[start..start + n]
    }

    pub fn skip_spaces(&mut self) {
        self.take_while(|c| c == ' ');
    }

    /// A field or static name followed by `=`.
    pub fn name(&mut self) -> &'a str {
        self.skip_spaces();
        let n = self.take_while(|c| c != '=' && c != '\n');
        self.expect("=");
        n
    }

    pub fn end_line(&mut self) {
        self.expect("\n");
    }

    fn int_text(&mut self) -> &'a str {
        let neg = self.peek("-");
        let start = self.pos;
        if neg {
            self.pos += 1;
        }
        self.take_while(|c| c.is_ascii_digit());
        &self.s[start..self.pos]
    }

    pub fn i64_value(&mut self) -> i64 {
        let t = self.int_text();
        t.parse().unwrap_or_else(|_| self.fail("expected an integer"))
    }

    fn hex(&mut self, prefix: &str) -> u64 {
        self.expect(prefix);
        let t = self.take_while(|c| c.is_ascii_hexdigit());
        u64::from_str_radix(t, 16).unwrap_or_else(|_| self.fail("expected hex digits"))
    }

    fn usize_value(&mut self) -> usize {
        let t = self.take_while(|c| c.is_ascii_digit());
        t.parse().unwrap_or_else(|_| self.fail("expected a number"))
    }

    pub fn register<T: Clone + 'static>(&mut self, id: usize, x: T) {
        self.objects.entry(id).or_default().push(Box::new(x));
    }

    pub fn lookup<T: Clone + 'static>(&self, id: usize) -> T {
        match self.objects.get(&id).and_then(|v| v.iter().find_map(|b| b.downcast_ref::<T>())) {
            Some(x) => x.clone(),
            None => self.fail(&format!("@{id} is not a {}", std::any::type_name::<T>())),
        }
    }

    /// `null`, `@id`, or `Name#id{` + fields + `}`. `make` creates the object
    /// (it registers it before reading fields); `field` reads one field.
    pub fn object<T: Clone + 'static>(&mut self, class: &str, make: impl FnOnce() -> T, mut field: impl FnMut(&mut Loader, &T, &str), null: T) -> T {
        if self.eat("null") {
            return null;
        }
        if self.eat("@") {
            let id = self.usize_value();
            return self.lookup(id);
        }
        self.expect(class);
        self.expect("#");
        let id = self.usize_value();
        self.expect("{\n");
        let x = make();
        self.register(id, x.clone());
        loop {
            self.skip_spaces();
            if self.eat("}") {
                return x;
            }
            let n = self.name();
            field(self, &x, n);
            self.end_line();
        }
    }

    /// The class name of the object that comes next (`Name#id{`), if one does.
    pub fn next_class(&self) -> Option<&'a str> {
        let rest = &self.s[self.pos..];
        let hash = rest.find('#')?;
        let name = &rest[..hash];
        if name.chars().all(|c| c.is_ascii_alphanumeric() || c == '_') && !name.is_empty() { Some(name) } else { None }
    }

    /// `[#id n` + optional ` HashSet(cap)` + elements + `]`.
    fn sequence<T: JLoad>(&mut self) -> (usize, Option<i32>, Vec<T>) {
        self.expect("[#");
        let id = self.usize_value();
        self.expect(" ");
        let n = self.usize_value();
        let cap = if self.eat(" HashSet(") {
            let c = self.usize_value() as i32;
            self.expect(")");
            Some(c)
        } else {
            None
        };
        self.end_line();
        let mut v = Vec::with_capacity(n);
        for _ in 0..n {
            self.skip_spaces();
            v.push(T::load(self));
            self.end_line();
        }
        self.skip_spaces();
        self.expect("]");
        (id, cap, v)
    }

    pub fn unknown_field(&self, class: &str, name: &str) -> ! {
        self.fail(&format!("{class} has no field {name}"))
    }
}

impl JLoad for i32 {
    fn load(l: &mut Loader) -> Self {
        l.i64_value() as i32
    }
}

impl JLoad for i16 {
    fn load(l: &mut Loader) -> Self {
        l.i64_value() as i16
    }
}

impl JLoad for i8 {
    fn load(l: &mut Loader) -> Self {
        l.i64_value() as i8
    }
}

impl JLoad for u16 {
    fn load(l: &mut Loader) -> Self {
        l.i64_value() as u16
    }
}

impl JLoad for i64 {
    fn load(l: &mut Loader) -> Self {
        let v = l.i64_value();
        l.expect("L");
        v
    }
}

impl JLoad for bool {
    fn load(l: &mut Loader) -> Self {
        if l.eat("true") {
            true
        } else if l.eat("false") {
            false
        } else {
            l.fail("expected a boolean")
        }
    }
}

impl JLoad for f64 {
    fn load(l: &mut Loader) -> Self {
        f64::from_bits(l.hex("d:"))
    }
}

impl JLoad for f32 {
    fn load(l: &mut Loader) -> Self {
        f32::from_bits(l.hex("f:") as u32)
    }
}

impl JLoad for Color {
    fn load(l: &mut Loader) -> Self {
        if l.eat("null") {
            return Color::NULL;
        }
        let argb = l.hex("c:") as u32;
        Color::rgba(((argb >> 16) & 0xff) as i32, ((argb >> 8) & 0xff) as i32, (argb & 0xff) as i32, (argb >> 24) as i32)
    }
}

impl JLoad for JStr {
    fn load(l: &mut Loader) -> Self {
        if l.eat("null") {
            return JStr::NULL;
        }
        l.expect("s");
        let n = l.usize_value();
        l.expect(":");
        // n UTF-16 units
        let start = l.pos;
        let mut units = 0;
        for c in l.s[start..].chars() {
            if units >= n {
                break;
            }
            units += c.len_utf16();
            l.pos += c.len_utf8();
        }
        JStr::from(&l.s[start..l.pos])
    }
}

impl JLoad for Stub {
    fn load(l: &mut Loader) -> Self {
        if l.eat("null") {
            Stub::NULL
        } else {
            l.expect("stub");
            Stub::NONNULL
        }
    }
}

impl<T: JLoad + Clone + 'static> JLoad for JRef<Vec<T>> {
    fn load(l: &mut Loader) -> Self {
        if l.eat("null") {
            return JRef::null();
        }
        if l.eat("@") {
            let id = l.usize_value();
            return l.lookup(id);
        }
        // Elements can't refer back to their array, so it's registered after them.
        let (id, cap, v) = l.sequence::<T>();
        if cap.is_some() {
            l.fail("a HashSet where an array or List is expected");
        }
        let a = JRef::new(v);
        l.register(id, a.clone());
        a
    }
}

impl<T: JLoad + Clone + 'static> JLoad for JRef<[T]> {
    fn load(l: &mut Loader) -> Self {
        if l.eat("null") {
            return JRef::null();
        }
        if l.eat("@") {
            let id = l.usize_value();
            return l.lookup(id);
        }
        // Elements can't refer back to their array, so it's registered after them.
        let (id, cap, v) = l.sequence::<T>();
        if cap.is_some() {
            l.fail("a HashSet where an array or List is expected");
        }
        let a = JRef::<[T]>::from_vec(v);
        l.register(id, a.clone());
        a
    }
}

impl<T: JLoad + JObject + JDump + 'static> JLoad for JCollection<T> {
    fn load(l: &mut Loader) -> Self {
        if l.eat("null") {
            return JRef::null();
        }
        if l.eat("@") {
            let id = l.usize_value();
            return l.lookup(id);
        }
        let (id, cap, v) = l.sequence::<T>();
        let c: JCollection<T> = match cap {
            Some(cap) => {
                // As java.util.HashSet(cap) + add(...) in iteration order: the
                // same table size and bins (see JHashSet::with_capacity).
                let mut s = JHashSet::with_capacity(cap);
                for x in v {
                    JCollectionT::add(&mut s, x);
                }
                let r = JRef::new(s);
                l.register(id, r.clone());
                r.as_collection()
            }
            None => {
                let r = JRef::new(v);
                l.register(id, r.clone());
                r.as_collection()
            }
        };
        l.register(id, c.clone());
        c
    }
}

impl JLoad for JRef<Random> {
    fn load(l: &mut Loader) -> Self {
        let r = l.object(
            "Random",
            || Random::new_seeded(0),
            |l, r, n| match n {
                "haveNextNextGaussian" => {
                    let v = bool::load(l);
                    r.get(|r| r.set_have_next_next_gaussian(v))
                }
                "nextNextGaussian" => {
                    let v = f64::load(l);
                    r.get(|r| r.set_next_next_gaussian(v))
                }
                "seed" => {
                    let v = i64::load(l);
                    r.get(|r| r.set_raw_seed(v))
                }
                _ => l.unknown_field("Random", n),
            },
            JRef::null(),
        );
        r
    }
}

fn load_rect(l: &mut Loader, r: &mut Rectangle, n: &str) {
    match n {
        "x" => r.x = i32::load(l),
        "y" => r.y = i32::load(l),
        "width" => r.width = i32::load(l),
        "height" => r.height = i32::load(l),
        _ => l.unknown_field("Rectangle", n),
    }
}

/// The geometry part of a translated subclass (`__super` fields).
pub fn load_rectangle_field(l: &mut Loader, r: &mut Rectangle, n: &str) {
    load_rect(l, r, n)
}

pub fn load_line2d_field(l: &mut Loader, r: &mut Line2D, n: &str) {
    match n {
        "x1" => r.x1 = f64::load(l),
        "y1" => r.y1 = f64::load(l),
        "x2" => r.x2 = f64::load(l),
        "y2" => r.y2 = f64::load(l),
        _ => l.unknown_field("Line2D.Double", n),
    }
}

impl JLoad for JRef<dyn RectangleT> {
    fn load(l: &mut Loader) -> Self {
        if !l.peek("null") && !l.peek("@") && l.next_class() != Some("Rectangle") {
            l.fail("only a java.awt.Rectangle can be loaded where a Rectangle is expected");
        }
        let r = l.object(
            "Rectangle",
            || JRef::new(Rectangle::default()),
            |l, r, n| {
                let mut v = r.get(|r| *r);
                load_rect(l, &mut v, n);
                r.set(|r| *r = v)
            },
            JRef::null(),
        );
        r.as_rectangle()
    }
}

impl JLoad for JRef<dyn Line2DT> {
    fn load(l: &mut Loader) -> Self {
        if !l.peek("null") && !l.peek("@") && l.next_class() != Some("Double") {
            l.fail("only a Line2D.Double can be loaded where a Line2D is expected");
        }
        let r = l.object(
            "Double",
            || JRef::new(Line2D::default()),
            |l, r, n| {
                let mut v = r.get(|r| *r);
                load_line2d_field(l, &mut v, n);
                r.set(|r| *r = v)
            },
            JRef::null(),
        );
        r.as_line2d()
    }
}

impl JLoad for JRef<Point> {
    fn load(l: &mut Loader) -> Self {
        l.object(
            "Point",
            || JRef::new(Point::default()),
            |l, p, n| match n {
                "x" => {
                    let v = i32::load(l);
                    p.set(|p| p.x = v)
                }
                "y" => {
                    let v = i32::load(l);
                    p.set(|p| p.y = v)
                }
                _ => l.unknown_field("Point", n),
            },
            JRef::null(),
        )
    }
}

impl JLoad for JRef<Point2D> {
    fn load(l: &mut Loader) -> Self {
        l.object(
            "Double",
            || JRef::new(Point2D::default()),
            |l, p, n| match n {
                "x" => {
                    let v = f64::load(l);
                    p.set(|p| p.x = v)
                }
                "y" => {
                    let v = f64::load(l);
                    p.set(|p| p.y = v)
                }
                _ => l.unknown_field("Point2D.Double", n),
            },
            JRef::null(),
        )
    }
}
