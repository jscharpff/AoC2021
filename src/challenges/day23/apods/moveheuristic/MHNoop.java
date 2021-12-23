package challenges.day23.apods.moveheuristic;

import java.util.List;

import challenges.day23.apods.ApodMove;

/**
 * Heuristic that does not change the list ordering at all
 * 
 * @author Joris
 */
public class MHNoop implements MoveHeuristic {
	@Override
	public void apply( List<ApodMove> moves ) {
		// NOOP		
	}
}
