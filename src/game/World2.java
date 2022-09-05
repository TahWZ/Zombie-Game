package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.World;

/**
 * A subclass of the World which is modified with different ways to end the game
 *
 */
public class World2 extends World {

	protected int human_count = 0;
	protected int zombie_count = 0;
	protected boolean foundMambo = false;
	protected Actor mamboMarie;
	protected boolean firstFound = false;
	
	/**
	 * Constructor for World2
	 * 
	 * @param display The display of the appliction currently running
	 */
	public World2(Display display) {
		super(display);
	}

	@Override
	/**
	 * Modified run method to search for the total number of humans and zombies and ends the game accordingly
	 */
	public void run() {
		if (player == null)
			throw new IllegalStateException();

		// initialize the last action map to nothing actions;
		for (Actor actor : actorLocations) {
			lastActionMap.put(actor, new DoNothingAction());
		}

		// This loop is basically the whole game
		findActors();
		while (stillRunning()) {
			findActors();
			display.println("Game Objectives");
			display.println("1) Kill Mambo Marie and all the zombies to win the game");
			display.println("2) If the player dies or all the humans dies, player loses the game\n");
			display.println("Total Humans: " + human_count); //human_count-1 because player is added to the count
			display.println("Total Zombies: " + zombie_count);
			if (foundMambo) {
				display.println("Mambo Marie is found");
			}
			else{
				if(firstFound == false) {
					display.println("Mambo Marie is still lurking around");
				}
				else {
					//Checks if Mambo Marie is alive
					if(!mamboMarie.isConscious()) {
						display.println("Mambo Marie is dead. Kill the remaining zombies to win the game.");
					}
					else {
						display.println("Mambo Marie is still lurking around");
					}
				}
			}
			GameMap playersMap = actorLocations.locationOf(player).map();
			playersMap.draw(display);

			// Process all the actors.
			for (Actor actor : actorLocations) {
				if (stillRunning())
					processActorTurn(actor);
			}

			// Tick over all the maps. For the map stuff.
			for (GameMap gameMap : gameMaps) {
				gameMap.tick();
			}

		}
		//If the player is dead. The player loses the game
		if (!player.isConscious()) {
			display.println("Player is Dead");
			display.println("Player Loses.");
		}
		
		// If all the humans are dead. The player loses the game
		if (human_count == 0) {
			display.println("All Humans are Dead.");
			display.println("Player Loses.");
		}
		//If Mambo Marie and the all the zombies are dead. Display "Player wins"
		else if (zombie_count == 0 && !mamboMarie.isConscious()) {
			display.println("All Zombies and Mambo Marie are Dead.");
			display.println("Player Wins.");
		}
		display.println(endGameMessage());
	}

	@Override
	/**
	 * Modified stillrunning which returns false if any one of the conditions is broken
	 */
	protected boolean stillRunning() {
		//Checks if all the humans are still alive
		if (human_count == 0) {
			return false;
		}
		else if(firstFound) {
			//Checks if Mambo Marie and all the zombies are dead
			if(!mamboMarie.isConscious() && zombie_count == 0) {
				return false;
			}
		}
		//Checks if the player is still in the game
		return actorLocations.contains(player);
	}
	
	/**
	 * A method for finding all the actors and segregates them into humans and zombies
	 */
	public void findActors() {
		human_count = 0;
		zombie_count = 0;
		boolean check = false;
		for (Actor actor : actorLocations) {
			if (actor.hasCapability(ZombieCapability.ALIVE)) {
				human_count += 1;
			} else {
				zombie_count += 1;
			}
			//Finds Mambo Marie
			if (actor.getDisplayChar() == 'M') {
				firstFound = true;
				mamboMarie = (MamboMarie) actor;
				zombie_count -= 1;// Minus 1 for Mambo Marie
				if(actorLocations.locationOf(player).map() == actorLocations.locationOf(actor).map()) {
					foundMambo = true;
					check = true;
				}
			}
		}
		//Minus 1 for the Player
		human_count -= 1;
		//If Mambo Marie is not found, reset foundMambo to zero
		if(check == false) {
			foundMambo = false;
		}
	}
}
