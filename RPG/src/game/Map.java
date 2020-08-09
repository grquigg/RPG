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
}
