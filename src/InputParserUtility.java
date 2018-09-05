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
	public static Graph ParseInput(String filename) {
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
			return new Graph();
		}
	}

	private static Graph parseInput(final List<String> inputLines) throws Exception {

		Graph graph = new Graph();

		List<String> lines = inputLines.stream() //
		.map(line -> line.replaceAll("^\\s+", "")) // remove leading spaces
		.filter(line -> (line.matches("^\\d+.*$"))) // filter lines that don't start with a digit
		.collect(Collectors.toList()); //

		lines.remove(0).replaceAll("[^\\d].*", ""); // we don't need the matrix size; we'll infer it.

		for (int i = 0; i < lines.size(); i++) {
			graph.addNodeX(new Node(i));
			graph.addNodeY(new Node(i));
		}

		for (int i = 0; i < lines.size(); i++) {
			Node nodeX = graph.getNodeX(i);

			int[] weights = Arrays.stream(lines.get(i).split("\\s+")) //
					.mapToInt(Integer::parseInt) //
					.toArray();

			for (int j = 0; j < weights.length; j++) {
				Node nodeY = graph.getNodeY(j);
				graph.addEdge(new Edge(nodeX, nodeY, weights[j]));
			}
		}

		return graph;
	}
}
