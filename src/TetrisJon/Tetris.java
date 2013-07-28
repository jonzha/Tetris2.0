package TetrisJon;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Tetris extends JFrame implements ActionListener {
	SinglePlayer s1;
	Multiplayer m1;
	Multiplayer2 m2;
	JMenuBar menuBar;
	JMenu file;
	JMenuItem instructions, about, menu;
	JPanel splash;
	JPanel cards;
	CardLayout cl;
	JButton startSingle;
	JButton startMulti;

	public Tetris() {
		cl = new CardLayout();
		cards = new JPanel(cl);
		Splash();

		add(cards);
		setSize(200, 425);

		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		menuBar = new JMenuBar();
		Action action = new aboutAction();
		Action instructionAction = new instructionAction();
		file = new JMenu("File");
		menuBar.add(file);
		about = new JMenuItem(action);
		about.setText("About");
		menu = new JMenuItem("Menu");
		menu.addActionListener(this);
		instructions = new JMenuItem(instructionAction);
		instructions.setText("Instructions");
		file.add(instructions);
		file.add(about);
		file.add(menu);

		this.setJMenuBar(menuBar);
	}

	public void Splash() {
		splash = new JPanel();
		splash.setLayout(new BoxLayout(splash, BoxLayout.LINE_AXIS));

		startSingle = new JButton("Single Player");
		startMulti = new JButton("Multiplayer");
		startSingle.addActionListener(this);
		startMulti.addActionListener(this);
		splash.add(startSingle, 0);
		splash.add(startMulti, 1);
		cards.add(splash, "Menu");

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startSingle) {
			cl.next(cards);
			SinglePlayer();
			s1.requestFocus();
		}
		if (e.getSource() == startMulti) {
			cl.next(cards);
			Multiplayer2();
			m2.requestFocus();
		}
		if (e.getSource() == menu) {
			System.out.println("menu");
			splash.setVisible(true);
			s1.setVisible(false);
			cl.next(cards);
		}

	}

	public void SinglePlayer() {
		s1 = new SinglePlayer();
		cards.add(s1, "SinglePlayer");
		// setSize(400, 425);

		s1.start();
	}

	public void Multiplayer() {
		m1 = new Multiplayer();
		add(m1);
		setSize(400, 425);
		m1.start();
	}

	public void Multiplayer2() {
		m2 = new Multiplayer2();
		cards.add(m2, "Multiplayer");
		setSize(400, 425);
		m2.start();
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}

	class aboutAction extends AbstractAction {

		public void actionPerformed(ActionEvent e) {
			ImageIcon ii = new ImageIcon(this.getClass().getResource("JZ.jpg"));
			JOptionPane.showMessageDialog(null, "Version 1.0 /n JON ZHANG",
					"About", 1, ii);
		}
	}

	class instructionAction extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			ImageIcon ii = new ImageIcon(this.getClass().getResource("JZ.jpg"));
			JOptionPane
					.showMessageDialog(
							null,
							" p- pause \n r- restart\n up- rotate \n down- speed drop \n space- hard drop",
							"Instructions", 1, ii);

		}
	}
}
