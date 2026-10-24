//! `java.util.Random`, bit for bit (the legacy 48-bit LCG, unchanged since
//! JDK 1.0; the algorithms below follow the JDK's documented contracts).

use std::cell::Cell;
use super::parallel;
use std::sync::atomic::{AtomicI64, Ordering};
use std::time::{SystemTime, UNIX_EPOCH};

use super::jmath;
use super::jref::JRef;
use super::throw;

const MULTIPLIER: i64 = 0x5DEECE66D;
const ADDEND: i64 = 0xB;
const MASK: i64 = (1 << 48) - 1;
const DOUBLE_UNIT: f64 = 1.0 / (1u64 << 53) as f64;
const FLOAT_UNIT: f32 = 1.0 / (1u32 << 24) as f32;

/// On a cache line of its own: with threads, every one of them writes the
/// seed of `Utils.random` all the time.
#[repr(align(128))]
pub struct Random {
    seed: Seed,
    next_next_gaussian: Cell<f64>,
    have_next_next_gaussian: Cell<bool>,
}

/// Java's `AtomicLong seed`: `next` updates it with a compare-and-set loop
/// when several threads may use the generator (Random is thread-safe in
/// Java), with a plain load and store otherwise.
struct Seed(AtomicI64);

impl Seed {
    fn new(v: i64) -> Self {
        Seed(AtomicI64::new(v))
    }
    #[inline]
    fn get(&self) -> i64 {
        self.0.load(Ordering::Relaxed)
    }
    #[inline]
    fn set(&self, v: i64) {
        self.0.store(v, Ordering::Relaxed)
    }
    /// Advances the seed to `f(seed)`, returning the new seed.
    #[inline]
    fn advance(&self, f: impl Fn(i64) -> i64) -> i64 {
        if !parallel::active() {
            let s = f(self.get());
            self.set(s);
            return s;
        }
        let mut old = self.get();
        loop {
            let s = f(old);
            match self.0.compare_exchange_weak(old, s, Ordering::Relaxed, Ordering::Relaxed) {
                Ok(_) => return s,
                Err(o) => old = o,
            }
        }
    }
}

impl Clone for Random {
    fn clone(&self) -> Self {
        Random {
            seed: Seed::new(self.seed.get()),
            next_next_gaussian: self.next_next_gaussian.clone(),
            have_next_next_gaussian: self.have_next_next_gaussian.clone(),
        }
    }
}

static SEED_UNIQUIFIER: AtomicI64 = AtomicI64::new(8682522807148012);

impl Random {
    fn scramble(seed: i64) -> i64 {
        (seed ^ MULTIPLIER) & MASK
    }

    fn raw(seed: i64) -> Random {
        Random {
            seed: Seed::new(Self::scramble(seed)),
            next_next_gaussian: Cell::new(0.0),
            have_next_next_gaussian: Cell::new(false),
        }
    }

    /// `new Random()`: an unpredictable seed, like Java's.
    pub fn new() -> JRef<Random> {
        let u = SEED_UNIQUIFIER
            .try_update(Ordering::Relaxed, Ordering::Relaxed, |u| {
                Some(u.wrapping_mul(1181783497276652981))
            })
            .unwrap()
            .wrapping_mul(1181783497276652981);
        let nanos = SystemTime::now()
            .duration_since(UNIX_EPOCH)
            .map(|d| d.as_nanos() as i64)
            .unwrap_or(0);
        JRef::new(Self::raw(u ^ nanos))
    }

    /// `new Random(seed)`
    pub fn new_seeded(seed: i64) -> JRef<Random> {
        JRef::new(Self::raw(seed))
    }

    fn next(r: &JRef<Random>, bits: u32) -> i32 {
        r.get(|r| {
            let s = r.seed.advance(|s| (s.wrapping_mul(MULTIPLIER).wrapping_add(ADDEND)) & MASK);
            (s as u64 >> (48 - bits)) as i64 as i32
        })
    }

    pub fn setSeed(r: &JRef<Random>, seed: i64) {
        r.get(|r| {
            r.seed.set(Self::scramble(seed));
            r.have_next_next_gaussian.set(false);
        })
    }

    pub fn nextInt(r: &JRef<Random>) -> i32 {
        Self::next(r, 32)
    }

