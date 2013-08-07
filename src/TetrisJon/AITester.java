package TetrisJon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AITester extends JFrame implements ActionListener {
	double[] scores = new double[20];
	AIGenetics[] population = new AIGenetics[20];
	Chromo[] currentGen = new Chromo[20];
	static Chromo[] nextGen = new Chromo[20];
	Chromo[] parents = new Chromo[2];
	double mutationChance = 0.05;
	double crossChance = 0.80;
	// ---Values for genes---//
	double rangeMin = -100;
	double rangeMax = 100;
	double badRangeMax = 10;
	double goodRangeMin = -10;
	// ---End Values---//
	int sumScores;
	int highscore = 0;
	Chromo highscoreStats, currentBestStats;
	int highscoreGeneration;
	int currentBest, currentBestScore;
	double average = 0;
	int[] testPieces = new int[1000000];
	int generations;
	JPanel info;
	boolean pause = false;
	boolean firstRun = true;
	JButton addChromo;
	static int insertedValue = -1; // -1 meaning no inserted value
	boolean marathon = true;

	public AITester() {

		// Initial Population
		testPieces = generateTestPieces();

		for (int i = 0; i < 20; i += 2) {
			if (i == 1000) {
				// Insert your own buddy here!
				population[0] = new AIGenetics(new Chromo(-195.85742797432313,
						-0.20144455909150594, 92.27243875284884,
						-70.74982869449009, 90.281208563613479,
						99.9959472448669, 57.07871830039713, 2), testPieces);
			} else {
				population[i] = new AIGenetics(new Chromo(randNum(rangeMin,
						badRangeMax), randNum(rangeMin, rangeMax), randNum(
						goodRangeMin, rangeMax),
						randNum(rangeMin, badRangeMax), randNum(goodRangeMin,
								rangeMax), randNum(rangeMin, rangeMax),
						randNum(rangeMin, rangeMax), randNum(goodRangeMin,
								rangeMax)), testPieces);
				population[i + 1] = new AIGenetics(new Chromo(randNum(rangeMin,
						badRangeMax), randNum(rangeMin, rangeMax), randNum(
						goodRangeMin, rangeMax),
						randNum(rangeMin, badRangeMax), randNum(goodRangeMin,
								rangeMax), randNum(rangeMin, rangeMax),
						randNum(rangeMin, rangeMax), randNum(goodRangeMin,
								rangeMax)), testPieces);

			}
			population[i].start();
			population[i + 1].start();
			// Come up with something
			// that'll do something when
			// gameover is true??
			while (population[i].gameOver == false
					|| population[i + 1].gameOver == false) {
				System.out.print("");
			}
			if (marathon) {

				scores[i] = population[i].score + 1;
				scores[i + 1] = population[i + 1].score + 1;

			} else {
				scores[i] = population[i].linesCleared + 1;
				scores[i + 1] = population[i + 1].linesCleared + 1;
			}
		}
		for (generations = 0; generations < 5000; generations++) {
			testPieces = generateTestPieces();
			sumScores = 0;
			System.out.print(generations);
			// System.out.print("Generation " + generations + ": ");
			for (int j = 0; j < 20; j++) {
				// System.out.print(" " + scores[j] + " ");
				sumScores += scores[j];
				// keep track of best
				if (scores[j] > highscore) {
					highscore = (int) scores[j];
					highscoreStats = new Chromo(population[j].holeAmt,
							population[j].blockadeAmt, population[j].clearAmt,
							population[j].heightAmt, population[j].flatAmt,
							population[j].wellAmt, population[j].wallAmt,
							population[j].floorAmt);
					highscoreGeneration = generations;
				}
				// Keep track of current best
				if (scores[j] > currentBestScore) {
					currentBest = j;
					currentBestScore = (int) scores[j];
					currentBestStats = new Chromo(population[j].holeAmt,
							population[j].blockadeAmt, population[j].clearAmt,
							population[j].heightAmt, population[j].flatAmt,
							population[j].wellAmt, population[j].wallAmt,
							population[j].floorAmt);
				}

				currentGen[j] = new Chromo(population[j].holeAmt,
						population[j].blockadeAmt, population[j].clearAmt,
						population[j].heightAmt, population[j].flatAmt,
						population[j].wellAmt, population[j].wallAmt,
						population[j].floorAmt);
			}
			System.out.print(".");
			// System.out.println();

			// Filling next gen
			for (int i = 0; i < 20; i++) {
				// If the user inserted their own lil buddy
				if (i == insertedValue) {
					insertedValue = -1;
					continue;
				}
				// Filling the parents set
				for (int o = 0; o < 2; o++) {
					int chosenOne = 0;
					double pick = randNum(0, sumScores);
					// selecting the right one
					for (int j = 0; j < 20; j++) {
						chosenOne += scores[j];
						if (chosenOne > pick) {
							// Makes sure the parents aren't identical
							if (o == 1) {
								if (currentGen[j].compareTo(parents[0])) {
									o--;
									break;
								}
							}
							parents[o] = currentGen[j];
							break;
						}
					}
				}

				// ----crossing over----
				if (randNum(0, 1) < crossChance) {
					nextGen[i] = new Chromo(parents[randInt()].holeAmt,
							parents[randInt()].blockadeAmt,
							parents[randInt()].clearAmt,
							parents[randInt()].heightAmt,
							parents[randInt()].flatAmt,
							parents[randInt()].wellAmt,
							parents[randInt()].wallAmt,
							parents[randInt()].floorAmt);
					// ----Mutation----
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].holeAmt = randNum(rangeMin, badRangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].blockadeAmt = randNum(rangeMin, rangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].clearAmt = randNum(goodRangeMin, rangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].heightAmt = randNum(rangeMin, badRangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].flatAmt = randNum(goodRangeMin, rangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].wellAmt = randNum(rangeMin, rangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].wallAmt = randNum(goodRangeMin, rangeMax);
					}
					if (randNum(0, 1) < mutationChance) {
						nextGen[i].floorAmt = randNum(rangeMin, rangeMax);
					}
				} else {
					nextGen[i] = parents[0].copy();
				}

				// ----Elitism----
				nextGen[0] = currentGen[currentBest];
			}
			System.out.print(".");

			display();

			for (int j = 0; j < 20; j++) {
				population[j] = new AIGenetics(nextGen[j], testPieces);
				population[j].start();
			}

			/*
			 * population[i] = new AIGenetics(nextGen[i], testPieces);
			 * population[i + 1] = new AIGenetics(nextGen[i + 1], testPieces);
			 * population[i + 2] = new AIGenetics(nextGen[i + 2], testPieces);
			 * population[i + 3] = new AIGenetics(nextGen[i + 3], testPieces);
			 * 
			 * 
			 * population[i].start(); population[i + 1].start(); population[i +
			 * 2].start(); population[i + 3].start();
			 */

			// Come up with something
			// that'll do something when
			// gameover is true??
			while (population[0].gameOver == false
					|| population[1].gameOver == false
					|| population[2].gameOver == false
					|| population[3].gameOver == false
					|| population[4].gameOver == false
					|| population[5].gameOver == false
					|| population[6].gameOver == false
					|| population[7].gameOver == false
					|| population[8].gameOver == false
					|| population[9].gameOver == false
					|| population[10].gameOver == false
					|| population[11].gameOver == false
					|| population[12].gameOver == false
					|| population[13].gameOver == false
					|| population[14].gameOver == false
					|| population[15].gameOver == false
					|| population[16].gameOver == false
					|| population[17].gameOver == false
					|| population[18].gameOver == false
					|| population[19].gameOver == false) {
				System.out.print("");
			}
			for (int j = 0; j < 20; j++) {
				if (marathon) {
					scores[j] = population[j].score + 1;

				} else {
					scores[j] = population[j].linesCleared + 1;
				}
			}

			/*
			 * scores[i] = population[i].linesCleared + 1; scores[i + 1] =
			 * population[i + 1].linesCleared + 1; scores[i + 2] = population[i
			 * + 2].linesCleared + 1; scores[i + 3] = population[i +
			 * 3].linesCleared + 1;
			 */

			System.out.print(".");
			average += sumScores;
			// Print best every now and then
			if (generations % 10 == 0) {

				System.out.println();
				System.out.println();
				System.out.println("*****CURRENT GEN: " + generations
						+ "******");
				System.out.println("-----BEST GUY SO FAR-----");
				System.out.println("Generation: " + highscoreGeneration);
				if (marathon) {
					System.out.println("Score: " + highscore);

				} else {
					System.out.println("Lines cleared: " + highscore);
				}
				System.out.println("HoleAmount: " + highscoreStats.holeAmt
						+ " Blockade amount: " + highscoreStats.blockadeAmt
						+ " Clear amount: " + highscoreStats.clearAmt
						+ " Height amount: " + highscoreStats.heightAmt
						+ " Flat amount: " + highscoreStats.flatAmt
						+ "\n Well amount: " + currentBestStats.wellAmt
						+ " Wall amount: " + currentBestStats.wallAmt
						+ " Floor amount: " + currentBestStats.floorAmt);
				System.out.println("++++ Last 10 generations ++++");
				if (marathon) {
					System.out.println("Score: " + currentBestScore);

				} else {
					System.out.println("Lines cleared: " + currentBestScore);
				}
				System.out.println("HoleAmount: " + currentBestStats.holeAmt
						+ " Blockade amount: " + currentBestStats.blockadeAmt
						+ " Clear amount: " + currentBestStats.clearAmt
						+ " Height amount: " + currentBestStats.heightAmt
						+ " Flat amount: " + currentBestStats.flatAmt
						+ "\n Well amount: " + currentBestStats.wellAmt
						+ " Wall amount: " + currentBestStats.wallAmt
						+ " Floor amount: " + currentBestStats.floorAmt);
				if (marathon) {
					System.out.println("Average score: " + average / 200);

				} else {
					System.out.println("Average lines cleared: " + average
							/ 200);
				}
				System.out.println();
				System.out.print("Generation: ");
				average = 0;
				currentBestScore = 0;

			}
		}
	}

	public double randNum(double min, double max) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}

	public int randInt() {
		return 0 + (int) (Math.random() * ((1 - 0) + 1));
	}

	public int randInt2(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public int[] generateTestPieces() {
		int[] ret = new int[1000000];
		for (int i = 0; i < 1000000; i++) {
			ret[i] = randInt2(1, 7);
		}
		return ret;
	}

	public double roundDown3(double d) {
		return Math.floor(d * 1e3) / 1e3;
	}

	public void display() {
		if (!firstRun) {
			remove(info);
		} else {
			firstRun = false;
		}
		info = new JPanel();
		info.setFocusable(true);

		JLabel staticInfo = new JLabel("Generation " + (generations - 1));
		info.add(staticInfo);
		JLabel[] chromos = new JLabel[20];
		for (int i = 0; i < 20; i++) {
			if (marathon) {
				chromos[i] = new JLabel("Chromo number: " + (i + 1)
						+ "    Hole: " + roundDown3(population[i].holeAmt)
						+ "    Blockade: "
						+ roundDown3(population[i].blockadeAmt) + "    Clear: "
						+ roundDown3(population[i].clearAmt) + "    Height: "
						+ roundDown3(population[i].heightAmt) + "    Flat: "
						+ roundDown3(population[i].flatAmt) + "    Well: "
						+ roundDown3(population[i].wellAmt) + "    Wall: "
						+ roundDown3(population[i].wallAmt) + "    Floor: "
						+ roundDown3(population[i].floorAmt) + "    Score: "
						+ population[i].score);
				info.add(chromos[i]);
			} else {
				chromos[i] = new JLabel("Chromo number: " + (i + 1)
						+ "    Hole: " + roundDown3(population[i].holeAmt)
						+ "    Blockade: "
						+ roundDown3(population[i].blockadeAmt) + "    Clear: "
						+ roundDown3(population[i].clearAmt) + "    Height: "
						+ roundDown3(population[i].heightAmt) + "    Flat: "
						+ roundDown3(population[i].flatAmt) + "    Well: "
						+ roundDown3(population[i].wellAmt) + "    Wall: "
						+ roundDown3(population[i].wallAmt) + "    Floor: "
						+ roundDown3(population[i].floorAmt) + "    Score: "
						+ population[i].linesCleared);
				info.add(chromos[i]);
			}
		}
		// info.setSize(1000, 460);
		// GUIContainer gui = new GUIContainer(nextGen);
		// gui.setSize(450, 300);
		addChromo = new JButton("Add chromo");
		addChromo.addActionListener(this);
		info.add(addChromo);
		// JPanel container = new JPanel();// new GridLayout(2, 1));

		// container.add(info);
		// container.add(gui);

		// container.add(info);
		// container.add(addChromo);
		add(info);
		// container.setSize(1070, 600);
		setSize(1070, 500);
		setTitle("Info");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		// setFocusableWindowState(false);

		// setResizable(false);
		// this.setLocationRelativeTo(null);

		setVisible(true);
		while (pause) {
			System.out.print("");
		}
	}

	public static void main(String[] args) {
		AITester game = new AITester();

		// AI game = new AI();
		// Tetris game = new Tetris();

	}

	class KeyHandler extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_P) {
				if (pause) {
					System.out.println("Paused");
				}
				pause = !pause;
			}

			switch (key) {
			case KeyEvent.VK_LEFT:
				// canMove(current, current.curX - 1, current.curY);
				break;
			}

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addChromo) {
			GUIContainer.getInstance();
		}
	}

}