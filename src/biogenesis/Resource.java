package biogenesis;

/**
 * Resource represents a resource in the world (CO2, O2, CH4, Detritus, CO1,
 * etc).
 * It's an abstraction so we can freely change resources to be localized in
 * space.
 * Only to be used by ResourceManager.
 * This class is not thread-safe.
 */
public class Resource implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private double amount;

    /**
     * Creates a new resource with the given name and amount.
     *
     * @param amount The amount of the resource.
     */
    public Resource(double amount) {
        this.amount = amount;
    }

    /**
     * Returns the amount of resource.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Adds the given amount of resource to this resource.
     */
    public void add(double amount) {
        this.amount += amount;
    }

    /**
     * Removes the given amount of resource from this resource. It may remove less
     * than the given amount if there isn't enough resource.
     *
     * @param amount The amount of resource to remove. It might remove less than
     *               this.
     */
    public double remove(double amount) {
        amount = Math.min(amount, this.amount);
        this.amount -= amount;
        return amount;
    }
}
