package util.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Graph Node
 * 
 * @author Joris
 */
public class Node {
	/** The node label */
	protected final String label;
	
	/** Connected edges */
	protected final Set<Edge> edges;
	
	/**
	 * Creates a new node with the specified label
	 * 
	 * @param label The node label
	 */
	public Node( final String label ) {
		this.label = label;
		
		this.edges = new HashSet<>( );
	}
	
	/** @return The label */
	public String getLabel( ) { return label; }
	
	/**
	 * @return String description of the node
	 */
	public String toString( ) {
		return "(" + getLabel( ) + ")";
	}

	/**
	 * Hash code needed for hash set, simply return that of its label
	 */
	@Override
	public int hashCode( ) {
		return label.hashCode( );
	}
	
	/**
	 * Adds an edge to the node edge list
	 * 
	 * @param edge The edge
	 * @return False if the edge was already known
	 */
	public boolean addEdge( final Edge edge ) {
		assert equals( edge.to ) || equals( edge.from) : "Node " + toString( ) + " is not the incoming node of arc " + edge.toString( );

		// prevent duplicate arcs
		if( edges.contains( edge ) ) return false;
		
		edges.add( edge );
		return true;
	}
	
	/**
	 * @return All the edges connected to this node
	 */
	public Collection<Edge> getEdges( ) {	return edges; }
	
	/**
	 * @return The set of all neighbouring nodes connected via at least one edge
	 */
	public Collection<Node> getNeighbours( ) {
		final Set<Node> neighbours = new HashSet<>( );
		for( final Edge e : edges ) {
			neighbours.add( e.getOther( this ) );
		}
		return neighbours;
	}
	
	/**
	 * Check if it equals another node
	 * 
	 * @param obj The object representing the other node
	 * @return True iff the labels are equal
	 */
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Node) ) return false;
		final Node node = (Node)obj;
		
		return node.label.equals( label );
	}
}
