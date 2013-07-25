package TetrisJon;

import javax.swing.JFrame;

public class Tetris extends JFrame {
	Board board = new Board();

	public Tetris() {
		add(board);
		setSize(200, 400);
		board.start();
		setTitle("Tetris");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();

		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}
