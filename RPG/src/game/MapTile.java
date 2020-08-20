package game;

public class MapTile {

	private int nTreasure;
	private Monster enemy = null;
	private boolean hasTreasure;
	private boolean visited;
	private boolean isMarked;
	
	public MapTile() {
		hasTreasure = false;
		visited = false;
		isMarked = false;
		nTreasure = 0;
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
	
	public boolean hasBeenVisited() {
		return visited;
	}
}
