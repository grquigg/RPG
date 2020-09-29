package game;

import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interface
import java.io.File;

import javax.swing.*;    // Using Swing's components and containers
import javax.swing.text.DefaultCaret;

/**
 * Custom Graphics Example: Using key/button to move a line left or right.
 */
@SuppressWarnings("serial")


public class Graph extends JFrame implements KeyListener, ActionListener {
   // Define constants for the various dimensions
	
	public enum State {InfoState, PlayingState};
	
	Map map; //the map
	Player player;
	public int h = 45;
	public int w = 45;
	private int offX = 50;
	private int offY = 30;
	State screenState;
	private Point selectedSquare;
	private DrawCanvas canvas;
	JProgressBar hpMeter;
	JProgressBar monstersMeter;
	JButton resetButton = new JButton("New Game");
	JButton startLevelOverButton = new JButton("Reset Level");
	JButton prevLevelButton = new JButton("Previous Level");
	JButton saveGameButton = new JButton("Save Game");
	JButton loadGameButton = new JButton("Load Game");
	JButton startGame = new JButton("Start Game!");
	JButton next = new JButton("Next Card");
	SaveFileWriter fs;
	int controlNum = 30; //the number of monsters for level and the difficulty of the map
	int numSquares = 0;
	JLabel levelPlayer;
	JLabel exp;
	JLabel resets;
	JLabel levelNum = new JLabel("Level #1");
	boolean isFirstLoad = true;
	boolean hasSaveGameBeenClicked = false;
	int numResets = 3;
	int monsterLevel = 0;
	int initialPlayerXP = 0;
	int startLevelPlayer = 1;
	int startSize = controlNum;
	int initialControlNum = controlNum;
	int initialMonsterLevel = monsterLevel;
	int initialLevelStart = startLevelPlayer;
	int initialMonsterStart = monsterLevel;
	int baseExpForLevel = 0;
	int numMonstersLeft = controlNum;
	JPanel mainPanel;
	JPanel cards = new JPanel(new CardLayout());
	String cardLabel1 = "Demo Card 1";
	JPanel card1 = new JPanel(new FlowLayout());
	JLabel cardString1 = new JLabel(cardLabel1);
	String cardLabel2 = "Demo Card 2";
	JPanel card2 = new JPanel(new FlowLayout());
	JLabel cardString2 = new JLabel(cardLabel2);
	String cardLabel3 = "Demo Card 3";
	JPanel card3 = new JPanel(new FlowLayout());
	JLabel cardString3 = new JLabel(cardLabel3);
	
 
   // Constructor to set up the GUI components and event handlers
   public Graph() {
	   playingStateSetup();
	   initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
	   isFirstLoad = true;
	   File file = new File("game.xml");
	   if(!file.exists()) {
		   loadGameButton.setEnabled(false);
	   }
//		   screenState = State.PlayingState;
//		   System.out.println("true");
//	   } else {
//		   screenState = State.InfoState;
//		   isFirstLoad = false;
//		   loadGameButton.setEnabled(false);
//	   }
//	   if(screenState == State.PlayingState) {
//		   playingStateSetup();
//		   initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
//	   }
//	   else if (screenState == State.InfoState) {
//		   infoStateSetup();
//	   }

   }
   
   private void infoStateSetup() {
	   mainPanel = new JPanel(new FlowLayout());
	   JLabel title = new JLabel("Welcome!");
	   title.setFont(new Font("Serif", Font.PLAIN, 30));
	   mainPanel.add(title);
	   card1.add(cardString1);
	   card2.add(cardString2);
	   card3.add(cardString3);
	   cards.add(card1, cardLabel1);
	   cards.add(card2, cardLabel2);
	   cards.add(card3, cardLabel3);
	   mainPanel.add(cards);
	   mainPanel.add(startGame);
	   mainPanel.add(next);
	   startGame.addActionListener(this);
	   next.addActionListener(this);
	   Container cp = getContentPane();
	   cp.add(mainPanel);
	   
	    // Handle the CLOSE button
	   setTitle("RPG");
	   
	   pack();           // pack all the components in the JFrame
	   setVisible(true); // show it
	   requestFocus();   // set the focus to JFrame to receive KeyEvent
	
   }

