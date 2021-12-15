package util.uniquepriorityqueue;

/**
 * A single element of the UniquePriorityQueue
 * 
 * @author Joris
 * @param <T> The data type of the key
 * @param <V> The data type of the value
 */
public class QElement<T, V> {
	/** The key to store */
	public final T key;
	
	/** The value to store */
	public final V value;
	
	/** The next element */
	protected QElement<T, V> prev;
	
	/** The previous element */
	protected QElement<T, V> next;
	
	/**
	 * Creates a new element of the Queue
	 * 
	 * @param key
	 * @param value
	 */
	public QElement( final T key, final V value ) {
		this.key = key;
		this.value = value;
		this.prev = null;
		this.next = null;
	}
	
	/** @return The string key=value */ 
	@Override
	public String toString( ) {
		return key + "=" + value;
	}
}