package challenges.day23.organiser.heuristics.energy;

import challenges.day23.apods.Burrow;

/**
 * Interface that allows implementation of various heuristics to determine the
 * minimal additional energy consumption that is required from the current
 * state onward. This <i>should</i> be an admissible heuristic that never
 * overestimates the energy costs, otherwise optimality may be lost.
 * 
 * @author Joris
 */
public interface EnergyHeuristic {
	
	/**
	 * Estimates the minimal additional required to a possible solution
	 * 
	 * @param burrow The current state of the burrow
	 * @return A heuristic value that never underestimates the additional energy
	 *   consumption.
	 */
	public long estimateMinimalEnergy( final Burrow burrow );
}
