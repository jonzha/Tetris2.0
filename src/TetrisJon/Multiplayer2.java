package TetrisJon;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Multiplayer2 extends JPanel implements ActionListener, KeyListener {
	final int BOARD_WIDTH = 20;
	final int BOARD_HEIGHT = 22;
	int[][] board;
	int[][] airBoard1;
	int[][] airBoard2;
	Tetri current;
	Tetri current2;
	Timer timer;
	int squareHeight;
	int squareWidth;
	int score;
	int delay;
	int level;
	int linesCleared;
	int topOfPiece;// used for scoring
	int[] topOfPieces;
	double multiplier;
	boolean pause;
	boolean gameOver;
	boolean realLanding;// used so that the pieces won't stack in midair
	BitSet keys = new BitSet(256);
	double height, width;
	int[] statistics1, statistics2;
	Clip backgroundMusic = null;
	boolean sound, music;

	public Multiplayer2() {
		board = new int[BOARD_WIDTH][BOARD_HEIGHT];
		airBoard1 = new int[BOARD_WIDTH][BOARD_HEIGHT];
		airBoard2 = new int[BOARD_WIDTH][BOARD_HEIGHT];
		setFocusable(true);
		delay = 400;
		timer = new Timer(delay, this);
		current = new Tetri(0);
		// topOfPieces = new int[BOARD_WIDTH];
		addKeyListener(this);

	}

	public void start() {
		this.setVisible(true);

		// getSqHeight();
		// getSqWidth();
		squareHeight = 16;
		squareWidth = 20;
		score = 0;
		delay = 400;
		level = 1;
		gameOver = false;
		linesCleared = 0;
		multiplier = 1;
		topOfPiece = 0;
		statistics1 = new int[8];
		statistics2 = new int[8];
		/*
		 * for (int i = 0; i < topOfPieces.length; i++) { topOfPieces[i] = 0; }
		 */
		fillWithEmpty();
		repaint();
		newPiece1();
		newPiece2();
		if (music) {
			playBackgroundMusic("BackgroundMusic.wav");
		}
		timer.start();

		// tryMove(current, 10, 40);
	}

	public void actionPerformed(ActionEvent e) {
		drop(current);
		drop(current2);
	}

	public void getSqHeight() {
		// System.out.println((int) (getSize().getHeight() / BOARD_HEIGHT));
		height = getSize().getHeight();
		squareHeight = ((int) ((height - 29) / BOARD_HEIGHT)); // -29 to account
																// for status
																// bar on bottom
	}

	public void getSqWidth() {
		width = getSize().getWidth();
		System.out.println((int) (getSize().getWidth() / BOARD_WIDTH));
		squareWidth = (int) (width / BOARD_WIDTH);
	}

	public void newPiece1() {
		current = new Tetri(1 + (int) (Math.random() * ((7 - 1) + 1)));
		current.player = 1;
		statistics1[current.identifier]++;
		current.curX = BOARD_WIDTH / 2 - 1 + 3;
		current.curY = 1;
		if (!canMove(current, current.curX, current.curY)) {
			gameOver = true;
			if (music) {
				backgroundMusic.stop();
			}
			timer.stop();
			repaint();
		}
	}

	public void newPiece2() {
		current2 = new Tetri(1 + (int) (Math.random() * ((7 - 1) + 1)));
		current2.player = 2;
		statistics2[current2.identifier]++;
		current2.curX = BOARD_WIDTH / 2 - 1 - 3;
		current2.curY = 1;
		if (!canMove(current2, current2.curX, current2.curY)) {
			if (music) {
				backgroundMusic.stop();
			}
			gameOver = true;
			timer.stop();
			repaint();
		}
	}

	public boolean canMove(Tetri tetri, int x, int y) {
		int newY, newX;
		// Holds positions of the invisible boundaries
		clearInvisi(tetri);

		for (int i = 0; i < 4; i++) {
			newY = y + tetri.coords[i][1];
			newX = tetri.coords[i][0] + x;
			if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT
					|| newY < 0) {
				realLanding = true;
				return false;
			}
			if (board[newX][newY] != 0) {
				realLanding = true;
				return false;
			}

			if (tetri.player == 1) {
				if (airBoard2[newX][newY] != 0) {
					System.out.println("POWEIJFPAOEWFJAPOWEF");
					realLanding = false;
					return false;
				}
			} else if (tetri.player == 2) {
				if (airBoard1[newX][newY] != 0) {
					System.out.println("hi");
					realLanding = false;
					return false;
				}
			}

		}

		// Adding the invisible boundaries
		if (tetri.player == 1) {
			for (int i = 0; i < 4; i++) {
				airBoard1[tetri.curX + tetri.coords[i][0]][tetri.curY
						+ tetri.coords[i][1]] = tetri.identifier;
			}
		} else if (tetri.player == 2) {
			for (int i = 0; i < 4; i++) {
				airBoard2[tetri.curX + tetri.coords[i][0]][tetri.curY
						+ tetri.coords[i][1]] = tetri.identifier;
			}

		}

		tetri.curX = x;
		tetri.curY = y;
		repaint();
		return true;
	}

	public void clearInvisi(Tetri tetri) {
		if (tetri.player == 2) {

			for (int i = 0; i < BOARD_WIDTH; i++) {
				for (int j = 0; j < BOARD_HEIGHT; j++) {
					airBoard2[i][j] = 0;
				}
			}
		} else if (tetri.player == 1) {
			for (int i = 0; i < BOARD_WIDTH; i++) {
				for (int j = 0; j < BOARD_HEIGHT; j++) {
					airBoard1[i][j] = 0;
				}
			}
		}

	}

	public void removeInvisiPiece(Tetri tetri) {
		if (tetri.player == 2) {
			for (int i = 0; i < 4; i++) {
				airBoard2[tetri.curX + tetri.coords[i][0]][tetri.curY
						+ tetri.coords[i][1]] = 0;
			}
		} else if (tetri.player == 1) {

			for (int i = 0; i < 4; i++) {
				airBoard1[tetri.curX + tetri.coords[i][0]][tetri.curY
						+ tetri.coords[i][1]] = 0;
			}

		}
	}

	public void drop(Tetri tetri) {
		if (!canMove(tetri, tetri.curX, tetri.curY + 1) && realLanding) {
			clearInvisi(tetri);
			pieceDropped(tetri);
		}

	}

	public void pieceDropped(Tetri tetri) {
		if (sound) {
			playSound("Pop.wav");
		} // WHY MUST YOU ADD CURRENT
			// Because Coords never change!
		for (int i = 0; i < 4; i++) {
			// System.out.println("x is " + (current.curX +
			// current.coords[i][0]) +
			// "y is " + (current.curY + current.coords[i][1]));
			board[tetri.curX + tetri.coords[i][0]][tetri.curY
					+ tetri.coords[i][1]] = tetri.identifier;
		}
		// getTopOfPieces();
		removeLines();
		if (tetri.player == 1) {
			newPiece1();
		} else {
			newPiece2();
		}
	}

	public void paint(Graphics g) {

		super.paint(g);
		getSqWidth();
		getSqHeight();
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				if (board[j][i] != 0) {
					drawSquare(g, j * squareWidth, i * squareHeight,
							board[j][i], i);
				}
			}
		}
		drawPiece(g, current);
		drawPiece(g, current2);

		// score
		g.setColor(Color.black);
		FontMetrics fm = getFontMetrics(getFont());
		int scoreWidth = fm.stringWidth("Score: " + score);
		g.drawString("Score: " + score, (int) (width - scoreWidth - 5), 15);

		// Statusbar
		g.fillRect(0, (int) (height - 29), (int) width, 29);

		// Statusbar information
		g.setColor(Color.white);
		g.drawString("Level: " + level, 10, (int) (height - 11));
		System.out.println(height + "height");
		g.drawString("Multiplier: " + multiplier + "x", (int) (width - 100),
				(int) (height - 11));

		if (gameOver) {
			gameOver(g);
		}
	}

	private void drawPiece(Graphics g, Tetri tetri) {
		for (int i = 3; i >= 0; i--) {
			int x = tetri.curX + tetri.coords[i][0];
			int y = tetri.curY + tetri.coords[i][1];
			drawSquare(g, x * squareWidth, y * squareHeight, tetri.identifier,
					i);
		}

		Tetri temp = new Tetri(tetri.identifier);
		temp.curX = tetri.curX;
		temp.curY = tetri.curY;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				temp.coords[i][j] = tetri.coords[i][j];
			}
		}
		while (canMove(temp, temp.curX, temp.curY + 1)) {
			;
		}
		for (int i = 3; i >= 0; i--) {
			int x = temp.curX + tetri.coords[i][0];
			int y = temp.curY + tetri.coords[i][1];
			drawOutline(g, x * squareWidth, y * squareHeight, temp.identifier);
		}

	}

	private void drawOutline(Graphics g, int x, int y, int identifier) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
				new Color(102, 204, 102), new Color(102, 102, 204),
				new Color(204, 204, 102), new Color(204, 102, 204),
				new Color(102, 204, 204), new Color(218, 170, 0) };
		Color color = colors[identifier];
		g.setColor(color);

		g.drawLine(x, y + squareHeight - 1, x, y);
		g.drawLine(x, y, x + squareWidth - 1, y);
		g.drawLine(x + 1, y + squareHeight - 1, x + squareWidth - 1, y
				+ squareHeight - 1);
		g.drawLine(x + squareWidth - 1, y + squareHeight - 1, x + squareWidth
				- 1, y + 1);

	}

	private void drawSquare(Graphics g, int x, int y, int identifier, int i) {
		Color colors[] = { new Color(0, 0, 0), new Color(204, 102, 102),
				new Color(102, 204, 102), new Color(102, 102, 204),
				new Color(204, 204, 102), new Color(204, 102, 204),
				new Color(102, 204, 204), new Color(218, 170, 0) };
		Color color = colors[identifier];

		// Creates the actual block
		g.setColor(color);
		g.fillRect(x + 1, y + 1, squareWidth - 2, squareHeight - 2);

		// Creates the brighter lines for 3D effect
		g.setColor(color.brighter());
		g.drawLine(x, y + squareHeight - 1, x, y);
		g.drawLine(x, y, x + squareWidth - 1, y);

		// Creates the darker lines for 3D effect
		g.setColor(color.darker());
		g.drawLine(x + 1, y + squareHeight - 1, x + squareWidth - 1, y
				+ squareHeight - 1);
		g.drawLine(x + squareWidth - 1, y + squareHeight - 1, x + squareWidth
				- 1, y + 1);
		g.setColor(Color.white);
		g.drawString(i + "", x + squareHeight / 2, y + squareWidth / 2);
	}

	public void fillWithEmpty() {
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				board[i][j] = 0;
				airBoard1[i][j] = 0;
				airBoard2[i][j] = 0;
			}
		}
	}

	public void leSuperDrop(Tetri tetri) {
		while (canMove(tetri, tetri.curX, tetri.curY + 1)) {
			;
		}
		topOfPiece = tetri.curY;
		pieceDropped(tetri);
	}

	public void rotate(Tetri tetri) {
		if (tetri.identifier == 5)
			return;
		Tetri temp = new Tetri(tetri.identifier);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				temp.coords[i][j] = tetri.coords[i][j];
			}
		}
		for (int i = 0; i < 4; i++) {
			int x = temp.coords[i][0];
			int y = temp.coords[i][1];
			temp.coords[i][0] = y;
			temp.coords[i][1] = -x;
		}
		if (canMove(temp, tetri.curX, tetri.curY)) {
			tetri.coords = temp.coords;
		}

	}

	public void removeLines() {

		int numRemoved = 0;
		for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
			boolean remove = true;

			// Check if they are all are filled
			for (int j = 0; j < BOARD_WIDTH; j++) {
				if (board[j][i] == 0) {
					remove = false;
				}
			}

			if (remove) {
				for (int j = 0; j < BOARD_WIDTH; j++) {
					board[j][i] = 0;

				}
				numRemoved++;
				linesCleared++;
				newLevel();
				for (int z = i; z > 0; z--) {
					for (int j = 0; j < BOARD_WIDTH; j++) {
						board[j][z] = board[j][z - 1];
					}
				}
				i++; // Needed so that the function can recheck the current line
						// which in reality is the next line up. Needed for
						// multiple line clears at the same time
			}
			// repaint();
		}
		if (numRemoved < 3) {
			score += 1000 * multiplier * numRemoved;
		} else if (numRemoved == 3) {
			score += 1000 * multiplier * numRemoved * 3;
		} else if (numRemoved == 4) {
			score += 1000 * multiplier * numRemoved * 5;

		}

	}

	public void newLevel() {

		if (linesCleared != 0 && linesCleared % 10 == 0) {
			level++;
			multiplier += 0.5;
			delay -= level * 10;
			timer.setDelay(delay);
		}
	}

	public void pause() {
		if (!pause) {
			timer.stop();
			pause = true;
		} else {
			timer.start();
			pause = false;
		}
	}

	public void restart() {
		pause = false;
		if (music) {
			backgroundMusic.stop();
		}
		fillWithEmpty();
		start();
	}

	public void gameOver(Graphics g) {
		System.out.println("Game Over");
		// timer.stop();
		g.setColor(Color.black);
		g.setFont(new Font(null, Font.BOLD, 15));
		FontMetrics fm = getFontMetrics(getFont());
		int gameOverWidth = fm.stringWidth("GAME OVER");
		g.drawString("GAME OVER", (int) (width / 2 - gameOverWidth / 2 - 8),
				(int) (height / 2 - 40));

		g.setFont(new Font(null, Font.BOLD, 20));
		FontMetrics a = getFontMetrics(getFont());
		int scoreWidth = a.stringWidth("YOUR SCORE:");
		g.drawString("YOUR SCORE:", (int) (width / 2 - scoreWidth / 2 - 20),
				(int) (height / 2 - 15));
		scoreWidth = a.stringWidth(score + "");
		g.drawString(score + "", (int) (width / 2 - scoreWidth / 2 - 15),
				(int) (height / 2 + 10));
	}

	public void getTopOfPieces() {
		for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				if (board[j][i] != 0) {
					topOfPieces[j] = i;
				}
			}
		}
	}

	public void playSound(String soundName) {
		Clip clip = null;
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(
					soundName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(audioInputStream);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		clip.start();

	}

	public void playBackgroundMusic(String soundName) {

		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(
					soundName).getAbsoluteFile());
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			backgroundMusic = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			backgroundMusic.open(audioInputStream);
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		keys.set(keyCode);
		keyAction();
	}

	@Override
	public void keyReleased(final KeyEvent event) {
		int keyCode = event.getKeyCode();
		keys.clear(keyCode);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	public boolean isKeyPressed(final int keyCode) {
		return keys.get(keyCode);
	}

	public void keyAction() {
		if (isKeyPressed(KeyEvent.VK_R)) {
			restart();
			return;
		}
		if (gameOver) {
			return;
		}
		if (isKeyPressed(KeyEvent.VK_P)) {
			pause();
		}
		if (pause) {
			return;
		}

		if (isKeyPressed(KeyEvent.VK_LEFT)) {
			canMove(current, current.curX - 1, current.curY);
		}
		if (isKeyPressed(KeyEvent.VK_A)) {
			canMove(current2, current2.curX - 1, current2.curY);
		}
		if (isKeyPressed(KeyEvent.VK_RIGHT)) {
			canMove(current, current.curX + 1, current.curY);
		}
		if (isKeyPressed(KeyEvent.VK_D)) {
			canMove(current2, current2.curX + 1, current2.curY);
		}
		if (isKeyPressed(KeyEvent.VK_DOWN)) {
			drop(current);
			score += 5 * multiplier;
		}
		if (isKeyPressed(KeyEvent.VK_S)) {
			drop(current2);
			score += 5 * multiplier;
		}
		if (isKeyPressed(KeyEvent.VK_SPACE)) {
			int startY = current.curY;
			leSuperDrop(current);
			score += (topOfPiece - startY) * 10 * multiplier;
		}
		if (isKeyPressed(KeyEvent.VK_F)) {
			int startY2 = current2.curY;
			leSuperDrop(current2);
			score += (topOfPiece - startY2) * 10 * multiplier;
		}
		if (isKeyPressed(KeyEvent.VK_UP)) {
			rotate(current);
		}
		if (isKeyPressed(KeyEvent.VK_W)) {
			rotate(current2);
		}
	}

}
