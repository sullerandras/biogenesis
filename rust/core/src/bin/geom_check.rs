//! The Rust side of rust/verify/biogenesis/GeomCheck.java: same arguments,
//! same output.

use biogenesis_core::rt::{JRef, Line2D, Random, Rectangle};

fn coord(r: &JRef<Random>) -> i32 {
    match Random::nextInt_bound(r, 4) {
        0 => Random::nextInt_bound(r, 21) - 10,
        1 => Random::nextInt_bound(r, 2001) - 1000,
        2 => i32::MAX - Random::nextInt_bound(r, 20),
        _ => Random::nextInt(r),
    }
}

fn dcoord(r: &JRef<Random>) -> f64 {
    if Random::nextInt_bound(r, 3) == 0 {
        (Random::nextInt_bound(r, 21) - 10) as f64
    } else {
        (Random::nextDouble(r) - 0.5) * 40.0
    }
}

fn rect(x: i32, y: i32, width: i32, height: i32) -> Rectangle {
    Rectangle { x, y, width, height }
}

fn main() {
    let args: Vec<String> = std::env::args().collect();
    let r = &Random::new_seeded(args[1].parse().unwrap());
    let n: i32 = args[2].parse().unwrap();
    let b01 = |b: bool| if b { 1 } else { 0 };
    let mut out = String::new();
    for _ in 0..n {
        // Java evaluates arguments left to right; so do these lets.
        let (x, y) = (coord(r), coord(r));
        let (w, h) = (Random::nextInt_bound(r, 25) - 2, Random::nextInt_bound(r, 25) - 2);
        let a = rect(x, y, w, h);
        let mut b = Rectangle::default();
        if Random::nextBoolean(r) {
            let (x, y) = (a.x + Random::nextInt_bound(r, 30) - 15, a.y + Random::nextInt_bound(r, 30) - 15);
            let (w, h) = (Random::nextInt_bound(r, 25) - 2, Random::nextInt_bound(r, 25) - 2);
            b.setBounds(x, y, w, h);
        } else {
            let (x, y, w, h) = (coord(r), coord(r), coord(r), coord(r));
            b.setBounds(x, y, w, h);
        }
        let (x, y) = (Random::nextInt_bound(r, 21) - 10, Random::nextInt_bound(r, 21) - 10);
        let (w, h) = (Random::nextInt_bound(r, 12) - 1, Random::nextInt_bound(r, 12) - 1);
        let small = rect(x, y, w, h);
        let mut l = Line2D::default();
        let (x1, y1, x2, y2) = (dcoord(r), dcoord(r), dcoord(r), dcoord(r));
        l.setLine(x1, y1, x2, y2);
        let (x1, y1, x2, y2) = (dcoord(r), dcoord(r), dcoord(r), dcoord(r));
        let m = Line2D { x1, y1, x2, y2 };
        let (px, py) = (coord(r), coord(r));
        let c1 = a.contains(px, py);
        let (cx, cy) = (a.x + Random::nextInt_bound(r, 30) - 5, a.y + Random::nextInt_bound(r, 30) - 5);
        let c2 = a.contains(cx, cy);
        let d = l.getP1().get(|p| *p).distanceSq(&m.getP2().get(|p| *p));
        out.push_str(&format!(
            "{}{}{}{}{}{}{} {} {:x}\n",
            b01(a.intersects(&b)),
            b01(b.intersects(&a)),
            b01(c1),
            b01(c2),
            b01(small.intersectsLine(&l)),
            b01(l.intersectsLine(&m)),
            b01(small.isEmpty()),
            Line2D::relativeCCW(l.x1, l.y1, l.x2, l.y2, m.x1, m.y1),
            d.to_bits()
        ));
    }
    print!("{out}");
}
