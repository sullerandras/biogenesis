//! The java.awt geometry classes the simulation uses, ported from the JDK 25
//! sources (Rectangle, Rectangle2D, Line2D.Double, Point, Point2D.Double).
//!
//! Java code can extend Rectangle and Line2D.Double (Organism,
//! ExLine2DDouble), and pass such objects where the base class is expected.
//! So a variable of these types is a `JRef<dyn RectangleT>` /
//! `JRef<dyn Line2DT>`, and a generated subclass keeps its base part in a
//! `__super` field and implements the trait. Methods are on the plain structs;
//! arguments of these types are passed as copies, since no JDK method used
//! here keeps or changes them.

#![allow(non_snake_case)]

use std::cell::RefCell;
use std::rc::Rc;

use super::dump::{Dumper, JDumpTarget};
use super::jref::JRef;

/// `java.awt.Rectangle`
#[derive(Clone, Copy, Default, Debug, PartialEq)]
pub struct Rectangle {
    pub x: i32,
    pub y: i32,
    pub width: i32,
    pub height: i32,
}

/// Anything that is a `java.awt.Rectangle`.
pub trait RectangleT: JDumpTarget {
    fn rect(&self) -> &Rectangle;
    fn rect_mut(&mut self) -> &mut Rectangle;
}

impl RectangleT for Rectangle {
    fn rect(&self) -> &Rectangle {
        self
    }
    fn rect_mut(&mut self) -> &mut Rectangle {
        self
    }
}

impl<T: RectangleT + 'static> JRef<T> {
    /// Use as a `Rectangle` (Java's implicit upcast).
    pub fn as_rectangle(self) -> JRef<dyn RectangleT> {
        self.map_rc(|rc| rc as Rc<RefCell<dyn RectangleT>>)
    }
}

impl Rectangle {
    /// `new Rectangle()`
    pub fn new_ref() -> JRef<dyn RectangleT> {
        JRef::new(Rectangle::default()).as_rectangle()
    }

    pub fn setBounds(&mut self, x: i32, y: i32, width: i32, height: i32) {
        self.x = x;
        self.y = y;
        self.width = width;
        self.height = height;
    }

    /// `setBounds(Rectangle r)`
    pub fn setBounds_rect(&mut self, r: &Rectangle) {
        self.setBounds(r.x, r.y, r.width, r.height);
    }

    pub fn getX(&self) -> f64 {
        self.x as f64
    }
    pub fn getY(&self) -> f64 {
        self.y as f64
    }
    pub fn getWidth(&self) -> f64 {
        self.width as f64
    }
    pub fn getHeight(&self) -> f64 {
        self.height as f64
    }

    pub fn isEmpty(&self) -> bool {
        self.width <= 0 || self.height <= 0
    }

    /// `intersects(Rectangle r)`, overflow handling included.
    pub fn intersects(&self, r: &Rectangle) -> bool {
        let mut tw = self.width;
        let mut th = self.height;
        let mut rw = r.width;
        let mut rh = r.height;
        if rw <= 0 || rh <= 0 || tw <= 0 || th <= 0 {
            return false;
        }
        let tx = self.x;
        let ty = self.y;
        let rx = r.x;
        let ry = r.y;
        rw = rw.wrapping_add(rx);
        rh = rh.wrapping_add(ry);
        tw = tw.wrapping_add(tx);
        th = th.wrapping_add(ty);
        (rw < rx || rw > tx) && (rh < ry || rh > ty) && (tw < tx || tw > rx) && (th < ty || th > ry)
    }

    /// `contains(int x, int y)` (= `inside`)
    pub fn contains(&self, X: i32, Y: i32) -> bool {
        let mut w = self.width;
        let mut h = self.height;
        if (w | h) < 0 {
            return false;
        }
        let x = self.x;
        let y = self.y;
        if X < x || Y < y {
            return false;
        }
        w = w.wrapping_add(x);
        h = h.wrapping_add(y);
        (w < x || w > X) && (h < y || h > Y)
    }

    pub const OUT_LEFT: i32 = 1;
    pub const OUT_TOP: i32 = 2;
    pub const OUT_RIGHT: i32 = 4;
    pub const OUT_BOTTOM: i32 = 8;

