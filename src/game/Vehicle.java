package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Item;

/**
 * An item that is not portable and place randomly in the game map, allows the player to travel between maps
 *
 */
public class Vehicle extends Item{
	
	/**
	 * Constructor for Vehicle
	 * 
	 * @param name The name of the vehicle
	 * @param displayChar The character for the display to show
	 * @param portable Can actors pick up the item
	 */
	public Vehicle(String name, char displayChar, boolean portable) {
		super(name,displayChar,portable);
	}
	
	/**
	 * Adds allowable action to this item
	 * 
	 * @param action The actions to be performed
	 */
	public void addAction(Action action) {
		this.allowableActions.add(action);
	}
}
