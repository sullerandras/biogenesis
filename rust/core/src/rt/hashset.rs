//! `java.util.HashSet`, ported from the JDK's `HashMap` (Java 25) so that
//! iteration order is Java's: the same table size, resize split, bin order and
//! treeified bins (a bin of 8+ entries becomes a red-black tree, and treeifying
//! moves the root to the front of the bin). Nodes live in an arena and link by
//! index; `NIL` is Java's null.
//!
//! The one non-deterministic part of Java's HashMap, `tieBreakOrder` (equal
//! hash codes of non-Comparable keys in a tree bin, decided by
//! `System.identityHashCode`), throws: such a run could not be reproduced.

use super::collections::{JCollectionT, JObject};
use super::dump::{Dumper, JDump, JDumpTarget};
use super::throw;

const NIL: u32 = u32::MAX;
const DEFAULT_INITIAL_CAPACITY: i32 = 1 << 4;
const MAXIMUM_CAPACITY: i32 = 1 << 30;
const DEFAULT_LOAD_FACTOR: f32 = 0.75;
const TREEIFY_THRESHOLD: i32 = 8;
const UNTREEIFY_THRESHOLD: i32 = 6;
const MIN_TREEIFY_CAPACITY: usize = 64;

#[derive(Clone)]
struct Node<K> {
    hash: i32,
    key: Option<K>,
    next: u32,
    // TreeNode
    tree: bool,
    parent: u32,
    left: u32,
    right: u32,
    prev: u32,
    red: bool,
}

#[derive(Clone)]
pub struct JHashSet<K> {
    /// Empty: Java's `table == null`.
    table: Vec<u32>,
    nodes: Vec<Node<K>>,
    free: Vec<u32>,
    size: i32,
    threshold: i32,
    load_factor: f32,
}

impl<K> Default for JHashSet<K> {
    fn default() -> Self {
        JHashSet { table: Vec::new(), nodes: Vec::new(), free: Vec::new(), size: 0, threshold: 0, load_factor: DEFAULT_LOAD_FACTOR }
    }
}

fn hash_of<K: JObject>(key: &K) -> i32 {
    if key.is_null() {
        0
    } else {
        let h = key.hash_code();
        h ^ ((h as u32) >> 16) as i32
    }
}

/// `k == key || (key != null && key.equals(k))`
fn same_key<K: JObject>(key: &K, k: &K) -> bool {
    key.same(k) || (!key.is_null() && key.equals(k))
}

impl<K: JObject> JHashSet<K> {
    /// `new HashSet<>()`
    pub fn new() -> Self {
        Self::default()
    }

    /// `new HashSet<>(initialCapacity)`: the table is allocated on the first add.
    pub fn with_capacity(initial_capacity: i32) -> Self {
        if initial_capacity < 0 {
            throw("java.lang.IllegalArgumentException");
        }
        if initial_capacity == 0 {
            // tableSizeFor(0) is 1
            return JHashSet { threshold: 1, ..Self::default() };
        }
        let n = (u32::MAX >> (initial_capacity - 1).leading_zeros()) as i64;
        let threshold = if n >= MAXIMUM_CAPACITY as i64 { MAXIMUM_CAPACITY } else { (n + 1) as i32 };
        JHashSet { threshold, ..Self::default() }
    }

    /// The table length (0 before the first add), which a save records:
    /// iteration order depends on it, and a HashMap never shrinks.
    pub fn capacity(&self) -> i32 {
        self.table.len() as i32
    }

    /// True if any bin is a tree. Reloading such a set (adding its elements in
    /// iteration order) may not give the same order back.
    pub fn has_tree_bins(&self) -> bool {
        self.table.iter().any(|&i| i != NIL && self.n(i).tree)
    }

    fn n(&self, i: u32) -> &Node<K> {
        &self.nodes[i as usize]
    }

    fn m(&mut self, i: u32) -> &mut Node<K> {
        &mut self.nodes[i as usize]
    }

    fn key(&self, i: u32) -> &K {
        self.n(i).key.as_ref().unwrap()
    }

