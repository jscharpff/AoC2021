package challenges.day08;

import java.util.List;

import aocutil.io.FileReader;

public class Day08 {

	/**
	 * Day 8 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/8
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day08.class.getResource( "day08_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day08.class.getResource( "day08_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Part 1 is easy, simply count all 2, 3, 4 or 7 length string in the output
	 * digit part of the input and return the count 
	 * 
	 * @param input The segment encodings as 10 space-separated [a-g]+ strings,
	 *   a pipe and four space-separated [a-g]+ strings
	 * @return The count of "unique" digits in the output
	 */
	public static long part1( final List<String> input ) {
		// part 1 is easy, just count the number of 2, 3, 4 and 7 length segments
		// in the output digits
		int count = 0;
		for( final String s : input ) {
			final String out = s.split( " \\| " )[1];
			for( final String d : out.split( " " ) ) {
				final int len = d.length( );
				if( len == 2 || len == 3 || len == 4 || len == 7 ) count++;
			}
		}
		
		return count;
	}
	
	/**
	 * Slightly more difficult. Use the input side of each line to deduce the
	 * encoding of segments to digits and use that to convert the output side
	 * of each line into an integer value. 
	 * 
	 * @param input The segment encodings as 10 space-separated [a-g]+ strings,
	 *   a pipe and four space-separated [a-g]+ strings
	 * @return The sum of all decoded values
	 */
	public static long part2( final List<String> input ) {
		// decode each line and sum over their output
		long sum = 0;
		for( final String s : input ) {
			// split each input into input and output part
			final String[] enc = s.split( " \\| " );
			
			// use input part to initialise decoder
			 final DigitDecoder d = new DigitDecoder( enc[0].split( " " ) );
			 
			 // and use decoder to decode output part
			 sum += d.decode( enc[1].split( " " ) );
		}
		return sum;
	}
}
