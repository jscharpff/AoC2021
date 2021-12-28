package challenges.day18;

import java.util.ArrayList;
import java.util.List;

import aocutil.io.FileReader;

public class Day18 {

	/**
	 * Day 18 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/18
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_tests = new FileReader( Day18.class.getResource( "day18_example_tests.txt" ) ).readLines();
		final List<String> ex_input = new FileReader( Day18.class.getResource( "day18_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day18.class.getResource( "day18_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Tests  : " + part1( ex_tests ) );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
		
	}
	
	/**
	 * Reads Snailfish numbers from the input and performs iterative addition
	 * of the numbers, adding each new number to the result of the previous
	 * addition. Returns the magnitude of the resulting Snailfish number
	 *
	 * @param input The list of Snailfish numbers to add
	 * @return The magnitude of the resulting Snailfish number
	 */
	public static long part1( final List<String> input ) {
		SNumber num = null;
		for( final String s : input ) {
			// read and parse the number
			final SNumber snum = SNumber.fromString( s );
			
			// if this is the first number just set it as the result, otherwise add
			// the new number to the result of the previous iteration
			if( num == null ) num = snum;
			else num = num.add( snum );
		}
		
		return num.getMagnitude( );
	}
	
	/**
	 * Determines the largest magnitude that can be achieved by adding any two
	 * Snailfish numbers from the input set 
	 *
	 * @param input The set of Snalifish numbers to consider    
	 * @return The value of the largest magnitude that adding any pair produces
	 */
	public static long part2( final List<String> input ) {
		// first transform the input into Snailfish numbers
		long maxmagnitude = -1;
		final List<SNumber> numbers = new ArrayList<>( );
		for( final String s : input ) numbers.add( SNumber.fromString( s ) );
		
		// go over all pairs of numbers, in both permutations as the operation is
		// not transitive
		for( int i = 0; i < numbers.size( ); i++ ) {
			for( int j = 0; j < numbers.size( ); j++ ) {
				if( i == j ) continue;
				
				// add the two Snailfish numbers and store largest magnitude
				final SNumber result = numbers.get( i ).copy().add( numbers.get( j ).copy( ) );
				final long mag = result.getMagnitude( );
				if( mag > maxmagnitude ) maxmagnitude = mag;				
			}
		}
		
		return maxmagnitude;		
	}
}
