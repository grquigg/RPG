package game;

import java.util.Random;

public class Monster {

	Random rn;
	private int health;
	private int attack;
	private int level;
	private int maxHP;
	private boolean isAlive;
	private int previousHealth;
	
	public Monster(int hp, int atk, int lvl) {
		rn = new Random();
		health = (int) (lvl * lvl * hp);
		attack = lvl * atk;
		if (attack <= 0) {
			attack = Integer.MAX_VALUE;
			System.out.println("Monsters in my head");
		}
		level = lvl;
		isAlive = true;
		maxHP = health;
	}
	
	public void attack(Player player) {
		player.takeDamage(this);
	}
	
	public void takeDamage(Player player) {
		previousHealth = health;
		//System.out.println(previousHealth);
		health -= (rn.nextInt(player.getAttack())+1);
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getPreviousAttack() {
		return previousHealth - health;
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
	
	public int getMaxHealth() {
		return maxHP;
	}
}
