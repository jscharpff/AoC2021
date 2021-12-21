package challenges.day19;

import java.util.List;

import util.io.FileReader;

public class Day19 {

	/**
	 * Day 19 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/19
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day19.class.getResource( "day19_example.txt" ) ).readLineGroups( "\n" );
		final List<String> input = new FileReader( Day19.class.getResource( "day19_input.txt" ) ).readLineGroups( "\n" );
		
		System.out.println( "---[ Example ]---" );
		reconstructScannerArray( ex_input );

		System.out.println( "\n---[ Part 2 ]---" );
		reconstructScannerArray( input );
		
	}
	
	/**
	 * Reconstructs a sensor array by determining the (relative) position and
	 * rotation of all scanners in the array, using their shared observations to
	 * fixate pairs of scanners. Using the resulting scanner array, the number
	 * of beacons and the scanning range is determined.
	 *
	 * @param input A list that describes the scanners, one String per scanner
	 *   which holds its ID and the beacons it observes relative from its current
	 *   position
	 */
	public static void reconstructScannerArray( final List<String> input ) {
		final ScannerArray array = ScannerArray.fromStringList( input );
		System.out.println( "Reconstructing scanner array..." );
		array.reconstruct( 12 );
		System.out.println( "Done!\n" );
		
		System.out.println( "Beacon count  : " + array.getBeacons( ).size( ) );
		System.out.println( "Scanning range: " + array.getScanningRange( ) );
	}
}
