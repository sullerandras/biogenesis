#![allow(non_snake_case)]

use std::collections::HashMap;

use crate::generated::{GeneticCode, Organism, World};
use crate::rt::*;

/// World.getDistinctCladeIDCount(int minPopulation):
///
/// ```java
/// return (int) _organisms.stream()
///         .filter(o -> o.isAlive())
///         .collect(Collectors.groupingBy(o -> o.getGeneticCode().getcladeID(), Collectors.counting()))
///         .entrySet().stream()
///         .filter(e -> e.getValue() >= minPopulation)
///         .count();
/// ```
/// (A count, so the order of the groups doesn't matter.)
pub fn getDistinctCladeIDCount1(this: &JRef<World>, minPopulation: i32) -> i32 {
    let organisms = this.get(|o| o._organisms.clone()).snapshot_array();
    let mut counts: HashMap<String, i64> = HashMap::new();
    for o in organisms.get(|v| v.clone()) {
        if Organism::isAlive(&o) {
            let id = GeneticCode::getcladeID(&Organism::getGeneticCode(&o));
            *counts.entry(id.as_str().to_string()).or_insert(0) += 1;
        }
    }
    counts.values().filter(|&&n| n >= minPopulation as i64).count() as i32
}
