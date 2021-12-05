package challenges.day04.bingo;

import java.util.ArrayList;
import java.util.List;

public class Bingo {
	/** The participating BingoCards */
	protected final List<BingoCard> cards;
	
	/** Sequence of numbers to draw from */
	protected final List<Integer> draws;
	
	/** The last winning number */
	protected int lastwinner = -1;
	
	/**
	 * Creates a new Bingo game
	 */
	protected Bingo( ) {
		this.cards = new ArrayList<>( );
		this.draws = new ArrayList<>( );
	}
	
	/**
	 * Runs the bingo game. Continues drawing numbers until one of the card has a
	 * Bingo. The first winning bingo card will be returned.
	 * 
	 * @return The winning card that had the first Bingo
	 */
	public BingoCard playUntilFirstWin( ) {
		// go over all numbers in the draw sequence
		for( final int num : draws ) {
			
			// check with the bingo cards for hits
			for( final BingoCard card : cards ) {
				// Bingo?
				if( card.check( num ) ) {
					// yes! store winning number and return the winning card
					lastwinner = num;
					return card;
				}
			}
		}
		
		// no winner
		return null;
	}
	
	/**
	 * Runs the bingo game until all numbers of the sequence have been drawn. The
	 * last winning bingo card will be returned and the last winning number will be
	 * stored.
	 * 
	 * @return The card that had Bingo the last
	 */
	public BingoCard playUntilLastWin( ) {
		// keep track of last winning Bingo card
		BingoCard lastwin = null;
		
		// go over all numbers and remove winning cards
		for( final int num : draws ) {
			for (int i = cards.size( ) - 1; i >= 0; i-- ) {
				final BingoCard card = cards.get( i );
				if( card.check( num ) ) {
					// we have a winner, store the number and card as the last winner
					lastwin = cards.remove( i );
					lastwinner = num;
				}
			}
		}
		
		// game is over, return last winning card
		return lastwin;
	}
	
	/**
	 * @return The last number that lead to a Bingo
	 */
	public int getLastWinner( ) {
		return lastwinner;
	}

	/**
	 * Creates a new Bingo game from a list of Strings. The top line contains
	 * a sequence of numbers to draw in the game, the remaining lines describe
	 * the participating Bingo cards
	 * 
	 * @param game The game as string
	 * @return The Bingo game
	 */
	public static Bingo fromStringList( final List<String> game ) {
		final Bingo b = new Bingo( );
		final List<String> input = new ArrayList<>( game );
		
		// first group is the number sequence
		for( final String n : input.remove( 0 ).split( "," ) )
			b.draws.add( Integer.parseInt( n ) );
		
		// the rest are bingo cards
		for( final String c : input )
			b.cards.add( BingoCard.fromString( c ) );

		return b;
	}
}
