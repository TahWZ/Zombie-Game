package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * Special Action for aiming targets with the sniper
 */
public class AimAction extends Action {

	protected Actor target;
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to aim
	 */
	public AimAction(Actor target) {
		this.target = target;
	}

	@Override
	/**
	 * Performs the required modifications to the focus value of the weapon (Indicating the turns the player aimed a the target)
	 * 
	 * @param actor		the actor performing the attack
	 * @param map		the current map being played
	 */
	public String execute(Actor actor, GameMap map) {

		Sniper weapon = (Sniper)actor.getWeapon();
		weapon.aim(target);
		String result = actor + " aim " + " target for " + weapon.getTurns() + " turns";
		return result;
	}

	@Override
	/**
	 * The option text to be displayed on the menu
	 * 
	 * @param actor		the actor performing the attack
	 * @return The option text to be displayed on the menu
	 */
	public String menuDescription(Actor actor) {
		return "Second option: " + actor + " aims " + target;
	}
}