//! The Rust side of rust/verify/biogenesis/WorldCheck.java: same arguments,
//! same output.
//!
//! world_check new <seed> <width> <height> <organisms> <frames> [options]
//! world_check load <file> <frames> [options]
//! options: save <frame> <file>, full <frame>, every <k> (hash every k-th frame only; 0: the last)

use biogenesis_core::generated::{World, utils};
use biogenesis_core::rt::{Random, Stub};
use biogenesis_core::save;

#[cfg(not(feature = "count-allocs"))]
#[global_allocator]
static GLOBAL: mimalloc::MiMalloc = mimalloc::MiMalloc;

/// With `--features count-allocs`, the number of allocations of the frames
/// goes to stderr.
#[cfg(feature = "count-allocs")]
#[global_allocator]
static GLOBAL: counting::Counting = counting::Counting;

#[cfg(feature = "count-allocs")]
mod counting {
    use std::alloc::{GlobalAlloc, Layout};
    use std::sync::atomic::{AtomicU64, Ordering::Relaxed};
    pub static ALLOCS: AtomicU64 = AtomicU64::new(0);
    pub struct Counting;

    /// With BIOGENESIS_ALLOC_SITES set, every 16th allocation records the
    /// return addresses of its callers (frame pointer walk, no allocation),
    /// printed at exit relative to the image, for `atos`.
    const DEPTH: usize = 8;
    const SLOTS: usize = 1 << 14;
    static mut SITES: [([usize; DEPTH], u64); SLOTS] = [([0; DEPTH], 0); SLOTS];
    pub static SAMPLING: std::sync::atomic::AtomicBool = std::sync::atomic::AtomicBool::new(false);

    #[inline(never)]
    fn record() {
        let n = ALLOCS.load(Relaxed);
        if n % 16 != 0 {
            return;
        }
        let mut chain = [0usize; DEPTH];
        let mut fp: *const usize;
        unsafe { std::arch::asm!("mov {}, x29", out(reg) fp) };
        for c in chain.iter_mut() {
            if fp.is_null() {
                break;
            }
            unsafe {
                *c = *fp.add(1);
                fp = *fp as *const usize;
            }
        }
        let mut h = chain.iter().fold(0usize, |h, &a| (h ^ a).wrapping_mul(0x100000001b3)) % SLOTS;
        unsafe {
            let sites = &mut *std::ptr::addr_of_mut!(SITES);
            loop {
                if sites[h].1 == 0 || sites[h].0 == chain {
                    sites[h].0 = chain;
                    sites[h].1 += 1;
                    return;
                }
                h = (h + 1) % SLOTS;
            }
        }
    }

    pub fn dump_sites() {
        let base = dump_sites as *const () as usize;
        eprintln!("sites base {base:#x}");
        let sites = unsafe { &*std::ptr::addr_of!(SITES) };
        for (chain, n) in sites.iter().filter(|s| s.1 > 0) {
            let c: Vec<String> = chain.iter().map(|a| format!("{a:#x}")).collect();
            eprintln!("site {} {}", n * 16, c.join(" "));
        }
    }

    unsafe impl GlobalAlloc for Counting {
        unsafe fn alloc(&self, l: Layout) -> *mut u8 {
            ALLOCS.fetch_add(1, Relaxed);
            if SAMPLING.load(Relaxed) {
                record();
            }
            unsafe { mimalloc::MiMalloc.alloc(l) }
        }
        unsafe fn dealloc(&self, p: *mut u8, l: Layout) {
            unsafe { mimalloc::MiMalloc.dealloc(p, l) }
        }
        unsafe fn realloc(&self, p: *mut u8, l: Layout, n: usize) -> *mut u8 {
            ALLOCS.fetch_add(1, Relaxed);
            unsafe { mimalloc::MiMalloc.realloc(p, l, n) }
        }
    }
}

/// Allocations so far (0 without the `count-allocs` feature).
fn allocs() -> u64 {
    #[cfg(feature = "count-allocs")]
    return counting::ALLOCS.load(std::sync::atomic::Ordering::Relaxed);
    #[cfg(not(feature = "count-allocs"))]
    0
}

fn main() {
    let args: Vec<String> = std::env::args().skip(1).collect();
    let (world, mut rest) = match args[0].as_str() {
        "new" => {
            let seed: i64 = args[1].parse().unwrap();
            utils::set_WORLD_WIDTH(args[2].parse().unwrap());
            utils::set_WORLD_HEIGHT(args[3].parse().unwrap());
            utils::set_INITIAL_ORGANISMS(args[4].parse().unwrap());
            utils::set_random(Random::new_seeded(seed));
            let world = World::new(Stub::NONNULL);
            World::genesis(&world);
            (world, &args[5..])
        }
        "load" => {
            let text = std::fs::read_to_string(&args[1]).unwrap();
            (save::load(&text), &args[2..])
        }
        _ => panic!("usage: world_check new|load ..."),
    };
    let frames: i32 = rest[0].parse().unwrap();
    rest = &rest[1..];
    let (mut save_at, mut save_file, mut full, mut every) = (-1, String::new(), -1, 1);
    while !rest.is_empty() {
        match rest[0].as_str() {
            "save" => {
                save_at = rest[1].parse().unwrap();
                save_file = rest[2].clone();
                rest = &rest[3..];
            }
            "every" => {
                every = rest[1].parse().unwrap();
                rest = &rest[2..];
            }
            "full" => {
                full = rest[1].parse().unwrap();
                rest = &rest[2..];
            }
            a => panic!("unknown argument {a}"),
        }
    }

    let mut out = String::new();
    let mut frame_allocs = 0;
    #[cfg(feature = "count-allocs")]
    counting::SAMPLING.store(std::env::var_os("BIOGENESIS_ALLOC_SITES").is_some(), std::sync::atomic::Ordering::Relaxed);
    for f in 0..=frames {
        if f > 0 {
            let before = allocs();
            World::time(&world);
            frame_allocs += allocs() - before;
        }
        let hashed = if every == 0 { f == frames } else { f % every == 0 };
        if !(hashed || f == save_at || f == full) {
            continue;
        }
        let s = if f == save_at { save::save(&world) } else { save::text(&world).0 };
        if f == save_at {
            std::fs::write(&save_file, &s).unwrap();
        }
        if f == full {
            print!("{s}");
            return;
        }
        if !hashed {
            continue;
        }
        out.push_str(&format!(
            "frame {f} population {} organisms {} hash {:x}\n",
            World::getPopulation(&world),
            world.get(|w| w._organisms.clone()).size(),
            fnv(&s)
        ));
    }
    print!("{out}");
    if cfg!(feature = "count-allocs") {
        eprintln!("allocations: {frame_allocs} in {frames} frames, {} a frame", frame_allocs / frames.max(1) as u64);
        #[cfg(feature = "count-allocs")]
        if std::env::var_os("BIOGENESIS_ALLOC_SITES").is_some() {
            counting::dump_sites();
        }
    }
}

/// FNV-1a, 64 bits, of the UTF-8 bytes.
fn fnv(s: &str) -> u64 {
    let mut h: u64 = 0xcbf29ce484222325;
    for b in s.bytes() {
        h ^= b as u64;
        h = h.wrapping_mul(0x100000001b3);
    }
    h
}
