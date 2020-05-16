package game;

import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interface
import javax.swing.*;    // Using Swing's components and containers
import javax.swing.text.DefaultCaret;

/**
 * Custom Graphics Example: Using key/button to move a line left or right.
 */
@SuppressWarnings("serial")
public class Graph extends JFrame implements KeyListener, ActionListener {
   // Define constants for the various dimensions
   
	Map map;
	Player player;
	int numMonsters;
	public int h = 50;
	public int w = 50;
	private int offX = 50;
	private int offY = 50;
	private Point selectedSquare;
	private DrawCanvas canvas;
	JProgressBar hp;
	JProgressBar monsters;
	JButton reset;
	JButton startLevelOver;
	int controlNum = 34;
	int totalM;
	int numSquares;
	JLabel levelPlayer;
	JLabel exp;
	int monsterlevel = 37;
	int startLevelPlayer = 46;
	int startSize = controlNum;
	int initialControlNum = controlNum;
	int initialMonsterLevel = monsterlevel;
	int initialLevelStart = startLevelPlayer;
	int initialMonsterStart = monsterlevel;
 
   // Constructor to set up the GUI components and event handlers
   public Graph() {

	   player = new Player(20, 5, startLevelPlayer);
	   map = new Map(player);
	   map.populateMap(controlNum, monsterlevel);
	   numMonsters = controlNum;
	   totalM = numMonsters;
	   numSquares = 0;
	   map.setPlayerPosition();
	   int x = map.getPlayerPosition()[0];
	   int y = map.getPlayerPosition()[1];
	   Point p = new Point(x, y);
	   selectedSquare = p;
	   // Set up a panel for the buttons
	   JPanel btnPanel = new JPanel(new FlowLayout());
	   reset = new JButton("New Game");
	   reset.addActionListener(this);
	   btnPanel.add(reset);
	   hp = new JProgressBar(0, 20);
	   hp.setValue(20);
	   hp.setStringPainted(true);
	   hp.setString(Integer.toString(player.getHealth()) + "/20");
	   JLabel lab1 = new JLabel("Player Health:");
	   btnPanel.add(lab1);
	   btnPanel.add(hp);
	   int width = 12*w + 3*offX;
	   int height = 12*h + 3*offY;
	   //btnPanel.setPreferredSize(new Dimension(width, 40));
	   JLabel lab2 = new JLabel("Monsters left: ");
	   btnPanel.add(lab2);
	   monsters = new JProgressBar(0, numMonsters);
	   monsters.setValue(numMonsters);
	   monsters.setStringPainted(true);
	   monsters.setString(Integer.toString(numMonsters) + "/" + Integer.toString(totalM));
	   btnPanel.add(monsters);
	   
	   levelPlayer = new JLabel("");
	   levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
	   btnPanel.add(levelPlayer);
	   
	   startLevelOver = new JButton("Reset Level");
	   startLevelOver.addActionListener(this);
	   btnPanel.add(startLevelOver);
	   // Set up a custom drawing JPanel
	   canvas = new DrawCanvas();
	   canvas.setPreferredSize(new Dimension(width, height));
	   addKeyListener(this);
	   // Add both panels to this JFrame's content-pane
	   Container cp = getContentPane();
	   cp.setLayout(new BorderLayout());
	   cp.add(canvas, BorderLayout.CENTER);
	   cp.add(btnPanel, BorderLayout.SOUTH);
	   //setPreferredSize(new Dimension(width, height+40));
      
	   
	  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button
	  setTitle("RPG");
	  pack();           // pack all the components in the JFrame
	  setVisible(true); // show it
	  requestFocus();   // set the focus to JFrame to receive KeyEvent

   }
   
