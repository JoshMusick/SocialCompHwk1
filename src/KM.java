import java.util.HashSet;
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
		
		// 1. Assign initial labels,  max(x,y) to each x, 0 for all y
		InitializeLabels(graph, tightEdges);
		
		
		// 2. Generate the "tight edge" Graph which will be E_l, this will 
		// 		include all edges connected to x nodes, with max weight (labels should be same)
		
		// 3. Select an initial matching M from the the E_l
		
		// 4. Begin algorithm (see pg 14 of lecture notes)
		
		
		
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
