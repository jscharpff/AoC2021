package challenges.day16.packet;

import challenges.day16.packet.OperandPacket.Operand;

/**
 * Class representing a packet that either holds a literal value or an
 * operation, possibly involving sub-packets
 * 
 * @author Joris
 */
public abstract class Packet {
	/** The version number of the packet */
	protected final int version;
	
	/**
	 * Creates a new packet
	 * 
	 * @param version The packet version
	 */
	public Packet( final int version ) {
		this.version = version;
	}
	
	/**
	 * Reduces the packet to a numerical value by either performing its operation
	 * or simply returning its value in case of a literal
	 * 
	 * @return The reduced value 
	 */
	public abstract long reduce( );
	
	/** @return String describing the packet */
	// explicitly declaring this enforces its implementation in child classes
	@Override public abstract String toString( );
	
	/**
	 * Decodes a hexadecimal input string into packets
	 * 
	 * @param input The hexadecimal input string
	 * @return The outer-level packet
	 */
	public static Packet fromHex( final String input ) {
		// convert the hex payload into a consumable Payload object 
		final Payload data = new Payload( input );
		
		// recursively parse the packages encoded in the bit string
		return decode( data );
	}
	
	/**
	 * Recursively decodes the payload into hierarchical packets. Consumes the
	 * payload in processing
	 * 
	 * @param data The (remaining) bit string
	 * @return The packet encoded by the bit string, with potential sub packets
	 */
	protected static Packet decode( final Payload data ) {
		// first decode version and type
		final int version = data.consumeInt( 3 );
		final int typeID = data.consumeInt( 3 );

		// create correct package type and let it process the package data
		switch( typeID ) {
			// a literal value packet
			case LiteralPacket.TypeID:
				return LiteralPacket.decode( version, data );
				
			// undecided yet, can be an operand
			default:
				// check if this is an operator
				final Operand op = Operand.fromTypeID( typeID );
				if( op != null ) return OperandPacket.decode( version, op, data );
		}

		// there is no known packet type with the type ID   
		throw new RuntimeException( "Unknown package type in input: " + typeID );
	}
	
	/**
	 * @return Sum of packet versions of this packet and possibly contained
	 *   packets 
	 */
	public int sumVersions( ) {
		return version;
	}
}
