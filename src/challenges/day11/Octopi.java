package challenges.day11;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import aocutil.geometry.Coord2D;
import aocutil.grid.CoordGrid;

/**
 * Simulation of a grid of octopi that emit lights. In each step of the
 * simulation octopi light levels increase until they reach a light value that
 * causes them to 'flash', i.e. they emit a flash of light and influence
 * neighbouring octopi light value levels. 
 * 
 * @author Joris
 */
public class Octopi {	
	/** The grid of Octopi, represented by their flash level */
	protected final CoordGrid<Integer> octopi;
	
	/**
	 * Creates a new grid of Octopi from a textual representation
	 * 
	 * @param input The list of input strings that describe the rows of octopi
	 */
	public Octopi( final List<String> input ) {
		this.octopi = CoordGrid.fromDigitGrid( input );
	}
	
	/**
	 * Runs the octopi flash simulation for n steps
	 * 
	 * @return The number of flashes occurring over all steps
	 */
	public long simulate( final int n ) {
		if( n <= 0 ) throw new IllegalArgumentException( "Invalid number of steps: " + n );
		
		// simulate for n rounds and sum the flash count
		long flashes = 0;
		for( int i = 0; i < n; i++ ) flashes += step( );
		return flashes;
	}
	
	
	/**
	 * Simulates the octopi flashing until they all flash at the same time
	 * 
	 * @return The first step at which they are 'synchronised', i.e. all flash
	 * in the step, -1 if failed to find within the max number of iterations
	 */
	public long findSynchronised( ) {
		// set a maximum to the number of steps we try
		final long MAX_RUNS = 1000000;
		long run = 0;
		while( run++ < MAX_RUNS ) {
			// simply run the simulation until a step returns that all the octopi
			// in the grid flashed in that step
			final long flashes = this.step( );
			if( flashes == octopi.size( ).x * octopi.size( ).y ) return run;			
		}
		
		// unable to determine the first sync moment within the MAX_RUNS 
		return -1;
	}
	
	
	/**
	 * Simulates a single step in the flashing octopi grid
	 * 
	 * @return The number of 'flashes' that happened in the step
	 */
	protected long step( ) {
		// first increase the light value for all octopi, keep track of those with level 9
		final Stack<Coord2D> toflash = new Stack<>();
		final Set<Coord2D> flashed = new HashSet<>( );
		
		// go over the grid
		for( final Coord2D c : octopi ) {
			final int newval = octopi.get( c ) + 1;
			if( newval > 9 ) toflash.add( c );
			octopi.set( c, newval );
		}
		
		// now 'flash' the octopi with new light value >= 9
		while( toflash.size( ) > 0 ) {
			final Coord2D c = toflash.pop( );
			if( flashed.contains( c ) ) continue; // flash only once
			flashed.add( c );
			
			// increase energy level of neighbours by one
			for( final Coord2D n : c.getNeighbours( true ) ) {
				// only consider valid candidates
				if( !octopi.hasValue( n ) ) continue;
				
				// increase value and it to flash list if now flashing
				final int newval = octopi.get( n ) + 1;
				if( newval > 9 && !flashed.contains( n ) ) toflash.add( n );
				octopi.set( n, newval );			}
		}
		
		// now reset all flashed octopi to 0
		for( final Coord2D c : flashed )
			octopi.set( c, 0 );
		
		// and return count of flashed octopi list
		return flashed.size( );
	}

	/**
	 * @return The octopi grid as String
	 */
	@Override
	public String toString( ) {
		return octopi.toString( x -> x == -1 ? " " : "" + x );
	}
}
