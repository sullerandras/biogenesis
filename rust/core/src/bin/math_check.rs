//! The Rust side of rust/verify/biogenesis/MathCheck.java: same arguments,
//! same output.

use biogenesis_core::generated::strict_math as m;
use biogenesis_core::rt::Random;

fn arg(r: &biogenesis_core::rt::JRef<Random>, kind: i32) -> f64 {
    match kind {
        0 => (Random::nextDouble(r) - 0.5) * 20.0,
        1 => (Random::nextDouble(r) - 0.5) * 2000.0,
        2 => f64::from_bits(Random::nextLong(r) as u64),
        3 => (Random::nextInt_bound(r, 2000) - 1000) as f64 + Random::nextDouble(r),
        _ => Random::nextDouble(r) * 2.0 - 1.0,
    }
}

fn main() {
    let args: Vec<String> = std::env::args().collect();
    let r = Random::new_seeded(args[1].parse().unwrap());
    let n: i32 = args[2].parse().unwrap();
    let mut out = String::new();
    for i in 0..n {
        let x = arg(&r, i % 5);
        let y = arg(&r, (i / 5) % 5);
        let v = [
            m::sin(x), m::cos(x), m::tan(x), m::asin(x), m::acos(x),
            m::atan(x), m::atan2(x, y), m::exp(x), m::log(x), m::log10(x),
            m::log1p(x), m::expm1(x), m::pow(x, y), m::cbrt(x), m::hypot(x, y),
            m::sinh(x), m::cosh(x), m::tanh(x),
        ];
        out.push_str(&format!("{:x} {:x}", x.to_bits(), y.to_bits()));
        for d in v {
            out.push_str(&format!(" {:x}", d.to_bits()));
        }
        out.push('\n');
    }
    print!("{out}");
}
