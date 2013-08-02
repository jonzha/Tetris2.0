package TetrisJon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class Tetris extends JFrame implements ActionListener {
	SinglePlayer s1;
	Multiplayer m1;
	Multiplayer2 m2;

	JPanel splash;
	JButton startSingle;
	JButton startMulti;
	int currentWindow;
	JPanel container;
	final int MENU_WIDTH = 300;
	final int MENU_HEIGHT = 250;
	int gameWidth = 200;
	int gameHeight = 425;
	JMenuBar menuBar;
	JMenu file;
	JMenuItem instructions, about, menu, statistics;
	JCheckBox musicButton, soundButton;
	boolean music = true;
	boolean sound = true;

	public Tetris() {
		container = new JPanel();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		Splash();
		windowSize();
		musicSoundBox();
		add(container);
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
		container.add(splash, BorderLayout.CENTER);
		currentWindow = 0;
		setSize(MENU_WIDTH, MENU_HEIGHT);
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

		statistics = new JMenuItem("Statistics");
		statistics.addActionListener(this);

		file.add(about);
		this.setJMenuBar(menuBar);

	}

	public void windowSize() {
		JRadioButton small = new JRadioButton("Small", true);
		JRadioButton medium = new JRadioButton("Medium");
		JRadioButton large = new JRadioButton("Large");

		small.setActionCommand("small");
		medium.setActionCommand("medium");
		large.setActionCommand("large");

		small.addActionListener(this);
		medium.addActionListener(this);
		large.addActionListener(this);

		ButtonGroup group = new ButtonGroup();
		group.add(small);
		group.add(medium);
		group.add(large);

		JPanel sizePanel = new JPanel(new GridLayout(1, 3));
		sizePanel.add(small);
		sizePanel.add(medium);
		sizePanel.add(large);

		container.add(sizePanel);
	}

	public void setSmall() {
		gameWidth = 200;
		gameHeight = 425;
	}

	public void setMed() {
		gameWidth = 290;
		gameHeight = 579;
	}

	public void setLarge() {
		gameWidth = 400;
		gameHeight = 711;
	}

	public void statistics() {

		if (currentWindow != 0) {
			ImageIcon ii = new ImageIcon(this.getClass().getResource("JZ.jpg"));

			if (currentWindow == 1) {
				s1.pause();
				JOptionPane.showMessageDialog(null, "Statistics: \n Z-Shape: "
						+ s1.statistics[1] + "\n S-Shape: " + s1.statistics[2]
						+ "\n Line-Shape: " + s1.statistics[3] + "\n T-Shape: "
						+ s1.statistics[4] + "\n Square-Shape: "
						+ s1.statistics[5] + "\n L-Shape: " + s1.statistics[6]
						+ "\n MirrorL-Shape: " + s1.statistics[7],
						"Statistics", 1, ii);
			} else {
				m2.pause();
				JOptionPane.showMessageDialog(null,
						"Statistics Player 1: \n Z-Shape: " + m2.statistics1[1]
								+ "\n S-Shape: " + m2.statistics1[2]
								+ "\n Line-Shape: " + m2.statistics1[3]
								+ "\n T-Shape: " + m2.statistics1[4]
								+ "\n Square-Shape: " + m2.statistics1[5]
								+ "\n L-Shape: " + m2.statistics1[6]
								+ "\n MirrorL-Shape: " + m2.statistics1[7]

								+ "\n\nStatistics Player 2: \n Z-Shape: "
								+ m2.statistics2[1] + "\n S-Shape: "
								+ m2.statistics2[2] + "\n Line-Shape: "
								+ m2.statistics2[3] + "\n T-Shape: "
								+ m2.statistics2[4] + "\n Square-Shape: "
								+ m2.statistics2[5] + "\n L-Shape: "
								+ m2.statistics2[6] + "\n MirrorL-Shape: "
								+ m2.statistics2[7], "Statistics", 1, ii);
			}
		}

		if (currentWindow != 0) {
			if (currentWindow == 1) {
				s1.pause();
			} else {
				m2.pause();
			}
		}

	}

	public void menu() {

		if (currentWindow == 1) {
			s1.pause();
		} else {
			m2.pause();
		}
		ImageIcon ii = new ImageIcon(this.getClass().getResource("sadface.png"));

		int result = JOptionPane.showConfirmDialog(null,
				"Are you sure you want to return to the menu?", "Menu",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ii);
		container.setVisible(true);
		if (result == JOptionPane.NO_OPTION) {
			if (currentWindow == 1) {
				s1.pause();
			} else {
				m2.pause();
			}
			return;
		}
		if (currentWindow == 1) {
			if (music) {
				s1.backgroundMusic.stop();
			}
			remove(s1);
		} else if (currentWindow == 2) {
			if (music) {
				m2.backgroundMusic.stop();
			}
			remove(m2);
		}
		file.remove(menu);
		file.remove(instructions);
		file.remove(statistics);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		currentWindow = 0;
		repaint();

	}

	public void instructions() {

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

	}

	public void about() {

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

	public void musicSoundBox() {
		musicButton = new JCheckBox("Music");
		musicButton.setSelected(true);

		soundButton = new JCheckBox("Sound");
		soundButton.setSelected(true);

		JPanel musicPanel = new JPanel();
		musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.PAGE_AXIS));

		musicPanel.add(musicButton);
		musicPanel.add(soundButton);
		musicButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		soundButton.setAlignmentX(Component.CENTER_ALIGNMENT);

		musicButton.addItemListener(itemListener);
		soundButton.addItemListener(itemListener);
		container.add(musicPanel);

	}

	ItemListener itemListener = new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
			Object source = e.getItemSelectable();
			if (source == musicButton) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					music = false;
					System.out.println("Music disabled!");
				} else {
					music = true;
				}
			} else if (source == soundButton) {
				if (e.getStateChange() == ItemEvent.DESELECTED) {
					sound = false;
					System.out.println("Sound disabled!");
				} else {
					sound = true;
				}
			}
		}
	};

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startSingle) {
			SinglePlayer();

		} else if (e.getSource() == startMulti) {
			Multiplayer2();
		} else if (e.getSource() == menu) {
			menu();
		} else if (e.getSource() == instructions) {
			instructions();
		} else if (e.getSource() == about) {
			about();
		} else if (e.getActionCommand() == "small") {
			setSmall();
			System.out.println("apwoeifj");
		} else if (e.getActionCommand() == "medium") {
			setMed();
			System.out.println("apwoeifj");
		} else if (e.getActionCommand() == "large") {
			setLarge();
			System.out.println("apwoeifj");
		} else if (e.getSource() == statistics) {
			statistics();
		}

	}

	public void SinglePlayer() {
		s1 = new SinglePlayer();
		add(s1);
		setSize(gameWidth, gameHeight);
		setLocationRelativeTo(null);
		container.setVisible(false);
		s1.music = music;
		s1.sound = sound;
		s1.start();
		s1.requestFocus();
		currentWindow = 1;
		file.add(menu);
		file.add(instructions);
		file.add(statistics);
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
		setSize(gameWidth * 2, gameHeight);
		setLocationRelativeTo(null);
		container.setVisible(false);
		m2.music = music;
		m2.sound = sound;
		m2.start();
		m2.requestFocus();
		currentWindow = 2;
		file.add(menu);
		file.add(instructions);
		file.add(statistics);

	}

	/*
	 * public static void main(String[] args) { Tetris game = new Tetris();
	 * game.setLocationRelativeTo(null); game.setVisible(true); }
	 */
}
