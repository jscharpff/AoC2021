package util.geometry;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a 3D cubic region
 * 
 * @author Joris
 */
public class Cube3D {
	/** The minimal coordinate values */
	private final Coord3D minCoord;
	
	/** The maximal coordinate values */
	private final Coord3D maxCoord;
	
	/**
	 * Creates a new Cube where the min and max values of all axes are set to
	 * the given radius, thus making it of size radius * 2 in all dimensions
	 * 
	 * @param radius The window size radius 
	 */
	public Cube3D( final int radius ) {
		this( -radius, radius, -radius, radius, -radius, radius );
	}
	
	/**
	 * Creates a new Cube from the given set of coordinates
	 */
	public Cube3D( final int x1, final int x2, final int y1, final int y2, final int z1, final int z2 ) {
		minCoord = new Coord3D( Math.min( x1, x2 ), Math.min( y1, y2 ), Math.min( z1, z2 ) );
		maxCoord = new Coord3D( Math.max( x1, x2 ), Math.max( y1, y2 ), Math.max( z1, z2 ) );
	}
	
	/**
	 * Tests whether the given coordinate is contained by this Cube, i.e. its
	 * coordinates are within the min and max values.
	 * 
	 * @param coord The coordinate to test
	 * @return True iff x in [xmin, xmax], y in [ymin, ymax] and z in [zmin, zmax]
	 */
	public boolean contains( final Coord3D coord ) {
		return coord.x >= minCoord.x && coord.y >= minCoord.y && coord.z >= minCoord.z && 
				coord.x <= maxCoord.x && coord.y <= maxCoord.y && coord.z <= maxCoord.z;
	}
	
	/**
	 * Tests whether another Cube overlaps this one at least partially
	 * 
	 * @param cube The other Cube to test
	 * @return True iff there is at least a partial overlap
	 */
	public boolean overlaps( final Cube3D cube ) {
		if( cube.getMaxX( ) < getMinX( ) || cube.getMinX( ) > getMaxX( ) ) return false;
		if( cube.getMaxY( ) < getMinY( ) || cube.getMinY( ) > getMaxY( ) ) return false;
		if( cube.getMaxZ( ) < getMinZ( ) || cube.getMinZ( ) > getMaxZ( ) ) return false;
		
		return true;
	}
	
	/**
	 * Determine the cube that represents the overlap between this cube and
	 * another one
	 * 
	 * @param cube The other cube
	 * @return The overlapping cube, is null in case of no overlap
	 */
	public Cube3D getOverlap( final Cube3D cube ) {
		// perform a quick test whether they overlap at all
		if( !overlaps( cube ) ) return null;
		
		// they should overlap somewhere, determine the cubic region that is in
		// both cubes
		return new Cube3D(
				Math.max( getMinX(), cube.getMinX( ) ), Math.min( getMaxX(), cube.getMaxX( ) ),
				Math.max( getMinY(), cube.getMinY( ) ), Math.min( getMaxY(), cube.getMaxY( ) ),
				Math.max( getMinZ(), cube.getMinZ( ) ), Math.min( getMaxZ(), cube.getMaxZ( ) )
		);
	}

	/**
	 * Removes the specified Cube from this cube by splitting the current cube
	 * into several that together span the volume of the original cube minus
	 * that of the specified intersecting cube
	 * 
	 * @param cube The cube to remove from the current cube
	 * @return List of cubes that together cover the volume of the original cube
	 *   minus the cube that is to be subtracted. May be empty if the entire cube
	 *   is contained within the given cube
	 */
	public Set<Cube3D> subtract( final Cube3D cube ) {
		final Set<Cube3D> result = new HashSet<>( );
		
		// if no overlap, subtract has no effect
		if( !overlaps( cube ) ) {
			result.add( this );
			return result;
		}
		
		// they do overlap, remove the other cube!
		// first cut in x range before and after the cube and store remaining part
		if( getMinX( ) < cube.getMinX( ) ) result.add( new Cube3D( getMinX( ), cube.getMinX( ) - 1, getMinY( ), getMaxY( ), getMinZ( ), getMaxZ( ) ) );
		if( getMaxX( ) > cube.getMaxX( ) ) result.add( new Cube3D( cube.getMaxX( ) + 1, getMaxX( ), getMinY( ), getMaxY( ), getMinZ( ), getMaxZ( ) ) );		
		final int xmin = Math.max( getMinX( ), cube.getMinX( ) );
		final int xmax = Math.min( getMaxX( ), cube.getMaxX( ) );
		 
		// use the remaining x range to cut in the y range and again keep remaining y range
		if( getMinY( ) < cube.getMinY( ) ) result.add( new Cube3D( xmin, xmax, getMinY( ), cube.getMinY( ) - 1, getMinZ( ), getMaxZ( ) ) );
		if( getMaxY( ) > cube.getMaxY( ) ) result.add( new Cube3D( xmin, xmax, cube.getMaxY( ) + 1, getMaxY( ), getMinZ( ), getMaxZ( ) ) );
		final int ymin = Math.max( getMinY( ), cube.getMinY( ) );
		final int ymax = Math.min( getMaxY( ), cube.getMaxY( ) );
		
		// finally, now the excess x and y ranges are removed, cut in the z range
		if( getMinZ( ) < cube.getMinZ( ) ) result.add( new Cube3D( xmin, xmax, ymin, ymax, getMinZ( ), cube.getMinZ( ) - 1 ) );
		if( getMaxZ( ) > cube.getMaxZ( ) ) result.add( new Cube3D( xmin, xmax, ymin, ymax, cube.getMaxZ( ) + 1, getMaxZ( ) ) );				

		return result;
	}
	
