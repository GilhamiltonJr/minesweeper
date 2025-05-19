import java.util.ArrayList;
import java.util.List;

public class Minefield {
	private final List<Square> squares;
	private final int width;
	private final int height;
	
	public Minefield(int width, int height, int mines) {
		
		squares = new ArrayList<Square>(width*height);
		this.width = width;
		this.height = height;
		
		for(int i = 0; i < width*height; i++) {
			squares.add(new Square(false));
		}
		
		int placedMines = 0;
		while(placedMines < mines) {
			
			int randX = (int)(Math.random()* width);
			int randY = (int)(Math.random()* height);
			Square square = squares.get(randY * width + randX);
			
			if(!square.isBombSquare()) {
				square.setBombSquare(true);
				placedMines++;
			}
		}
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Square getSquare(int x, int y) {
		return (squares.get(y*width + x));
	}
}
