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
	private String monsterType;
	
	public Monster(int hp, int atk, int lvl) {
		setMonsterType("Normal");
		rn = new Random();
		health = (int) (lvl * lvl * hp);
		attack = lvl * atk;
		if (attack <= 0) {
			attack = Integer.MAX_VALUE;
		}
		level = lvl;
		isAlive = true;
		maxHP = health;
	}
	
	public void attack(Player player) {
		player.takeDamage(this);
	}
	
	public void attack(Player player, int attack) {
		player.takeDamage(attack);
	}
	
	public void takeDamage(Player player) {
		previousHealth = health;
		//System.out.println(previousHealth);
		health -= (rn.nextInt(player.getAttack())+1);
	}
	
	public void takeDamage(int attack) {
		previousHealth = health;
		//System.out.println(previousHealth);
		health -= attack;
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
	
	public void setMonsterType(String s) {
		monsterType = s;
	}
	
	public String getMonsterType() {
		return monsterType;
	}
	
	public void resurrect() {
		health = maxHP;
		isAlive = true;
	}
}
