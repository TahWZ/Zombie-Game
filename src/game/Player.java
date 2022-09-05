package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Menu;

/**
 * Class representing the Player.
 */
public class Player extends Human {

	private Menu menu = new Menu();

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}
	
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// Handle multi-turn Actions

		// The Player now has the ability to consume items
		for (Item item : this.getInventory()) {
			//Consume
			if (item instanceof ConsumableItem) {
				actions.add(new ConsumeAction((ConsumableItem) item));
			//Craft
			} else if (item instanceof MaterialItem) {
				actions.add(new CraftingAction((MaterialItem) item));
			} 
		}

		Location currentLocation = map.locationOf(this);
		
		if (getWeapon() instanceof Range && ((RangedWeapon) getWeapon()).getAmmo() >= 1) {
			if (getWeapon() instanceof Sniper && ((Sniper)getWeapon()).isFocus()) {
				Sniper weapon = (Sniper)getWeapon();
				if (!(lastAction instanceof ShootMenu||lastAction instanceof SniperMenu)) {
					weapon.loseFocus();
				} else {
					actions.add(new SniperMenu(weapon.getTarget(),display));
				}
			}
			actions.add(new ShootMenu(currentLocation,display));
		}

		// If the player stands on a ripe crop, he can harvest it, and the food will be
		// added directly to the player's inventory
		if (currentLocation.getGround() instanceof Crop) {
			Crop crop = (Crop) currentLocation.getGround();
			if (crop.isRipe()) {
				actions.add(new HarvestAction(currentLocation));
			}
		}

		// If the player stands near a cop, he can harvest it, and the food will be
		// added directly to the player's inventory
		for (Exit exit : map.locationOf(this).getExits()) {
			Location destination = exit.getDestination();
			// If the player stands next to the crop, it can harvest it
			if (destination.getDisplayChar() == 'R') {
				actions.add(new HarvestAction(destination));
				break;
			}
		}
		//Add a quit Action
		actions.add(new QuitAction());
		
		if (lastAction.getNextAction() != null)
			return lastAction.getNextAction();
		return menu.showMenu(this, actions, display);
	}
}
