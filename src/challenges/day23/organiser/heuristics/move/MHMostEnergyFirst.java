package challenges.day23.organiser.heuristics.move;

import java.util.Comparator;
import java.util.List;

import challenges.day23.apods.Burrow;
import challenges.day23.organiser.ApodMove;

/**
 * Move heuristic that prefers the costliest moves first 
 * 
 * @author Joris
 */
public class MHMostEnergyFirst implements MoveHeuristic {
	
	@Override
	public void apply( final Burrow burrow, final List<ApodMove> moves ) {
		moves.sort( new Comparator<ApodMove>( ) {
			@Override
			public int compare( ApodMove o1, ApodMove o2 ) {
				return (int)o2.energy - (int)o1.energy;
			}			
		} );
	}
}
