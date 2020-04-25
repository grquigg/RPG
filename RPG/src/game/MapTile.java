package game;

public class MapTile {

	private int nEnemies;
	private int nTreasure;
	private Monster enemy = null;
	private boolean hasTreasure;
	private boolean visited;
	
	public MapTile() {
		hasTreasure = false;
		visited = false;
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
	
	public boolean isTreasureHere() {
		return hasTreasure;
	}
	
	public boolean hasEnemyHere() {
		if (enemy == null) {
			return false;
		}
		else return true;
	}
	
	public void toggleVisited() {
		visited = true;
	}
	
	public boolean hasBeenVisited() {
		return visited;
	}
}
