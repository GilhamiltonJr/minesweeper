import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

public class JMinesweeper {
	JPanel mainPanel;
	JMinefield minefield;
	JLabel mineCountLabel = new JLabel("");
	
	public static void main(String args[]) {
		new JMinesweeper();
	}
	
	public JMinesweeper() {
		JFrame frame = new JFrame("Minesweeper");
		mainPanel = new JPanel(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.add(new AbstractAction("New Game") {
			
			//overrides the default action preformed in the anonymous subclass
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		toolBar.add(mineCountLabel);
		mainPanel.add(toolBar, BorderLayout.NORTH);
		toolBar.setFloatable(false);
		newGame();
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	public void newGame() {
		if(minefield != null) {
			mainPanel.remove(minefield);
		}
		Minefield m = new Minefield(30,16,2);
		m.addGameListener(new GameListener() {
			@Override
			public void update() {
				mineCountLabel.setText("Mines Flagged: " + m.getFlaggedMineCount() + " / " + m.getMineCount());
			}
		});
		minefield = new JMinefield(m, 40);//8,8,10 for easy, 20,20,38 for medium, 30,16,99 for hard
		mainPanel.add(minefield, BorderLayout.CENTER);
		mainPanel.revalidate();
	}
}
