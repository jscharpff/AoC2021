package challenges.day14;

/**
 * The ElementCount class is a container that holds counts per element type
 * that is used in the polymerisation process. It uses an char-indexed array as
 * its internal data structure.
 */
public class ElementCount {
	/** First index for array translation */
	private final static int START_IDX = (int)'A';
	
	/** The count per element */
	protected long[] count;
	
	/**
	 * Creates a new empty ElementCount
	 */
	public ElementCount( ) {
		this.count = new long[ 26 ];
	}
	
	/**
	 * Creates a new ElementCount with only a single element as its initial
	 * count
	 * 
	 * @param element The initial element to hold
	 */
	public ElementCount( final char element ) {
		this( );
		add( element, 1 );
	}
	
	/**
	 * Creates a new ElementCount with the initial polymer to set start counts
	 * 
	 * @param polymer The initial polymer
	 */
	public ElementCount( final String polymer ) {
		this( );
		
		for( int i = 0; i < polymer.length( ); i++ )
			add( polymer.charAt( i ), 1 );
	}
	
	/**
	 * Adds the count to the given element (as capital character)
	 * 
	 * @param element The element to increase count of
	 * @param value The value to add
	 */
	public void add( final char element, final long value ) {
		final int idx = (int)element - START_IDX;
		count[ idx ] += value;
	}
	
	/**
	 * Adds all the element counts of the other ElementCount to this one
	 * 
	 * @param elems The other element count object
	 */
	public void addAll( final ElementCount elems ) {
		for( int i = 0; i < count.length; i++ ) {
			count[i] += elems.count[i];
		}
	}
	
	/** @return The highest element count */
	public long getHighestCount( ) {
		long max = -1;
		for( int i = 0; i < count.length; i++ )
			if( count[i] > max ) max = count[i];
		return max;
	}

	/** @return The lowest element count (over present elements with count > 0) */
	public long getLowestCount( ) {
		long min = Long.MAX_VALUE;
		for( int i = 0; i < count.length; i++ ) {
			if( count[i] > 0 && count[i] < min ) min = count[i];
		}
		return min;
	}
	
	/**
	 * @return The string description that includes the count for present
	 *   elements, i.e. where the count > 0
	 */
	@Override
	public String toString( ) {
		String res = "";
		for( int i = 0; i < count.length; i++ ) {
			if( count[i] > 0 ) res += (char)(i + START_IDX) + "=" + count[i] + ",";
		}
		return res.length( ) > 0 ? res.substring( 0, res.length( ) - 1 ) : res;
	}
}
