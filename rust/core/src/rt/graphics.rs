//! `java.awt.Graphics`: what translated drawing code (`Organism.draw`) draws
//! on. A front end implements it; the simulation never calls it itself.

use super::color::Color;

pub trait Graphics {
    fn setColor(&mut self, c: Color);
    fn drawLine(&mut self, x1: i32, y1: i32, x2: i32, y2: i32);
    fn drawRect(&mut self, x: i32, y: i32, width: i32, height: i32) {
        self.drawLine(x, y, x + width, y);
        self.drawLine(x + width, y, x + width, y + height);
        self.drawLine(x + width, y + height, x, y + height);
        self.drawLine(x, y + height, x, y);
    }
}
