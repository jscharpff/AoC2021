package challenges.day10.syntaxchecker.error;

import challenges.day10.syntaxchecker.Chunk;

/**
 * Error that is thrown whenever a chunk closing is found that does not match
 * the closing character that was expected for the last seen chunk opening.
 *   
 * @author Joris
 */
@SuppressWarnings( "serial" )
public class ChunkIncorrectError extends SyntaxError {
	/** Store the offending chunk ending */
	protected final char offending;
	
	/**
	 * Creates a ChunkIncorrectError
	 * 
	 * @param expected The chunk closing character that was expected
	 * @param found The chunk closing character that was actually encountered
	 * @param index The index at which it was encountered
	 */
	public ChunkIncorrectError( final char expected, final char found, final int index ) {
		super( "Chunk incorrect, expected " + expected + " but found " + found + " instead", index );
		this.offending = found;
	}
	
	/** @return The offending character */
	public char getOffending( ) {
		return offending;
	}
	
	/**
	 * @return The syntax error score for the chunk type
	 */
	@Override
	public long getScore( ) {
		return Chunk.fromChar( offending ).getIncorrectScore( );
	}
}
