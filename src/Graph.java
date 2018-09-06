import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Graph {

	private List<Node> nodeListX = new ArrayList<Node>(); //initialize to empty set
	private List<Node> nodeListY = new ArrayList<Node>(); //initialize to empty set
	private Set<Edge> edgeSet = new HashSet<Edge>(); //initialize to empty set

	/**
	 * Constructor for a bi-partite graph
	 *  
	 * @param numNodes Number of nodes in each group, this number will effectively be doubled
	 */

	public void addNodeX(Node node) {
		nodeListX.add(node.GetIndex(), node);
	}

	public void addNodeY(Node node) {
		nodeListY.add(node.GetIndex(), node);
	}

	public Node getNodeX(int index) {
		return nodeListX.get(index);
	}

	public Node getNodeY(int index) {
		return nodeListY.get(index);
	}

	public void addEdge(Edge edge) {
		edgeSet.add(edge);
	}

	/**
	 * @return the nodeListX
	 */
	public List<Node> getNodeListX() {
		return nodeListX;
	}

	/**
	 * @param nodeListX the nodeListX to set
	 */
	public void setNodeListX(List<Node> nodeListX) {
		this.nodeListX = nodeListX;
	}

	/**
	 * @return the nodeListY
	 */
	public List<Node> getNodeListY() {
		return nodeListY;
	}

	/**
	 * @param nodeListY the nodeListY to set
	 */
	public void setNodeListY(List<Node> nodeListY) {
		this.nodeListY = nodeListY;
	}

	/*
	 * This method will print the edge matches and their combined weight
	 */
	public void printSolution() {
		int weight = edgeSet.stream() //
				.map(e -> e.GetWeight()) //
				.reduce(0, Integer::sum);
		System.out.println(weight);

		edgeSet.stream() //
		.sorted((edgeA, edgeB) -> edgeA.nodeX.GetIndex() - edgeB.nodeX.GetIndex()) // sort based on the X node
		.forEach(edge -> System.out.println("("+ (edge.nodeX.GetIndex() + 1) +"," + (edge.nodeY.GetIndex() + 1) +")"));
	}

	/*
	 * This method will print a simple matrix of edge pairs and their weight,
	 * This is a test to ensure the matrix of edge weights has been loaded properly
	 */
	public void printGraphEdges() {
		for (int i = 0; i < nodeListX.size(); i++) {
			for (int j = 0; j < nodeListY.size(); j++) {
				Edge edge = getEdge(i, j);
				System.out.println("("+ i +"," + j +") - " + edge.GetWeight());
			}
		}
	}

	/*
	 * This method will print the node cover (label) for each index of X and Y
	 */
	public void printNodeCover() {
		System.out.println("Index \tX Label \tY Label");
		for (int i = 0; i < nodeListX.size(); i++) {
			System.out.println((i + 1) + " \t " + nodeListX.get(i).GetLabel() + "\t\t" + nodeListY.get(i).GetLabel());
		}		
	}
	
	// Returns the number of nodes in each of the two sets of nodes
	public int numNodes() { return nodeListX.size(); }
	
	/**
	 * Convenience method for retrieving an edge from the graph, based upon the input
	 *  indices of the nodes which the edge connects
	 * @param x Index of the x node array
	 * @param y Index of the y node array
	 * @return Edge which corresponds to the given x and y indices
	 */
	public Edge getEdge(int x, int y) {
		if ((x < 0) || (y < 0)) { throw new IndexOutOfBoundsException(); }
		if ((x >= numNodes()) || (y >= numNodes())) { throw new IndexOutOfBoundsException(); }
		
		return edgeSet.stream() //
		.filter(edge -> edge.nodeX.GetIndex() == x) //
		.filter(edge -> edge.nodeY.GetIndex() == y) //
		.findFirst() //
		.orElse(null);
	}

	public void removeEdge(Edge edge) {
		edgeSet.remove(edge);
	}

}
