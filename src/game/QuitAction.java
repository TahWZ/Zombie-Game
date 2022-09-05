package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

/**
 * This action allows the player to quit the game and appears as one of the menu options
 *
 */
public class QuitAction extends Action{

	@Override
	/**
	 * @param actor - the actor executing this action
	 * @param map - the map the actor is currently in
	 */
	public String execute(Actor actor, GameMap map) {
		System.exit(0);
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " quits game";
	}

}
