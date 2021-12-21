package challenges.day21.dirac;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A not so friendly game of Dirac Dice with a QUANTUM DICE that will open up a
 * new universe for every throw of the dice *DUM*DUM*DUM*DUM*
 *  
 * @author Joris
 */
public class QuantumDirac {
	/** The number of players in this game */
	private final int N;
	
	/** The starting positions of the players in the inital game */
	private final int[] position;
	
	/** Memoise game outcomes for every combination of start positions, score and dice */
	private final Map<GameState, Outcome> Mwinner;
	
	/** Cache hits */
	private long cachehits = 0;
	
	/**
	 * Create a new QuantumDirac game with the specified DiracDice game as its
	 * initial game settings
	 * 
	 * @param start The player starting positions
	 */
	private QuantumDirac( final int... start ) {
		N = start.length;
		position = new int[ N ];
		for( int i = 0; i < N; i++ ) position[i] = start[i];
		
		// memoize winners for every combination of position, score and dice
		Mwinner = new HashMap<GameState, Outcome>( );
	}
	
	/**
	 * Plays the Quantum DiracDice game until the score has been reached. The
	 * game will be played in several dimensions all at once, so instead of a
	 * single winner, this game will return an array that contains the number of
	 * wins for every players summed over all universes.
	 * 
	 * @param winscore The score required to win the game in any of the universes
	 * @return An array that for every player in the game returns the number of
	 *   universes it won in
	 */
	public long[] play( final int winscore ) {
		cachehits = 0;
		final Outcome outcomes = play( winscore, new GameState( position, new int[ position.length ], 0 ) );
		System.out.println( "Cache hits: " + cachehits );
		return outcomes.toLongArray();
	}
	
	/**
	 * Plays a single round of the game from the given current game state
	 * 
	 * @param winscore The score to reach to win the game
	 * @param state The current game state
	 * @return The outcomes that will result from this game state
	 */
	private Outcome play( final int winscore, final GameState state ) {
		// return stored win count if we know it
		if( Mwinner.containsKey( state ) ) return Mwinner.get( state ).copy();
		
		// check if we have a winner now?
		final int winner = state.hasWinner( winscore );
		final Outcome out;
		if( winner != -1 ) {
			// yes, return this outcome
			out = Outcome.playerWins( N, winner );
		} else { 
			// nope, roll the dice three times, opening up another 3^3 universes
			final List<GameState> newstates = state.roll( 3 );
			out = new Outcome( N );
			for( final GameState gs : newstates ) {
				out.add( play( winscore, gs ) );
			}
		}
		
		// store the resulting outcome for future re-use
		Mwinner.put( state, out );
		return out;		
	}


	/**
	 * Creates a new Quantum DiracDice game from a list of strings
	 * 
	 * @param input List of strings that describe the starting positions of the
	 *   players
	 * @return A new game
	 */
	public static QuantumDirac fromStringList( final List<String> input ) {
		final int[] in = new int[ input.size( ) ];
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "Player (\\d+) starting position: (\\d+)" ).matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid player description in input: " + s );
			
			// set starting position of the player
			in[ Integer.parseInt( m.group( 1 ) ) - 1 ] = Integer.parseInt( m.group( 2 ) ) - 1; 
		}
		
		return new QuantumDirac( in ); 
	}

}
