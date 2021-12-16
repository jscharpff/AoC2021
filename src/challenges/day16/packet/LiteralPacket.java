package challenges.day16.packet;

/**
 * Packet that holds a literal value, i.e. a constant
 * 
 * @author Joris
 */
public class LiteralPacket extends Packet {
	/*** The type ID of this packet type */
	protected final static int TypeID = 4;
	
	/*** The actual literal value stored by this packet */
	protected final long value;
	
	/**
	 * Creates a new LiteralPacket
	 * 
	 * @param version The version of the packet
	 * @param value The value to hold
	 */
	public LiteralPacket( final int version, final long value ) {
		super( version );
		this.value = value;
	}
	
	/** @return Simply return the value of the literal packet */
	@Override
	public long reduce( ) {
		return value;
	}
	
	/** @return The literal value of the packet */
	@Override
	public String toString( ) {
		return "(" + value + ")";
	}

	/**
	 * Decodes the literal value packet from a bit string. This operation will
	 * consume the (part of the) bit string that is decoded 
	 * 
	 * @param version The version of the packet
	 * @param data The payload data describing the packet contents
	 */
	protected static LiteralPacket decode( final int version, final Payload data ) {
		// read all the groups in the bit string and convert them into a value
		long value = 0;
		boolean hasNext = true;
		while( hasNext ) {
			// perform a "shift left" of 4 bits
			value *= 16;
			
			// get the next part of four bits and add them to the value
			hasNext = data.consume( ) == '1';
			value += data.consumeInt( 4 );
		}
		
		return new LiteralPacket( version, value );
	}
}
