package challenges.day23.organiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Container for a (current) solution to organise the apods optimally
 */
public class Solution {
	/** The sequential list of moves to perform */
	protected List<ApodMove> bestmoves;
	
	/** The total energy consumption of this solution */
	protected long leastenergy;
	
	/**
	 * Creates a new empty solution
	 */
	public Solution( ) {
		this.bestmoves = null;
		this.leastenergy = Long.MAX_VALUE;
	}
	
	/**
	 * Updates the Solution to the best one found so far
	 * 
	 * @param moves The list of moves to perform to get all Apods organised
	 * @param energy The total energy consumption required to organise them
	 */
	public void set( final Stack<ApodMove> moves, final long energy ) {
		if( energy >= leastenergy ) throw new RuntimeException( "Invalid update of solution (best: " + leastenergy + ", new best: " + energy + ")" );
		
		this.bestmoves = new ArrayList<>( moves );
		this.leastenergy = energy;
	}
	
	/** @return The solution description */
	@Override
	public String toString( ) {
		if( bestmoves == null ) return "(No solution yet)";
		
		return "[E: " + leastenergy + ", M: " + bestmoves.size( ) + "]";   
	}
}
