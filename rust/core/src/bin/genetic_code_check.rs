//! The Rust side of rust/verify/biogenesis/GeneticCodeCheck.java: same
//! arguments, same output.

use biogenesis_core::generated::{GeneticCode, utils};
use biogenesis_core::rt::{Dumper, JDump, Random};

fn main() {
    let args: Vec<String> = std::env::args().collect();
    let seed: i64 = args[1].parse().unwrap();
    let lineages: i32 = args[2].parse().unwrap();
    let generations: i32 = args[3].parse().unwrap();
    let hot = args.get(4).is_some_and(|a| a == "hot");
    utils::set_random(Random::new_seeded(seed));
    if hot {
        utils::set_META_MUTATION_RATE(4000);
        utils::set_XY_MUTATION_RATE(4000);
        utils::set_MIN_MUTATION_RATE(300);
        utils::set_MAX_MUTATION_RATE(6000);
        utils::set_MIN_CLONE_RATE(10);
        utils::set_MAX_CLONE_RATE(90);
        utils::set_INITIAL_COMPLEXITY(6);
    }
    let mut d = Dumper::new();
    for l in 0..lineages {
        let mut code = GeneticCode::new();
        d.raw(&format!("lineage {l}\n"));
        code.dump(&mut d);
        d.raw("\n");
        for g in 0..generations {
            let mut child = GeneticCode::new1(code.clone());
            if g % 7 == 3 {
                child = GeneticCode::jclone(&child);
            }
            child.dump(&mut d);
            d.raw("\n");
            code = child;
        }
    }
    d.raw(&format!("random {}\n", Random::nextLong(&utils::random())));
    print!("{}", d.out);
}
