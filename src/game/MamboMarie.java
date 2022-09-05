package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;

/**
 * The "boss" of the game which summons zombies, wander and chant. It does not attack but is a major threat in the map for the player
 *
 */
public class MamboMarie extends ZombieActor {
	
	private int turns,health;
	private boolean chanting;
	private Behaviour[] behaviours = {
			new ChantBehaviour(5),
			new WanderBehaviour()
	};

	/**
	 * Constructor
	 * 
	 * @param health 		The health amount MamboMarie should have
	 */
	public MamboMarie(int health) {
		super("Mambo Marie", 'M', health, ZombieCapability.UNDEAD);
		this.health = health;
		this.turns = 0;
	}
	
	/**
	 * Retrieves Mambo Marie's health amount
	 * @return The health amount for Mambo Marie
	 */
	public int getHealth() {
		return this.health;
	}
	
	/**
	 * Retrieves the amount of turns Mambo Marie was present on the map
	 * @return The number of turns Mambo Marie was present on the map
	 * 
	 */
	public int getTurns() {
		return this.turns;
	}

	/**
	 * Retrieves a boolean value which indicates whether Mambo Marie is performing a chant
	 * 
	 * @return Boolean value which determines whether Mambo Marie performed a chant last turn
	 */
	public boolean isChanting() {
		return this.chanting;
	}
	
	/**
	 * Set the boolean value of chanting
	 * 
	 * @param chanting 		The new boolean value to be set on chanting
	 */
	public void setChanting(boolean chanting) {
		this.chanting = chanting;
	}
	
	@Override
	/**
	 * Returns the appropriate action Mambo Marie should perform based on its behavior
	 * 
	 * @param actions 		the actions allowed
	 * @param lastAction 	the action performed in the previous turn
	 * @param map 			the map of the game
	 * @param display 		the display of the game
	 * @return Action 		Mambo Marie should perform
	 */
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		this.turns += 1;
		if (turns == 30) {
			return new VanishAction();
		}
		for (Behaviour behaviour : behaviours) {
			Action action = behaviour.getAction(this, map);
			if (action != null)
				return action;
		}
		return new DoNothingAction();
	}
}
