package challenges.day23.apods;

/**
 * Represents a single room in the burrow in which only Apods may reside of a
 * given family, unless they started in the room
 * 
 * @author Joris
 *
 */
public class BurrowRoom {
	/** The family to which this room belongs */
	protected final ApodFamily family;
	
	/** The integer position of this room onto the hallway */
	protected final int door;
	
	/** The apods contained in this room, per room depth */
	private final Apod[] apods;
	
	/** The number of occupants currently in the room */
	private int occupants;
	
	/** The room size */
	protected final int size;
	
	/**
	 * Creates a new BurrowRoom for the given family of Apods
	 * 
	 * @param family The family of apods that should end up in this room
	 * @param size The size (max depth) of this room
	 * @param opening The position at which this room opens up into the burrow
	 *   hallway
	 */
	public BurrowRoom( final ApodFamily family, final int size, final int opening ) {
		this.family = family;
		this.size = size;
		this.door = opening;
		
		this.apods = new Apod[ size ];
	}
	
	/**
	 * Adds a Apod to the room, can only add it to the first available slot
	 * 
	 * @param apod The apod to add to the room
	 */
	public void add( final Apod apod ) {
		if( occupants >= size ) throw new RuntimeException( "Failed to add apod " + apod + " to the room " + toString( ) + ": it is full!" );

		// add it in first available slot
		apods[ getFirstAvailable( ) ] = apod;
		occupants++;
	}
	
	/**
	 * Removes an apod from the room if it is the next one in the room
	 * 
	 * @param apod The apod to remove
	 */
	public void remove( final Apod apod ) {
		if( !canLeave( apod ) )
			throw new RuntimeException( "Cannot remove apod " + apod + " from the room " + toString( ) + ": it is not at the entrance of room!" );
		
		apods[ size - occupants ] = null;
		occupants--;
	}

	/**
	 * Checks if the given Apod is allowed to enter this room
	 * 
	 * @param apod The apod that wants to enter
	 * @return True iff the room's family type matches that of the Apod and no
	 *   Apods of other families are present in this room
	 */
	public boolean canEnter( final Apod apod ) {
		if( family != apod.family ) return false;
		if( occupants >= size ) return false;
		
		// check if occupied spots are my family members
		for( int i = size - occupants; i < size; i++ )
			if( apods[i].family != apod.family ) return false;
		
		// good to go!
		return true;
	}
	
	/**
	 * Checks if the given Apod can leave the room
	 * 
	 * @param apod The apod to test
	 * @return True iff the apod is the next in line from the door
	 */
	public boolean canLeave( final Apod apod ) {
		// can only leave if the apod is the next in line from the door
		return apods[size - occupants].equals( apod );
	}
	
	/**
	 * @return First available slot in the room, -1 for none
	 */
	public int getFirstAvailable( ) {
		return size - occupants - 1;
	}
	
	/**
	 * Checks if all occupants of this room are of the right family type
	 * 
	 * @return True iff all occupants are of the family that this room is
	 * supposed to be for
	 */
	public boolean checkHome( final Apod apod ) {
		if( apod.family != family ) return false;
		
		// check if everybody lower than me is of my family
		for( int i = apod.getY( ); i < size; i++ )
			if( apods[i].family != family ) return false;
		
		return true;
	}
	
	/** @return The number of apods in the room currently */
	public int getOccupied( ) { return occupants; }
	
	/**
	 * @param depth The room depth level (0 to size-1)
	 * @return The apod at the given room depth
	 */
	protected Apod getApod( final int depth ) {
		if( depth < 0 || depth > size - 1 ) throw new IllegalArgumentException( "Invalid room depth: " + depth + " (room size is " + size + ")" );
		return apods[ depth ];
	}
	
	/** @return The room description */
	@Override
	public String toString( ) {
		String res = family.toString( ) + door + ":";
		for( int i = 0; i < size; i++ )
			res += " " + (apods[i] != null ? apods[i].toString( ) : "__");
		return res;
	}

}
