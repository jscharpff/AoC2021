package challenges.day23;

import java.util.List;

import challenges.day23.apods.Burrow;
import challenges.day23.apods.moveheuristic.MHLeastEnergyFirst;
import util.io.FileReader;

public class Day23 {

	/**
	 * Day 23 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/23
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day23.class.getResource( "day23_example.txt" ) ).readLines();
		final List<String> ex2_input = new FileReader( Day23.class.getResource( "day23_example2.txt" ) ).readLines();
		final List<String> input = new FileReader( Day23.class.getResource( "day23_input.txt" ) ).readLines( );
		final List<String> input2 = new FileReader( Day23.class.getResource( "day23_input2.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + optimiseBurrow( ex_input ) );
		System.out.println( "Answer : " + optimiseBurrow( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + optimiseBurrow( ex2_input ) );
		System.out.println( "Answer : " + optimiseBurrow( input2) );
	}
	
	/**
	 * Finds the minimum energy required to optimise the allocation of Amphipods
	 * in the burrow in such as way that Apods of the same family are within the
	 * same room in the burrow
	 * 
	 * @param input The burrow in a list of strings
	 * @return The minimal consumption of energy required to optimise the burrow
	 */
	public static long optimiseBurrow( final List<String> input ) {
		final Burrow b = Burrow.fromStringList( input );
		return b.organise( new MHLeastEnergyFirst( ) );
	}
}
