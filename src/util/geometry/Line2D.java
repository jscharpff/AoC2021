package util.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Captures a unmutable 2D line segment
 * 
 * @author schar503
 */
public class Line2D {
	/** The line starting coordinate */
	public final Coord2D A;
	
	/** The line ending coordinate */
	public final Coord2D B;

	/**
	 * Creates a new lien segment from point A to B
	 * 
	 * @param a The starting point
	 * @param b The ending point
	 */
	public Line2D( final Coord2D A, final Coord2D B ) {
		this.A = A; this.B = B;
	}
	
	
	/**
	 * Computes list of points on line segment from A to B with step size 1
	 * 
	 * @return The list of points from A to B
	 */
	public List<Coord2D> getPoints( ) {
		return getPoints( 1 );
	}
	

	/**
	 * Compute list of points on this line segment, with steps of length delta
	 * from point A to B. Note that the end coordinate of the segment is not
	 * guaranteed to be in the output for step sizes larger than 1, as it may be
	 * "overshot". I.e., taking steps of size > 1 will not end up in B perfectly.
	 * 
	 * @param delta The step size, both horizontal and vertical
	 * @return The list of points from A to B
	 */
	public List<Coord2D> getPoints( final int delta ) {
		return getPoints( delta, delta );
	}
	
	/**
	 * Compute list of points on this line segment, with steps of length delta 
	 * from point A to B. Note that the end coordinate of the segment is not
	 * guaranteed to be in the output for step sizes larger than 1, as it may be
	 * "overshot". I.e., taking steps of size d>1 will not end up in B perfectly.
	 * 
	 * @param deltax The horizontal step size
	 * @param deltay The vertical step size
	 * @return The list of points from A to B
	 */
	public List<Coord2D> getPoints( final int deltax, final int deltay ) {
		final List<Coord2D> points = new ArrayList<>( );
		
		// store absolute deltas for convenience
		final int dx = Math.abs( deltax );
		final int dy = Math.abs( deltay );

		// determine X and Y directions
		final int xinc = (B.x - A.x) == 0 ? 0 : ((B.x - A.x) >= 0 ? dx : -dx);
		final int yinc = (B.y - A.y) == 0 ? 0 : ((B.y - A.y) >= 0 ? dy : -dy);
		
		// "walk" from A to B in step sizes d, start from A
		int x = A.x; int y = A.y;
		points.add( new Coord2D( x, y ) );
		
		// and keep walking until we are less than d away from B
		while( Math.abs( B.x - x ) >= dx || Math.abs( B.y - y ) >= dy ) {
			if( Math.abs( B.x - x ) >= dx ) x += xinc;
			if( Math.abs( B.y - y ) >= dy ) y += yinc;
			
			points.add( new Coord2D( x, y ) );
		}

		// return the coordinates we've traversed
		return points;
	}

	/**
	 * @return True iff the line segment is a horizontal line
	 */
	public boolean isHorizontal( ) {
		return A.y == B.y;
	}
	
	/**
	 * @return True iff the line segment is a vertical line
	 */
	public boolean isVertical( ) {
		return A.x == B.x;
	}
	
	/**
	 * Creates a 2D line from a string description coord1 -> coord2
	 * 
	 * @param line The string description of the line
	 * @return The Line2D
	 * @throws IllegalArgumentException if the format is incorrect
	 */
	public static Line2D fromString( final String line ) throws IllegalArgumentException {
		final String[] coords = line.split( "->" );
		if( coords.length != 2 ) throw new IllegalArgumentException( "Invalid line: " + line );
		
		// try and parse the individual coordinates and return the line between them
		return new Line2D( Coord2D.fromString( coords[0] ), Coord2D.fromString( coords[1] ) );
	}
	
	/**
	 * @return String representation of the line
	 */
	@Override
	public String toString( ) {
		return A + " -> " + B;
	}
}
