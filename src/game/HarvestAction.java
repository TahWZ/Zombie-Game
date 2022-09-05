package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

public class HarvestAction extends Action {
	private Location location;
	
	/**
	 * Constructor for Harvest Action
	 * 
	 * @param location - the location for which harvest action will take place
	 */
	public HarvestAction(Location location) {
		this.location = location;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		//Creates a new Food object
		Food food = new Food("Food", 'f', 10);
		//If the actor is a Farmer, the food will drop to the location where it was harvested
		if (actor instanceof Farmer) {
			location.addItem(food);
		//If the actor is a Player, the food will be placed in the Player's inventory
		} else if (actor instanceof Player) {
			actor.addItemToInventory(food);
		}
		//Set the ground back to dirt
		location.setGround(new Dirt());
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " harvests a crop";
	}
}
