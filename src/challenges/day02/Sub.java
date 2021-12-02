package challenges.day02;

import util.geometry.Coord2D;

/**
 * Santa's submarine
 * 
 * @author Joris
 *
 */
public class Sub {
	/** The position of the submarine */
	protected Coord2D pos;
	
	/** The aim of the submarine */
	protected int aim;
	
	/** Movement directions */
	protected enum DIR {
		FORWARD("forward"), UP("up"), DOWN("down");
		
		/** The string representation of the direction */
		protected final String label;
		private DIR( final String l ) { this.label = l; }

		/**
		 * Creates a direction from string value
		 * @param s The label value
		 */
		public static DIR fromString( final String s ) {
			for( DIR d : DIR.values( ) )
				if( d.label.equals( s ) ) return d;
			throw new RuntimeException( "No such enum value " + s );
		}
	}
	
	/**
	 * Creates a new submarine at position (0,0) and aim 0
	 */
	public Sub( ) {
		this( new Coord2D( 0, 0 ), 0 );
	}
	
	/**
	 * Creates a new submarine at the given start position
	 * 
	 * @param pos The starting position
	 * @patam aim The initial aim of the sub
	 */
	public Sub( final Coord2D pos, final int aim ) {
		this.pos = pos;
		this.aim = aim;
	}
	
	/**
	 * Move by command and distance
	 * 
	 * @param dir The movement direction
	 * @param dist The distance to move
	 */
	public void move( final DIR dir, int dist ) {
		switch( dir ) {
			case FORWARD: pos = pos.move( dist, 0 ); break;
			case UP: pos = pos.move( 0, -dist ); break;
			case DOWN: pos = pos.move( 0, dist ); break;
		}
	}
	
	/**
	 * Move by command and distance, now using the "aimed" movement strategy
	 * 
	 * @param dir The movement direction
	 * @param dist The distance to move
	 */
	public void moveAimed( final DIR dir, int dist ) {
		switch( dir ) {
			case FORWARD: pos = pos.move( dist, aim * dist ); break;
			case UP: aim -= dist; break;
			case DOWN: aim += dist; break;
		}
	}
	
	/**
	 * @return The current position of the submarine
	 */
	public Coord2D getPosition( ) {
		return pos;
	}
}
