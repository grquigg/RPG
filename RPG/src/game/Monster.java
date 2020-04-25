package game;

import java.util.Random;

public class Monster {

	Random rn;
	private int health;
	private int attack;
	private int level;
	private boolean isAlive;
	private int previousAttack;
	
	public Monster(int hp, int atk, int lvl) {
		rn = new Random();
		health = hp;
		attack = atk;
		level = lvl;
		isAlive = true;
	}
	
	public void attack(Player player) {
		player.takeDamage(this);
	}
	
	public void takeDamage(Player player) {
		previousAttack = (rn.nextInt(player.getAttack()) + 1);
		health = health - previousAttack;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getPreviousAttack() {
		return previousAttack;
	}
	
	
	public int getLevel() {
		return level;
	}
	
	public void isDead() {
		isAlive = false;
	}
	
	public boolean isAlive() {
		return isAlive;
	}
}
