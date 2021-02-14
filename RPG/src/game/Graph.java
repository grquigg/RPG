package game;

import java.awt.*;       // Using AWT's Graphics and Color
import java.awt.event.*; // Using AWT's event classes and listener interface
import java.io.File;
import java.util.ArrayList;

import javax.swing.*;    // Using Swing's components and containers
import javax.swing.text.DefaultCaret;

/**
 * Custom Graphics Example: Using key/button to move a line left or right.
 */
@SuppressWarnings("serial")


public class Graph extends JFrame implements KeyListener, ActionListener {
   // Define constants for the various dimensions
	
	public enum State {InfoState, PlayingState};
	public enum CanvasState { OVERLAY, MAP};
	public enum Difficulty { EASY, HARD };
	private MenuBar menuBar;
	private Menu fileMenu, editMenu, viewMenu;
	private MenuItem mi, loadGame, difficulty;
	Map map; //the map
	Map m2; //overlay map
	Player player;
	public int cellSize = 50;
	public int h = cellSize;
	public int w = cellSize;
	int offX = 30;
	int offY = 0;
	public int width = 12*w;
	public int height = 12*h + 4*offY;
	State screenState;
	Point selectedSquare;
	private DrawCanvas canvas;
	JProgressBar hpMeter;
	JProgressBar monstersMeter;
	JButton startLevelOverButton = new JButton("Reset Level");
	JButton prevLevelButton = new JButton("Previous Level");
	JButton startGame = new JButton("Start Game!");
	JButton next = new JButton("Next Card");
	SaveFileWriter fs;
	int controlNum = 40; //the number of monsters for level and the difficulty of the map
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
	int[][] overlayArray = new int[13][13];
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
	
