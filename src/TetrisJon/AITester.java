package TetrisJon;

import javax.swing.JFrame;

public class AITester extends JFrame {
	AI ai = new AI();

	public AITester() {
		add(ai);
		setSize(200, 425);
		ai.start();
		setTitle("AI");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		AITester game = new AITester();

		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}