    fn new_node(&mut self, hash: i32, key: K, next: u32) -> u32 {
        let node = Node { hash, key: Some(key), next, tree: false, parent: NIL, left: NIL, right: NIL, prev: NIL, red: false };
        if let Some(i) = self.free.pop() {
            self.nodes[i as usize] = node;
            i
        } else {
            self.nodes.push(node);
            (self.nodes.len() - 1) as u32
        }
    }

    pub fn size(&self) -> i32 {
        self.size
    }

    pub fn contains(&self, key: &K) -> bool {
        self.get_node(key) != NIL
    }

    fn get_node(&self, key: &K) -> u32 {
        let n = self.table.len();
        if n == 0 {
            return NIL;
        }
        let hash = hash_of(key);
        let first = self.table[(n - 1) & hash as usize];
        if first == NIL {
            return NIL;
        }
        if self.n(first).hash == hash && same_key(key, self.key(first)) {
            return first;
        }
        let mut e = self.n(first).next;
        if e != NIL {
            if self.n(first).tree {
                return self.get_tree_node(first, hash, key);
            }
            loop {
                if self.n(e).hash == hash && same_key(key, self.key(e)) {
                    return e;
                }
                e = self.n(e).next;
                if e == NIL {
                    break;
                }
            }
        }
        NIL
    }

    /// `add(key)`: true if the set did not contain it.
    pub fn add(&mut self, key: K) -> bool {
        let hash = hash_of(&key);
        if self.table.is_empty() {
            self.resize();
        }
        let n = self.table.len();
        let i = (n - 1) & hash as usize;
        let p = self.table[i];
        if p == NIL {
            let x = self.new_node(hash, key, NIL);
            self.table[i] = x;
        } else {
            let e;
            if self.n(p).hash == hash && same_key(&key, self.key(p)) {
                e = p;
            } else if self.n(p).tree {
                e = self.put_tree_val(p, hash, key);
            } else {
                let mut p = p;
                let mut bin_count = 0;
                loop {
                    let nx = self.n(p).next;
                    if nx == NIL {
                        let x = self.new_node(hash, key, NIL);
                        self.m(p).next = x;
                        if bin_count >= TREEIFY_THRESHOLD - 1 {
                            self.treeify_bin(hash);
                        }
                        e = NIL;
                        break;
                    }
                    if self.n(nx).hash == hash && same_key(&key, self.key(nx)) {
                        e = nx;
                        break;
                    }
                    p = nx;
                    bin_count += 1;
                }
            }
            if e != NIL {
                return false; // existing mapping for key
            }
        }
        self.size += 1;
        if self.size > self.threshold {
            self.resize();
        }
        true
    }

    fn resize(&mut self) {
        let old_cap = self.table.len() as i32;
        let old_thr = self.threshold;
        let new_cap;
        let mut new_thr = 0;
        if old_cap > 0 {
            if old_cap >= MAXIMUM_CAPACITY {
                self.threshold = i32::MAX;
                return;
            }
            new_cap = old_cap << 1;
            if new_cap < MAXIMUM_CAPACITY && old_cap >= DEFAULT_INITIAL_CAPACITY {
                new_thr = old_thr << 1;
            }
        } else if old_thr > 0 {
            new_cap = old_thr;
        } else {
            new_cap = DEFAULT_INITIAL_CAPACITY;
            new_thr = (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY as f32) as i32;
        }
        if new_thr == 0 {
            let ft = new_cap as f32 * self.load_factor;
            new_thr = if new_cap < MAXIMUM_CAPACITY && ft < MAXIMUM_CAPACITY as f32 { ft as i32 } else { i32::MAX };
        }
        self.threshold = new_thr;
        let old_tab = std::mem::replace(&mut self.table, vec![NIL; new_cap as usize]);
        for j in 0..old_cap as usize {
            let mut e = old_tab[j];
            if e == NIL {
                continue;
            }
            if self.n(e).next == NIL {
                let h = self.n(e).hash;
                self.table[(h & (new_cap - 1)) as usize] = e;
            } else if self.n(e).tree {
                self.split(e, j, old_cap);
            } else {
                // preserve order
                let (mut lo_head, mut lo_tail, mut hi_head, mut hi_tail) = (NIL, NIL, NIL, NIL);
                loop {
                    let next = self.n(e).next;
                    if self.n(e).hash & old_cap == 0 {
                        if lo_tail == NIL {
                            lo_head = e;
                        } else {
                            self.m(lo_tail).next = e;
                        }
                        lo_tail = e;
                    } else {
                        if hi_tail == NIL {
                            hi_head = e;
                        } else {
                            self.m(hi_tail).next = e;
                        }
                        hi_tail = e;
                    }
                    e = next;
                    if e == NIL {
                        break;
                    }
                }
                if lo_tail != NIL {
                    self.m(lo_tail).next = NIL;
                    self.table[j] = lo_head;
                }
                if hi_tail != NIL {
                    self.m(hi_tail).next = NIL;
                    self.table[j + old_cap as usize] = hi_head;
                }
            }
        }
    }

