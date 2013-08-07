package TetrisJon;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AI extends JPanel implements ActionListener {
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
	int[] statistics;
	Clip backgroundMusic = null;
	boolean music, sound;
	int analyzeLevel = 1;
	int analyzeRuns;
	boolean analyze = false;
	int[] testPieces = { 1 };
	int testPiece;
	boolean auto = true;
	double holeAmt = -67.2967130383513;// -96.42254331216778;
	double blockadeAmt = -3.5782178537107114;// -0.20144455909150594;
	double clearAmt = 29.262742279738056;// 92.27243875284884;
	double heightAmt = -30.239290894575035;// -52.97011586972147;
	double flatAmt = 81.83680231556878;// 90.281208563613479;
	double wellAmt = 19.97089351954328;// 99.9959472448669;
	double wallAmt = 41.83356708026082;// 57.07871830039713;
	double floorAmt = -35.566057676679065;// 2;
	boolean animate = true;
	boolean marathon = true; // End game after x amount of lines?
	int endAfter = 100; // End the game after x amount of lines

	public AI() {
		board = new int[BOARD_WIDTH][BOARD_HEIGHT];
		setFocusable(true);
		if (animate) {
			delay = 100;
		} else {
			delay = 1;
		}
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
		if (animate) {
			delay = 100;
		} else {
			delay = 1;
		}
		timer.setDelay(delay);
		level = 1;
		gameOver = false;
		linesCleared = 0;
		multiplier = 1;
		topOfPiece = 0;
		statistics = new int[8];
		analyzeRuns = 0;
		testPiece = 0;
		// Thread runner = new Thread(this, "AI Thread");
		// runner.start(); // (2) Start the thread.

		fillWithEmpty();
		repaint();
		newPiece();
		if (music) {
			playBackgroundMusic("BackgroundMusic.wav");
		}
		timer.start();
		// tryMove(current, 10, 40);
	}

	public int analyzeNext(int[][] tempBoard) {

		int value = 0;
		// Check each piece for that drop
		for (int j = 1; j < 8; j++) {
			value += intelligence2(j, tempBoard);
		}
		analyzeRuns = 0;
		return value;
	}

	public double intelligence2(int identifier, int[][] tempBoard) {
		Tetri temp = new Tetri(identifier);
		temp.curX = BOARD_WIDTH / 2 - 1;
		temp.curY = 1;
		BestSpot[] spots = new BestSpot[4];
		double bestValue = Integer.MIN_VALUE;
		int yPos = 0;
		for (int i = 0; i < 4; i++) {
			spots[i] = analyzePiece(temp, tempBoard);
			if (spots[i].value > bestValue) {
				bestValue = spots[i].value;
				yPos = spots[i].yPos;
			}
			// Checks if they're equal to pick the lower one
			else if (spots[i].value == bestValue) {
				if (spots[i].yPos > yPos) {
					bestValue = spots[i].value;
					yPos = spots[i].yPos;
				}
			}
			rotate(temp);
			// System.out.println();
		}
		return bestValue;
	}

	public void intelligence() {
		Tetri temp = new Tetri(current.identifier);
		temp.curX = current.curX;
		temp.curY = current.curY;
		BestSpot[] spots = new BestSpot[4];
		double bestValue = Integer.MIN_VALUE;
		int bestPosition = 0;
		int rotateAmt = 0;
		int yPos = 0;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				temp.coords[i][j] = current.coords[i][j];
			}
		}
		// System.out.println(dropNow(temp));
		for (int i = 0; i < 4; i++) {
			spots[i] = analyzePiece(temp, board);
			rotate(temp);
		}
		for (int i = 0; i < 4; i++) {
			if (spots[i].value > bestValue) {
				bestValue = spots[i].value;
				bestPosition = spots[i].xPos;
				rotateAmt = i;
				yPos = spots[i].yPos;
			}
			// Checks if they're equal to pick the lower one
			else if (spots[i].value == bestValue) {
				if (spots[i].yPos > yPos) {
					bestValue = spots[i].value;
					bestPosition = spots[i].xPos;
					rotateAmt = i;
					yPos = spots[i].yPos;
				}
			}
			// System.out.println();
		}
		animation(rotateAmt, bestPosition);
	}

	public void animation(int rotateAmt, int xPos) {

		// The actual piece falling and whatnot
		for (int i = 0; i < rotateAmt; i++) {
			rotate(current);
		}
		while (current.curX != xPos) {
			if (current.curX > xPos) {
				canMove(current, current.curX - 1, current.curY);
			} else if (current.curX < xPos) {
				canMove(current, current.curX + 1, current.curY);
			}
		}
		if (delay < 10) {
			if (canMove(current, xPos, current.curY)) {
				leSuperDrop();
			}
		}
	}

	public void BadDropTest() {
		Tetri temp = new Tetri(current.identifier);
		temp.curX = current.curX;
		temp.curY = current.curY;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				temp.coords[i][j] = current.coords[i][j];
			}
		}
		System.out.println(dropNow(temp, board).value);

	}

	public BestSpot analyzePiece(Tetri temp, int[][] board) {
		double value = Integer.MIN_VALUE;
		int orgX = temp.curX;
		int orgY = temp.curY;
		int yPos = 0;
		int xPos = 0;
		ArrayList<ValueInfo> moves = new ArrayList<ValueInfo>();

		// Go all the way to the left
		for (int i = 0; canMove(temp, temp.curX - i, temp.curY); i++) {
			moves.add(dropNow(temp, board));
			temp.curX = orgX;
			temp.curY = orgY;
		}
		// Go all the way to the right
		for (int i = 1; canMove(temp, temp.curX + i, temp.curY); i++) {
			moves.add(dropNow(temp, board));
			temp.curX = orgX;
			temp.curY = orgY;
		}
		for (int i = 0; i < moves.size(); i++) {
			// System.out.print(moves.get(i).value + " ");

			if (moves.get(i).value > value) {
				value = moves.get(i).value;
				// System.out.println("Best Value has been updated to " +
				// value);
				yPos = moves.get(i).yPos;
				xPos = moves.get(i).xPos;

				// If they are equal, decide based on which piece will land
				// lower
			} else if (moves.get(i).value == value) {
				if (moves.get(i).yPos > yPos) {
					value = moves.get(i).value;
					// System.out.println("Best Value has been updated to "
					// + value);
					yPos = moves.get(i).yPos;
					xPos = moves.get(i).xPos;
				}
			}
		}

		// leSuperDrop();
		return new BestSpot(xPos, value, yPos);
	}

	public ValueInfo dropNow(Tetri temp, int[][] board) {
		int tempBoard[][] = new int[BOARD_WIDTH][BOARD_HEIGHT];
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				tempBoard[j][i] = board[j][i];
			}
		}

		while (canMove(temp, temp.curX, temp.curY + 1)) {
			;
		}
		for (int i = 0; i < 4; i++) {
			tempBoard[temp.curX + temp.coords[i][0]][temp.curY
					+ temp.coords[i][1]] = 10;// This is set to 10 to
												// differentiate between this
												// piece and actual fallen
												// pieces. Used for blockade
												// detection
		}
		// analyze
		int nextValue = 0;
		if (analyze) {
			if (analyzeRuns < analyzeLevel) {
				// System.out.println("analyzing...");
				analyzeRuns++;

				nextValue = analyzeNext(tempBoard);
				// System.out.println(nextValue);
			}
		}

		// All that stuff up there sets up the thing for the actual code

		double badDrop = badDrop(tempBoard, temp);
		// System.out.println("Value of badDrop is " + badDrop);

		return new ValueInfo(badDrop + nextValue, temp.curY, temp.curX);

	}

	// Returns number of pieces where underneath is a blank
	public double badDrop(int[][] tempBoard, Tetri temp) {
		double count = 0;

		// Copy of board for the remove function
		int tempBoard2[][] = new int[BOARD_WIDTH][BOARD_HEIGHT];
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				tempBoard2[j][i] = tempBoard[j][i];
			}
		}

		// ----If the piece will clear a line----
		count += clearAmt * remove(tempBoard2);

		// System.out.println("My count is now: " + count);
		for (int i = 0; i < 4; i++) {
			int tempXPos = temp.curX + temp.coords[i][0];
			int tempYPos = temp.curY + temp.coords[i][1];

			int firstBlockPos = 0; // Check if there is at least one block in
			// this column. Used for blockade
			// detection

			// ----Flat Bonus----
			// To the right
			if (tempXPos + 1 < BOARD_WIDTH) {
				if (tempBoard[tempXPos + 1][tempYPos] != 0
						&& tempBoard[tempXPos + 1][tempYPos] != 10) {
					count += flatAmt;
					// System.out.println("Flat bonus because of right");
				}
			}
			// To the left
			if (tempXPos - 1 >= 0) {
				if (tempBoard[tempXPos - 1][tempYPos] != 0
						&& tempBoard[tempXPos - 1][tempYPos] != 10) {
					count += flatAmt;
					// System.out.println("Flat bonus because of left");

				}
			}
			// To the bottom
			if (tempYPos + 1 < BOARD_HEIGHT) {
				if (tempBoard[tempXPos][tempYPos + 1] != 0
						&& tempBoard[tempXPos][tempYPos + 1] != 10) {
					count += flatAmt;
					// System.out.println("Flat bonus because of bottom");

				}
			}

			// ---Wall Check---
			if (tempXPos == 0 || tempXPos == BOARD_WIDTH - 1) {
				count += wallAmt;
				// System.out.println("Wall detected at piece " + i);
			}

			// ---Floor check---
			// If it's at the bottom restart because blockades and holes can't
			// occur also for floor check
			if (tempYPos == BOARD_HEIGHT - 1) {
				count += floorAmt;
				// / System.out.println("Floor detected at piece " + i);
				continue;
			}

			// Analyze for if there are more than one blanks below
			for (int j = 1; tempYPos + j < BOARD_HEIGHT; j++) {
				if (tempBoard[tempXPos][tempYPos + j] == 0) {
					// System.out.println("Count is now " + count);
					count += holeAmt;
					/*
					 * System.out.println("My count has been decreased by: " +
					 * holeAmt + " because of a hole");
					 */
				} else {
					break;
				}
			}

			// Find first piece in the column (Used for blockade)
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				if (tempBoard[tempXPos][j] != 0 && tempBoard[tempXPos][j] != 10) {
					firstBlockPos = j;
					break;
				}
			}
			// ----Analyze for blockades----
			// Check for repeat blockades. Redundant and inaccurate
			if (tempBoard[tempXPos][tempYPos + 1] != 10) {
				if (firstBlockPos != 0) {
					for (int j = 1; firstBlockPos + j < BOARD_HEIGHT; j++) {
						if (tempBoard[tempXPos][firstBlockPos + j] == 0) {
							count += blockadeAmt;
							// System.out.println("My count has been decreased by: "
							// + blockadeAmt + " because of a blockade");

							/*
							 * System.out.println("Blockade encountered at piece"
							 * + i + " . Current Y is " + (temp.curY +
							 * temp.coords[i][1]));
							 */
						}
					}
				}
			}

		}
		// Height penalty
		count += (BOARD_HEIGHT - temp.curY) * heightAmt;

		/*
		 * System.out .println("My count has been decreased by: " +
		 * (BOARD_HEIGHT - temp.curY) * heightAmt + " because of height");
		 */

		// ----Well detection----
		for (int i = 0; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				tempBoard2[j][i] = tempBoard[j][i];
			}
		}
		count -= wellCount(tempBoard2) * wellAmt;

		// System.out.println("Count's final value is " + count);

		return count;
	}

	public int remove(int[][] tempBoard) {

		int numRemoved = 0;
		for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
			boolean remove = true;

			// Check if they are all are filled
			for (int j = 0; j < BOARD_WIDTH; j++) {
				if (tempBoard[j][i] == 0) {
					remove = false;
					break;
				}
			}
			if (remove) {
				for (int j = 0; j < BOARD_WIDTH; j++) {
					tempBoard[j][i] = 0;

				}
				numRemoved++;
				for (int z = i; z > 0; z--) {
					for (int j = 0; j < BOARD_WIDTH; j++) {
						tempBoard[j][z] = tempBoard[j][z - 1];
					}
				}
				i++; // Needed so that the function can recheck the current line
						// which in reality is the next line up. Needed for
						// multiple line clears at the same time
			}
			// repaint();
		}
		return numRemoved;
	}

	// ---Analyzes how many wells there are on the current board---
	public int wellCount(int[][] tempBoard) {
		// Note a value of 11 means that blank has been checked.
		int count = 0;
		for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
			// ends at width-1 because those are the last rows
			for (int j = 0; j < BOARD_WIDTH - 1; j++) {
				// Check the left edge
				if (j == 1 && tempBoard[j][i] != 0 && tempBoard[0][i] == 0) {
					int holeCount = 0;
					// Going down the hole!
					for (int o = i; o >= 0; o--) {
						// if either the hole stops being a hole or the boundary
						// stops being a boundary break
						// System.out.println("At point" + (j - 1) + ", " + o
						// + " there is a " + tempBoard[j - 1][o]);

						// IF YOU WANT WELLS TO COUNT EVEN IF COVERED
						/*
						 * if (tempBoard[j - 1][o] != 0 || tempBoard[j][o] == 0)
						 * { break; }
						 */

						// Check if the well is covered. If it is it's not a
						// well!
						boolean stop = false;
						for (int m = o; m >= 0; m--) {
							if (tempBoard[j - 1][m] != 0) {
								stop = true;
								break;
							}
						}
						if (stop) {
							break;
						}

						// If the side isn't a thing
						if (tempBoard[j][o] == 0) {
							break;
						}

						tempBoard[j - 1][o] = 11;
						holeCount++;
						if (holeCount == 3) {
							count++;
							// System.out
							// .println("Well encountered at " + (j - 1));

							// Set next piece to checked as well since a l piece
							// is 4 long, but a well is only 3
							if (o > 0) {
								tempBoard[j - 1][o - 1] = 11;
							}
						}
					}

				}
				// check for wells looking to the right
				if (tempBoard[j][i] != 0 && tempBoard[j + 1][i] == 0) {
					// ===Check for wells in the middle===
					if (j < BOARD_WIDTH - 2) {
						if (tempBoard[j + 2][i] != 0) {
							int holeCount = 0;
							// Going down the hole!
							for (int o = i; o >= 0; o--) {
								/*
								 * // if either the hole stops being a hole or
								 * the // boundary stops being a boundary break
								 * if (tempBoard[j + 1][o] != 0 ||
								 * tempBoard[j][o] == 0 || tempBoard[j + 2][o]
								 * == 0) { break; }
								 */

								// Check if the well is covered. If it is it's
								// not a
								// well!
								boolean stop = false;
								for (int m = o; m >= 0; m--) {
									if (tempBoard[j + 1][m] != 0) {
										stop = true;
										break;
									}
								}
								if (stop) {
									break;
								}

								// If the side isn't a thing
								if (tempBoard[j][o] == 0
										|| tempBoard[j + 2][o] == 0) {
									break;
								}

								tempBoard[j + 1][o] = 11;

								holeCount++;
								if (holeCount == 3) {
									count++;
									// System.out.println("Well encountered at "
									// + (j + 1));

									// Set next piece to checked as well since a
									// l piece is 4 long, but a well is only 3
									if (o > 0) {
										tempBoard[j + 1][o - 1] = 11;
									}

								}
							}
						}
						j++;// Skips the next row because we know it's blank
					}
					// ===Check for wells at the right edge===
					else if (j == BOARD_WIDTH - 2) {
						int holeCount = 0;
						// Going down the hole!
						for (int o = i; o >= 0; o--) {
							/*
							 * // Same thing as above really if (tempBoard[j +
							 * 1][o] != 0 || tempBoard[j][o] == 0) { break; }
							 */

							// Check if the well is covered. If it is it's not a
							// well!
							boolean stop = false;
							for (int m = o; m >= 0; m--) {
								if (tempBoard[j + 1][m] != 0) {
									stop = true;
									break;
								}
							}
							if (stop) {
								break;
							}

							// If the side isn't a thing
							if (tempBoard[j][o] == 0) {
								break;
							}

							holeCount++;
							tempBoard[j + 1][o] = 11;

							if (holeCount == 3) {
								count++;
								// System.out.println("Well encountered at "
								// + (j + 1));
								// Set next piece to checked as well since a l
								// piece is 4 long, but a well is only 3
								if (o > 0) {
									tempBoard[j + 1][o - 1] = 11;
								}

							}
						}
					}
				}
			}
		}
		return count;
	}

	public void actionPerformed(ActionEvent e) {
		if (animate)
			drop();
		if (auto)
			intelligence();
	}

	public void getSqHeight() {
		height = getSize().getHeight();
		squareHeight = ((int) ((height - 29) / BOARD_HEIGHT)); // -29 to account
																// for status
																// bar on bottom
	}

	public void getSqWidth() {
		width = getSize().getWidth();
		squareWidth = (int) (width / BOARD_WIDTH);
	}

	public void newPiece() {

		current = new Tetri(// testPieces[testPiece]);
				// testPiece++;
				1 + (int) (Math.random() * ((7 - 1) + 1)));
		statistics[current.identifier]++;
		current.curX = BOARD_WIDTH / 2 - 1;
		current.curY = 1;
		if (!canMove(current, current.curX, current.curY)) {
			gameOver = true;
			if (music) {
				backgroundMusic.stop();
			}
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
		// System.out.println("Current x is " + current.curX);
		return true;
	}

	public void drop() {
		if (!canMove(current, current.curX, current.curY + 1)) {
			pieceDropped();
		}

	}

	public void pieceDropped() {
		if (sound) {
			playSound("Pop.wav");
		}
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

		drawPiece(g);

		// score
		g.setColor(Color.black);
		FontMetrics fm = getFontMetrics(getFont());
		int scoreWidth = fm.stringWidth("Score: " + linesCleared);
		g.drawString("Score: " + linesCleared, (int) (width - scoreWidth - 5),
				15);

		// Statusbar
		g.fillRect(0, (int) (height - 29), (int) width, 29);

		// Statusbar information
		g.setColor(Color.white);
		g.drawString("Level: " + level, 10, (int) (height - 11));
		g.drawString("Multiplier: " + multiplier + "x", (int) (width - 100),
				(int) (height - 11));

		// Gameover
		if (gameOver) {
			gameOver(g);
		}
	}

	private void drawPiece(Graphics g) {
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
		repaint();

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
				if (sound)
					playSound("Boom.wav");
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
		if (numRemoved == 1) {
			score += 40;
		} else if (numRemoved == 2) {
			score += 100;
		} else if (numRemoved == 3) {
			score += 300;
		} else if (numRemoved == 4) {
			score += 1200;

		}
		if (marathon) {
			if (linesCleared >= endAfter) {
				timer.stop();
				gameOver = true;
			}
		}
	}

	public void newLevel() {

		if (linesCleared != 0 && linesCleared % 10 == 0) {
			level++;
			multiplier += 0.5;
			// delay -= level * 10;
			// timer.setDelay(delay);
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
				// return;
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
				// score += 5 * multiplier;
				break;
			case KeyEvent.VK_SPACE:
				int startY = current.curY;
				leSuperDrop();
				// score += (topOfPiece - startY) * 10 * multiplier;
				break;
			case KeyEvent.VK_UP:
				rotate(current);
				break;
			case KeyEvent.VK_I:
				intelligence();
				break;
			case KeyEvent.VK_B:
				BadDropTest();
				break;
			case KeyEvent.VK_J:
				delay -= 10;
				if (delay < 1) {
					delay = 1;
				}
				timer.setDelay(delay);
				break;
			case KeyEvent.VK_K:
				delay += 10;
				timer.setDelay(delay);
				break;
			}

		}
	}

	public class BestSpot {
		int xPos, yPos;
		double value;

		BestSpot(int xPos, double value2, int yPos) {
			this.xPos = xPos;
			this.value = value2;
			this.yPos = yPos;
		}
	}

	// Used so that l can return both the value and y position
	public class ValueInfo {
		int yPos, xPos;
		double value;

		ValueInfo(double d, int yPos, int xPos) {
			this.yPos = yPos;
			this.value = d;
			this.xPos = xPos;
		}
	}
}
// print board
/*
 * for (int i = BOARD_HEIGHT - 1; i >= 0; i--) { for (int j = 0; j <
 * BOARD_WIDTH; j++) { System.out.print(tempBoard[j][i] + " "); }
 * System.out.println(); }
 */