	/**
	 * @return The size of the Cube per axis
	 */
	public Coord3D size( ) {
		return new Coord3D( maxCoord.x - minCoord.x + 1, maxCoord.y - minCoord.y + 1, maxCoord.z - minCoord.z + 1 );
	}
	
	/** @return The volume of the cube */
	public long volume( ) {
		final Coord3D size = size( );
		return (long)size.x * (long)size.y * (long)size.z; 
	}

	/** @return The minimal coordinate values */
	public Coord3D getMinCoord( ) { return minCoord; }

	/** @return The maximal coordinate values */
	public Coord3D getMaxCoord( ) { return maxCoord; }
	
	/** @return The lowest value of the x axis */
	public int getMinX( ) { return getMinCoord( ).x; }

	/** @return The highest value of the x axis */
	public int getMaxX( ) { return getMaxCoord( ).x; }

	/** @return The lowest value of the y axis */
	public int getMinY( ) { return getMinCoord( ).y; }
	
	/** @return The highest value of the y axis */
	public int getMaxY( ) { return getMaxCoord( ).y; }	

	/** @return The lowest value of the z axis */
	public int getMinZ( ) { return getMinCoord( ).z; }

	/** @return The highest value of the y axis */
	public int getMaxZ( ) { return getMaxCoord( ).z; }	
	
	/** @return The Cube as a string x=[min,max],y=[min,max],z=[min,max] */
	@Override
	public String toString( ) {
		return "[x=" + getMinX( ) + ".." + getMaxX( ) + ",y=" + getMinY( ) + ".." + getMaxY( ) + ",z=" + getMinZ( ) + ".." + getMaxZ( ) + "]";
	}
	
	/**
	 * Creates a new Cube from a String description
	 * 
	 * @param input The input string, formatted as x=min..max,y=min..max,z=min..,max
	 * @return The Cube that corresponds to the string description
	 */
	public static Cube3D fromString( final String input ) {
		// match three pairs of values formatted as <axis>=<min>..<max>
		final Matcher m = Pattern.compile( "^\\s*x\\s*=\\s*(-?\\d+)\\s*..\\s*(-?\\d+)\\s*,\\s*y\\s*=\\s*(-?\\d+)\\s*..\\s*(-?\\d+)\\s*,\\s*z\\s*=\\s*(-?\\d+)\\s*..\\s*(-?\\d+)\\s*" ).matcher( input );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid window: " + input );
		
		// map all values to integers and return the Cube
		final int[] c = new int[ 6 ];
		for( int i = 0; i < c.length; i++ ) c[i] = Integer.parseInt( m.group( 1 + i ) );
		return new Cube3D( c[0], c[1], c[2], c[3], c[4], c[5] ); 
	}
	
	/**
	 * Compares this cube to another object
	 * 
	 * @param obj The object to compare against
	 * @return True iff the obj is a Cube3D and has the same coordinates
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null && !(obj instanceof Cube3D) ) return false;
		final Cube3D cube = (Cube3D)obj;
		
		return minCoord.equals( cube.minCoord ) && maxCoord.equals( maxCoord ); 
	}

	/** @return The toString hash code as this must be unique */
	@Override
	public int hashCode( ) {
		return toString( ).hashCode( );
	}
}
