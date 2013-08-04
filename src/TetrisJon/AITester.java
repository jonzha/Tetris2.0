package TetrisJon;

import java.util.Random;

import javax.swing.JFrame;

public class AITester extends JFrame {
	static double score = 0;
	static double[] scores = new double[16];
	AIGenetics[] population = new AIGenetics[16];
	Chromo[] currentGen = new Chromo[16];
	Chromo[] nextGen = new Chromo[16];
	Chromo[] parents = new Chromo[2];
	double mutationChance = 0.05;
	double crossChance = 0.16;
	int sumScores;
	int highscore = 0;
	Chromo highscoreStats, currentBestStats;
	int highscoreGeneration;
	int currentBest, currentBestScore;
	double average = 0;
	int[] testPieces = new int[5000];

	public AITester() {
		for (int i = 0; i < 16; i++) {
			testPieces = generateTestPieces();
			population[i] = new AIGenetics(new Chromo(randNum(-8, 8), randNum(
					-8, 8), randNum(-8, 8), randNum(-8, 8)), testPieces);
			population[i].start();
			// Come up with something
			// that'll do something when
			// gameover is true??
			while (population[i].gameOver == false) {
				System.out.print("");
			}
			scores[i] = population[i].linesCleared + 1;
		}
		for (int generations = 0; generations < 5000; generations++) {
			testPieces = generateTestPieces();
			sumScores = 0;
			currentBestScore = 0;
			System.out.print(generations);
			// System.out.print("Generation " + generations + ": ");
			for (int j = 0; j < 16; j++) {
				// System.out.print(" " + scores[j] + " ");
				sumScores += scores[j];
				// keep track of best
				if (scores[j] > highscore) {
					highscore = (int) scores[j];
					highscoreStats = new Chromo(population[j].holeAmt,
							population[j].blockadeAmt, population[j].clearAmt,
							population[j].heightAmt);
					highscoreGeneration = generations;
				}
				// Keep track of current best
				if (scores[j] > currentBestScore) {
					currentBest = j;
					currentBestScore = (int) scores[j];
					currentBestStats = new Chromo(population[j].holeAmt,
							population[j].blockadeAmt, population[j].clearAmt,
							population[j].heightAmt);
				}

				currentGen[j] = new Chromo(population[j].holeAmt,
						population[j].blockadeAmt, population[j].clearAmt,
						population[j].heightAmt);
			}
			System.out.print(".");
			// System.out.println();

			// Filling next gen
			for (int i = 0; i < 16; i++) {
				// Filling the parents set
				for (int o = 0; o < 2; o++) {
					int chosenOne = 0;
					double pick = randNum(0, sumScores);
					// selecting the right one
					for (int j = 0; j < 16; j++) {
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
				// crossing over
				if (randNum(0, 1) < crossChance) {
					nextGen[i] = new Chromo(parents[randInt()].holeAmt,
							parents[randInt()].blockadeAmt,
							parents[randInt()].clearAmt,
							parents[randInt()].heightAmt);
				} else {
					nextGen[i] = parents[0].copy();
				}
				// Mutation
				if (randNum(0, 1) < mutationChance) {
					int gene = randInt2(1, 4);
					if (gene == 1) {
						nextGen[i].blockadeAmt += randNum(-2, 2);
					} else if (gene == 2) {
						nextGen[i].holeAmt += randNum(-2, 2);
					} else if (gene == 3) {
						nextGen[i].clearAmt += randNum(-2, 2);
					} else if (gene == 4) {
						nextGen[i].heightAmt += randNum(-2, 2);
					}
				}
				nextGen[0] = currentGen[currentBest];
			}
			System.out.print(".");

			for (int i = 0; i < 16; i++) {
				population[i] = new AIGenetics(nextGen[i], testPieces);
				population[i].start();
				// Come up with something
				// that'll do something when
				// gameover is true??
				while (population[i].gameOver == false) {
					System.out.print("");
				}
				scores[i] = population[i].linesCleared + 1;
			}
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
				System.out.println("Lines cleared: " + highscore);
				System.out.println("HoleAmount: " + highscoreStats.holeAmt
						+ " Blockade amount: " + highscoreStats.blockadeAmt
						+ " Clear amount: " + highscoreStats.clearAmt
						+ " Height amount: " + highscoreStats.heightAmt);
				System.out.println("++++ Last 10 generations ++++");
				System.out.println("Lines cleared: " + currentBestScore);
				System.out.println("HoleAmount: " + currentBestStats.holeAmt
						+ " Blockade amount: " + currentBestStats.blockadeAmt
						+ " Clear amount: " + currentBestStats.clearAmt
						+ " Height amount: " + currentBestStats.heightAmt);
				System.out.println("Average lines cleared: " + average / 160);
				System.out.println();
				System.out.print("Generation: ");
				average = 0;

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
		int[] ret = new int[5000];
		for (int i = 0; i < 5000; i++) {
			ret[i] = randInt2(1, 7);
		}
		return ret;
	}

	public static void main(String[] args) {
		AITester game = new AITester();
		// AI game = new AI();
		// Tetris game = new Tetris();
		// game.setLocationRelativeTo(null);
		// game.setVisible(true);

	}

	public class Chromo {
		double holeAmt, blockadeAmt, clearAmt, heightAmt;

		Chromo(double holeAmt, double blockadeAmt, double clearAmt,
				double heightAmt) {
			this.holeAmt = holeAmt;
			this.blockadeAmt = blockadeAmt;
			this.clearAmt = clearAmt;
			this.heightAmt = heightAmt;
		}

		public boolean compareTo(Chromo o) {
			if (this.holeAmt != o.holeAmt) {
				return false;
			} else if (this.blockadeAmt != o.blockadeAmt) {
				return false;
			} else if (this.clearAmt != o.clearAmt) {
				return false;
			} else if (this.heightAmt != o.heightAmt) {
				return false;
			}
			return true;
		}

		public Chromo copy() {
			return new Chromo(holeAmt, blockadeAmt, clearAmt, heightAmt);
		}
	}

}