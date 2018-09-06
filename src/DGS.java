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
		Graph solution = executeDGSAlgorithm(graph);

		solution.printSolution();
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
			Node bidder = nodeQueue.remove();
			Node matchedNode = auctionRound(bidder, solution, reference);
			if (matchedNode != null) {
				Node previousBidder = matchedNode.getOwner();
				matchedNode.setOwner(bidder);
				matchedNode.setPrice(matchedNode.getPrice() + delta);

				if (previousBidder != null) {
					//add the previous bidder back into the queue
					nodeQueue.add(previousBidder);

					//remove the previous match from the solution;
					Edge previousEdge = solution.getEdge(previousBidder.GetIndex(), matchedNode.GetIndex());
					solution.removeEdge(previousEdge);
				}

				//add the new matched edge to the solution;
				Edge matchedEdge = reference.getEdge(bidder.GetIndex(), matchedNode.GetIndex());
				solution.addEdge(matchedEdge);
			}
		}
		return solution;

	}

	private static Node auctionRound(Node bidder, Graph solution, Graph reference) {
		// Attempt to find a Node in the 'seller' nodes that maximizes the value of:
		// the weight of the edge between the two nodes less the price of the node.
		Node matchedNode = null;
		Float bestValue = 0F;

		for (Node seller : reference.getNodeListY()) {
			Edge edge = reference.getEdge(bidder.GetIndex(), seller.GetIndex()); // find edge
			Float value = edge.GetWeight() - seller.getPrice();

			if (value > bestValue) {
				matchedNode = seller;
				bestValue = value;
			}
		}

		return matchedNode;
	}

}
