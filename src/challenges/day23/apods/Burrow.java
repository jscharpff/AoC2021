package challenges.day23.apods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import challenges.day23.apods.moveheuristic.MoveHeuristic;
import util.geometry.Coord2D;

/**
 * Class that represents a Burrow in which Apods live, one type per room
 * 
 * @author Joris
 */
public class Burrow {
	/** The hallway size */
	protected final int hallsize;
	
	/** the room size (depth) */
	protected final int roomsize;
	
	/** The position of Apods in the hallway, null for empty */
	private final Apod[] hallway;
	
	/** The set of apods in the burrow */
	private final Set<Apod> apods;
	
	/** The rooms per family and their current occupation */
	private final Map<ApodFamily, BurrowRoom> rooms;
	
	/** Pre-computed array of illegal spaces, due to the door blocking it */
	private int[] doors;
	
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
		
		// compute illegal positions in hallway
		doors = new int[ rooms.size( ) ];
		int i = 0;
		for( final BurrowRoom r : rooms.values( ) )
			doors[ i++ ] = r.door;
	}
	
	
	/**
	 * Organises all the Apods in the burrow so that they are in their correct
	 * rooms, i.e. the one assigned to their family
	 * 
	 * @param heuristic The heuristic to use to determine the next best move
	 * @return The minimal energy required to move all the Apods to their correct
	 * rooms
	 */
	public long organise( final MoveHeuristic heuristic ) {
		// perform a branch-and-bound evaluation of all possible moves
		final Solution solution = new Solution( );
		final Stack<ApodMove> moves = new Stack<>( );
		try { 
		
			// run the organise algorithm
			organise( heuristic, solution, moves, 0 );
		
		} catch( Exception e ) {
			// print current situation
			for( final Apod a : apods)
				System.err.print( a.toLongString( ) + ", " );
			System.err.println( "\n" + rooms.values( ) );
			System.err.println( this );
			
			
			// print current move stack			
			System.err.println( "[MOVE STACK]");
			Collections.reverse( moves );
			while( moves.size( ) > 0 ) System.err.println( "> " + moves.pop( ) );
			System.err.println(  );
			throw e;
		}
		return solution.leastenergy;
	}
	
	
	/**
	 * The actual algorithm to determine the minimal energy consumption to arrange
	 * the Apods. Uses a branch-and-bound style to abandon paths that exceed the
	 * cost of the current best solution. 
	 * 
	 * @param H The heuristic to use for next move selection
	 * @param currbest The current known best solution
	 * @param moves The moves that we have performed until now
	 * @param currenergycost The energy cost of performing those moves
	 */	
	private void organise( final MoveHeuristic H, Solution currbest, final Stack<ApodMove> moves, final long currenergycost ) {
		// abandon the current solution?
		if( currenergycost >= currbest.leastenergy ) return;
		
		// try all available next moves until we reach the desired configuration
		// stop pursuing the current solution if its energy costs exceeds the
		// currently best minimum value
		final List<ApodMove> nextmoves = generateNextMoves( currbest.leastenergy - currenergycost );
		if( nextmoves.size( ) == 0 ) {
			// check if we have a solution now, if not simply return. This path is
			// infeasible
			for( final Apod apod : apods ) 
				if( !apod.isHome( ) ) return;
			
			// new best solution!
			currbest.set( moves, currenergycost );
			System.err.println( "New solution found: " + currbest );
			return;
		}
				
		// apply heuristic to guide move selection
		H.apply( nextmoves );
		
		// and perform the moves in their heuristic order!
		for( final ApodMove move : nextmoves ) {
			moves.add( move );
			execute( move, false );

			organise( H, currbest, moves, currenergycost + move.energy );

			execute( move, true );
			moves.pop( );
		}
	}
	
	
	/**
	 * Generates all possible moves from the current state of the Burrow
	 * 
	 * @param maxcost Prevent adding moves that will lead to non-optimal
	 *   solutions anyway by restricting the cost it may incur
	 * @return The list of all possible moves, empty if no more move is possible
	 *   from the current burrow state
	 */
	private List<ApodMove> generateNextMoves( final long maxcost ) {
		final List<ApodMove> moves = new ArrayList<>( );
		
		// add moves for all apods that are not already in their room
		for( final Apod apod : apods ) {
			if( apod.isHome( ) ) continue;
			
			// is it currently in the hallway and wanting to move into a room?
			if( apod.inHallway( ) ) {
				// yes, get the room the apod wants to move into and check if it is possible
				final BurrowRoom room = rooms.get( apod.family );
				final long roomenergy = checkRoomMove( apod, room );				
				if( roomenergy != -1 && roomenergy < maxcost ) moves.add( new ApodMove( apod, room, roomenergy ) );
			} else {
				// no, get all the available hallway positions
				for( int x = 0; x < hallsize; x++ ) {
					final long hallenergy = checkHallMove( apod, x );
					if( hallenergy != -1 && hallenergy < maxcost ) moves.add( new ApodMove( apod, x, hallenergy ) );
				}
			}
		}
		
		return moves;
	}
	
	/**
	 * Execute the given move
	 * 
	 * @param move The move to execute
	 * @param undo True to undo the action
	 */
	private void execute( final ApodMove move, final boolean undo ) {
		final Apod apod = move.apod;
		final ApodMove m = !undo ? move : move.undo( ); 
				
		// check the type of move
		if( m.toHallway( ) ) {
			// move to a hallway position
			hallway[ m.getTarget( ).x ] = apod;
			apod.leaveRoom( );
			apod.setPosition( m.getTarget( ) );
		} else {
			// move it into a room
			hallway[ m.getOrigin( ).x ] = null;
			apod.enterRoom( m.getRoom( ), !undo );
			apod.setPosition( m.getTarget( ) );
		}
	}
	
	
	/**
	 * Checks if the apod can move to the given hallway position and returns the
	 * energy it will cost it to move. If not possible, -1 is returned
	 * 
	 * @param apod The apod to move
	 * @param x The hallway x coordinate to move into
	 * @return Energy required to move the Apod to the position, -1 for invalid
	 *   target positions. Target positions are invalid because they are either
	 *   illegal or unreachable due to other apods blocking the way  
	 */
	private long checkHallMove( final Apod apod, final int x ) {
		// cannot move the Apod to the hallway if it is already in there
		// or the target position is not even within the hallway
		if( apod.inHallway( ) ) return -1;
		if( x < 0 || x > hallsize ) return -1;
		
		// cannot move the Apod if it is in a room and blocked by another Apod
		if( apod.isBlockedInRoom( ) ) return -1;
		
		// cannot move to a hallway position occupied by a door
		for( int i = 0; i < doors.length; i++ )
			if( x == doors[i] ) return -1;
		
		// cannot move if the hallway path is blocked
		if( !canMove( apod.getX( ), x ) ) return -1;
		
		// the move is allowed, compute the energy cost of such a move
		return apod.getEnergyConsumption( Math.abs( x - apod.getX( ) ), apod.getY( ) ); 
	}
	
	/**
	 * Checks if the apod can move into its family room and returns the energy
	 * it will cost it to move. If not possible, -1 is returned
	 * 
	 * @param apod The apod to check if it can go home finally
	 * @return The energy cost of going home, -1 if it is not possible
	 */
	private long checkRoomMove( final Apod apod, final BurrowRoom room ) {
		// can only move into the room if it i in the hallway now
		if( !apod.inHallway( ) ) return -1;
				
		// check if the room is available
		if( !room.canEnter( apod ) ) return -1;
		
		// check if the path to the room is not blocked
		if( !canMove( apod.getX( ), room.door ) ) return -1;
		
		// the move is allowed, compute the energy cost of such a move
		return apod.getEnergyConsumption( Math.abs( room.door - apod.getX( ) ), room.getFirstAvailable( ) + 1 );	}
	
	/**
	 * Checks if there is a free path in the hallway between from and to for any
	 * apod to walk along
	 * 
	 * @param from The starting position
	 * @param to The target position 
	 * @return True iff there is nothing blocking the path
	 */
	private boolean canMove( final int from, final int to ) {		
		// cannot move if any of the hallway positions along the way is blocked
		final int dx = to > from ? 1 : -1;
		for( int x = from + dx; x != to; x += dx )
			if( hallway[x] != null ) return false;
		
		// and finally check target position
		return hallway[to] == null;
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

	/**
	 * Container for a (current) solution to organise the apods optimally
	 */
	private class Solution {
		/** The sequential list of moves to perform */
		private List<ApodMove> bestmoves;
		
		/** The total energy consumption of this solution */
		private long leastenergy;
		
		/**
		 * Creates a new empty solution
		 */
		public Solution( ) {
			this.bestmoves = null;
			this.leastenergy = Long.MAX_VALUE;
		}
		
		/**
		 * Updates the Solution to the best one found so far
		 * 
		 * @param moves The list of moves to perform to get all Apods organised
		 * @param energy The total energy consumption required to organise them
		 */
		public void set( final Stack<ApodMove> moves, final long energy ) {
			if( energy >= leastenergy ) throw new RuntimeException( "Invalid update of solution (best: " + leastenergy + ", new best: " + energy + ")" );
			
			this.bestmoves = new ArrayList<>( moves );
			this.leastenergy = energy;
		}
		
		/** @return The solution description */
		@Override
		public String toString( ) {
			if( bestmoves == null ) return "(No solution yet)";
			
			return "[E: " + leastenergy + ", M: " + bestmoves.size( ) + "]";   
		}
	}	
}
