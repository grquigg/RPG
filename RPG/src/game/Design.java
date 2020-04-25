package game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.GUI.DrawCanvas;

public class Design extends JFrame implements KeyListener {

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
	public Design(){
		setTitle("RPG");
		
		player = new Player(20, 5, 1);
		map = new Map(player);
		map.populateMap(30);
		numMonsters = 30;
		map.setPlayerPosition();
		int x = map.getPlayerPosition()[0];
		int y = map.getPlayerPosition()[1];
		Point p = new Point(x, y);
		selectedSquare = p;
	    // Set up a panel for the buttons
	    JPanel btnPanel = new JPanel(new FlowLayout());
	    JButton btnLeft = new JButton("Move Left ");
	    btnPanel.add(btnLeft);
	    int width = 12*w + 3*offX;
	    int height = 12*h + 3*offY;
	    btnPanel.setPreferredSize(new Dimension(width, 40));
	    JButton btnRight = new JButton("Move Right");
	    btnPanel.add(btnRight);
	    //btnPanel.setVisible(true);
	    // Set up a custom drawing JPanel
	    canvas = new DrawCanvas();
	    canvas.setVisible(true);
	    canvas.setPreferredSize(new Dimension(width, height));
	 
	    // Add both panels to this JFrame's content-pane
	    getContentPane().setLayout(new BorderLayout());
	    getContentPane().add(canvas, BorderLayout.CENTER);
	    getContentPane().add(btnPanel, BorderLayout.SOUTH);
	    
	    JPanel panel = new JPanel();
	    getContentPane().add(panel, BorderLayout.CENTER);
	    addKeyListener(this);
	    setPreferredSize(new Dimension(width, height+40));

	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle the CLOSE button
	    setTitle("RPG");
	    setFocusable(true);
	    pack();
	    setVisible(true); // show it
	}
	
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Design frame = new Design();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
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
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@SuppressWarnings("serial")
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

}
