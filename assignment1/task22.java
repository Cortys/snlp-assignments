import java.io.IOException;
import java.util.List;

class task22 {
	public static void main(String[] args) {
		try {
			List<DFA.Match> result = Parser.readTransitions(args[0])
										.dRecognizeAll(Parser.readInput(args[1]));

			result.forEach(match -> System.out.println(match.match));
		} catch(IOException e) {
			System.out.println("Could not read transitions or input.");
		}
	}
}
