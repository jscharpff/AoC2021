package util.geometry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A 3 dimensional integer-valued coordinate
 * 
 * @author Joris
 */
public class Coord3D extends CoordND {	
	/** The x, y and z values */
	public final int x, y, z;
	
	/**
	 * Creates a new 3D coordinate
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Coord3D( final int x, final int y, final int z ) {
		super( new int[] {x, y, z} );
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Converts a N-dimensional coordinate with 3 dimensions to a Coord3D
	 * 
	 * @param coord The CoordND object of 3 dimensions
	 */
	private static Coord3D fromND( final CoordND coord ) {
		return new Coord3D( coord.values[0], coord.values[1], coord.values[2] );
	}
	
	/**
	 * Moves the coordinate by the specified dx, dy and dz
	 * 
	 * @param dx
	 * @param dy
	 * @param dz
	 * @return The new coordinate at (x + dx, y + dy, z + dz)
	 */
	public Coord3D move( final int dx, final int dy, final int dz ) {
		return Coord3D.fromND( move( new int[] { dx, dy, dz } ) );
	}
	
	/**
	 * Adds the axis values of the other coordinate
	 * 
	 * @param coord The coordinate values to add to this one
	 * @return A new coordinate at (x + coord.x, y + coord.y, z + coord.z)
	 */
	public Coord3D add( final Coord3D coord ) {
		return move( coord.x, coord.y, coord.z );
	}
	
	/**
	 * Rotates the coordinate as specified relative to the (0,0,0) coordinate.
	 * This operation computes a new Rotation3D matrix and then applies it to the
	 * coordinate. If this matrix is already available, the function 
	 * <code>rotate( final Rotation3D rotation )</code> should be used.
	 * 
	 * @param rotx The x-axis rotation
	 * @param roty The y-axis rotation
	 * @param rotz The z-axis rotation
	 * @return The rotated coordinate
	 */
	public Coord3D rotate( final int rotx, final int roty, final int rotz ) {
		return rotate( new Rotation3D( rotx, roty, rotz ) );
	}
	
	/**
	 * Rotates this coordinate relative to the (0,0,0) coordinate using the
	 * specified rotation matrix.
	 * 
	 * @param rotation The rotation matrix
	 * @return The rotated coordinate
	 */
	public Coord3D rotate( final Rotation3D rotation ) {
		final double[] result = rotation.apply( new double[] { x, y, z } );
		return new Coord3D( (int)result[0], (int)result[1], (int)result[2] );
	}

	/**
	 * Computes the per-axis difference between this coord and another
	 * 
	 * @param coord The other coordinate
	 * @param absolute true to return absolute differences
	 * @return The difference as a new cooridnate
	 */
	public Coord3D diff( final Coord3D coord, boolean absolute ) {
		return Coord3D.fromND( super.diff( coord, absolute ) );
	}
	
	/**
	 * Negates the values of all axes and returns the value as a new coordinate
	 * 
	 * @return The coordinate at (-x, -y, -z)
	 */
	public Coord3D negate( ) {
		return new Coord3D( -x, -y, -z );
	}
	
	/**
	 * Creates a new 3D Coordinate from a String representation in either (x,y,z)
	 * or x,y,z format
	 * 
	 * @param coord The coordinate as string
	 * @return The Coord2D
	 * @throws IllegalArgumentException if the format of the coordinate is incorrect
	 */
	public static Coord3D fromString( final String coord ) throws IllegalArgumentException {
		final Matcher m = Pattern.compile( "^\\(?\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)?$" ).matcher( coord.trim( ) );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid coordinate: " + coord );
		
		return new Coord3D( Integer.parseInt( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ), Integer.parseInt( m.group( 3 ) ) );
	}
}
