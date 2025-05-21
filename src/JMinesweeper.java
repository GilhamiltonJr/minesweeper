import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class JMinesweeper {
	JPanel mainPanel;
	JMinefield minefield;
	
	public static void main(String args[]) {
		new JMinesweeper();
	}
	
	public JMinesweeper() {
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			//UIManager.setLookAndFeel("");
		} catch (Exception ignored) {
			//
		}
		JFrame frame = new JFrame("Minesweeper");
		mainPanel = new JPanel(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.add(new AbstractAction("New Game") {
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		mainPanel.add(toolBar, BorderLayout.NORTH);
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
		minefield = new JMinefield(new Minefield(16,16,40));
		mainPanel.add(minefield, BorderLayout.CENTER);
		mainPanel.revalidate();
	}
}
