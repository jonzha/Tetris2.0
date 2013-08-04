package TetrisJon;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame {
	JLabel statusbar;

	public Main() {
		AI board = new AI();
		add(board);
		board.start();
		setSize(200, 425);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		Main game = new Main();

		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}

}