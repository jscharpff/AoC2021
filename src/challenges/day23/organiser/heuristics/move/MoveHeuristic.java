package challenges.day23.organiser.heuristics.move;

import java.util.List;

import challenges.day23.apods.Burrow;
import challenges.day23.organiser.ApodMove;

public interface MoveHeuristic {
	/**
	 * Orders the moves in the set according to the implementation of the
	 * heuristic. This happens in place, i.e. the ordering of the input list is
	 * changed
	 * 
	 * @param burrow The burrow that is reorganising
	 * @param moves The set of available next moves
	 */
	public void apply( final Burrow burrow, final List<ApodMove> moves );

}
