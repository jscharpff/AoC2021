package challenges.day09;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aocutil.geometry.Coord2D;
import aocutil.io.FileReader;

public class Day09 {

	/**
	 * Day 9 of the Advent of Code 2021
	 * 
	 * https://adventofcode.com/2021/day/9
	 * 
	 * @param args The command line arguments
	 * @throws Exception
	 */
	public static void main( final String[] args ) throws Exception {
		final List<String> ex_input = new FileReader( Day09.class.getResource( "day09_example.txt" ) ).readLines();
		final List<String> input = new FileReader( Day09.class.getResource( "day09_input.txt" ) ).readLines( );
		
		System.out.println( "---[ Part 1 ]---" );
		System.out.println( "Example: " + part1( ex_input ) );
		System.out.println( "Answer : " + part1( input ) );

		System.out.println( "\n---[ Part 2 ]---" );
		System.out.println( "Example: " + part2( ex_input ) );
		System.out.println( "Example: " + part2( input ) );
	}
	
	/**
	 * Computes the sum of risk levels by finding the lowest points in the
	 * height map then summing over their height levels plus one. 
	 * 
	 * @param input The height map as a grid of height levels, one row per line
	 * @return The risk level of the height map
	 */
	public static long part1( final List<String> input ) {
		// build height map and find lowest points
		final HeightMap hm = new HeightMap( input );
		final List<Coord2D> lowest = hm.getLowestPoints( );
		
		// sum over risk levels of lowest points (height + 1)
		long risk = 0;
		for( final Coord2D c : lowest )
			risk += hm.getHeight( c ) + 1;
		
		return risk;
	}
	
	/**
	 * Determines the product of the 3 largest basins in the height map.
	 * 
	 * @param input The height map as a grid of height levels, one row per line
	 * @return The product of the 3 largest basins
	 */
	public static long part2( final List<String> input ) {
		final HeightMap hm = new HeightMap( input );		
		final List<Coord2D> lowest = hm.getLowestPoints( );
		
		final Map<Coord2D, Integer> basins = new HashMap<>( lowest.size( ) );
		// for every lowest point, find the basin sizes
		for( final Coord2D c : lowest ) {
			basins.put( c, hm.getBasinSize( c ) );
		}
		
		// then return the product of the three largest basin sizes
		final List<Integer> basinsizes = new ArrayList<>( basins.values( ) );
		Collections.sort( basinsizes );
		long prod = 1;
		for( int i = 0; i < 3; i++ ) {
			prod *= basinsizes.get( basinsizes.size( ) - i - 1 );
		}
		
		return prod;
	}
}
