package challenges.day04;

import java.util.List;

import aocutil.io.FileReader;
import challenges.day04.bingo.Bingo;
import challenges.day04.bingo.BingoCard;

public class Day04 {

	/**
	 * Day 4 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/4
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day04.class.getResource( "day04_example.txt" ) ).readLineGroups( "," );
		final List<String> input = new FileReader( Day04.class.getResource( "day04_input.txt" ) ).readLineGroups( "," );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + playBingo( ex_input, true ) );
		System.out.println( "Answer : " + playBingo( input, true ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + playBingo( ex_input, false ) );
		System.out.println( "Answer : " + playBingo( input, false ) );
	}
	
	/**
	 * Plays a game of Bingo!
	 * 
	 * @param input The bingo game, encoded in a list of strings
	 * @param firstWin True to play until first win, false to play until last win
	 * @return The product of the last winning number and the sum of numbers not
	 *   marked on the first/last winning card
	 */
	protected final static long playBingo( final List<String> input, final boolean firstWin ) {
		final Bingo bingo = Bingo.fromStringList( input );
		final BingoCard card = firstWin ? bingo.playUntilFirstWin( ) : bingo.playUntilLastWin( );		
		return bingo.getLastWinner( ) * card.sumUnmarked( );
	}
}
