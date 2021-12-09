package challenges.day08;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class that can decode character-encoded digits by inferring the encoding
 * from a set of input signals
 * 
 * @author Joris
 */
public class DigitDecoder {
	/** The map of all segments in the input and their decoded value */
	protected final Map<Integer, Set<Character>> digits;

	/**
	 * Creates a new DigitEncoder
	 * 
	 * @param strinput The initial input that contains the segment encoding for
	 *   all 10 digits from zero to nine
	 */
	public DigitDecoder( final String[] strinput ) {
		if( strinput.length != 10 ) 
			throw new RuntimeException( "Invalid segment encoding as input, exactly 10 segments are required."  );
		
		// allocate result map and start finding deducing the encoding!
		digits = new HashMap<>( 10 );
		buildMapping( strinput );
	}
	
	/**
	 * Reconstructs the digit mapping from the input samples
	 */
	protected void buildMapping( final String[] input ) {
		// list of remaining input strings, discarding the output part for now
		final List<Set<Character>> remaining = new ArrayList<>(  );
		for( final String in : input )
			remaining.add( str2charset( in ) );
			
		// get input segments that lead to unique digits 	
		for( final String s : input ) {
			final Set<Character> cs = str2charset( s );
			if( s.length( ) == 2 ) { found( remaining, cs, 1 ); continue; }
			if( s.length( ) == 3 ) { found( remaining, cs, 7 ); continue; }
			if( s.length( ) == 4 ) { found( remaining, cs, 4 ); continue; }
			if( s.length( ) == 7 ) { found( remaining, cs, 8 ); continue; }
		}
		
		// now deduce digit encodings from segment descriptions
		
		// the segment diff between 8 and 1 can only be in 6
		final Set<Character> diff81 = new HashSet<Character>( digits.get( 8 ) );
		diff81.removeAll( digits.get( 1 ) );
		found( remaining, find( remaining, diff81, -1, true ), 6);
		
		// of the now remaining digits, the diff between 8 and 4 can now only be 2 or 0
		final Set<Character> diff84 = new HashSet<Character>( digits.get( 8 ) );
		diff84.removeAll( digits.get( 4 ) );
		found( remaining, find( remaining, diff84, 6, true ), 0 );
		found( remaining, find( remaining, diff84, 5, true ), 2 );
		
		// of the remaining digits, the only one that does NOT have the difference between 8 and 2 is 3
		final Set<Character> diff82 = new HashSet<Character>( digits.get( 8 ) );
		diff82.removeAll( digits.get( 2 ) );
		found( remaining, find( remaining, diff82,-1, false ), 3 );
		
		// finally, 5 and 9 can be discerned by their length
		for( int i = remaining.size( ) - 1; i >= 0; i-- ) {
			final Set<Character> s = remaining.get( i );
			if( s.size( ) == 5 ) found( remaining, s, 5 );
			else if( s.size( ) == 6 ) found( remaining, s, 9 );
		}
		
		// check length
		if( remaining.size( ) > 0 ) throw new RuntimeException( "Not all digits have been decoded, remaining: " + remaining );
	}
	
	/**
	 * Sets the encoding for the given digit and removes it from the remaining list
	 * 
	 * @param remaining The list of digits remaining
	 * @param encoding The segment encoding of the digit 
	 * @param digit The integer value of the digit to store
	 */
	protected void found( final List<Set<Character>> remaining, final Set<Character> encoding, final int digit ) {
		digits.put( digit, encoding );
		remaining.remove( encoding );
	}
	
	/**
	 * Goes over all remaining segment encodings to find the segment code to 
	 * match/not match. Returns the segment encoding.
	 * 
	 * Parameters are length to consider only segments of a given size and
	 * shouldmatch to specify whether the segment should or should not contain
	 * all of the segments in the match string
	 * 
	 * @param remaining The list of remaining encoded segments
	 * @param match The string of segments to match
	 * @param digit The digit that we are looking for
	 * @param length The number of segments we are looking for in the encoding. 
	 *   Can be -1 for any length
	 * @param shouldmatch True to require all segments of match to be present, 
	 *   false to require that none of the segments of match are present
	 * @return The segment encoding that matches the filter criteria
	 */
	protected Set<Character> find( final List<Set<Character>> remaining, final Set<Character> match, final int length, final boolean shouldmatch ) {
		for( int i = remaining.size( ) - 1; i >= 0; i-- ) {
			final Set<Character> s = remaining.get( i );
			if( s.containsAll( match ) == shouldmatch ) {
				if( length != -1 && s.size( ) != length ) continue;
				
				return s;
			}
		}
		
		throw new RuntimeException( "No segment matched: " + match );
	}
	
	/**
	 * Decodes a given array of segment encodings into an integer value
	 * 
	 * @param segments The array of encoded segments to decode
	 * @return The decoded integer from the list of digit encodings
	 */
	public long decode( final String... segments ) {
		long value = 0;
		int d = 1;
		
		// do a backwards loop to reconstruct per decimal
		for( int i = segments.length - 1; i >= 0; i-- ) {
			value += decodeSegment( segments[i] ) * d;
			d *= 10;
		}
		return value;
	}
	
	/**
	 * Decodes a single digit
	 * 
	 * @param in The segment encoding of the digit
	 * @return The digit that it encodes
	 */
	protected long decodeSegment( final String in ) {		
		for( int i : digits.keySet( ) ) {
			final Set<Character> dstr = digits.get( i );
			
			// segment encodings may be in any order, so check length first and then
			// compare all characters of both segments
			if( in.length( ) == dstr.size( ) &&  dstr.containsAll( str2charset( in ) ) ) return i;
		}
		
		throw new RuntimeException( "Failed to decode digit from input " + in );
	}
	
	
	protected Set<Character> str2charset( final String str ) {
		final Set<Character> chars = new HashSet<>( );
		str.chars( ).forEach( c -> chars.add( (char)c ) );
		return chars;
	}
	
	/**
	 * @return The string that describes the current mapping 
	 */
	@Override
	public String toString( ) {
		return digits.toString( );
	}
}
