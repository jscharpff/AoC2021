package challenges.day25;

import java.util.List;

/**
 * Class that represents a grid of Sea Cucumbers and simulates their collective
 * movement.
 * 
 * @author Joris
 */
public class SeaCucumbers {
	/** The grid of sea cucumbers */
	private final char[][] grid;
	
	/** The number of rows and columns in the grid */
	private final int rows, columns;
	
	/** The grid values, i.e. the types of Sea Cucumbers */
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
	 * Simulates the movement for the given number of steps
	 * 
	 * @param steps The number of steps to simulate
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
		return move( C_EAST, 0, 1 ) + move( C_SOUTH, 1, 0 );
	}
	
	/**
	 * Performs the move for the specified family of Sea Cucumbers. This will
	 * update the grid and return the number of Sea Cucumbers that moved.
	 * 
	 * @param ctype The type of Sea Cucumbers to move
	 * @param dy The vertical movement
	 * @param dx The horizontal movement
	 * @return The number of Sea Cucumbers of the family that successfully moved
	 */
	protected int move( final char ctype, final int dy, final int dx ) {	
		// copy the current grid
		final char[][] newgrid = new char[ rows ][ columns ];
		for( int y = 0; y < rows; y++ )
			for( int x = 0; x < columns; x++ )
				newgrid[y][x] = grid[y][x];
		
		// move the sea cucumbers if their target position is available
		int moves = 0;
		for( int y = 0; y < rows; y++ )
			for( int x = 0; x < columns; x++ ) {
				final int newy = (y + dy) % rows, newx = (x + dx) % columns;
				if( grid[y][x] != ctype || grid[newy][newx] != C_EMPTY ) continue;
					
				// this one can move!
				newgrid[newy][newx] = ctype;
				newgrid[y][x] = C_EMPTY;
				moves++;
			}

		// update the grid after all moves have been performed (if any)
		if( moves > 0 ) {
			for( int y = 0; y < rows; y++ )
				for( int x = 0; x < columns; x++ )
					grid[y][x] = newgrid[y][x];
		}
		
		return moves;
	}

	/**
	 * Creates a grid of Sea Cucumbers from a list of string
	 * 
	 * @param input The list of strings
	 * @return The SeaCucumber grid
	 */
	public static SeaCucumbers fromStringList( final List<String> input ) {
		// first determine grid size
		final int N = input.size( );
		final int M = input.get( 0 ).length( );
		
		// then get all the Sea Cucumbers themselves
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
