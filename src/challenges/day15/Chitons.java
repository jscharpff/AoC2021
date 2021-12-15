package challenges.day15;

import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import util.geometry.Coord2D;
import util.grid.CoordGrid;
import util.uniquepriorityqueue.QElement;
import util.uniquepriorityqueue.UniquePriorityQueue;

public class Chitons {
	/** The CoordGrid representing the Chitons risk level */
	protected final CoordGrid<Integer> chitons;
	
	/** Tuple that holds a coordinate and its distance from a starting coord */
	private class CoordDist implements Comparable<CoordDist> {
		protected int x;
		protected int y;
		protected int dist;
		
		protected CoordDist( final int x, final int y, final int dist ) {
			this.x = x;
			this.y = y;
			this.dist = dist;
		}
		
		@Override
		public int compareTo( CoordDist o ) {
			return dist - o.dist;
		}
		
		@Override
		public String toString( ) {
			return "(" + x + "," + y + ")=" + dist;
		}
	}
	
	/**
	 * Creates a new grid of Chitons
	 * 
	 * @param input The list of strings describing the risk level per Chiton in
	 * a grid 
	 */
	public Chitons( final List<String> input ) {
		chitons = CoordGrid.fromDigitGrid( input );
	}
	
	/**
	 * Replicate this grid x and y times, increasing the risk level by riskinc
	 * whenever x or y increases
	 * 
	 * @param x The number of horizontal replicas
	 * @param y The number of vertical replicas
	 * @param riskinc The increase of risk level per copy
	 * @return The new Chitons grid
	 */
	public void replicate( final int x, final int y, final int riskinc ) {
		// the size of the initial grid
		final Coord2D size = chitons.size( );
		final Set<Coord2D> keyset = new HashSet<>( chitons.getKeys( ) );
		
		for( final Coord2D c : keyset ) {
			final int risk = chitons.get( c );
			for( int i = 0; i <= x; i++ ) {
				for( int j = 0; j <= y; j++ ) {
					if( i == 0 && j == 0 ) continue;
					final int newrisk = (risk + (i + j) * riskinc);
					chitons.add( c.move( size.x * i,  size.y * j ), newrisk > 9 ? newrisk - 9 : newrisk );
				}
			}
		}
	}
	
	/**
	 * Find the route from start to end through the Chiton grid that minimises
	 * the risk level
	 * 
	 * @param start The start position
	 * @param end The ending position
	 * @return The minimal risk level
	 */
	public int findMinimalRiskRoute( final Coord2D start, final Coord2D end ) {
		// perform a weighted-BFS search to determine distance matrix from start
		// to all other coordinates
		final int[][] R = getRiskMatrixUniqueQueue( start );
		return R[ end.x ][ end.y ];
	}

