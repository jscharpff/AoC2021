package challenges.day20;

import util.BitString;
import util.geometry.Coord2D;
import util.geometry.Window2D;

/**
 * Class to enhance images based using a given algorithm to convert 3x3 blocks
 * of pixels into "enhanced" pixel
 * 
 * @author Joris
 */
public class ImageScanner {
	/** The index to pixel conversion map */
	protected final boolean[] pixmap;
	
	/**
	 * Creates a new image scanner
	 * 
	 * @param pixmap The pixel mapping algorithm
	 */
	private ImageScanner( final boolean[] pixmap ) {
		this.pixmap = pixmap;
	}
	
	/**
	 * Enhances the image using the pixelmap and returns a new, enhanced image
	 * 
	 * @param image The input image
	 * @param step The current enhancement step
	 * @return An enhanced image
	 */
	public Image enhance( final Image input, final int step ) {
		// the number of steps is required to determine how to deal with "unknown"
		// parts of the image, but only in case pixmap[0] is set because that
		// causes all 000000000 bit strings outside the image to set a pixel in
		// odd numbered enhancements
		final Image output = new Image( step % 2 == 0 ? pixmap[0] : false );
						
		final Window2D win = input.window( );
		for( int x = win.getMinX( ) - 1; x <= win.getMaxX( ) + 1; x++ ) {
			for( int y = win.getMinY( ) - 1; y <= win.getMaxY( ) + 1; y++ ) {
				// convert to coordinate
				final Coord2D coord = new Coord2D( x, y );
				
				// get bit string encoded by the 3x3 grid with this coordinate at its center
				final BitString bits = new BitString( 9 );
				for( final int dy : new int[] { -1, 0, 1 } )
					for( final int dx : new int[] { -1, 0, 1 } )
						bits.set( (1-dy)*3 + (1-dx), input.get( coord.move( dx, dy ) ) );					

				// and set the pixel in the output accordingly
				output.set( coord, pixmap[ bits.toInt( ) ] );				
			}
		}
		
		// return the enhanced image
		return output;
	}
	

	/**
	 * Creates a new image scanner from the given string description of the pixel
	 * mapping
	 * 
	 * @param input The pixel map
	 * @return The ImageScanner with the given pixel mapping
	 */
	public static ImageScanner fromString( final String input ) {
		final boolean[] map = new boolean[ input.length( ) ];
		for( int i = 0; i < input.length( ); i++ )
			map[i] = input.charAt( i ) == '#';
		return new ImageScanner( map );
	}
}
