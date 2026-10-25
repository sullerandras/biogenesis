#![allow(non_snake_case)]

use crate::generated::Organism;
use crate::rt::*;

/// Organism.calculateBounds(), a hot spot (~17% of a frame):
///
/// ```java
/// double left=java.lang.Double.MAX_VALUE, right=java.lang.Double.MIN_VALUE,
/// top=java.lang.Double.MAX_VALUE, bottom=java.lang.Double.MIN_VALUE;
/// final double sin = Math.sin(_theta);
/// final double cos = Math.cos(_theta);
/// for (int i=0; i < _segments; i++) {
///     x1[i] = (int) (_startPointX[i] * cos - _startPointY[i] * sin);
///     y1[i] = (int) (_startPointX[i] * sin + _startPointY[i] * cos);
///     x2[i] = (int) (_endPointX[i] * cos - _endPointY[i] * sin);
///     y2[i] = (int) (_endPointX[i] * sin + _endPointY[i] * cos);
///     left   = Utils.min(left,   x1[i] + _dCenterX, x2[i] + _dCenterX);
///     right  = Utils.max(right,  x1[i] + _dCenterX, x2[i] + _dCenterX);
///     top    = Utils.min(top,    y1[i] + _dCenterY, y2[i] + _dCenterY);
///     bottom = Utils.max(bottom, y1[i] + _dCenterY, y2[i] + _dCenterY);
/// }
/// setBounds((int)left, (int)top, (int)(right-left+1)+1, (int)(bottom-top+1)+1);
/// ```
///
/// The same arithmetic on slices borrowed once, in two loops the compiler
/// vectorizes: the rotation, then the bounds. Math.min/max return one of
/// their operands, so the order of the bounds doesn't matter unless there is
/// a NaN or a -0.0: `int + finite double` is never -0.0, and with a
/// non-finite center or angle the Java loop runs as written.
pub fn calculateBounds(this: &JRef<Organism>) {
    let sin = jmath::sin(this.get(|o| o._theta));
    let cos = jmath::cos(this.get(|o| o._theta));
    let (left, right, top, bottom) = this.get(|o| {
        let (cx, cy) = (o._dCenterX, o._dCenterY);
        let n = o._segments.max(0) as usize;
        o._startPointX.get(|sx| o._startPointY.get(|sy| o._endPointX.get(|ex| o._endPointY.get(|ey| {
            o.x1.set(|x1| o.y1.set(|y1| o.x2.set(|x2| o.y2.set(|y2| {
                if [sx.len(), sy.len(), ex.len(), ey.len(), x1.len(), y1.len(), x2.len(), y2.len()].iter().any(|&l| l < n) {
                    throw("java.lang.ArrayIndexOutOfBoundsException");
                }
                let (sx, sy, ex, ey) = (&sx[..n], &sy[..n], &ex[..n], &ey[..n]);
                let (x1, y1, x2, y2) = (&mut x1[..n], &mut y1[..n], &mut x2[..n], &mut y2[..n]);
                for i in 0..n {
                    let (a, b, c, d) = (sx[i] as f64, sy[i] as f64, ex[i] as f64, ey[i] as f64);
                    x1[i] = (a * cos - b * sin) as i32;
                    y1[i] = (a * sin + b * cos) as i32;
                    x2[i] = (c * cos - d * sin) as i32;
                    y2[i] = (c * sin + d * cos) as i32;
                }
                let (mut left, mut right, mut top, mut bottom) = (f64::MAX, f64::from_bits(1), f64::MAX, f64::from_bits(1));
                if cx.is_finite() && cy.is_finite() && sin.is_finite() && cos.is_finite() {
                    for i in 0..n {
                        let (ax, bx) = (x1[i] as f64 + cx, x2[i] as f64 + cx);
                        let (ay, by) = (y1[i] as f64 + cy, y2[i] as f64 + cy);
                        left = left.min(ax).min(bx);
                        right = right.max(ax).max(bx);
                        top = top.min(ay).min(by);
                        bottom = bottom.max(ay).max(by);
                    }
                } else {
                    for i in 0..n {
                        let (ax, bx) = (x1[i] as f64 + cx, x2[i] as f64 + cx);
                        let (ay, by) = (y1[i] as f64 + cy, y2[i] as f64 + cy);
                        left = jmath::min_f64(jmath::min_f64(left, ax), bx);
                        right = jmath::max_f64(jmath::max_f64(right, ax), bx);
                        top = jmath::min_f64(jmath::min_f64(top, ay), by);
                        bottom = jmath::max_f64(jmath::max_f64(bottom, ay), by);
                    }
                }
                (left, right, top, bottom)
            }))))
        }))))
    });
    this.set(|o| {
        o.__super.setBounds(left as i32, top as i32, ((right - left) + 1f64) as i32 + 1, ((bottom - top) + 1f64) as i32 + 1)
    });
}
