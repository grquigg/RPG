package game;

import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import game.Game.GameModes;
import game.Graph.DrawCanvas;


public class GUI extends JFrame implements KeyListener {

	Map map;
	Player player;
	int numMonsters;
	public int h = 50;
	public int w = 50;
	private int offX = 50;
	private int offY = 50;
	private Point selectedSquare;
	private Point checkSquare;
	private boolean dontChange = false; 
	private boolean close = true;
	private DrawCanvas canvas;
	private boolean[][] visitedSquares = new boolean[12][12];


	/**
	 * Constructor sets up UI and default board with numbers
	 */
	public GUI(){
		
		   player = new Player(20, 5, 1);
		   map = new Map(player);
		   map.populateMap(30);
		   numMonsters = 30;
		   //totalM = numMonsters;
		   map.setPlayerPosition();
		   int x = map.getPlayerPosition()[0];
		   int y = map.getPlayerPosition()[1];
		   Point p = new Point(x, y);
		   selectedSquare = p;
		   // Set up a panel for the buttons
		   JPanel btnPanel = new JPanel(new FlowLayout());
		   //hp = new JProgressBar(0, 20);
		   //hp.setValue(20);
		   //hp.setStringPainted(true);
		   //hp.setString(Integer.toString(player.getHealth()) + "/20");
		   JLabel lab1 = new JLabel("Player Health:");
		   btnPanel.add(lab1);
		   //btnPanel.add(hp);
		   int width = 12*w + 3*offX;
		   int height = 12*h + 3*offY;
		   //btnPanel.setPreferredSize(new Dimension(width, 40));
		   JLabel lab2 = new JLabel("Monsters left: ");
		   btnPanel.add(lab2);
		   //monsters = new JProgressBar(0, numMonsters);
		  //monsters.setValue(numMonsters);
		   //monsters.setStringPainted(true);
		   //monsters.setString(Integer.toString(numMonsters) + "/" + Integer.toString(totalM));
		   //btnPanel.add(monsters);
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
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
			else if (r == 12) {
				for (int i = 0; i < 2; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
			else {
				for (int i = r-1; i <=r+1; i++) {
					for (int j = 0; j < 2; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
		}
		else if (c == 12) {
			if (r == 0) { //if column and row are both zero
				for (int i = 0; i < 2; i++) {
					for (int j = 11; j <= 12; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
			else if (r == 12) {
				for (int i = c-1; i <= c; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
			else {
				for (int i = r-1; i <=r+1; i++) {
					for (int j = 11; j <= 12; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
		}
		else if (r == 0){
			if (c == 0) {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < 2; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			} else if (c == 12) {
				for (int i = 0; i < 2; i++) {
					for (int j = 11; j <= 12; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
			else {
				for (int i = 0; i < 2; i++) {
					for (int j = c-1; j <= c+1; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
		} else if (r == 12) {
			if (c == 0) {
				for (int i = 0; i < 2; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			} else if (c == 12) {
				for (int i = c-1; i <= c; i++) {
					for (int j = r-1; j <= r; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
			else {
				//System.out.println("This should print here");
				for (int i = r-1; i <=r; i++) {
					for (int j = c-1; j <= c+1; j++) {
						if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
					}
				}
				return count;
			}
		} else {
			for (int i = r-1; i <= r+1; i++) {
				for (int j = c-1; j <= c+1; j++) {
					if(map.getMapTileAt(j, i).hasEnemyHere()) count+=1;
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
	

	
	public int getWidth() {
		return w;
	}
	
	public int getHeight() {
		return h;
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
			makeMessageWindow();
			tile.toggleVisited();
			break;
		}
		System.out.println(val);
		//System.out.println(map.currentPosition[0] + " " + map.currentPosition[1]);
		//System.out.println(selectedSquare.x + " " + selectedSquare.y);
		canvas.repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		System.out.println(arg0.getKeyCode());
		
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
            String currentHp = Integer.toString(player.getHealth()) + "/20";
            playerHp = new JLabel(currentHp);
            monsterHp = new JLabel();
            fight.addActionListener(this);
            fight.setVisible(false);
            monsterHp.setVisible(false);
            button.addActionListener(this);
            if (tile.isTreasureHere()) {
            	textArea.append("Congrats! You found treasure!\n");
            }
            if (tile.hasEnemyHere()) {
            	textArea.append("Found an enemy here! Prepare to fight!\n");
            	mp = tile.getEnemy();
            	fight.setVisible(true);
            	monsterHp.setVisible(true);
            	String text = Integer.toString(mp.getHealth()) + "/10";
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
				textArea.append("Monster has taken " + Integer.toString(player.getAttack()) + " damage!\n");
            	String text = Integer.toString(mp.getHealth()) + "/10";
            	monsterHp.setText(text);
            	mp.attack(player);
            	textArea.append("Player has taken " + Integer.toString(mp.getAttack()) + " damage!\n");
                String currentHp = Integer.toString(player.getHealth()) + "/20";
                playerHp.setText(currentHp);
                if(mp.getHealth() <= 0) {
                	textArea.append("Congrats! You won the fight!\n");
                	mp.isDead();
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
				}
			}
			
		}
	}
	
	class DrawCanvas extends JPanel {
		 @Override
		 public void paint(Graphics g) {
			 super.paint(g);
		     drawContentForSquares(g);
		     drawLinesForGrid(g);
		     highlightSelectedSquares(g);     
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
			/**
			 * Draws the lines to clearly separate tiles. 
			 * Line color is black. 
			 * @param g the graphics object to draw on
			 */
			private void drawLinesForGrid(Graphics g) {
				// user can choose to see lines or not
				for (int i=0;i<=13;i++){
					//Lines
					g.setColor(Color.black);
					g.drawLine(offX,i*h+offY,w*13+offX,i*h+offY);
					g.drawLine(i*w+offX, offY, i*w+offX, (h*13)+offY);
				}
			}
			
			/**
			 * Puts content into the individual squares, i.e. numbers or image data.
			 * @param g the graphics object to draw on
			 */
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
					}
				}
			}
	}
	/**
	 * Main method to start the application.
	 * @param args not used
	 */
	public static void main(String[] args){
		GUI pgf = new GUI();
		pgf.setVisible(true);
	}
}