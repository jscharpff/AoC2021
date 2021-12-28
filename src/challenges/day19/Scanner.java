package challenges.day19;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aocutil.geometry.Coord3D;
import aocutil.geometry.Rotation3D;

/**
 * Class that holds a Scanner and the beacon it observes at coordinates
 * relative to its own position
 * 
 * @author Joris
 */
public class Scanner {
	/** The ID of the scanner */
	private final int ID;
	
	/** The set of beacons observed by this scanner, represented by 3D coords */
	private final Set<Coord3D> beacons;
	
	/** The position of the scanner, if known */
	protected Coord3D position;
	
	/** The orientation of the scanner, as rotation per axis, if known */
	protected Rotation3D rotation;

	/**
	 * Creates a new Scanner with the given ID
	 * 
	 * @param ID The ID of the scanner
	 */
	public Scanner( final int ID ) {
		this.ID = ID;
		this.beacons = new HashSet<>( );
		this.position = null;
		this.rotation = null;
	}
	
	/** @return The current reference position of the scanner */
	public Coord3D getPosition( ) {
		return this.position;
	}
	
	/** 
	 * Sets the position of the scanner
	 * 
	 * @param pos The position of the scanner
	 */
	public void setPosition( final Coord3D pos ) {
		this.position = pos;
	}
	
	/** @return The current rotation of the scanner */
	public Rotation3D getRotation( ) {
		return this.rotation;
	}

	/**
	 * Sets the orientation of the scanner
	 * 
	 * @param rotation
	 */
	public void setRotation( final Rotation3D rotation ) {
		this.rotation = rotation;
	}
	
	/**
	 * Returns the set of beacons observed by this scanner, transformed by the
	 * current rotation and/or position of the scanner
	 * 
	 * @param rotate True to rotate the beacon positions according to the
	 *   scanner's orientation
	 * @param transpose True to transpose the beacon positions according to the
	 *   scanner's position
	 * @return The set of beacons, possible transformed relative to the current
	 *   scanner rotation and position
	 */
	public Set<Coord3D> getBeaconsTransformed( final boolean rotate, final boolean transpose ) {
		if( rotate && rotation == null ) throw new IllegalArgumentException( "Rotation matrix is not set of " + this );
		if( transpose && position == null ) throw new IllegalArgumentException( "Reference position not set of " + this );
		
		// apply transformations to each beacon
		final Set<Coord3D> result = new HashSet<>( beacons.size( ) );
		for( Coord3D b : beacons ) {
			if( rotate ) b = b.rotate( rotation );
			if( transpose ) b = b.add( position );
			
			result.add( b );
		}
		return result;
	}
	
	/** @return The number of beacons */
	public int numObservations( ) {
		return beacons.size( );
	}
	
	
	/**
	 * Creates a scanner from a Strings that describe the index and the beacons
	 * it observes, one per line in the string
	 * 
	 * @param input The string describing the scanner and observed beacon
	 * @return The Scanner
	 */
	public static Scanner fromString( final String input ) {
		final String[] in = input.split( "\\n" );
		final Matcher m = Pattern.compile( "--- scanner (\\d+) ---" ).matcher( in[0] );
		if( !m.find( ) ) throw new IllegalArgumentException( "Invalid Scanner input: " + input );

		// create scanner and parse beacons
		final Scanner scanner = new Scanner( Integer.parseInt( m.group( 1 ) ) );
		for( int i = 1; i < in.length; i++ ) 
			scanner.beacons.add( Coord3D.fromString( in[i] ) );
		return scanner;
	}
	
	/** @return The scanner ID */
	@Override
	public String toString( ) {
		return "Scanner " + ID;
	}
	
	/**
	 * Checks if this scanner is equal to another object
	 * 
	 * @param obj The other object to test against
	 * @return True iff obj is a Scanner and has the same ID
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Scanner) ) return false;
		return ((Scanner)obj).ID == ID;
	}
	
	/** @return The hash code of the scanner, simply use the unique ID */
	@Override
	public int hashCode( ) {
		return ID;
	}
}
