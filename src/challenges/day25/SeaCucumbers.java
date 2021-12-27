package challenges.day25;

import java.util.List;

public class SeaCucumbers {
	/** The grid od sea cucumbers */
	private final char[][] grid;
	
	/** The number of rows and columns in the grid */
	private final int rows, columns;
	
	/** The grid values */
	private final char C_EAST = '>', C_SOUTH = 'v', C_EMPTY = '.';
	
	/**
	 * Creates a new NxM empty grid of sea cucumbers
	 * 
	 * @param rows The number of rows
	 * @param cols The number of columns
	 */
	private SeaCucumbers( final int rows, final int cols ) {
		this.grid = new char[rows][cols];
		this.rows = rows;
		this.columns = cols;
	}
	
	/**
	 * Simulates the given number of steps
	 */
	public void simulate( final int steps ) {
		for( int i = 0; i < steps; i++ )
			step( );
	}
	
	/**
	 * Simulates the sea cucumber movement until no cucumber moves anymore
	 * 
	 * @return The number of steps until they stop moving
	 */
	public int simulateUntilStopped( ) {
		int steps = 0;
		while( steps < 100000 ) {
			steps++;
			if( step( ) == 0 ) return steps;
		}
		return -1;
	}
	
	/**
	 * Simulates a single step in the sea cucumber movement
	 * 
	 * @return The number of sea cucumbers that moved
	 */
	private int step( ) {
		int moves = 0;
		final char[][] newgrid = new char[ rows ][ columns ];
		for( int y = 0; y < rows; y++ )
			for( int x = 0; x < columns; x++ )
				newgrid[y][x] = grid[y][x];
		
		// east facing first
		for( int y = 0; y < rows; y++ )
			for( int x = 0; x < columns; x++ ) {
				if( grid[y][x] == C_EAST && grid[y][(x+1)%columns] == C_EMPTY ) {
					newgrid[y][(x+1)%columns] = C_EAST;
					newgrid[y][x] = C_EMPTY;
					moves++;
				}
			}

		// swap to update for east moves
		for( int y = 0; y < rows; y++ )
			for( int x = 0; x < columns; x++ )
				grid[y][x] = newgrid[y][x];
		
		// then south facing
		for( int x = 0; x < columns; x++ )
			for( int y = 0; y < rows; y++ )
				if( grid[y][x] == C_SOUTH && grid[(y+1)%rows][x] == C_EMPTY ) {
					newgrid[(y+1)%rows][x] = C_SOUTH;
					newgrid[y][x] = C_EMPTY;
					moves++;
				}

		for( int y = 0; y < rows; y++ )
			for( int x = 0; x < columns; x++ )
				grid[y][x] = newgrid[y][x];
		return moves;
	}

	/**
	 * Creates a grid of Sea Cucmbers from a list of string
	 * 
	 * @param input The list of strings
	 * @return The SeaCucumber grid
	 */
	public static SeaCucumbers fromStringList( final List<String> input ) {
		final int N = input.size( );
		final int M = input.get( 0 ).length( );
		
		final SeaCucumbers sc = new SeaCucumbers( N, M );
		for( int row = 0; row < sc.rows; row++ )
			for( int col = 0; col < sc.columns; col++ )
				sc.grid[row][col] = input.get( row ).charAt( col );
		
		return sc;
	}
	
	/** @param Prints a grid of Sea Cucumbers */
	@Override
	public String toString( ) {
		String res = "";
		for( int y = 0; y < rows; y++ ) {
			for( int x = 0; x < columns; x++ ) {
				res += "" + grid[y][x];
			}
			res += "\n";
		}
		return res.substring( 0, res.length( ) - 1 );
	}
}
