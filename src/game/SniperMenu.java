package game;

import java.util.ArrayList;
import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Menu;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.WeaponItem;

public class SniperMenu extends Action{
	
	Actor target;
	Menu menu = new Menu();
	Display display;
	
	public SniperMenu(Actor target,Display display) {
		this.display = display;
		this.target = target;
	}
	
	@Override
	public String execute(Actor actor, GameMap map) {
		Sniper weapon = (Sniper)actor.getWeapon();
		Actions actions = new Actions();
		actions.add(new SnipeAction(target));
		if (weapon.getTurns() < 2) {
			actions.add(new AimAction(target));
		}
		return menu.showMenu(actor, actions, display).execute(actor, map);
	}
	
	@Override
	public String menuDescription(Actor actor) {
		String result = "";
		Sniper weapon = (Sniper)actor.getWeapon();
		if (weapon.isFocus()) {
			result = actor + " uses sniper (Total turns focused: " + weapon.getTurns() + ")";
		}
		else {
			result = "Choose target: " + target;
		}
		return result;
	}
	
}
