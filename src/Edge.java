

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
	public int edgeWeight; 
	
	
	/**
	 * Two nodes at the ends of this edge
	 * They are annotated as Left and Right as if viewing them 
	 * in a BiPartite graph that has 2 columns of nodes
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
	
	public void SetEdgeData(Node nodex, Node nodey, int weight) 
	{
		nodeX = nodex;
		nodeY = nodey;
		edgeWeight = weight;
	}
	
	
	/** 
	 * Method for displaying the node index at each end of this edge.
	 */
	public void PrintEdge() {
		System.out.println("("+nodeX.nodeIndex+","+nodeY.nodeIndex+")");
	}
}