    fn treeify_bin(&mut self, hash: i32) {
        let n = self.table.len();
        if n < MIN_TREEIFY_CAPACITY {
            self.resize();
            return;
        }
        let index = (n - 1) & hash as usize;
        let mut e = self.table[index];
        if e == NIL {
            return;
        }
        // replacementTreeNode: the same nodes, as tree nodes, in the same order
        let hd = e;
        let mut tl = NIL;
        loop {
            {
                let p = self.m(e);
                p.tree = true;
                p.parent = NIL;
                p.left = NIL;
                p.right = NIL;
                p.red = false;
                p.prev = tl;
            }
            tl = e;
            e = self.n(e).next;
            if e == NIL {
                break;
            }
        }
        self.table[index] = hd;
        self.treeify(hd);
    }

    /// `remove(key)`: true if the set contained it.
    pub fn remove(&mut self, key: &K) -> bool {
        let hash = hash_of(key);
        let n = self.table.len();
        if n == 0 {
            return false;
        }
        let index = (n - 1) & hash as usize;
        let mut p = self.table[index];
        if p == NIL {
            return false;
        }
        let mut node = NIL;
        if self.n(p).hash == hash && same_key(key, self.key(p)) {
            node = p;
        } else {
            let mut e = self.n(p).next;
            if e != NIL {
                if self.n(p).tree {
                    node = self.get_tree_node(p, hash, key);
                } else {
                    loop {
                        if self.n(e).hash == hash && same_key(key, self.key(e)) {
                            node = e;
                            break;
                        }
                        p = e;
                        e = self.n(e).next;
                        if e == NIL {
                            break;
                        }
                    }
                }
            }
        }
        if node == NIL {
            return false;
        }
        if self.n(node).tree {
            self.remove_tree_node(node, true);
        } else if node == p {
            self.table[index] = self.n(node).next;
        } else {
            let nx = self.n(node).next;
            self.m(p).next = nx;
        }
        self.size -= 1;
        self.m(node).key = None;
        self.free.push(node);
        true
    }

    pub fn clear(&mut self) {
        if !self.table.is_empty() && self.size > 0 {
            self.size = 0;
            for t in self.table.iter_mut() {
                *t = NIL;
            }
            self.nodes.clear();
            self.free.clear();
        }
    }

    /// The elements in iteration order.
    pub fn snapshot(&self) -> Vec<K> {
        let mut v = Vec::with_capacity(self.size as usize);
        for &first in &self.table {
            let mut e = first;
            while e != NIL {
                v.push(self.key(e).clone());
                e = self.n(e).next;
            }
        }
        v
    }

    // ------------------------------------------------------------ TreeNode

    fn root(&self, mut r: u32) -> u32 {
        loop {
            let p = self.n(r).parent;
            if p == NIL {
                return r;
            }
            r = p;
        }
    }

    fn move_root_to_front(&mut self, root: u32) {
        let n = self.table.len();
        if root == NIL || n == 0 {
            return;
        }
        let index = (n - 1) & self.n(root).hash as usize;
        let first = self.table[index];
        if root != first {
            self.table[index] = root;
            let rp = self.n(root).prev;
            let rn = self.n(root).next;
            if rn != NIL {
                self.m(rn).prev = rp;
            }
            if rp != NIL {
                self.m(rp).next = rn;
            }
            if first != NIL {
                self.m(first).prev = root;
            }
            self.m(root).next = first;
            self.m(root).prev = NIL;
        }
    }

