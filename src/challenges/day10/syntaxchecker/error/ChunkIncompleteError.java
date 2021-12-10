package challenges.day10.syntaxchecker.error;

import java.util.Stack;

import challenges.day10.syntaxchecker.Chunk;

/**
 * Error that is thrown when input is processed but there are still unfinished
 * chunk openings.
 * 
 * @author Joris
 */
@SuppressWarnings( "serial" )
public class ChunkIncompleteError extends SyntaxError {
	/** The stack that contains the incomplete opening characters */
	protected final Stack<Character> incomplete;
	
	/**
	 * Creates a new ChunkIncompleteError
	 *  
	 * @param incomplete The stack that contains the unfinished chunk openings 
	 */
	public ChunkIncompleteError( final Stack<Character> incomplete ) {
		super( "Chunk incomplete, chunks uncomplete " + incomplete );
		
		// copy the stack
		this.incomplete = new Stack<>( );
		this.incomplete.addAll( incomplete );
	}
	
	/**
	 * Computes the syntax error score for this error.
	 * 
	 * @return The syntax score
	 */
	@Override
	public long getScore( ) {
		// to determine score, we have to reconstruct the chunks from the expected stack
		final Stack<Character> st = new Stack<>( );
		st.addAll( incomplete );
		
		// for every unfinished chunk we first multiply the current score value by
		// 5 and then add the syntax error score for the chunk to it
		long score = 0;
		while( st.size( ) > 0 ) {
			score *= 5;
			score += Chunk.fromChar( st.pop() ).getIncompleteScore( );
		}
		
		return score;
	}
}
