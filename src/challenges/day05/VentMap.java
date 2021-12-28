package challenges.day05;

import java.util.ArrayList;
import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Line2D;
import aocutil.grid.CoordGrid;

/**
 * Class capturing all hydrothermal vent outputs
 * 
 * @author Joris
 */
public class VentMap {
	/** The list of line segments that represent the vents */
	protected List<Line2D> vents;

	/** The coordinate grid that holds the number of overlaps */
	protected final CoordGrid<Integer> grid;
	
	/**
	 * Creates a new hydrothermal VentMap
	 */
	public VentMap( ) {
		vents = new ArrayList<>( );
		grid = new CoordGrid<>( 0 );
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
			grid.set( c, grid.get( c, 0 ) + 1 );
	}
		
	/**
	 * Counts the number of points at which at least two vents overlap
	 * 
	 * @return The number of such intersections
	 */
	public long countOverlap( ) {
		long count = 0;
		for( final Integer c : grid.getValues( ) )
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
		String res = vents.toString( ) + "\n\n";		
		res += "--[Grid " + grid.size( ) + "]--\n";
		res += grid.toString(  );
		return res;
	}
}
