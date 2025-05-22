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
	
	private final JButton[][] buttons;
	private final Minefield minefield;
	private final boolean[] firstClick = new boolean[] {true};
	Font font = new Font("Monospaced", Font.BOLD, 20);
	
	public JMinefield(Minefield minefield) {
		this.minefield = minefield;
		this.font = font;
		this.setLayout(new GridLayout(minefield.getHeight(), minefield.getWidth()));
		this.buttons = new JButton[minefield.getWidth()][minefield.getHeight()];
		
		for(int y = 0; y < minefield.getHeight(); y ++) {
			for(int x = 0; x < minefield.getWidth(); x++) {
				JButton b = new JButton("");
//				b.addActionListener(new ActionListener() {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						if(firstClick[0]) {
//							minefield.firstReveal(finalX, finalY);
//							firstClick[0] = false;
//						} else if (minefield.getSquare(finalX, finalY).isRevealed() && minefield.getNeighborsCount(finalX, finalY) > 0){
//							minefield.choord(finalX, finalY);
//						} else {
//							minefield.reveal(finalX, finalY);
//						}
//					}});
				
				b.addMouseListener(this);
				b.setPreferredSize(new Dimension(20, 20));
				add(b);
				buttons[x][y] = b;
			}
			
		}
		updateButtons();
		minefield.addGameListener(new GameListener() {
			@Override
			public void update() {
				updateButtons();
				if(minefield.getIsGameOver()) {
					JOptionPane.showMessageDialog(null, "Game Over!");
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
				if(minefield.getSquare(x, y).isFlagged()) {
					b.setText(">");
					b.setForeground(Color.RED);
					b.setBackground(Color.decode("#c7c7c7"));
					b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
				} else {
					b.setText("");
					b.setBackground(Color.decode("#d3d3d3"));
					b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
				}
				if(minefield.getSquare(x, y).isRevealed()) {
					if(minefield.getNeighborsCount(x, y) == 0 && !minefield.getSquare(x, y).isBombSquare()) {
						b.setEnabled(false);
						b.setBackground(Color.decode("#c7c7c7"));
						b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
					}
					if(minefield.getSquare(x,y).isBombSquare()) { // BOMB
						b.setText("#");
						b.setForeground(Color.BLACK);
						b.setBackground(Color.decode("#c7c7c7"));
						b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
						//end game
					} else if (count > 0){                                    //NOT BOMB
						b.setText("" + count);
						b.setForeground(getTextColorForNeighborCount(count));
						b.setBackground(Color.decode("#c7c7c7"));
						b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
					}
					
					
					
				} else {
					b.setBackground(Color.decode("#d3d3d3"));
					b.setBorder(BorderFactory.createLineBorder(Color.decode("#e0e0e0"), 1));
				}
				
				b.setMargin( new Insets(1, 1, 1, 1) );
				b.setFont(font);
				b.setContentAreaFilled(true);
				b.setBorderPainted(true);
				b.setFocusPainted(false);
				
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
								square.setFlagged(!square.isFlagged());
								updateButtons();
							}
						} else if (e.getButton() == MouseEvent.BUTTON1){
							if(firstClick[0]) {
								minefield.firstReveal(x, y);
								firstClick[0] = false;
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
	
}
