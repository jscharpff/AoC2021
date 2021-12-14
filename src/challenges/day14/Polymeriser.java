package challenges.day14;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that performs the polymerisation process
 * 
 * @author Joris
 */
public class Polymeriser {
	/** The polymerisation rule set */
	protected final Map<String, Character> rules;
			
	/**
	 * Creates a new Polymeriser from a list of strings that describe its element
	 * expansion rule set
	 * 
	 * @param input The list of strings that contain XY -> Z rules
	 */
	public Polymeriser( final List<String> input ) {
		rules = new HashMap<>( input.size( ) );
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "(\\w\\w)\\s*->\\s*(\\w)" ).matcher( s );
			if( !m.find( ) ) throw new IllegalArgumentException( "Invalid polymerisation rule: " + s );
			
			rules.put( m.group( 1 ), m.group( 2 ).charAt( 0 ) );
		}
	}
	
	/**
	 * Performs the polymerisation process on the given polymer string for a
	 * given number of steps. In each step, all pairs of the polymer are expanded 
	 * according to the rule set. The resulting polymer is the input for the next
	 * step until the specified number of steps have been performed. 
	 * 
	 * @param polymer The initial polymer
	 * @param steps The number of polymerisation steps to perform
	 * @return The result as a count per elements
	 */
	public ElementCount polymerise( final String polymer, int steps ) {
		if( steps < 1 ) throw new IllegalArgumentException( "Invalid number of steps: " + steps );
		
		// initialise a memoisation table to use while expanding polymers
		final Map<String, ElementCount> M = new HashMap<>( );
		
		// split the polymer into pairs and recursively polymerise each pair for the
		// given number of steps
		final ElementCount result = new ElementCount( polymer );
		for( int i = 0; i < polymer.length( ) - 1; i++ ) {
			result.addAll( polymerise( M, polymer.charAt( i ), polymer.charAt( i + 1 ), steps - 1 ) );
		}
		
		return result;
	}
	
	/**
	 * Polymerises the specific pair and continues the process until the number
	 * of steps have been performed. Uses a memoisation table to store counts
	 * for every combination of (e1,e2,steps) for future re-use.
	 */
	private ElementCount polymerise( final Map<String, ElementCount> M, final char e1, final char e2, int steps ) {
		// check the memoisation table if we already now this expansion
		final String memkey = "" + e1 + e2 + steps;
		if( M.containsKey( memkey ) )	return M.get( memkey );
		
		// get the new element by applying the rule set and create counter that
		// holds this element, the rest will be added in future iterations (if any)
		final char newelem = rules.get( "" + e1 + e2 );
		final ElementCount elements = new ElementCount( newelem );
		
		// end of the line?
		if( steps == 0 ) return elements;
		
		// nope, expand further and add to the element counter
		elements.addAll( polymerise( M, e1, newelem, steps - 1 ) );
		elements.addAll( polymerise( M, newelem, e2, steps - 1 ) );
		
		// store the result in the memoisation table for re-use
		M.put( memkey, elements );		
		return elements;
	}
	
	/**
	 * @return Returns the string description of the rule set
	 */
	@Override
	public String toString( ) {
		return rules.toString( );
	}

}