    /// `Rectangle.outcode(double, double)`
    pub fn outcode(&self, x: f64, y: f64) -> i32 {
        let mut out = 0;
        if self.width <= 0 {
            out |= Self::OUT_LEFT | Self::OUT_RIGHT;
        } else if x < self.x as f64 {
            out |= Self::OUT_LEFT;
        } else if x > self.x as f64 + self.width as f64 {
            out |= Self::OUT_RIGHT;
        }
        if self.height <= 0 {
            out |= Self::OUT_TOP | Self::OUT_BOTTOM;
        } else if y < self.y as f64 {
            out |= Self::OUT_TOP;
        } else if y > self.y as f64 + self.height as f64 {
            out |= Self::OUT_BOTTOM;
        }
        out
    }

    /// `Rectangle2D.intersectsLine(double, double, double, double)`
    pub fn intersectsLine_xy(&self, mut x1: f64, mut y1: f64, x2: f64, y2: f64) -> bool {
        let out2 = self.outcode(x2, y2);
        if out2 == 0 {
            return true;
        }
        loop {
            let out1 = self.outcode(x1, y1);
            if out1 == 0 {
                return true;
            }
            if out1 & out2 != 0 {
                return false;
            }
            if out1 & (Self::OUT_LEFT | Self::OUT_RIGHT) != 0 {
                let mut x = self.getX();
                if out1 & Self::OUT_RIGHT != 0 {
                    x += self.getWidth();
                }
                y1 = y1 + (x - x1) * (y2 - y1) / (x2 - x1);
                x1 = x;
            } else {
                let mut y = self.getY();
                if out1 & Self::OUT_BOTTOM != 0 {
                    y += self.getHeight();
                }
                x1 = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
                y1 = y;
            }
        }
    }

    /// `Rectangle2D.intersectsLine(Line2D l)`
    pub fn intersectsLine(&self, l: &Line2D) -> bool {
        self.intersectsLine_xy(l.getX1(), l.getY1(), l.getX2(), l.getY2())
    }
}

impl JDumpTarget for Rectangle {
    fn dump_target(&self, d: &mut Dumper, id: usize) {
        d.object("Rectangle", id, |d| self.dump_fields(d));
    }
}

impl Rectangle {
    /// The fields, for the dump of a subclass (sorted with its own).
    pub fn dump_fields(&self, d: &mut Dumper) {
        d.field("height", &self.height);
        d.field("width", &self.width);
        d.field("x", &self.x);
        d.field("y", &self.y);
    }
}

/// `java.awt.geom.Line2D.Double` (and `Line2D` variables, which only ever
/// hold one here).
#[derive(Clone, Copy, Default, Debug, PartialEq)]
pub struct Line2D {
    pub x1: f64,
    pub y1: f64,
    pub x2: f64,
    pub y2: f64,
}

/// Anything that is a `java.awt.geom.Line2D.Double`.
pub trait Line2DT: JDumpTarget {
    fn line(&self) -> &Line2D;
    fn line_mut(&mut self) -> &mut Line2D;
}

impl Line2DT for Line2D {
    fn line(&self) -> &Line2D {
        self
    }
    fn line_mut(&mut self) -> &mut Line2D {
        self
    }
}

impl<T: Line2DT + 'static> JRef<T> {
    /// Use as a `Line2D` (Java's implicit upcast).
    pub fn as_line2d(self) -> JRef<dyn Line2DT> {
        self.map_rc(|rc| rc as Rc<RefCell<dyn Line2DT>>)
    }
}

impl Line2D {
    /// `new Line2D.Double()`
    pub fn new_ref() -> JRef<dyn Line2DT> {
        JRef::new(Line2D::default()).as_line2d()
    }

    pub fn setLine(&mut self, x1: f64, y1: f64, x2: f64, y2: f64) {
        self.x1 = x1;
        self.y1 = y1;
        self.x2 = x2;
        self.y2 = y2;
    }
    pub fn getX1(&self) -> f64 {
        self.x1
    }
    pub fn getY1(&self) -> f64 {
        self.y1
    }
    pub fn getX2(&self) -> f64 {
        self.x2
    }
    pub fn getY2(&self) -> f64 {
        self.y2
    }

