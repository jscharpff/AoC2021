package challenges.day25;

import java.util.List;

import util.io.FileReader;

public class Day25 {

	/**
	 * Day 25 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/25
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day25.class.getResource( "day25_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day25.class.getResource( "day25_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + simulate( ex_input ) );
		System.out.println( "Answer : " + simulate( input ) );
	}
	
	/**
	 * Simulates the movements of a grid of Sea Cucumbers until their movement
	 * comes to a halt due to their movement constraints 
	 * 
	 * @param input The list of strings that describes the grid of Sea Cucumbers
	 * @return The number of simulation steps until all movement halted
	 */
	public static long simulate( final List<String> input ) {
		final SeaCucumbers sc = SeaCucumbers.fromStringList( input );
		return sc.simulateUntilStopped( );
	}
}
