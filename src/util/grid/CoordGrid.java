package util.grid;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import util.geometry.Coord2D;
import util.geometry.Window2D;

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
	private Window2D window;
	
	/** Default value for all non-set coordinates */
	protected T defaultValue;

	/**
	 * Creates a new coordinate based grid
	 * 
	 * @param defaultValue The default value for non-assigned coordinates
	 */
	public CoordGrid( final T defaultValue ) {
		map = new HashMap<Coord2D, T>( );
		window = new Window2D( );
		setDefaultValue( defaultValue );
	}
	
	/** @return The current default value for coordinates witout a value */
	public T getDefaultValue( ) {
		return defaultValue;
	}
	
	/**
	 * Sets the default value for grid coordinates that do not have a value
	 * stored.
	 * 
	 * @param newdefault The default value to set
	 */
	public void setDefaultValue( T newdefault ) {
		this.defaultValue = newdefault;
	}
	
	/**
	 * Adds a new value to the coordinate grid at the specified coordinate
	 * 
	 * @param coord The coordinate to set the value for
	 * @param value The value to set
	 * @return The previous value that was set, null if it was not set before
	 */
	public T add( final Coord2D coord, final T value ) {
		window.include( coord );
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
	 * Clears the value for a given coordinate and resizes the grid if necessary
	 * 
	 * @param coord The coordinate to remove from the grid
	 * @return The previous value that was stored at the coordinate, can be null 
	 */
	public T unset( final Coord2D coord ) {
		final T oldvalue = map.remove( coord );
		// does the removal influence the bounds? if yes, update
		if( window.onBorder( coord ) ) window.resize( getKeys( ) );
		return oldvalue;
	}
	
	/**
	 * Clears an entire set of coordinates at once, only recomputes bounds once
	 * 
	 * @param coords The set of coordinates to unset 
	 */
	public void unsetAll( final Collection<Coord2D> coords ) {
		for( final Coord2D c : coords )
			map.remove( c );
		
		window.resize( getKeys( ) );
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
	 * Checks if the given coordinate is within the bounds of the grid. It does
	 * not check whether the coordinate actually holds a value, simply whether it
	 * could be `contained' by the coordinates of this grid. To check whether it
	 * actually stores a value use <code>hasValue( Coord2D )</code>.
	 * 
	 * @param coord The coordinate to test
	 * @return True iff the coordinate's x and y positions are within the bounds
	 *   of this grid
	 */
	public boolean contains( final Coord2D coord ) {
		return window.contains( coord );
	}
	
	/** 
	 * Determines and returns the size of the current grid, i.e. the span between
	 * minimal and maximal coordinate
	 * 
	 * @return The size of the current grid as 2D coordinate or null if empty
	 */
	public Coord2D size( ) {
		return window.size( );
	}
	
	/** @return The current window spanned by this grid */
	public Window2D window( ) {
		return window;
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
		return window.iterator( );
	}
	
	/**
	 * Constructs a Coordinate grid from a grid, represented by a list of Strings
	 * for each row. Columns are separated by the given separator, which may be
	 * empty for character-based grids that hold a single value per char.  
	 * 
	 * @param <U> The type of the data elements to be contained
	 * @param input The list of strings that describe a n x m grid, one string
	 *   per row
	 * @param separator The column separator as regex, "" or null for simple,
	 *   single character columns
	 * @param mapfunc The function to map each character into a value
	 * @param defaultValue The default value to set for the empty entries
	 * @return The CoordGrid that is constructed from the list of strings
	 */
	public static <U> CoordGrid<U> fromStringList( final List<String> input, final String separator, final Function<String, U> mapfunc, final U defaultValue ) {
		final CoordGrid<U> grid = new CoordGrid<>( defaultValue );
		
		// fill in blank separator iff it is null
		final String sep = separator != null ? separator : ""; 
		
		// parse rows and columns
		int y = -1;
		for( final String row : input ) {
			y++;
			int x = -1;
			for( final String col : row.split( sep ) ) {
				x++;
				grid.add( new Coord2D( x, y ), mapfunc.apply( col ) );
			}
		}

		return grid;
	}
	
	/**
	 * Shorthand function to construct an Integer-valued CoordGrid from a char
	 * grid. Can only hold integer values 0-9 (as the input is a char grid) in
	 * parsed elements, defaultValue of non-set coordinates will be -1.
	 * 
	 * @param input The list of row strings that describe the digits on that row 
	 * @return An digit-based CoordGrid with the values as read from the input
	 *   and defaultValue -1 for non-contained coordinates
	 */
	public static CoordGrid<Integer> fromDigitGrid( final List<String> input ) {
		return fromStringList( input, null, Integer::parseInt, -1 );
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
		Coord2D prev = null;
		for( final Coord2D c : this ) {
			// new line after every row end
			if( prev == null || prev.y != c.y ) res += "\n";
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
