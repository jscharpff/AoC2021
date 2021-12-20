package util;

import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

/**
 * Class to represent and manipulate a bit string of fixed length. All bit
 * indexing is performed little-endian (e.g. right-to-left)
 * 
 * @author Joris
 *
 */
public class BitString implements Iterable<Boolean> {
	/** The value that represents the bit string */
	protected long value;
	
	/** The bit string length */
	protected final int length;
	
	/** The bit mask of all ones corresponding to the length of the bit stream */
	private final long LENGTH_MASK;
	
	/**
	 * Creates a new BitString of length n and initial value 0
	 * 
	 * @param n The bit string length
	 */
	public BitString( final int n ) {
		this.length = n;
		this.LENGTH_MASK = (long)Math.pow( 2, length ) - 1;
		this.value = 0;
	}
	
	/**
	 * Creates a BitString from another BitString
	 * 
	 * @param bits The other BitString
	 */
	public BitString( final BitString bits ) {
		this( bits.length );
		this.value = bits.value;
	}
	
	/**
	 * Sets the bit to true at the specified index
	 * 
	 * @param index The bit index of the bit to set
	 * @return The previous value of the bit
	 * @throws IndexOutOfBoundsException if the index is outside of the bit
	 *   string length
	 */
	public boolean set( final int index ) throws IndexOutOfBoundsException {
		return set( index, true );
	}
	
	/**
	 * Sets the bit at the specified index to a specific value
	 * 
	 * @param index The bit index of the bit to set
	 * @param value The value to set
	 * @return The previous value of the bit
	 * @throws IndexOutOfBoundsException if the index is outside of the bit
	 *   string length
	 */
	public boolean set( final int index, final boolean value ) throws IndexOutOfBoundsException {
		if( index < 0 || index >= length ) throw new IndexOutOfBoundsException( index );

		final boolean oldval = get( index );
		if( value )
			this.value |= indexToBitMask( index );
		else
			this.value &= indexToBitMask( index, true );
		return oldval;
	}
	
	/**
	 * Retrieves the bit value at the specified index
	 * 
	 * @param index The bit index
	 * @return The boolean value of the bit
	 * @throws IndexOutOfBoundsException if the index is outside of the bit
	 *   string length
	 */
	public boolean get( final int index ) throws IndexOutOfBoundsException {
		if( index < 0 || index >= length ) throw new IndexOutOfBoundsException( index );
		
		return (value & indexToBitMask( index )) > 0;
	}
	
	/**
	 * Clears the value of the bit at the specified index
	 * 
	 * @param index The bit index of the bit to clear
	 * @return The previous value of the bit
	 * @throws IndexOutOfBoundsException if the index is outside of the bit
	 *   string length
	 */
	public boolean clear( final int index ) throws IndexOutOfBoundsException {
		return set( index, false );
	}

	
	/**
	 * Negates the bit string
	 */
	public void negate( ) {
		// negate and keep long value within bit length
		value = negate( value );
	}
	
	/**
	 * @return The length of the bit string
	 */
	public int length( ) {
		return length;
	}
	
	
	/**
	 * Performs length-bound negation to make sure the resulting long value
	 * still correctly represents only the bits in this strin (not all other bits
	 * of a 64-bit long variable) 
	 * 
	 * @param val The value to negate
	 * @return The negation of the value in the string length part
	 */
	private long negate( final long val ) {
		return ~val & LENGTH_MASK;
	}
	
	
	/**
	 * Computes the bit mask with a 1 in the bit that corresponds to the index
	 * 
	 * @param index The bit index 
	 * @return The mask with all zeroes except at the specified bit index
	 */
	private long indexToBitMask( final int index ) {
		return indexToBitMask( index, false );
	}
	
	/**
	 * Computes the bit mask with either a 1 or 0 in the bit that corresponds
	 * to the index, depending on the negate parameter
	 * 
	 * @param index The bit index
	 * @param negate True to negate the mask, e.g. get all ones except for the bit
	 *   ate the specified index 
	 * @return The mask for the index
	 */
	private long indexToBitMask( final int index, final boolean negate ) {
		long mask = (long)Math.pow( 2, index );
		if( negate ) mask = negate( mask );
		return mask;
	}
	
	/**
	 * Creates a stream of boolean values from the bit string
	 * 
	 * @return The stream of boolean values in the bit string, lowest to highest bit
	 */
	public Stream<Boolean> stream( ) {
		final Builder<Boolean> builder = Stream.builder( );
		for( int i = 0; i < length; i++ )
			builder.add( get( i ) );
		
		return builder.build( );
	}
	
	/**
	 * @return iterator that moves over the bit string, from lowest to highest bit
	 */
	@Override
	public Iterator<Boolean> iterator( ) {
		return new Iterator<Boolean>( ) {
			/** The current bit index */
			int curr = 0;
			
			@Override
			public Boolean next( ) {
				return get( curr++ );
			}
			
			@Override
			public boolean hasNext( ) {
				return curr < length;
			}
		};
	}
	
	
	/**
	 * Creates a bit string from a long value, string length is computed from 
	 * the number of bits required to capture the value 
	 * 
	 * @param bits The number of bits to use
	 * @param value The value to initialise the bit string from
	 * @return A BitString with length bits that captures the part of the value
	 *   that fits within the bit length
	 */
	public static BitString fromLong( final long value ) {
		final int bits = (int)Math.ceil( Math.log( value + 1 ) / Math.log( 2 ) );		
		return fromLong( value, bits );
	}
	
	/**
	 * Creates a bit string from a long value with a fixed bit length. Overflows
	 * are silently ignored
	 * 
	 * @param value The value to initialise the bit string from
	 * @param bits The number of bits to use
	 * @return A BitString with length bits that captures the part of the value
	 *   that fits within the bit length
	 */
	public static BitString fromLong( final long value, final int bits ) {
		final BitString b = new BitString( bits );
		b.value = value;
		return b;
	}
	
	/**
	 * @return The long value represented by the bit string (unsigned)
	 */
	public long toLong( ) {
		return value;
	}
	
	/** @return The integer value of the bit string (unsafe cast!) */
	public int toInt( ) {
		return (int)value;
	}
	
	
	/**
	 * Creates a bit string from a String representation 
	 * 
	 * @param str The bit string as textual string of ones and zeroes
	 * @return The bit string equivalent
	 * @throws IllegalArgumentException if the bit string contains other 
	 *   characters than zeroes and ones 
	 */
	public static BitString fromString( final String str ) throws IllegalArgumentException {
		final BitString b = new BitString( str.length( ) );
		b.value = Long.valueOf( str, 2 );
		return b;
	}
	
	/**
	 * Converts a hexadecimal string into a bit string with a length of 4 times
	 * the hex string
	 * 
	 * @param hex The hexadecimal input string
	 * @return The bitstring
	 */
	public static BitString fromHex( final String hex ) {
		final BitString b = new BitString( hex.length( ) * 4 );
		b.value = Integer.parseInt( hex, 16 );
		return b;
	}
	
	/**
	 * @return Textual representation of the bit strings in zeroes and ones
	 */
	@Override
	public String toString( ) {
		String res = "";
		for( boolean b : this )
			res = (b ? "1" : "0") + res;
		return res;
	}
}
