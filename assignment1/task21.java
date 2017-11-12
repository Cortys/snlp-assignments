import java.io.IOException;
import java.io.PrintWriter;

class task21 {
	public static void main(String[] args) {
		try {
			DFA.Result result = Parser.readTransitions(args[0])
									.dRecognize(Parser.readInput(args[1]));

			PrintWriter w = new PrintWriter(args[2]);
			result.stateTransitions.forEach(w::println);
			w.println(result.accepts ? "yes" : "no");
			w.close();
		} catch(IOException e) {
			System.out.println("IOException: " + e.getMessage());
		}
	}
}
