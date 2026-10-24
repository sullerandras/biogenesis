package j2rtest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/** Collections and lambdas, the way World and OrganismBuckets use them. */
public class Bag {
	interface Matcher {
		boolean match(Item i);
	}

	Collection<Item> items = Collections.synchronizedSet(new HashSet<>());
	List<Double> weights = new ArrayList<>();
	double limit = 9;

	Item find(Matcher m) {
		for (Item i : items) {
			if (m.match(i)) return i;
		}
		return null;
	}

	double threshold() {
		return limit;
	}

	/** A block lambda that uses this. */
	Item heavy() {
		return find(i -> {
			if (i.w > threshold()) return true;
			return false;
		});
	}
}
