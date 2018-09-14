

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
	 * Node index at each ends of this edge
	 * They are annotated as X and Y as in a Bi-partite graph 
	 */	
	private int nodeXIndex;
	private int nodeYIndex;
	
	/** 
	 * Basic constructor for an edge
	 */
	public Edge()
	{
		nodeXIndex = 0;
		nodeYIndex = 0;
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
		nodeXIndex = node_x.GetIndex();
		nodeYIndex = node_y.GetIndex();
		edgeWeight = weight;
	}
	
	public int GetX_Index() {
		return nodeXIndex;
	}
	
	public int GetY_Index() {
		return nodeYIndex;
	}
	
	// Returns true if the given node is one of the two that this edge connects
	public boolean GetConnectsNodes(Node node) {
		if (node.GetIsXNode()) {
			return node.GetIndex() == nodeXIndex;
		}
		return node.GetIndex() == nodeYIndex; 
	}
	
	/** 
	 * Method for displaying the node index at each end of this edge.
	 */
	public void PrintEdge() {
		System.out.println("("+nodeXIndex + ","+nodeYIndex +")");
	}
}
