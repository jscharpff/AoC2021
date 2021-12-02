package challenges.day02;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import challenges.day02.Sub.DIR;
import util.geometry.Coord2D;
import util.io.FileReader;

public class Day02 {

	/**
	 * Day 2 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/2
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day02.class.getResource( "day02_example.txt" ) ).readLines( );
		final List<String> input = new FileReader( Day02.class.getResource( "day02_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + moveSub( ex_input, false ) ); 
		System.out.println( "Answer : " + moveSub( input, false ) ); 

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + moveSub( ex_input, true ) ); 
		System.out.println( "Answer : " + moveSub( input, true ) ); 
	}

	/**
	 * Processes the move commands to move the submarine
	 * 
	 * @param input The list of movement commands
	 * @param aimed True to use "aimed" movement, false for simple 
	 * @return The product of the resulting horizontal position and depth after
	 *   all movement has been processed
	 */
	private static long moveSub( final List<String> input, final boolean aimed ) {

		// parse movement commands and move the sub
		final Sub sub = new Sub( );
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "(\\w+) (\\d+)" ).matcher( s );
			if( !m.find( ) ) throw new RuntimeException( "Invalid movement format" );

			// what type of movement do we process?
			if( !aimed )
				sub.move( DIR.fromString( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
			else
				sub.moveAimed( DIR.fromString( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
		}

		// return product of horizontal pos and depth as result
		final Coord2D p = sub.getPosition( );		
		return p.x * p.y;
	}
}
