package j2rtest;

/** A HashSet element with its own hashCode and equals (as Organism has). */
public class Item {
	int id;
	double w;

	Item(int id, double w) {
		this.id = id;
		this.w = w;
	}

	@Override
	public int hashCode() {
		return id * 7919;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Item) {
			return id == ((Item) o).id;
		}
		return false;
	}
}
