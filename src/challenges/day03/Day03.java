package challenges.day03;

import java.util.ArrayList;
import java.util.List;

import util.BitString;
import util.io.FileReader;

public class Day03 {

	/**
	 * Day 3 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/3
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day03.class.getResource( "day03_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day03.class.getResource( "day03_input.txt" ) ).readLines( );
		
		// convert to bit strings
		final List<BitString> ex_bits = ex_input.stream( ).map( BitString::fromString ).toList( );
		final List<BitString> inp_bits = input.stream( ).map( BitString::fromString ).toList( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_bits ) );
		System.out.println( "Answer : " + part1( inp_bits ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_bits ) );
		System.out.println( "Answer : " + part2( inp_bits ) );
	}

	/**
	 * Determine gamma and epsilon rates of power consumption by finding the 
	 * most/least common bit strings
	 * 
	 * @param input The list of bit strings, String formatted
	 * @return The product of gamma and epsilon level int values
	 */
	protected static final long part1( final List<BitString> input ) {
		final BitString gamma = findCommon( input, true );
		final BitString epsilon = findCommon( input, false );
		return gamma.toLong( ) * epsilon.toLong( );
	}

	/**
	 * Determine oxygen/co2 level bit strings by iteratively filtering based upon
	 * most/least occurring bit
	 * 
	 * @param input The list of bit strings, String formatted
	 * @return The product of oxygen and co2 level int values
	 */
	protected static final long part2( final List<BitString> input ) {
		final BitString oxy = retainCommon( input, true );
		final BitString co2 = retainCommon( input, false );
		return oxy.toLong( ) * co2.toLong( );
	}

	/**
	 * Filters the list of bit strings by going over each bit and only keeping
	 * those bit strings that for that bit have the same value as the bit in the
	 * most/least common bit string (of the remaining set).
	 * 
	 * @param input The list of bit strings in String format
	 * @param most Use most or least common bit to filter the list
	 * @return The single bit string that in each iteration matched the most or
	 *   least common bit string over the remaining set
	 */
	protected static final BitString retainCommon( final List<BitString> input, final boolean most ) {
		int len = input.get( 0 ).length( );
		List<BitString> binaries = new ArrayList<>( input );
		
		// go over all bits and keep only binaries that match with common bit
		for( int b = len - 1; b >= 0; b-- ) {
			final BitString common = findCommon( binaries, most );
			final int bit = b;
			binaries = binaries.stream( ).filter( x -> x.get( bit ) == common.get( bit ) ).toList( );

			// stop with only one bit string left, otherwise the least common bit
			// string will be the inverse of it and lead to its removal
			if( binaries.size( ) == 1 ) break;
		}
		
		// check if we indeed have a single bit string after filtering
		if( binaries.size( ) > 1 ) throw new RuntimeException( "Failed to find single binary value" );		
		return binaries.get( 0 );
	}
	
	/**
	 * Finds the most or least common bit in a list of bit strings 
	 * 
	 * @param input The bit strings, each in string format
	 * @param most Return the bit string with most or least occurring bit
	 * @return A single bit string that contains the most or least occurring bit
	 *   value per bit over the list of bit strings
	 */
	protected static final BitString findCommon( final List<BitString> input, final boolean most ) {
		final int len = input.get( 0 ).length( );
		final int[] countOnes = new int[ len ];
		final int N = input.size( );
		
		// count ones
		for( final BitString s : input )
			for( int i = 0; i < s.length( ); i++ )
				if( s.get( i ) ) countOnes[i]++;
		
		// reconstruct most common from count
		final BitString res = new BitString( len );
		for( int i = 0; i < countOnes.length; i++ )
			res.set( i, countOnes[i] >= (N / 2.0) );
		
		// In case of least occurring bit the result can be produced by flipping the outcome
		if( !most ) res.negate( );
		return res;
	}
}
