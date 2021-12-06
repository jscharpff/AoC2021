package challenges.day06;

/**
 * Class describing a school of LanternFish
 * 
 * @author Joris
 */
public class LanternSchool {
	/** The fish in the school currently, represented by number of fish per spawn time */
	protected final long[] fishtimers;
	
	/** Number of fish currently in the school */
	protected long count;
	
	/**
	 * Creates a new, empty school of lanterns 
	 */
	public LanternSchool( ) {
		fishtimers = new long[ 10 ];
		count = 0;
	}
	
	/**
	 * Creates a new school of Lanternfish from a given population
	 * 
	 * @param fish The initial fish population, represented by spawn timers
	 */
	public LanternSchool( final int[] fish ) {
		this( );
		count = fish.length;
		
		// convert individual fish timers into a summary that just counts the
		// number of fish per timer value
		for( int i = 0; i < fish.length; i++ )
			fishtimers[ fish[i] ]++;
	}
	
	/**
	 * Runs the school simulation for the given number of days
	 * 
	 * @param days The number of days to run
	 */
	public void runSimulation( final int days ) {
		for( int i = 0; i < days; i++ ) step( );
	}
	
	/**
	 * Runs one round (day) of the school simulation
	 */
	protected void step( ) {
		// first treat the spawning fish (at index 0): reset their index to 6 and
		// add an equal number of new fish at 8
		// NOTE: indexes are 7 and 9 as they will be shift left next
		final long newfish = fishtimers[0];
		fishtimers[7] += newfish;
		fishtimers[9] += newfish;
		count += newfish;
		
		// move all timers one down by shifting them left in the array
		// this automatically clears the number of spawning fish
		for( int i = 0; i < fishtimers.length - 1; i++ )
			fishtimers[i] = fishtimers[ i + 1 ];
		
		// clear last index after shift
		fishtimers[ fishtimers.length - 1 ] = 0;
	}
	
	/**
	 * @return The number of fish currently in the school
	 */
	public long size( ) {
		return count;
	}
		
	/**
	 * Creates a new school of Lanternfish from the string description
	 * 
	 * @param input The string describing the fish population
	 * @return A new school of Lanternfish
	 */
	public static LanternSchool fromString( final String input ) {
		final LanternSchool ls = new LanternSchool( );
		
		// store not the fish itself but add one to the index that corresponds
		// to the spawn timer value of the fish. This is vastly more compact than
		// storing individual fish timers
		for( final String s : input.split( "," ) ) {
			ls.fishtimers[ Integer.parseInt( s ) ]++;
			ls.count++;
		}
		
		return ls;
	}
	
	/**
	 * @return A string describing the school of fish
	 */
	@Override
	public String toString( ) {
		String res = "" + fishtimers[0];
		for( int i = 1 ; i < fishtimers.length; i++ )
			res += "," + fishtimers[i];
		return res;			
	}
}
