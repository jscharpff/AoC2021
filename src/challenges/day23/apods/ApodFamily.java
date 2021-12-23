package challenges.day23.apods;

/**
 * The different families of Amphipods
 * 
 * @author Joris
 */
public enum ApodFamily {
	Amber ('A', 1),
	Bronze ('B', 10),
	Copper ('C', 100),
	Desert ('D', 1000);
	
	/** The family classification letter */
	protected final char classification;
	
	/** The energy consumption per move of this family */
	protected final int energy;

	/**
	 * Creates a new Amphipod family type
	 * 
	 * @param classification The letter that classifies its family
	 * @param enrgy The energy consumption per move
	 */
	private ApodFamily( final char classification, final int energy ) {
		this.classification = classification;
		this.energy = energy;
	}
	
	/** @return The family classification letter */
	@Override
	public String toString( ) {
		return "" + classification;
	}
	
	/** 
	 * @param type The family classification letter 
	 * @return The family type from the classification letter
	 */ 
	public static ApodFamily fromFamilyClass( final char type ) {
		for( final ApodFamily fam : values( ) )
			if( fam.classification == type ) return fam;
		
		throw new IllegalArgumentException( "Invalid fmailiy classification: " + type );
	}
}
