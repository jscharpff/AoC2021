package challenges.day14;

import java.util.ArrayList;
import java.util.List;

import aocutil.io.FileReader;

public class Day14 {

	/**
	 * Day 14 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/14
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day14.class.getResource( "day14_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day14.class.getResource( "day14_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + polymerise( ex_input, 10 ) );
		System.out.println( "Answer : " + polymerise( input, 10 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + polymerise( ex_input, 40 ) );
		System.out.println( "Answer : " + polymerise( input, 40 ) );
	}
	
	/**
	 * Performs the polymerisation process for a given string, list of element 
	 * expansion rules and a number of steps.
	 *
	 * @param input The list of strings that contains of the input polymer, a
	 *   blank line and the element expansion rules (one per remaining line) 
	 * @return The difference between the element counts of the most and least
	 *   commonly occurring elements after polymerisation
	 */
	public static long polymerise( final List<String> input, final int steps ) {
		final List<String> inp = new ArrayList<>( input );

		// parse the input, first part is the initial polymer
		final String polymer = inp.remove( 0 );
		
		// the remaining set is the list of rules
		inp.remove( 0 );
		final Polymeriser p = new Polymeriser( inp );
		
		// perform the polymerisation process
		final ElementCount count = p.polymerise( polymer, steps );
		
		return count.getHighestCount( ) - count.getLowestCount( );
	}
}
