package challenges.day23.organiser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import challenges.day23.apods.Apod;
import challenges.day23.apods.Burrow;
import challenges.day23.apods.BurrowRoom;
import challenges.day23.organiser.heuristics.energy.EnergyHeuristic;
import challenges.day23.organiser.heuristics.move.MoveHeuristic;

public class Organiser {
	/** The burrow to organise */
	protected final Burrow burrow;
	
	/** Pre-computed array of illegal spaces, due to the door blocking it */
	private int[] doors;

	/** Verbose mode */
	protected boolean verbose;
	
	/** The organiser statistics */
	protected OrganiserStats stats;
	
	/** The heuristic for next move selection */
	protected MoveHeuristic Hmove;
	
	/** The (ADMISSABLE) heuristic for early solution discarding */
	protected EnergyHeuristic Henergy;

	/**
	 * Creates a new organiser
	 * 
	 * @param burrow The burrow to organise
	 */
	public Organiser( final Burrow burrow ) {
		this.burrow = burrow;
		this.verbose = false;
		
		// compute illegal positions in hallway
		final Collection<BurrowRoom> rooms = burrow.getRooms( );
		doors = new int[ rooms.size( ) ];
		int i = 0;
		for( final BurrowRoom r : rooms )
			doors[ i++ ] = r.getDoorX( );

	}
	
	/**
	 * Sets/unsets verbose mode
	 * 
	 * @param v The new value for the verbose flag
	 */
	public void setVerbose( final boolean v ) {
		this.verbose = v;
	}
	
	/**
	 * Sets the heuristic for next move ordering
	 * 
	 * @param H The new heuristic
	 */
	public void setMoveHeuristic( final MoveHeuristic H ) {
		this.Hmove = H;
	}
	
	/**
	 * Sets the heuristic for minimal energy consumption estimation
	 * 
	 * @param H The new heuristic
	 */
	public void setEnergyHeuristic( final EnergyHeuristic H ) {
		this.Henergy = H;
	}
	
