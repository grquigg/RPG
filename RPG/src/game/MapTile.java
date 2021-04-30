package game;

public class MapTile {

	private int nTreasure;
	private Monster enemy = null;
	private boolean hasTreasure;
	private boolean visited;
	private boolean isMarked;
	private int index;
	
	public MapTile() {
		hasTreasure = false;
		visited = false;
		isMarked = false;
		nTreasure = 0;
		index = -1;
	}
	
	public void setEnemy(Monster m) {
		enemy = m;
	}
	
	public Monster getEnemy() {
		return enemy;
	}
	
	public void setNumTreasure(int n) {
		nTreasure = n;
		hasTreasure = true;
		
	}
	
	public int getTreasure() {
		return nTreasure;
	}
	
	public boolean isTreasureHere() {
		return hasTreasure;
	}
	
	public boolean hasEnemyHere() {
		if (enemy == null) {
			return false;
		}
		else return true;
	}
	
	public void toggleMarked() {
		if(!visited) {
			isMarked = !isMarked;
		}
	}
	
	public boolean hasBeenMarked() {
		return isMarked;
	}
	
	public void toggleVisited() {
		visited = true;
		isMarked = false;
	}
	
	public void switchVisited() {
		visited = false;
		isMarked = false;
	}
	public boolean hasBeenVisited() {
		return visited;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
}
