//! `java.lang.Math` / `StrictMath` functions whose Rust equivalents differ
//! from Java in edge cases. Functions that are identical (sqrt, floor, ceil,
//! abs, the operators) are used directly by the generated code.

/// `Math.max(double, double)`: NaN wins, and max(-0.0, 0.0) is 0.0.
#[inline]
pub fn max_f64(a: f64, b: f64) -> f64 {
    if a.is_nan() {
        return a;
    }
    if a == 0.0 && b == 0.0 && a.to_bits() == (-0.0f64).to_bits() {
        return b;
    }
    if a >= b { a } else { b }
}

/// `Math.min(double, double)`: NaN wins, and min(-0.0, 0.0) is -0.0.
#[inline]
pub fn min_f64(a: f64, b: f64) -> f64 {
    if a.is_nan() {
        return a;
    }
    if a == 0.0 && b == 0.0 && b.to_bits() == (-0.0f64).to_bits() {
        return b;
    }
    if a <= b { a } else { b }
}

#[inline]
pub fn max_f32(a: f32, b: f32) -> f32 {
    if a.is_nan() {
        return a;
    }
    if a == 0.0 && b == 0.0 && a.to_bits() == (-0.0f32).to_bits() {
        return b;
    }
    if a >= b { a } else { b }
}

#[inline]
pub fn min_f32(a: f32, b: f32) -> f32 {
    if a.is_nan() {
        return a;
    }
    if a == 0.0 && b == 0.0 && b.to_bits() == (-0.0f32).to_bits() {
        return b;
    }
    if a <= b { a } else { b }
}

/// `Math.round(double)`: floor(a + 0.5) computed without rounding error
/// (the JDK 7+ algorithm).
pub fn round_f64(a: f64) -> i64 {
    let bits = a.to_bits() as i64;
    let biased_exp = (bits & 0x7FF0_0000_0000_0000) >> 52;
    let shift = (51 + 1023) - biased_exp;
    if shift & -64 == 0 {
        let mut r = (bits & 0x000F_FFFF_FFFF_FFFF) | 0x0010_0000_0000_0000;
        if bits < 0 {
            r = -r;
        }
        ((r >> shift) + 1) >> 1
    } else {
        a as i64
    }
}

/// `Math.round(float)`
pub fn round_f32(a: f32) -> i32 {
    let bits = a.to_bits() as i32;
    let biased_exp = (bits & 0x7F80_0000) >> 23;
    let shift = (22 + 127) - biased_exp;
    if shift & -32 == 0 {
        let mut r = (bits & 0x007F_FFFF) | 0x0080_0000;
        if bits < 0 {
            r = -r;
        }
        ((r >> shift) + 1) >> 1
    } else {
        a as i32
    }
}

/// `Math.signum(double)`
#[inline]
pub fn signum_f64(d: f64) -> f64 {
    if d == 0.0 || d.is_nan() { d } else { 1f64.copysign(d) }
}

const LN2_HI: f64 = 6.93147180369123816490e-01; // 3fe62e42 fee00000
const LN2_LO: f64 = 1.90821492927058770002e-10; // 3dea39ef 35793c76
const TWO54: f64 = 1.80143985094819840000e+16; // 43500000 00000000
const LG1: f64 = 6.666666666666735130e-01; // 3FE55555 55555593
const LG2: f64 = 3.999999999940941908e-01; // 3FD99999 9997FA04
const LG3: f64 = 2.857142874366239149e-01; // 3FD24924 94229359
const LG4: f64 = 2.222219843214978396e-01; // 3FCC71C5 1D8E78AF
const LG5: f64 = 1.818357216161805012e-01; // 3FC74664 96CB03DE
const LG6: f64 = 1.531383769920937332e-01; // 3FC39A09 D078C69F
const LG7: f64 = 1.479819860511658591e-01; // 3FC2F112 DF3E5244

#[inline]
fn hi(x: f64) -> i32 {
    (x.to_bits() >> 32) as i32
}