	/**
	 * Organises all the Apods in the burrow so that they are in their correct
	 * rooms, i.e. the one assigned to their family
	 * 
	 * @return The minimal energy required to move all the Apods to their correct
	 * rooms
	 */
	public long organise( ) {
		// perform a branch-and-bound evaluation of all possible moves
		final Solution solution = new Solution( );
		final Stack<ApodMove> moves = new Stack<>( );
		stats = new OrganiserStats( );

		if( verbose ) {
			System.out.println( "---[ Organiser ]---" );
			System.out.println( "Starting organiser run!" );
		}
		
		try { 
		
			// run the organise algorithm
			stats.starttime = System.currentTimeMillis( );
			organise( solution, moves, 0 );
			stats.endtime = System.currentTimeMillis( );
		
		} catch( Exception e ) {
			if( !verbose ) throw e;
			
			// print current situation
			System.err.println( "[BURROW STATE]");
			System.err.println( burrow.toLongString( ) );			
			
			// print current move stack			
			System.err.println( "[MOVE STACK]");
			Collections.reverse( moves );
			while( moves.size( ) > 0 ) System.err.println( "> " + moves.pop( ) );
			System.err.println(  );
			throw e;
		}
		
		if( verbose ) {
			System.out.println( "Solution found: " + solution.leastenergy );
			System.out.println(  );
			System.out.println( stats );
			System.out.println(  );
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
	private void organise( Solution currbest, final Stack<ApodMove> moves, final long currenergycost ) {
		stats.stateCount++;
		
		// stop pursuing the current solution if its energy costs exceeds the
		// currently best minimum value
		// (fail-safe, should not hit this due to action selection)
		if( currenergycost >= currbest.leastenergy ) return;
		
		// do a quick computation to see if, even when ignoring all rules, the
		// current configuration will lead to a lower energy consumption. I.e. this
		// uses an admissable heuristic to discard bad solutions early on
		if( Henergy != null ) {
			final long minestimate = Henergy.estimateMinimalEnergy( burrow );
			if( currenergycost + minestimate >= currbest.leastenergy  )	{
				stats.discardedHEnergy++;
				return;
			}
		}
		
		// try all available next moves until we reach the desired configuration
		// but only consider those moves 
		final List<ApodMove> nextmoves = generateNextMoves( currbest.leastenergy - currenergycost );
		if( nextmoves.size( ) == 0 ) {
			// check if we have a solution now, if not simply return. This path is
			// infeasible
			for( final Apod apod : burrow.getApods( ) ) 
				if( !apod.isHome( ) ) {
					stats.deadends++;
					return;
				}
			
			// new best solution!
			currbest.set( moves, currenergycost );
			if( verbose ) System.out.println( ">> New solution found: " + currbest );
			return;
		}
				
		// apply heuristic to guide move selection
		if( Hmove != null ) Hmove.apply( burrow, nextmoves );
		
		// and perform the moves in their heuristic order!
		for( final ApodMove move : nextmoves ) {
			moves.add( move );
			burrow.execute( move, false );
			stats.executed++;

			organise( currbest, moves, currenergycost + move.energy );

			burrow.execute( move, true );
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
		for( final Apod apod : burrow.getApods( ) ) {
			if( apod.isHome( ) ) continue;
			
			// is it currently in the hallway and wanting to move into a room?
			if( apod.inHallway( ) ) {
				// yes, get the room the apod wants to move into and check if it is possible
				final BurrowRoom room = burrow.getApodRoom( apod );
				final long roomenergy = checkRoomMove( apod, room );				
				if( roomenergy != -1 && roomenergy < maxcost ) moves.add( new ApodMove( apod, room, roomenergy ) );
			} else {
				// no, get all the available hallway positions
				for( int x = 0; x < burrow.hallsize; x++ ) {
					final long hallenergy = checkHallMove( apod, x );
					if( hallenergy != -1 && hallenergy < maxcost ) moves.add( new ApodMove( apod, x, hallenergy ) );
				}
			}
		}
		
		return moves;
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
		if( x < 0 || x > burrow.hallsize ) return -1;
		
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
		if( !canMove( apod.getX( ), room.getDoorX( ) ) ) return -1;
		
		// the move is allowed, compute the energy cost of such a move
		return apod.getEnergyConsumption( Math.abs( room.getDoorX( ) - apod.getX( ) ), room.getFirstAvailable( ) + 1 );	}
	
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
			if( burrow.getHallway( x ) != null ) return false;
		
		// and finally check target position
		return burrow.getHallway( to ) == null;
	}

	/**
	 * Organiser run stats
	 */
	private class OrganiserStats {
		/** The run start time */
		private long starttime = -1;
		
		/** The run end time */
		private long endtime = -1;
		
		/** The number of burrow states evaluated */
		private long stateCount = 0;
		
		/** The number of dead ends encountered */
		private long deadends = 0;
		
		/** The number of solutions discarded due to the energy heuristic */
		public long discardedHEnergy = 0;
		
		/** The number of moves executed */
		public long executed;
		
		private String perc( final long x, final long y ) {
			return " (" + String.format( "%1.2f", (double)x * 100.0 / (double)y ) + "%)";
		}
		
		/**
		 * @return The run statistics
		 */
		@Override
		public String toString( ) {
			String res = "";
			res += "---[ Run statistics ]---";
			res += "\nRun time (s): " + (endtime == -1 ? "(running)" : (endtime-starttime) );
			res += "\nStates evaluated: " + stateCount;
			res += "\nMoves executed: " + executed;
			res += "\nDead ends: " + deadends;
			res += "\nSolutions discarded based upon heuristic: " + discardedHEnergy + perc( discardedHEnergy, stateCount ); 
			return res ;
		}
	}
}
