package challenges.day12;

import java.util.ArrayList;
import java.util.List;

import util.graph.Node;

/**
 * Container that holds a path of successive nodes
 * 
 * @author Joris
 */
public class Path {
	/** The nodes in this path, from start to end */
	protected List<Node> nodes;
	
	/** True iff the path has a small node that is visited twice */
	protected final boolean visitedtwice;
	
	/**
	 * Creates a new path from the given start node
	 * 
	 * @param start The starting node of the path
	 */
	public Path( final Node start ) {
		this.nodes = new ArrayList<>( 1 );
		nodes.add( start );
		visitedtwice = false;
	}
	
	/**
	 * Copies an existing path and extends it by one node
	 */
	private Path( final Path p, final Node newnode ) {
		this.nodes = new ArrayList<>( p.size() + 1 );
		this.nodes.addAll( p.nodes );
		boolean twice = p.visitedtwice;
		
		// add the new node and check if this leads to a small node being visited twice 
		if( !twice ) {
			if( CaveSystem.isSmallCave( newnode ) && nodes.contains( newnode ) ) twice = true;
		}
		this.visitedtwice = twice;
		this.nodes.add( newnode );
	}
	
	/**
	 * @return The last node in the path
	 */
	public Node getEnd( ) {
		return nodes.get( nodes.size( ) - 1 );
	}
	
	/**
	 * Copies the path and appends the node
	 * 
	 * @param node The node to add
	 */
	public Path extend( final Node node ) {
		return new Path( this, node );
	}
	
	/**
	 * Checks if the path has a small cave that is visited twice
	 * 
	 * @return True iff the path has one such cave node
	 */
	public boolean hasSmallTwice( ) {
		return visitedtwice;
	}
	
	/**
	 * Checks if the path already contains the node
	 * 
	 * @param node The node to check
	 * @return True iff the node is already in the node list of this path
	 */
	public boolean contains( final Node node ) {
		return nodes.contains( node );
	}
	
	/** @return The number of nodes in the path */
	public int size( ) {
		return nodes.size( );
	}
}
