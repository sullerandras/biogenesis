package biogenesis;

/**
 * Resources keeps track of the resourses in one region.
 * This class is not thread-safe, it is expected that ResourceManager will
 * synchronize access to this class.
 */
public class ResourceRegion implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Resource o2;
    private Resource co2;
    private Resource ch4;
    private Resource detritus;
    private Resource co1;

    /**
     * Creates a new resource region with the given amounts of resources.
     */
    public ResourceRegion(double o2, double co2, double ch4, double detritus, double co1) {
        this.o2 = new Resource(o2);
        this.co2 = new Resource(co2);
        this.ch4 = new Resource(ch4);
        this.detritus = new Resource(detritus);
        this.co1 = new Resource(co1);
    }

    /**
     * Converts the given amount of CO2 into O2. It may convert less than the given
     * amount if there isn't enough CO2.
     *
     * @param q The amount of CO2 to convert.
     * @return The amount of CO2 converted. Might be less than the given amount.
     */
    public double convertCO2ToO2(double q) {
        return convert(co2, o2, q);
    }

    /**
     * Converts the given amount of O2 into CO2. It may convert less than the given
     * amount if there isn't enough O2.
     *
     * @param q The amount of O2 to convert.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public double convertO2ToCO2(double q) {
        return convert(o2, co2, q);
    }

    /**
     * Converts the given amount of O2 into CH4. It may convert less than the given
     * amount if there isn't enough O2.
     *
     * @param q The amount of O2 to convert.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public double convertO2ToCH4(double q) {
        return convert(o2, ch4, q);
    }

    /**
     * Converts the given amount of O2 into CO1. It may convert less than the given
     * amount if there isn't enough O2.
     *
     * @param q The amount of O2 to convert.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public double convertO2ToCO1(double q) {
        return convert(o2, co1, q);
    }

    /**
     * Converts the given amount of O2 into Detritus. It may convert less than the
     * given amount if there isn't enough O2.
     *
     * @param q The amount of O2 to convert.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public double convertO2ToDetritus(double q) {
        return convert(o2, detritus, q);
    }

    /**
     * Converts the given amount of CH4 into O2. It may convert less than the given
     * amount if there isn't enough CH4.
     *
     * @param q The amount of CH4 to convert.
     * @return The amount of CH4 converted. Might be less than the given amount.
     */
    public double convertCH4ToO2(double q) {
        return convert(ch4, o2, q);
    }

    /**
     * Converts the given amount of CO1 into O2. It may convert less than the given
     * amount if there isn't enough CO1.
     *
     * @param q The amount of CO1 to convert.
     * @return The amount of CO1 converted. Might be less than the given amount.
     */
    public double convertCO1ToO2(double q) {
        return convert(co1, o2, q);
    }

    /**
     * Converts the given amount of Detritus into O2. It may convert less than the
     * given amount if there isn't enough Detritus.
     *
     * @param q The amount of Detritus to convert.
     * @return The amount of Detritus converted. Might be less than the given
     */
    public double convertDetritusToO2(double q) {
        return convert(detritus, o2, q);
    }

    /*
     * Reactions turning CO2 and CH4 into each other, detritus into CO, and CO into
     * CO2.
     */
    public void reactions() {
        convertCO2ToCH4(Math.min(getCO2() / Utils.CO2_TO_CH4_DIVISOR, getCO2()));
        convertCH4ToCO2(Math.min(getCH4() / Utils.CH4_TO_CO2_DIVISOR, getCH4()));
        convertDetritusToCO1(Math.min(getDetritus() / Utils.DETRITUS_TO_CO1_DIVISOR, getDetritus()));
        if (getCO1() > getCO2()) {
            convertCO1ToCO2(Math.min((getCO1() + (getCO1() - getCO2())) / Utils.CO1_TO_CO2_DIVISOR, getCO1()));
        } else {
            convertCO1ToCO2(Math.min(getCO1() / Utils.CO1_TO_CO2_DIVISOR, getCO1()));
        }
    }

    /**
     * Returns the amount of O2 in this region.
     */
    public double getO2() {
        return o2.getAmount();
    }

    /**
     * Returns the amount of CO2 in this region.
     */
    public double getCO2() {
        return co2.getAmount();
    }

    /**
     * Returns the amount of CH4 in this region.
     */
    public double getCH4() {
        return ch4.getAmount();
    }

    /**
     * Returns the amount of Detritus in this region.
     */
    public double getDetritus() {
        return detritus.getAmount();
    }

    /**
     * Returns the amount of CO1 in this region.
     */
    public double getCO1() {
        return co1.getAmount();
    }

    /**
     * Adds the given amount of CO2 to this region.
     */
    public void addCO2(double amount) {
        co2.add(amount);
    }

    /**
     * Adds the given amount of CH4 to this region.
     */
    public void addCH4(double amount) {
        ch4.add(amount);
    }

    /**
     * Adds the given amount of Detritus to this region.
     */
    public void addDetritus(double amount) {
        detritus.add(amount);
    }

    /**
     * Adds the given amount of CO1 to this region.
     */
    public void addCO1(double amount) {
        co1.add(amount);
    }

    /**
     * Removes the given amount of CO2 from this region. It may remove less than the
     * given amount if there isn't enough CO2.
     *
     * @param amount The amount of CO2 to remove.
     * @return The amount of CO2 removed. Might be less than the given amount.
     */
    public double removeCO2(double amount) {
        return co2.remove(amount);
    }

    /**
     * Removes the given amount of CH4 from this region. It may remove less than the
     * given amount if there isn't enough CH4.
     *
     * @param amount The amount of CH4 to remove.
     * @return The amount of CH4 removed. Might be less than the given amount.
     */
    public double removeCH4(double amount) {
        return ch4.remove(amount);
    }

    /**
     * Removes the given amount of detritus from this region. It may remove less
     * than the
     * given amount if there isn't enough detritus.
     *
     * @param amount The amount of detritus to remove.
     * @return The amount of detritus removed. Might be less than the given amount.
     */
    public double removeDetritus(double amount) {
        return detritus.remove(amount);
    }

    /**
     * Removes the given amount of CO1 from this region. It may remove less than the
     * given amount if there isn't enough CO1.
     *
     * @param amount The amount of CO1 to remove.
     * @return The amount of CO1 removed. Might be less than the given amount.
     */
    public double removeCO1(double amount) {
        return co1.remove(amount);
    }

    /**
     * Converts the given amount of resource from one resource to another.
     *
     * @param from   The resource to convert from.
     * @param to     The resource to convert to.
     * @param amount The amount of resource to convert.
     * @return The amount of resource converted. Might be less than the given
     *         amount.
     */
    private double convert(Resource from, Resource to, double amount) {
        amount = from.remove(amount);
        to.add(amount);
        return amount;
    }

    private double convertCO2ToCH4(double q) {
        return convert(co2, ch4, q);
    }

    private double convertCH4ToCO2(double q) {
        return convert(ch4, co2, q);
    }

    private double convertDetritusToCO1(double q) {
        return convert(detritus, co1, q);
    }

    private double convertCO1ToCO2(double q) {
        return convert(co1, co2, q);
    }
}
