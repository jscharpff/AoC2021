package challenges.day17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;

/**
 * A probe that can be fired from Santa's submarine to explore the trenches of
 * the ocean.
 *  
 * @author Joris
 */
public class Probe {
	/** The target area to probe */
	protected final Window2D target;
	
	/**
	 * Creates a new probe set to seek out the given target area, relative to the
	 * probe's position
	 * 
	 * @param target The window that describes the target
	 */
	private Probe( final Window2D target ) {
		this.target = target;
	}
	
	/**
	 * Finds the velocity that attains the highest altitude (y value) while still
	 * hitting the target area
	 * 
	 * @return The highest achieved altitude
	 */
	public long findHighestHit( ) {		
		// get range of X values to consider
		final Set<Integer> xvelocities = getXVelocities(  );

		// and the range of y values to consider. Additionally, sort y velocities
		// on value, so the highest velocity is evaluated first for every x
		// velocity. Then, the first y velocity that hits is guaranteed to reach
		// the highest altitude
		final List<Integer> yvelocities = new ArrayList<>( getYVelocities( ) );
		Collections.sort( yvelocities, new Comparator<Integer>( ) {
			// sort high to low
			public int compare(Integer o1, Integer o2) { return o2.intValue( ) - o1.intValue( ); }; }
		);						
		
		// now evaluate all combinations, starting with highest y velocities first
		for( final int vy : yvelocities ) {
			for( final int vx : xvelocities ) {
				final Coord2D vel = new Coord2D( vx, vy );
				
				// check if we hit the target?
				final Coord2D hit = fire( vel );
				if( hit == null ) continue;
				
				// yes, as this is the highest y velocity, it must be the greatest
				// height we can attain
				return traceMaxHeight( vel );
			}
		}

		throw new RuntimeException( "Falied to hit the target!" );
	}
	
	/**
	 * Finds the velocity that attains the highest altitude (y value) while still
	 * hitting the target area.
	 * 
	 * @return The highest achieved altitude
	 */
	public long countHitVelocities( ) {
		// this version uses a brute force algorithm over pre-defined ranges of x
		// and y velocities that potentially hit the target area. That is, they
		// are guaranteed to hit the target area on their respective axes but their
		// combination might not and they have to be evaluated one by one
		long count = 0;
		
		final Set<Integer> xvelocities = getXVelocities( );
		final Set<Integer> yvelocities = getYVelocities( );
		
		// do a brute force search over the entire range
		for( final int vx : xvelocities ) {
			for( final int vy : yvelocities ) {
				final Coord2D vel = new Coord2D( vx, vy );
				
				// check if we hit the target?
				final Coord2D hit = fire( vel );
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
	protected Coord2D fire( final Coord2D velocity ) {		
		// quick check to test it is even possible to hit the target with the
		// initial velocity
		if( target.getMinX( ) > 0 && velocity.x <= 0 ) return null;
		if( target.getMaxX( ) < 0 && velocity.x >= 0 ) return null;
		if( target.getMinY( ) > 0 && velocity.y <= 0 ) return null;
		
		// copy starting values and keep moving the probe until we either hit the
		// target or over/undershoot it
		Coord2D pos = new Coord2D( 0, 0 );
		Coord2D vel = velocity;
		while( pos.y >= target.getMinY( ) && pos.x <= target.getMaxX( ) ) {
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
	 * initial velocity
	 * 
	 * @param velocity The initial velocity
	 * @return The highest attained altitude (y value) in the probe's trajectory 
	 */
	protected long traceMaxHeight( final Coord2D velocity ) {
		long y = 0;
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
	 * Determines set of x velocities that need to be considered as potential
	 * candidates to hit the target area
	 * 
	 * @return Set of x velocities that may result in hitting the target area,
	 *   depending on chosen y velocity
	 */
	private Set<Integer> getXVelocities( ) {
		final Set<Integer> velocities = new HashSet<Integer>( );
		
		// go over all possible velocities and keep those that may hit the target
		final int minX = target.getMinX( );
		final int maxX = target.getMaxX( );
		for( int vel = 1; vel <= maxX; vel++ ) {
			int x = 0;
			int currvel = vel;
			
			while( currvel >= 0 && x <= maxX ) {
				x += currvel;
				currvel--;
				
				// hit?
				if( minX <= x && x <= maxX) {
					velocities.add( vel );
					break;
				}
			}
		}
		
		return velocities;
	}
	
	/**
	 * Determines set of y velocities that need to be considered as potential
	 * candidates to hit the target area
	 * 
	 * @return Set of y velocities that may result in hitting the target area,
	 *   depending on chosen x velocity
	 */
	private Set<Integer> getYVelocities( ) {
		final Set<Integer> velocities = new HashSet<Integer>( );
		
		// go over all possible velocities and keep those that may hit the target
		// Y velocities that could hit the target may range between the one shot
		// down (minY) and its positive value (-minY). The latter is due to the
		// symmetry of the y function that guarantees that when the y velocity vy
		// starts at a value > 0 then the probe must cross y=0 with velocity
		// exactly -vy
		final int minY = target.getMinY( );
		final int maxY = target.getMaxY( );
		for( int vel = minY; vel <= -minY; vel++ ) {
			int y = 0;
			int currvel = vel;
			
			while( y >= minY ) {
				y += currvel;
				currvel--;
				
				// hit?
				if( minY <= y && y <= maxY ) {
					velocities.add( vel );
					break;
				}
			}
		}
		
		return velocities;
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

	/** @return The description of the probe as [target area] */
	@Override
	public String toString( ) {
		return target.toString( );
	}
}
