package challenges.day11;

import java.util.List;

import aocutil.io.FileReader;

public class Day11 {

	/**
	 * Day 11 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/11
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day11.class.getResource( "day11_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day11.class.getResource( "day11_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Example: " + part2( input ) );
	}
	
	/**
	 * Simulates the light emitting octopi for 100 steps and sums over the number
	 * of flashes that occur during this simulation
	 *
	 * @param input The grid of Octopi in terms of their current light level 
	 * @return The total number of flashes that occurred in 100 simulation steps
	 */
	public static long part1( final List<String> input ) {
		final Octopi oct = new Octopi( input );
		return oct.simulate( 100 );
	}
	
	/**
	 * Determines the first simulation step at which all the octopi flash at the
	 * same time.
	 * 
	 * @param input The grid of Octopi in terms of their current light level 
	 * @return The first step at which all octopi flashed simultaneously
	 */	
	public static long part2( final List<String> input ) {
		final Octopi oct = new Octopi( input );
		return oct.findSynchronised( );
	}
}
