package challenges.day13;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.io.FileReader;

public class Day13 {

	/**
	 * Day 13 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/13
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day13.class.getResource( "day13_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day13.class.getResource( "day13_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + foldManual( ex_input, true ) );
		System.out.println( "Answer : " + foldManual( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " );
		foldManual( ex_input, false );
		System.out.println( "\nAnswer: " );
		foldManual( input, false );
	}
	
	/**
	 * Takes a string description of dots and folding lines as input and from
	 * that reconstructs a visual code that is hidden in the manual.   
	 *
	 * @param input The input as a list of coordinates that describe the dots, a
	 *   blank line and a list of folding instructions
	 * @param foldonce True to only fold once and return the count of resulting
	 *   dots in the manual, false to perform all folding instructions and print
	 *   the result
	 * @return If foldonce is true the function will return the number of dots
	 *   in the manual after the first fold, otherwise it will return 0
	 */
	public static long foldManual( final List<String> input, final boolean foldonce ) {
		// split the input into two parts, one is the dot list the other the folds
		final List<String> dots = new ArrayList<>( );
		final List<String> folds = new ArrayList<>( );
		int idx = -1;
		final String[] strin = input.toArray( new String[] {} );
		while( !strin[++idx].equals( "" ) ) dots.add( strin[idx] );
		while( ++idx < strin.length ) folds.add( strin[idx] );
		
		// process input, first part describes the initial set of dots
		final Manual manual = new Manual( dots );
		
		// then fold once in the first part
		for( final String f : folds ) {
			final Matcher m = Pattern.compile( "fold along ([xy])=(\\d+)" ).matcher( f );
			if( !m.find( ) ) throw new RuntimeException( "Invalid fold line description: " + m );

			// perform the fold
			final int fold = Integer.parseInt( m.group( 2 ) );
			if( m.group( 1 ).equals( "x" ) )		
				manual.fold( fold, 0 );
			else
				manual.fold( 0, fold );
			
			// only once in part 1, otherwise keep folding
			if( foldonce ) return manual.getDotCount( );
		}

		// in part 2 just print the resulting digits
		System.out.println( manual );
		return 0;
	}
}
