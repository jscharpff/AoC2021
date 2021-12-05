package challenges.day05;

import java.util.List;

import util.io.FileReader;

public class Day05 {

	/**
	 * Day 5 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/5
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day05.class.getResource( "day05_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day05.class.getResource( "day05_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + countOverlappingVents( ex_input, false ) );
		System.out.println( "Answer : " + countOverlappingVents( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + countOverlappingVents( ex_input, true ) );
		System.out.println( "Answer : " + countOverlappingVents( input, true ) );
	}
	
	/**
	 * Reads the list of hydrothermal vents from the input and computes the
	 * number of points at which two or more vents overlap
	 * 
	 * @param input The list of vents described by line segments
	 * @param diagonals True to include also diagonal lines, false for only 
	 *   horizontal and vertical vents
	 * @return The number of coordinates at which at least two vents overlap
	 */
	protected final static long countOverlappingVents( final List<String> input, final boolean diagonals ) {
		final VentMap vm = VentMap.fromList( input, diagonals );
		return vm.countOverlap( );
	}
}
