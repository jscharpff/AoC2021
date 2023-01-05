package challenges.day18;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Snailfish number!
 * 
 * @author Joris
 */
public class SNumber {
	/** The containing SNumber */
	protected SNumber parent;
	
	/** The index of this SNumber at the parent */
	protected int parentindex;
	
	/** The pair contained by this SNumber */
	protected final SNumber numbers[];
	
	/** Or, a literal value */
	protected final long value;
	
	/**
	 * Creates a new SNumber that holds a pair of literal values
	 * 
	 * @param value1
	 * @param value2
	 */
	public SNumber( final long value1, final long value2 ) {
		this( new SNumber( value1 ), new SNumber( value2 ) );
	}
	
	/**
	 * Creates a new SNumber that holds a pair of two other SNumbers
	 * 
	 * @param snum1
	 * @param snum2
	 */
	private SNumber( final SNumber snum1, final SNumber snum2 ) {
		this.parent = null;
		this.numbers = new SNumber[ 2 ];
		this.set( 0, snum1 );
		this.set( 1, snum2 );		
		this.value = -1;
	}
	
	/**
	 * Creates a new literal value SNumber
	 * 
	 * @param value The value it holds
	 */
	private SNumber( final long value ) {
		this.parent = null;
		this.numbers = null;
		this.value = value;
	}
	
	/**
	 * Sets the child element of this SNumber
	 * 
	 * @param index The index to set
	 * @param snum The SNumber to set at the index
	 */
	private void set( final int index, final SNumber snum ) {
		this.numbers[index] = snum;
		snum.parent = this;
		snum.parentindex = index;
	}
		
	/**
	 * Adds another Snailfish Number to this number and returns the result as a
	 * new SNumber
	 * 
	 * @param snum The Snailfish Number to add
	 * @return A new Snailfish Number that contains the result of the addition
	 */
	public SNumber add( final SNumber snum ) {
		// first part is simply making a pair out of the two numbers;
		final SNumber result = new SNumber( this, snum );
		
		// but after addition, it must be reduced!
		result.reduce( );
		
		return result;
	}
	
	/**
	 * Reduces the current SNumber
	 */
	protected void reduce( ) {
		// reduce until no changes occur
		boolean newround = true;
		while( newround ) {
			// first try to explode pairs that are contained within 4 other pairs
			newround = explode( 1 );
			
			// if explosion happened no other operation may be performed this iteration
			if( newround ) continue; 
			
			// no explosion, try splitting 
			newround = split( );
		}
	}
	
	/**
	 * Explodes a snail number of it is more than four pairs deep
	 * 
	 * @param depth The current depth of the pair
	 * @return True iff an explosion happened
	 */
	private boolean explode( final int depth ) {
		// cannot explode literals
		if( isLiteral( ) ) return false;
		
		// depth lower than 5 means further recursion to check contained numbers
		if( depth < 5 ) {
			if( numbers[0].explode( depth + 1 ) ) return true;
			if( numbers[1].explode( depth + 1 ) ) return true;
			return false;
		}
		
		// depth >= 5 means we have to explode!
		this.explode( );
		return true;
	}
	
	/**
	 * Performs the explosion of a SNumber
	 */
	private void explode( ) {
		// get the values to explode
		final long[] values = new long[] { numbers[0].value, numbers[1].value };
		
		// replace this element in the parent by a new literal zero
		parent.set( parentindex, new SNumber( 0 ) );
		
		// and explode the pair values to the left and right
		for( final int idx : new int[] { 0, 1 } ) {
			SNumber curr = this;
			while( curr.parent != null ) {
				// is this the element we are looking for, then we can set the value
				if( curr.parentindex == (1-idx) ) {
					// make sure we have the left/rightmost literal to explode into
					// i.e. exploding left needs the rightmost element on the left hand side
					SNumber explodeinto = curr.parent.numbers[idx];
					while( !explodeinto.isLiteral( ) ) explodeinto = explodeinto.numbers[(1-idx)];
					explodeinto.parent.set( explodeinto.parentindex, new SNumber( explodeinto.value + values[idx] ) );
					break;
				}
				
				// another level deeper
				curr = curr.parent;
			}
		}
	}
	
	/**
	 * Splits a regular number if its value is greater than 10
	 * 
	 * @return True iff a split was performed
	 */
	private boolean split( ) {
		if( isLiteral( ) ) {
			if( value < 10 ) return false;
			
			// split the number into a pair and replace it in the parent
			final long floor = Math.floorDiv( value, 2 );
			final SNumber splitnum = new SNumber( floor, value - floor );
			parent.set( parentindex, splitnum );
			
			return true;
		} 
		
		// not a literal, check for splits in the pair
		if( numbers[0].split( ) ) return true;
		if( numbers[1].split( ) ) return true;
		return false;		
	}
	
	/** @return True iff the SNumber is a literal value */
	public boolean isLiteral( ) { return this.numbers == null; }
	
	/**
	 * Creates a Snailfish Number from a string description. Will recursively
	 * decode the input String in case of contained numbers
	 * 
	 * @param input The string input
	 * @return The Snailfish Number
	 */
	public static SNumber fromString( final String input ) {
		// try and parse this Snailfish Number as a pair first
		final Matcher m = Pattern.compile( "^\\[(.+)\\]$" ).matcher( input );
		if( m.find( ) ) {
			// parse its contents
			final String contents = m.group( 1 );
	
			// go over the string and find the comma separating the pairs
			int depth = 0;
			int split = -1;
			for( int i = 0; i < contents.length( ); i++ )
				if( contents.charAt( i ) == '[' ) depth++;
				else if( contents.charAt( i ) == ']' ) depth--;
				else if( contents.charAt( i ) == ',' && depth == 0 ) {
					split = i;
					break;
				}
					
			if( split == -1 ) throw new IllegalArgumentException( "Invalid pair: " + input );
					
			return new SNumber( SNumber.fromString( contents.substring( 0, split ) ), 
					SNumber.fromString( contents.substring( split + 1 ) ) );
		} else {
			// it is a single value, parse it as such
			return new SNumber( Long.parseLong( input ) );
		}
	}
	
	/**
	 * Computes the magnitude of the number
	 * 
	 * @return The magnitude
	 */
	public long getMagnitude( ) {
		if( isLiteral( ) ) return value;
		
		return 3 * numbers[0].getMagnitude( ) + 2 * numbers[1].getMagnitude( );
	}
	
	/**
	 * @return A string representation of the Snailfish Number, recursively
	 *   evaluates contained numbers
	 */
	@Override
	public String toString( ) {
		if( isLiteral( ) ) return "" + value;
		
		return "[" + numbers[0] + "," + numbers[1] + "]";
	}
	
	/**
	 * Two SNumbers equal if they are both a literal or they contain the same
	 * elements
	 * 
	 * @param obj The other SNumber to test
	 * @return True iff obj equals this SNumber
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof SNumber) ) return false;
		final SNumber snum = (SNumber) obj;
		
		if( isLiteral( ) ) return snum.isLiteral( ) && this.value == snum.value;
		else return !snum.isLiteral( ) && numbers[0].equals( snum.numbers[0] ) && numbers[1].equals( snum.numbers[1] );
	}
	
	/**
	 * Copies a Snailfish number
	 * 
	 * @return The copy
	 */
	public SNumber copy( ) {
		if( isLiteral( ) )
			return new SNumber( this.value );
		else
			return new SNumber( numbers[0].copy( ), numbers[1].copy( ) );
		
	}
}
