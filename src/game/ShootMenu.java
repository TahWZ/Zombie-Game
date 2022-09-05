package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Menu;

/**
 * Special action which opens a Menu for the player which displays options which 
 * vary based on the ranged weapon equipped by the player.
 * 
 */
public class ShootMenu extends Action{
	
	Location location;
	Menu menu = new Menu();
	Display display;
	
	/**
	 * Constructor
	 * 
	 * @param location The location of the player when performing the ShootMenu action.
	 * @param display The application display which the user interacts with
	 */
	public ShootMenu(Location location,Display display) {
		this.display = display;
		this.location = location;
	}
	
	@Override
	/**
	 * Displays a menu with varying options based on the weapon equipped by the player
	 * 1. Shotgun: shows every direction as options to select for the player to perform the SpreadShot action on
	 * 2. Sniper: checks all enemy actors in range to perform sniper actions on (after further selections on the SniperMenu)
	 * 
	 * @param actor		the actor performing the shoot action (Player)
	 * @param map		the current map being played
	 * @return the String result of the action selected by the player
	 */
	public String execute(Actor actor, GameMap map) {
		//Stores action options for players to select
		Actions actions = new Actions();
		//An array containing string names for directions
		String[] directions = new String[] {"North", "North-East", "East", "South-East", "South", "South-West", "West","North-West"};
		RangedWeapon weapon = (RangedWeapon) actor.getWeapon();
		for (int i = 0; i < directions.length; i++) {
			//Checks if weapon is a shotgun
			if (weapon.hasCapability(RangeCapability.SPREAD))
				actions.add(new SpreadshotAction(directions[i],weapon.searchTargets(map, this.location, directions[i], ZombieCapability.UNDEAD)));
			//Checks if weapon is a sniper
			else if (weapon.hasCapability(RangeCapability.SNIPE)) {
				Sniper sniper = (Sniper)weapon;
				//Checks if the aim action was used last turn
				if (sniper.isFocus()) {
					sniper.loseFocus();
				}
				//List of all targets possible to perform actions on
				ArrayList<Actor> targets = weapon.searchTargets(map, this.location, directions[i], ZombieCapability.UNDEAD);
				//List of unique targets to avoid duplicate options
				ArrayList<Actor> uniqueTargets = new ArrayList<Actor>();
				for (int j = 0; j < targets.size(); j++) {
					if (!uniqueTargets.contains(targets.get(j))) {
						actions.add(new SniperMenu(targets.get(j),this.display));
						uniqueTargets.add(targets.get(j));
					}
				}
			}
		}
		return menu.showMenu(actor, actions, display).execute(actor, map);
	}
	
	@Override
	/**
	 * Provides the appropriate menu option text based on the weapon used
	 * 
	 * @param actor		the actor performing the shot
	 * @return The text to be shown on the menu option
	 */
	public String menuDescription(Actor actor) {
		RangedWeapon weapon = (RangedWeapon) actor.getWeapon();
		String result = "";
		if (weapon.hasCapability(RangeCapability.SPREAD)) {
			result = actor + " uses shotgun with " + weapon.getAmmo() + " ammo (Opens sub-menu for player to select direction to shoot)";
		} else if (weapon.hasCapability(RangeCapability.SNIPE)) {
			Sniper sniper = (Sniper)weapon;
			result = actor + " uses sniper with " + weapon.getAmmo() + " ammo ";
			if (sniper.isFocus()){
				result += "(Warning: will lose focus on current target: " + sniper.getTarget() + ")";
			} else {
				result += "(Opens sub-menu for player to select target to snipe)";
			}
		}
		return result;
	}
	
}