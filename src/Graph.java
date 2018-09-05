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

	public Edge findEdge(int xIndex, int yIndex) {
		return edgeSet.stream() //
		.filter(edge -> edge.nodeX.GetIndex() == xIndex) //
		.filter(edge -> edge.nodeY.GetIndex() == yIndex) //
		.findFirst() //
		.orElse(null);
	}
	/*
	 * This method will print a simple matrix of edge pairs and their weight,
	 * This is a test to ensure the matrix of edge weights has been loaded properly
	 */
	public void PrintGraphEdges() {
		for (int i = 0; i < nodeListX.size(); i++) {
			for (int j = 0; j < nodeListY.size(); j++) {
				Edge edge = findEdge(i, j);
				System.out.println("("+ i +"," + j +") - " + edge.GetWeight());
			}
		}
	}

	/*
	 * This method will print the node cover (label) for each index of X and Y
	 */
	public void PrintNodeCover() {
		System.out.println("Index \tX Label \tY Label");
		for (int i = 0; i < nodeListX.size(); i++) {
			System.out.println((i + 1) + " \t " + nodeListX.get(i).GetLabel() + "\t\t" + nodeListY.get(i).GetLabel());
		}		
	}
	
	// Returns the number of nodes in each of the two sets of nodes
	public int NumNodes() { return numberNodes; }
	
	/**
	 * Convenience method for retrieving an edge from the graph, based upon the input
	 *  indices of the nodes which the edge connects
	 * @param x Index of the x node array
	 * @param y Index of the y node array
	 * @return Edge which corresponds to the given x and y indices
	 */
	public Edge GetEdge(int x, int y) {
		// Should this throw an exception instead?
		if ((x < 0) || (y < 0)) { return null; }
		if ((x >= NumNodes()) || (y >= NumNodes())) { return null; }
		return edges[x][y];
	}
	
	/*
	 * Number of nodes on each side of a bipartite graph, this is effectively doubled
	 */
	protected int numberNodes;
	
	/* 
	 * Array of nodes on each side of the bi-partite graph
	 */
	protected Node[] nodex;
	protected Node[] nodey;
	
	/* 
	 * Array of edges between two sets of nodes, the first index will be from the nodex array, 
	 * the second index is from the nodey array
	 */
	protected Edge [][] edges;




}
