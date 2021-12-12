package util.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Container for a node based graph
 * 
 * @author Joris
 */
public class Graph {
	/** The node set, indexed by node label */
	protected final Map<String, Node> nodes;
	
	/** The edge set */
	protected final List<Edge> edges;
	
	/**
	 * Creates a new, empty Graph of nodes
	 */
	public Graph( ) {
		nodes = new HashMap<String, Node>( );
		edges = new ArrayList<Edge>( );
	}
	
	/**
	 * Adds a node to the graph, gets the exiting one if already in the graph
	 * 
	 * @param nodelabel The node to add by label
	 * @return The node
	 */
	public Node addNode( final String nodelabel ) {
		Node node = this.getNode( nodelabel );
		if( node == null ) node = new Node( nodelabel );
		
		nodes.put( node.getLabel( ), node );
		return node;
	}

	/**
	 * Retrieves a node by its label
	 * 
	 * @param label The node label
	 * @return The node or null if not found in the graph
	 */
	public Node getNode( final String label ) {
		return nodes.get( label );
	}
	
	/**
	 * @return The set of nodes
	 */
	public Set<Node> getNodes( ) {
		return (Set<Node>) nodes.values( );
	}
	
	/**
	 * @return The number of nodes in the graph
	 */
	public int size() { return nodes.size( ); }
	
	/**
	 * @return List of edges
	 */
	public List<Edge> getEdges( ) {
		return edges;
	}
	
	/**
	 * Adds an edge to the graph that connects the two specified nodes, if not
	 * already present in the edge list
	 * 
	 * @param edge The edge to add
	 * @return False if the edge was already in the list
	 */
	public boolean addEdge( final Edge edge ) {
		if( edges.contains( edge ) ) return false;
		
		// add the edge and its nodes
		edges.add( edge );
		
		// make sure nodes also know of the arc
		edge.from.addEdge( edge );
		edge.to.addEdge( edge );
		return true;
	}
	
	/**
	 * Constructs a graph from a list of Strings that describe its edges
	 * 
	 * @param input The list of edges as strings
	 * @return The graph
	 */
	public static Graph fromStringList( final List<String> input ) {
		final Graph g = new Graph( );

		// parse the edges
		for( final String s : input ) {
			final Matcher m = Pattern.compile( "^(\\w+)\\s*-\\s*(\\w+)$" ).matcher( s.trim( ) );
			if( !m.find( ) ) throw new IllegalArgumentException( "Edge not formatted properly: " + s );

			// first make sure we know the nodes of the edge
			final Node A = g.addNode( m.group( 1 ) );
			final Node B = g.addNode( m.group( 2 ) );
			
			// and add the edge
			g.addEdge( new Edge( A, B, 0 ) );
		}
		
		return g;
	}
	
	/**
	 * @return String representation of the tree
	 */
	public String toString( ) {
		return "[Nodes " + size( ) + "]\n" + nodes.toString( ) + 
				"\n\n[Edges " + edges.size( ) + "]\n" + edges.toString( );
	}
}
