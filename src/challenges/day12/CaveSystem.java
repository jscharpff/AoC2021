package challenges.day12;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import util.graph.Graph;
import util.graph.Node;

public class CaveSystem {
	/** The graph that represents the case system */
	protected final Graph graph;
	
	/**
	 * Creates a new cave system from a list of strings describing the paths
	 * 
	 * @param input List of strings that, one per line, describe the possible
	 *   paths in the caves
	 */
	public CaveSystem( final List<String> input ) {
		graph = Graph.fromStringList( input );
	}
	
	/** @return Starting node of the cave system */
	public Node getStart( ) { return graph.getNode( "start" ); }
	
	/** @return Ending node of cave system */
	public Node getEnd( ) { return graph.getNode( "end" ); }
	
	/**
	 * Checks whether the node represents a large cave, i.e. one that is
	 * interesting enough to visit more than once
	 * 
	 * @param node The node to check
	 * @return True iff the node represents a large cave
	 */
	public static boolean isLargeCave( final Node node ) {
		return node.getLabel( ).matches( "[A-Z]+" );
	}

	/**
	 * Checks whether the node represents a small cave, i.e. one that can be
	 * visited only once
	 * 
	 * @param node The node to check
	 * @return True iff the node represents a small cave
	 */
	public static boolean isSmallCave( final Node node ) {
		if( isLargeCave( node ) ) return false;
		return !node.getLabel( ).equals( "start" ) && !node.getLabel( ).equals( "end" );
		}

	/**
	 * Finds all unique paths from node A to B that visit small caves not more
	 * than once
	 * 
	 * @param start The starting node
	 * @param end The ending node
	 * @param allowtwice True to allow one small cave to be visited twice
	 * @return The set of all possible unique paths from start to end
	 */
	public Collection<Path> findAllRoutes( final Node start, final Node end, final boolean allowtwice ) {
		// create initial path from starting node
		final Path path = new Path( start );
		
		//  explore all neighbours recursively and return all options
		final Collection<Path> paths = new ArrayList<>( );
		findAllRoutes( paths, path, end, allowtwice );
		return paths;
	}
	
	/**
	 * Actual algorithm that finds unique paths by recursively extending the
	 * current path by one of the neighbouring nodes, if eligible.
	 * 
	 * @param paths The set of unique paths constructed so far
	 * @param path The current path that is being explored
	 * @param end The target node to reach
	 * @param allowtwice True to allow ONE small cave to be explored twice, false
	 *   for exploring small caves only once
	 */
	private void findAllRoutes( final Collection<Path> paths, final Path path, final Node end, final boolean allowtwice ) {
		// get the last node in the path
		final Node node = path.getEnd( );
		
		// are we at the end node now? if so, store the unique path and stop
		// exploring this path further
		if( node.equals( end ) ) {
			paths.add( path );
			return;
		}
		
		// no, try to extend the path by one of the unvisited nodes
		for( final Node n : node.getNeighbours( ) ) {
			// can we visit this node again?
			if( !path.contains( n ) || isLargeCave( n ) || (isSmallCave( n ) && allowtwice && !path.hasSmallTwice( )) ) {			
				// yes, try and explore from here
				findAllRoutes( paths, path.extend( n ), end, allowtwice );			
			}
		}
	}
	
	/**
	 * @return The string of the graph that represents the cave system
	 */
	@Override
	public String toString( ) {
		return graph.toString( );
	}
	
}