    /// `getP1()`: a new `Point2D.Double`
    pub fn getP1(&self) -> JRef<Point2D> {
        Point2D::new_ref(self.x1, self.y1)
    }
    pub fn getP2(&self) -> JRef<Point2D> {
        Point2D::new_ref(self.x2, self.y2)
    }

    /// `Line2D.relativeCCW(x1, y1, x2, y2, px, py)`
    pub fn relativeCCW(x1: f64, y1: f64, mut x2: f64, mut y2: f64, mut px: f64, mut py: f64) -> i32 {
        x2 -= x1;
        y2 -= y1;
        px -= x1;
        py -= y1;
        let mut ccw = px * y2 - py * x2;
        if ccw == 0.0 {
            ccw = px * x2 + py * y2;
            if ccw > 0.0 {
                px -= x2;
                py -= y2;
                ccw = px * x2 + py * y2;
                if ccw < 0.0 {
                    ccw = 0.0;
                }
            }
        }
        if ccw < 0.0 {
            -1
        } else if ccw > 0.0 {
            1
        } else {
            0
        }
    }

    /// `Line2D.linesIntersect(...)`
    #[allow(clippy::too_many_arguments)]
    pub fn linesIntersect(x1: f64, y1: f64, x2: f64, y2: f64, x3: f64, y3: f64, x4: f64, y4: f64) -> bool {
        Self::relativeCCW(x1, y1, x2, y2, x3, y3) * Self::relativeCCW(x1, y1, x2, y2, x4, y4) <= 0
            && Self::relativeCCW(x3, y3, x4, y4, x1, y1) * Self::relativeCCW(x3, y3, x4, y4, x2, y2) <= 0
    }

    /// `intersectsLine(Line2D l)`
    pub fn intersectsLine(&self, l: &Line2D) -> bool {
        Self::linesIntersect(l.getX1(), l.getY1(), l.getX2(), l.getY2(), self.getX1(), self.getY1(), self.getX2(), self.getY2())
    }

    pub fn dump_fields(&self, d: &mut Dumper) {
        d.field("x1", &self.x1);
        d.field("x2", &self.x2);
        d.field("y1", &self.y1);
        d.field("y2", &self.y2);
    }
}

impl JDumpTarget for Line2D {
    fn dump_target(&self, d: &mut Dumper, id: usize) {
        d.object("Double", id, |d| self.dump_fields(d));
    }
}

/// `java.awt.Point`
#[derive(Clone, Copy, Default, Debug, PartialEq)]
pub struct Point {
    pub x: i32,
    pub y: i32,
}

impl Point {
    /// `new Point(x, y)`
    pub fn new_ref(x: i32, y: i32) -> JRef<Point> {
        JRef::new(Point { x, y })
    }
}

impl JDumpTarget for Point {
    fn dump_target(&self, d: &mut Dumper, id: usize) {
        d.object("Point", id, |d| {
            d.field("x", &self.x);
            d.field("y", &self.y);
        });
    }
}

/// `java.awt.geom.Point2D.Double`
#[derive(Clone, Copy, Default, Debug, PartialEq)]
pub struct Point2D {
    pub x: f64,
    pub y: f64,
}

impl Point2D {
    /// `new Point2D.Double(x, y)`
    pub fn new_ref(x: f64, y: f64) -> JRef<Point2D> {
        JRef::new(Point2D { x, y })
    }

    pub fn getX(&self) -> f64 {
        self.x
    }
    pub fn getY(&self) -> f64 {
        self.y
    }

    /// `distanceSq(Point2D pt)`
    pub fn distanceSq(&self, pt: &Point2D) -> f64 {
        let px = pt.getX() - self.getX();
        let py = pt.getY() - self.getY();
        px * px + py * py
    }
}

impl JDumpTarget for Point2D {
    fn dump_target(&self, d: &mut Dumper, id: usize) {
        d.object("Double", id, |d| {
            d.field("x", &self.x);
            d.field("y", &self.y);
        });
    }
}
