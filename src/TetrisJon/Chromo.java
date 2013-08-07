package TetrisJon;


public class Chromo {
	double holeAmt, blockadeAmt, clearAmt, heightAmt, flatAmt, wellAmt,
			wallAmt, floorAmt;

	public Chromo(double holeAmt, double blockadeAmt, double clearAmt,
			double heightAmt, double flatAmt, double wellAmt, double wallAmt,
			double floorAmt) {
		this.holeAmt = holeAmt;
		this.blockadeAmt = blockadeAmt;
		this.clearAmt = clearAmt;
		this.heightAmt = heightAmt;
		this.flatAmt = flatAmt;
		this.wellAmt = wellAmt;
		this.wallAmt = wallAmt;
		this.floorAmt = floorAmt;
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
		} else if (this.flatAmt != o.flatAmt) {
			return false;
		} else if (this.wellAmt != o.wellAmt) {
			return false;
		} else if (this.wallAmt != o.wallAmt) {
			return false;
		} else if (this.floorAmt != o.floorAmt) {
			return false;
		}
		return true;
	}

	public Chromo copy() {
		return new Chromo(holeAmt, blockadeAmt, clearAmt, heightAmt, flatAmt,
				wellAmt, wallAmt, floorAmt);
	}

}