    /// `nextInt(int bound)`
    pub fn nextInt_bound(r: &JRef<Random>, bound: i32) -> i32 {
        if bound <= 0 {
            throw("java.lang.IllegalArgumentException: bound must be positive");
        }
        let mut x = Self::next(r, 31);
        let m = bound - 1;
        if bound & m == 0 {
            ((bound as i64 * x as i64) >> 31) as i32
        } else {
            let mut u = x;
            loop {
                x = u % bound;
                if u.wrapping_sub(x).wrapping_add(m) >= 0 {
                    return x;
                }
                u = Self::next(r, 31);
            }
        }
    }

    pub fn nextLong(r: &JRef<Random>) -> i64 {
        let hi = Self::next(r, 32) as i64;
        let lo = Self::next(r, 32) as i64;
        (hi << 32).wrapping_add(lo)
    }

    pub fn nextBoolean(r: &JRef<Random>) -> bool {
        Self::next(r, 1) != 0
    }

    pub fn nextFloat(r: &JRef<Random>) -> f32 {
        Self::next(r, 24) as f32 * FLOAT_UNIT
    }

    pub fn nextDouble(r: &JRef<Random>) -> f64 {
        let hi = (Self::next(r, 26) as i64) << 27;
        let lo = Self::next(r, 27) as i64;
        (hi + lo) as f64 * DOUBLE_UNIT
    }

    /// `synchronized` in Java (the cached second value).
    pub fn nextGaussian(r: &JRef<Random>) -> f64 {
        let _sync = if parallel::active() { Some(parallel::lock_object(r.identity())) } else { None };
        let cached = r.get(|r| {
            if r.have_next_next_gaussian.get() {
                r.have_next_next_gaussian.set(false);
                Some(r.next_next_gaussian.get())
            } else {
                None
            }
        });
        if let Some(g) = cached {
            return g;
        }
        loop {
            let v1 = 2.0 * Self::nextDouble(r) - 1.0;
            let v2 = 2.0 * Self::nextDouble(r) - 1.0;
            let s = v1 * v1 + v2 * v2;
            if s < 1.0 && s != 0.0 {
                let multiplier = (-2.0 * jmath::strict_log(s) / s).sqrt();
                r.get(|r| {
                    r.next_next_gaussian.set(v2 * multiplier);
                    r.have_next_next_gaussian.set(true);
                });
                return v1 * multiplier;
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::*;

    // Reference values printed by Java 25: new Random(42), then these calls in order.
    #[test]
    fn matches_java() {
        let r = Random::new_seeded(42);
        assert_eq!(Random::nextInt(&r), -1170105035);
        assert_eq!(Random::nextInt_bound(&r, 10), 3);
        assert_eq!(Random::nextInt_bound(&r, 16), 10);
        assert_eq!(Random::nextInt_bound(&r, 1000000007), 102948884);
        assert_eq!(Random::nextLong(&r), 5694868678511409995);
        assert_eq!(Random::nextBoolean(&r), false);
        assert_eq!(Random::nextDouble(&r).to_bits() as i64, 4604549709834685269);
        assert_eq!(Random::nextFloat(&r).to_bits() as i32, 1035667552);
        assert_eq!(Random::nextGaussian(&r).to_bits() as i64, 4605122269962577731);
        assert_eq!(Random::nextGaussian(&r).to_bits() as i64, -4625180662873541370);
        assert_eq!(Random::nextGaussian(&r).to_bits() as i64, -4612796209896339087);
        assert_eq!(Random::nextInt_bound(&r, 3), 1);
    }
}

/// Saves and the world dumps include `Utils.random`: its fields as
/// java.util.Random has them (`seed` is an AtomicLong there).
impl Random {
    pub fn set_raw_seed(&self, seed: i64) {
        self.seed.set(seed);
    }
    pub fn set_next_next_gaussian(&self, v: f64) {
        self.next_next_gaussian.set(v);
    }
    pub fn set_have_next_next_gaussian(&self, v: bool) {
        self.have_next_next_gaussian.set(v);
    }
}

impl super::dump::JDumpTarget for Random {
    fn dump_target(&self, d: &mut super::dump::Dumper, id: usize) {
        d.object("Random", id, |d| {
            d.field("haveNextNextGaussian", &self.have_next_next_gaussian.get());
            d.field("nextNextGaussian", &self.next_next_gaussian.get());
            d.field("seed", &self.seed.get());
        });
    }
}