	private int getMonstersInArea(int c, int r) {
		int count = 0;
		if (c == 0) {
			if (r == 0) { //if column and row are both zero
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else if (r == 12) {
				for (int i = 0; i < 2; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				for (int i = r-1; i <=r+1; i++) {
					for (int j = 0; j < 2; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		}
		else if (c == 12) {
			if (r == 0) { //if column and row are both zero
				for (int i = 0; i < 2; i++) {
					for (int j = 11; j <= 12; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else if (r == 12) {
				for (int i = c-1; i <= c; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				for (int i = r-1; i <=r+1; i++) {
					for (int j = 11; j <= 12; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		}
		else if (r == 0){
			if (c == 0) {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			} else if (c == 12) {
				for (int i = 0; i < 2; i++) {
					for (int j = 11; j <= 12; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				for (int i = 0; i < 2; i++) {
					for (int j = c-1; j <= c+1; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		} else if (r == 12) {
			if (c == 0) {
				for (int i = 0; i < 2; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			} else if (c == 12) {
				for (int i = c-1; i <= c; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				//System.out.println("This should print here");
				for (int i = r-1; i <=r; i++) {
					for (int j = c-1; j <= c+1; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		} else {
			for (int i = r-1; i <= r+1; i++) {
				for (int j = c-1; j <= c+1; j++) {
					if(map.getMapTileAt(j, i).hasEnemyHere()) count+=map.getMapTileAt(j, i).getEnemy().getLevel();
				}
			}
			return count;
		}
		
	}
	/////////////// internal, private methods /////////////////////
	// Helper methods to translate row and column indices into pixel coordinates
	// Note: (0,0) are the coordinates of the top-left corner of the window
	// x coordinate grows to the right, so x relates to columns
	// y coordinate grows to the bottom, so y relates to rows
	// offX and offY describe offsets to account for the frame surrounding the board
	//
	/**
	 * Compute the top-left y coordinate for the current row y.
	 * Used for drawing a tile.
	 * @param r current row
	 * @return y pixel coordinate for top left corner of this row
	 */
	private int getTopLeftY(int r) {
		return r*h+offY;
	}
	/**
	 * Compute the y coordinate for the center point in this row
	 * @param r current row
	 * @return y pixel coordinate for center point of this row
	 */
	private int getCenterY(int r) {
		return r*h+(offY+h/2);
	}
	/**
	 * Compute the top-left x coordinate for the current column c.
	 * Used for drawing a tile.
	 * @param c current column
	 * @return x pixel coordinate for top left corner of this column
	 */
	private int getTopLeftX(int c) {
		return c*w+offX;
	}
	/**
	 * Compute the x coordinate for the center point in this column
	 * @param c current column
	 * @return x pixel coordinate for center point of this column
	 */
	private int getCenterX(int c) {
		return c*w+(offX+w/2);
	}
	
 
   /**
    * Define inner class DrawCanvas, which is a JPanel used for custom drawing.
    */
   class DrawCanvas extends JPanel {
      @Override
      public void paint(Graphics g) {
         super.paint(g);
         drawContentForSquares(g);
         drawLinesForGrid(g);
         highlightSelectedSquares(g);
         
      }
      
      private void drawLinesForGrid(Graphics g) {
		// user can choose to see lines or not
		for (int i=0;i<=13;i++){
			//Lines
			g.setColor(Color.black);
			g.drawLine(offX,i*h+offY,w*13+offX,i*h+offY);
			g.drawLine(i*w+offX, offY, i*w+offX, (h*13)+offY);
		}
      }
      
  	private void drawContentForSquares(Graphics g) {
		for (int r=0;r<=12;r++){
			for (int c=0;c<=12;c++){
				if (map.getMapTileAt(c, r).hasBeenVisited()) {
					if (map.getMapTileAt(c, r).hasEnemyHere()) {
						g.setColor(Color.blue);
						g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
					}
					else {
						g.setColor(Color.green);
						g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
					}
					g.setColor(Color.black);
					g.drawString(""+Integer.toString(getMonstersInArea(c, r)),getCenterX(c),getCenterY(r));
				}
				else {
					g.setColor(Color.gray);
					g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
				}
				if (player.getHealth() <= 0) {
					g.setColor(Color.red);
					g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
				}
				if (numSquares >= 169) {
					g.setColor(Color.green);
					g.fillRect(getTopLeftX(c), getTopLeftY(r), w, h);
				}
			}
		}
		if (numSquares >= 169) {
			g.setColor(Color.black);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g.drawString("Congrats! You won!", getCenterX(5), getCenterY(5));
		}
		if (numSquares >= 169) {
			goToNextLevel();
		}
  	}
  	
  	public void goToNextLevel() {
  		try {
  			Thread.sleep(2000);
  			controlNum++;
  			monsterlevel += 9;
  			System.out.println("Player stats");
  			System.out.println(player.getLevel());
  			System.out.println(player.getMaxHealth());
  			initialLevelStart = player.getLevel();
  			initialMonsterStart = monsterlevel;
  			initializeGame(player.getLevel(), monsterlevel, controlNum);
  		}
  		catch(Exception e){
  			e.printStackTrace();
  		};
  	}
    
	/**
	 * If squares are dragged, highlight selected ones with a red frame
	 * @param g the graphics object to draw on
	 */
	private void highlightSelectedSquares(Graphics g) {
		if (selectedSquare!=null){
			System.out.println("Graphics are reset");
			// paint selected square with red border
			g.setColor(Color.red);
			g.drawRect(getTopLeftX(selectedSquare.x), getTopLeftY(selectedSquare.y),w,h);
				// paint check square with red border
		}
	}
   }
 
   // The entry main() method
   public static void main(String[] args) {
      // Run GUI codes on the Event-Dispatcher Thread for thread safety
      SwingUtilities.invokeLater(new Runnable() {
         @Override
         public void run() {
            new Graph(); // Let the constructor do the job
         }
      });
   }

   public void makeMessageWindow() {
		
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new PromptWindow("Out").createWindow();
           }
		});
	}
	
	public class PromptWindow extends JFrame implements ActionListener{
		
		JButton fight;
		JButton button;
		JTextArea textArea;
		JLabel playerHp;
		JLabel monsterHp;
		MapTile tile;
		Monster mp;
		public PromptWindow(String title) {
			super(title);
			
			setSize(100, 200);
		}
		
		public void createWindow() {
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch(Exception e) {
				e.printStackTrace();
			}
			System.out.println("This code should run");
			System.out.println(h);
			tile = map.returnCurrentMapTile();
           JPanel panel = new JPanel();
           panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
           panel.setOpaque(true);
           textArea = new JTextArea(15, 50);
           textArea.setWrapStyleWord(true);
           textArea.setEditable(false);

           JScrollPane scroller = new JScrollPane(textArea);
           scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
           scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
           JPanel inputpanel = new JPanel();
           inputpanel.setLayout(new FlowLayout());
           fight = new JButton("Attack!");
           button = new JButton("Run");
           String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
           playerHp = new JLabel(currentHp);
           monsterHp = new JLabel();
           fight.addActionListener(this);
           fight.setVisible(false);
           monsterHp.setVisible(false);
           button.addActionListener(this);
           if (tile.isTreasureHere()) {
           		textArea.append("Congrats! You found treasure! Health fully restored \n");
           		player.restoreHealth();
           }
           if (tile.hasEnemyHere()) {
           	textArea.append("Found an enemy here! Prepare to fight!\n");
           	mp = tile.getEnemy();
           	fight.setVisible(true);
           	monsterHp.setVisible(true);
           	String text = Integer.toString(mp.getHealth()) + "/" + Integer.toString(mp.getMaxHealth());
           	monsterHp.setText(text);
           	
           } else {
           	textArea.setText("There's nothing here. HP restored by 1.\n");
           	player.increaseHealth();
           	
           }
           DefaultCaret caret = (DefaultCaret) textArea.getCaret();
           caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
           panel.add(scroller);
           inputpanel.add(playerHp);
           inputpanel.add(fight);
           inputpanel.add(button);
           inputpanel.add(monsterHp);
           panel.add(inputpanel);
           getContentPane().add(BorderLayout.CENTER, panel);
           pack();
           setLocationByPlatform(true);
           setVisible(true);
           setResizable(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == fight) {
				textArea.append("Fight!\n");
				player.attack(mp);
				textArea.append("Monster has taken " + Integer.toString(mp.getPreviousAttack()) + " damage!\n");
				String text = Integer.toString(mp.getHealth()) + "/" + Integer.toString(mp.getMaxHealth());
				monsterHp.setText(text);
				mp.attack(player);
				textArea.append("Player has taken " + Integer.toString(player.getPreviousAttack()) + " damage!\n");
				String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
				playerHp.setText(currentHp);
				if(mp.getHealth() <= 0 && mp.isAlive()) {
					textArea.append("Congrats! You won the fight!\n");
					textArea.append(player.incExp(mp) + "\n");
					mp.isDead();
					numMonsters--;
				}
				if(player.getHealth() <= 0) {
					textArea.append("Game Over! You're dead!\n");
					setBackground(Color.RED);
				}
           	
			}
			else if (e.getSource() == button) {
				if (tile.hasEnemyHere() && mp.isAlive()) {
					textArea.append("Cannot run from a fight!\n");
				}
				else {
					dispose();
					levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
					hp.setMaximum(player.getMaxHealth());
					hp.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
					hp.setValue(player.getHealth());
					monsters.setString(Integer.toString(numMonsters) + "/" + Integer.toString(totalM));
					monsters.setValue(numMonsters);
				}
			}
			
		}
	}   

@Override
public void keyTyped(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyPressed(KeyEvent arg0) {
	int val = arg0.getKeyCode();
	Point newPosition;
	switch (val) {
	case 87:
		if (selectedSquare.y <= 0) return;
		newPosition = new Point(selectedSquare.x, selectedSquare.y-1);
		selectedSquare = newPosition;
		map.currentPosition[1] = selectedSquare.y;
		break;
	case 83:
		if (selectedSquare.y >= 12) return;
		newPosition = new Point(selectedSquare.x, selectedSquare.y+1);
		selectedSquare = newPosition;
		map.currentPosition[1] = selectedSquare.y;
		break;
	case 65:
		if (selectedSquare.x <= 0) return;
		newPosition = new Point(selectedSquare.x -1, selectedSquare.y);
		selectedSquare = newPosition;
		map.currentPosition[0] = selectedSquare.x;
		break;
	case 68:
		if (selectedSquare.x >= 12) return;
		newPosition = new Point(selectedSquare.x +1, selectedSquare.y);
		selectedSquare = newPosition;
		map.currentPosition[0] = selectedSquare.x;
		break;
	case 10:
		MapTile tile = map.returnCurrentMapTile();
		if (!tile.hasBeenVisited()) {
			makeMessageWindow();
			tile.toggleVisited();
			numSquares++;
		}
		break;
	}
	System.out.println(val);
	//System.out.println(map.currentPosition[0] + " " + map.currentPosition[1]);
	//System.out.println(selectedSquare.x + " " + selectedSquare.y);
	canvas.repaint();
	
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void actionPerformed(ActionEvent e) {
	if (e.getSource() == reset) {
		initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum);
	}
	else if (e.getSource() == startLevelOver) {
		initializeGame(initialLevelStart, initialMonsterStart, controlNum);
	}
	
}

private void initializeGame(int playerLevel, int monsterLevel, int difficulty) {
	System.out.println("This code should run");
	player = new Player(20, 5, playerLevel);
	map = new Map(player);
	map.populateMap(difficulty, monsterLevel);
	numMonsters = difficulty;
	totalM = numMonsters;
	map.setPlayerPosition();
	int x = map.getPlayerPosition()[0];
	int y = map.getPlayerPosition()[1];
	Point p = new Point(x, y);
	selectedSquare = p;
	numSquares = 0;
	levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
	hp.setMaximum(player.getMaxHealth());
	hp.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
	hp.setValue(player.getHealth());
	monsters.setString(Integer.toString(numMonsters) + "/" + Integer.toString(totalM));
	monsters.setValue(numMonsters);
	canvas.repaint();
	requestFocus();
}
}