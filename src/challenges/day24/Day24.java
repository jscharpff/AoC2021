package challenges.day24;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day24.alu.ALU;
import challenges.day24.alu.mem.MemRegister;

public class Day24 {

	/**
	 * Day 24 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/24
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day24.class.getResource( "day24_example.txt" ) ).readLineGroups( "," );
		final List<String> input = new FileReader( Day24.class.getResource( "day24_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Tests: " );
		test( ex_input, new String[] { "34", "7", "" } );
		System.out.println(  );
		System.out.println( "Answer : " + testSerial( input, "99893999291967" ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Answer : " + testSerial( input, "34171911181211" ) );
	}
	
	/**
	 * Performs tests by running the programs specified as list of strings and
	 * feeding them the inputs from the array of inputs. Prints the results
	 * 
	 * @param input The list of programs to run as test
	 * @param proginput The input to each of the programs, one per program in
	 *   input
	 */
	public static void test( final List<String> input, final String[] proginput ) {
		int i = 0;
		for( final String s : input ) {
			final ALU alu = new ALU( );
			alu.loadProgram( List.of( s.split( "," ) ) );
			System.out.println( (i+1) + ": " + alu.run( proginput[i++] ) );
		}
	}
	
	/**
	 * Tests the given serial against the NOMAD program, encoded in the input.
	 * Returns the serial if it is a correct model number, -1 otherwise
	 * 
	 * @param input The ALU program to run the NOMAD model checker
	 * @param serial The model serial number to check
	 * @return The serial if it is a valid serial, -1 otherwise 
	 */
	public static long testSerial( final List<String> input, final String serial ) {
		// create ALU for the program
		final ALU alu = new ALU( );
		alu.loadProgram( input );

		// the serial was deducted by analysing the instructions, no programming
		// was done to get it. Just test it here
		// see day_24reveng files for the reverse engineering of the assembly
		final int result = (int)alu.run( serial ).read( MemRegister.Z );
	
		// only valid if the program returns 0
		if( result != 0 ) return -1;
		
		// valid serial, return it
		return Long.parseLong( serial );
	}
}
	