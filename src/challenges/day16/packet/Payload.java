package challenges.day16.packet;

import aocutil.string.BitString;

/**
 * Container for a bit-encoded payload that offers convenient methods to
 * consume (parts of) the data it contains
 * 
 * @author Joris
 */
public class Payload {
	/** The current (remaining) payload as a bit string */
	protected String payload;
	
	/**
	 * Creates a new, empty payload 
	 */
	private Payload( ) {
		payload = "";
	}
	
	/**
	 * Creates a new empty payload from a Hex encoded string
	 * 
	 * @param hex The hexadecimal input
	 */
	public Payload( final String hex ) {
		this( );
		
		for( int i = 0; i < hex.length( ); i++ )
			payload += BitString.fromHex( "" + hex.charAt( i ) ).toString();
	}
	
	/**
	 * Consumes the next bit and returns the value
	 * 
	 * @return The first bits from the payload
	 */
	public char consume( ) {
		final char data = payload.charAt( 0 );
		payload = payload.substring( 1 );
		return data;
	}
	
	/**
	 * Consumes the number of bits and returns the value
	 * 
	 * @param n The number of bits to consume
	 * @return The first n bits from the payload
	 */
	public String consume( final int n ) {
		final String data = payload.substring( 0, n );
		payload = payload.substring( n );
		return data;
	}

	/**
	 * Consumes the number of bits and converts them into an integer value
	 * 
	 * @param n theThe number of bits to consume
	 * @return The integer value encoded by the bits
	 */
	public int consumeInt( final int n ) {
		return BitString.fromString( consume( n ) ).toInt( );
	}
	
	/**
	 * Consumes the number of bits and returns them as a new payload
	 * 
	 * @param n The number of bits to consume
	 * @return A new payload object with n bits of data
	 */
	public Payload consumePayload( final int n ) {
		final Payload p = new Payload( );
		p.payload = consume( n );
		return p;
	}
	
	/** @return The size of the (remaining) payload */
	public int size( ) {
		return payload.length( );
	}
	
	/** @return The bit string that describes the (remaining) payload */ 
	@Override
	public String toString( ) {
		return "[(" + size() + ")" + payload + "]";
	}
}
