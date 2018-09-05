
public class KM {

	public static void main(String[] args) {
		long startTime=System.nanoTime();

		if (args.length == 0) {
			System.out.println("no input parameter provided\n");
			return;
		}
		int[][] matrix = InputParserUtility.ParseInput(args[0]);

		int num = 4;
		int weight[][] = new int[num][];
		
		for (int i = 0; i < num; ++i) {
			weight[i] = new int[num];
			for (int j = 0; j < num; ++j) {
				weight[i][j] = num * i + j;
			}
		}
		
		
		Graph G = new Graph(num, weight);
		
		G.PrintGraphEdges();
		G.PrintNodeCover();
		
		G = new Graph(3, matrix);
		G.PrintGraphEdges();
		G.PrintNodeCover();
				
		
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
