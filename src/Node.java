
/**
 * A basic class for a graph Node.  This node is intended to represent
 * a node in a bi-partite graph.  Therefore it will have a unique index 
 * number.
 * @author Joshua Musick
 *
 */

public class Node {

	// Label is for storing the label weight in KM
	public int nodeLabel;
	public int nodeIndex;
	
	public Node() {
		nodeIndex = 0;
		nodeLabel = 0;
	}
	
	public Node(int index) {
		nodeIndex = index;
		nodeLabel = 0;
	}
	
}
