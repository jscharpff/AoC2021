package challenges.day23.apods.moveheuristic;

import java.util.Comparator;
import java.util.List;

import challenges.day23.apods.ApodMove;

/**
 * Move heuristic that prefers the cheapest moves first 
 * 
 * @author Joris
 */
public class MHLeastEnergyFirst implements MoveHeuristic {
	
	@Override
	public void apply( List<ApodMove> moves ) {
		moves.sort( new Comparator<ApodMove>( ) {
			@Override
			public int compare( ApodMove o1, ApodMove o2 ) {
				return (int)o1.energy - (int)o2.energy;
			}			
		} );
	}
}
