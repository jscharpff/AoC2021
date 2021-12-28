package challenges.day17;

import aocutil.io.FileReader;

public class Day17 {

	/**
	 * Day 17 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/17
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final String ex_input = new FileReader( Day17.class.getResource( "day17_example.txt" ) ).readLines().get( 0 );
		final String input = new FileReader( Day17.class.getResource( "day17_input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
		
	}
	
	/**
	 * Determines the highest altitude a probe may achieve will still ending up
	 * in the target area described by the input
	 * 
	 * @param input The target area description   
	 * @return The maximum altitude that may be attained by the probe while still
	 *   ending in the target area, according to its movement rules
	 */
	public static long part1( final String input ) {
		final Probe p = Probe.fromString( input );
		return p.findHighestHit( );
	}

	/**
	 * Counts the number of unique velocity vectors that will move the probe into
	 * the target area
	 * 
	 * @param input The target area description   
	 * @return The count of unique velocity vectors that, if movement rules are
	 *   applied, will bring the probe into the target area 
	 */
	public static long part2( final String input ) {
		final Probe p = Probe.fromString( input );
		return p.countHitVelocities( );
	}
}
