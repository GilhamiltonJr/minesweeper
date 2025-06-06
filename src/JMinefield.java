import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JMinefield extends JPanel implements MouseListener{
	
	private JButton[][] buttons;
	private Minefield minefield;
	private boolean firstClick = true;
	private boolean isCrazy;
	private boolean isReduction;
	Font font;
	
	public JMinefield(Minefield minefield, int squareSize) {
		
		
		
		font = new Font("", Font.BOLD, squareSize);
		this.minefield = minefield;
		this.setLayout(new GridLayout(minefield.getHeight(), minefield.getWidth()));
		this.buttons = new JButton[minefield.getWidth()][minefield.getHeight()];
		
		for(int y = 0; y < minefield.getHeight(); y ++) {
			for(int x = 0; x < minefield.getWidth(); x++) {
				JButton b = new JButton("");
				
				b.addMouseListener(this);
				b.setPreferredSize(new Dimension(squareSize, squareSize));
				add(b);
				buttons[x][y] = b;
			}
			
		}
		updateButtons();
		minefield.addGameListener(new GameListener() {
			@Override
			public void update() {
				updateButtons();
				if(minefield.isGameWon()) {
					minefield.setGameOver(true);
					JOptionPane.showMessageDialog(JMinefield.this, "You Win!");
				}
				else if(minefield.getIsGameOver()) {
					JOptionPane.showMessageDialog(JMinefield.this, "Game Over!");
				//} else if(minefield.getFlaggedMineCount() == minefield.getMineCount()) {
				//	System.out.println("Game Won");
				}
			}
		});
	}
	private static Color getTextColorForNeighborCount(int count) {
		switch(count) {
			case 1: return(Color.decode("#0000ff"));
			case 2: return(Color.decode("#008000"));
			case 3: return(Color.decode("#808000"));
			case 4: return(Color.decode("#800080"));
			case 5: return(Color.decode("#ff0000"));
			case 6: return(Color.decode("#800000"));
			case 7: return(Color.decode("#008080"));
			default: return(Color.decode("#2b0000"));
		}
	}
	
		
	public boolean isOutOfBounds(int x, int y, Minefield minefield) {
		return(x < 0 || y < 0 || x >= minefield.getWidth() || y >= minefield.getHeight());
	}
	
	private void updateButtons() {
		for(int y = 0; y < minefield.getHeight(); y ++) {
			for(int x = 0; x < minefield.getWidth(); x++) {
				JButton b = buttons[x][y];
				int count = minefield.getNeighborsCount(x,y);
				Square square = minefield.getSquare(x, y);
				
				b.setMargin(new Insets(1, 1, 1, 1));
				b.setContentAreaFilled(true);
				b.setBorderPainted(true);
				b.setFocusPainted(false);
				b.setFont(font);
				if(!minefield.getIsGameOver()) {
					if(!square.isRevealed()) {
						if(square.isFlagged()) {
							//flag
							b.setText(">");
							b.setForeground(Color.RED);
						} else if(square.isQuestion()) {
							//question
							b.setText("?");
							b.setForeground(Color.PINK);
						} else {
							//not revealed and blank
							b.setText("");
							b.setForeground(Color.BLACK);
						}
						b.setBackground(Color.decode("#dedede"));
						b.setBorder(BorderFactory.createLineBorder(Color.decode("#cccccc"), 1));
					} else {
						b.setBackground(Color.decode("#c7c7c7"));
						b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
						
						if(count > 0) {
							//square with number
							if(isReduction) {
								b.setText("" + (count - minefield.getNeighborsFlagCount(x,y)));
							} else {
								b.setText("" + count);
							}
							//setText("" + (count - minefield.getNeighborsFlagCount(x,y)));
							//ultra top secret mode too
							
							b.setForeground(getTextColorForNeighborCount(count));
							b.setBackground(Color.decode("#c7c7c7"));
							b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
						} else {
							//blank square
							b.setText("");
							b.setEnabled(false);
						}
					}	
					//ultra hard super top secret mode
					if(isCrazy) {
						b.setText("" + (int)(Math.random()*9));
					}
					
				} else {
					if(square.isBombSquare() && !square.isFlagged()) {
						b.setText("#");
						b.setForeground(Color.BLACK);
						b.setBackground(Color.decode("#c7c7c7"));
					} else if(square.isFlagged() && !square.isBombSquare()) {
						b.setText("X");
						b.setForeground(Color.RED);
						b.setBackground(Color.decode("#ffca57"));
						b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
					}
				}
			}
			
		}
	}
	public void mousePressed(MouseEvent e) {
		
	}
	
	
	public void mouseClicked(MouseEvent e) {
		if(!minefield.getIsGameOver()) {
			
			for(int y = 0; y < minefield.getHeight(); y++) {
				for(int x = 0; x < minefield.getWidth(); x++) {
					if(buttons[x][y] == e.getSource()) {
						Square square = minefield.getSquare(x, y);
						if(e.getButton() == MouseEvent.BUTTON3/*right click */) {
							if(!square.isRevealed()) {
								if(square.isFlagged()) {
									minefield.setFlagged(x, y, false);
									minefield.setQuestion(x,y,true);
								} else if(square.isQuestion()){
									minefield.setQuestion(x,y,false);
								} else {
									minefield.setFlagged(x, y, true);
								}
							}
						} else if (e.getButton() == MouseEvent.BUTTON1){
							if(firstClick) {
								minefield.firstReveal(x, y);
								firstClick = false;
							} else if (minefield.getSquare(x, y).isRevealed() && minefield.getNeighborsCount(x, y) > 0){
								minefield.choord(x, y);
							} else {
								minefield.reveal(x, y);
							}
						}
					}
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e) {
	}
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	
	public void setCrazy(boolean state) {
		isCrazy = state;
		updateButtons();
	}
	public void setReduction(boolean state) {
		isReduction = state;
		updateButtons();
	}
	
}