   private void playingStateSetup() {
	   JPanel bottomPanel = new JPanel(new FlowLayout());
	   resetButton.addActionListener(this);
	   JLabel healthPlayer = new JLabel("Player Health:");
	   int width = 12*w + 3*offX;
	   int height = 12*h + 3*offY;
	   JLabel leftMonsters = new JLabel("Monsters left: ");
	   bottomPanel.add(levelNum);
	   bottomPanel.add(leftMonsters);
	   monstersMeter = new JProgressBar(0, numMonstersLeft);
	   monstersMeter.setStringPainted(true);
	   bottomPanel.add(monstersMeter);
	   
	   
	   startLevelOverButton.addActionListener(this);
	   saveGameButton.addActionListener(this);
	   loadGameButton.addActionListener(this);
	   prevLevelButton.addActionListener(this);
	   
	   canvas = new DrawCanvas();
	   canvas.setPreferredSize(new Dimension(width, height));
	   addKeyListener(this);
	   // Add both panels to this JFrame's content-pane
	   
	   hpMeter = new JProgressBar(0, 20);
	   hpMeter.setStringPainted(true);
	   bottomPanel.add(healthPlayer);
	   bottomPanel.add(hpMeter);
	   
	   levelPlayer = new JLabel("");
	   bottomPanel.add(levelPlayer);
	  
	   
	   JPanel topPanel = new JPanel(new FlowLayout());
	   resets = new JLabel("");
	   resets.setText("Resets left " + Integer.toString(numResets) + "/3");
	   topPanel.add(levelNum);
	   topPanel.add(saveGameButton);
	   topPanel.add(loadGameButton);
	   topPanel.add(resetButton);
	   topPanel.add(resets);
	   topPanel.add(prevLevelButton);
	   topPanel.add(startLevelOverButton);
	   
	   Container cp = getContentPane();
	   cp.setLayout(new GridBagLayout());
	   GridBagConstraints gc = new GridBagConstraints();
	   gc.fill = GridBagConstraints.HORIZONTAL;
	   gc.gridx = 0;
	   gc.gridy = 0;
	   cp.add(topPanel, gc);
	   gc.gridy = 1;
	   cp.add(canvas, gc);
	   gc.gridy = 2;
	   cp.add(bottomPanel, gc);

	   //setPreferredSize(new Dimension(width, height+40));
	   fs = new SaveFileWriter("game.xml");
	   setDefaultCloseOperation(EXIT_ON_CLOSE); // Handle the CLOSE button
	   setTitle("RPG");
	   pack();           // pack all the components in the JFrame
	   setVisible(true); // show it
	   requestFocus();   // set the focus to JFrame to receive KeyEvent
   }
   
