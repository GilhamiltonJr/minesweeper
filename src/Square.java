
public class Square {
	
	private boolean bombSquare;
	private boolean isRevealed;
	private boolean isFlagged;
	private boolean isQuestion;
	
	private int adjacentBombs;
	
	public Square(boolean bombSquare) {
		this.bombSquare = bombSquare;
	}
	
	public boolean isQuestion() {
		return isQuestion;
	}
	public void setQuestion(boolean state) {
		this.isQuestion = state;
	}

	public boolean isBombSquare() {
		return bombSquare;
	}

	public void setBombSquare(boolean bombSquare) {
		this.bombSquare = bombSquare;
	}

	public boolean isRevealed() {
		return isRevealed;
	}

	public void setRevealed(boolean isRevealed) {
		this.isRevealed = isRevealed;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public int getAdjacentBombs() {
		return adjacentBombs;
	}

	public void setAdjacentBombs(int adjacentBombs) {
		this.adjacentBombs = adjacentBombs;
	}
	
}
