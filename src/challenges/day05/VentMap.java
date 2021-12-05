package challenges.day05;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import util.geometry.Coord2D;
import util.geometry.Line2D;

/**
 * Class capturing all hydrothermal vent outputs
 * 
 * @author Joris
 */
public class VentMap {
	/** The list of line segments that represent the vents */
	protected List<Line2D> vents;
	
	/** Current size of the vent grid */
	protected Coord2D size;
	
	/** The grid that captures the number of vents per coordinate */
	protected final Map<Coord2D, Integer> grid;
	/**
	 * Creates a new hydrothermal VentMap
	 */
	public VentMap( ) {
		vents = new ArrayList<>( );
		grid = new HashMap<>( );
		size = null; // not computed yet
	}
	
	/**
	 * Adds a vent to the vent map, also updates grid
	 * 
	 * @param vent The vent to add
	 */
	public void addVent( final Line2D vent ) {
		vents.add( vent );
		
		// update coordinate overlap in the grid
		for( final Coord2D c : vent.getPoints( ) )
			grid.put( c, grid.getOrDefault( c, 0 ) + 1);

		// clear size to require new computation
		size = null;
	}
	
	/** 
	 * @return The size of the current grid
	 */
	protected Coord2D size( ) {
		// already computed?
		if( size != null ) return size;
		
		// nope, go over all point and store maximum X and Y values
		int maxX = 0; int maxY = 0;
		for( final Coord2D c : grid.keySet( ) ) {
			if( c.x > maxX ) maxX = c.x;
			if( c.y > maxY ) maxY = c.y;
		}
		
		// store size and return it
		size = new Coord2D( maxX, maxY );
		return size;
	}
	
	/**
	 * Counts the number of points at which at least two vents overlap
	 * 
	 * @return The number of such intersections
	 */
	public long countOverlap( ) {
		long count = 0;
		for( final Integer c : grid.values( ) )
			if( c > 1 ) count++;
		return count;
	}
	
	/**
	 * Creates a VentMap from a string description of all its vent lines
	 * 
	 * @param vents The list of vents
	 * @param diagonals True to include also include diagonal lines
	 * @return The VentMap object
	 * @throws IllegalArgumentException if the parsing failed
	 */
	public static VentMap fromList( final List<String> vents, final boolean diagonals ) throws IllegalArgumentException {
		final VentMap vm = new VentMap( );
		
		// go over the input and add only those vents that are applicable
		for( final String vent : vents ) {
			final Line2D v = Line2D.fromString( vent );
			
			// only include non-diagonals?
			if( diagonals || v.isHorizontal() || v.isVertical() )
				vm.addVent( v );
		}
		return vm;
	}
	
	/**
	 * @return String description of the vent map
	 */
	@Override
	public String toString( ) {
		String res = vents.toString( ) + "\n";		
		
		// visualise grid
		for( int y = 0; y <= size.y; y++ ) {
			res += "\n";
			for( int x = 0; x <= size.x; x++ ) {
				res += grid.getOrDefault( new Coord2D( x, y ), 0 );
			}
		}
		return res;
	}
}
