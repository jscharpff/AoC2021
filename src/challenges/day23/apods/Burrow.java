package challenges.day23.apods;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import challenges.day23.organiser.ApodMove;
import util.geometry.Coord2D;

/**
 * Class that represents a Burrow in which Apods live, one type per room
 * 
 * @author Joris
 */
public class Burrow {
	/** The hallway size */
	public final int hallsize;
	
	/** the room size (depth) */
	public final int roomsize;
	
	/** The position of Apods in the hallway, null for empty */
	private final Apod[] hallway;
	
	/** The set of apods in the burrow */
	private final Set<Apod> apods;
	
	/** The rooms per family and their current occupation */
	private final Map<ApodFamily, BurrowRoom> rooms;
	
	/**
	 * Creates a new burrow with the given map of apods and their starting
	 * positions
	 * 
	 * @param hallsize The size of the hallway in the burrow 
	 * @param rooms The map of rooms per family
	 * @param apods The map of apods and their current position
	 */
	private Burrow( final int hallsize, final int roomsize, final Map<ApodFamily, BurrowRoom> rooms, final Set<Apod> apods ) {
		this.hallsize = hallsize;
		this.roomsize = roomsize;
		this.rooms = new HashMap<>( rooms );
		this.apods = new HashSet<>( apods );

		// create array to contain positions of apods in the hallway
		this.hallway = new Apod[ hallsize ];
		for( final Apod a : apods ) {
			if( !a.inHallway( ) ) continue;
			this.hallway[ a.getX( ) ] = a;
		}
	}
	
	/** @return The set of apods in the burrow */
	public Set<Apod> getApods( ) {
		return apods;
	}
	
	/** @return The list of rooms */
	public Collection<BurrowRoom> getRooms( ) {
		return rooms.values( );
	}
	
	/** @return The apod at the hallway position, null for no apod */
	public Apod getHallway( final int x ) {
		if( x < 0 || x >= hallsize ) throw new IndexOutOfBoundsException( x );
		return hallway[ x ];
	}
	
	/**
	 * Returns the room that belongs to the apod's family
	 * 
	 * @param apod The apod to return the room for
	 * @return The room that is meant for this apod's family
	 */
	public BurrowRoom getApodRoom( final Apod apod ) {
		return rooms.get( apod.family );
	}
	
	/**
	 * Execute the given move
	 * 
	 * @param move The move to execute
	 * @param undo True to undo the action
	 */
	public void execute( final ApodMove move, final boolean undo ) {
		final Apod apod = move.apod;
		final ApodMove m = !undo ? move : move.undo( ); 
				
		// check the type of move
		if( m.toHallway( ) ) {
			// move to a hallway position
			hallway[ m.target.x ] = apod;
			apod.leaveRoom( );
			apod.setPosition( m.target );
		} else {
			// move it into a room
			hallway[ m.origin.x ] = null;
			apod.enterRoom( m.getRoom( ), !undo );
			apod.setPosition( m.target );
		}
	}
	
	
	/**
	 * Outputs the current Burrow as a visual image like:
	 * 
	 * #############
	 * #...........#
	 * ###B#C#B#D###
	 *   #A#D#C#A#
	 *   #########
	 *   
	 * @return The visual representation of the output
	 */
	@Override
	public String toString( ) {
		String res = "";
		// upper wall
		for( int i = 0 ; i < hallsize + 2; i++ ) res += "#";
		
		// hallway
		res += "\n#";
		for( int i = 0 ; i < hallsize; i++ ) {
			res += (hallway[i] != null ? hallway[i].family.classification : ".");
		}
		res += "#";		
		
		// rooms
		for( int y = 0; y <= roomsize; y++ ) {
			final char blank = y == 0 ? '#' : ' ';
			// fill with blanks first
			final char[] todraw = new char[ hallsize ];
			for( int i = 0; i < todraw.length; i++ )
				todraw[i] = blank;

			// then fill in room layout for the given room depth
			for( final BurrowRoom room : rooms.values( ) ) {
				final int x = room.door;
				todraw[ x - 1 ] = '#';
				todraw[ x ] = (y < roomsize ? '.' : '#');
				todraw[ x + 1 ] = '#';
				
				// check for apods in the room
				if( y < roomsize ) {
					final Apod a = room.getApod( y );
					if( a != null ) todraw[ x ] = a.family.classification;
				}
			}
			
			// and compose a string from it
			res += "\n" + blank;
			for( int i = 0; i < todraw.length; i++ )
				res += todraw[i];
			res += blank;
		}
		
		return res;
	}
	
	/**
	 * @return A longer descriptive string of the current burrow
	 */
	public String toLongString( ) {
		String res = "";
		for( final Apod a : apods )
			res += a.toLongString( ) + ", ";
		res = res.substring( 0, res.length( ) - 2 ) +  "\n" + rooms.values( ) + "\n";
		res += toString( );
		return res;
	}
	
	/**
	 * Creates a new Burrow from a list of strings that visually describe its
	 * inner layout like this:
	 * 
	 * #############
	 * #...........#
	 * ###B#C#B#D###
	 *   #A#D#C#A#
	 *   #########
	 * 
	 * @param input The list of strings
	 * @return The burrow
	 */
	public static Burrow fromStringList( final List<String> input ) {	
		// parse apods and their starting positions from the strings
		final Map<ApodFamily, BurrowRoom> rooms = new HashMap<>( );
		final Set<Apod> apods = new HashSet<>( );
		
		// read the size of the burrow from the first line
		final int hallsize = input.get( 0 ).length( ) - 2;
		
		// derive room depth from remaining lines
		final int roomsize = input.size( ) - 3;
		
		// get positions of rooms with respect to the doorways
		final Map<Integer, BurrowRoom> pos2room = new HashMap<Integer, BurrowRoom>( );
		final String roomdoors = input.get( 2 ); 
		char currtype = 'A';
		for( int i = 0; i < roomdoors.length( ); i++ )
			if( roomdoors.charAt( i ) != '#' ) {
				final ApodFamily fam = ApodFamily.fromFamilyClass( currtype++ );
				final BurrowRoom room = new BurrowRoom( fam, roomsize, i - 1 );
				rooms.put( fam, room );
				pos2room.put( room.door, room );
			}
		
		// now parse the apods themselves, bottom to top to preserve room ordering
		for( int y = roomsize - 1; y >= 0 ; y-- ) {
			final String apodpositions = input.get( 2 + y ); 			
			for( int x = 0; x < apodpositions.length( ); x++ ) {
				final char apodclass = apodpositions.charAt( x );
				if( apodclass == '#' || apodclass == ' ' ) continue;
				
				// create apod and add it to the right room
				final Apod apod = new Apod( apodclass, new Coord2D( x - 1, y + 1 ) );
				apods.add( apod );
				apod.enterRoom( pos2room.get( apod.getX( ) ), false );
			}
		}
				
		return new Burrow( hallsize, roomsize, rooms, apods );
	}
}
