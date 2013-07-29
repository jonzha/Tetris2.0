package TetrisJon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
	JButton startSingle;
	JButton startMulti;
	int currentWindow;

	public Tetris() {

		Splash();
		setTitle("Tetris");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addMenuBar();
	}

	public void Splash() {
		splash = new JPanel();
		splash.setLayout(new BoxLayout(splash, BoxLayout.PAGE_AXIS));
		splash.add(Box.createRigidArea(new Dimension(30, 0)));
		// splash.add(Box.createVerticalGlue());

		ImageIcon ii = new ImageIcon(this.getClass().getResource("JZ.jpg"));
		JLabel picLabel = new JLabel(ii);
		picLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		splash.add(picLabel);

		startSingle = new JButton("Single Player");
		startMulti = new JButton("Multiplayer");
		startMulti.setAlignmentX(Component.CENTER_ALIGNMENT);

		startSingle.setAlignmentX(Component.CENTER_ALIGNMENT);
		startSingle.addActionListener(this);
		startMulti.addActionListener(this);
		splash.add(startSingle);
		splash.add(startMulti);
		add(splash, BorderLayout.CENTER);
		currentWindow = 0;
		setSize(200, 200);
		splash.setVisible(true);

	}

	public void addMenuBar() {
		menuBar = new JMenuBar();
		file = new JMenu("File");
		menuBar.add(file);
		about = new JMenuItem("About");
		about.addActionListener(this);

		menu = new JMenuItem("Menu");
		menu.addActionListener(this);

		instructions = new JMenuItem("Instructions");
		instructions.addActionListener(this);

		file.add(about);
		this.setJMenuBar(menuBar);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startSingle) {
			SinglePlayer();
			s1.requestFocus();
			currentWindow = 1;
			file.add(menu);
			file.add(instructions);
		} else if (e.getSource() == startMulti) {
			Multiplayer2();
			m2.requestFocus();
			currentWindow = 2;
			file.add(menu);
			file.add(instructions);
		} else if (e.getSource() == menu) {
			if (currentWindow == 1) {
				s1.pause();
			} else {
				m2.pause();
			}
			ImageIcon ii = new ImageIcon(this.getClass().getResource(
					"sadface.png"));

			int result = JOptionPane
					.showConfirmDialog(null,
							"Are you sure you want to return to the menu?",
							"Menu", JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE, ii);
			splash.setVisible(true);
			if (result == JOptionPane.NO_OPTION) {
				if (currentWindow == 1) {
					s1.pause();
				} else {
					m2.pause();
				}
				return;
			}
			if (currentWindow == 1) {
				remove(s1);
			} else if (currentWindow == 2) {
				remove(m2);
			}
			file.remove(menu);
			file.remove(instructions);
			setSize(200, 200);
			currentWindow = 0;
			repaint();
		} else if (e.getSource() == instructions) {
			ImageIcon ii = new ImageIcon(this.getClass().getResource("JZ.jpg"));
			if (currentWindow == 1) {
				s1.pause();

				JOptionPane
						.showMessageDialog(
								null,
								" p- pause \n r- restart\n up- rotate \n down- speed drop \n space- hard drop",
								"Instructions", 1, ii);
				s1.pause();

			} else if (currentWindow == 2) {
				m2.pause();
				JOptionPane
						.showMessageDialog(
								null,
								" p- pause \n r- restart\n\n Player 1 \n up- rotate \n down- speed drop \n space- hard drop \n \n Player 2 \n w- rotate \n s- speed drop \n f- hard drop",
								"Instructions", 1, ii);
				m2.pause();

			}

		} else if (e.getSource() == about) {
			if (currentWindow != 0) {
				if (currentWindow == 1) {
					s1.pause();
				} else {
					m2.pause();
				}
			}
			ImageIcon ii = new ImageIcon(this.getClass().getResource("JZ.jpg"));

			JOptionPane.showMessageDialog(null, "Version 1.0 \n JON ZHANG",
					"About", 1, ii);
			if (currentWindow != 0) {
				if (currentWindow == 1) {
					s1.pause();
				} else {
					m2.pause();
				}
			}
		}

	}

	public void SinglePlayer() {
		s1 = new SinglePlayer();
		add(s1);
		setSize(200, 425);
		s1.setVisible(true);
		splash.setVisible(false);
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
		add(m2);
		setSize(400, 425);
		m2.start();
	}

	public static void main(String[] args) {
		Tetris game = new Tetris();
		game.setLocationRelativeTo(null);
		game.setVisible(true);
	}
}
