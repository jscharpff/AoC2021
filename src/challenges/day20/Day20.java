package challenges.day20;

import java.util.ArrayList;
import java.util.List;

import util.io.FileReader;

public class Day20 {

	/**
	 * Day 20 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/20
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day20.class.getResource( "day20_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day20.class.getResource( "day20_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + enhanceImage( ex_input, 2 ) );
		System.out.println( "Answer : " + enhanceImage( input, 2 ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + enhanceImage( ex_input, 50 ) );
		System.out.println( "Answer : " + enhanceImage( input, 50 ) );
		
	}
	
	/**
	 * Reads an image algorithm and image from input and enhances that image for
	 * the specified number of times using the algorithm, the outputs the number
	 * of lit pixels.
	 *
	 * @param input The input with the algorithm on one line, a blank line and
	 *   the images on the remaining lines   
	 * @param enhance The number of times to enhance the image
	 * @return The count of pixels that are lit in the resulting image
	 */
	public static long enhanceImage( final List<String> input, final int enhance ) {
		// process input, first line is the image scanner, then a blank line
		final List<String> in = new ArrayList<>( input );
		final ImageScanner scanner = ImageScanner.fromString( in.remove( 0 ) );
		in.remove( 0 );
		
		// the rest is the input for the initial image
		Image image = Image.fromStringList( in );
		
		// enhance it for the given number of times!
		for( int i = 0; i < enhance; i++ ) {
			image = scanner.enhance( image, i );
		}		
			
		// and return the count of set pixel
		return image.getLit( );
	}
}
