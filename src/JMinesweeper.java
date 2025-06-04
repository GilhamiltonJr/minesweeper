import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

public class JMinesweeper {
	JFrame frame;
	JPanel mainPanel;
	JMinefield minefield;
	JLabel mineCountLabel = new JLabel("");
	final Difficulty[] nextDifficulty = new Difficulty[1];
	final Gamemode[] gamemode = new Gamemode[1];
	
	public static void main(String args[]) {
		new JMinesweeper();
	}
	
	public JMinesweeper() {
		frame = new JFrame("Minesweeper");
		mainPanel = new JPanel(new BorderLayout());
		JToolBar toolBar = new JToolBar();
		toolBar.add(new AbstractAction("New Game") {
			//overrides the default action preformed in the anonymous subclass
			@Override
			public void actionPerformed(ActionEvent e) {
				newGame();
			}
		});
		JComboBox<Difficulty> difficultyBox = new JComboBox<>();
		difficultyBox.addItem(nextDifficulty[0] = new Difficulty("Beginner", 9, 9, 10, 70));
		difficultyBox.addItem(new Difficulty("Intermediate", 16, 16, 40, 60));
		difficultyBox.addItem(new Difficulty("Expert", 30, 16, 99, 50));

		difficultyBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				nextDifficulty[0] = (Difficulty) e.getItem();
				//making it final is necessary so i gotta make it a final array so you can edit the contents still :D
				newGame();
				
			}
		});
		
		JComboBox<Gamemode> gamemodeBox = new JComboBox<>();
		gamemodeBox.addItem(gamemode[0] = new Gamemode("Default", false, false));
		gamemodeBox.addItem(new Gamemode("Reduction", true, false));
		gamemodeBox.addItem(new Gamemode("Crazy", false, true));
		
		gamemodeBox.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				gamemode[0] = (Gamemode) e.getItem();
				//newGame();
				minefield.setCrazy(gamemode[0].getCrazy());
				minefield.setReduction(gamemode[0].getReduction());
			}
		});
		
		toolBar.add(difficultyBox);
		toolBar.add(gamemodeBox);
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
		Minefield m = new Minefield(nextDifficulty[0].width,nextDifficulty[0].height,nextDifficulty[0].mineCount);

		m.addGameListener(new GameListener() {
			@Override
			public void update() {
				mineCountLabel.setText("Mines Flagged: " + m.getFlaggedMineCount() + " / " + m.getMineCount());
			}
		});
		minefield = new JMinefield(m, nextDifficulty[0].dimension);//8,8,10 for easy, 20,20,38 for medium, 30,16,99 for hard
		minefield.setCrazy(gamemode[0].getCrazy());
		minefield.setReduction(gamemode[0].getReduction());
		mainPanel.add(minefield, BorderLayout.CENTER);
		mainPanel.revalidate();
		frame.pack();
	}
	
	private class Difficulty{
		
		String name;
		int width;
		int height;
		int mineCount;
		int dimension;
		
		public Difficulty(String name, int width, int height, int mineCount, int dimension){
			this.name = name;
			this.width = width;
			this.height = height;
			this.mineCount = mineCount;
			this.dimension = dimension;
		}
		
		@Override
		public String toString() {
			return String.format("%s (%dx%d, %d mines)", name, width, height, mineCount);
		}
	}
	
	private class Gamemode{
		String name;
		boolean reduction;
		boolean crazy;
		
		public Gamemode(String name, boolean reduction, boolean crazy) {
			this.name = name;
			this.reduction = reduction;
			this.crazy = crazy;
			
		}
		@Override
		public String toString() {
			return(name + " Mode");
		}
		public boolean getCrazy() {
			return crazy;
		}
		public boolean getReduction() {
			return reduction;
		}
	}
	public Gamemode getGamemode(Gamemode gamemode) {
		return gamemode;
	}
}
