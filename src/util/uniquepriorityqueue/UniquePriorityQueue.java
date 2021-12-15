package util.uniquepriorityqueue;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Priority queue implementation that only allows unique keys. If a key is
 * added twice to the queue, its value will be updated iff the value is lower
 * according to the comparator of the value
 * 
 * @author Joris
 *
 * @param <T> The type of the key element that can be queued
 * @param <V> The type of the values stored for the keys
 */
public class UniquePriorityQueue<T,V extends Comparable<V>> {
	/** The head of the queue */
	private QElement<T, V> head;
	
	/** The values for the elements currently in the Queue */
	private final Map<T, V> values;
	
	/**
	 * Creates a new empty PriorityQueue that will only hold unique keys as its
	 * elements
	 */
	public UniquePriorityQueue( ) {
		head = null;
		values = new HashMap<>( );
	}
	
	/**
	 * Inserts a single element into the queue. If the element was already
	 * present, its value will be replaced if the new value is lower according
	 * to the comparator implemented on the value data type class. 
	 * 
	 * @param key The key to insert
	 * @param value The value to set for the key
	 */
	public void insert( final T key, final V value ) {
		// first element
		if( head == null ) {
			head = new QElement<T,V>( key, value );
			values.put( key, value );
			return;
		}
		
		if( values.containsKey( key ) ) {
			// already a better value in the queue?
			final V oldvalue = values.get( key );
			if( oldvalue.compareTo( value ) <= 0 ) return;
			
			// no, remove it before adding the new one
			remove( key );
		}
		
		// set the (new) value for the key
		final QElement<T, V> elem = new QElement<>( key, value );
		values.put( key, value );
		
		// find the position to insert the new element at
		QElement<T,V> curr = head;
		QElement<T,V> last = null;
		do {
			last = curr;
			if( value.compareTo( values.get( curr.key ) ) > 0 ) continue;
			
			// insert before this one
			if( curr.equals( head ) ) head = elem;
			else curr.prev.next = elem;
			
			elem.prev = curr.prev;
			elem.next = curr;
			curr.prev = elem;
			return;
		} while( (curr = curr.next) != null );
		
		// not inserted, insert at back of queue
		last.next = elem;
		elem.prev = last;
 
	}
	
	/**
	 * Removes a key from the queue
	 * 
	 * @param key The key to remove
	 */
	public void remove( final T key ) {
		values.remove( key );
		QElement<T, V> curr = head;
		do {
			if( !curr.equals( key ) ) continue;
			
			if( curr.prev == null ) {
				head = curr.next;
				head.prev = null;
			} else {
				curr.prev.next = curr.next;
				curr.next.prev = curr.prev;
			}
		} while( (curr = curr.next) != null );
	}
	
	/**
	 * Removes and returns the head of the queue
	 * 
	 * @return The current head
	 * @throws NoSuchElementException if the queue is empty
	 */
	public QElement<T, V> poll( ) {
		if( size() == 0 ) throw new NoSuchElementException();
		
		final QElement<T, V> e = head;
		values.remove( head.key );
		head = head.next;
		if( head != null ) head.prev = null;
		return e;
	}
	
	/**
	 * @return The size of the current queue
	 */
	public int size( ) {
		return values.size( );
	}
	
	/**
	 * @return The current elements in the queue
	 */
	@Override
	public String toString( ) {
		if( head == null ) return "(empty)";

		String res = head.toString( );
		QElement<T, V> curr = head;
		while( (curr = curr.next) != null ) {
			res += "," + curr.toString( );
		}
		return res;
	}
}
