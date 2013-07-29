package TetrisJon;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

public class SinglePlayer extends JPanel implements ActionListener {
	final int BOARD_WIDTH = 10;
	final int BOARD_HEIGHT = 22;
	int[][] board;
	Tetri current;
	Timer timer;
	int squareHeight;
	int squareWidth;
	int score;
	int delay;
	int level;
	int linesCleared;
	int topOfPiece;// used for scoring
	int[] topOfPieces;
	double height, width;
	double multiplier;
	boolean pause;
	boolean gameOver;

	public SinglePlayer() {
		board = new int[BOARD_WIDTH][BOARD_HEIGHT];
		setFocusable(true);
		delay = 400;
		timer = new Timer(delay, this);
		current = new Tetri(0);
		// topOfPieces = new int[BOARD_WIDTH];
		addKeyListener(new KeyHandler());
	}

	public void start() {
		this.setVisible(true);
		System.out.println("Started");
		squareHeight = 16;
		squareWidth = 20;
		score = 0;
		delay = 400;
		level = 1;
		gameOver = false;
		linesCleared = 0;
		multiplier = 1;
		topOfPiece = 0;
		/*
		 * for (int i = 0; i < topOfPieces.length; i++) { topOfPieces[i] = 0; }
		 */
		fillWithEmpty();
		repaint();
		newPiece();
		timer.start();
		// tryMove(current, 10, 40);
	}

	public void actionPerformed(ActionEvent e) {

		drop();

	}

	public void getSqHeight() {
		System.out.println((int) (getSize().getHeight() / BOARD_HEIGHT));
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

	public void newPiece() {

		System.out.println("newpiece");

		current = new Tetri(1 + (int) (Math.random() * ((7 - 1) + 1)));
		current.curX = BOARD_WIDTH / 2 - 1;
		current.curY = 1;
		if (!canMove(current, current.curX, current.curY)) {
			gameOver = true;
			timer.stop();
			repaint();
		}
		/*
		 * if (score % 10 == 0) { delay -= 20; timer.setDelay(delay); }
		 */
	}

	public boolean canMove(Tetri shape, int x, int y) {
		int newY, newX;
		for (int i = 0; i < 4; i++) {
			newY = y + shape.coords[i][1];
			newX = shape.coords[i][0] + x;
			if (newX < 0 || newX >= BOARD_WIDTH || newY >= BOARD_HEIGHT
					|| newY < 0) {
				return false;
			}
			if (board[newX][newY] != 0) {
				return false;
			}
		}
		// current = shape;
		shape.curX = x;
		shape.curY = y;
		repaint();
		return true;
	}

	public void drop() {
		if (!canMove(current, current.curX, current.curY + 1)) {
			pieceDropped();
		}

	}

	public void pieceDropped() {
		// WHY MUST YOU ADD CURRENT
		// Because Coords never change!
		for (int i = 0; i < 4; i++) {
			// System.out.println("x is " + (current.curX +
			// current.coords[i][0]) +
			// "y is " + (current.curY + current.coords[i][1]));
			board[current.curX + current.coords[i][0]][current.curY
					+ current.coords[i][1]] = current.identifier;
		}
		// getTopOfPieces();
		removeLines();
		newPiece();
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

		for (int i = 3; i >= 0; i--) {
			int x = current.curX + current.coords[i][0];
			int y = current.curY + current.coords[i][1];
			drawSquare(g, x * squareWidth, y * squareHeight,
					current.identifier, i);
		}

		Tetri temp = new Tetri(current.identifier);
		temp.curX = current.curX;
		temp.curY = current.curY;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				temp.coords[i][j] = current.coords[i][j];
			}
		}
		while (canMove(temp, temp.curX, temp.curY + 1)) {
			;
		}
		for (int i = 3; i >= 0; i--) {
			int x = temp.curX + current.coords[i][0];
			int y = temp.curY + current.coords[i][1];
			drawOutline(g, x * squareWidth, y * squareHeight, temp.identifier);
		}

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
		System.out.println(width + "Width");
		g.drawString("Multiplier: " + multiplier + "x", (int) (width - 100),
				(int) (height - 11));

		// Gameover
		if (gameOver) {
			gameOver(g);
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
			}
		}
	}

	public void leSuperDrop() {
		while (canMove(current, current.curX, current.curY + 1)) {
			;
		}
		topOfPiece = current.curY;
		pieceDropped();
	}

	public void rotate() {
		if (current.identifier == 5)
			return;
		Tetri temp = new Tetri(current.identifier);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				temp.coords[i][j] = current.coords[i][j];
			}
		}
		for (int i = 0; i < 4; i++) {
			int x = temp.coords[i][0];
			int y = temp.coords[i][1];
			temp.coords[i][0] = y;
			temp.coords[i][1] = -x;
		}
		if (canMove(temp, current.curX, current.curY)) {
			current.coords = temp.coords;
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
					break;
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
		fillWithEmpty();
		start();
	}

	public void gameOver(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
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

	class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			if (key == KeyEvent.VK_R) {
				restart();
				return;
			}
			if (gameOver) {
				return;
			}
			if (key == KeyEvent.VK_P) {
				pause();
			}
			if (pause) {
				return;
			}
			switch (key) {
			case KeyEvent.VK_LEFT:
				canMove(current, current.curX - 1, current.curY);
				break;
			case KeyEvent.VK_RIGHT:
				canMove(current, current.curX + 1, current.curY);
				break;
			case KeyEvent.VK_DOWN:
				drop();
				score += 5 * multiplier;
				break;
			case KeyEvent.VK_SPACE:
				int startY = current.curY;
				leSuperDrop();
				score += (topOfPiece - startY) * 10 * multiplier;
				break;
			case KeyEvent.VK_UP:
				rotate();
				break;
			}

		}
	}
}
