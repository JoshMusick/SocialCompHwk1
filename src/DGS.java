
public class DGS {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		long startTime=System.nanoTime();

		for (int i = 0; i < 100000; ++i) {
			
			int a = 5 + 8 * 12;
			if (a < 0) {
				a = 0;
			}
		}
		
		
		long endTime=System.nanoTime();
		long totalTime=endTime-startTime;

		System.out.println("Total time taken for DGS is "+totalTime);
		
	}

}