#[inline]
fn lo(x: f64) -> u32 {
    x.to_bits() as u32
}

#[inline]
fn with_hi(x: f64, hi: i32) -> f64 {
    f64::from_bits(((hi as u32 as u64) << 32) | lo(x) as u64)
}

/// `StrictMath.log`: fdlibm's `__ieee754_log`.
pub fn strict_log(mut x: f64) -> f64 {
    let mut hx = hi(x);
    let lx = lo(x);
    let mut k: i32 = 0;
    if hx < 0x0010_0000 {
        // x < 2**-1022
        if ((hx & 0x7fff_ffff) as u32 | lx) == 0 {
            return f64::NEG_INFINITY; // log(+-0) = -inf
        }
        if hx < 0 {
            return f64::NAN; // log(-#) = NaN
        }
        k -= 54;
        x *= TWO54; // subnormal number, scale up x
        hx = hi(x);
    }
    if hx >= 0x7ff0_0000 {
        return x + x;
    }
    k += (hx >> 20) - 1023;
    hx &= 0x000f_ffff;
    let i = (hx + 0x95f64) & 0x10_0000;
    x = with_hi(x, hx | (i ^ 0x3ff0_0000)); // normalize x or x/2
    k += i >> 20;
    let f = x - 1.0;
    if (0x000f_ffff & (2 + hx)) < 3 {
        // |f| < 2**-20
        if f == 0.0 {
            if k == 0 {
                return 0.0;
            }
            let dk = k as f64;
            return dk * LN2_HI + dk * LN2_LO;
        }
        let r = f * f * (0.5 - 0.33333333333333333 * f);
        if k == 0 {
            return f - r;
        }
        let dk = k as f64;
        return dk * LN2_HI - ((r - dk * LN2_LO) - f);
    }
    let s = f / (2.0 + f);
    let dk = k as f64;
    let z = s * s;
    let mut i = hx - 0x6147a;
    let w = z * z;
    let j = 0x6b851 - hx;
    let t1 = w * (LG2 + w * (LG4 + w * LG6));
    let t2 = z * (LG1 + w * (LG3 + w * (LG5 + w * LG7)));
    i |= j;
    let r = t2 + t1;
    if i > 0 {
        let hfsq = 0.5 * f * f;
        if k == 0 {
            f - (hfsq - s * (hfsq + r))
        } else {
            dk * LN2_HI - ((hfsq - (s * (hfsq + r) + dk * LN2_LO)) - f)
        }
    } else if k == 0 {
        f - s * (f - r)
    } else {
        dk * LN2_HI - ((s * (f - r) - dk * LN2_LO) - f)
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    #[test]
    fn log_constants_match_their_bit_patterns() {
        let pairs = [
            (LN2_HI, 0x3fe62e42fee00000u64),
            (LN2_LO, 0x3dea39ef35793c76),
            (TWO54, 0x4350000000000000),
            (LG1, 0x3FE5555555555593),
            (LG2, 0x3FD999999997FA04),
            (LG3, 0x3FD2492494229359),
            (LG4, 0x3FCC71C51D8E78AF),
            (LG5, 0x3FC7466496CB03DE),
            (LG6, 0x3FC39A09D078C69F),
            (LG7, 0x3FC2F112DF3E5244),
        ];
        for (v, bits) in pairs {
            assert_eq!(v.to_bits(), bits, "{v}");
        }
    }

    #[test]
    fn round_matches_java() {
        assert_eq!(round_f64(0.49999999999999994), 0);
        assert_eq!(round_f64(0.5), 1);
        assert_eq!(round_f64(-0.5), 0);
        assert_eq!(round_f64(-1.5), -1);
        assert_eq!(round_f64(2.5), 3);
        assert_eq!(round_f64(f64::NAN), 0);
        assert_eq!(round_f64(1e300), i64::MAX);
        assert_eq!(round_f32(2.5), 3);
        assert_eq!(round_f32(-2.5), -2);
    }
}
