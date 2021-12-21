package util.geometry;

import java.util.function.BinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A N dimensional coordinate
 * 
 * @author Joris
 */
public class CoordND {
	/** The coordinate value it holds per axis*/
	public final int[] values; 
	
	/** The number of dimensions it holds */
	public final int N;
	
	/** The cached hash code, it is immutable */
	private final int hashcode;
	
	/**
	 * Creates a new unit vector CoordND
	 * 
	 * @param dimensions The number of dimensions
	 * @param value The value to set for all dimensions
	 */
	public CoordND( final int dimensions, final int value ) {
		N = dimensions;
		values = new int[ N ];
		for( int i = 0; i < N; i++ )
			values[i] = value;		
		
		this.hashcode = toString( ).hashCode( );
	}
	
	/**
	 * Creates a new n-dimensional coordinate
	 * 
	 * @param values The values of the coordinate per axis
	 */
	public CoordND( final int... values ) {
		this.values = values;
		this.N = values.length;
		this.hashcode = toString( ).hashCode( );
	}
	
	/**
	 * Reduces all the axis values to a scalar value using the specified
	 * reduction function
	 * 
	 * @param redfunc The reduction function
	 * @return The scalar value
	 */
	public int reduce( final IntBinaryOperator redfunc ) {
		return IntStream.of( values ).reduce( redfunc ).getAsInt( );
	}
	
	/**
	 * Maps a function to all the dimensions of this coordinate
	 * 
	 * @param mapfunc The function to map to all dimension values
	 * @return A new coordinate with the mapping applied
	 */
	public CoordND map( final UnaryOperator<Integer> mapfunc ) {
		final int[] result = new int[ N ];
		for( int i = 0; i < N; i++ )
			result[i] = mapfunc.apply( values[i] );
		
		return new CoordND( result );		
	}
	
	/**
	 * Returns coordinate that is the combination of this and the other
	 * coordinate
	 * 
	 * @param coord The other coordinate
	 * @param combfunc The combination function to apply per dimension
	 * @return A new coordinate that contains the reduced version of both
	 *   coordinates
	 * @throws IllegalArgumentException if the dimensions of the other
	 *   coordinate do not match 
	 */
	public CoordND combine( final CoordND coord, final BinaryOperator<Integer> combfunc ) {
		if( coord.N != N ) throw new IllegalArgumentException( "The number of dimensions do not agree" );
		
		final int[] result = new int[ N ];
		for( int i = 0; i < N; i++ )
			result[i] = combfunc.apply( this.values[i], coord.values[i] );
		
		return new CoordND( result );
	}
	
	/**
	 * Shorthand for <code>reduce( coord, Math::min )</code> to return the
	 * minimum value for each dimension as a new coord
	 * 
	 * @param coord The other coordinate
	 * @return A new coordinate that contains the minimum value for each dimension
	 */
	public CoordND min( final CoordND coord ) {
		return combine( coord, Math::min );
	}

	
	/**
	 * Moves the coordinate by the specified delta per dimension
	 * 
	 * @param delta The increase/decrease per dimension
	 * @return The new coordinate at (x + dx, y + dy, z + dz, ...)
	 * @throws IllegalArgumentException if the delta array does not equal the
	 *   coordinate's dimensions
	 */
	public CoordND move( final int... delta ) {
		// check length
		if( delta.length != N ) 
			throw new IllegalArgumentException( "The delta array must hold a value for exactly " + N + " dimensions" );
		
		return combine( new CoordND( delta ), (x,y) -> (x + y) );
	}
	
	/**
	 * Returns difference with other coord in all axis
	 * 
	 * @param coord The other coordinate
	 * @param absolute True to return the absolute value
	 * @return New coordinate that represents the (absolute) difference per dimension
	 */
	public CoordND diff( final CoordND coord, final boolean absolute ) {
		return combine( coord, (x,y) -> absolute ? Math.abs(x - y) : x - y );
	}
	
	
	/**
	 * Computes the Manhattan distance from this coordinate to another coordinate
	 * 
	 * @param coord The other coordinate
	 * @return The Manhattan distance to the other coord, i.e. the sum of 
	 *   absolute differences over all axes
	 */
	public int getManhattanDist( final CoordND coord ) {
		if( coord.N != N ) throw new IllegalArgumentException( "The number of dimensions do not agree" );

		return diff( coord, true ).reduce( Math::addExact );
	}
	
	/**
	 * @returns The size of this 3D coordinate, which is defined by the square
	 * root of the sum of squared elements
	 */
	public double size( ) {
		int size = 0;
		for( int i = 0; i < N; i++ )
			size += values[i] * values[i];
		return Math.sqrt( size );
	}
	
	/** @returns a coordinate string (x,y,z,...) */
	@Override
	public String toString( ) {
		if( N <= 0 ) return "()";
		
		String res = "(" + values[0];
			for( int i = 1; i < N; i++ )
			res += "," + values[i];
		res += ")";
		
		return res;
	}
	
	/** @return The string's hashcode */
	@Override
	public int hashCode( ) {
		return hashcode;
	}

	/**
	 * Checks if this coordinate is equal to another object
	 * 
	 * @param obj The object to compare to
	 * @return True iff the other object is a CoordND, has equal dimensions and
	 *   equal values per dimension
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof CoordND) ) return false;
		final CoordND c = (CoordND) obj;
		
		// check if number of dimensions agree and all values are equal
		if( c.N != N ) return false;		
		for( int i = 0; i < N; i++ )
			if( values[i] != c.values[i] ) return false;
		return true;
		
	}
}
