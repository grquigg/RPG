package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import game.Graph.CanvasState;

public class DrawCanvas extends JPanel {
	
	public Graph game;
	public Map map;
	private int offX;
	private int offY;
	private int h, w;
	private CanvasState c;
	private int numSquares;
	private Player player;
	private Map m2;
	Point selectedSquare;
	
	public DrawCanvas(Graph g, Map m) {
		game = g;
		offX = g.offX;
		offY = g.offY;
		h = g.h;
		w = g.w;
		map = m;
		c = game.c;
		selectedSquare = game.selectedSquare;
	}
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
//   	g.setColor(Color.black);
//		g.drawLine(offX, offY, w*13+offX, offY);
//		g.drawLine(offX, offY, offX, (h*13)+offY);
//		g.drawLine(offX, 13*h+offY, w*13+offX, 13*h+offY);
//		g.drawLine(13*w+offX, offY, 13*w+offX, (h*13)+offY);
     }
     
 	private void drawContentForSquares(Graphics g) {
 		if(c == CanvasState.MAP) {
			for (int r = 0; r <= 12; r++){
				for (int c = 0; c <=12; c++){
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
						g.drawString(""+Integer.toString(game.getMonstersInArea(c, r)),getCenterX(c),getCenterY(r));
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
						game.confineUser();
					}
					if (game.checkVictoryConditions()) {
						g.setColor(Color.green);
						g.fillRect(getTopLeftX(c), getTopLeftY(r), w, h);
					}
				}
			}
			if (game.checkVictoryConditions() && player.getHealth() > 0) { //if the number of squares visited - num
				g.setColor(Color.black);
				g.setFont(new Font("TimesRoman", Font.PLAIN, 49));
				g.drawString("Congrats! You won!", getCenterX(2), getCenterY(6));
				g.setFont(new Font("TimesRoman", Font.PLAIN, 24));
				g.drawString("Press SPACE to continue", getTopLeftX(4), getCenterY(7));
				
				
			}
 		}
 		else if (c == CanvasState.OVERLAY) {
 			System.out.println("Overlay");
 			for (int r = 0; r <= 12; r++) {
 				for (int c = 0; c <= 12; c++) {
 					if (m2.getMapTileAt(c, r).hasEnemyHere()) {
 						g.setColor(Color.red);
 						g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
 						g.setColor(Color.black);
 						g.drawString(""+Integer.toString(game.getMonstersInArea(c, r, m2)),getCenterX(c),getCenterY(r));
 					}
 					else {
 						g.setColor(Color.white);
 						g.fillRect(getTopLeftX(c), getTopLeftY(r),w,h);
 						g.setColor(Color.black);
 						g.drawString(""+Integer.toString(game.getMonstersInArea(c, r, m2)),getCenterX(c),getCenterY(r));
 					}
 				}
 			}
 			//code for overlay goes here
 			/*
 			 * 1. The user can move around wherever on the board they want
 			 * 2. They press enter, they get a screen where they can enter in a number
 			 * 3. Once they press done, the number will be entered in such that it outputs on the screen. The surrounding areas will be lit up in green. 
 			 */
 		}

 	}

   
	private void highlightSelectedSquares(Graphics g) {
		if (selectedSquare !=null && player.getHealth() > 0){
			// paint selected square with red border
			g.setColor(Color.red);
			g.drawRect(getTopLeftX(selectedSquare.x), getTopLeftY(selectedSquare.y),w,h);
				// paint check square with red border
		}
	}
	
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
	
	public void notifyForMapUpdate() {
		map = game.map;
		repaint();
	}
	
	public void updateSelectedSquare() {
		selectedSquare = game.selectedSquare;
		System.out.println(selectedSquare.x);
		System.out.println(selectedSquare.y);
	}
	public void notifyForPlayerUpdate() {
		player = game.player;
		repaint();
	}
  }
