package challenges.day16.packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.LongStream;

/**
 * Packet that represents an operation
 * 
 * @author Joris
 */
public class OperandPacket extends Packet {
	/** The operation to perform */
	protected final Operand op;
	
	/** The packets to operate upon */
	protected Packet[] packets;
	
	/** The available operands */
	protected enum Operand {
		Add ('+', 0), Multiply ('*', 1), 
		Min ('m', 2), Max( 'M', 3),
		GreaterThan ('>', 5), LessThan ('<', 6),
		Equals( '=', 7);
		
		/** The symbol representation of the operand */
		protected final char symbol;
		
		/** The typeID of the operands (used in packet decoding) */
		protected final int typeID;
		
		/**
		 * Creates a new Operand
		 * 
		 * @param symbol The character that represents it
		 * @param typeID The typeID of this operation for packet decoding
		 */
		private Operand( final char symbol, final int typeID ) { this.symbol = symbol; this.typeID = typeID; }

		/**
		 * Reconstructs the operand from its typeId. If no operand exists for the
		 * specified typeId, null is returned
		 * 
		 * @param typeId The type ID to find operand for
		 * @return The operand corresponding to the type, null if no such operand
		 *   exists
		 */
		protected static Operand fromTypeID( final int typeId ) {
			for( final Operand op : values( ) ) {
				if( op.typeID == typeId ) return op;
			}
			
			return null;
		}
		
		/** 
		 * @return True if the operand is a strictly binary operator, e.g. it 
		 *   requires exactly 2 arguments for its execution
		 */
		protected boolean isBinary( ) {
			return this == LessThan || this == GreaterThan || this == Equals;
		}
	}

	/**
	 * Creates a new OperandPacket that captures the specified operation over
	 * the given sub-packets
	 * 
	 * @param version The version of the packet
	 * @param op The operation
	 * @param packets The sub-packets
	 */
	public OperandPacket( int version, final Operand op, final Packet... packets  ) {
		super( version );
		
		this.op = op;
		this.packets = packets;
		
		// perform a sanity check of strictly binary operators
		if( op.isBinary( ) && packets.length != 2 ) {
			throw new RuntimeException( "Invalid number of parameters (" + packets.length + ") for binary operand " + op ); 
		}
	}
	
	/**
	 * Reduces the packet to its value by evaluating the operation described by
	 * its operand and arguments
	 * 
	 * @return The value resulting from evaluating the operation
	 */
	@Override
	public long reduce( ) {
		// create a stream of the parameters, for convenient processing
		final LongStream args = Arrays.stream( packets ).mapToLong( Packet::reduce ); 
		
		switch( op ) {
			case Add: return args.reduce( Math::addExact ).getAsLong( );
			case Multiply: return args.reduce( Math::multiplyExact ).getAsLong( );
			
			case Min: return args.reduce( Math::min ).getAsLong( );
			case Max: return args.reduce( Math::max ).getAsLong( );
			
			case GreaterThan: return packets[0].reduce( ) > packets[1].reduce( ) ? 1 : 0;
			case LessThan: return packets[0].reduce( ) < packets[1].reduce( ) ? 1 : 0;
			case Equals: return packets[0].reduce( ) == packets[1].reduce( ) ? 1 : 0;
			
			default:
				throw new RuntimeException( "Operand not implemented: "+ op );
		}
	}
	
	/** @return The operation as a string, sub packets are stringified recursively */ 
	@Override
	public String toString( ) {
		String res = "(" + op;
		for( final Packet p : packets )	res += ", " + p;
		res += ")";
		return res;
	}

	/**
	 * Decodes the literal value packet from a bit string, consumes the bits
	 * decoded
	 * 
	 * @param version The version of the packet
	 * @param op The operand
	 * @param data The payload data describing the packet contents
	 */
	protected static OperandPacket decode( final int version, final Operand op, final Payload data ) {
		// determine operator mode
		final boolean immediate = data.consume( ) == '0';
		
		final List<Packet> subpackets = new ArrayList<>( );
		if( immediate ) {
			// immediate mode will treat the remaining data as packages
			// determine total bit length of sub packets and process them
			final int length = data.consumeInt( 15 );
			final Payload pdata = data.consumePayload( length );

			while( pdata.size( ) > 0 ) {
				subpackets.add( Packet.decode( pdata ) );
			}
		} else {
			// non-immediate mode will read and process a number of sub packages
			// determine the number of sub packets
			final int packets = data.consumeInt( 11 );
			
			for( int i = 0; i < packets; i++ ) {
				subpackets.add( Packet.decode( data ) );
			}
		}
		
		// return the decoded operand
		return new OperandPacket( version, op, subpackets.toArray( new Packet[0] ) );
	}

	/** @return The sum of this packets version plus that of all sub packets */
	@Override
	public int sumVersions( ) {
		int sum = super.sumVersions( );
		for( final Packet p : packets ) sum += p.sumVersions( );
		return sum;
	}
}
