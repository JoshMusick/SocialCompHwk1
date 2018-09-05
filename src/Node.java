
/**
 * A basic class for a graph Node.  This node is intended to represent
 * a node in a bi-partite graph.  Therefore it will have a unique index 
 * number.
 * @author Joshua Musick
 *
 */

public class Node {

	// Label is for storing the label weight in KM
	private int nodeLabel;
	private int nodeIndex;
	
	/**
	 * Access methods
	 */
	public int GetLabel() { return nodeLabel; }
	public void SetLabel(int val) { nodeLabel = val; }
	
	public int GetIndex() { return nodeIndex; }
	
	/**
	 * Default Constructor
	 */
	public Node() {
		nodeIndex = 0;
		nodeLabel = 0;
	}
	
	/**
	 * Constructor used by the Graph Class
	 * @param index index of the node in a bi-partite graph
	 */
	public Node(int index) {
		nodeIndex = index;
		nodeLabel = 0;
	}
	
}
