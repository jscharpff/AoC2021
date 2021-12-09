package challenges.day09;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import util.geometry.Coord2D;
import util.geometry.CoordGrid;

public class HeightMap {
	/** The coordinate grid */
	protected final CoordGrid<Integer> map;
	
	/**
	 * Creates a new height map
	 */
	public HeightMap( ) {
		map = new CoordGrid<Integer>( -1 );
	}
	
	/**
	 * @return The height at the given coordinate
	 */
	public int getHeight( final Coord2D coord ) {
		return map.get( coord );
	}
	
	/**
	 * Finds all lowest points in the height map, i.e. those coordinates that
	 * are lower than all their neighbours
	 * 
	 * @return List of lowest points
	 */
	public List<Coord2D> getLowestPoints( ) {
		final List<Coord2D> result = new ArrayList<>( );
		
		// go over all coordinates and check whether it is the lowest among its neighbours
		for( final Coord2D c : map ) {
			final int h = getHeight( c );
			boolean lowest = true;
			for( final Coord2D n : c.getNeighbours( false ) ) {
				// only include coordinates actually in the grid
				if( !map.hasValue( n ) ) continue; 
				
				lowest &= getHeight( n ) > h;
			}
			if( lowest ) result.add( c );		
		}
		
		return result;
	}

	/**
	 * Explore, from a given starting point, the size of a basin. This is done by
	 * adding (unvisited) neighbouring coordinates iff they are at a higher level
	 * but lower than 9. The size of the basin is given by the number of 
	 * coordinates contained by it.
	 * 
	 * @param start The starting coordinate to start exploration from
	 * @return The size of the basin in the number of coordinates contained
	 */
	public int getBasinSize( final Coord2D start ) {
		// create initial exploration set and keep track of explored coordinates
		Stack<Coord2D> explore = new Stack<>( );
		final Set<Coord2D> explored = new HashSet<>( ); 
		explore.push( start );
		
		// explore from current node
		while( explore.size( ) > 0 ) {
			// explore the next coord on our backlog
			final Coord2D node = explore.pop( );
			if( explored.contains( node ) ) continue; // skip if already explored
			explored.add( node );

			// get height of this node
			final int height = getHeight( node );
			
			// find all coords around the node with height + 1 to explore in the next iteration
			for( final Coord2D newcoord : node.getNeighbours( false ) ) {
				// skip already visited coords or coords outside the grid
				if( explored.contains( newcoord ) && !map.hasValue( newcoord ) ) continue;
				
				// skip borders and coords that are not higher than the current coord
				final int nheight = map.get( newcoord );
				if( nheight >= 9 || nheight <= height ) continue;
				
				// this coordinate is part of the basin! Add it to list of coordinates
				// to explore later
				explore.push( newcoord );
			}
		}

		// return the size of the basin
		return explored.size( );
	}
	
	/**
	 * Creates a new height map from a list of strings
	 * 
	 * @param str The map as list of strings, one string per row
	 * @return The height map
	 */
	public static HeightMap fromString( final List<String> str ) {
		final HeightMap h = new HeightMap( );
		
		int y = -1;
		for( final String row : str ) {
			y++;
			for( int x = 0; x < row.length( ); x++ ) {
				h.map.add( new Coord2D( x, y ), Integer.parseInt( "" + row.charAt( x ) ) );
			}
		}
		
		return h;
	}
	
	/**
	 * @return The visualisation of the height map as a grid of height levels
	 */
	@Override
	public String toString( ) {
		return map.toString( );
	}

}
