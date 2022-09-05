package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.PickUpItemAction;

/**
 * Class representing an ordinary human.
 * 
 * 
 * @author ram
 *
 */
public class Human extends ZombieActor {
	private Behaviour behaviour = new WanderBehaviour();

	/**
	 * The default constructor creates default Humans
	 * 
	 * @param name the human's display name
	 */
	public Human(String name) {
		super(name, 'H', 50, ZombieCapability.ALIVE);
	}

	/**
	 * The protected constructor can be used to create subtypes of Human, such as
	 * the Player
	 * 
	 * @param name        the human's display name
	 * @param displayChar character that will represent the Human in the map
	 * @param hitPoints   amount of damage that the Human can take before it dies
	 */
	protected Human(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints, ZombieCapability.ALIVE);
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		Action action = behaviour.getAction(this, map);
		Location currentLocation = map.locationOf(this);

		// If the human has suffered some injuries, the Human will consume a food if it is already in its inventory
		if (this.hitPoints != this.maxHitPoints) {
			for (Item item : this.getInventory()) {
				if (item instanceof Food) {
					return new ConsumeAction((Food)item);
				}
			}
			// Otherwise, check if the current location has a food in it, if so the Human can pick it up
			if (currentLocation.getItems() instanceof Food) {
				Food food = (Food) currentLocation.getItems();
				return new PickUpItemAction(food);
			}
		}
		if (action != null)
			return action;
		// FIXME humans are pretty dumb, maybe they should at least run away from zombies?
		return new DoNothingAction();
	}
}
