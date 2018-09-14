import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class KM {
	
	// Create an empty sets to be used in the KM Algorithm
	private Set<Edge> tightEdges = new HashSet<Edge>();
	private Set<Edge> matching = new HashSet<Edge>();
	private Set<Node> S_set = new HashSet<Node>();
	private Set<Node> T_set = new HashSet<Node>();	

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
		
		KM km = new KM();
		
		km.GenerateMatching(graph);
		
		long endTime=System.nanoTime();
		long totalTime=endTime-startTime;

		System.out.println("Total time taken for KM is " + totalTime + " ns");
	}
	
	public void GenerateMatching(Graph graph)
	{		
		// Assign initial labels,  max(x,y) to each x, 0 for all y,
		// 		and assign tight edges
		InitializeLabels(graph);
		
		boolean getNewX = true;
		int x_index = 0;
		// Loop through algorithm while the matching is not a perfect match
		while (matching.size() < graph.numNodes()) {
			
			// Step 2. - Pick free vertex in X and assign to S
			Node u = null;
			if (getNewX) {
				u = graph.getNodeX(x_index++);
				S_set.add(u);
				getNewX = false;
			}
			
			// Step 3. - Test if Neighbors == T
			Set<Node> Neigh = GetNeighbors(graph);
			if (IsSetEqual(Neigh, T_set)) {
				// Adjust the labels
				AdjustLabels(graph);
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
					AugmentPath(u, y, graph);
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
				// 		Re-test for Neighbors (getNewX = false), go to step 3
						}
					}								
				}
			}		
		}		
	}
	
	public void AdjustLabels(Graph graph) {
		// Calculate the minimum alpha for all edges from S to y not in T, 
		// Add that edge to the tight edges, and subtract S node labels
		// by alpha, add alpha to T set, 
		
		int minAlpha = Integer.MAX_VALUE;
		
		int xIdx = -1;
		int yIdx = -1;
		
		// Get nodes in Y but not in T_set
		HashSet<Node> availNodes = new HashSet<Node>();
		List<Node> yNodes = graph.getNodeListY();
		for (Node n : yNodes) {
			if (!T_set.contains(n) ) {
				availNodes.add(n);
			}
		}
		
		// Now compute the alpha for each edge
		for (Node s : S_set) {
			for (Node y: availNodes) {
				int alpha = s.GetLabel() + y.GetLabel() - graph.getEdge(s.GetIndex(), y.GetIndex()).GetWeight();
				if (alpha < minAlpha) {
					minAlpha = alpha;
					xIdx = s.GetIndex();
					yIdx = y.GetIndex();
				}
			}			
		}
		
		if ((xIdx == -1) || (yIdx == -1)) {
			System.out.println("ERROR -- No new edges found in AdjustLabels!!");
			return;
		}
		
		for (Node s : S_set) {
			s.SetLabel(s.GetLabel() - minAlpha);
		}
		for (Node t : T_set) {
			t.SetLabel(t.GetLabel() + minAlpha);
		}
		
		// Finally add edge from xIdx to yIdx to tightEdges
		tightEdges.add(graph.getEdge(xIdx, yIdx));
		System.out.println("----------");
		System.out.println("After Updating Labels, and adding edge from ("+xIdx+", "+yIdx+")");
		PrintEdges(tightEdges, 0);
		
	}
	
	public void AugmentPath(Node a, Node b, Graph graph) {
		// Augment the path from a to b (neither should be in the matching)
		if (IsNodeMatched(a, matching) || IsNodeMatched(b, matching)) {
			System.out.println("ERROR - Attempting to augment between two nodes and one is already matched...");
			return;
		}
		
		// Now we can do the augmenting ...
		
		// Simple check, is edge (x,y) in the tight edges but not in the matching?
		Edge e = graph.getEdge(a.GetIndex(), b.GetIndex());
		
		if (tightEdges.contains(e)) {
			if (!matching.contains(e)) {
				matching.add(e);
				return;
			}
		}
		
		
	}
	
	// Recursive function for finding the augmenting path
	// Returns true if path was found, false if no path was found 
	public boolean FindPath(Node a, Node b, Graph graph, Set<Node> visited) {
		// node b is the desired end-node
		// node a is the starting node, (current node being visited
		// visited is the set of nodes that have been considered in this search path
		// 	to prevent infinite loops
		// the size of visited will also be used to determine if an edge should 
		// 	be selected from current matching or tight edges.
		// 	If |visited| is odd, edge can't be in matching
		//  If |visited| is even, edge must be in matching
		
		if (visited.contains(a)) return false;
		visited.add(a);
		
		boolean getMatchedEdge = (visited.size() % 2 == 0);
		if (getMatchedEdge) {
			System.out.println("Looking for a matched edge, because visited is even with " + visited.size() +" nodes");
		}
		
		if (getMatchedEdge) {
			
		} else {
			
		}
		
		
		return false;
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
	
	public Set<Node> GetNeighbors(Graph graph) {
	
		Set<Node> neigh = new HashSet<Node>();
		// Test all nodes in S
		for (Node n : S_set) {
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
	
	public void InitializeLabels(Graph graph) {
		
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
