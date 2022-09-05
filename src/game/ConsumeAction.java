package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

public class ConsumeAction extends Action {

	private ConsumableItem item;
	
	/**
	 * Constructor for the ConsumeAction
	 * 
	 * @param item - an item of type ConsumableItem
	 */
	public ConsumeAction(ConsumableItem item) {
		this.item = item;
	}
	
	@Override
	/**
	 * Heals the actor based on the item's value and removes the item from the actor's inventory
	 * 
	 * @param actor - the actor attempting to consume the item
	 * @param map - the current map being played
	 */
	public String execute(Actor actor, GameMap map) {
		actor.heal(item.getConsumableValue());
		actor.removeItemFromInventory(item);
		return menuDescription(actor) + actor + " heals " + item.getConsumableValue() + " health ";
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " consumes an item. ";
	}
}
