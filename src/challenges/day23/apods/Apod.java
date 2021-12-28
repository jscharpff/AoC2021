package challenges.day23.apods;

import aocutil.geometry.Coord2D;

/**
 * One single Amphipods, or Apod for short, that lives in the deep sea
 * 
 * @author Joris
 */
public class Apod {
	/** The index to uniquely identify it in sets and lists */
	private final int ID;
	
	/** The next available ID */
	private static int nextID = 0;
	
	/** The family of Apods it belongs to */
	protected final ApodFamily family;
	
	/** The current hallway position of the Apod, -1 for not in hall */
	private Coord2D position;
	
	/** The room that the apod is currently in */
	private BurrowRoom room;
	
	
	/**
	 * Creates a new Apod of the given family
	 * 
	 * @param family The family of Apods it belongs to
	 * @param position It's starting position
	 */
	protected Apod( final char family, final Coord2D position ) {
		this.family = ApodFamily.fromFamilyClass( family );
		this.ID = nextID++;
		this.position = position;
		this.room = null;
	}
	
	/** @return The position in the hallway of the apod */
	public int getX( ) {
		return position.x;
	}
	
	/** @return The position in the room of the apod */
	public int getY( ) {
		return position.y;
	}

	/** 
	 * Sets the new position of the Apod
	 * 
	 *  @param newpos The new position
	 */
	protected void setPosition( final Coord2D newpos ) {
		this.position = newpos;
	}
	
	/** @return True iff the apod is in the hallway */
	public boolean inHallway( ) {
		return room == null;
	}
	
	/** @return True iff the Apod is in the right room */
	public boolean isHome( ) { 
		return room != null && room.checkHome( this );
	}
	
	/**
	 * Sets the room the apod is currently in
	 * 
	 * @param room The room to put it in
	 */
	public void setRoom( final BurrowRoom room ) {
		this.room = room;
	}
	
	/** @return The current room the apod is in, null if it is in the hallway */
	public BurrowRoom getRoom( ) { return room; }
	
	/** @return True if this apod is in a room and blocked by another */
	public boolean isBlockedInRoom( ) {
		if( room == null ) throw new RuntimeException( "The apod is not in any room: " + toString( ) );
		return !room.canLeave( this );
	}
	
	
	/**
	 * Enters a room, if possible
	 * 
	 * @param newroom The room to enter
	 * @param checkmove False to disable sanity checks (undo / initial)
	 */
	public void enterRoom( final BurrowRoom newroom, final boolean checkmove ) {
		if( checkmove ) {
			if( room != null ) throw new RuntimeException( "The apod is already in another room: " + room.toString( ) );
			if( !newroom.canEnter( this ) ) throw new RuntimeException( "The apod " + toString( ) + " cannot enter room " + newroom );
		}

		// reuse set code after performing sanity checks
		room = newroom;
		room.add( this );	
	}
	
	/**
	 * Leaves the room it is currently in
	 */
	public void leaveRoom( ) {
		if( room == null ) throw new RuntimeException( "The apod is not in any room: " + toString( ) );
	
		room.remove( this );
		room = null;
	}
	
	/**
	 * Computes the energy consumption for moving dx and dy positions
	 * 
	 * @param dx The horizontal movement
	 * @param dy The vertical movement
	 * @return The apod's energy consumption for such a move
	 */
	public long getEnergyConsumption( final int dx, final int dy ) {
		return (dx + dy) * family.energy;
	}
	
	/** @return The apod description */
	@Override
	public String toString( ) {
		return family.toString( ) + ID;
	}	
	
	/** @return The apod description with more info */
	public String toLongString( ) {
		return family.toString( ) + ID + ": " + position + (room != null ? " (R" + room.family + ")" : "");
	}
	
	/**
	 * Compares two Apods, equal only if their IDs are equal
	 * 
	 * @param obj The other object to compare against
	 */
	@Override
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Apod) ) return false;
		return ((Apod)obj).ID == ID;
	}
	
	/** @return The unique ID of the Apod as its hash code */
	@Override
	public int hashCode( ) {
		return ID;
	}
}
