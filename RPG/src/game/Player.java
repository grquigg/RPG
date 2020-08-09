package game;

import java.util.Random;

public class Player {

	Random rn;
	private int baselineHp = 20;
	private int health;
	private int attack;
	private int level;
	private int maxHP;
	private int previousHealth;
	private int exp;
	private int expLim;
	private int baseAttack;
	
	public Player(int hp, int atk, int lvl) {
		rn = new Random();
		level = lvl;
		health = hp * level;
		attack = atk * level;
		baseAttack = atk;
		maxHP = health;
		expLim = 5 * level * (level + 1) / 2;
		exp = 0;
		baselineHp = hp;
	}
	
	public void attack(Monster enemy) {
		enemy.takeDamage(this);
	}
	
	public void attack(Monster enemy, int attack) {
		enemy.takeDamage(attack);
	}
	public void takeDamage(Monster m) {
		previousHealth = health;
		health = health - (rn.nextInt(m.getAttack())+1);
	}
	
	public void takeDamage(int attack) {
		previousHealth = health;
		health = health - attack;
	}
	
	public int getAttack() {
		return attack;
	}
	
	public int getPreviousAttack() {
		return previousHealth - health;
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
	
	public void setHealth(int h) {
		health = h;
	}
	
	public String incExp(Monster m) {
		exp += (m.getLevel() * 3);
		String val;
		System.out.println("Level up");
		if(exp > expLim) {
			val = "Player gained " + Integer.toString(m.getLevel() * 3) + " exp. Level up!";
			while (exp > expLim) { //only updates level once
				System.out.println(level);
				level++;
				expLim += 5 * level;
				attack = baseAttack * level;
				if (attack <= 0) {
					attack = Integer.MAX_VALUE;
					System.out.println("Max value");
				}
				maxHP += baselineHp;
			}
			
		}
		else {
			val = "Player gained " + Integer.toString(m.getLevel() * 3);
		}
		return val;
	}
	
	public void restoreHealth() {
		health = maxHP;
	}
	public int getMaxHealth() {
		return maxHP;
	}
	
	public int getExp() {
		System.out.println("Exp limit is " + expLim);
		return exp;
	}
	
	public void setExp(int x) {
		exp = x;
	}

}
