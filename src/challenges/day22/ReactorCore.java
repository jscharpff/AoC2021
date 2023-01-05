package challenges.day22;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aocutil.geometry.Cube3D;

/**
 * Class that models the submarine reactor's Core, consisting of cubes in a
 * three-dimensional integer space that may be activated or not. 
 *  
 * @author Joris
 */
public class ReactorCore {
	/** The map that contains the regions of cubes that are on  */
	private Set<Cube3D> regions;
	
	/**
	 * Creates a new reactor core
	 */
	private ReactorCore( ) {
		this.regions = new HashSet<>( );
	}
	
	/**
	 * Turns on the cubes in the specified region to on or off
	 * 
	 * @param region The region to set value of
	 * @param value True to activate the cubes in the area, false to deactivate
	 */
	private void set( final Cube3D region, final boolean value ) {
		// reconstruct set of active cubes
		final Set<Cube3D> newset = new HashSet<>( );
		
		// check with all existing regions if they overlap and make sure to keep
		// only non-overlapping areas
		for( final Cube3D cube : regions ) {
			if( !region.overlaps( cube ) )
				// no overlap means no change 
				newset.add( cube );
			else
				// if they do overlap, remove the new region from the cube and add
				// the result of the subtraction to the set
				newset.addAll( cube.subtract( region ) );
		}		
		
		// if the region should active cubes add it and swap the sets
		if( value ) newset.add( region );		
		regions = newset;
	}
	
	/**
	 * Counts the number of set cubes int the specified region
	 * 
	 * @param region The region to consider 
	 * @return The number of activated cubes in the given region 
	 */
	public long getActivatedCubes( ) {
		long count = 0;
		for( final Cube3D c : regions ) {
			count += c.volume( );
		}
		return count;
	}

	/**
	 * @return The regions of cubes that are currently active in the reactor core 
	 */
	@Override
	public String toString( ) {
		return regions.toString( );
	}

	/**
	 * Creates a new reactor core from a booting sequence
	 * 
	 * @param bootregion The boot region, null for unbounded
	 * @param sequence The list of boot instructions
	 * @return The reactor core
	 */
	public static ReactorCore fromBootingSequence( final Cube3D bootregion, final List<String> sequence ) {
		final ReactorCore r = new ReactorCore( );
		
		// parse sequence of instructions
		for( final String seq : sequence ) {
			final String[] s = seq.split( " " );
			final boolean on = s[0].equals( "on" );
			final Cube3D region = Cube3D.fromString( s[1] );
			
			// check if the region is within the boot region, otherwise discard it
			if( bootregion != null && !region.overlaps( bootregion ) ) continue;
						
			// and apply the instruction
			r.set( region, on );
		}
		
		return r;
	}
}