   private void getNumMonsters() {
	   int count = 0;
	   for (int i = 0; i < 13; i++) {
		   for (int j = 0; j < 13; j++) {
			   if(map.getMapTileAt(i, j).hasEnemyHere() && map.getMapTileAt(i, j).getEnemy().isAlive()) {
				   count++;
			   }
		   }
	   }
	   numMonstersLeft = count;
	   System.out.println(numMonstersLeft);
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
				for (int j = 0; j < 2; j++) {
					for (int i = r-1; i <= r; i++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) {
							count+=map.getMapTileAt(j, i).getEnemy().getLevel();
						}
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
//    	g.setColor(Color.black);
//		g.drawLine(offX, offY, w*13+offX, offY);
//		g.drawLine(offX, offY, offX, (h*13)+offY);
//		g.drawLine(offX, 13*h+offY, w*13+offX, 13*h+offY);
//		g.drawLine(13*w+offX, offY, 13*w+offX, (h*13)+offY);
      }
      
  	private void drawContentForSquares(Graphics g) {
		for (int r=0;r<=12;r++){
			for (int c=0;c<=12;c++){
				g.setFont(new Font("TimesRoman", Font.PLAIN, 12));
				if (map.getMapTileAt(c, r).hasBeenMarked()) {
					g.setColor(Color.white);
					g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
				}
				else if (map.getMapTileAt(c, r).hasBeenVisited()) {
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
					g.setColor(Color.black);
					g.setFont(new Font("TimesRoman", Font.PLAIN, 49));
					g.drawString("Sorry! You lost!", getCenterX(3), getCenterY(6));
					g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
					g.drawString("Press the LOAD GAME button to try again", getCenterX(2), getCenterY(7));
					Graph.this.confineUser();
				}
				if (numSquares - (controlNum - numMonstersLeft) >= 169 - controlNum) {
					g.setColor(Color.green);
					g.fillRect(getTopLeftX(c), getTopLeftY(r), w, h);
				}
			}
		}
		if (numSquares - (controlNum - numMonstersLeft) >= 169 - controlNum && player.getHealth() > 0) { //if the number of squares visited - num
			g.setColor(Color.black);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 49));
			g.drawString("Congrats! You won!", getCenterX(2), getCenterY(6));
			g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
			g.drawString("Press SPACE to continue", getTopLeftX(4), getCenterY(7));
			
			
		}

  	}
  	
  	public void goToNextLevel() {
  		try {
  			Thread.sleep(2000);
  			int lPlayerLevel = player.getLevel();
  			if(controlNum == numMonstersLeft) {
  				lPlayerLevel+=controlNum;
  			}
  			controlNum++;
  			monsterLevel += 8;
  			initialLevelStart = player.getLevel();
  			initialMonsterStart = monsterLevel;
  			baseExpForLevel = player.getExp();
  			//System.out.println("Base exp for level " + baseExpForLevel);
  			initializeGame(lPlayerLevel, monsterLevel, controlNum, baseExpForLevel);
  			numResets = 3;
  			resets.setText("Resets left " + Integer.toString(numResets) + "/3");
  		}
  		catch(Exception e){
  			e.printStackTrace();
  		};
  	}
    
	private void highlightSelectedSquares(Graphics g) {
		if (selectedSquare!=null && player.getHealth() > 0){
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
   
   public void defaultCloseOperationCallback() {
	   setDefaultCloseOperation(EXIT_ON_CLOSE);
   }
   
   public void confineUser() {
	   setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
   }
   public void makeMessageWindow() {
	   	setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new PromptWindow("Output").createWindow();
           }
		});
	}
	
	public class PromptWindow extends JFrame implements ActionListener{
		
		JButton fight;
		JButton button;
		JButton ult;
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
           ult = new JButton("Ultimate");
           button = new JButton("Run");
           String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
           playerHp = new JLabel(currentHp);
           monsterHp = new JLabel();
           fight.addActionListener(this);
           fight.setVisible(false);
           monsterHp.setVisible(false);
           ult.addActionListener(this);
           ult.setVisible(false);
           button.addActionListener(this);
           if (tile.isTreasureHere()) {
           		textArea.append("Congrats! You found treasure! Health fully restored \n");
           		player.restoreHealth();
           }
           if (tile.hasEnemyHere()) {
	           	mp = tile.getEnemy();
	           	textArea.append("Found a level " + Integer.toString(mp.getLevel()) + " enemy here! Prepare to fight!\n");
	           	fight.setVisible(true);
	           	ult.setVisible(true);
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
           inputpanel.add(ult);
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
					System.out.println(player.getExp());
					mp.isDead();
					numMonstersLeft--;
					map.moveEnemies();
					canvas.repaint();
				}
				if(player.getHealth() <= 0) {
					textArea.append("Game Over! You're dead!\n");
					setBackground(Color.RED);
				}
           	
			}
			else if (e.getSource() == button) {
				if (tile.hasEnemyHere() && mp.isAlive() && player.getHealth() > 0) {
					textArea.append("Cannot run from a fight!\n");
				}
				else {
					dispose();
					numSquares++;
					Graph.this.setEnabled(true);
					Graph.this.requestFocus();
					levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
					hpMeter.setMaximum(player.getMaxHealth());
					hpMeter.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
					hpMeter.setValue(player.getHealth());
					monstersMeter.setString(Integer.toString(numMonstersLeft) + "/" + Integer.toString(controlNum));
					monstersMeter.setValue(numMonstersLeft);
					//map.moveEnemies();
					canvas.repaint();
					Graph.this.defaultCloseOperationCallback();
				}
			} else if(e.getSource() == ult) {
				int max = player.getMaxHealth();
				textArea.append("Use ultimate move!\n");
				if(player.getHealth() != max) {
					textArea.append("...but the player wasn't strong enough to handle it.\n");
					player.setHealth(0);
					String currentHp = Integer.toString(0) + "/" + Integer.toString(player.getMaxHealth());
					playerHp.setText(currentHp);
					textArea.append("Game Over! You're dead!\n");
					setBackground(Color.RED);
				} else {
					int enemyHp = mp.getMaxHealth();
					player.attack(mp, enemyHp);
					textArea.append("Monster has taken " + Integer.toString(enemyHp) + " damage!\n");
					String text = Integer.toString(mp.getHealth()) + "/" + Integer.toString(mp.getMaxHealth());
					monsterHp.setText(text);
					mp.attack(player, player.getMaxHealth()-1);
					textArea.append("Player has taken " + Integer.toString(player.getMaxHealth()-1) + " damage!\n");
					String currentHp = Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth());
					playerHp.setText(currentHp);
					if(mp.getHealth() <= 0 && mp.isAlive()) {
						textArea.append("Congrats! You won the fight!\n");
						textArea.append(player.incExp(mp) + "\n");
						System.out.println(player.getExp());
						mp.isDead();
						numMonstersLeft--;
						map.moveEnemies();
						canvas.repaint();
						Graph.this.defaultCloseOperationCallback();
					}
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
	MapTile tile = map.returnCurrentMapTile();
	boolean isMovementRestricted;
	if(numSquares == 0) {
		isMovementRestricted = false;
	}
	else isMovementRestricted = true;
	//the player can only move from mapTiles 
	switch (val) {
		case 87: //up
			if (selectedSquare.y <= 0) return;
			if(isMovementRestricted) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x, selectedSquare.y-1).hasBeenVisited()
						&& ((selectedSquare.x >= 12 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y-1).hasBeenVisited())
						&& (selectedSquare.x <= 0 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y-1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x, selectedSquare.y-1);
			selectedSquare = newPosition;
			map.currentPosition[1] = selectedSquare.y;
			break;
		case 83: //down
			if (selectedSquare.y >= 12) return;
			if(isMovementRestricted) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x, selectedSquare.y+1).hasBeenVisited()
						&& ((selectedSquare.x >= 12 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y+1).hasBeenVisited())
						&& (selectedSquare.x <= 0 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y+1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x, selectedSquare.y+1);
			selectedSquare = newPosition;
			map.currentPosition[1] = selectedSquare.y;
			break;
		case 65: //left
			if (selectedSquare.x <= 0) return;
			if(isMovementRestricted) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y).hasBeenVisited()
						&& ((selectedSquare.y >= 12 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y+1).hasBeenVisited())
						&& (selectedSquare.y <= 0 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y-1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x-1, selectedSquare.y);
			selectedSquare = newPosition;
			map.currentPosition[0] = selectedSquare.x;
			break;
		case 68: //right
			if (selectedSquare.x >= 12) return;
			if(isMovementRestricted) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y).hasBeenVisited()
						&& ((selectedSquare.y >= 12 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y+1).hasBeenVisited())
						&& (selectedSquare.y <= 0 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y-1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x +1, selectedSquare.y);
			selectedSquare = newPosition;
			map.currentPosition[0] = selectedSquare.x;
			break;
		case 10:
			if (!tile.hasBeenVisited()) {
				makeMessageWindow();
				tile.toggleVisited();
			}
			break;
		case 32:
			if (numSquares - (controlNum - numMonstersLeft) >= 169 - controlNum) {
				canvas.goToNextLevel();
			}
			break;
		case 77:
			if (!tile.hasBeenVisited()) {
				tile.toggleMarked();
			}
	}
	canvas.repaint();
	
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
}

