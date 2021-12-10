package challenges.day10.syntaxchecker;

/**
 * Set of predefined chunks, their delimiters and syntax error scores
 * 
 * @author Joris
 */
public enum Chunk {
	/** The pre-defined chunks */
	Parenthesis( '(', ')', 3, 1 ),
	SqBrackets( '[', ']', 57, 2 ),
	Brackets( '{', '}', 1197, 3 ),
	Chevrons( '<', '>', 25137, 4 );
	
	/** The opening character */
	private final char open;
	
	/** The closing character */
	private final char close;
	
	/** The incorrect syntax error score for */
	private final int score_incorrect;
	
	/** The incomplete syntax error score */
	private final int score_incomplete;
	
	/**
	 * Creates a new Chunk enumerator
	 * 
	 * @param open The opening character
	 * @param close The closing character
	 * @param scoreincorrect The syntax error score for an incorrect chunk completion
	 * @param scoreincomplete The syntax error score for an incomplete chunk
	 */
	private Chunk( final char open, final char close, final int scoreincorrect, final int scoreincomplete ) {
		this.open = open;
		this.close = close;
		this.score_incorrect = scoreincorrect;
		this.score_incomplete = scoreincomplete;
	}
	
	/**
	 * Finds the expected opening character for the given closing character
	 * 
	 * @param close The closing character
	 * @return The opening character paired to the closing character
	 * @throws IllegalArgumentException if the closing character is unknown
	 */
	public static char getExpectedOpening( final char close ) {
		for( final Chunk ch : Chunk.values( ) )
			if( close == ch.close ) return ch.open;
		
		throw new IllegalArgumentException( "Unknown closing character: " + close);
	}
	
	/**
	 * Finds the expected closing character for the given opening character
	 * 
	 * @param open The opening character
	 * @return The closing character paired to the closing character
	 * @throws IllegalArgumentException if the closing character is unknown
	 */
	public static char getExpectedClosing( final char open ) {
		for( final Chunk ch : Chunk.values( ) )
			if( open == ch.open ) return ch.close;
		
		throw new IllegalArgumentException( "Unknown opening character: " + open);
	}
	
	/**
	 * Check if the specified character is a chunk opening character
	 * 
	 * @param c The character to test
	 * @return True iff the character is the opening character of some chunk
	 */
	public static boolean isOpening( final char c ) {
		for( final Chunk ch : Chunk.values( ) )
			if( ch.open == c ) return true;
		return false;
	}

	/**
	 * Check if the specified character is a chunk closing character
	 * 
	 * @param c The character to test
	 * @return True iff the character is the closing character of some chunk
	 */
	public static boolean isClosing( final char c ) {
		for( final Chunk ch : Chunk.values( ) )
			if( ch.close == c ) return true;
		return false;
	}
	
	/**
	 * Determines the chunk from either the opening or closing character
	 * 
	 * @param c The character
	 * @return The corresponding chunk
	 * @throws IllegalArgumentException if the closing character is unknown
	 */
	public static Chunk fromChar( final char c ) {
		for( final Chunk ch : Chunk.values( ) )
			if( c == ch.open || c == ch.close ) return ch;
		
		throw new IllegalArgumentException( "Unknown opening or closing character: " + c );
	}
	
	/** @return The incorrect syntax score for this chunk */
	public int getIncorrectScore( ) { return score_incorrect; }

	/** @return The incorrect syntax score for this chunk */
	public int getIncompleteScore( ) { return score_incomplete; }
}