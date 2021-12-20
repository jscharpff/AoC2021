package util.geometry;

import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a 2D coordinate window between two coordinates
 * 
 * @author Joris
 */
public class Window2D implements Iterable<Coord2D> {
	/** The window minimal and maximal coordinates */
	protected Coord2D minCoord, maxCoord;
	
	/**
	 * Constructs a new empty window
	 */
	public Window2D( ) {
		clear( );
	}
	
	/**
	 * Constructs a new window from the given min and max x and y values
	 * 
	 * @param xmin
	 * @param ymin
	 * @param xmax
	 * @param ymax
	 */
	public Window2D( final int xmin, final int ymin, final int xmax, final int ymax ) {
		minCoord = new Coord2D( xmin, ymin );
		maxCoord = new Coord2D( xmax, ymax );
	}
	
	/**
	 * Constructs a new window that spans the given coordinates
	 * 
	 * @param c1 
	 * @param c2 
	 */
	public Window2D( final Coord2D c1, final Coord2D c2 ) {
		this( Math.min( c1.x, c2.x ), Math.min( c1.y, c2.y ),
				Math.max( c1.x, c2.x ), Math.max( c1.y, c2.y ) );
	}
	
	/**
	 * Checks whether the window includes the coordinate, if not it is resized
	 * to include it
	 * 
	 * @param coord The coordinate to include
	 */
	public void include( final Coord2D coord ) {
		// first coordinate of the window?
		if( empty() ) {
			minCoord = coord;
			maxCoord = coord;
			return;
		}
		
		// update bounds if necessary
		minCoord = new Coord2D( Math.min( minCoord.x, coord.x ), Math.min( minCoord.y, coord.y ) );
		maxCoord = new Coord2D( Math.max( maxCoord.x, coord.x ), Math.max( maxCoord.y, coord.y ) );
	}
	
	/**
	 * Resizes the window to fit all coordinates in the set
	 * 
	 * @param coords The coordinates to fit in
	 */
	public void resize( final Collection<Coord2D> coords ) {
		// resize to empty coordinate set means clearing the window
		if( coords.size( ) == 0 ) {
			clear( );
			return;
		}
		
		// compute new bounds
		int minx = Integer.MAX_VALUE, miny = Integer.MAX_VALUE;
		int maxx = -1, maxy = -1;
		for( final Coord2D c : coords ) {
			if( c.x < minx ) minx = c.x; if( c.y < miny ) miny = c.y;
			if( c.x > maxx ) maxx = c.x; if( c.y > maxy ) maxy = c.y;
		}
		
		// set the new bounds
		minCoord = new Coord2D( minx, miny );
		maxCoord = new Coord2D( maxx, maxy );
	}
	
	/**
	 * Clears the entire window
	 */
	public void clear( ) {
		minCoord = null;
		maxCoord = null;
	}
	
	/**
	 * Tests whether the given coordinate is contained by this window, i.e. its
	 * coordinates are within the min and max values.
	 * 
	 * @param coord The coordinate to test
	 * @return True iff x in [xmin, xmax] and y in [ymin, ymax]
	 */
	public boolean contains( final Coord2D coord ) {
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		
		return coord.x >= minCoord.x && coord.y >= minCoord.y &&
				coord.x <= maxCoord.x && coord.y <= maxCoord.y;
	}
	
	/**
	 * Tests whether the given coordinate is on the border of this window
	 * 
	 * @param coord The coordinate to test
	 * @return True iff x = xmin/xmax or y = ymin/ymax
	 */
	public boolean onBorder( final Coord2D coord ) {
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		
		return coord.x == minCoord.x || coord.y == minCoord.y ||
				coord.x == maxCoord.x || coord.y == maxCoord.y;
	}
	
	/**
	 * @return True iff the window is currently empty
	 */
	public boolean empty( ) {
		return minCoord == null;
	}

	/**
	 * @return The size of the window per axis
	 */
	public Coord2D size( ) {
		if( empty( ) ) return null;
		return new Coord2D( maxCoord.x - minCoord.x + 1, maxCoord.y - minCoord.y + 1 );
	}
	
	/** @return The minimal coordinate values */
	public Coord2D getMinCoord( ) {
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		return minCoord;
	}

	/** @return The maximal coordinate values */
	public Coord2D getMaxCoord( ) {
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		return maxCoord;
	}
	
	/** @return The lowest value of the x axis */
	public int getMinX( ) { 
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		return getMinCoord( ).x;
	}

	/** @return The lowest value of the y axis */
	public int getMinY( ) { 
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		return getMinCoord( ).y;
	}

	/** @return The highest value of the x axis */
	public int getMaxX( ) { 
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		return getMaxCoord( ).x;
	}

	/** @return The highest value of the y axis */
	public int getMaxY( ) { 
		if( empty( ) ) throw new RuntimeException( "The current window is not defined" );
		return getMaxCoord( ).y;
	}

	/**
	 * Creates an Iterator that goes over all coordinates in the window
	 * 
	 * @return An iterator of coordinates that goes over all coordinates in the
	 * window span, from (minX,minY) to (maxX, maxY). Iteration is performed
	 * horizontally, i.e. increasing x until end of row.
	 */
	@Override
	public Iterator<Coord2D> iterator( ) {
		// no coordinates in this window?
		if( empty( ) ) return new Iterator<Coord2D>( ) { 
			@Override	public boolean hasNext( ) { return false; }
			@Override	public Coord2D next( ) { return null; }
		};
		
		return new Iterator<Coord2D>( ) {
			/** Current coordinate */
			protected Coord2D curr = minCoord.move( -1, 0 );
			
			/** Start coordinate */
			protected final Coord2D start = minCoord;
			
			/** End coordinate */
			protected final Coord2D end = maxCoord;
			
			@Override
			public Coord2D next( ) {
				if( curr.x < end.x ) curr = curr.move( 1, 0 );
				else curr = new Coord2D( start.x, curr.y + 1 );

				return curr;
			}
			
			@Override
			public boolean hasNext( ) {
				return !end.equals( curr );
			}
		};
	}
	
	/** @return The string (xmin,ymin)-(xmax,ymax) describing the 2D window */
	@Override
	public String toString( ) {
		if( empty( ) ) return "(empty)";
		return minCoord.toString( ) + "-" + maxCoord.toString( ); 
	}
}
