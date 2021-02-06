package game;

import java.util.ArrayList;

public class Set {
	
	private ArrayList<int []> ray;
	private int size;
	private int index;
	
	public Set() {
		ray = new ArrayList<int []>();
		size = 0;
	}
	
	public void addToSet(int [] arr) {
		ray.add(arr);
		size++;
		
	}
	

	public int getSize() {
		return size;
	}
	
	public int [] getCell(int x) {
		return ray.get(x);
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	public void merge(Set b, Map map) {
		for (int i = 0; i < b.getSize(); i++) {
			int [] arr = b.getCell(i);
			map.getMapTileAt(arr[0], arr[1]).setIndex(index);
			ray.add(b.getCell(i));
			size++;
		}
	}
}
