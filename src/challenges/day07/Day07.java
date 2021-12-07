package challenges.day07;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;

import util.io.FileReader;

public class Day07 {

	/**
	 * Day 7 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/7
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new FileReader( Day07.class.getResource( "day07_example.txt" ) ).readIntArray( );
		final int[] input = new FileReader( Day07.class.getResource( "day07_input.txt" ) ).readIntArray( );

		System.out.println( "---[ Part 1 ]---" );
		final Function<Integer, Long> simplecost = x -> (long)x;
		System.out.println( "Example: " + computeFuelCostBFS( ex_input, simplecost ) );
		System.out.println( "Answer : " + computeFuelCostBFS( input, simplecost ) );

		System.out.println( "\n---[ Part 2 ]---" );
		final Function<Integer, Long> naturalsum = x -> ((long)x * ((long)x+1)) / 2;
		System.out.println( "Example: " + computeFuelCostBFS( ex_input, naturalsum ) );
		System.out.println( "Answer : " + computeFuelCostBFS( input, naturalsum ) );
	}
	
	/**
	 * Computes the minimal fuel consumption of the army of crab submarines by
	 * going over all horizontal positions and determining the minimal 
	 * consumption according to the given fuel consumption function
	 * 
	 * @param positions The horizontal positions of the crab submarines
	 * @param fuelfunc Function to compute the fuel consumption for a given
	 *   number of integer moves to make
	 * @return The minimum total fuel consumption to align all crab submarines 
	 */
	protected static long computeFuelCostFull( final int[] positions, final Function<Integer, Long> fuelfunc ) {
		long bestmoves = Long.MAX_VALUE;
		final int maxpos = Arrays.stream( positions ).max( ).orElse( 1 );
		
		// go over all positions and compute minimum fuel cost
		for( int d = 1; d <= maxpos; d++ ) {
			long moves = 0;
			for( int i = 0; i < positions.length; i++ )
				moves += fuelfunc.apply( Math.abs( positions[i] - d )  );
			
			// either we improve (decrease) the fuel cost or we are done, the minimal
			// fuel cost must be a global minimum due to the nature of its function 
			if( moves < bestmoves ) bestmoves = moves;
		}
		
		return bestmoves; 
	}
	
	/**
	 * Also computes fuel costs but instead of going over all positions until
	 * global minimum is found, this one performs a BFS exploration of 
	 * neighbouring positions starting from the mean position.
	 * <br><br>
	 * <b>NOTE</b>: This function only works for monotonous increasing fuel cost
	 *   functions, otherwise a local optimum is not guaranteed to be a global
	 *   optimum.
	 * 
	 * @param positions The initial crab submarine positions 
	 * @param fuelfunc The <i><b>monotonous, increasing</b></i> fuel function to
	 *   compute fuel cost per move
	 * @return The minimum fuel cost to align all crab submarines
	 */
	protected static long computeFuelCostBFS( final int[] positions, final Function<Integer, Long> fuelfunc ) {
		final int maxpos = Arrays.stream( positions ).max( ).orElse( 0 );
		final int meanpos = Arrays.stream( positions ).sum( ) / positions.length;
		long bestmoves = Long.MAX_VALUE;

		// keep track of to explore and explored positions
		final Stack<Integer> tocheck = new Stack<Integer>( );
		final Set<Integer> checked = new HashSet<>( );

		// start at mean position, first try improving by decreasing then increasing the pos
		tocheck.push( meanpos );
		while( tocheck.size( ) > 0 ) {
			final int pos = tocheck.pop( );
			checked.add( pos );
			
			// check fuel cost for this position
			int fuelcost = 0;
			for( int i = 0; i < positions.length; i++ )
				fuelcost += fuelfunc.apply( Math.abs( positions[i] - pos )  );
			
			// no improvement? do not check its neighbours any further
			if( fuelcost > bestmoves ) continue;

			// store new best and check its neighbours for further improvement
			bestmoves = fuelcost;
			if( pos > 0 && !checked.contains( pos - 1 ) ) tocheck.push( pos - 1 );
			if( pos < maxpos && !checked.contains( pos + 1 ) ) tocheck.push( pos + 1 );
		}
		
		return bestmoves; 
	}
}
