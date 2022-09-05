package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * An action unique to Mambo Marie, allows her to vanish from the map. Having an item added to store her state before vanishing.
 *
 */
public class VanishAction extends Action {
	
	/**
	 * Constructor
	 */
	public VanishAction() {
	}

	@Override
	/**
	 * Mambo Marie will be removed from the map, but its state will be store in an item called MarieEssence
	 * 
	 * @param actor		the actor performing the vanish (Mambo Marie)
	 * @param map		the current map being played
	 * @return the String result of the action
	 */
	public String execute(Actor actor, GameMap map) {
		MamboMarie MM = (MamboMarie)actor;
		map.locationOf(actor).addItem(new MarieEssence(MM.getHealth()));
		map.removeActor(actor);
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " has vanished, but she shall return";
	}
}
