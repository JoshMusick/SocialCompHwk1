
/**
 * A basic class for a graph Node.  This node is intended to represent
 * a node in a bi-partite graph.  Therefore it will have a unique index 
 * number.
 * @author Joshua Musick
 *
 */

public class Node {

	public long nodeLabel;
	public long nodeIndex;
	
	public Node(long index) {
		nodeIndex = index;
		nodeLabel = 0;
	}
	
}
