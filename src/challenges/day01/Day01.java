package challenges.day01;

import aocutil.io.FileReader;

public class Day01 {

	/**
	 * Day 1 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/1
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final int[] ex_input = new FileReader( Day01.class.getResource( "day01_example.txt" ) ).readIntArray( );
		final int[] input = new FileReader( Day01.class.getResource( "day01_input.txt" ) ).readIntArray( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + countIncreases( ex_input, 1 ) ); 
		System.out.println( "Answer : " + countIncreases( input, 1 ) ); 

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + countIncreases( ex_input, 3 ) ); 
		System.out.println( "Answer : " + countIncreases( input, 3 ) ); 
	}
	
	/**
	 * Count how many times the value increases for every two successive array
	 * windows of n elements
	 * 
	 * @param input Array of integer inputs
	 * @param n The window size
	 * @return The number of times an increase is observed between elements
	 */
	private static long countIncreases( final int[] input, final int n ) {
		long inccount = 0;
		for( int i = n; i < input.length; i++ ) {
			int sumA = 0; int sumB = 0;
			for( int j = i - n + 1; j <= i; j++ ) {
				sumA += input[j-1];
				sumB += input[j];
			}
			if( sumB > sumA ) inccount++;
		}

		return inccount;
	}
}
