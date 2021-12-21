package challenges.day21.dirac;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A friendly game of DiracDice
 * 
 * @author Joris
 */
public class DiracDice {
	/** The player's current positions (0-9) */
	private final int[] position;
	
	/** THe player's scores */
	private final int[] score;
	
	/** The dice used in this game */
	private final DeterministicDice dice;
	
	/**
	 * Creates a new game of Dirac Dice with the given player start positions
	 * 
	 * @param dice The dice to use in the game
	 * @param start The starting positions per player
	 */
	private DiracDice( final DeterministicDice dice, final int... start ) {
		this.dice = dice;
		this.position = new int[ start.length ];
		for( int i = 0; i < start.length; i++ ) position[i] = start[i];
		this.score = new int[ start.length ];
	}
	
	/**
	 * Plays the game until one of the player reaches the given score
	 * 
	 * @param winscore The score needed to win
	 * @return The winning player index
	 */
	public int play( final int winscore ) {
		// play until we have a winner
		while( true ) {
			
			// let every player roll the dice, player 0 rolls first
			for( int i = 0; i < position.length; i++ ) {
				// roll three times
				int rolls = 0;
				for( int j = 0; j < 3; j++ ) rolls += dice.roll( );
				
				// update position and score accordingly
				position[i] = (position[i] + rolls) % 10;
				score[i] += position[i] + 1;
					
				// and return the score
				if( score[i] >= winscore ) return i;
			}
			
		}
	}
	
	/** 
	 * @param player The player number (starting at 0)
	 * @return The score of the player
	 */
	public int getScore( final int player ) {
		return score[ player ];
	}

	/** @return The number of times the dice has been rolled */
	public int getDiceRolls( ) {
		return dice.getRolls( );
	}
	
	/**
	 * Creates a new game from a list of strings
	 * 
	 * @param input List of strings that describe the starting positions of the
	 *   players
	 * @return A new game
	 */
	public static DiracDice fromStringList( final List<String> input ) {
		final int[] in = new int[ input.size( ) ];
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "Player (\\d+) starting position: (\\d+)" ).matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid player description in input: " + s );
			
			// set starting position of the player
			in[ Integer.parseInt( m.group( 1 ) ) - 1 ] = Integer.parseInt( m.group( 2 ) ) - 1; 
		}
		
		return new DiracDice( new DeterministicDice( ), in ); 
	}
}