	JDialog cellsizeDialog;
	JDialog overlayDialog;
	JTextField text;
	JTextField input;
	CanvasState c = CanvasState.MAP;
	Difficulty diff = Difficulty.HARD;
	ArrayList<Set> sets = new ArrayList<Set>();
   // Constructor to set up the GUI components and event handlers
	public Graph() {
	   System.out.println("offX: " + Integer.toString(offX));
	   setMenuBar(menuBar = new MenuBar());
	   menuBar.add(fileMenu=new Menu("Options"));
	   fileMenu.add(mi=new MenuItem("Cell Size"));
	   mi.addActionListener(this);
	   fileMenu.add(difficulty = new MenuItem("Difficulty"));
	   difficulty.addActionListener(this);
	   menuBar.add(fileMenu=new Menu("Game"));
	   fileMenu.add(mi=new MenuItem("New Game"));
	   mi.addActionListener(this);
	   fileMenu.add(mi=new MenuItem("Save Game"));
	   mi.addActionListener(this);
	   fileMenu.add(loadGame=new MenuItem("Load Game"));
	   loadGame.addActionListener(this);
	   GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
	   Rectangle bounds = environment.getMaximumWindowBounds();
	   System.out.println("Screen Bounds = " + bounds);
	   
	   GraphicsDevice device = environment.getDefaultScreenDevice();
	   GraphicsConfiguration config = device.getDefaultConfiguration();
	   System.out.println("Screen Size = " + config.getBounds());
	   int max_height = bounds.height;
	   System.out.println(max_height);
	   cellSize = (max_height - 87) / 13;
	   System.out.println(cellSize);
	   h = cellSize - 1;
	   w = cellSize - 1;
	   System.out.println("Total width");
	   System.out.println(12*w);
	   System.out.println(offX);
	   width = 13*w + 2*offX;
	   height = 13*h;
	   setSize(width+124, height+86);
	   playingStateSetup();
	   System.out.println("Frame Size = " + getSize());
	   initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
	   isFirstLoad = true;
	   File file = new File("game.xml");
	   if(!file.exists()) {
		   loadGame.setEnabled(false);
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
	   JLabel healthPlayer = new JLabel("Player Health:");
	   JLabel leftMonsters = new JLabel("Monsters left: ");
	   bottomPanel.add(levelNum);
	   bottomPanel.add(leftMonsters);
	   monstersMeter = new JProgressBar(0, numMonstersLeft);
	   monstersMeter.setStringPainted(true);
	   bottomPanel.add(monstersMeter);
	   
	   
	   startLevelOverButton.addActionListener(this);
	   prevLevelButton.addActionListener(this);
	   
	   canvas = new DrawCanvas(this, map);
	   System.out.println("Width: " + Integer.toString(width));
	   System.out.println("Height: " + Integer.toString(height));
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
	   topPanel.add(prevLevelButton);
	   topPanel.add(startLevelOverButton);
	   topPanel.add(resets);
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
	   addComponentListener(new ComponentAdapter() 
	   {  
	           public void componentResized(ComponentEvent evt) {
	               System.out.println("Component resized");
	               System.out.println("Frame Width = " + getSize().width);
	               System.out.println("Frame Height = " + getSize().height);
	               //int offset = (getSize().width / 2) - (width / 2)
	           }
	   });
	   
	   //setPreferredSize(new Dimension(width, height+40));
	   fs = new SaveFileWriter("game.xml");
	   setDefaultCloseOperation(EXIT_ON_CLOSE); // Handle the CLOSE button
	   setTitle("RPG");
	   pack();           // pack all the components in the JFrame
	   setVisible(true); // show it
	   requestFocus();   // set the focus to JFrame to receive KeyEvent
	   System.out.println("Monster meter size: " + monstersMeter.getSize());
	   System.out.println("Bottom panel size " + bottomPanel.getSize());
	   System.out.println("Top panel size " + topPanel.getSize());
	   System.out.println("Canvas size: " + canvas.getSize());
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
   
   public boolean checkVictoryConditions() {
	   if (numSquares - (controlNum - numMonstersLeft) >= 169 - controlNum) {
		   return true;
	   } else return false;
   }
   public int getMonstersInArea(int c, int r, Map m) {
		int count = 0;
		if (c == 0) {
			if (r == 0) { //if column and row are both zero
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else if (r == 12) {
				for (int j = 0; j < 2; j++) {
					for (int i = r-1; i <= r; i++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) {
							count+=m.getMapTileAt(j, i).getEnemy().getLevel();
						}
					}
				}
				return count;
			}
			else {
				for (int i = r-1; i <=r+1; i++) {
					for (int j = 0; j < 2; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		}
		else if (c == 12) {
			if (r == 0) { //if column and row are both zero
				for (int i = 0; i < 2; i++) {
					for (int j = 11; j <= 12; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else if (r == 12) {
				for (int i = c-1; i <= c; i++) {
					for (int j = r-1; j <= r; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				for (int i = r-1; i <=r+1; i++) {
					for (int j = 11; j <= 12; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		}
		else if (r == 0){
			if (c == 0) {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			} else if (c == 12) {
				for (int i = 0; i < 2; i++) {
					for (int j = 11; j <= 12; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				for (int i = 0; i < 2; i++) {
					for (int j = c-1; j <= c+1; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		} else if (r == 12) {
			if (c == 0) {
				for (int i = 0; i < 2; i++) {
					for (int j = r-1; j <= r; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			} else if (c == 12) {
				for (int i = c-1; i <= c; i++) {
					for (int j = r-1; j <= r; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
			else {
				//System.out.println("This should print here");
				for (int i = r-1; i <=r; i++) {
					for (int j = c-1; j <= c+1; j++) {
						if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
					}
				}
				return count;
			}
		} else {
			for (int i = r-1; i <= r+1; i++) {
				for (int j = c-1; j <= c+1; j++) {
					if(m.getMapTileAt(j, i).hasEnemyHere()) count+=m.getMapTileAt(j, i).getEnemy().getLevel();
				}
			}
			return count;
		}
   }
   
	public int getMonstersInArea(int c, int r) {
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
	
	public boolean getSolution() {
		System.out.println("Get solution");
		boolean foundSolution = true;
		boolean isFirstTile = false;
		int count = 0;
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				if(!map.getMapTileAt(i, j).hasEnemyHere()) {
					if (!isFirstTile) {
						int [] coordinates = new int[2];
						coordinates[0] = i;
						coordinates[1] = j;
						if(sets.size() == 0) {
							Set s = new Set();
							s.addToSet(coordinates);
							s.setIndex(0);
							sets.add(s);
							map.getMapTileAt(i, j).setIndex(0);
							isFirstTile = true;
						}
					}
					else {
						if(i == 0) {
							if(map.getMapTileAt(i, j-1).getIndex() != -1) {
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								map.getMapTileAt(i, j).setIndex(count);
								sets.get(map.getMapTileAt(i, j-1).getIndex()).addToSet(arr);
							} else {
								Set s = new Set();
								int [] coordinates = new int[2];
								coordinates[0] = i;
								coordinates[1] = j;
								s.addToSet(coordinates);
								count++;
								s.setIndex(count);
								sets.add(s);
								map.getMapTileAt(i, j).setIndex(count);
							}
						} else if (j == 0) {
							if(map.getMapTileAt(i-1, j).getIndex() != -1) {
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								map.getMapTileAt(i, j).setIndex(map.getMapTileAt(i-1, j).getIndex());
								sets.get(map.getMapTileAt(i-1, j).getIndex()).addToSet(arr);
							} else {
								Set s = new Set();
								int [] coordinates = new int[2];
								coordinates[0] = i;
								coordinates[1] = j;
								s.addToSet(coordinates);
								count++;
								s.setIndex(count);
								sets.add(s);
								map.getMapTileAt(i, j).setIndex(count);
							}
						} else {
							if(map.getMapTileAt(i, j-1).getIndex() == -1 && map.getMapTileAt(i-1, j).getIndex() == -1) {
								Set s = new Set();
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								s.addToSet(arr);
								count++;
								s.setIndex(count);
								sets.add(s);
								map.getMapTileAt(i, j).setIndex(count);
							} else if (map.getMapTileAt(i-1, j).getIndex() != -1 && map.getMapTileAt(i, j-1).getIndex() == -1) {
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								map.getMapTileAt(i, j).setIndex(map.getMapTileAt(i-1, j).getIndex());
								sets.get(map.getMapTileAt(i, j).getIndex()).addToSet(arr);
							} else if (map.getMapTileAt(i, j-1).getIndex() != -1 && map.getMapTileAt(i-1, j).getIndex() == -1) {
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								map.getMapTileAt(i, j).setIndex(map.getMapTileAt(i, j-1).getIndex());
								sets.get(map.getMapTileAt(i, j).getIndex()).addToSet(arr);
							} else if (map.getMapTileAt(i, j-1).getIndex() != -1 && map.getMapTileAt(i-1, j).getIndex() != -1 && map.getMapTileAt(i, j-1).getIndex() != map.getMapTileAt(i-1, j).getIndex()) {
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								//set the top 
								map.getMapTileAt(i, j).setIndex(map.getMapTileAt(i-1, j).getIndex());

								sets.get(map.getMapTileAt(i, j).getIndex()).addToSet(arr);
								
								int temp = map.getMapTileAt(i, j).getIndex();
								sets.get(map.getMapTileAt(i, j-1).getIndex()).merge(sets.get(map.getMapTileAt(i, j).getIndex()), map);
							} else {
								int [] arr = new int[2];
								arr[0] = i;
								arr[1] = j;
								map.getMapTileAt(i, j).setIndex(map.getMapTileAt(i-1, j).getIndex());
								sets.get(map.getMapTileAt(i, j).getIndex()).addToSet(arr);
							}
						}
					}
				}
				}
				
			}
			int [] arr = sets.get(0).getCell(0);
			int ind = map.getMapTileAt(arr[0], arr[1]).getIndex();
			for (int j = 1; j < sets.size(); j++) {
				int [] array = sets.get(j).getCell(0);
				int index = map.getMapTileAt(array[0], array[1]).getIndex();
				if(ind != index) {
					foundSolution = false;
				}
			}
			System.out.println(foundSolution);
			return foundSolution;
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
           if (diff == Difficulty.HARD && tile.isTreasureHere()) {
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
					if(mp.getMonsterType() == "Necromancer") {
						textArea.append("You killed a necromancer!\n");
					}
					System.out.println(player.getExp());
					mp.isDead();
					numMonstersLeft--;
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
					if(diff == Difficulty.HARD) {
						ArrayList<MapTile> tiles = map.getNecromancers();
						if(tiles.size() != 0) {
							System.out.println("RES");
							System.out.println("Necromancers Left: " + Integer.toString(tiles.size()));
							Necromancer n = (Necromancer) tiles.get(0).getEnemy();
							boolean decide = n.resurrectMonster(map, numMonstersLeft, controlNum);
							if(decide) {
								numMonstersLeft++;
							}
						}
//						map.moveEnemies();
					}
					numSquares++;
					Graph.this.setEnabled(true);
					Graph.this.requestFocus();
					levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
					hpMeter.setMaximum(player.getMaxHealth());
					hpMeter.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
					hpMeter.setValue(player.getHealth());
					monstersMeter.setString(Integer.toString(numMonstersLeft) + "/" + Integer.toString(controlNum));
					monstersMeter.setValue(numMonstersLeft);
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
//						map.moveEnemies();
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
			if(isMovementRestricted && c == CanvasState.MAP) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x, selectedSquare.y-1).hasBeenVisited()
						&& ((selectedSquare.x >= 12 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y-1).hasBeenVisited())
						&& (selectedSquare.x <= 0 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y-1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x, selectedSquare.y-1);
			selectedSquare = newPosition;
			if (c == CanvasState.MAP) {
				selectedSquare = newPosition;
				map.currentPosition[1] = selectedSquare.y;
			}
			break;
		case 83: //down
			if (selectedSquare.y >= 12) return;
			if((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
				System.out.println("Ctrl S");
				fs.writeFile(player, monsterLevel, controlNum, numSquares, map, initialLevelStart, baseExpForLevel, numResets);
				hasSaveGameBeenClicked = true;
				System.out.println("Number of monsters is " + controlNum);
				if(!loadGame.isEnabled()) {
					loadGame.setEnabled(true);
				}
				requestFocus();
			}
			else {
				if(isMovementRestricted && c == CanvasState.MAP) {
					if(!tile.hasBeenVisited() 
							&& !map.getMapTileAt(selectedSquare.x, selectedSquare.y+1).hasBeenVisited()
							&& ((selectedSquare.x >= 12 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y+1).hasBeenVisited())
							&& (selectedSquare.x <= 0 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y+1).hasBeenVisited()))) return;
				}
				newPosition = new Point(selectedSquare.x, selectedSquare.y+1);
				selectedSquare = newPosition;
				if (c == CanvasState.MAP) {
					selectedSquare = newPosition;
					map.currentPosition[1] = selectedSquare.y;
				}
			}
			break;
		case 65: //left
			if (selectedSquare.x <= 0) return;
			if(isMovementRestricted && c == CanvasState.MAP) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y).hasBeenVisited()
						&& ((selectedSquare.y >= 12 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y+1).hasBeenVisited())
						&& (selectedSquare.y <= 0 || !map.getMapTileAt(selectedSquare.x-1, selectedSquare.y-1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x-1, selectedSquare.y);
			selectedSquare = newPosition;
			if (c == CanvasState.MAP) {
				selectedSquare = newPosition;
				map.currentPosition[0] = selectedSquare.x;
			}
			break;
		case 68: //right
			if (selectedSquare.x >= 12) return;
			if(isMovementRestricted && c == CanvasState.MAP) {
				if(!tile.hasBeenVisited() 
						&& !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y).hasBeenVisited()
						&& ((selectedSquare.y >= 12 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y+1).hasBeenVisited())
						&& (selectedSquare.y <= 0 || !map.getMapTileAt(selectedSquare.x+1, selectedSquare.y-1).hasBeenVisited()))) return;
			}
			newPosition = new Point(selectedSquare.x +1, selectedSquare.y);
			selectedSquare = newPosition;
			if (c == CanvasState.MAP) {
				selectedSquare = newPosition;
				map.currentPosition[0] = selectedSquare.x;
			}
			break;
		case 10:
			if (c == CanvasState.MAP) {
				if (!tile.hasBeenVisited()) {
					makeMessageWindow();
					tile.toggleVisited();
				}
			} else if (c == CanvasState.OVERLAY) {
				System.out.println("Enter");
				makeOverlayWindow();
			}
			break;
		case 32:
			if (numSquares - (controlNum - numMonstersLeft) >= 169 - controlNum) {
				goToNextLevel();
			}
			break;
		case 77:
			if (!tile.hasBeenVisited()) {
				tile.toggleMarked();
			}
			break;
		case 81:
			if((arg0.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
				System.out.println("Load Game");
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
			}
			break;
		case 69: //nice
			if(c == CanvasState.MAP) {
				System.out.println("Switching to overlay");
				c = CanvasState.OVERLAY;
			}
			else if (c == CanvasState.OVERLAY) {
				System.out.println("Switching to map");
				c = CanvasState.MAP;
				selectedSquare.x = map.getPlayerPosition()[0];
				selectedSquare.y = map.getPlayerPosition()[1];
			}
			break;
		case 84: //thing for when the difficulty is easy
			if (diff == Difficulty.EASY) {
				if (player.getHeals() > 0) {
					player.restoreHealth();
					player.heal();
					System.out.println(player.getHeals());
					hpMeter.setMaximum(player.getMaxHealth());
					hpMeter.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
				} else {
					System.out.println("Cannot heal");
				}
			}
			break;
	}
	canvas.updateSelectedSquare();
	canvas.repaint();
	
}

private void makeOverlayWindow() {
	JFrame frame = new JFrame("frame");
	overlayDialog = new JDialog(frame, "Overlay menu");
	JPanel p = new JPanel(new GridBagLayout());
	GridBagConstraints gc = new GridBagConstraints();
	gc.gridx = 0;
	gc.gridy = 0;
	gc.ipadx = 10;
	JLabel l = new JLabel("Enter number for cell");
	p.add(l, gc);
	gc.gridx = 1;
	gc.gridy = 0;
	gc.ipadx = 0;
	input = new JTextField();
	input.setColumns(5);
	p.add(input, gc);
	gc.gridx = 1;
	gc.gridy = 1;
	JButton enter = new JButton("Enter");
	enter.addActionListener(this);
	p.add(enter, gc);
	overlayDialog.add(p);
	overlayDialog.setSize(400, 200);
	overlayDialog.setVisible(true);
	
}

@Override
public void keyReleased(KeyEvent e) {
	// TODO Auto-generated method stub
}

@Override
public void actionPerformed(ActionEvent e) {
	if(e.getActionCommand().equals("Cell Size")) {
		runCellSizeDialog();
		System.out.println("Adjust");
	}
	else if(e.getActionCommand().equals("New Game")) {
		System.out.println("New Game");
		initializeGame(startLevelPlayer, initialMonsterLevel, initialControlNum, 0);
		numResets = 3;
		resets.setText("Resets left " + Integer.toString(numResets) + "/3");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	else if(e.getActionCommand().equals("Load Game")) {
		System.out.println("Load Game");
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
	}
	else if(e.getActionCommand().equals("Save Game")) {
		System.out.println("Save Game");
		fs.writeFile(player, monsterLevel, controlNum, numSquares, map, initialLevelStart, baseExpForLevel, numResets);
		hasSaveGameBeenClicked = true;
		System.out.println("Number of monsters is " + controlNum);
		if(!loadGame.isEnabled()) {
			loadGame.setEnabled(true);
		}
		requestFocus();
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
	else if (e.getSource() ==  startGame) {
		screenState = State.PlayingState;
		remove(mainPanel);
		revalidate();
		
		this.playingStateSetup();
		this.initializeGame(startLevelPlayer, monsterLevel, controlNum, baseExpForLevel);
	} else if (e.getSource() == next) {
		  CardLayout cl = (CardLayout)(cards.getLayout());
		  cl.next(cards);
	} else if (e.getActionCommand().equals("Apply")) {
		System.out.println("Apply");
		cellsizeDialog.dispose();
		String s = text.getText();
		System.out.println(s);
		cellSize = Integer.parseInt(s);
		h = cellSize;
		w = cellSize;
		width = 13*w + 2*offX;
		height = 13*h + 4*offY;
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.repaint();
	} else if (e.getActionCommand().equals("Enter")) {
		int i = Integer.parseInt(input.getText());
		if(i == 0) {
			m2.getMapTileAt(selectedSquare.x, selectedSquare.y).setEnemy(null);
		}
		else {
			m2.getMapTileAt(selectedSquare.x, selectedSquare.y).setEnemy(new Monster(0, 0, Integer.parseInt(input.getText())));
		}
		overlayDialog.dispose();
	} else if (e.getActionCommand().equals("Difficulty")) {
		System.out.println("Difficulty");
	}
	canvas.repaint();
	
}

private void runCellSizeDialog() {
	JFrame frame = new JFrame("frame");
	cellsizeDialog = new JDialog(frame, "Change Cell Size");
	JPanel p = new JPanel(new GridBagLayout());
	GridBagConstraints gc = new GridBagConstraints();
	gc.gridx = 0;
	gc.gridy = 0;
	gc.ipadx = 10;
	JLabel l = new JLabel("Current Cell Size");
	p.add(l, gc);
	gc.gridx = 1;
	gc.gridy = 0;
	gc.ipadx = 0;
	text = new JTextField(Integer.toString(cellSize));
	text.setColumns(5);
	p.add(text, gc);
	gc.gridx = 1;
	gc.gridy = 1;
	JButton apply = new JButton("Apply");
	apply.addActionListener(this);
	p.add(apply, gc);
	cellsizeDialog.add(p);
	cellsizeDialog.setSize(200, 200);
	cellsizeDialog.setVisible(true);
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
	m2 = new Map(player);
	m2.setPlayerPosition(map.getPlayerPosition());
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
	levelNum.setText("Level #" + Integer.toString(controlNum-39));
	canvas.repaint();
	requestFocus();
	sets.clear();
	map.clearIndexes();
	getSolution();
}

private void initializeGame(int playerLevel, int monsterLevel, int difficulty, int exp) {
	player = new Player(20, 5, playerLevel);
	player.setExp(exp);
	map = new Map(player);
	map.populateMap(difficulty, monsterLevel, diff);
	sets.clear();
	map.clearIndexes();
	while(!getSolution()) {
		map = new Map(player);
		map.populateMap(difficulty, monsterLevel, diff);
		sets.clear();
		map.clearIndexes();
	}
//	getSolution();
//	map = new Map(player);
//	map.populateMap(difficulty, monsterLevel, diff);
	controlNum = difficulty;
	numMonstersLeft = controlNum;
	map.setPlayerPosition();
	int x = map.getPlayerPosition()[0];
	int y = map.getPlayerPosition()[1];
	Point p = new Point(x, y);
	selectedSquare = p;
	numSquares = 0;
	m2 = new Map(player);
	m2.setPlayerPosition(map.getPlayerPosition());
	levelPlayer.setText("Level " + Integer.toString(player.getLevel()));
	hpMeter.setMaximum(player.getMaxHealth());
	hpMeter.setString(Integer.toString(player.getHealth()) + "/" + Integer.toString(player.getMaxHealth()));
	hpMeter.setValue(player.getHealth());
	monstersMeter.setString(Integer.toString(numMonstersLeft) + "/" + Integer.toString(controlNum));
	monstersMeter.setValue(numMonstersLeft);
	levelNum.setText("Level #" + Integer.toString(controlNum-39));
	canvas.notifyForMapUpdate();
	canvas.notifyForPlayerUpdate();
	canvas.updateSelectedSquare();
}

}