import java.util.List;
import java.util.LinkedList;

/**
 * Represents a deterministic finite automaton.
 */
class DFA {
	public final int startState = 0;
	/**
	 * A transition table. Represented by a 2d-array.
	 * The first index is the id of the current state, the second index is the ASCII code
	 * of the currently read input and the value is the id of the target state.
	 */
	public final int[][] transitions;
	/**
	 * Indices represent state ids. The boolean value represents whether a given state is accepting.
	 */
	public final boolean[] acceptStates;

	public DFA(int[][] transitions, boolean[] acceptStates) {
		this.transitions = transitions;
		this.acceptStates = acceptStates;
	}

	/**
	 * Represents the result of a DFA.
	 */
	public class Result {
		/**
		 * A list of sthe ids of the states that were traversed.
		 */
		public final List<Integer> stateTransitions;
		/**
		 * Whether the DFA accepted the input.
		 */
		public final boolean accepts;
		/**
		 * A list of input positions at which the DFA would have accepted, assuming no further input had followed.
		 */
		public final List<Integer> possibleAccepts;

		private Result(List<Integer> stateTransitions, boolean accepts) {
			this.stateTransitions = stateTransitions;
			this.accepts = accepts;
			this.possibleAccepts = new LinkedList<>();

			int i = 0;
			for(int state: stateTransitions) {
				if(acceptStates[state])
					this.possibleAccepts.add(i);
				i++;
			}
		}
	}

	/**
	 * Represents a substring of an input.
	 */
	public class Match {
		/**
		 * The offset of the match.
		 */
		public final int pos;
		/**
		 * The string representation of the match.
		 */
		public final String match;

		private Match(int pos, String match) {
			this.pos = pos;
			this.match = match;
		}
	}

	private Result dRecognize(char[] input, int offset) {
		int index = offset;
		int state = startState;
		LinkedList<Integer> stateTransitions = new LinkedList<>();
		stateTransitions.add(state);

		while(true) {
			if(index == input.length)
				return new Result(stateTransitions, acceptStates[state]);
			else {
				try {
					int nextState = transitions[state][input[index]];

					if(nextState < 0)
						throw new Exception("No state transition for state " + state + " and input " + input[index] + " defined.");

					index++;
					state = nextState;
					stateTransitions.add(state);
				} catch(Exception e) {
					return new Result(stateTransitions, false);
				}
			}
		}
	}

	/**
	 * Checks whether a given string is matched by the DFA.
	 * @param String input The input string.
	 * @return A Result object, that contains the states that were traversed and the binary acceptance output.
	 */
	public Result dRecognize(String input) {
		return dRecognize(input.toCharArray(), 0);
	}

	/**
	 * Checks a given input string for all possible substring matches.
	 * @param String inputRaw The input string.
	 * @return A list of Match objects. Every match contains the offset from the input and the matched string.
	 */
	public List<Match> dRecognizeAll(String inputRaw) {
		List<Match> matches = new LinkedList<>();
		char[] input = inputRaw.toCharArray();

		for(int offset = 0; offset < input.length; offset++) {
			Result r = dRecognize(input, offset);

			for(int len: r.possibleAccepts)
				matches.add(new Match(offset, new String(input, offset, len)));
		}

		return matches;
	}
}
