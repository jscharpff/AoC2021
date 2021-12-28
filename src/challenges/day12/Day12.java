package challenges.day12;

import java.util.List;

import aocutil.io.FileReader;

public class Day12 {

	/**
	 * Day 12 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/12
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex1_input = new FileReader( Day12.class.getResource( "day12_example.txt" ) ).readLines();
		final List<String> ex2_input = new FileReader( Day12.class.getResource( "day12_example2.txt" ) ).readLines();
		final List<String> input = new FileReader( Day12.class.getResource( "day12_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example 1: " + countCaveRoutes( ex1_input, false ) );
		System.out.println( "Example 2: " + countCaveRoutes( ex2_input, false ) );
		System.out.println( "Answer   : " + countCaveRoutes( input, false ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example 1: " + countCaveRoutes( ex1_input, true ) );
		System.out.println( "Example 2: " + countCaveRoutes( ex2_input, true ) );
		System.out.println( "Answer   : " + countCaveRoutes( input, true ) );
	}
	
	/**
	 * Determines the number of unique cave routes
	 *
	 * @param input The list of edges that describe the cave system
	 * @param allowedtwice True to allow at most one small cave to be visited
	 *   twice, false means that all small caves can only be visited once
	 * @return The number of unique paths through the cave system from start to
	 *   end
	 */
	public static long countCaveRoutes( final List<String> input, final boolean allowtwice ) {
		final CaveSystem caves = new CaveSystem( input );
		return caves.findAllRoutes( caves.getStart(), caves.getEnd( ), allowtwice ).size( );
	}
}
