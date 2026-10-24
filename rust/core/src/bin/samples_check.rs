//! The Rust side of rust/verify/j2rtest/SamplesCheck.java.

use biogenesis_core::samples::samples;

fn main() {
    for seed in 0..40 {
        println!("{}", samples::run(seed).as_str());
    }
}
