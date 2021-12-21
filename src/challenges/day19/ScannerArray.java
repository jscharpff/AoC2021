package challenges.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.geometry.Coord3D;
import util.geometry.Rotation3D;

/**
 * Class that represents an array of scanners, provides functionality to
 * reconstruct the array from shared beacon observations.
 * 
 * @author Joris
 */
public class ScannerArray {
	/** The array of scanners in this array */
	protected List<Scanner> scanners;
	
	/**
	 * Creates a new empty ScannerArray
	 */
	private ScannerArray( ) {
		this.scanners = new ArrayList<>( );
	}
		
	/**
	* Reconstructs the scanner array by comparing observations of the various
	* scanners and finding shared sets of observations in their beacon data to
	* correlate and reposition the scanners. Sets the position and rotation
	* of the scanners once known
	* 
	* @param N The beacon clustering size to compare observations over
	*/
	public void reconstruct( final int N ) {
		// now reconstruct scanner array from their cluster observations, add the
		// first one as reference point for all others
		final int NUM_SCANNERS = scanners.size( );
		final Set<Scanner> fixed = new HashSet<>( );
		final Scanner s0 = scanners.get( 0 );
		s0.setPosition( new Coord3D( 0, 0, 0 ) );
		s0.setRotation( new Rotation3D( ) );
		fixed.add( s0 );
	
		// now fix the rest against this reference point
		int idx = 0, itercount = 0;
		while( fixed.size( ) < NUM_SCANNERS ) {
			/* Fail-safe to prevent infinite loops */
			if( ++itercount > NUM_SCANNERS + 1 ) throw new RuntimeException( "Failed to fix remaining scanners: " + (NUM_SCANNERS - fixed.size( ) ) );

			// use a rotating index to find the next scanner to fix
			idx = (idx + 1) % NUM_SCANNERS;
			final Scanner s = scanners.get( idx );
			if( fixed.contains( s ) ) continue;
			
			// found a new candidate to fixate, try all rotations of the scanner
			final Scanner fixAgainst = tryAndFixScanner( s, fixed, N );
			if( fixAgainst != null ) {
				// successfully fixated it!
				System.out.println( "> Fixed " + s + " against " + fixAgainst + " (position: " + s.getPosition( ) + ", rotation: " + s.getRotation() + ")" );
				itercount = 0;
				fixed.add( s );
			}
		}
	}
	
	/**
	 * Tries to fixate the scanner against any of the already fixed scanners
	 * 
	 * @param s The scanner to fixate
	 * @param fixed The set of already fixed scanners
	 * @param sharedbeacons The number of beacons that are shared between the
	 *   scanners
	 * @return The scanner against which it can be fixed, null otherwise
	 */
	private Scanner tryAndFixScanner( final Scanner s, final Set<Scanner> fixed, final int sharedbeacons ) {
		for( final Rotation3D R : Rotation3D.getOrientationMatrices( ) ) {
			// try a new orientation
			s.setRotation( R );
			
			// check if the observations of this scanner overlaps with an already fixed one
			for( final Scanner f : fixed ) {
				// compare the observations between the fixed scanner and this one
				final Map<Coord3D, Integer> diff = new HashMap<>( f.numObservations( ) * s.numObservations( ) );
				for( final Coord3D c1 : f.getBeaconsTransformed( true, false ) )
					for( final Coord3D c2 : s.getBeaconsTransformed( true, false ) ) {
						final Coord3D d = c1.diff( c2, false );
						diff.put( d, diff.getOrDefault( d, 0 ) + 1 );
					}
				
				// and check if there are 12 equal distances
				for( final Coord3D k : diff.keySet( ) ) {
					if( diff.get( k ) >= sharedbeacons ) {
						// there are enough pairs that are equi-distant, hence assume
						// these two scanners see the same beacons. Fixate the scanner
						// against its reference frame
						s.setPosition( f.getPosition( ).add( k ) );
						return f;
					}
				}
			}
		}
		
		// no success..
		return null;
	}
	
	/**
	 * Reconstructs the 3D grid of beacons now that the scanners are in the 
	 * right position
	 * 
	 * @return The set containing all the positions at which a beacon is present
	 */
	public Set<Coord3D> getBeacons( ) {
		final Set<Coord3D> beacons = new HashSet<>( );
		
		// add beacons observed by all scanners, overlap is filtered because set
		// keys must be unique
		for( final Scanner s : scanners ) {
			for( final Coord3D b : s.getBeaconsTransformed( true, true ) )
				beacons.add( b );
		}
		
		return beacons;
	}
	
	/**
	 * Determines the maximum scanning range by finding the maximal Manhattan
	 * distance between any two pairs of scanners in the array
	 * 
	 * @return The maximal scanning range
	 */
	public int getScanningRange( ) {
		int maxrange = -1;
		for( int i = 0; i < scanners.size( ) - 1; i++ )
			for( int j = i + 1; j < scanners.size( ); j++ ) {
				final int dist = scanners.get( i ).getPosition( ).getManhattanDist( scanners.get( j ).getPosition( ) );
				if( dist > maxrange ) maxrange = dist;
			}
		
		return maxrange;
	}
	

	/**
	 * Creates a ScannerArray from a list of String groups that describe the
	 * individual scanners and their observations
	 * 
	 * @param input The list of String groups
	 * @return The ScannerArray containing all the scanners
	 */
	public static ScannerArray fromStringList( final List<String> input ) {
		final ScannerArray array = new ScannerArray( );
		
		for( final String s : input )
			array.scanners.add( Scanner.fromString( s ) );

		return array;
	}
	
	/** @return The scanners in this array, one per line */
	@Override
	public String toString( ) {
		if( scanners.size( ) == 0 ) return "";
		
		String res = scanners.get( 0 ).toString( );
		for( int i = 1; i < scanners.size( ); i++ )
			res += "\n" + scanners.get( i );
		
		return res;
	}
}
