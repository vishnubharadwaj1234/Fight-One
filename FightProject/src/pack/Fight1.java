package pack;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.Random;

// This is the main class where all of the methods are called and the story takes place.
public class Fight1 {
	Scanner scan = new Scanner(System.in);
	String name;
	public static void main(String[] args) {
		Fight1 f1 = new Fight1();
		f1.runner();
	}
	public void runner() {
		System.out.println("Welcome to FightOne!");
		System.out.print("What is your name? ");
		name = scan.nextLine();
		System.out.println("Welcome " + name + ". Your fight is going to be against Beezlebub");
		System.out.print("Do you wish to proceed (yes/no)? ");
		String ready1 = scan.next();
		if(ready1.equals("yes")) fight();
		else System.out.println("Coward.");
		System.exit(0);
	}
	// Method where the fight is going to happen
	public void fight() {
		Player p1 = new Player(name);
		Beezlebub beez = new Beezlebub();
		Tools.displayHealth(p1, beez);
		while(p1.health > 0 && beez.health > 0) {
			Tools.oneCycle(p1, beez);
		}
		if (beez.health <= 0) System.out.println("You beat Beezlebub!");
		else System.out.println("You lost!");
	}
}

// This class contains methods and other tools used in the code.
class Tools {
	static Scanner scan = new Scanner(System.in);
	static Random rand = new Random();
	// Codes for one cycle in the fight
	public static void oneCycle(Player player, Beezlebub enemy) {	
		player.blockPower = 0;
		if(enemy.burnTurns > 0) {
			player.health -= 5;
			System.out.println("You took burn damage.");
			enemy.burnTurns--;
			Tools.displayHealth(player, enemy);
		}
		System.out.println("What would you like to do?");
		System.out.println("1. Stab");
		System.out.println("2. Slash");
		System.out.print("3. Thrust and Lunge");
		if(player.talCooldown > 0) System.out.print(" - Cooldown: " + player.talCooldown + " turns");
		System.out.println("\n4. Block next attack");
		System.out.print("Make your choice (1-4): ");
		try {
			int choice = scan.nextInt();
			if (choice == 1) player.stab(enemy);
			else if (choice == 2) player.slash(enemy);
			else if (choice == 3) player.thrustAndLunge(enemy);
			else if (choice == 4) player.blockOneAttack(enemy);
			else {
				System.out.println("You did not choose a valid option.");
				System.out.println("You lost your turn.");
			}
			Tools.displayHealth(player, enemy);
		} catch(InputMismatchException e) {
			System.out.println("You did not enter an integer.");
			System.out.println("You lost your turn.");
		}
		boolean foundMove = false;
		int beezMove = -1;
		while(!foundMove) {
			beezMove = rand.nextInt(100);
			/*
			 * Hell Hornet: 35% chance
			 * Searing Damnation: 30% chance
			 * Infernal Swarm: 20% chance% chance
			 * Lord of the Flames: 15% chance
			 */
			if(beezMove <= 34) enemy.hellHornet(player);
			else if(beezMove <= 64) enemy.searingDamnation(player);
			else if(beezMove <= 84) enemy.infernalSwarm(player);
			else {
				if(enemy.lotfCooldown == 0) {
					enemy.lordOfTheFlames(player);
					foundMove = true;
				}
			}
			if(beezMove <= 84) foundMove = true;
		}
		Tools.displayHealth(player, enemy);
		if(player.talCooldown > 0) player.talCooldown--;
		if(enemy.lotfCooldown > 0) enemy.lotfCooldown--;
	}
	// Displays the health of the player and Beezlebub (formatted)
	public static void displayHealth(Player player, Beezlebub beez) {
		String health1 = player.PLAYERNAME + ": " + player.health + " HP";
		String health2 = beez.ENEMYNAME + ": " + beez.health + " HP";
		System.out.printf("\n%-30s%s\n", health1, health2);
	}
	// Checks if Beezlebub's health is negative
	public static void checkBeezHealth(Beezlebub beez) {
		if(beez.health < 0) beez.health = 0;
	}
	// Checks if the player's health is negative
	public static void checkPlayerHealth(Player player) {
		if(player.health < 0) player.health = 0;
	}
	// Checks if player doesn't take any damage from blocking for infernal swarm
	public static int checkBeezDamage(Player player) {
		if(player.blockPower > 0) return 0;
		return 5;
	}
}

// Class for the player
class Player {
	final String PLAYERNAME;
	int health;
	int blockPower;
	int talCooldown;
	public Player(String name) {
		PLAYERNAME = name;
		health = 100;
		blockPower = 0;
		talCooldown = 0;
	}
	// first move of the player
	public void stab(Beezlebub beez) {
		beez.health -= 20;
		System.out.println("You used stab.");
		Tools.checkBeezHealth(beez);
	}
	// second move of the player
	public void slash(Beezlebub beez) {
		beez.health -= 30;
		System.out.println("You used slash.");
		Tools.checkBeezHealth(beez);
	}
	// third move of the player
	public void thrustAndLunge(Beezlebub beez) {
		if(talCooldown == 0) {
			beez.health -= 50;
			talCooldown = 3;
			System.out.println("You used thrust and lunge.");
			Tools.checkBeezHealth(beez);
		} else {
			System.out.println("This move is still cooling down.");
			System.out.println("You lost your turn.");
		}
	}
	// blocking move
	public void blockOneAttack(Beezlebub beez) {
		blockPower = 15;
		System.out.println("Beezlebub's next attack will do less damage.");
	}
}

// Class for Beezlebub
class Beezlebub {
	int health;
	int burnTurns;
	int lotfCooldown;
	// might choose to add more enemies later
	final String ENEMYNAME;
	public Beezlebub() {
		ENEMYNAME = "Beezlebub";
		health = 200;
		burnTurns = 0;
		lotfCooldown = 0;
	}
	// first move of Beezlebub
	public void hellHornet(Player player) {
		player.health -= 15 - player.blockPower;
		System.out.println("Beezlebub used Hell Hornet.");
		if(player.blockPower > 0) System.out.println("You blocked the attack.");
		Tools.checkPlayerHealth(player);
	}
	// second move of Beezlebub
	public void searingDamnation(Player player) {
		player.health -= 20 - player.blockPower;
		System.out.println("Beezlebub used Searing Damnation.");
		Tools.checkPlayerHealth(player);
	}
	// third move of Beezlebub
	public void infernalSwarm(Player player) {
		player.health -= Tools.checkBeezDamage(player);
		System.out.println("Beezlebub used Infernal Swarm.");
		if(player.blockPower > 0) System.out.println("You blocked the attack.");
		else {
			System.out.println("You will take burn damage for the next 4 turns.");
			burnTurns = 4;
		}
		Tools.checkPlayerHealth(player);
	}
	// fourth move of Beezlebub
	public void lordOfTheFlames(Player player) {
		player.health -= 35 - player.blockPower;
		System.out.println("Beezlebub is using his special move...");
		System.out.println("Lord of the Flames.");
		lotfCooldown = 3;
		System.out.println("Beezlebub won't be able to use his special move for the next 3 turns.");
		Tools.checkPlayerHealth(player);
	}
}