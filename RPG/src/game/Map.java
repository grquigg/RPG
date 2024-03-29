package game;

import java.util.ArrayList;
import java.util.Random;

import game.Graph.Difficulty;

public class Map {

	private int[] nPop = new int[18];
	private int difficulty;
	private int b;
	private int numSquares;
	private int resetsLeft;
	private int numHeals;
	private int[] numArray;
	MapTile[][] array = new MapTile[Constants.NUM_CELLS][Constants.NUM_CELLS];
	int[] currentPosition;
	Random rn = new Random();
	Player player;
	
	public Map(Player p) {
		player = p;
		numHeals = 0;
		currentPosition = new int[2];
		for (int i = 0; i < Constants.NUM_CELLS; i++) {
			for (int j = 0; j < Constants.NUM_CELLS; j++) {
				array[i][j] = new MapTile();
			}
		}
	}
	
	public Map(Player p, int d, int bias, int num) {
		numSquares = num;
		numHeals = 0;
		player = p;
		difficulty = d;
		b = bias;
		currentPosition = new int[2];
		for (int i = 0; i < Constants.NUM_CELLS; i++) {
			for (int j = 0; j < Constants.NUM_CELLS; j++) {
				array[i][j] = new MapTile();
			}
		}
	}
	
	public void populateMap(int d, int bias, Difficulty diff) {
		b = bias;
		difficulty = d;
		numArray = new int[difficulty];
		populate(difficulty);
		//method for calculating the constants for each monster
		for (int i = 0; i < d; i++) {
			int enA = rn.nextInt(Constants.NUM_CELLS-1);
			int enB = rn.nextInt(Constants.NUM_CELLS-1);
			while(array[enA][enB].getEnemy() != null) {
				enA = rn.nextInt(Constants.NUM_CELLS-1);
				enB = rn.nextInt(Constants.NUM_CELLS-1);
			}
			if(i % 10 == 0 && diff == Difficulty.HARD) {
				array[enA][enB].setEnemy(new Necromancer(6, 1, (numArray[i] + bias)));
			} else {
				array[enA][enB].setEnemy(new Monster(6, 1, (numArray[i]) + bias));
			}
			if (i % 5 == 0) {
				int tA = rn.nextInt(Constants.NUM_CELLS-1);
				int tB = rn.nextInt(Constants.NUM_CELLS-1);
				while(array[tA][tB].isTreasureHere()) {
					tA = rn.nextInt(Constants.NUM_CELLS-1);
					tB = rn.nextInt(Constants.NUM_CELLS-1);
				}
				array[tA][tB].setNumTreasure(1);
				numHeals++;
			}

		}
		player.setNumHeals(numHeals);
	}
	
	public void populate(int d) {
		int total = d;
		int iterVar = 1;
		while (total >= 0) {
			System.out.println(total);
			for (int i = 0; i < iterVar; i++) {
				nPop[i]++;
				total--;
			}
			iterVar++;
		}
		System.out.println(nPop);
		
		int index = 0;
		for (int i = 0; i < nPop.length; i++) {
			for (int j = 0; j < nPop[i]; j++) {
				if (index >= numArray.length) return;
				numArray[index] = i+1;
				index++;
			}
		}
	}
	
	public void setPlayerPosition(int [] position) {
		currentPosition = position;
	}
	
	public void setPlayerPosition() {
		currentPosition[0] = rn.nextInt(Constants.NUM_CELLS-1);
		currentPosition[1] = rn.nextInt(Constants.NUM_CELLS-1);
	}
	
	public int[] getPlayerPosition() {
		return currentPosition;
	}
	
	public MapTile returnCurrentMapTile() {
		return array[currentPosition[0]][currentPosition[1]];
	}
	
