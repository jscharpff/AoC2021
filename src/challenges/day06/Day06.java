package challenges.day06;

import util.io.FileReader;

public class Day06 {

	/**
	 * Day 6 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/6
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new FileReader( Day06.class.getResource( "day06_example.txt" ) ).readIntArray( );
		final int[] input = new FileReader( Day06.class.getResource( "day06_input.txt" ) ).readIntArray( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + runFishSim( ex_input, 80 ) );
		System.out.println( "Answer : " + runFishSim( input, 80 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + runFishSim( ex_input, 256 ) );
		System.out.println( "Answer : " + runFishSim( input, 256 ) );
	}
	
	/**
	 * Creates a school of Lanternfish from the string input and then simulates
	 * their population growth for a given number of days
	 * 
	 * @param input An array of fish spawn timers describing the initial fish
	 *   population
	 * @param days The number of days to run the simulation for
	 * @return The number of fish in the school after running the school 
	 *   simulation for the specified number of days 
	 */
	protected static long runFishSim( final int[] input, final int days ) {
		final LanternSchool school = new LanternSchool( input );
		school.runSimulation( days );
		return school.size( );
	}
}
