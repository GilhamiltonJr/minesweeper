package com.github.gilhamiltonjr.minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Minefield {
	private List<Square> squares;
	private int width;
	private int height;
	private final int desiredMineCount;
	private int mines;
	private List<GameListener> gameListeners;
	private boolean isGameOver;
	private int flaggedMines;
	private int unrevealedSquares;
	
	public record Coordinate(int x, int y) {}

	public Minefield(int width, int height, int mines) {

		squares = new ArrayList<>(width * height);
		this.width = width;
		this.height = height;
		this.desiredMineCount = mines;
		this.mines = 0;
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
			for (Coordinate neighbor : neighbors(x, y)) {
				doReveal(neighbor.x, neighbor.y);
			}
		}
		gameUpdated();
	}

	private int xyToIndex(int x, int y) {
		return y * width + x;
	}

	public void firstReveal(int x, int y) {
		while(mines < desiredMineCount) {
			int randX = (int)(Math.random()* width);
			int randY = (int)(Math.random()* height);
			Square square = squares.get(xyToIndex(randX, randY));

			if(!square.isBombSquare() && (Math.abs(x-randX) > 1 || Math.abs(y-randY) > 1)) {
				placeMine(randX, randY);
			}
		}
		reveal(x,y);
	}

	public void placeMine(Coordinate coord) {
		placeMine(coord.x, coord.y);
	}

	public void placeMine(int x, int y) {
		Square square = squares.get(xyToIndex(x, y));
		if (square.isBombSquare()) {
			return;
		}
		square.setBombSquare(true);
		mines++;
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
	public void setGameOver(boolean state) {
		this.isGameOver = state;
	}
	public void setQuestion(int x, int y, boolean state) {
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

	public List<Coordinate> getMinimumClicksToSolve() {
		var clicks = new ArrayList<Coordinate>();
		boolean[] marked = new boolean[squares.size()];

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int index = xyToIndex(x, y);
				if (squares.get(index).isBombSquare() || marked[index] || getNeighborsCount(x, y) != 0) {
					continue;
				}

				marked[index] = true;
				clicks.add(new Coordinate(x, y));
				floodFillAndMark(x, y, marked);
			}
		}

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int index = xyToIndex(x, y);
				if (!marked[index] && !squares.get(index).isBombSquare()) {
					clicks.add(new Coordinate(x, y));
				}
			}
		}

		return clicks;
	}

	private void floodFillAndMark(int x, int y, boolean[] marked) {
		for (Coordinate neighbor : neighbors(x, y)) {
			int index = xyToIndex(neighbor.x, neighbor.y);
			if (marked[index]) {
				continue;
			}

			marked[index] = true;
			if (!squares.get(index).isBombSquare() && getNeighborsCount(neighbor.x, neighbor.y) == 0) {
				floodFillAndMark(neighbor.x, neighbor.y, marked);
			}
		}
	}

	private Iterable<Coordinate> neighbors(int x, int y) {
		var neighbors = new ArrayList<Coordinate>(8);
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				if (r != c || c != 0) {
					int newX = x + r;
					int newY = y + c;
					if (newX >= 0 && newX < width && newY >= 0 && newY< height) {
						neighbors.add(new Coordinate(x + r, y + c));
					}
				}
			}
		}
		return neighbors;
	}
}
