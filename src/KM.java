import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class KM {

	public static void main(String[] args) {
		long startTime=System.nanoTime();

		if (args.length == 0) {
			System.out.println("no input parameter provided\n");
			return;
		}
		Graph graph = InputParserUtility.ParseInput(args[0]);
		graph.printGraphEdges();
		graph.printNodeCover();
		
		System.out.println("----------------");
		
		GenerateMatching(graph);

		long endTime=System.nanoTime();
		long totalTime=endTime-startTime;

		System.out.println("Total time taken for KM is " + totalTime + " ns");
	}
	
	public static void GenerateMatching(Graph graph)
	{
		// Create an empty matching
		Set<Edge> tightEdges = new HashSet<Edge>();
		Set<Edge> matching = new HashSet<Edge>();
		Set<Node> S_set = new HashSet<Node>();
		Set<Node> T_set = new HashSet<Node>();
		
		// Assign initial labels,  max(x,y) to each x, 0 for all y,
		// 		and assign tight edges
		InitializeLabels(graph, tightEdges);
		
		boolean getNewX = true;
		int x_index = 0;
		// Loop through algorithm while the matching is not a perfect match
		while (matching.size() < graph.numNodes()) {
			
			// Step 2. - Pick free vertex in X and assign to S
			Node u = null;
			if (getNewX) {
				graph.getNodeX(x_index++);
				S_set.add(u);
				getNewX = false;
			}

			
			// Step 3. - Test if Neighbors == T
			Set<Node> Neigh = GetNeighbors(S_set, tightEdges, graph);
			if (IsSetEqual(Neigh, T_set)) {
				// Adjust the labels
				// ...
				
			} else {
			// Step 4. 
				// Remove T from Neighbors
				Neigh.removeAll(T_set);
				// select a y in Neigh
				Node y = null;
				Iterator<Node> iter = Neigh.iterator();
				if (iter.hasNext()) {
					y = iter.next();
				} else {
					System.out.println("ERROR - no nodes found in the Neighbors - T set");					
					return;
				}
				
				if (!IsNodeMatched(y, matching)) {
					// if y is free, u -> y is an augmenting path
					// Augment u -> y
					getNewX = true;
				} else {
					// else y is matched to some z
					// find what node y is matched with.
					T_set.add(y);
					for (Edge edge : matching) {
//				 		Add z to S  and y to T
						if (edge.GetY_Index() == y.GetIndex()) {
							S_set.add(graph.getNodeX(edge.GetX_Index()));
							break;
				// 		Re-test for Neighbors, go to step 3
						}
					}								
				}
			}		
		}		
	}
	
	public static void AdjustLabels(Set<Node> S_set, Set<Node> T_set,
			Set<Edge> tightSet, Graph graph) {
		// Calculate the minimum alpha for all edges from S to y not in T, 
		// Add that edge to the tight edges, and subtract S node labels
		// by alpha, add alpha to T set, 
		
		
	}
	
	public static void AugmentPath(Node a, Node b, Set<Edge> tightEdges, Set<Edge> matching) {
		// Augment the path from a to b (neither should be in the matching)
		if (IsNodeMatched(a, matching) || IsNodeMatched(b, matching)) {
			System.out.println("ERROR - Attempting to augment between two nodes and one is already matched...");
			return;
		}
		
		// Now we can do the augmenting ...
		
	}
	
	// This will determine if a node is matched to an edge in the matching
	public static boolean IsNodeMatched(Node node, Set<Edge> matching) {
		// Since nodes have an X or Y(ness), this function will determine
		// if the node is matched to any edge in the matching
		int index = node.GetIndex();
		for (Edge edge : matching) {
			if (node.GetIsXNode()) {
				if (edge.GetX_Index() == index) return true;
			} else {
				if (edge.GetY_Index() == index) return true;
			}
		}		
		return false;
	}
	
	public static boolean IsSetEqual(Set<Node> s1, Set<Node> s2) {
		
		if (s1.size() != s2.size()) { return false; }
		if (s1.containsAll(s2)) { return true; }
		return false;
	}
	
	public static Set<Node> GetNeighbors(Set<Node> S, 
			Set<Edge> tightEdges, Graph graph) {
	
		Set<Node> neigh = new HashSet<Node>();
		// Test all nodes in S
		for (Node n : S) {
			int index = n.GetIndex();
			// If a node in S has an outgoing edge in the tight
			//  	edges, add that node to the neighbors
			for (Edge e : tightEdges) {
				if (e.GetX_Index() == index) {
					// Add the y node to the neighbors set
					neigh.add(graph.getNodeY(e.GetY_Index()));
				}
			}			
		}
		// return the set of neighbors
		return neigh;
	}
	
	public static void InitializeLabels(Graph graph, Set<Edge> tightEdges) {
		
		for (int i = 0; i < graph.numNodes(); ++i) {
			graph.getNodeY(i).SetLabel(0);
			// Find the maximum edge weight incident to each X node, 
			int maxWeight = 0;
			for (int j = 0; j < graph.numNodes(); ++j) {
				int w = graph.getEdge(i, j).GetWeight();
				if (w > maxWeight) { maxWeight = w; }
			}
			for (int j = 0; j < graph.numNodes(); ++j) {
				if (graph.getEdge(i, j).GetWeight() == maxWeight) {
					tightEdges.add(graph.getEdge(i, j));
				}
			}
			graph.getNodeX(i).SetLabel(maxWeight);			
		}
		System.out.println("Initial labels are:");
		graph.printNodeCover();
		System.out.println("Tight Edges:");
		PrintEdges(tightEdges, 0);
	}

	// Added function to print edges in a set (almost identical to Graph function)
	public static void PrintEdges(Set<Edge> edges, int offset) {
		int weight = edges.stream() //
				.map(e -> e.GetWeight()) //
				.reduce(0, Integer::sum);
		System.out.println(weight);
		
		edges.stream() //
		.sorted((edgeA, edgeB) -> edgeA.GetX_Index() - edgeB.GetX_Index()) // sort based on the X node
		.forEach(edge -> System.out.println("("+ (edge.GetX_Index() + offset) +"," + (edge.GetY_Index() + offset) +")"));
		
	}

}
