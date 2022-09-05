package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.PickUpItemAction;

public class Farmer extends Human {
	
	private Behaviour[] behaviours = {new FarmerBehaviour(), new WanderBehaviour()};
	
	/**
	 * Constructor for Farmer
	 * 
	 * @param name The name of the farmer
	 */
	public Farmer(String name) {
		super(name, 'F', 50);
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		
		Location currentLocation = map.locationOf(this);
		// If the Farmer has suffered some injuries, the Farmer will consume a food if it is already in its inventory
		if (this.hitPoints != this.maxHitPoints) {
			for (Item item : this.getInventory()) {
				if (item instanceof Food) {
					return new ConsumeAction((Food) item);
				}
			}
			// Otherwise, check if the current location has a food in it, if so the Farmer can pick it up
			if (currentLocation.getItems() instanceof Food) {
				Food food = (Food) currentLocation.getItems();
				return new PickUpItemAction(food);
			}
		}
		
		for (Behaviour behaviour : behaviours) {
			Action action = behaviour.getAction(this, map);
			if (action != null)
				return action;
		}
		return new DoNothingAction();
	}
}
