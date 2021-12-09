package util.geometry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Class that captures a of a coordinate based grid
 * 
 * @author Joris
 *
 * @param <T> The data type that of the grid
 */
public class CoordGrid<T> implements Iterable<Coord2D> {	
	/** The map that backs the grid */
	protected final Map<Coord2D, T> map;

	/** Current size of the vent grid, given by a minimal and maximal coordinate */
	private Coord2D minCoord, maxCoord;
	
	/** Default value for all non-set coordinates */
	protected T defaultValue;

	/**
	 * Creates a new coordinate based grid
	 * 
	 * @param defaultValue The default value for non-assigned coordinates
	 */
	public CoordGrid( final T defaultValue ) {
		map = new HashMap<Coord2D, T>( );
		maxCoord = null;
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Adds a new value to the coordinate grid at the specified coordinate
	 * 
	 * @param coord The coordinate to set the value for
	 * @param value The value to set
	 * @return The previous value that was set, null if it was not set before
	 */
	public T add( final Coord2D coord, final T value ) {
		updateBounds( coord );
		return map.put( coord, value );
	}
	
	/**
	 * Retrieves the value for the given coordinate
	 * 
	 * @param coord The coordinate to get the value for
	 * @return The value that is stored at the coordinate or the grid's default
	 *   value if not found
	 */
	public T get( final Coord2D coord ) {
		return get( coord, defaultValue );
	}
	
	/**
	 * Retrieves the value at the coordinate grid at the given x and y position.
	 * This function offers a more convenient fetch operations when iterating
	 * over coordinates by x and y values.
	 * <br/><br/>
	 * Note that it behaves similar to <code>get( Coord2D )</code> in that it
	 * does not check whether the position is actually on the grid, it just
	 * returns the default value if not found. 
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @return The value stored at (x,y) or the default value if not set
	 */
	public T get( final int x, final int y ) {
		return get( new Coord2D( x, y ), defaultValue );
	}
	
	/**
	 * Retrieves the value for the given coordinate. Uses specific default value 
	 * if the coordinate was not stored in the map
	 * 
	 * @param coord The coordinate to get the value for
	 * @param valDefault The default value to return if the coordinate has no
	 *   value in the grid, overrides class-level default value
	 * @return The value stored at the coordinate or the default value if no
	 *   value was stored at the specified coordinate
	 */
	public T get( final Coord2D coord, final T valDefault ) {
		return map.getOrDefault( coord, valDefault );
	}

	/**
	 * @return The collection of coordinates that have a set value in the grid
	 */
	public Set<Coord2D> getKeys( ) {
		return map.keySet( );
	}
	
	/**
	 * @return The collection of values in the grid
	 */
	public Collection<T> getValues( ) {
		return map.values( );
	}
	
	/**
	 * Checks whether the specified coordinate has a value stored in the grid
	 * 
	 * @param coord The coordinate to check
	 * @return True iff a value is stored at the given coordinate (i.e. not the
	 *   default value)
	 */
	public boolean hasValue( final Coord2D coord ) {
		return map.containsKey( coord );
	}
	
	/** 
	 * Determines and returns the size of the current grid, i.e. the span between
	 * minimal and maximal coordinate
	 * 
	 * @return The size of the current grid as 2D coordinate or null if empty
	 */
	public Coord2D size( ) {
		if( map.size( ) == 0 ) return null;
		
		// compute distance between points plus one, this is the span length between
		// min and max coords
		return maxCoord.diffAbs( minCoord ).move( 1, 1 );
	}
	
	/**
	 * Update the current min and max coordinates of the gird, given the new
	 * coordinate
	 * 
	 * @param coord The new coordinate
	 */
	private void updateBounds( final Coord2D coord ) {
		if( minCoord == null ) minCoord = coord;
		if( maxCoord == null ) maxCoord = coord;
		
		minCoord = new Coord2D( Math.min( minCoord.x, coord.x ), Math.min( minCoord.y, coord.y ) );
		maxCoord = new Coord2D( Math.max( maxCoord.x, coord.x ), Math.max( maxCoord.y, coord.y ) );
	}
	
	
	/**
	 * Creates an Iterator that goes over all coordinates in the grid, including
	 * those without a value
	 * 
	 * @return An iterator of coordinates that goes over all coordinates in the
	 * grid span, from (minX,minY) to (maxX, maxY). Iteration is performed
	 * horizontally, i.e. increasing x until end of row.
	 */
	@Override
	public Iterator<Coord2D> iterator( ) {
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

	/**
	 * Generates a grid-like output of the CoordGrid, from top-left coordinate to
	 * the bottom-right using the object's own toString function 
	 * 
	 * @return A grid of values
	 */
	@Override
	public String toString( ) {
		return toString( T::toString );
	}
	
	/**
	 * Generates a grid-like output of the CoordGrid, from top-left coordinate to
	 * the bottom-right using a custom Stringify function  
	 * 
	 * @param stringFunc The function to Stringify elements
	 * @return A grid of 
	 */
	public String toString( final Function<T, String> stringFunc ) {
		String res = "";
		Coord2D prev = minCoord;
		for( final Coord2D c : this ) {
			// new line after every row end
			if( prev.y != c.y ) res += "\n";
			try {
				res += stringFunc.apply( get( c ) );
			} catch( final NullPointerException e ) {
				res += "N";
			}
			prev = c;
		}
		
		// return result minus the last newline
		return res;
	}
}
