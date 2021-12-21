package challenges.day21.dirac;

/**
 * Models a deterministic dice that will always return the integer value one
 * larger than the previous roll. If the dice hits the roll value of 100 it
 * resets to 1. 
 * 
 * @author Joris
 */
public class DeterministicDice {
	/** The number of total rolls */
	private int rolls;

	/** The last rolled number */
	private int lastroll;
	
	/**
	 * Creates a new Deterministic Dice
	 */
	public DeterministicDice( ) {
		this.rolls = 0;
		this.lastroll = 0;
	}
	
	/**
	 * Rolls the dice another time
	 * 
	 * @return The roll value
	 */
	public int roll( ) {
		rolls++;
		if( ++lastroll > 100 ) lastroll = 1;
		return lastroll;
	}	
	
	/** @return The total number of rolls */
	public int getRolls( ) { return rolls; } 
}
