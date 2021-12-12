package util.graph;

/**
 * Edge between nodes A to B
 * 
 * @author Joris
 */
public class Edge {
	/** The starting node */
	protected final Node from;
	
	/** The end node */
	protected final Node to;
	
	/** The weight of the arc */
	protected int weight;
	
	/**
	 * Constructs a new edge between A and B width weight w
	 * 
	 * @param A The starting node
	 * @param B The ending node
	 * @param weight The edge weight
	 */
	public Edge( final Node A, final Node B, final int weight ) {
		this.from = A;
		this.to = B;
		this.weight = weight;
	}
	
	/** @return The start node */
	public Node getFrom( ) { return from; }
	
	/** @return The end node */
	public Node getTo( ) { return to; }
	
	/**
	 * @param node The node to start from
	 * @return The node at the other end of the edge
	 */
	public Node getOther( final Node node ) {
		if( to.equals( node ) ) return from;
		if( from.equals( node ) ) return to;
		
		throw new RuntimeException( "Node " + node + " not contained by edge " + this );
	}
	
	/** @return Both nodes in the edge */
	public Node[] getNodes( ) { return new Node[] { from, to }; }

	/** @return The edge weight */
	public int getWeight( ) { return weight; }

	/**
	 * @return String representation of the edge
	 */
	public String toString( ) {
		if( weight > 0 ) return from.toString( ) + "-(" + weight + ")-" + to.toString( );
		
		return from.toString( ) + "-" + to.toString( );
	}
	
	/**
	 * Check if it equals another edge
	 * 
	 * @param obj The object representing the other edge
	 * @return True iff the edge contains the same nodes and weight
	 */
	public boolean equals( Object obj ) {
		if( obj == null || !(obj instanceof Edge) ) return false;
		final Edge edge = (Edge)obj;
		
		return edge.to.equals( to ) && edge.from.equals( from ) && edge.weight == weight;
	}
	
	/**
	 * @return The hash code of its String representation
	 */
	@Override
	public int hashCode( ) {
		return toString( ).hashCode( );
	}
}
