package challenges.day10.syntaxchecker.error;

/**
 * Should not be thrown but in here for completeness... This is thrown if the
 * input contains any character that cannot be parsed correctly
 * 
 * @author Joris
 */
@SuppressWarnings( "serial" )
public class UnknownCharacterError extends SyntaxError {
	/** Store the offending chunk ending */
	protected final char offending;
	
	/**
	 * Creates a new UnknownCharacterError
	 * @param unknown The unknown character
	 * @param index The index at which the character was found 
	 */
	public UnknownCharacterError( final char unknown, final int index ) {
		super( "Invalid character in syntax: " + unknown + " instead", index );
		this.offending = unknown;
	}
	
	/** @return The offending character */
	public char getOffending( ) {
		return offending;
	}
	
	/**
	 * @return 0 as this error has no syntax scoring rules 
	 */
	@Override
	public long getScore( ) {
		return 0;
	}
}
