//! `java.awt.Color` as a value. Java colors are immutable and the simulation
//! compares them with `equals` (by RGB value), so a copyable value with a null
//! state behaves the same.

use super::throw;

#[derive(Clone, Copy, Default, PartialEq, Eq, Hash, Debug)]
pub struct Color(Option<u32>);

impl Color {
    pub const NULL: Color = Color(None);

    pub const WHITE: Color = Color::rgb(255, 255, 255);
    pub const LIGHT_GRAY: Color = Color::rgb(192, 192, 192);
    pub const GRAY: Color = Color::rgb(128, 128, 128);
    pub const DARK_GRAY: Color = Color::rgb(64, 64, 64);
    pub const BLACK: Color = Color::rgb(0, 0, 0);
    pub const RED: Color = Color::rgb(255, 0, 0);
    pub const PINK: Color = Color::rgb(255, 175, 175);
    pub const ORANGE: Color = Color::rgb(255, 200, 0);
    pub const YELLOW: Color = Color::rgb(255, 255, 0);
    pub const GREEN: Color = Color::rgb(0, 255, 0);
    pub const MAGENTA: Color = Color::rgb(255, 0, 255);
    pub const CYAN: Color = Color::rgb(0, 255, 255);
    pub const BLUE: Color = Color::rgb(0, 0, 255);

    /// `new Color(r, g, b)`
    pub const fn rgb(r: i32, g: i32, b: i32) -> Color {
        Color::rgba(r, g, b, 255)
    }

    /// `new Color(r, g, b, a)`
    pub const fn rgba(r: i32, g: i32, b: i32, a: i32) -> Color {
        if r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255 || a < 0 || a > 255 {
            panic!("java.lang.IllegalArgumentException: Color parameter outside of expected range");
        }
        Color(Some(((a as u32) << 24) | ((r as u32) << 16) | ((g as u32) << 8) | b as u32))
    }

    #[inline]
    pub fn is_null(self) -> bool {
        self.0.is_none()
    }

    #[inline]
    fn value(self) -> u32 {
        match self.0 {
            Some(v) => v,
            None => throw("java.lang.NullPointerException (Color)"),
        }
    }

    /// `c.getRGB()`
    #[inline]
    pub fn getRGB(self) -> i32 {
        self.value() as i32
    }

    #[inline]
    pub fn getRed(self) -> i32 {
        ((self.value() >> 16) & 0xff) as i32
    }

    #[inline]
    pub fn getGreen(self) -> i32 {
        ((self.value() >> 8) & 0xff) as i32
    }

    #[inline]
    pub fn getBlue(self) -> i32 {
        (self.value() & 0xff) as i32
    }

    #[inline]
    pub fn getAlpha(self) -> i32 {
        ((self.value() >> 24) & 0xff) as i32
    }

    /// `a.equals(b)`: NullPointerException when `a` is null, false when `b` is.
    #[inline]
    pub fn equals(a: Color, b: Color) -> bool {
        let av = a.value();
        b.0 == Some(av)
    }
}
