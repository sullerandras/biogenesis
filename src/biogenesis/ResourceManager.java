package biogenesis;

import java.awt.Color;

/**
 * ResourceManager manages the resources in the world.
 * Only this class can change resource amounts.
 * This class is thread-safe.
 */
public class ResourceManager implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private final int regionSize;
    private final int maxWidth;
    private final int maxHeight;
    private final int totalRegions;
    private final ResourceRegion[][] regions;

    private double o2;
    private double co2;
    private double ch4;
    private double detritus;
    private double co1;

    /**
     * Creates a new resource manager with the given amounts of resources.
     *
     * @param o2        The amount of O2.
     * @param co2       The amount of CO2.
     * @param ch4       The amount of CH4.
     * @param detritus  The amount of Detritus.
     * @param co1       The amount of CO1.
     * @param mapWidth  The width of the map.
     * @param mapHeight The height of the map.
     */
    public ResourceManager(double o2, double co2, double ch4, double detritus, double co1,
            int mapWidth, int mapHeight) {
        this.regionSize = Math.min(mapWidth / 10, mapHeight / 10);
        this.maxWidth = mapWidth / regionSize - 1;
        this.maxHeight = mapHeight / regionSize - 1;
        this.totalRegions = (maxWidth + 1) * (maxHeight + 1);
        this.regions = new ResourceRegion[maxHeight + 1][maxWidth + 1];
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                regions[y][x] = new ResourceRegion(
                        o2 / totalRegions,
                        co2 / totalRegions,
                        ch4 / totalRegions,
                        detritus / totalRegions,
                        co1 / totalRegions);
            }
        }

        this.o2 = o2;
        this.co2 = co2;
        this.ch4 = ch4;
        this.detritus = detritus;
        this.co1 = co1;
    }

    /**
     * Converts the given amount of CO2 into O2. It may convert less than the given
     * amount if there isn't enough CO2 in the region around the given coordinates.
     *
     * @param q The amount of CO2 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of CO2 converted. Might be less than the given amount.
     */
    public synchronized double convertCO2ToO2(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertCO2ToO2(q);
        co2 -= q;
        o2 += q;
        return q;
    }

    /**
     * Converts the given amount of O2 into CO2. It may convert less than the given
     * amount if there isn't enough O2 in the region around the given coordinates.
     *
     * @param q The amount of O2 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public synchronized double convertO2ToCO2(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertO2ToCO2(q);
        o2 -= q;
        co2 += q;
        return q;
    }

    /**
     * Converts the given amount of O2 into CH4. It may convert less than the given
     * amount if there isn't enough O2 in the region around the given coordinates.
     *
     * @param q The amount of O2 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public synchronized double convertO2ToCH4(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertO2ToCH4(q);
        o2 -= q;
        ch4 += q;
        return q;
    }

    /**
     * Converts the given amount of O2 into CO1. It may convert less than the given
     * amount if there isn't enough O2 in the region around the given coordinates.
     *
     * @param q The amount of O2 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public synchronized double convertO2ToCO1(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertO2ToCO1(q);
        o2 -= q;
        co1 += q;
        return q;
    }

    /**
     * Converts the given amount of O2 into Detritus. It may convert less than the
     * given amount if there isn't enough O2 in the region around the given
     * coordinates.
     *
     * @param q The amount of O2 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of O2 converted. Might be less than the given amount.
     */
    public synchronized double convertO2ToDetritus(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertO2ToDetritus(q);
        o2 -= q;
        detritus += q;
        return q;
    }

    /**
     * Converts the given amount of CH4 into O2. It may convert less than the given
     * amount if there isn't enough CH4 in the region around the given coordinates.
     *
     * @param q The amount of CH4 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of CH4 converted. Might be less than the given amount.
     */
    public synchronized double convertCH4ToO2(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertCH4ToO2(q);
        ch4 -= q;
        o2 += q;
        return q;
    }

    /**
     * Converts the given amount of CO1 into O2. It may convert less than the given
     * amount if there isn't enough CO1 in the region around the given coordinates.
     *
     * @param q The amount of CO1 to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of CO1 converted. Might be less than the given amount.
     */
    public synchronized double convertCO1ToO2(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertCO1ToO2(q);
        co1 -= q;
        o2 += q;
        return q;
    }

    /**
     * Converts the given amount of Detritus into O2. It may convert less than the
     * given amount if there isn't enough Detritus in the region around the given
     * coordinates.
     *
     * @param q The amount of Detritus to convert.
     * @param x The x coordinate of the organism.
     * @param y The y coordinate of the organism.
     * @return The amount of Detritus converted. Might be less than the given
     */
    public synchronized double convertDetritusToO2(double q, int x, int y) {
        q = regions[y / regionSize][x / regionSize].convertDetritusToO2(q);
        detritus -= q;
        o2 += q;
        return q;
    }

    /*
     * Reactions turning CO2 and CH4 into each other, detritus into CO, and CO into
     * CO2.
     * It calls the reactions() method of each region.
     */
    public synchronized void reactions() {
        double o2 = 0;
        double co2 = 0;
        double ch4 = 0;
        double detritus = 0;
        double co1 = 0;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                ResourceRegion region = regions[y][x];
                region.reactions();
                o2 += region.getO2();
                co2 += region.getCO2();
                ch4 += region.getCH4();
                detritus += region.getDetritus();
                co1 += region.getCO1();
            }
        }
        this.o2 = o2;
        this.co2 = co2;
        this.ch4 = ch4;
        this.detritus = detritus;
        this.co1 = co1;
    }

    /**
     * Diffuses the resources across the world.
     * Swaps a small portion of resources between adjacent regions.
     *
     * Not a good implementation, resources can spread to the right and bottom quickly, while they spread to the left and top slowly.
     */
    public synchronized void diffusion() {
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                ResourceRegion region = regions[y][x];
                if (x < maxWidth) {
                    region.swapResources(regions[y][x + 1]);
                }
                if (y < maxHeight) {
                    region.swapResources(regions[y + 1][x]);
                }
            }
        }
    }

    /**
     * Returns the total amount of O2 in the world.
     */
    public double getTotalO2() {
        return o2;
    }

    /**
     * Returns the total amount of CO2 in the world.
     */
    public double getTotalCO2() {
        return co2;
    }

    /**
     * Returns the total amount of CH4 in the world.
     */
    public double getTotalCH4() {
        return ch4;
    }

    /**
     * Returns the total amount of Detritus in the world.
     */
    public double getTotalDetritus() {
        return detritus;
    }

    /**
     * Returns the total amount of CO1 in the world.
     */
    public double getTotalCO1() {
        return co1;
    }

    /**
     * Returns the amount of O2 in the region around the given coordinates.
     */
    public double getO2(int x, int y) {
        return regions[y / regionSize][x / regionSize].getO2();
    }

    /**
     * Returns the amount of CO2 in the region around the given coordinates.
     */
    public double getCO2(int x, int y) {
        return regions[y / regionSize][x / regionSize].getCO2();
    }

    /**
     * Returns the amount of CH4 in the region around the given coordinates.
     */
    public double getCH4(int x, int y) {
        return regions[y / regionSize][x / regionSize].getCH4();
    }

    /**
     * Returns the amount of Detritus in the region around the given coordinates.
     */
    public double getDetritus(int x, int y) {
        return regions[y / regionSize][x / regionSize].getDetritus();
    }

    /**
     * Returns the amount of CO1 in the region around the given coordinates.
     */
    public double getCO1(int x, int y) {
        return regions[y / regionSize][x / regionSize].getCO1();
    }

    /**
     * Adds the given amount of CO2 to the world. It distributes it across all
     * regions.
     */
    public synchronized void addCO2(double amount) {
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                regions[y][x].addCO2(amountPerRegion);
            }
        }
        co2 += amount;
    }

    /**
     * Adds the given amount of CH4 to the world. It distributes it across all
     * regions.
     */
    public synchronized void addCH4(double amount) {
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                regions[y][x].addCH4(amountPerRegion);
            }
        }
        ch4 += amount;
    }

    /**
     * Adds the given amount of Detritus to the world. It distributes it across all
     * regions.
     */
    public synchronized void addDetritus(double amount) {
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                regions[y][x].addDetritus(amountPerRegion);
            }
        }
        detritus += amount;
    }

    /**
     * Adds the given amount of CO1 to the world. It distributes it across all
     * regions.
     */
    public synchronized void addCO1(double amount) {
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                regions[y][x].addCO1(amountPerRegion);
            }
        }
        co1 += amount;
    }

    /**
     * Removes the given amount of CO2 from the world. It removes a small portion
     * from each region. The total CO2 removed might be less than the given amount
     * if some regions don't have enough CO2.
     *
     * @param amount The amount of CO2 to remove.
     * @return The amount of CO2 removed. Might be less than the given amount.
     */
    public synchronized double removeCO2(double amount) {
        double removed = 0;
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                removed += regions[y][x].removeCO2(amountPerRegion);
            }
        }
        co2 -= removed;
        return removed;
    }

    /**
     * Removes the given amount of CH4 from the world. It removes a small portion
     * from each region. The total CH4 removed might be less than the given amount
     * if some regions don't have enough CH4.
     *
     * @param amount The amount of CH4 to remove.
     * @return The amount of CH4 removed. Might be less than the given amount.
     */
    public synchronized double removeCH4(double amount) {
        double removed = 0;
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                removed += regions[y][x].removeCH4(amountPerRegion);
            }
        }
        ch4 -= removed;
        return removed;
    }

    /**
     * Removes the given amount of Detritus from the world. It removes a small
     * portion from each region. The total Detritus removed might be less than the
     * given amount if some regions don't have enough Detritus.
     *
     * @param amount The amount of Detritus to remove.
     * @return The amount of Detritus removed. Might be less than the given amount.
     */
    public synchronized double removeDetritus(double amount) {
        double removed = 0;
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                removed += regions[y][x].removeDetritus(amountPerRegion);
            }
        }
        detritus -= removed;
        return removed;
    }

    /**
     * Removes the given amount of CO1 from the world. It removes a small portion
     * from each region. The total CO1 removed might be less than the given amount
     * if some regions don't have enough CO1.
     *
     * @param amount The amount of CO1 to remove.
     * @return The amount of CO1 removed. Might be less than the given amount.
     */
    public synchronized double removeCO1(double amount) {
        double removed = 0;
        double amountPerRegion = amount / totalRegions;
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                removed += regions[y][x].removeCO1(amountPerRegion);
            }
        }
        co1 -= removed;
        return removed;
    }

    /**
     * Draws the grid of regions. Mainly used for debugging.
     */
    public void draw(java.awt.Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for (int y = 0; y <= maxHeight; y++) {
            for (int x = 0; x <= maxWidth; x++) {
                ResourceRegion region = regions[y][x];
                g.drawLine(x * regionSize, y * regionSize, (x + 1) * regionSize, y * regionSize);
                g.drawLine(x * regionSize, y * regionSize, x * regionSize, (y + 1) * regionSize);
                g.drawString(String.format("O2: %.2f", region.getO2()), x * regionSize + 10, y * regionSize + 10);
                g.drawString(String.format("CO2: %.2f", region.getCO2()), x * regionSize + 10, y * regionSize + 20);
                g.drawString(String.format("CH4: %.2f", region.getCH4()), x * regionSize + 10, y * regionSize + 30);
                g.drawString(String.format("Detritus: %.2f", region.getDetritus()), x * regionSize + 10, y * regionSize + 40);
                g.drawString(String.format("CO1: %.2f", region.getCO1()), x * regionSize + 10, y * regionSize + 50);
                g.drawString(String.format("Total: %.2f", region.getO2()+region.getCO2()+region.getCH4()+region.getDetritus()+region.getCO1()), x * regionSize + 10, y * regionSize + 60);
            }
        }
    }
}
