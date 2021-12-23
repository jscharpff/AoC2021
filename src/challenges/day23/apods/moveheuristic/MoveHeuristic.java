package challenges.day23.apods.moveheuristic;

import java.util.List;

import challenges.day23.apods.ApodMove;

public interface MoveHeuristic {
	/**
	 * Orders the moves in the set according to the implementation of the
	 * heuristic. This happens in place, i.e. the ordering of the input list is
	 * changed
	 * 
	 * @param moves The set of available next moves
	 */
	public void apply( final List<ApodMove> moves );

}
