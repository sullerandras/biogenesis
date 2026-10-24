//! The Rust side of rust/verify/biogenesis/HashSetCheck.java: same
//! arguments, same output.

use biogenesis_core::rt::{JHashSet, JObject, Random, JRef};

#[derive(Clone)]
struct Key(i32);

impl JObject for Key {
    fn hash_code(&self) -> i32 {
        if self.0 % 3 == 0 { self.0 << 10 } else { self.0 }
    }
    fn equals(&self, other: &Self) -> bool {
        self.0 == other.0
    }
    fn same(&self, _other: &Self) -> bool {
        false // a new Key object every time
    }
}

fn main() {
    let args: Vec<String> = std::env::args().collect();
    let r = Random::new_seeded(args[1].parse().unwrap());
    let ops: i32 = args[2].parse().unwrap();
    let r: &JRef<Random> = &r;
    let mut set = JHashSet::new();
    let mut out = String::new();
    for i in 1..=ops {
        let pool = 1 + (i / 1000) % 4 * 700;
        let op = Random::nextInt_bound(r, 1000);
        let id = Random::nextInt_bound(r, pool);
        let b = if op == 0 {
            set.clear();
            true
        } else if op < 600 {
            set.add(Key(id))
        } else if op < 950 {
            set.remove(&Key(id))
        } else {
            set.contains(&Key(id))
        };
        out.push(if b { '+' } else { '-' });
        if i % 500 == 0 {
            out.push_str(&format!("\nsize {}:", set.size()));
            for k in set.snapshot() {
                out.push_str(&format!(" {}", k.0));
            }
            out.push('\n');
        }
    }
    print!("{out}");
}
