package challenges.day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import challenges.day10.syntaxchecker.SyntaxChecker;
import challenges.day10.syntaxchecker.error.ChunkIncompleteError;
import challenges.day10.syntaxchecker.error.ChunkIncorrectError;
import challenges.day10.syntaxchecker.error.UnknownCharacterError;
import util.io.FileReader;

public class Day10 {

	/**
	 * Day 10 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/10
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day10.class.getResource( "day10_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day10.class.getResource( "day10_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * Checks the syntax of the given lines and sums over syntax error scores for
	 * ChunkIncorrectErrors, i.e. whenever a unexpected chunk closing occurs
	 * 
	 * @param input The lines of syntax to check 
	 * @return The sum of syntax error scores over incorrect chunk ending errors
	 */
	public static long part1( final List<String> input ) {
		final SyntaxChecker sc = new SyntaxChecker( );
		
		// parse all lines and sum over the incorrect syntax error scores only
		long score = 0;
		for( final String s : input ) {
			try {
				sc.check( s );
			} catch( ChunkIncompleteError incomplete ) {
				// skip these for now
			} catch( ChunkIncorrectError incorrect ) {
				// add the score of this line to the total sum
				score += incorrect.getScore( );
			} catch( UnknownCharacterError se ) {
				// unexpected error
				System.err.println( "Unexpected syntax error: " + se.getMessage( ) );
			}
		}
		return score;
	}

	/**
	 * Checks the syntax of the given lines and finds the mean syntax error score
	 * for ChunkIncompleteErrors, i.e. whenever a line has unfinished chunks.
	 * 
	 * @param input The lines of syntax to check
	 * @return The mean syntax error score over incomplete chunk ending errors
	 */
	public static long part2( final List<String> input ) {
		final SyntaxChecker sc = new SyntaxChecker( );
		
		// first find all incomplete chunk error scores
		final List<Long> scores = new ArrayList<>( );
		for( final String s : input ) {
			try {
				sc.check( s );
			} catch( ChunkIncompleteError incomplete ) {
				// store the score found
				scores.add( incomplete.getScore( ) );
			} catch( ChunkIncorrectError incorrect ) {
				// now skip these, we're only interested in incomplete lines
			} catch( UnknownCharacterError se ) {
				// unexpected error
				System.err.println( "Unexpected syntax error: " + se.getMessage( ) );
			}
		}
		
		// return mean of score array which is at index n / 2 rounded down in a
		// sorted array
		Collections.sort( scores );
		return scores.get( (int) Math.floor( (double)scores.size( ) / 2.0 ) );
	}
}
