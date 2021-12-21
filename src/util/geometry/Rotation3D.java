package util.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * A 3D Rotation matrix that can be applied to rotate coordinates
 * 
 * @author Joris
 */
public class Rotation3D {
	/** The x-axis rotation (roll) */
	public final double rotX;
	
	/** The y-axis rotation (pitch) */
	public final double rotY;
	
	/** The z-axis rotation (yaw) */
	public final double rotZ;
	
	/** The pre-computed rotation matrix */
	private final double[][] R;
	
	/** Pre-computed set of orientation rotation matrices */
	private static Rotation3D[] orientations;
	
	/**
	 * Creates a new 3D rotation matrix for the initial rotation
	 */
	public Rotation3D( ) {
		this( 0, 0, 0 );
	}
	
	/**
	 * Creates a new 3D rotation matrix
	 * 
	 * @param rotX The x-axis rotation in degrees
	 * @param rotY The y-axis rotation in degrees
	 * @param rotZ The z-axis rotation in degrees
	 */
	public Rotation3D( final double rotX, final double rotY, final double rotZ ) {
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		
		// compute matrix
    double su = Math.sin( Math.toRadians( rotX ) );
    double cu = Math.cos( Math.toRadians( rotX ) );
    double sv = Math.sin( Math.toRadians( rotY ) );
    double cv = Math.cos( Math.toRadians( rotY ) );
    double sw = Math.sin( Math.toRadians( rotZ ) );
    double cw = Math.cos( Math.toRadians( rotZ ) );
    
    //Create and populate RotationMatrix
    R = new double[3][3];
    R[0][0] = cv * cw;
    R[0][1] = su*sv*cw - cu*sw;
    R[0][2] = su*sw + cu*sv*cw;
    R[1][0] = cv*sw;
    R[1][1] = cu*cw + su*sv*sw;
    R[1][2] = cu*sv*sw - su*cw;
    R[2][0] = -sv;
    R[2][1] = su*cv;
    R[2][2] = cu*cv;
    
    // keep a 6 digit precision
    for( int i = 0; i < 3; i++ )
    	for( int j = 0; j < 3; j++ )
    		R[i][j] = Math.round(  R[i][j] * 100000.0 ) / 100000.0;
	}
	
	/**
	 * Applies the rotation matrix to a 3D coordinate
	 * 
	 * @param coord A array of three doubles that contains the values per axis
	 * @return An array of 3 doubles containing the result of the roatation
	 */
	public double[] apply( final double[] in ) {
		if( in.length != 3 ) throw new IllegalArgumentException( "The input array should have exactly 3 elements" );
		
		return new double[] {
				in[0] * R[0][0] + in[1] * R[0][1] + in[2] * R[0][2], 
				in[0] * R[1][0] + in[1] * R[1][1] + in[2] * R[1][2], 
				in[0] * R[2][0] + in[1] * R[2][1] + in[2] * R[2][2]
		};
	}
	
	
	/**
	 * Checks if two Rotation3D matrices are equal with a precision of 6 decimal
	 * numbers
	 * 
	 * @param obj The other object to compare
	 * @return True if obj is a valid Rotation3D and their matrix entries equal
	 *   up to six decimals
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Rotation3D) ) return false;
		final Rotation3D r = (Rotation3D)obj;
		
		// compare rotation angle matrices
		for( int i = 0; i < 3; i++ )
			for( int j = 0; j < 3; j++ )
					if( Math.abs( r.R[i][j] - R[i][j] ) > 0.00001 ) return false;
		return true;
	}

	/**
	 * Returns set of all 24 rotation matrices that cover all axis orientations
	 * in a 3D coordinate system. This set is computed on the first call and then
	 * cached for future calls.
	 * 
	 * @return The set of 24 unique rotation matrices that span all 3D
	 *   orientations
	 */
	public static Rotation3D[] getOrientationMatrices( ) {
		if( orientations != null ) return orientations;
		
		// ugly way to generate them all but it works...
		final List<Rotation3D> rotations = new ArrayList<>( 24 );
		for( int rx = 0; rx < 360; rx += 90 )
			for( int ry = 0; ry < 360; ry += 90 )
				for( int rz = 0; rz < 360; rz += 90 ) {
					final Rotation3D R = new Rotation3D( rx, ry, rz );
					if( !rotations.contains( R ) ) rotations.add( R );
				}
					
		orientations = rotations.toArray( new Rotation3D[ 24 ] );
		return orientations;
	}
	
	/** @return The string description of the rotation matrix */
	@Override
	public String toString( ) {
		String res = "";
		for( int i = 0; i < 3; i++ ) {
			res += "[";
			for( int j = 0; j < 3; j++ ) 
				res += String.format( "%1.3f", R[i][j] ) + ", ";
			res = res.substring( 0, res.length( ) - 2 ) + "]" + (i < 2 ? ", " : "");
		}
		return res;
	}
}
