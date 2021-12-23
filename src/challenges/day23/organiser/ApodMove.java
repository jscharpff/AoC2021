package challenges.day23.organiser;

import challenges.day23.apods.Apod;
import challenges.day23.apods.BurrowRoom;
import util.geometry.Coord2D;

/**
 * Container to store a single available move
 * 
 * @author Joris
 */
public class ApodMove {
	/** The apod that is to perform the move */
	public final Apod apod;
	
	/** The original position it came from */
	public final Coord2D origin;
	
	/** The target position of the move */
	public final Coord2D target;
	
	/** The origin or target room, if this is respectively a hall or room move */
	private final BurrowRoom room;
	
	/** The energy consumption of the move */
	public final long energy;
	
	/**
	 * Creates a new Room move for the Apod
	 * 
	 * @param apod The apod that is to move into the room
	 * @param room The room it moves into
	 * @param energy The energy it will cost the Apod to get there
	 */
	protected ApodMove( final Apod apod, final BurrowRoom room, final long energy ) {
		this( apod, new Coord2D( apod.getX( ), apod.getY( ) ), new Coord2D( room.getDoorX( ), room.getFirstAvailable( ) + 1 ), room, energy );
	}

	/**
	 * Creates a new Hallway move for the Apod
	 * 
	 * @param apod The apod that is to move into the room
	 * @param hallpos The hallway position it wants to move to
	 * @param energy The energy it will cost the Apod to get there
	 */
	protected ApodMove( final Apod apod, final int hallpos, final long energy ) {
		this( apod, new Coord2D( apod.getX( ), apod.getY( ) ), new Coord2D( hallpos, 0 ), apod.getRoom( ), energy );
	}
	
	/**
	 * Creates a new move, this is an internal constructor that is also used for
	 * undo moves
	 * 
	 * @param apod The apod that is moving
	 * @param origin The original position where it starts moving from
	 * @param target The target to which it moves
	 * @param room The room it want to go to (Room move) or came from (Hallway move)
	 * @param energy The energy consumption required for this move
	 */
	private ApodMove( final Apod apod, final Coord2D origin, final Coord2D target, final BurrowRoom room, final long energy ) {
		this.apod = apod;
		this.origin = origin;
		this.target = target;
		this.energy = energy;
		this.room = room;		
	}
	
	/**
	 * Creates a undo move from this one
	 * 
	 * @return The ApodMove that will have the opposite effect of the move
	 */
	public ApodMove undo( ) {
		return new ApodMove( apod, target, origin, room, -energy );
	}
	
	/** @return True if this is a move to the hallway */
	public boolean toHallway( ) { return target.y == 0; }
	
	/** @return The target room */
	public BurrowRoom getRoom( ) {
		if( toHallway( ) ) throw new RuntimeException( "Tried to get target room from a hallway move" );
		return room;
	}
	
	/** @return The string describing the move */
	@Override
	public String toString( ) {
		if( toHallway( ) )
			return apod.toString( ) + " moves from " + origin + " to hallway position " + target.x;
		else
			return apod.toString( ) + " moves from" + origin + " into room " + room; 
	}
}