import java.util.ArrayList;
import java.util.List;

public class Minefield {
	private List<Square> squares;
	private int width;
	private int height;
	private int mines;
	private List<GameListener> gameListeners;
	private boolean isGameOver;
	private int flaggedMines;
	private int unrevealedSquares;
	
	
	public Minefield(int width, int height, int mines) {
		
		squares = new ArrayList<Square>(width*height);
		this.width = width;
		this.height = height;
		this.mines = mines;
		this.flaggedMines = 0;
		this.unrevealedSquares = width*height;
		this.isGameOver = false;
		this.gameListeners = new ArrayList<>();
		
		for(int i = 0; i < width*height; i++) {
			squares.add(new Square(false));
		}
		
	}
	public int getNeighborsCount(int x, int y) {
		int count = 0;
		
		for(int r = -1; r <= 1; r ++) {
			for(int c = -1; c<= 1; c++) {
				if(r == c && c == 0) {
					continue;
				}
				int neighborX = x + c;
				int neighborY = y + r;
				if((neighborX < 0 || neighborX >= width || neighborY < 0 || neighborY >= height)) {
					continue;
				}
				if(getSquare(neighborX, neighborY).isBombSquare()) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	public int getNeighborsFlagCount(int x, int y) {
		int count = 0;
		
		for(int r = -1; r <= 1; r ++) {
			for(int c = -1; c<= 1; c++) {
				if(r == c && c == 0) {
					continue;
				}
				int neighborX = x + c;
				int neighborY = y + r;
				if((neighborX < 0 || neighborX >= width || neighborY < 0 || neighborY >= height)) {
					continue;
				}
				if(getSquare(neighborX, neighborY).isFlagged()) {
					count++;
				}
			}
		}
		
		return count;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void reveal(int x, int y) {
		if(!isGameOver) {
			doReveal(x,y);
			gameUpdated();
		}
	}
	
	private void doReveal(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) {
			return;
		}
		Square square = getSquare(x,y);
		
		if(square.isRevealed() || square.isFlagged() || square.isQuestion()) {
			return;
		}
		
		square.setRevealed(true);
		unrevealedSquares--;
		//System.out.println(unrevealedSquares + "");
		
		if(square.isBombSquare()) {
			isGameOver = true;
			return;
		}
		if(getNeighborsCount(x,y) == 0) {
			for(int r = -1; r <= 1; r ++) {
				for(int c = -1; c <= 1; c++) {
					if(r != 0 || c != 0) {
						doReveal(x+r, y+c);
					}
				}
			}
		}
	}
	public void choord(int  x, int y) {
		//reveal all adjecent tiles that arent flaged reguardless of if they are bombs or not
		//getSquare(x-1,y).setFlagged(true);
		if(getNeighborsFlagCount(x,y) == getNeighborsCount(x,y)) {
			for(int r = -1; r <= 1; r++) {
				for(int c= -1; c <= 1; c ++) {
					if(r != 0 || c != 0) {
						//System.out.println("choord");
//						getSquare(x+r,y+c).setRevealed(true);
//						gameUpdated();
						doReveal(x+r,y+c);
					}
				}
			}
		}
		gameUpdated();
	}
	
	public void firstReveal(int x, int y) {
		int placedMines = 0;
		while(placedMines < mines) {
			
			int randX = (int)(Math.random()* width);
			int randY = (int)(Math.random()* height);
			Square square = squares.get(randY * width + randX);
			
			if(!square.isBombSquare() && (Math.abs(x-randX) > 1 || Math.abs(y-randY) > 1)) {
				square.setBombSquare(true);
				placedMines++;
			}
		}
		reveal(x,y);
	}
	
	public Square getSquare(int x, int y) {
		return (squares.get(y*width + x));
	}
	
	public void addGameListener(GameListener l) {
		gameListeners.add(l);
	}
	private void gameUpdated() {
		for(GameListener l : gameListeners) {
			l.update();
		}
	}
	public boolean getIsGameOver() {
		return isGameOver;
	}
	public boolean isGameWon() {
		return(unrevealedSquares == mines && !isGameOver);
	}
 	public int getFlaggedMineCount() {
		return flaggedMines;
	}
	public int getMineCount() {
		return mines;
	}
	public int getUnrevealedSquares() {
		return unrevealedSquares;
	}
	public void setGameOver(boolean state) {
		this.isGameOver = state;
	}
	public void setQuestion(int x,int y, boolean state) {
		getSquare(x,y).setQuestion(state);
		gameUpdated();
	}

	public void setFlagged(int x, int y, boolean flagged) {
		getSquare(x, y).setFlagged(flagged);
		if(flagged) {
			flaggedMines++;
		} else {
			flaggedMines--;
		}
		gameUpdated();
	}
}