	public MapTile getMapTileAt(int x, int y) {
		return array[x][y];
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public int getBias() {
		return b;
	}
	
	public int getNumSquares() {
		return numSquares;
	}
	
	public void setResetsLeft(int x) {
		resetsLeft = x;
	}
	
	public int getResetsLeft() {
		return resetsLeft;
	}
	
	public void moveEnemies() {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				//i is the column, j is the row
				if(array[i][j].hasEnemyHere() && array[i][j].getEnemy().isAlive()) {
					//System.out.println("Monster at " + Integer.toString(i) + ", " + Integer.toString(j));
					int direction = rn.nextInt(4);
					if(isValid(direction, i, j)) { //how do we protect against infinite loops with this though?
						moveMonster(array[i][j], direction, i, j);
					}
				}
			}
		}
	}

	private void moveMonster(MapTile mapTile, int direction, int y, int x) {
		Monster temp = mapTile.getEnemy();
		switch(direction) {
		case 0:
			array[y][x-1].setEnemy(temp);
			array[y][x].setEnemy(null);
			break;
		case 1:
			array[y+1][x].setEnemy(temp);
			array[y][x].setEnemy(null);
			break;
		case 2:
			array[y][x+1].setEnemy(temp);
			array[y][x].setEnemy(null);
			break;
		case 3:
			array[y-1][x].setEnemy(temp);
			array[y][x].setEnemy(null);
			break;
		}
		
	}

	private boolean isValid(int direction, int y, int x) {
		switch(direction) {
		case 0: //North
			if (x != 0 && !array[y][x-1].hasBeenVisited() && !array[y][x-1].hasEnemyHere()) {
				return true;
			}
			else {
				return false;
			}
		case 1: //East
			if (y != array.length-1 && !array[y+1][x].hasBeenVisited() && !array[y+1][x].hasEnemyHere()) {
				return true;
			} else {
				return false;
			}
		case 2: //South
			if(x != array.length-1 && !array[y][x+1].hasBeenVisited() && !array[y][x+1].hasEnemyHere()) {
				return true;
			} else {
				return false;
			}
		case 3: //North
			if(y != 0 && !array[y-1][x].hasBeenVisited() && !array[y-1][x].hasEnemyHere()) {
				return true;
			} else {
				return false;
			}
		default: return false;
		}
	}
	
	public void clearIndexes() {
		for (int i = 0; i < Constants.NUM_CELLS; i++) {
			for (int j = 0; j < Constants.NUM_CELLS; j++) {
				array[i][j].setIndex(-1);
			}
		}
	}
	
	public ArrayList<MapTile> getMonstersOnMap() {
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();
		for (int i = 0; i < Constants.NUM_CELLS; i++) {
			for (int j = 0; j < Constants.NUM_CELLS; j++) {
				if(getMapTileAt(i, j).hasEnemyHere()) {
					tiles.add(getMapTileAt(i, j));
				}
			}
		}
		return tiles;
	}
	
	public ArrayList<MapTile> getDeadMonstersOnMap() {
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();
		for (int i = 0; i < Constants.NUM_CELLS; i++) {
			for (int j = 0; j < Constants.NUM_CELLS; j++) {
				if(getMapTileAt(i, j).hasEnemyHere() && !getMapTileAt(i, j).getEnemy().isAlive()) {
					tiles.add(getMapTileAt(i, j));
				}
			}
		}
		return tiles;
	}
	
	public ArrayList<MapTile> getNecromancers() {
		ArrayList<MapTile> tiles = new ArrayList<MapTile>();
		for (int i = 0; i < Constants.NUM_CELLS; i++) {
			for (int j = 0; j < Constants.NUM_CELLS; j++) {
				if(getMapTileAt(i, j).hasEnemyHere() && getMapTileAt(i, j).getEnemy().isAlive() && getMapTileAt(i, j).getEnemy().getMonsterType() == "Necromancer") {
					tiles.add(getMapTileAt(i, j));
				}
			}
		}
		return tiles;
		
	}
	
	public void generateMap(int nTiles) {
		System.out.println("Generate map");
		ArrayList<MapTile> availableTiles = new ArrayList<MapTile>();
		availableTiles.add(new MapTile());
		int r = 0;
		System.out.println(r);
		int t = rn.nextInt(4);
		System.out.println(t);
		switch(t) {
			case 0: //north
				
		}
	}
}
