package challenges.day13;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Class that holds a manual of transparent paper with dots on it
 * 
 * @author Joris
 */
public class Manual {
	/** The set of dots that is currently all we have from the manual (true means
	 *  that a dot is set at that position) */
	protected CoordGrid<Boolean> dots;

	/**
	 * Creates a new Manual from a set of dots
	 * 
	 * @param input The list of coordinates that describe the dots
	 */
	public Manual( final List<String> input ) {
		this.dots = new CoordGrid<>( false );
		for( final String s : input ) {
			dots.add( Coord2D.fromString( s ), true );			
		}
	}
	
	/**
	 * Folds the manual over the x or y axis, or both. Horizontal folds will
	 * result in all dots 'below' the y-line being flipped into the segment
	 * above the line. Vertical folding happens similarly albeit from right to
	 * left. The result of the operation is that the manual is resized and
	 * updated.
	 * 
	 * @param horizontal True for horizontal folding, false for vertical 
	 * @param foldline The axis value to fold at, e.g. fold at x=foldline or
	 *   y=foldline
	 */
	public void fold( final boolean horizontal, final int foldline ) {
		if( foldline < 0 ) throw new IllegalArgumentException( "Invalid fold line value " + foldline );

		// copy the part above the fold line into a new CoordGrid
		// and fold the coordinates under the fold
		final CoordGrid<Boolean> copy = new CoordGrid<>( false );
		for( final Coord2D c : dots.getKeys( ) ) {
			if( horizontal ) {
				if( c.y <= foldline ) copy.add( c, true );
				else copy.add( new Coord2D( c.x, 2 * foldline - c.y ), true );
			} else {
				if( c.x <= foldline ) copy.add( c, true );			
				else copy.add( new Coord2D( 2 * foldline - c.x, c.y ), true );
			}
		}

		// swap grids now
		dots = copy;
	}
	
	/** @return The number of dots in the current manual grid */
	public int getDotCount( ) {
		return dots.getValues( ).size( );
	}
	
	/** @return The manual as string, which is a drawing of its dots */
	@Override
	public String toString( ) {
		return dots.toString( x -> x ? "#" : "." );
	}
}