    /// Java's `find(h, k, kc)`; `kc` (comparableClassFor) is whether the key is Comparable.
    fn find(&self, mut p: u32, h: i32, k: &K) -> u32 {
        loop {
            let (ph, pl, pr) = (self.n(p).hash, self.n(p).left, self.n(p).right);
            if ph > h {
                p = pl;
            } else if ph < h {
                p = pr;
            } else if same_key(k, self.key(p)) {
                return p;
            } else if pl == NIL {
                p = pr;
            } else if pr == NIL {
                p = pl;
            } else if let Some(dir) = k.compare_to(self.key(p)).filter(|&d| d != 0) {
                p = if dir < 0 { pl } else { pr };
            } else {
                let q = self.find(pr, h, k);
                if q != NIL {
                    return q;
                }
                p = pl;
            }
            if p == NIL {
                return NIL;
            }
        }
    }

    fn get_tree_node(&self, this: u32, h: i32, k: &K) -> u32 {
        let r = if self.n(this).parent != NIL { self.root(this) } else { this };
        self.find(r, h, k)
    }

    fn tie_break_order() -> i32 {
        throw("J2R: HashMap tieBreakOrder (equal hash codes in a tree bin) uses System.identityHashCode")
    }

    fn treeify(&mut self, this: u32) {
        let mut root = NIL;
        let mut x = this;
        while x != NIL {
            let next = self.n(x).next;
            self.m(x).left = NIL;
            self.m(x).right = NIL;
            if root == NIL {
                self.m(x).parent = NIL;
                self.m(x).red = false;
                root = x;
            } else {
                let h = self.n(x).hash;
                let mut p = root;
                loop {
                    let ph = self.n(p).hash;
                    let dir = if ph > h {
                        -1
                    } else if ph < h {
                        1
                    } else {
                        match self.key(x).compare_to(self.key(p)) {
                            Some(d) if d != 0 => d,
                            _ => Self::tie_break_order(),
                        }
                    };
                    let xp = p;
                    p = if dir <= 0 { self.n(p).left } else { self.n(p).right };
                    if p == NIL {
                        self.m(x).parent = xp;
                        if dir <= 0 {
                            self.m(xp).left = x;
                        } else {
                            self.m(xp).right = x;
                        }
                        root = self.balance_insertion(root, x);
                        break;
                    }
                }
            }
            x = next;
        }
        self.move_root_to_front(root);
    }

    /// The bin starting at `this` as plain nodes (same order).
    fn untreeify(&mut self, this: u32) -> u32 {
        let mut q = this;
        while q != NIL {
            let n = self.m(q);
            n.tree = false;
            n.parent = NIL;
            n.left = NIL;
            n.right = NIL;
            n.prev = NIL;
            n.red = false;
            q = n.next;
        }
        this
    }

    /// Returns the existing node, or NIL after inserting.
    fn put_tree_val(&mut self, this: u32, h: i32, k: K) -> u32 {
        let mut searched = false;
        let root = if self.n(this).parent != NIL { self.root(this) } else { this };
        let mut p = root;
        loop {
            let ph = self.n(p).hash;
            let dir;
            if ph > h {
                dir = -1;
            } else if ph < h {
                dir = 1;
            } else if same_key(&k, self.key(p)) {
                return p;
            } else {
                match k.compare_to(self.key(p)) {
                    Some(d) if d != 0 => dir = d,
                    _ => {
                        if !searched {
                            searched = true;
                            let (l, r) = (self.n(p).left, self.n(p).right);
                            if l != NIL {
                                let q = self.find(l, h, &k);
                                if q != NIL {
                                    return q;
                                }
                            }
                            if r != NIL {
                                let q = self.find(r, h, &k);
                                if q != NIL {
                                    return q;
                                }
                            }
                        }
                        dir = Self::tie_break_order();
                    }
                }
            }
            let xp = p;
            p = if dir <= 0 { self.n(p).left } else { self.n(p).right };
            if p == NIL {
                let xpn = self.n(xp).next;
                let x = self.new_node(h, k, xpn);
                self.m(x).tree = true;
                if dir <= 0 {
                    self.m(xp).left = x;
                } else {
                    self.m(xp).right = x;
                }
                self.m(xp).next = x;
                self.m(x).parent = xp;
                self.m(x).prev = xp;
                if xpn != NIL {
                    self.m(xpn).prev = x;
                }
                let r = self.balance_insertion(root, x);
                self.move_root_to_front(r);
                return NIL;
            }
        }
    }

