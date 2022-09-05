package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * The behaviour which decides whether Mambo Marie should chant the current turn
 * 
 */
public class ChantBehaviour implements Behaviour {
	
	private int zombieCount;
	
	/**
	 * Constructor
	 * 
	 * @param zombieCount the number of zombies to spawn with the chant action
	 */
	public ChantBehaviour(int zombieCount) {
		this.zombieCount = zombieCount;
	}

	@Override
	/**
	 * Returns the action or null based on whether Mambo Marie can perform chant action
	 * 
	 * @param actor		the actor performing the shoot action 
	 * @param map		the current map being played
	 * @return null if the action is not valid
	 * @return chant action if the conditions allow it
	 */
	public Action getAction(Actor actor, GameMap map) {
		// is mambo marie capable of chanting this turn
		if (((MamboMarie)actor).getTurns()%10==0 || ((MamboMarie)actor).isChanting()) {
			return new ChantAction(this.zombieCount);
		}
		return null;
	}

}
