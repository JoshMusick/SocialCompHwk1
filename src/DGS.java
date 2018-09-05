import java.util.LinkedList;
import java.util.Queue;

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


	private static Graph executeDGSAlgorithm(Graph graph) {
		// Algorithm implementation here:
		Graph solution = new Graph();
		solution.setNodeListX(graph.getNodeListX());
		solution.setNodeListY(graph.getNodeListY());

		//enqueue all nodes in solution.nodeListX
		Queue<Node> nodeQueue = new LinkedList<Node>(solution.getNodeListX());

		//TODO: calculate delta
		//TODO: set all 'prices' to 0  (store in the 'y' node label?)
		while (nodeQueue.peek() != null) {
			Node activeNode = nodeQueue.remove();
			Node matchedNode = auctionRound(activeNode, solution);
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


	private static Node auctionRound(Node activeNode, Graph solution) {
		// TODO Implement single round of the auction algorithm here...
		return null;
	}

}
