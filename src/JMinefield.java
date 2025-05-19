import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class JMinefield extends JPanel{
	
	public JMinefield(Minefield minefield) {
		super(new GridLayout(minefield.getWidth(), minefield.getHeight()));
		
		for(int x = 0; x < minefield.getWidth(); x ++) {
			for(int y = 0; y < minefield.getHeight(); y++) {
				JButton b = new JButton("");
				b.setPreferredSize(new Dimension(50, 50));
				add(b);
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
}
