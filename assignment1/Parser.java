import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.List;
import java.util.LinkedList;

/**
 * A helper class that is used to read and parse files.
 */
class Parser {
	/**
	 * Parses the string representation of a transition table.
	 * @param List<String> transitionsRaw A list of the lines of the string representation that should be parsed.
	 * @return A DFA.
	 */
	public static DFA parseTransitions(List<String> transitionsRaw) {
		boolean first = true;
		int[][] transitions = new int[0][0];
		boolean[] acceptStates = new boolean[0];
		char[] posToChar = new char[0];

		for(String line: transitionsRaw) {
			String[] cells = line.split("\t");

			if(first) {
				posToChar = new char[cells.length];
				int maxChar = 0;

				for(int i = 1; i < cells.length; i++) {
					posToChar[i] = cells[i].charAt(0);
					maxChar = Math.max(cells[i].charAt(0), maxChar);
				}

				transitions = new int[transitionsRaw.size() - 1][maxChar + 1];
				acceptStates = new boolean[transitionsRaw.size() - 1];

				first = false;
				continue;
			}

			String stateString = cells[0];
			boolean accepts = false;

			if(stateString.charAt(stateString.length() - 1) == ':') {
				accepts = true;
				stateString = stateString.substring(0, stateString.length() - 1);
			}

			int state = Integer.parseInt(stateString);

			acceptStates[state] = accepts;

			for(int i = 0; i < transitions[state].length; i++)
				transitions[state][i] = -1;

			for(int i = 1; i < cells.length; i++)
				transitions[state][posToChar[i]] = Integer.parseInt(cells[i]);
		}

		return new DFA(transitions, acceptStates);
	}

	/**
	 * @param String path The path to an input file.
	 * @return The text contained in the file at the given path.
	 */
	public static String readInput(String path) throws IOException {
		try(Stream<String> lines = Files.lines(Paths.get(path))) {
			return String.join("\n", lines.collect(Collectors.toList()));
		}
	}

	/**
	 * @param String path The path to a file containing a transition table.
	 * @return A DFA for the given transition table.
	 */
	public static DFA readTransitions(String path) throws IOException {
		try(Stream<String> lines = Files.lines(Paths.get(path))) {
			return parseTransitions(lines.collect(Collectors.toList()));
		}
	}
}
