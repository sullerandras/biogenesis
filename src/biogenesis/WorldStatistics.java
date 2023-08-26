/* Copyright (C) 2006-2010  Joan Queralt Molina
 * Color Mod (C) 2012-2022  MarcoDBAA
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.gson.annotations.Expose;

public class WorldStatistics implements Serializable {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	@Expose
	private long time;

	private int maxPopulation = 0;

	private int maxBirths = 0;

	private int maxDeaths = 0;

	private long maxPopulationTime;

	private int minPopulation = 100000;

	private long minPopulationTime;

	// private int lastTimePopulation;
	private int massExtintions;

	private int massExtintionState;

	private static final int EXTINTION_NO = 0;

	private static final int EXTINTION_POSSIBLE = 1;

	private static final int EXTINTION_CONFIRMED = 2;

	private static final int EXTINTION_FINISHING = 3;

	private int createdOrganisms;

	private long populationSum;

	private long deathSum;

	private int deathLastTime;

	private long birthSum;

	private int birthLastTime;

	private long infectionsSum;

	private double maxOxygen = 0;

	private long maxOxygenTime;

	private double minOxygen = Utils.INITIAL_CO2 +  Utils.INITIAL_CH4 +  Utils.INITIAL_DETRITUS + Utils.INITIAL_O2;

	private long minOxygenTime;

	private double maxCarbonDioxide = 0;

	private double maxMethane = 0;

	private double maxDetritus = 0;

	private long maxCarbonDioxideTime;

	private long maxMethaneTime;

	private long maxDetritusTime;

	private double minCarbonDioxide = Utils.INITIAL_CO2 +  Utils.INITIAL_CH4 +  Utils.INITIAL_DETRITUS + Utils.INITIAL_O2;

	private double minMethane = Utils.INITIAL_CO2 +  Utils.INITIAL_CH4 +  Utils.INITIAL_DETRITUS + Utils.INITIAL_O2;

	private double minDetritus = Utils.INITIAL_CO2 +  Utils.INITIAL_CH4 +  Utils.INITIAL_DETRITUS + Utils.INITIAL_O2;

	private long minCarbonDioxideTime;

	private long minMethaneTime;

	private long minDetritusTime;

	private GeneticCode aliveBeingMostChildren;
	private Organism aliveOrganismMostChildren;
	private int aliveBeingMostChildrenNumber;

	private GeneticCode aliveBeingMostKills;
	private Organism aliveOrganismMostKills;
	private int aliveBeingMostKillsNumber;

	private GeneticCode aliveBeingMostInfections;
	private Organism aliveOrganismMostInfections;
	private int aliveBeingMostInfectionsNumber;

	private GeneticCode aliveBeingMostMass;
	private Organism aliveOrganismMostMass;
	private double aliveBeingMostMassNumber;

	private GeneticCode aliveBeingMostEnergy;
	private Organism aliveOrganismMostEnergy;
	private double aliveBeingMostEnergyNumber;

	private double totalMass;
	private double totalEnergy;

	private GeneticCode beingMostChildren;

	private int beingMostChildrenNumber;

	private long beingMostChildrenTime;

	private GeneticCode beingMostKills;

	private int beingMostKillsNumber;

	private long beingMostKillsTime;

	private GeneticCode beingMostInfections;

	private int beingMostInfectionsNumber;

	private long beingMostInfectionsTime;

	private GeneticCode lastBornBeing;

	private GeneticCode lastDeadBeing;

	private GeneticCode lastInfectedBeing;

	private List<Double> populationList = new ArrayList<Double>(100);

	private List<Double> distinctCladesList = new ArrayList<Double>(100);

	private List<Double> deathList = new ArrayList<Double>(100);

	private List<Double> birthList = new ArrayList<Double>(100);

	private List<Double> oxygenList = new ArrayList<Double>(100);

	private List<Double> carbonDioxideList = new ArrayList<Double>(100);

	private List<Double> methaneList = new ArrayList<Double>(100);

	private List<Double> detritusList = new ArrayList<Double>(100);

	private transient MainWindowInterface mainWindowInterface;

	public WorldStatistics(MainWindowInterface mainWindowInterface) {
		if (mainWindowInterface == null) {
			throw new IllegalArgumentException("mainWindowInterface == null");
		}
		this.mainWindowInterface = mainWindowInterface;
	}

	public void saveGameLoaded(MainWindowInterface mainWindowInterface) {
		this.mainWindowInterface = mainWindowInterface;

		// Since we renamed `distinctSpiciesList` to `distinctCladesList`, the existing saves
		// may not have this attribute, in which case the value is going to be reset to `null`
		// for some reason.
		if (distinctCladesList == null) {
			distinctCladesList = new ArrayList<Double>(100);
			// fill distinctCladesList with elements if there are elements in other lists.
			for (int i = 0; i < populationList.size(); i++) {
				distinctCladesList.add(Double.valueOf(0));
			}
		}
	}

	public long getTime() {
		return time;
	}

	public int getMaxPopulation() {
		return maxPopulation;
	}

	public int getMaxPopulationFromList() {
		return populationList.stream().max(Comparator.comparingDouble(Double::doubleValue)).orElse(Double.valueOf(0)).intValue();
	}

	public int getMaxDistinctClades() {
		return distinctCladesList.stream().max(Comparator.comparingDouble(Double::doubleValue)).orElse(Double.valueOf(0)).intValue();
	}

	public int getMaxBirth() {
		return maxBirths;
	}

	public int getMaxBirthFromList() {
		return birthList.stream().max(Comparator.comparingDouble(Double::doubleValue)).orElse(Double.valueOf(0)).intValue();
	}

	public int getMaxDeaths() {
		return maxDeaths;
	}

	public int getMaxDeathsFromList() {
		return deathList.stream().max(Comparator.comparingDouble(Double::doubleValue)).orElse(Double.valueOf(0)).intValue();
	}

	public long getMaxPopulationTime() {
		return maxPopulationTime;
	}

	public int getMinPopulation() {
		return minPopulation;
	}

	public long getMinPopulationTime() {
		return minPopulationTime;
	}

	public int getMassExtintions() {
		return massExtintions;
	}

	public int getCreatedOrganisms() {
		return createdOrganisms;
	}

	public double getAveragePopulation() {
		if (time > 0)
			return populationSum / (double) time;
		return 0;
	}

	public double getAverageDeaths() {
		if (time > 0)
			return deathSum / (double) time;
		return 0;
	}

	public double getAverageBirths() {
		if (time > 0)
			return birthSum / (double) time;
		return 0;
	}

	public double getAverageInfections() {
		if (time > 0)
			return infectionsSum / (double) time;
		return 0;
	}

	public double getMaxOxygen() {
		return maxOxygen;
	}

	public long getMaxOxygenTime() {
		return maxOxygenTime;
	}

	public double getMinOxygen() {
		return minOxygen;
	}

	public long getMinOxygenTime() {
		return minOxygenTime;
	}

	public double getMaxCarbonDioxide() {
		return maxCarbonDioxide;
	}

	public long getMaxCarbonDioxideTime() {
		return maxCarbonDioxideTime;
	}

	public double getMinCarbonDioxide() {
		return minCarbonDioxide;
	}

	public long getMinCarbonDioxideTime() {
		return minCarbonDioxideTime;
	}

	public double getMaxMethane() {
		return maxMethane;
	}

	public long getMaxMethaneTime() {
		return maxMethaneTime;
	}

	public double getMinMethane() {
		return minMethane;
	}

	public long getMinMethaneTime() {
		return minMethaneTime;
	}

	public double getMaxDetritus() {
		return maxDetritus;
	}

	public long getMaxDetritusTime() {
		return maxDetritusTime;
	}

	public double getMinDetritus() {
		return minDetritus;
	}

	public long getMinDetritusTime() {
		return minDetritusTime;
	}

	public GeneticCode getAliveBeingMostChildren() {
		return aliveBeingMostChildren;
	}

	public Organism getAliveOrganismMostChildren() {
		return aliveOrganismMostChildren;
	}

	public int getAliveBeingMostChildrenNumber() {
		return aliveBeingMostChildrenNumber;
	}

	public GeneticCode getAliveBeingMostKills() {
		return aliveBeingMostKills;
	}

	public Organism getAliveOrganismMostKills() {
		return aliveOrganismMostKills;
	}

	public int getAliveBeingMostKillsNumber() {
		return aliveBeingMostKillsNumber;
	}

	public GeneticCode getAliveBeingMostInfections() {
		return aliveBeingMostInfections;
	}

	public Organism getAliveOrganismMostInfections() {
		return aliveOrganismMostInfections;
	}

	public int getAliveBeingMostInfectionsNumber() {
		return aliveBeingMostInfectionsNumber;
	}

	public GeneticCode getAliveBeingMostMass() {
		return aliveBeingMostMass;
	}

	public Organism getAliveOrganismMostMass() {
		return aliveOrganismMostMass;
	}

	public double getAliveBeingMostMassNumber() {
		return aliveBeingMostMassNumber;
	}

	public GeneticCode getAliveBeingMostEnergy() {
		return aliveBeingMostEnergy;
	}

	public Organism getAliveOrganismMostEnergy() {
		return aliveOrganismMostEnergy;
	}

	public double getAliveBeingMostEnergyNumber() {
		return aliveBeingMostEnergyNumber;
	}

	public double getTotalMass() {
		return totalMass;
	}

	public double getTotalEnergy() {
		return totalEnergy;
	}

	public GeneticCode getBeingMostChildren() {
		return beingMostChildren;
	}

	public int getBeingMostChildrenNumber() {
		return beingMostChildrenNumber;
	}

	public long getBeingMostChildrenTime() {
		return beingMostChildrenTime;
	}

	public GeneticCode getBeingMostKills() {
		return beingMostKills;
	}

	public int getBeingMostKillsNumber() {
		return beingMostKillsNumber;
	}

	public long getBeingMostKillsTime() {
		return beingMostKillsTime;
	}

	public GeneticCode getBeingMostInfections() {
		return beingMostInfections;
	}

	public int getBeingMostInfectionsNumber() {
		return beingMostInfectionsNumber;
	}

	public long getBeingMostInfectionsTime() {
		return beingMostInfectionsTime;
	}

	public GeneticCode getLastBornBeing() {
		return lastBornBeing;
	}

	public GeneticCode getLastDeadBeing() {
		return lastDeadBeing;
	}

	public GeneticCode getLastInfectedBeing() {
		return lastInfectedBeing;
	}

	public List<Double> getPopulationList() {
		return populationList;
	}

	public List<Double> getDistinctCladesList() {
		return distinctCladesList;
	}

	public List<Double> getDeathList() {
		return deathList;
	}

	public List<Double> getBirthList() {
		return birthList;
	}

	public List<Double> getOxygenList() {
		return oxygenList;
	}

	public List<Double> getCarbonDioxideList() {
		return carbonDioxideList;
	}

	public List<Double> getMethaneList() {
		return methaneList;
	}

	public List<Double> getDetritusList() {
		return detritusList;
	}

	public void eventPopulationIncrease(int newPopulation) {
		if ((newPopulation > maxPopulation) && (time >= 10)) {
			maxPopulation = newPopulation;
			maxPopulationTime = time;
		}
	}

	public void eventPopulationDecrease(int newPopulation) {
		if ((newPopulation < minPopulation) && (time >= 10)) {
			minPopulation = newPopulation;
			minPopulationTime = time;
		}
	}

	public void eventOrganismCreated() {
		createdOrganisms++;
	}

	public void eventOrganismBorn(Organism newOrganism, Organism parent) {
		lastBornBeing = newOrganism.getGeneticCode();
		if (parent.getTotalChildren() > beingMostChildrenNumber) {
			beingMostChildren = parent.getGeneticCode();
			beingMostChildrenNumber = parent.getTotalChildren();
			beingMostChildrenTime = time;
		}
		birthSum++;
		birthLastTime++;
	}

	public void eventOrganismDie(Organism dyingOrganism,
			Organism killingOrganism) {
		lastDeadBeing = dyingOrganism.getGeneticCode();
		if (killingOrganism != null
				&& killingOrganism.getTotalKills() > beingMostKillsNumber) {
			beingMostKills = killingOrganism.getGeneticCode();
			beingMostKillsNumber = killingOrganism.getTotalKills();
			beingMostKillsTime = time;
		}
		deathSum++;
		deathLastTime++;
	}

	public void eventOrganismInfects(Organism infectedOrganism,
			Organism infectingOrganism) {
		lastInfectedBeing = infectedOrganism.getGeneticCode();
		if (infectingOrganism != null
				&& infectingOrganism.getTotalInfected() > beingMostInfectionsNumber) {
			beingMostInfections = infectingOrganism.getGeneticCode();
			beingMostInfectionsNumber = infectingOrganism.getTotalInfected();
			beingMostInfectionsTime = time;
		}
		infectionsSum++;
	}

	public void eventTime(int population, int distinctClades, int distinctCladesWith10Orgs, int distinctCladesWith100Orgs, double O2, double CO2, double CH4, double detritus, List<Organism> organisms) {
		time++;
		if (deathLastTime > 1.5 * getAverageDeaths()) {
			if (deathLastTime > 3 * getAverageDeaths()) {
				if (massExtintionState != EXTINTION_CONFIRMED
						&& massExtintionState != EXTINTION_FINISHING)
					massExtintions++;
				massExtintionState = EXTINTION_CONFIRMED;
			} else {
				switch (massExtintionState) {
				case (EXTINTION_NO):
					massExtintionState = EXTINTION_POSSIBLE;
					break;
				case (EXTINTION_POSSIBLE):
					massExtintionState = EXTINTION_CONFIRMED;
					massExtintions++;
					break;
				case (EXTINTION_FINISHING):
					massExtintionState = EXTINTION_CONFIRMED;
					break;
				}
			}
		} else {
			switch (massExtintionState) {
			case (EXTINTION_POSSIBLE):
			case (EXTINTION_FINISHING):
				massExtintionState = EXTINTION_NO;
				break;
			case (EXTINTION_CONFIRMED):
				massExtintionState = EXTINTION_FINISHING;
				break;
			}
		}
		//System.out.println(deathLastTime + " " + getAverageDeaths() + " " +
		//massExtintionState + " " + massExtintions);
		// lastTimePopulation = population;
		populationSum += population;

		if ((O2 > maxOxygen) && (time >= 10)) {
			maxOxygen = O2;
			maxOxygenTime = time;
		}
		if ((O2 < minOxygen) && (time >= 10)) {
			minOxygen = O2;
			minOxygenTime = time;
		}
		if ((CO2 > maxCarbonDioxide) && (time >= 10)) {
			maxCarbonDioxide = CO2;
			maxCarbonDioxideTime = time;
		}
		if ((CO2 < minCarbonDioxide) && (time >= 10)) {
			minCarbonDioxide = CO2;
			minCarbonDioxideTime = time;
		}
		if ((CH4 > maxMethane) && (time >= 10)) {
			maxMethane = CH4;
			maxMethaneTime = time;
		}
		if ((CH4 < minMethane) && (time >= 10)) {
			minMethane = CH4;
			minMethaneTime = time;
		}
		if ((detritus > maxDetritus) && (time >= 10)) {
			maxDetritus = detritus;
			maxDetritusTime = time;
		}
		if ((detritus < minDetritus) && (time >= 10)) {
			minDetritus = detritus;
			minDetritusTime = time;
		}
		if (birthLastTime > maxBirths)
			maxBirths = birthLastTime;
		if (deathLastTime > maxDeaths)
			maxDeaths = deathLastTime;
		if (populationList.size() == 100)
			populationList.remove(0);
		populationList.add(Double.valueOf(population));
		if (distinctCladesList.size() == 100)
			distinctCladesList.remove(0);
		distinctCladesList.add(Double.valueOf(distinctClades));
		if (deathList.size() == 100)
			deathList.remove(0);
		deathList.add(Double.valueOf(deathLastTime));
		if (birthList.size() == 100)
			birthList.remove(0);
		birthList.add(Double.valueOf(birthLastTime));
		if (oxygenList.size() == 100)
			oxygenList.remove(0);
		oxygenList.add(Double.valueOf(Math.sqrt(Math.sqrt(O2))));
		if (carbonDioxideList.size() == 100)
			carbonDioxideList.remove(0);
		carbonDioxideList.add(Double.valueOf(Math.sqrt(Math.sqrt(CO2))));
		if (methaneList.size() == 100)
			methaneList.remove(0);
		methaneList.add(Double.valueOf(Math.sqrt(Math.sqrt(CH4))));
		if (detritusList.size() == 100)
			detritusList.remove(0);
		detritusList.add(Double.valueOf(Math.sqrt(Math.sqrt(detritus))));
		deathLastTime = 0;
		birthLastTime = 0;

		if ((Utils.AUTO_BACKUP_CSV) && (mainWindowInterface.getBioFile() != null)) {
			mainWindowInterface.getBioFile().appendToCsv(time, population, distinctClades, distinctCladesWith10Orgs, distinctCladesWith100Orgs, O2, CO2, CH4, detritus, organisms);
		}
	}

	/**
	 * Finds the remarkable beings in the world. Also calculates totalMass and totalEnergy.
	 * @param organisms
	 */
	public void findBestAliveBeings(List<Organism> organisms) {
		aliveBeingMostChildren = null;
		aliveOrganismMostChildren = null;
		aliveBeingMostChildrenNumber = 0;

		aliveBeingMostKills = null;
		aliveOrganismMostKills = null;
		aliveBeingMostKillsNumber = 0;

		aliveBeingMostInfections = null;
		aliveOrganismMostInfections = null;
		aliveBeingMostInfectionsNumber = 0;

		aliveBeingMostMass = null;
		aliveOrganismMostMass = null;
		aliveBeingMostMassNumber = 0;

		aliveBeingMostEnergy = null;
		aliveOrganismMostEnergy = null;
		aliveBeingMostEnergyNumber = 0;

		totalMass = 0;
		totalEnergy = 0;

		synchronized(organisms) {
			for (Iterator<Organism> it = organisms.iterator(); it.hasNext();) {
				Organism org = it.next();
				if (org.isAlive()) {
					if (org.getTotalChildren() > aliveBeingMostChildrenNumber) {
						aliveBeingMostChildrenNumber = org.getTotalChildren();
						aliveBeingMostChildren = org.getGeneticCode();
						aliveOrganismMostChildren = org;
					}
					if (org.getTotalKills() > aliveBeingMostKillsNumber) {
						aliveBeingMostKillsNumber = org.getTotalKills();
						aliveBeingMostKills = org.getGeneticCode();
						aliveOrganismMostKills = org;
					}
					if (org.getTotalInfected() > aliveBeingMostInfectionsNumber) {
						aliveBeingMostInfectionsNumber = org.getTotalInfected();
						aliveBeingMostInfections = org.getGeneticCode();
						aliveOrganismMostInfections = org;
					}
					if (org.getMass() > aliveBeingMostMassNumber) {
						aliveBeingMostMassNumber = org.getMass();
						aliveBeingMostMass = org.getGeneticCode();
						aliveOrganismMostMass = org;
					}
					if (org.getEnergy() > aliveBeingMostEnergyNumber) {
						aliveBeingMostEnergyNumber = org.getEnergy();
						aliveBeingMostEnergy = org.getGeneticCode();
						aliveOrganismMostEnergy = org;
					}
					totalMass += org.getMass();
					totalEnergy += org.getEnergy();
				}
			}
		}
	}
}