	/**
	 * Runs an A*-like algorithm to determine the risk distance to all other
	 * coordinates in the Chitons grid. Used a simple priority queue and a
	 * pruning technique to keep the queue size manageable.
	 * 
	 * @param start The coordinate to start the distance computation from
	 * @return The risk (distance) matric to all other cooridnates in the grid
	 */
	protected int[][] getRiskMatrixPruning( final Coord2D start ) {
		// initialise a risk matrix for fast look-ups
		final int W = chitons.size( ).x;
		final int H = chitons.size( ).y;
		final int[][] R = new int[ W ][ H ];
		for( int x = 0; x < W; x++ )
			for( int y = 0; y < H; y++ )
				R[x][y] = chitons.get( new Coord2D( x, y ) );		
		
		// initialise a distance matrix
		final int[][] D = new int[ W ][ H ];
		for( int x = 0; x < W; x++ )
			for( int y = 0; y < H; y++ )
				D[x][y] = -1;

		// the set of nodes to explore next, in order of their risk level
		final int MAX_QUEUE_SIZE = W * H / 10;
		Queue<CoordDist> Q = new PriorityQueue<>( MAX_QUEUE_SIZE + 1 );
		Q.add( new CoordDist( start.x, start.y, 0 ) );
		while( !Q.isEmpty( ) ) {
			// if the queue becomes too big, we prune it
			if( Q.size( ) > MAX_QUEUE_SIZE ) {
				// prune list of coords to visit, remove coordinates that either are
				// already in the Q with a smaller distance or those nodes for which
				// we already have a better distance in the risk matrix
				final Queue<CoordDist> newQ = new PriorityQueue<>( MAX_QUEUE_SIZE + 1 );
				final Set<Coord2D> coords = new HashSet<>( );
				for( final CoordDist elem : Q ) {
					final Coord2D c = new Coord2D( elem.x, elem.y );
					if( coords.contains( c ) ) continue;
					if( D[elem.x][elem.y] != -1 && D[elem.x][elem.y] < elem.dist ) continue;
					
					coords.add( c );
					newQ.offer( elem );
				}
				Q = newQ;
			}
		
			// get next coord with minimal risk level and explore further from there
			// if this improves the current best distance for the coord
			final CoordDist cd = Q.poll( );
			final int dist = D[cd.x][cd.y];
			if( dist != -1 && dist < cd.dist ) continue;
			D[cd.x][cd.y] = cd.dist;
			
			
			// explore its neighbours
			for( final Coord2D n : new Coord2D( cd.x, cd.y ).getNeighbours( false ) ) {
				final int newx = n.x;
				final int newy = n.y;
				
				// keep it within the grid!
				if( newx < 0 || newx >= W ) continue;
				if( newy < 0 || newy >= H ) continue;
				
				// add to queue for exploration with their risk distance
				final int newdist = cd.dist + R[newx][newy];
				final int currdist = D[newx][newy];
				if( currdist == -1 || currdist > newdist ) {
					Q.offer( new CoordDist( newx, newy, newdist ) );
				}
			}
		}

		return D;
	}

	/**
	 * Runs an A*-like algorithm to determine the risk distance to all other
	 * coordinates in the Chitons grid. This version is based on a custom
	 * UniquePriorityQueue that only allows coordinates to be queued once.
	 * 
	 * @param start The coordinate to start the distance computation from
	 * @return The risk (distance) matric to all other cooridnates in the grid
	 */
	protected int[][] getRiskMatrixUniqueQueue( final Coord2D start ) {
		// initialise a risk matrix for fast look-ups
		final int W = chitons.size( ).x;
		final int H = chitons.size( ).y;
		final int[][] R = new int[ W ][ H ];
		for( int x = 0; x < W; x++ )
			for( int y = 0; y < H; y++ )
				R[x][y] = chitons.get( new Coord2D( x, y ) );		
		
		// initialise a distance matrix
		final int[][] D = new int[ W ][ H ];
		for( int x = 0; x < W; x++ )
			for( int y = 0; y < H; y++ )
				D[x][y] = -1;

		// the set of nodes to explore next, in order of their risk level
		final UniquePriorityQueue<Coord2D, Integer> Q = new UniquePriorityQueue<>( );
		Q.insert( new Coord2D( start.x, start.y), 0 );
		while( Q.size( ) > 0 ) {		
			// get next coord with minimal risk level and explore further from there
			// if this improves the current best distance for the coord
			final QElement<Coord2D, Integer> elem = Q.poll( );
			final CoordDist cd = new CoordDist( elem.key.x, elem.key.y, elem.value );
			final int dist = D[cd.x][cd.y];
			if( dist != -1 && dist < cd.dist ) continue;
			D[cd.x][cd.y] = cd.dist;
			
			
			// explore its neighbours
			for( final Coord2D n : new Coord2D( cd.x, cd.y ).getNeighbours( false ) ) {
				final int newx = n.x;
				final int newy = n.y;
				
				// keep it within the grid!
				if( newx < 0 || newx >= W ) continue;
				if( newy < 0 || newy >= H ) continue;
				
				// add to queue for exploration with their risk distance
				final int newdist = cd.dist + R[newx][newy];
				final int currdist = D[newx][newy];
				if( currdist == -1 || currdist > newdist ) {
					Q.insert( new Coord2D( newx, newy), newdist );
				}
			}
		}

		return D;
	}

	/**
	 * @return The Chitons grid in a string
	 */
	@Override
	public String toString( ) {
		return chitons.toString( );
	}
}
