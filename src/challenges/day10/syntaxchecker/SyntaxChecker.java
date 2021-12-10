package challenges.day10.syntaxchecker;

import java.util.Stack;

import challenges.day10.syntaxchecker.error.ChunkIncompleteError;
import challenges.day10.syntaxchecker.error.ChunkIncorrectError;
import challenges.day10.syntaxchecker.error.UnknownCharacterError;

/**
 * Class that performs simple chunk-based syntax checks
 * 
 * @author Joris
 */
public class SyntaxChecker {
	/**
	 * Checks the syntax of the specified line. Throws an exception if a syntax
	 * error is encountered
	 * 
	 * @param line The line of chunks to process
	 * @throws ChunkIncorrectError if an unexpected closing character was
	 *   encountered
	 * @throws ChunkIncompleteError if line has been parsed but not all chunk
	 *   openings were matched against their closing symbols
	 * @throws UnknownCharacterError if the input contains an unknown character
	 */
	public void check( final String line ) throws ChunkIncompleteError, ChunkIncorrectError, UnknownCharacterError {
		// stack of chunk openings that we have seen so far
		final Stack<Character> stack = new Stack<>( );
		
		// go over all characters and process then
		for( int i = 0; i < line.length( ); i++ ) {
			final char c = line.charAt( i );
			
			//opening chunk?
			if( Chunk.isOpening( c ) ) {
				// add to stack and process next character
				stack.push( c );
				continue;
			}
			
			// closing chunk?
			if( Chunk.isClosing( c ) ) {
				// pop the last seen opening character from the stack and 
				// check if this is the closing character we were expecting
				final char open = stack.pop( );
				final char expected_open = Chunk.getExpectedOpening( c );
				if( open != expected_open ) throw new ChunkIncorrectError( Chunk.getExpectedClosing( open ), c, i );
				
				// yes it is, continue to the next character
				continue;
			}
			
			// any other character we didn't expect?
			throw new UnknownCharacterError( c, i );
		}
		
		// all parsed, do we still have open chunks?
		if( stack.size( ) > 0 )
			throw new ChunkIncompleteError( stack );
	}
}
