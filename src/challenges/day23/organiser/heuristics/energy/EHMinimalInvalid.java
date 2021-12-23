package challenges.day23.organiser.heuristics.energy;

import challenges.day23.apods.Apod;
import challenges.day23.apods.Burrow;
import challenges.day23.apods.BurrowRoom;

/**
 * Underestimates the minimal energy required by simply ignoring all burrow
 * rules and summing over the energy costs of bringing all the Apods home from
 * here.
 * 
 * @author Joris
 */
public class EHMinimalInvalid implements EnergyHeuristic {
	@Override
	public long estimateMinimalEnergy( Burrow burrow ) {
		long energy = 0;
	
		for( final Apod a : burrow.getApods( ) ) {
			// already home, no need to estimate costs
			if( a.isHome( ) ) continue;
			
			// compute cost for this apod to reach the first spot in its room
			final BurrowRoom r = burrow.getApodRoom( a );
			final int dx = Math.abs( r.getDoorX( ) - a.getX( )); /* minimal horizontal distance */
			final int dy = a.getY( ) + 1; /* vertical distances depends on its current room/hallway depth */
			energy += a.getEnergyConsumption( dx, dy );
		}
		
		return energy;
	}
}
