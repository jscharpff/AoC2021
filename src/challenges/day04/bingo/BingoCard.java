package challenges.day04.bingo;

/**
 * Class that holds one Bingo card
 *  
 * @author Joris
 */
public class BingoCard {
	/** The number of columns */
	protected final int cols;
	
	/** The number of rows */
	protected final int rows;
	
	/** The actual numbers on the card */
	protected final int[][] numbers;
	
	/** Flags that indicate if a number has been drawn */
	protected final boolean[][] hit;
	
	/**
	 * Creates a new N x M sized Bingo card
	 * 
	 * @param N The numbers of columns
	 * @param M The number of rows
	 */
	protected BingoCard( final int N, final int M ) {
		this.cols = N;
		this.rows = M;
		
		numbers = new int[N][M];
		hit = new boolean[N][M];
	}
	
	/**
	 * Checks whether the card has a the specified number on it. If so and his
	 * leads to a Bingo, return true. Otherwise false
	 * 
	 * @param number The number to check
	 * @return True if the number is on the card and let to a Bingo
	 */
	protected boolean check( final int number ) {
		// go over the entries and try and find the number
		for( int i = 0; i < rows; i++ )
			for( int j = 0; j < cols; j++ ) {
				if( numbers[i][j] == number ) {
					// we found the number on our card, check also if this leads to a Bingo
					hit[i][j] = true;
					return checkBingo( i, j );
				}
			}
		
		// not on this card
		return false;
	}
	
	/**
	 * Checks if a Bingo occurs on either the row or column number as result of a
	 * new hit
	 * 
	 * @param row The row to check
	 * @param col The column to check
	 * @return True iff a Bingo occurred
	 */
	protected boolean checkBingo( final int row, final int col ) {
		// check row first
		boolean bingo = true;
		for( int j = 0; j < cols; j++ ) bingo &= hit[row][j];
		if( bingo ) return true;
		
		// check column
		bingo = true;
		for( int i = 0; i < rows; i++ ) bingo &= hit[i][col];
		if( bingo ) return true;
		
		// false bingo! hahaha... haha.. ha...
		return false;
	}
	
	/**
	 * @return The sum of unmarked numbers
	 */
	public long sumUnmarked( ) {
		long res = 0;
		for( int i = 0; i < rows; i++ )
			for( int j = 0; j < cols; j++ )
				if( !hit[i][j] ) res += numbers[i][j];
		
		return res;
	}

	/**
	 * Creates a new Bingo Card from a String description. Columns are space
	 * separated, rows by newlines
	 * 
	 * @param card The string representation of the Card
	 * @return The BingoCard
	 */
	public static BingoCard fromString( final String card ) {
		// determine bingo card size and instantiate it
		final String[] rows = card.split( "," );
		final int columns = rows[0].split( "\s+" ).length;
		final BingoCard bc = new BingoCard( rows.length, columns );
		
		// now parse each row
		for( int i = 0; i < rows.length; i++ ) {
			final String[] c = rows[i].split( "\s+" );
			for( int j = 0; j < c.length; j++ ) {
				bc.numbers[i][j] = Integer.parseInt( c[j] );
				bc.hit[i][j] = false;
			}
		}
		
		return bc;
	}
	
	/**
	 * @return The bingo card as N x M table in a String
	 */
	@Override
	public String toString( ) {
		String res = "";
		for( int i = 0; i < rows; i++ ) {
			for( int j = 0; j < cols; j++ ) {
				res += "" + numbers[i][j] + " ";
			}
			res += "\n";
		}
		return res;
	}
}
