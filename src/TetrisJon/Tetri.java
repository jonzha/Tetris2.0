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
	int x;
	int y;
	int dx;
	int dy;
	int identifier;
	int curX;
	int curY;
	int player;

	public Tetri(int tetri) {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				coords[i][j] = coordsTable[tetri][i][j];
			}
		}
		// coords = test;
		identifier = tetri;
	}

	public void setdX(int dx) {
		this.dx = dx;
	}

	public void setdY(int dy) {
		this.dy = dy;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}