    fn remove_tree_node(&mut self, this: u32, movable: bool) {
        let n = self.table.len();
        if n == 0 {
            return;
        }
        let index = (n - 1) & self.n(this).hash as usize;
        let mut first = self.table[index];
        let mut root = first;
        let succ = self.n(this).next;
        let pred = self.n(this).prev;
        if pred == NIL {
            self.table[index] = succ;
            first = succ;
        } else {
            self.m(pred).next = succ;
        }
        if succ != NIL {
            self.m(succ).prev = pred;
        }
        if first == NIL {
            return;
        }
        if self.n(root).parent != NIL {
            root = self.root(root);
        }
        if root == NIL || (movable && (self.n(root).right == NIL || self.n(root).left == NIL || self.n(self.n(root).left).left == NIL)) {
            self.table[index] = self.untreeify(first); // too small
            return;
        }
        let p = this;
        let (pl, pr) = (self.n(p).left, self.n(p).right);
        let replacement;
        if pl != NIL && pr != NIL {
            let mut s = pr;
            loop {
                let sl = self.n(s).left;
                if sl == NIL {
                    break;
                }
                s = sl; // find successor
            }
            let c = self.n(s).red;
            self.m(s).red = self.n(p).red;
            self.m(p).red = c; // swap colors
            let sr = self.n(s).right;
            let pp = self.n(p).parent;
            if s == pr {
                // p was s's direct parent
                self.m(p).parent = s;
                self.m(s).right = p;
            } else {
                let sp = self.n(s).parent;
                self.m(p).parent = sp;
                if sp != NIL {
                    if s == self.n(sp).left {
                        self.m(sp).left = p;
                    } else {
                        self.m(sp).right = p;
                    }
                }
                self.m(s).right = pr;
                if pr != NIL {
                    self.m(pr).parent = s;
                }
            }
            self.m(p).left = NIL;
            self.m(p).right = sr;
            if sr != NIL {
                self.m(sr).parent = p;
            }
            self.m(s).left = pl;
            if pl != NIL {
                self.m(pl).parent = s;
            }
            self.m(s).parent = pp;
            if pp == NIL {
                root = s;
            } else if p == self.n(pp).left {
                self.m(pp).left = s;
            } else {
                self.m(pp).right = s;
            }
            replacement = if sr != NIL { sr } else { p };
        } else if pl != NIL {
            replacement = pl;
        } else if pr != NIL {
            replacement = pr;
        } else {
            replacement = p;
        }
        if replacement != p {
            let pp = self.n(p).parent;
            self.m(replacement).parent = pp;
            if pp == NIL {
                root = replacement;
                self.m(root).red = false;
            } else if p == self.n(pp).left {
                self.m(pp).left = replacement;
            } else {
                self.m(pp).right = replacement;
            }
            self.m(p).left = NIL;
            self.m(p).right = NIL;
            self.m(p).parent = NIL;
        }
        let r = if self.n(p).red { root } else { self.balance_deletion(root, replacement) };
        if replacement == p {
            // detach
            let pp = self.n(p).parent;
            self.m(p).parent = NIL;
            if pp != NIL {
                if p == self.n(pp).left {
                    self.m(pp).left = NIL;
                } else if p == self.n(pp).right {
                    self.m(pp).right = NIL;
                }
            }
        }
        if movable {
            self.move_root_to_front(r);
        }
    }

