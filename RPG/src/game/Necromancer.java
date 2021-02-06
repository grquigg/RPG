package game;

import java.util.ArrayList;

public class Necromancer extends Monster {

	public Necromancer(int hp, int atk, int lvl) {
		super(hp, atk, lvl);
		setMonsterType("Necromancer");
	}
	
	public boolean resurrectMonster(Map m, int numMonstersLeft, int numMonsters) {
		if(this.isAlive()) {
			if(numMonsters - 5 > numMonstersLeft) {
				System.out.println("Resurrection");
				ArrayList<MapTile> map = m.getDeadMonstersOnMap();
				if (map.size() != 0) {
					int a = rn.nextInt(map.size());
					MapTile t = map.get(a);
					t.getEnemy().resurrect();
					t.switchVisited();
					return true;
				}
			}
		} else {
			System.out.println("Monster is dead");
		}
		return false;
	}

}
