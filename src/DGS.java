import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

public class DGS {

	public static void main(String[] args) {
		long startTime=System.nanoTime();

		if (args.length == 0) {
			System.out.println("no input parameter provided\n");
			return;
		}
		Graph graph = InputParserUtility.ParseInput(args[0]);
		graph.printGraphEdges();
		graph.printNodeCover();
		Graph solution = executeDGSAlgorithm(graph);

		solution.printNodeCover();
		long endTime=System.nanoTime();
		long totalTime=endTime-startTime;

		System.out.println("Total time taken for DGS is "+totalTime + " ns");
	}


	private static Graph executeDGSAlgorithm(Graph reference) {
		// Algorithm implementation here:
		Graph solution = new Graph();
		solution.setNodeListX(reference.getNodeListX());
		solution.setNodeListY(reference.getNodeListY());

		//enqueue all nodes in solution.nodeListX
		Queue<Node> nodeQueue = new LinkedList<Node>(solution.getNodeListX());

		//calculate the delta
		Float delta = Float.valueOf(1.0F/(solution.numNodes() + 1));

		while (nodeQueue.peek() != null) {
			Node activeNode = nodeQueue.remove();
			Node matchedNode = auctionRound(activeNode, solution, reference);
			if (matchedNode != null) {
				//If we found a match, we have to:
				//1) determine if there is already an edge to the matched node
				//   if so:
				//   a) place the previously matched node back into the queue
				//   b) remove the edge from the solution
				//2) create a new edge from activeNode to matchedNode and add to the solution
				//3) increment the price by delta
			}
		}
		return solution;

	}

	private static Node auctionRound(Node activeNode, Graph solution, Graph reference) {
		// Attempt to find a Node in the 'seller' nodes that maximizes the value of:
		// the weight of the edge between the two nodes less the price of the node.
		Node matchedNode = null;
		Float bestValue = 0F;

		for (Node potential : reference.getNodeListY()) {
			Edge edge = reference.getEdge(activeNode.GetIndex(), potential.GetIndex()); // find edge
			Float value = edge.GetWeight() - potential.getPrice();

			if (value > bestValue) {
				matchedNode = potential;
				bestValue = value;
			}
		}

		return matchedNode;
	}

}
