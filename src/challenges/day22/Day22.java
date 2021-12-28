package challenges.day22;

import java.util.List;

import aocutil.geometry.Cube3D;
import aocutil.io.FileReader;

public class Day22 {

	/**
	 * Day 22 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/22
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day22.class.getResource( "day22_example.txt" ) ).readLines();
		final List<String> ex2_input = new FileReader( Day22.class.getResource( "day22_example2.txt" ) ).readLines();
		final List<String> input = new FileReader( Day22.class.getResource( "day22_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + getActiveCubes( ex_input, new Cube3D( 50 ) ) );
		System.out.println( "Answer : " + getActiveCubes( input, new Cube3D( 50 ) ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + getActiveCubes( ex2_input, null ) );
		System.out.println( "Answer : " + getActiveCubes( input, null ) );
	}
	
	/**
	 * Initiates the boot sequence of the Submarine's reactor by feeding it the
	 * list of boot instructions
	 * 
	 * @param input The set of booting sequence instructions to turn reactor
	 *   cubes on or off
	 * @param bootregion The region to initiate the boot sequence within
	 * @return The number of cubes that are now active in the reactor as a
	 *   result of completing the boot sequence
	 */
	public static long getActiveCubes( final List<String> input, final Cube3D bootregion ) {
		final ReactorCore core = ReactorCore.fromBootingSequence( bootregion, input );
		return core.getActivatedCubes( );
	}
}
