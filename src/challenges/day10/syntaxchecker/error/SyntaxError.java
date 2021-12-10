package challenges.day10.syntaxchecker.error;

/**
 * Abstract class for all errors that can be produced by the syntax checker
 * 
 * @author Joris
 */
@SuppressWarnings( "serial" )
public abstract class SyntaxError extends Exception {
	/** The index at which the error occurs */
	protected final int index;

	/**
	 * Creates a new SyntaxError without line index
	 * 
	 * @param message The error message that describes the problem
	 */
	public SyntaxError( final String message ) {
		this( message, -1 );
	}
	
	/**
	 * Creates a new SyntaxError
	 * 
	 * @param message The error message that describes the problem
	 * @param index The index at whicvh the error occurred
	 */
	public SyntaxError( final String message, final int index ) {
		super( message );
		this.index = index;
	}
	
	/** @return The index at which the error occurred */
	public int getLineIndex( ) {
		return index;
	}
	
	/** @return Computes the syntax error score for the error */
	public abstract long getScore( );
	
	/**
	 * @return The error message, including line index if set
	 */
	@Override
	public String toString( ) {
		return super.toString( ) + (index > -1 ? " (index " + getLineIndex( ) + ")" : index );
	}
}
