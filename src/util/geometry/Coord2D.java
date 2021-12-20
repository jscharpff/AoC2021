package util.geometry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple 2 dimensional coordinate
 * 
 *  @author Joris
 */
public class Coord2D {
	/** The horizontal position */
	public final int x;
	
	/** The vertical position */
	public final int y;
	
	/** The cached hash code, it is immutable */
	private final int hashcode;

	/**
	 * Creates a new 2D coordinate
	 * 
	 * @param x The horizontal position
	 * @param y The vertical position
	 */
	public Coord2D( final int x, final int y ) {
		this.x = x;
		this.y = y;
		
		// compute hash code once and store it
		hashcode = toString( ).hashCode( );
	}
	
	/**
	 * Computes new coordinate when moving with specified dx and dy
	 * 
	 * @param dx Horizontal movement
	 * @param dy Vertival movement
	 * @return The new coordinate
	 */
	public Coord2D move( final int dx, final int dy ) {
		return new Coord2D( x + dx, y + dy );
	}
	
	/**
	 * Moves in the specified direction
	 * 
	 * @param direction The angle to move in (0 = North)
	 * @param distance The distance to move
	 * @return The new position
	 */
	public Coord2D moveDir( final int direction, final int distance ) {
		// for now only the 4 major directions are supported
		final int d = direction % 360;
		switch( d ) {
			case 0: return move( 0, -distance );
			case 90: return move( distance, 0 );
			case 180: return move( 0, distance );
			case 270: return move( -distance, 0 );
			default: throw new IllegalArgumentException( "Only major directions are supported" );
		}
	}
	
	/**
	 * Rotates the point along the 0,0 coordinate
	 * 
	 * @param degrees The angle to rotate
	 * @return The new coordinate
	 */
	public Coord2D rotate( final int degrees ) {
		// for now only the 4 major directions are supported
		final int d = (degrees + 360) % 360;
		switch( d ) {
			case 0: return move( 0, 0 );
			case 90: return new Coord2D( -y, x );
			case 180: return new Coord2D( -x, -y  );
			case 270: return new Coord2D( y, -x );
			default: throw new IllegalArgumentException( "Only major directions are supported" );
		}
	}
	
	/**
	 * Computes the difference between this and another point
	 * 
	 * @param coord The other coordinate
	 * @return A new coordinate that represents coord - this
	 */
	public Coord2D diff( final Coord2D coord ) {
		return new Coord2D( coord.x - this.x, coord.y - this.y );
	}
	
	/**
	 * Computes the absolute difference between this and another point
	 * 
	 * @param coord The other coordinate
	 * @return A new coordinate representing the absolute difference
	 */
	public Coord2D diffAbs( final Coord2D coord ) {
		return new Coord2D( Math.abs( coord.x - this.x ), Math.abs( coord.y - this.y ) );
	}
	
	/**
	 * Computes Manhattan distance to other coordinate
	 * 
	 * @param coord The other coordinate
	 * @return The Manhattan distance |dx + dy|
	 */
	public int getManhattanDistance( final Coord2D coord ) {
		return Math.abs( coord.x - this.x ) + Math.abs( coord.y - this.y );
	}
	
	/**
	 * Finds all neighbouring coordinates of this Coord2D
	 * 
	 * @param diagonal True to include diagonal positions
	 * @return Collection containing all neighbouring positions
	 */
	public Collection<Coord2D> getNeighbours( final boolean diagonals ) {
		final Collection<Coord2D> N = new ArrayList<>( diagonals ? 8 : 4 );
		
		// add regular neighbours
		N.add( move( -1,  0 ) );
		N.add( move(  1,  0 ) );
		N.add( move(  0, -1 ) );
		N.add( move(  0,  1 ) );
		
		// diagonals?
		if( !diagonals ) return N;

		// add diagonal neighbours
		N.add( move( -1, -1 ) );
		N.add( move( -1,  1 ) );
		N.add( move(  1, -1 ) );
		N.add( move(  1,  1 ) );
		
		return N;
}
	
	/**
	 * Compares this coordinate to another object
	 * 
	 * @return True iff the other object is a Coord2D and its x and y values
	 *   are equal
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Coord2D) ) return false;
		final Coord2D c = (Coord2D)obj;
		
		return c.x == x && c.y == y;
	}
	
	/**
	 * Creates a new 2D Coordinate from a String representation in either (x,y)
	 * or x,y format
	 * 
	 * @param coord The coordinate as string
	 * @return The Coord2D
	 * @throws IllegalArgumentException if the format of the coordinate is incorrect
	 */
	public static Coord2D fromString( final String coord ) throws IllegalArgumentException {
		final Matcher m = Pattern.compile( "^\\(?\\s*(-?\\d+)\\s*,\\s*(-?\\d+)\\s*\\)?$" ).matcher( coord.trim( ) );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid coordinate: " + coord );
		
		return new Coord2D( Integer.parseInt( m.group( 1 ) ), Integer.parseInt( m.group( 2 ) ) );
	}
	
	/** @return The (x,y) string describing the position */
	@Override
	public String toString( ) {
		return "(" + x + "," + y + ")";
	}
	
	/** @return The unique hash code for the coordinate, used in sets/collections */
	@Override
	public int hashCode( ) {
		return hashcode;
	}
}
