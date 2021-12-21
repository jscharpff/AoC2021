package challenges.day21.dirac;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single, unique state of the game
 * 
 * @author Joris
 */
public class GameState {
	/** The player's positions */
	private final int[] position;
	
	/** The player's scores */
	private final int[] score;
	
	/** The player up next */
	private final int player;
	
	/** The String that describes this state */
	private final String state;
	
	/**
	 * Creates a new container for a game state
	 */
	public GameState( final int[] pos, final int[] score, final int p ) {
		this.position = pos.clone( );
		this.score = score.clone( );
		this.player = p;
		
		// the unique state description string
		state = "" + p + position[0] + position[1] + score[0] + "," + score[1];
	}
	
	/**
	 * Rolls the dice of the active player, opening up 3^ new universes
	 * 
	 * @return Three new game states that reflect the state of the game in all
	 *   three freshly opened universes
	 */
	public List<GameState> roll( final int numrolls ) {
		// get player who is rolling
		final int p = this.player;
		
		// open up a universe for every possible combination of dice rolls
		// generate positions where the current player may end up in
		List<Integer> newpos = new ArrayList<>( 1 );
		newpos.add( position[p] );
		for( int i = 0; i < numrolls; i++ ) {
			final List<Integer> rollpos = new ArrayList<>( newpos.size( ) * 3 );
			for( final int pos : newpos )
				for( final int roll : new int[] { 1, 2, 3 } ) {
					rollpos.add( (pos + roll) % 10 );
				}
			newpos = rollpos;
		}
		
		// and for every new position, generate a new game state
		final List<GameState> newstates = new ArrayList<>( newpos.size( ) );
		for( final int pos : newpos )
			newstates.add( move( p, pos ) );

		return newstates;
	}
	
	/**
	 * Generates the game state that corresponds to the player moving to the 
	 * specified position from the current state, increasing its score as a
	 * result
	 * 
	 * @param p The player that moves
	 * @param pos The position that the player ends up in
	 * @return The new game state as a result
	 */
	private GameState move( final int p, final int pos ) {
		final int[] positions = position.clone( );
		positions[ p ] = pos;
		final int[] scores = score.clone( );
		scores[ p ] += pos + 1;
		
		return new GameState( positions, scores, (1 - p) );
	}
	
	/**
	 * Checks if there is a winning player in this state
	 * 
	 * @param winscore
	 * @return The index of the winning player
	 */
	public int hasWinner( final int winscore ) {
		for( int i = 0; i < score.length; i++ )
			if( score[i] >= winscore ) return i;
		
		return -1;
	}
	
	/**
	 * Checks whether this game state equals another
	 * 
	 * @param obj The other object to test against
	 * @return True iff the object is a valid GameState and their states equal in
	 *   all elements
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof GameState) ) return false;
		return state.equals( ((GameState)obj).state );
	}
	
	/** @return The has code of the cached state string */
	@Override
	public int hashCode( ) {
		return state.hashCode( );
	}

	/** @return The unique state description string */
	@Override
	public String toString( ) {
		return state;
	}

}