    fn split(&mut self, this: u32, index: usize, bit: i32) {
        let (mut lo_head, mut lo_tail, mut hi_head, mut hi_tail) = (NIL, NIL, NIL, NIL);
        let (mut lc, mut hc) = (0, 0);
        let mut e = this;
        while e != NIL {
            let next = self.n(e).next;
            self.m(e).next = NIL;
            if self.n(e).hash & bit == 0 {
                self.m(e).prev = lo_tail;
                if lo_tail == NIL {
                    lo_head = e;
                } else {
                    self.m(lo_tail).next = e;
                }
                lo_tail = e;
                lc += 1;
            } else {
                self.m(e).prev = hi_tail;
                if hi_tail == NIL {
                    hi_head = e;
                } else {
                    self.m(hi_tail).next = e;
                }
                hi_tail = e;
                hc += 1;
            }
            e = next;
        }
        if lo_head != NIL {
            if lc <= UNTREEIFY_THRESHOLD {
                self.table[index] = self.untreeify(lo_head);
            } else {
                self.table[index] = lo_head;
                if hi_head != NIL {
                    self.treeify(lo_head);
                }
            }
        }
        if hi_head != NIL {
            let hi_index = index + bit as usize;
            if hc <= UNTREEIFY_THRESHOLD {
                self.table[hi_index] = self.untreeify(hi_head);
            } else {
                self.table[hi_index] = hi_head;
                if lo_head != NIL {
                    self.treeify(hi_head);
                }
            }
        }
    }

    fn red(&self, i: u32) -> bool {
        i != NIL && self.n(i).red
    }

    fn rotate_left(&mut self, mut root: u32, p: u32) -> u32 {
        if p != NIL {
            let r = self.n(p).right;
            if r != NIL {
                let rl = self.n(r).left;
                self.m(p).right = rl;
                if rl != NIL {
                    self.m(rl).parent = p;
                }
                let pp = self.n(p).parent;
                self.m(r).parent = pp;
                if pp == NIL {
                    root = r;
                    self.m(r).red = false;
                } else if self.n(pp).left == p {
                    self.m(pp).left = r;
                } else {
                    self.m(pp).right = r;
                }
                self.m(r).left = p;
                self.m(p).parent = r;
            }
        }
        root
    }

    fn rotate_right(&mut self, mut root: u32, p: u32) -> u32 {
        if p != NIL {
            let l = self.n(p).left;
            if l != NIL {
                let lr = self.n(l).right;
                self.m(p).left = lr;
                if lr != NIL {
                    self.m(lr).parent = p;
                }
                let pp = self.n(p).parent;
                self.m(l).parent = pp;
                if pp == NIL {
                    root = l;
                    self.m(l).red = false;
                } else if self.n(pp).right == p {
                    self.m(pp).right = l;
                } else {
                    self.m(pp).left = l;
                }
                self.m(l).right = p;
                self.m(p).parent = l;
            }
        }
        root
    }

    fn balance_insertion(&mut self, mut root: u32, mut x: u32) -> u32 {
        self.m(x).red = true;
        loop {
            let mut xp = self.n(x).parent;
            if xp == NIL {
                self.m(x).red = false;
                return x;
            }
            let mut xpp = self.n(xp).parent;
            if !self.n(xp).red || xpp == NIL {
                return root;
            }
            let xppl = self.n(xpp).left;
            if xp == xppl {
                let xppr = self.n(xpp).right;
                if self.red(xppr) {
                    self.m(xppr).red = false;
                    self.m(xp).red = false;
                    self.m(xpp).red = true;
                    x = xpp;
                } else {
                    if x == self.n(xp).right {
                        x = xp;
                        root = self.rotate_left(root, x);
                        xp = self.n(x).parent;
                        xpp = if xp == NIL { NIL } else { self.n(xp).parent };
                    }
                    if xp != NIL {
                        self.m(xp).red = false;
                        if xpp != NIL {
                            self.m(xpp).red = true;
                            root = self.rotate_right(root, xpp);
                        }
                    }
                }
            } else if self.red(xppl) {
                self.m(xppl).red = false;
                self.m(xp).red = false;
                self.m(xpp).red = true;
                x = xpp;
            } else {
                if x == self.n(xp).left {
                    x = xp;
                    root = self.rotate_right(root, x);
                    xp = self.n(x).parent;
                    xpp = if xp == NIL { NIL } else { self.n(xp).parent };
                }
                if xp != NIL {
                    self.m(xp).red = false;
                    if xpp != NIL {
                        self.m(xpp).red = true;
                        root = self.rotate_left(root, xpp);
                    }
                }
            }
        }
    }

