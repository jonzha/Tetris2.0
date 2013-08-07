package TetrisJon;

public class Tetri {
	// "NoShape", "ZShape", "SShape", "LineShape", "TShape","SquareShape",
	// "LShape", "MirroredLShape"
	int[][][] coordsTable = { { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } },
			{ { 0, -1 }, { 0, 0 }, { -1, 0 }, { -1, 1 } },
			{ { 0, -1 }, { 0, 0 }, { 1, 0 }, { 1, 1 } },
			{ { 0, -1 }, { 0, 0 }, { 0, 1 }, { 0, 2 } },
			{ { -1, 0 }, { 0, 0 }, { 1, 0 }, { 0, 1 } },
			{ { 0, 0 }, { 1, 0 }, { 0, 1 }, { 1, 1 } },
			{ { -1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } },
			{ { 1, -1 }, { 0, -1 }, { 0, 0 }, { 0, 1 } } };
	int[][] coords = new int[4][2];
	int[][] test = { { -1, 0 }, { 0, 0 }, { 0, 1 }, { 1, 1 } };
	int identifier;
	int curX;
	int curY;
	int player;
	int leftMoves, rightMoves, totalMoves;

	public Tetri(int tetri) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				coords[i][j] = coordsTable[tetri][i][j];
			}
		}
		// coords = test;
		identifier = tetri;
		if (identifier == 1) {
			leftMoves = 3;
			rightMoves = 5;
		} else if (identifier == 2) {
			leftMoves = 4;
			rightMoves = 4;
		} else if (identifier == 3) {
			leftMoves = 4;
			rightMoves = 5;
		} else if (identifier == 4) {
			leftMoves = 3;
			rightMoves = 4;
		} else if (identifier == 5) {
			leftMoves = 4;
			rightMoves = 4;
		} else if (identifier == 6) {
			leftMoves = 3;
			rightMoves = 5;
		} else if (identifier == 7) {
			leftMoves = 4;
			rightMoves = 4;
		}
		totalMoves = leftMoves + rightMoves;
	}

}