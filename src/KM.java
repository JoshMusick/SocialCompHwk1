import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class KM {
	
	// flag to enable debugging
	public boolean debug = false;
	
	// Create an empty sets to be used in the KM Algorithm
	private Set<Edge> tightEdges = new HashSet<Edge>();
	private Set<Edge> matching = new HashSet<Edge>();
	private Set<Edge> nonMatching = new HashSet<Edge>();
	private Set<Node> S_set = new HashSet<Node>();
	private Set<Node> T_set = new HashSet<Node>();

	public static void main(String[] args) {
		long startTime=System.nanoTime();

		if (args.length == 0) {
			System.out.println("no input parameter provided\n");
			return;
		}
		KM km = new KM();
//		km.debug = true;
		Graph graph = InputParserUtility.ParseInput(args[0]);
		
		if (km.debug) {
			graph.printGraphEdges();
			graph.printNodeCover();
			System.out.println("----------------");
		}	
				
		km.GenerateMatching(graph);
		
		long endTime=System.nanoTime();
		long totalTime=endTime-startTime;

		km.PrintMatching();
		System.out.println("Total time taken for KM is " + totalTime + " ns");
	}
	
	private void AddMatchingEdge(Edge e) {
		matching.add(e);
		nonMatching.remove(e);
	}
	
	private void RemoveMatchingEdge(Edge e) {
		matching.remove(e);
		nonMatching.add(e);
	}
	
	private void AddTightEdge(Edge e) {
		tightEdges.add(e);
		nonMatching.add(e);
	}
	
	// Calculates the Neighbor nodes of set S
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
	
	public void Update(Graph graph, Node u) {
		
		dp("Calculating Neighbors for Step 3 or 4");
				
		// Calculate the neighbors of S
		Set<Node> Neigh = GetNeighbors(graph);
		
		printSandT();
		dp("Neighbors are...");
		printNodes(Neigh);

		if (IsSetEqual(Neigh, T_set)) {
			dp("Starting Step 3 ---------------------");
			dp("T set and N(S) are equal, adjusting labels");
			// Adjust the labels
			AdjustLabels(graph);
		} 
		Step4Augment(graph, u);	
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
		AddTightEdge(graph.getEdge(xIdx, yIdx));
		if (debug) {
			System.out.println("----------");
			System.out.println("Below are the tight edges after Updating Labels, and adding edge from ("+xIdx+", "+yIdx+")");
			PrintEdges(tightEdges, 0);
		}
	}
	
	public void Step4Augment(Graph graph, Node u) {
		
		dp("Starting Step 4 ---------------------");
		
		Set<Node> neighbors = GetNeighbors(graph);
		dp("T set and N(S) are NOT equal, selecting a y and augmenting");
		// Remove T from Neighbors
		neighbors.removeAll(T_set);
		// select a y in Neigh
		Node y = null;
		Iterator<Node> iter = neighbors.iterator();
		if (iter.hasNext()) {
			y = iter.next();
		} else {
			System.out.println("ERROR - no nodes found in the Neighbors - T set");					
			return;
		}
		
		if (!IsNodeMatched(y, matching)) {
			dp("y is free (" + y.GetIndex() + " -> augmenting...");
			// if y is free, u -> y is an augmenting path
			// Augment u -> y
			AugmentPath(u, y, graph);
			return;
		} else {
			// else y is matched to some z
			// find what node y is matched with.
			dp("y is matched to some z");
			T_set.add(y);
			for (Edge edge : matching) {
				//	Add z to S  and y to T
				if (edge.GetY_Index() == y.GetIndex()) {
					Node z = graph.getNodeX(edge.GetX_Index());
					S_set.add(z);
					dp("(z,y) is ("+z.GetIndex()+","+y.GetIndex()+")");
					if (debug) {
						PrintEdges(matching, 0);
					}
					printSandT();
					break;
				}
			}
			dp("Going to step 3 from step 4...");
			//  go to step 3
			Update(graph, u);
		}		
	}
	
	public void GenerateMatching(Graph graph)
	{		
		// Assign initial labels,  max(x,y) to each x, 0 for all y,
		// 		and assign tight edges
		InitializeLabels(graph);
		
		// Loop through algorithm while the matching is not a perfect match
		while (matching.size() < graph.numNodes()) {
			
			dp("Starting the while loop");
			// Step 2. - Pick free vertex in X and assign to S
			Node u = InitializeStep2(graph);
			dp("Selecting x node at index "+u.GetIndex()+" for inclusion in set X");
			
			// Step 3 and 4 - improve l by adding edges or augmenting matching M
			Update(graph, u);	
		}		
	}
	
	public Node GetAvailableXNode(Graph graph) {
		Node x = null;
		
		HashSet<Node> Xset = new HashSet<Node>(graph.getNodeListX());
	//	for (Node n : Xset) {
	//		dp("Node is from " + (n.GetIsXNode() ? "X" : "Y") + " the x node, with index " + n.GetIndex());
	//	}
		
		for (Edge e : matching) {
			int xIndx = e.GetX_Index();
			Xset.remove(graph.getNodeX(xIndx));
		}
		
		Iterator<Node> iter = Xset.iterator();
		if (iter.hasNext()) {
			x = iter.next();
		}		
		return x;		
	}
	
	public Node InitializeStep2(Graph graph) {

		dp("Starting Step 2 ---------------------");
		S_set.clear();
		T_set.clear();
		
		Node x = GetAvailableXNode(graph);
		S_set.add(x);
		
		return x;	
	}
	
	public void AugmentPath(Node a, Node b, Graph graph) {
		// Augment the path from a to b (neither should be in the matching)
		if (IsNodeMatched(a, matching) || IsNodeMatched(b, matching)) {
			System.out.println("ERROR - Attempting to augment between two nodes and one is already matched...");
			return;
		}
		
		dp("Augmenting a path from "+a.GetName() + " to " + b.GetName());
		
		// Now we can do the augmenting ...
		dp("Pre-Augment match and non-match edges");
		PrintMatchedAndUnmatched();
				
		if (!a.GetIsXNode()) {
			System.out.println("ERROR!! Augment path called on a non-X node...");
			return;
		}
		// Simple check, is edge (x,y) in the tight edges but not in the matching?
		Edge e = graph.getEdge(a.GetIndex(), b.GetIndex());
		
		if (tightEdges.contains(e)) {
			if (!matching.contains(e)) {
				AddMatchingEdge(e);
				dp("Added a direct edge...");
				if (debug) {
					PrintMatching();
				}
				return;
			}
		}
		
		List<Node> visited = new ArrayList<Node>();
		if (!FindPath(a, b, graph, visited)) {
			System.out.println("ERROR!! Not able to find an augmenting path...");
			return;
		}
		
		dp("There are " + visited.size() + " nodes");
		String str = "Augmented path is: ";
		for (Node n : visited) {
			str += n.GetName() + " -> ";
		}
		dp(str);
		
		// Now, loop through the list and augment the edges between matched and unmatched
		for (int i = 1; i < visited.size(); ++i) {
			
			Edge ed = graph.getEdge(visited.get(i - 1), visited.get(i));

			// if i is odd, edge is unmatched and should become matched
			if (i % 2 == 0) {
				// i is even
				RemoveMatchingEdge(ed);
			} else {
				AddMatchingEdge(ed);
			}			
		}
		
		dp("After augmenting path...");
		PrintMatchedAndUnmatched();
		
		
	}
	
	public void PrintMatchedAndUnmatched() {
		if (debug) {
			System.out.println("----------");
			System.out.println("All matching edges...");
			PrintEdges(matching, 0 );
			System.out.println("All non-Matching Edges");
			PrintEdges(nonMatching, 0);
			System.out.println("----------");
		}
	}
	
	// Recursive function for finding the augmenting path
	// Returns true if path was found, false if no path was found 
	public boolean FindPath(Node a, Node b, Graph graph, List<Node> visited) {
		// node b is the desired end-node
		// node a is the starting node, (current node being visited
		// visited is the set of nodes that have been considered in this search path
		// 	to prevent infinite loops
		// the size of visited will also be used to determine if an edge should 
		// 	be selected from current matching or tight edges.
		// 	If |visited| is odd, edge can't be in matching
		//  If |visited| is even, edge must be in matching
		
		// early out condition (a has already been visited)
		if (visited.contains(a)) return false;
		
		// Add the node to the list of visited nodes
		visited.add(a);
		
		// the desired node has been reached, augmenting path has been found
		if (a == b) return true;
				
		// Next determine if the next edge is matched or not
		boolean getMatchedEdge = (visited.size() % 2 == 0);
		
		Iterator<Edge> sIter = null;

		if (getMatchedEdge) {
			dp("Looking for a matched edge, because visited is even with " + visited.size() +" nodes");
			sIter = matching.iterator();
		} else {
			sIter = nonMatching.iterator();
		}
		
		while (sIter.hasNext()) {
			Edge e = sIter.next();
			if (e.GetConnectsNodes(a)) {
				// edge e connects to node a
				Node next = null;	
				if (a.GetIsXNode()) {
					next = graph.getNodeY(e.GetY_Index());
				} else {
					next = graph.getNodeX(e.GetX_Index());
				}
				if (FindPath(next, b, graph, visited)) {
					return true;
				}
			}			
		}
		visited.remove(a);
		return false;
	}
	
	// This will determine if a node is matched to an edge in the matching
	public static boolean IsNodeMatched(Node node, Set<Edge> match) {
		// Since nodes have an X or Y(ness), this function will determine
		// if the node is matched to any edge in the matching
		int index = node.GetIndex();
		for (Edge edge : match) {
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
					AddTightEdge(graph.getEdge(i, j));
				}
			}
			graph.getNodeX(i).SetLabel(maxWeight);			
		}
		if (debug) {
			System.out.println("Initial labels are:");
			graph.printNodeCover();
			System.out.println("Tight Edges:");
			PrintEdges(tightEdges, 0);
		}
		
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
	
	public void PrintMatching() {
		PrintEdges(matching, 1);
	}
	
	// debug printing function (saving me from typing system.out ...) and conditionally disabling debug under
	// debug flag (so not just pure laziness)
	public void dp(String s) {
		if (debug) {
			System.out.println(s);
		}
	}

	public void printSandT() {
		String str;
		str = "S = [";
		for (Node n : S_set) {
			str += n.GetIndex() + ", ";
		}
		str += "]";
		dp(str);
		
		str = "T = [";
		for (Node n : T_set) {
			str += n.GetIndex() + ", ";
		}
		str += "]";
		dp(str);
		
	}
	
	public void printNodes(Set<Node> nodes) {
		String str;
		str = "Nodes = [";
		for(Node n : nodes) {
			str += n.GetIndex() + ", ";
		}
		str += "]";
		dp(str);
	}
}
