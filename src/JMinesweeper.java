import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class JMinesweeper {
	public static void main(String args[]) {
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
			//UIManager.setLookAndFeel("");
		} catch (Exception ignored) {
			//
		}
		JFrame frame = new JFrame("Minesweeper");
		JPanel mainPanel = new JPanel(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.add(new AbstractAction("New Game") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("new game pressed");
				
			}
		});
		mainPanel.add(toolBar, BorderLayout.NORTH);
		
		mainPanel.add(new JMinefield(new Minefield(16,16,40)), BorderLayout.CENTER);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
