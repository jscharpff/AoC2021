package challenges.day15;

import java.util.List;

import util.geometry.Coord2D;
import util.io.FileReader;

public class Day15 {

	/**
	 * Day 15 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/15
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day15.class.getResource( "day15_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day15.class.getResource( "day15_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
		
	}
	
	/**
	 * Finds the route with minimal risks from the top left to the bottom right
	 * coordinate of a grid of Chitons, described by the input
	 *
	 * @param input The input as list of strings that model the Chiton grid   
	 * @return The summed risk over the minimal risk path
	 */
	public static long part1( final List<String> input ) {
		final Chitons c = new Chitons( input );
		return c.findMinimalRiskRoute( new Coord2D( 0, 0 ), c.chitons.size( ).move( -1, -1 ) );
	}
	
	/**
	 * Same as part1 but now with a grid that is 5 times as wide and high.
	 *
	 * @param input The input as list of strings that model the Chiton grid   
	 * @return The summed risk over the minimal risk path
	 */
	public static long part2( final List<String> input ) {
		final Chitons c = new Chitons( input );
		c.replicate( 4, 4,  1 );		
		return c.findMinimalRiskRoute( new Coord2D( 0, 0 ), c.chitons.size( ).move( -1, -1 ) );
	}
}
