package game;

public class GraphNode extends MapTile {
	public boolean[] availableEdges;
	private int xCor;
	private int yCor;
	public GraphNode top;
	public GraphNode bottom;
	public GraphNode right;
	public GraphNode left;
	
	public GraphNode() {
		availableEdges = new boolean[4];
	}
	
	public void markCorner(int a) {
		availableEdges[a] = true;
	}
	
	public void setCoordinates(int x, int y) {
		
	}
}
