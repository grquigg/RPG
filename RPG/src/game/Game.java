package game;


import java.util.Scanner;
import java.util.Random;

public class Game {
	
	GameModes currentMode;
	Random rn = new Random();
	
	public enum GameModes {Map, Menu, Combat, Win, Loss, Quit}

	public void onUserInput(String i) {
		switch(i) {
		case "Quit":
			System.out.println("Quitting the game:");
			currentMode = GameModes.Quit;
			break;
		case "North":
			System.out.println("Moving north");
			break;
		case "East":
			System.out.println("Moving east");
			break;
		case "South":
			System.out.println("Moving south");
			break;
		case "West":
			System.out.println("Moving west");
			break;
		}
	}
	
	public void prompt() {
		switch(currentMode) {
		case Map:
			System.out.println("Use the WASD keys to move");
			break;
		case Menu:
			System.out.println("Menu");
			break;
		case Combat:
			System.out.println("Fight!");
			break;
		case Win:
			System.out.println("Congratulations! You won!");
			break;
		case Loss:
			System.out.println("Sorry! You suck!");
		}
	}

}
