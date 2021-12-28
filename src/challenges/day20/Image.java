package challenges.day20;

import java.util.List;

import aocutil.geometry.Coord2D;
import aocutil.geometry.Window2D;
import aocutil.grid.CoordGrid;

/**
 * Class that holds an image of simple pixels that can be either on or off 
 * 
 * @author Joris
 */
public class Image {
	/** The coordgrid that represents the pixels in the image */
	private final CoordGrid<Boolean> pixels;
	
	/**
	 * Creates a new empty image
	 * 
	 * @param defpixel The default value for pixels if not explicitly set
	 */
	public Image( final boolean defpixel ) {
		this.pixels = new CoordGrid<Boolean>( defpixel );
	}
	
	/** @return The count of lit pixels */
	public int getLit( ) {
		int count = 0;
		for( final Coord2D c : window( ) )
			count += pixels.get( c ) ? 1 : 0;
		
		return count;
	}
	
	/**
	 * Returns the value of the pixel at x,y
	 * 
	 * @param coord The pixel
	 * @return The value of the pixel, true for lit
	 */
	public boolean get( final Coord2D coord ){
		return pixels.get( coord );
	}
	
	/**
	 * Sets the pixel at x,y to be on or off
	 * 
	 * @param coord The coordinate
	 * @param value The value to set, true for on
	 */
	public void set( final Coord2D coord, final boolean value ) {
		// only add if not equal to default to keep the grid sparse
		if( value != pixels.getDefaultValue( ) )
			pixels.add( coord, value );
	}
	
	/** @return The size of the image as 2D window */
	public Window2D window( ) {
		return pixels.window( );
	}
	
	/**
	 * Reconstructs an image from a grid
	 * 
	 * @param input The image as list of strings describing the rows of pixels
	 * @return The reconstructed image
	 */
	public static Image fromStringList( final List<String> input ) {
		final Image image = new Image( false );
				
		// parse rows and columns
		int y = -1;
		for( final String row : input ) {
			y++;
			for( int x = 0; x < row.length( ); x++ ) {
				image.set( new Coord2D( x, y ), row.charAt( x ) == '#' );
			}
		}
		
		return image;
	}
	
	/** @return The image as grid of '#' and '.' pixels */
	@Override
	public String toString( ) {
		return pixels.toString( x -> x ? "#" : "." );
	}
}
