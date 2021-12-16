package challenges.day16;

import java.util.List;

import challenges.day16.packet.Packet;
import util.io.FileReader;

public class Day16 {

	/**
	 * Day 16 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/16
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day16.class.getResource( "day16_example.txt" ) ).readLines();
		final String input = new FileReader( Day16.class.getResource( "day16_input.txt" ) ).readLines( ).get( 0 );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Examples:" );
		for( final String s : ex_input )
			 System.out.println( "> " + s + " => " + part1( s ) );
		System.out.println( "\nAnswer: " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Examples:" );
		for( final String s : ex_input )
			 System.out.println( "> " + s + " => " + part2( s ) );
		System.out.println( "\nAnswer: " + part2( input ) );	
	}
	
	/**
	 * Parses the hexadecimal encoding of the transmitted packets and sums over
	 * the versions of all (encapsulated) packets
	 *
	 * @param input The hexadecimal packet payload    
	 * @return The sum of versions of all packets contained within the payload
	 */
	public static long part1( final String input ) {
		final Packet p = Packet.fromHex( input );
		return p.sumVersions( );
	}
	
	/**
	 * Parses the hexadecimal encoding of the transmitted packets and evaluates
	 * their encoded operations 
	 * 
	 * @param input The hexadecimal packet payload    
	 * @return The result of evaluating the packet contents
	 */
	public static long part2( final String input ) {
		final Packet p = Packet.fromHex( input );
		return p.reduce( );
	}
}
