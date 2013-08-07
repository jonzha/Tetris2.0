package TetrisJon;

public class AIFaceOff {
	int[] scores1 = new int[100];
	int[] scores2 = new int[100];
	int[] testPieces = new int[1000000];
	int[] testPieces2 = { 1 };
	double sum1 = 0;
	double sum2 = 0;
	double average1;
	double average2;

	public AIFaceOff() {
		for (int i = 0; i < 100; i++) {
			testPieces = generateTestPieces();
			AIGenetics one = new AIGenetics(new Chromo(-96.42254331216778,
					-0.20144455909150594, 92.27243875284884,
					-52.97011586972147, 90.281208563613479, 99.9959472448669,
					57.07871830039713, 2), testPieces);
			AIGenetics two = new AIGenetics(new Chromo(-195.85742797432313,
					-0.20144455909150594, 92.27243875284884,
					-52.97011586972147, 90.281208563613479, 99.9959472448669,
					57.07871830039713, 2), testPieces);

			one.start();
			two.start();
			// Come up with something
			// that'll do something when
			// gameover is true??
			while (one.gameOver == false || two.gameOver == false) {
				System.out.print("");
			}
			scores1[i] = one.linesCleared;
			scores2[i] = two.linesCleared;
			sum1 += one.linesCleared;
			sum2 += two.linesCleared;
			System.out.println("Round " + i + " finished");
			System.out.println("One scored " + one.linesCleared);
			System.out.println("Two Scored " + two.linesCleared);
		}
		average1 = sum1 / 100;
		average2 = sum2 / 100;
		System.out.println("Average of 1 " + average1);
		System.out.println("Average of 2 " + average2);
	}

	public int[] generateTestPieces() {
		int[] ret = new int[1000000];
		for (int i = 0; i < 1000000; i++) {
			ret[i] = randInt2(1, 7);
		}
		return ret;
	}

	public int randInt2(int min, int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

	public static void main(String[] args) {
		new AIFaceOff();
	}
}