@Override
public void actionPerformed(ActionEvent e) {
	if (e.getSource() == resetButton) {
		initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
		numResets = 3;
		resets.setText("Resets left " + Integer.toString(numResets) + "/3");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	else if (e.getSource() == startLevelOverButton) {
		System.out.println("Initial monster start");
		System.out.println(initialMonsterStart);
		initializeGame(initialLevelStart, initialMonsterStart, controlNum, baseExpForLevel);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	else if (e.getSource() == prevLevelButton) {
		if(monsterLevel > 0) {
			goToPreviousLevel();
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	else if (e.getSource() == saveGameButton) {
		fs.writeFile(player, monsterLevel, controlNum, numSquares, map, initialLevelStart, baseExpForLevel, numResets);
		hasSaveGameBeenClicked = true;
		System.out.println("Number of monsters is " + controlNum);
		if(!loadGameButton.isEnabled()) {
			loadGameButton.setEnabled(true);
		}
		requestFocus();
	}
	else if (e.getSource() == loadGameButton) {
		Map temp = fs.readFromFile();
		if(hasSaveGameBeenClicked) {
			isFirstLoad = false;
		}
		if(numResets > 0 && !isFirstLoad) {
			//System.out.println("This route should ideally be hit");
			initializeGameFromFile(temp);
			numResets--;
			fs.writeFile(player, monsterLevel, controlNum, numSquares, map, initialLevelStart, baseExpForLevel, numResets);
			System.out.println("Num resets " + Integer.toString(numResets));
			resets.setText("Resets left " + Integer.toString(numResets) + "/3");
		}
		else if(isFirstLoad) {
			isFirstLoad = false;
			initializeGameFromFile(temp);
		}
		else {
			goToPreviousLevel();
			numResets = 3;
			resets.setText("Resets left " + Integer.toString(numResets) + "/3");
			fs.writeFile(player, monsterLevel, controlNum, numSquares, map, initialLevelStart, baseExpForLevel, numResets);
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	} else if (e.getSource() ==  startGame) {
		screenState = State.PlayingState;
		remove(mainPanel);
		revalidate();
		
		this.playingStateSetup();
		this.initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
	} else if (e.getSource() == next) {
		  CardLayout cl = (CardLayout)(cards.getLayout());
		  cl.next(cards);
	}
	canvas.repaint();
	
}

public void goToPreviousLevel() {
	if (monsterLevel > 0) {
		controlNum--;
		monsterLevel -= 8;
		System.out.println("Player stats");
		System.out.println(player.getLevel());
		System.out.println(player.getMaxHealth());
		System.out.println(initialLevelStart);
		initialMonsterLevel = monsterLevel;
		if (numResets == 0) {
			initializeGame(player.getLevel(), monsterLevel, controlNum, baseExpForLevel);
		}
		else {
			initializeGame(initialLevelStart, monsterLevel, controlNum, baseExpForLevel);
		}
	}
	else {
		initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
	}
}

private void initializeGameFromFile(Map newMap) {
	player = newMap.getPlayer();
	monsterLevel = newMap.getBias();
	controlNum = newMap.getDifficulty();
	int x = newMap.getPlayerPosition()[0];
	int y = newMap.getPlayerPosition()[1];
	Point p = new Point(x, y);
	selectedSquare = p;
	numSquares = newMap.getNumSquares();
	levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
	map = newMap;
	getNumMonsters();
	System.out.println("Number of monsters is " + controlNum);
	System.out.println("Player exp " + player.getExp());
	hpMeter.setMaximum(player.getMaxHealth());
	hpMeter.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
	hpMeter.setValue(player.getHealth());
	monstersMeter.setString(Integer.toString(numMonstersLeft) + "/" + Integer.toString(controlNum));
	monstersMeter.setValue(numMonstersLeft);
	initialMonsterStart = monsterLevel;
	initialLevelStart = fs.getInitialPlayerLevel();
	baseExpForLevel = fs.getBaseExpForLevel();
	numResets = map.getResetsLeft();
	resets.setText("Resets left " + Integer.toString(numResets) + "/3");
	levelNum.setText("Level #" + Integer.toString(controlNum-29));
	canvas.repaint();
	requestFocus();
}

private void initializeGame(int playerLevel, int monsterLevel, int difficulty, int exp) {
	player = new Player(20, 5, playerLevel);
	player.setExp(exp);
	map = new Map(player);
	map.populateMap(difficulty, monsterLevel);
	controlNum = difficulty;
	numMonstersLeft = controlNum;
	map.setPlayerPosition();
	int x = map.getPlayerPosition()[0];
	int y = map.getPlayerPosition()[1];
	Point p = new Point(x, y);
	selectedSquare = p;
	numSquares = 0;
	levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
	hpMeter.setMaximum(player.getMaxHealth());
	hpMeter.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
	hpMeter.setValue(player.getHealth());
	monstersMeter.setString(Integer.toString(numMonstersLeft) + "/" + Integer.toString(controlNum));
	monstersMeter.setValue(numMonstersLeft);
	levelNum.setText("Level #" + Integer.toString(controlNum-29));
	canvas.repaint();
	requestFocus();
}

}