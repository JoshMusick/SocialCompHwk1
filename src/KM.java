
public class KM {

	public static void main(String[] args) {
		long startTime=System.nanoTime();

		if (args.length == 0) {
			System.out.println("no input parameter provided\n");
			return;
		}
		Graph graph = InputParserUtility.ParseInput(args[0]);
		graph.PrintGraphEdges();
		graph.PrintNodeCover();

		long endTime=System.nanoTime();
		long totalTime=endTime-startTime;

		System.out.println("Total time taken for KM is " + totalTime + " ns");
	}
	
	public void GenerateMatching()
	{
		// Create an empty matching
		
		// 1. 
		
		
		
	}

}
