package TetrisJon;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame {
	JLabel statusbar;

	public Main() {

		AI board = new AI();
		add(board);
		board.start();
		setSize(200, 425);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// add(new GUIContainer(new Chromo[8]));
		// setSize(450, 500);
	}

	public void GUI() {
		JPanel GUI = new JPanel();
		GUI.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		add(GUI);
		setSize(1, 425);
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

	}

	public static void main(String[] args) {
		Main game = new Main();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}

}