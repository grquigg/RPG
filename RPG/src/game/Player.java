package game;

import java.util.Random;

public class Player {

	Random rn;
	private int health;
	private int attack;
	private int level;
	private int maxHP;
	private int previousAttack;
	
	public Player(int hp, int atk, int lvl) {
		rn = new Random();
		health = hp;
		attack = atk;
		level = lvl;
		maxHP = health;
	}
	
	public void attack(Monster enemy) {
		enemy.takeDamage(this);
	}
	
	public void takeDamage(Monster m) {
		previousAttack = (rn.nextInt(m.getAttack()) + 1);
		health = health - previousAttack;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getPreviousAttack() {
		return previousAttack;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void increaseHealth() {
		if (health == maxHP) return;
		health++;
	}
	
	public int getLevel() {
		return level;
	}
	
}
