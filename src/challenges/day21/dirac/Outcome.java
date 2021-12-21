package challenges.day21.dirac;

/**
 * Simple container for game outcomes
 * 
 * @author Joris
 */
public class Outcome {
	/** The wins per player */
	private long[] wins;
	
	/**
	 * Creates a new Outcomes container for the number of players
	 * 
	 * @param players The number of players in the game
	 */
	public Outcome( final int players ) {
		this.wins = new long[ players ];
	}
	
	/**
	 * Creates a new Outcome container
	 * 
	 * @param w The wins per player
	 */
	public Outcome( final long[] wins ) {
		this.wins = wins.clone( );
	}
	
	/**
	 * Adds the outcomes of another outcomes object to this one
	 * 
	 * @param outs The other outcomes
	 */
	public void add( final Outcome outs ) {
		for( int i = 0; i < wins.length; i++ )
			wins[i] += outs.wins[i];
	}
	
	/** @return The wins as an array of long values */
	public long[] toLongArray( ) {
		return wins;
	}

	/** @return A copy of the outcome */
	public Outcome copy( ) {
		return new Outcome( this.wins );
	}
	
	/**
	 * Creates a new outcome in which the given player wins
	 * 
	 * @param players The number of players
	 * @param winner The index of the player that wins
	 * @return A new outcome object
	 */
	public static Outcome playerWins( final int players, final int winner ) {
		final long[] wins = new long[ players ];
		wins[ winner ]++;
		return new Outcome( wins );
	}
}