    fn balance_deletion(&mut self, mut root: u32, mut x: u32) -> u32 {
        loop {
            if x == NIL || x == root {
                return root;
            }
            let mut xp = self.n(x).parent;
            if xp == NIL {
                self.m(x).red = false;
                return x;
            } else if self.n(x).red {
                self.m(x).red = false;
                return root;
            }
            let mut xpl = self.n(xp).left;
            if xpl == x {
                let mut xpr = self.n(xp).right;
                if self.red(xpr) {
                    self.m(xpr).red = false;
                    self.m(xp).red = true;
                    root = self.rotate_left(root, xp);
                    xp = self.n(x).parent;
                    xpr = if xp == NIL { NIL } else { self.n(xp).right };
                }
                if xpr == NIL {
                    x = xp;
                } else {
                    let sl = self.n(xpr).left;
                    let mut sr = self.n(xpr).right;
                    if !self.red(sr) && !self.red(sl) {
                        self.m(xpr).red = true;
                        x = xp;
                    } else {
                        if !self.red(sr) {
                            if sl != NIL {
                                self.m(sl).red = false;
                            }
                            self.m(xpr).red = true;
                            root = self.rotate_right(root, xpr);
                            xp = self.n(x).parent;
                            xpr = if xp == NIL { NIL } else { self.n(xp).right };
                        }
                        if xpr != NIL {
                            self.m(xpr).red = if xp == NIL { false } else { self.n(xp).red };
                            sr = self.n(xpr).right;
                            if sr != NIL {
                                self.m(sr).red = false;
                            }
                        }
                        if xp != NIL {
                            self.m(xp).red = false;
                            root = self.rotate_left(root, xp);
                        }
                        x = root;
                    }
                }
            } else {
                // symmetric
                if self.red(xpl) {
                    self.m(xpl).red = false;
                    self.m(xp).red = true;
                    root = self.rotate_right(root, xp);
                    xp = self.n(x).parent;
                    xpl = if xp == NIL { NIL } else { self.n(xp).left };
                }
                if xpl == NIL {
                    x = xp;
                } else {
                    let mut sl = self.n(xpl).left;
                    let sr = self.n(xpl).right;
                    if !self.red(sl) && !self.red(sr) {
                        self.m(xpl).red = true;
                        x = xp;
                    } else {
                        if !self.red(sl) {
                            if sr != NIL {
                                self.m(sr).red = false;
                            }
                            self.m(xpl).red = true;
                            root = self.rotate_left(root, xpl);
                            xp = self.n(x).parent;
                            xpl = if xp == NIL { NIL } else { self.n(xp).left };
                        }
                        if xpl != NIL {
                            self.m(xpl).red = if xp == NIL { false } else { self.n(xp).red };
                            sl = self.n(xpl).left;
                            if sl != NIL {
                                self.m(sl).red = false;
                            }
                        }
                        if xp != NIL {
                            self.m(xp).red = false;
                            root = self.rotate_right(root, xp);
                        }
                        x = root;
                    }
                }
            }
        }
    }
}

impl<K: JObject + JDump + 'static> JCollectionT<K> for JHashSet<K> {
    fn size(&self) -> i32 {
        JHashSet::size(self)
    }
    fn add(&mut self, x: K) -> bool {
        JHashSet::add(self, x)
    }
    fn remove(&mut self, x: &K) -> bool {
        JHashSet::remove(self, x)
    }
    fn contains(&self, x: &K) -> bool {
        JHashSet::contains(self, x)
    }
    fn clear(&mut self) {
        JHashSet::clear(self)
    }
    fn snapshot(&self) -> Vec<K> {
        JHashSet::snapshot(self)
    }
}

/// Like a list, plus the capacity: `[#id n HashSet(cap)`.
impl<K: JObject + JDump> JDumpTarget for JHashSet<K> {
    fn dump_target(&self, d: &mut Dumper, id: usize) {
        let items = self.snapshot();
        if self.has_tree_bins() {
            d.warnings.push(format!("HashSet #{id} has a tree bin: a game loaded from this save may iterate it in another order"));
        }
        d.sequence(id, items.len(), &format!(" HashSet({})", self.capacity()), |d| {
            for x in &items {
                d.element(x);
            }
        });
    }
}
