package biogenesis;

/**
 * ResourceManager manages the resources in the world.
 * Only this class can change resource amounts.
 */
public class ResourceManager {
    private Resource o2;
    private Resource co2;
    private Resource ch4;
    private Resource detritus;
    private Resource co1;

    public ResourceManager(double o2, double co2, double ch4, double detritus, double co1) {
        this.o2 = new Resource(Resource.Name.O2, o2);
        this.co2 = new Resource(Resource.Name.CO2, co2);
        this.ch4 = new Resource(Resource.Name.CH4, ch4);
        this.detritus = new Resource(Resource.Name.Detritus, detritus);
        this.co1 = new Resource(Resource.Name.CO1, co1);
    }

    public double convertCO2ToO2(double q) {
        return convert(co2, o2, q);
    }

    public double convertO2ToCO2(double q) {
        return convert(o2, co2, q);
    }

    public double convertO2ToCH4(double q) {
        return convert(o2, ch4, q);
    }

    public double convertO2ToCO1(double q) {
        return convert(o2, co1, q);
    }

    public double convertO2ToDetritus(double q) {
        return convert(o2, detritus, q);
    }

    public double convertCH4ToO2(double q) {
        return convert(ch4, o2, q);
    }

    public double convertCO1ToO2(double q) {
        return convert(co1, o2, q);
    }

    public double convertDetritusToO2(double q) {
        return convert(detritus, o2, q);
    }

    /*
     * Reactions turning CO2 and CH4 into each other, detritus into CO, and CO into
     * CO2.
     */
    public synchronized void reactions() {
        convertCO2ToCH4(Math.min(getCO2() / Utils.CO2_TO_CH4_DIVISOR, getCO2()));
        convertCH4ToCO2(Math.min(getCH4() / Utils.CH4_TO_CO2_DIVISOR, getCH4()));
        convertDetritusToCO1(Math.min(getDetritus() / Utils.DETRITUS_TO_CO1_DIVISOR, getDetritus()));
        if (getCO1() > getCO2()) {
            convertCO1ToCO2(Math.min((getCO1() + (getCO1() - getCO2())) / Utils.CO1_TO_CO2_DIVISOR, getCO1()));
        } else {
            convertCO1ToCO2(Math.min(getCO1() / Utils.CO1_TO_CO2_DIVISOR, getCO1()));
        }
    }

    public double getO2() {
        return o2.getAmount();
    }

    public double getCO2() {
        return co2.getAmount();
    }

    public double getCH4() {
        return ch4.getAmount();
    }

    public double getDetritus() {
        return detritus.getAmount();
    }

    public double getCO1() {
        return co1.getAmount();
    }

    public void addO2(double amount) {
        o2.add(amount);
    }

    public void addCO2(double amount) {
        co2.add(amount);
    }

    public void addCH4(double amount) {
        ch4.add(amount);
    }

    public void addDetritus(double amount) {
        detritus.add(amount);
    }

    public void addCO1(double amount) {
        co1.add(amount);
    }

    public void removeO2(double amount) {
        o2.remove(amount);
    }

    public void removeCO2(double amount) {
        co2.remove(amount);
    }

    public void removeCH4(double amount) {
        ch4.remove(amount);
    }

    public void removeDetritus(double amount) {
        detritus.remove(amount);
    }

    public void removeCO1(double amount) {
        co1.remove(amount);
    }

    private synchronized double convert(Resource from, Resource to, double amount) {
        amount = Math.min(amount, from.getAmount());
        from.remove(amount);
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
