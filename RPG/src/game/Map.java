package game;

import java.util.Random;

public class Map {

	private int[] nPop = new int[18];
	private int difficulty;
	private int b;
	private int numSquares;
	private int resetsLeft;
	private int[] numArray;
	MapTile[][] array = new MapTile[13][13];
	int[] currentPosition;
	Random rn = new Random();
	Player player;
	
	public Map(Player p) {
		player = p;
		
		currentPosition = new int[2];
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				array[i][j] = new MapTile();
			}
		}
	}
	
	public Map(Player p, int d, int bias, int num) {
		numSquares = num;
		player = p;
		difficulty = d;
		b = bias;
		currentPosition = new int[2];
		for (int i = 0; i < 13; i++) {
			for (int j = 0; j < 13; j++) {
				array[i][j] = new MapTile();
			}
		}
	}
	
	public void populateMap(int d, int bias) {
		b = bias;
		difficulty = d;
		numArray = new int[difficulty];
		populate(difficulty);
		//method for calculating the constants for each monster
		for (int i = 0; i < d; i++) {
			int enA = rn.nextInt(12);
			int enB = rn.nextInt(12);
			while(array[enA][enB].getEnemy() != null) {
				enA = rn.nextInt(12);
				enB = rn.nextInt(12);
			}
			array[enA][enB].setEnemy(new Monster(6, 1, (numArray[i]) + bias));
			if (i % 5 == 0) {
				int tA = rn.nextInt(12);
				int tB = rn.nextInt(12);
				while(array[tA][tB].isTreasureHere()) {
					tA = rn.nextInt(12);
					tB = rn.nextInt(12);
				}
				array[tA][tB].setNumTreasure(1);
			}

		}
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
		currentPosition[0] = rn.nextInt(12);
		currentPosition[1] = rn.nextInt(12);
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
					System.out.println("Monster at " + Integer.toString(i) + ", " + Integer.toString(j));
					boolean [] choices = {true, true, true, true};
					
					int direction = rn.nextInt(4);
					while(!isValid(direction, i, j)) { //how do we protect against infinite loops with this though?
						choices[direction] = false;
						System.out.println("Monster cannot move there");
						if(!(choices[0] || choices[1] || choices[2] || choices[3])) {
							System.out.println("Movement is impossible");
							break;
						}
						while(!choices[direction]) {
							direction = rn.nextInt(4);
							System.out.println("Already determined that the monster cannot move there");
						}
					}
					System.out.println("Found a place for the monster to move");
					moveMonster(array[i][j], direction, i, j);
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
}
