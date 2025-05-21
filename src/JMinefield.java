import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JMinefield extends JPanel{
	
	private final JButton[][] buttons;
	private final Minefield minefield;
	private final boolean[] firstClick = new boolean[] {true};
	
	public JMinefield(Minefield minefield) {
		this.minefield = minefield;
		this.setLayout(new GridLayout(minefield.getHeight(), minefield.getWidth()));
		this.buttons = new JButton[minefield.getWidth()][minefield.getHeight()];
		
		for(int y = 0; y < minefield.getHeight(); y ++) {
			for(int x = 0; x < minefield.getWidth(); x++) {
				final int finalX = x;
				final int finalY = y;
				JButton b = new JButton("");
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(firstClick[0]) {
							minefield.firstReveal(finalX, finalY);
							firstClick[0] = false;
						} else {
							minefield.reveal(finalX, finalY);
						}
					}});
				b.setPreferredSize(new Dimension(50, 50));
				add(b);
				buttons[x][y] = b;
			}
			
		}
		updateButtons();
		minefield.addGameListener(new GameListener() {
			@Override
			public void update() {
				updateButtons();
			}
		});
//		for(int y = 0; y < minefield.getHeight(); y ++) {
//			for(int x = 0; x < minefield.getWidth(); x++) {
//				if(minefield.getSquare(x,y).isRevealed()) {
//					System.out.print(minefield.getNeighborsCount(x, y) +" ");
//				} else {
//					System.out.print("  ");
//				}
//			}
//			System.out.println();
//		}
		
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
				if(minefield.getSquare(x, y).isRevealed()) {
					if(minefield.getNeighborsCount(x, y) == 0 && !minefield.getSquare(x, y).isBombSquare()) {
						b.setEnabled(false);
					}
						
					if(minefield.getSquare(x,y).isBombSquare()) {
						b.setText("*");
						b.setForeground(Color.RED);
					} else {
						int count = minefield.getNeighborsCount(x,y);
						if (count > 0) {
							b.setText("" + count);
							b.setForeground(getTextColorForNeighborCount(count));
						}
					}
				}
			}
		}
	}
}
