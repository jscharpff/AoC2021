package challenges.day21;

import java.util.List;

import challenges.day21.dirac.DiracDice;
import challenges.day21.dirac.QuantumDirac;
import util.io.FileReader;

public class Day21 {

	/**
	 * Day 21 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/21
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day21.class.getResource( "day21_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day21.class.getResource( "day21_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Answer : " + part2( input ) );
	}
	
	/**
	 * 
	 * @param input 
	 * @return
	 */
	public static long part1( final List<String> input ) {
		final DiracDice game = DiracDice.fromStringList( input );
		final int winner = game.play( 1000 );		
		return game.getScore(1 - winner) * game.getDiceRolls( );
	}
	
	public static long part2( final List<String> input ) {
		final QuantumDirac qd = QuantumDirac.fromStringList( input );
		
		final long[] wins = qd.play( 21 );
		return wins[0] > wins[1] ? wins[0] : wins[1];
	}
}
