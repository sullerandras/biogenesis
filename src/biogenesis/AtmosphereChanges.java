package biogenesis;

/**
 * Changes to the atmosphere that a worker thread has made while organisms move
 * on several threads, and that aren't in the world's fields yet (see
 * World.atmosphereChanges).
 */
public class AtmosphereChanges {
	/** The world the changes are for (set by the first change). */
	World world;
	double O2;
	double CO2;
	double CH4;
	double CO1;
	double detritus;
	double N2;
	double NH3;
}
