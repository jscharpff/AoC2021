package challenges.day17;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import util.geometry.Coord2D;
import util.geometry.Window2D;

/**
 * A probe that can be fired from Santa's submarine to explore the trenches of
 * the ocean.
 *  
 * @author Joris
 */
public class Probe {
	/** The position of the probe */
	protected final Coord2D position;
	
	/** The target area to probe */
	protected final Window2D target;
	
	/**
	 * Creates a new probe set to seek out the given target area
	 * 
	 * @param target The window that describes the target
	 */
	private Probe( final Window2D target ) {
		this.target = target;
		this.position = new Coord2D( 0, 0 );
	}
	
	/**
	 * Finds the velocity that attains the highest altitude (y value) while still
	 * hitting the target area
	 * 
	 * @return The highest achieved altitude
	 */
	public long findHighestHit( ) {
		final Coord2D startpos = this.position;
		long maxalt = -1;
		
		for( int vx = 1; vx < target.getMaxCoord( ).x; vx++ ) {
			for( int vy = 1000; vy > 0; vy-- ) {
				final Coord2D vel = new Coord2D( vx, vy );
				
				// check if we hit the target?
				final Coord2D hit = fire( startpos, vel );
				if( hit == null ) continue;
				
				// yes, does this one have a better height than our current best?
				final long maxy = traceMaxHeight( startpos, vel );
				if( maxy > maxalt ) maxalt = maxy;
				
				// no need to look for slower y velocities, highest velocity hits
				// are guaranteed to achieve the maximum height
				break;
			}
		}

		return maxalt;
	}
	
	/**
	 * Finds the velocity that attains the highest altitude (y value) while still
	 * hitting the target area.
	 * 
	 * @return The highest achieved altitude
	 */
	public long countHitVelocities( ) {
		// first version uses a brute force algorithm over bounded areas where
		// the min and max y velocities are iteratively increased until no more
		// improvements are found

		// use a heuristic window size that should be large enough to cover all
		// potential hit vectors
		final int windowsize = target.size( ).y * target.size( ).x / 10;
		
		// keep shifting the window until hit count stops increasing
		long hitcount = 0;
		int vycurrent = target.getMinCoord( ).y;
		boolean nextwindow = true;
		while( nextwindow ) {
			final long hits = countHitVelocitiesBForce( vycurrent, vycurrent + windowsize - 1 );
			hitcount += hits;
			
			vycurrent += windowsize;
			nextwindow = hits != 0; 
		}
		
		return hitcount;
	}
	
	/**
	 * Finds the velocity that attains the highest altitude (y value) while still
	 * hitting the target area. This first version uses a brute force algorithm
	 * that simply iterates over all possible initial velocity vectors from the
	 * position to the target, given a minimum and maximum Y velocity to bound
	 * the search56 space.
	 * 
	 * @param minYVelocity The minimum Y velocity value 
	 * @param maxYVelocity The maximum Y velocity value 
	 * @return The highest achieved altitude
	 */
	protected long countHitVelocitiesBForce( final int minYVelocity, final int maxYVelocity ) {
		final Coord2D startpos = this.position;
		long count = 0;
		
		// do a brute force search over the entire range
		for( int vx = 1; vx <= target.getMaxCoord( ).x; vx++ ) {
			for( int vy = minYVelocity; vy <= maxYVelocity; vy++ ) {
				final Coord2D vel = new Coord2D( vx, vy );
				
				// check if we hit the target?
				final Coord2D hit = fire( startpos, vel );
				if( hit == null ) continue;
				
				// hit!
				count++;
			}
		}

		return count;
	}
	
	/**
	 * Moves the probe according to the given initial velocity and tests whether
	 * this will result in hitting the target area
	 * 
	 * @param velocity The initial velocity
	 * @return The coordinate at which it hits the target, null for a miss
	 */
	protected Coord2D fire( final Coord2D startpos, final Coord2D velocity ) {		
		// quick check to test it is even possible to hit the target with the
		// initial velocity
		if( startpos.diff( target.getMinCoord( ) ).x > 0 && velocity.x <= 0 ) return null;
		if( startpos.diff( target.getMaxCoord( ) ).x < 0 && velocity.x >= 0 ) return null;
		if( startpos.diff( target.getMinCoord( )	).y > 0 && velocity.y <= 0 ) return null;
		
		// copy starting values and keep moving the probe until we either hit the
		// target or over/undershoot it
		Coord2D pos = startpos;
		Coord2D vel = velocity;
		while( pos.y >= target.getMinCoord( ).y && pos.x <= target.getMaxCoord( ).x ) {
			// move by the velocity
			pos = pos.move( vel.x, vel.y );
			
			// check if this is now in the target zone
			if( target.contains( pos ) ) return pos;
			
			// and adjust the velocity
			vel = new Coord2D( vel.x == 0 ? 0 : (vel.x > 0 ? vel.x - 1 : vel.x + 1), vel.y - 1 );
		}
		
		// no hit
		return null;
	}
	
	/**
	 * Computes the maximum altitude achieved by a probe fired from the given
	 * initial position and velocity
	 * 
	 * @param startpos The initial position
	 * @param velocity The initial velocity
	 * @return The highest attained altitude (y value) in the probe's trajectory 
	 */
	protected long traceMaxHeight( final Coord2D startpos, final Coord2D velocity ) {
		long y = startpos.y;
		long yvel = velocity.y;
		
		// move by the y velocity until it has slowed to 0 (i.e. no more height 
		// increase)
		while( yvel > 0 ) {
			y += yvel;
			yvel--;			
		}
		
		return y;
	}
	
	
	/**
	 * Creates a new Probe with its target described by a string
	 * 
	 * @param input The target description "target area: x=xmin..xmax, y=ymin..ymax
	 * @return A new probe with the target area set
	 */
	public static Probe fromString( final String input ) {
		final Matcher m = Pattern.compile( "target area:\\s*x=(-?\\d+)..(-?\\d+),\\s*y=(-?\\d+)..(-?\\d+)" ).matcher( input );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid target description: " + input );
		
		// convert all capture groups to int values
		final int[] values = new int[ 4 ];
		for( int i = 1; i <= 4; i++ ) values[i-1] = Integer.parseInt( m.group( i ) );

		// and create target window by supplying the values to their window
		// counterparts (in a different order than the input)
		return new Probe( new Window2D( values[0], values[2], values[1], values[3] ) );
	}

	/** @return The description of the probe as (position) -> [target area] */
	@Override
	public String toString( ) {
		return position.toString( ) + " -> " + target.toString( );
	}
}
