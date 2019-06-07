import java.awt.EventQueue;

public class Starter {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				PuzzleBoard puzzle = new PuzzleBoard();
				puzzle.setVisible(true);
			}
		});

	}

}
