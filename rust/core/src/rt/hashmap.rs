//! `java.util.HashMap` for lookups only: J2R translates `put`,
//! `getOrDefault`, `containsKey` and `size`, with int or long keys. No
//! iteration: that would need the JDK's order (as `JHashSet` has), so J2R
//! refuses it.

use std::collections::HashMap;
use std::hash::Hash;

use super::jref::JRef;

#[derive(Default)]
pub struct JHashMap<K, V>(HashMap<K, V>);

impl<K: Hash + Eq + Copy + 'static, V: Copy + 'static> JRef<JHashMap<K, V>> {
    /// `new HashMap<>()`
    pub fn new_map() -> Self {
        JRef::new(JHashMap(HashMap::new()))
    }

    /// `m.put(k, v)`, its result unused (J2R makes it a statement).
    pub fn put(&self, k: K, v: V) {
        self.set(|m| m.0.insert(k, v));
    }

    pub fn getOrDefault(&self, k: K, default: V) -> V {
        self.get(|m| m.0.get(&k).copied().unwrap_or(default))
    }

    pub fn containsKey(&self, k: K) -> bool {
        self.get(|m| m.0.contains_key(&k))
    }

    pub fn size(&self) -> i32 {
        self.get(|m| m.0.len() as i32)
    }
}
