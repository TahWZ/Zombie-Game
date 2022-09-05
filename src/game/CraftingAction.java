package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.WeaponItem;

public class CraftingAction extends Action{

	protected WeaponItem weapon;
	protected MaterialItem material;
	
	public CraftingAction(MaterialItem item) {
		this.material = item;
		this.weapon = item.Crafting();
	}
	
	@Override
	/**
	 * Removes the material item from the inventory and add the craftable weapon
	 * 
	 * @param actor		the actor attempting to craft a weapon
	 * @param map		the current map being played
	 */
	public String execute(Actor actor, GameMap map) {
		// TODO Auto-generated method stub
		//Remove material item
		actor.removeItemFromInventory(material);
		//Add craftable weapon
		actor.addItemToInventory(weapon);
		return menuDescription(actor);
	}
	
	public String menuDescription(Actor actor) {
		return actor + " crafts " + weapon;
	}
}
