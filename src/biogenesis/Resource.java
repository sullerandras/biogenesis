package biogenesis;

/**
 * Resource represents a resource in the world (CO2, O2, CH4, Detritus, CO1, etc).
 * It's an abstraction so we can freely change resources to be localized in space.
 * Only to be used by ResourceManager.
 */
public class Resource {
    /**
     * The name of the resource.
     */
    public static enum Name {
        CO2, O2, CH4, Detritus, CO1
    }

    private Name name;
    private double amount;

    /**
     * Creates a new resource with the given name and amount.
     * @param name The name of the resource.
     * @param amount The amount of the resource.
     */
    public Resource(Name name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    public Name getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public void add(double amount) {
        this.amount += amount;
    }

    /**
     * Removes the given amount of resource from this resource. It may remove less
     * than the given amount if there isn't enough resource.
     * @param amount The amount of resource to remove. It might remove less than this.
     */
    public void remove(double amount) {
        this.amount -= Math.min(amount, this.amount);
    }

    @Override
    public String toString() {
        return name + ": " + amount;
    }
}
