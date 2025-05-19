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
				b.setPreferredSize(new Dimension(30, 30));
				add(b);
				if(minefield.getSquare(x,y).isBombSquare()) {
					b.setText("*");
				} else {
					int count = minefield.getNeighborsCount(x,y);
					if (count > 0) {
						b.setText("" + count);
					}
				}
			}
		}
	}
}
