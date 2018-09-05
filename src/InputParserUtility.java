import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InputParserUtility {

	/**
	 *
	 * @param filename
	 * @return a 2D array
	 */
	public static int[][] ParseInput(String filename) {
		File inputFile = new File(filename);
		if (!inputFile.canRead()) {
			System.out.println("cannot read input file: " + inputFile.getName());
		}
		try {
			List<String> lines = Files.readAllLines(inputFile.toPath());
			return parseInput(lines);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("fail to read input file: " + e.getMessage());
			return new int[0][0];
		}
	}

	private static int[][] parseInput(final List<String> inputLines) throws Exception {

		List<String> lines = inputLines.stream() //
		.map(line -> line.replaceAll("^\\s+", "")) // remove leading spaces
		.filter(line -> (line.matches("^\\d+.*$"))) // filter lines that don't start with a digit
		.collect(Collectors.toList()); //

		// remove the non-digit characters in the size string
		String matrixSizeString = lines.remove(0).replaceAll("[^\\d].*", "");
		Integer matrixSize = Integer.valueOf(matrixSizeString);


		int[][] matrix = new int[matrixSize][];

		for (int i = 0; i < lines.size(); i++) {
			matrix[i] = Arrays.stream(lines.get(i).split("\\s+")) //
			.mapToInt(Integer::parseInt) //
			.toArray();
		}

		return matrix;
	}
}
