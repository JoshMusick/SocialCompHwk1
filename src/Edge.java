

/** 
 * A basic class for a graph Edge.  This edge will contain a weight
 * and the two nodes it attaches to.  
 * 
 * @author Joshua Musick
 *
 */
public class Edge {

	/**
	 * The weight of this Edge
	 */
	private int edgeWeight; 
	
	// Accessor to retrieve edge weight
	public int GetWeight() { return edgeWeight; }
	
	/**
	 * Two nodes at the ends of this edge
	 * They are annotated as X and Y as in a Bi-partite graph 
	 */
	public Node nodeX;
	public Node nodeY;
	
	/** 
	 * Basic constructor for an edge
	 */
	public Edge()
	{
		nodeX = null;
		nodeY = null;
		edgeWeight = 0;
	}
	
	/**
	 * Simple constructor for an edge in the graph
	 * 
	 * @param node_x Node on the X grouping of a bi-partite graph
	 * @param node_y Node on the Y grouping of a bi-partite graph
	 * @param weight Edge weight 
	 */
	public Edge(Node node_x, Node node_y, int weight)
	{
		nodeX = node_x;
		nodeY = node_y;
		edgeWeight = weight;
	}
	
	/** 
	 * Method for displaying the node index at each end of this edge.
	 */
	public void PrintEdge() {
		System.out.println("("+nodeX.GetIndex() + ","+nodeY.GetIndex() +")");
	}
}
