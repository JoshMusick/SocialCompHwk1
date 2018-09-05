
public class Graph {

	/**
	 * Constructor for a bi-partite graph
	 *  
	 * @param numNodes Number of nodes in each group, this number will effectively be doubled
	 */
	public Graph(int numNodes, int [][] weights) {
		numberNodes = numNodes;
		
		if (numberNodes < 0) return;
		
		edges = new Edge[numNodes][numNodes];
		nodex = new Node[numNodes];
		nodey = new Node[numNodes];
		
		for (int i = 0; i < numNodes; ++i) {
			nodex[i].nodeIndex = i;
			nodey[i].nodeIndex = i;
			for (int j = 0; j < numNodes; ++j) {
				edges[i][j].SetEdgeData(nodex[i], nodey[j], weights[i][j]);
			}
		}
	}
	
	/*
	 * This method will print a simple matrix of edge pairs and their weight,
	 * This is a test to ensure the matrix of edge weights has been loaded properly
	 */
	public void PrintGraphEdges() {
		for (int i = 0; i < numberNodes; ++i) {
			for (int j = 0; j < numberNodes; ++j) {
				System.out.println("("+ i +"," + j +") - " + edges[i][j].edgeWeight);
			}
		}
	}

	/*
	 * This method will print the node cover (label) for each index of X and Y
	 */
	public void PrintNodeCover() {
		System.out.println("Index \t\t X Label \t\t Y Label");
		for (int i = 0; i < numberNodes; ++i) {
			System.out.println((i + 1) + " \t\t " + nodex[i].nodeLabel + "\t\t" + nodey[i].nodeLabel);		
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
