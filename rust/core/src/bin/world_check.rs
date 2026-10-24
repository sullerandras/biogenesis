//! The Rust side of rust/verify/biogenesis/WorldCheck.java: same arguments,
//! same output.
//!
//! world_check new <seed> <width> <height> <organisms> <frames> [options]
//! world_check load <file> <frames> [options]
//! options: save <frame> <file>, full <frame>, every <k> (hash every k-th frame only; 0: the last)

use biogenesis_core::generated::{World, utils};
use biogenesis_core::rt::{Random, Stub};
use biogenesis_core::save;

#[global_allocator]
static GLOBAL: mimalloc::MiMalloc = mimalloc::MiMalloc;

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
    for f in 0..=frames {
        if f > 0 {
            World::time(&world);
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
