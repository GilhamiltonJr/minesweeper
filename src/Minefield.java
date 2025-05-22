import java.util.ArrayList;
import java.util.List;

public class Minefield {
	private final List<Square> squares;
	private final int width;
	private final int height;
	private final int mines;
	private final List<GameListener> gameListeners;
	
	public Minefield(int width, int height, int mines) {
		
		squares = new ArrayList<Square>(width*height);
		this.width = width;
		this.height = height;
		this.mines = mines;
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
		doReveal(x,y);
		gameUpdated();
	}
	
	private void doReveal(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) {
			return;
		}
		Square square = getSquare(x,y);
		
		if(square.isRevealed() || square.isFlagged()) {
			return;
		}
		
		square.setRevealed(true);
		
		if(square.isBombSquare()) {
			//TODO game over
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
						//getSquare(x,y).setRevealed(true);
						//gameUpdated();
					}
				}
			}
		}
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
}
