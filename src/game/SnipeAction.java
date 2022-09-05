package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Special Action is a variant of an attack performed when a sniper weapon is used.
 */
public class SnipeAction extends Action {

	protected Actor target;
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to snipe
	 */
	public SnipeAction(Actor target) {
		this.target = target;
	}

	@Override
	/**
	 * Performs the required modifications on the target and actor. Effects will vary:
	 * 1. if the weapon performed an aim action once, damage is doubled
	 * 2. if the weapon performed an aim action twice before this, it becomes an instakill
	 * 3. the losing limbs function is called only if the result wasn't an instakill
	 * 
	 * @param actor		the actor performing the attack
	 * @param map		the current map being played
	 */
	public String execute(Actor actor, GameMap map) {
		boolean instaKill = false;
		String damageText, result;
		Sniper weapon = (Sniper)actor.getWeapon();
		weapon.removeAmmo(1);
		int hitChance = 75,damage = weapon.damage();
		
		//Modifications based on how the player aim in the previous turns
		if (!weapon.focus) {
			damageText = "Standard damage";
		} else if (weapon.getTurns() == 1) {
			hitChance = 90;
			damage *= 2;
			damageText = "Double damage";
		} else {
			instaKill = true;
			damageText = "an instakill";
		}

		//Chance to miss if no aim or aim turn is 1
		if (rand.nextInt(100)+1 > hitChance && instaKill) {
			return actor + " misses " + target + ".";
		}
			
		result = actor + " " + weapon.verb() + " " + target + " with " + damageText;
		
		//Removes target if instaKill is activated
		if (!instaKill) {
			target.hurt(damage);
			result += " dealing " + damage + ", " + losingLimb(map);
			result += losingLimb(map);
		}
		
		//Modification on target death
		if (!target.isConscious()||instaKill) {
			//Item corpse = new PortableItem("dead " + target, '%');
			
			Actions dropActions = new Actions();
			for (Item item : target.getInventory())
				dropActions.add(item.getDropAction());
			for (Action drop : dropActions)	
				drop.execute(target, map);
			map.removeActor(target);	
			
			//Text to state the corpse being placed
			result += System.lineSeparator() + target + " is killed.";
		}
		//Remove all focus from the weapon
		weapon.loseFocus();
		return result;
	}

	/**
	 * Performs the required modifications on the target as: 
	 * 1. Zombies have a chance to lose limbs and drop its weapon
	 * 2. Zombies should differ after losing its limb
	 * 
	 * @param map		the current map being played
	 */
	private String losingLimb(GameMap map) {
		String result = "";
		//Check if the target is undead
		if (target instanceof Zombie) {
			//Chance to drop an arm
			boolean armChance = rand.nextInt(4)+1 == 1;
			//Chance to drop a leg
			boolean legChance = rand.nextInt(4)+1 == 1;
			//Chance to drop a weapon
			boolean dropWeapon = false;
			//If the chance succeeds and the zombie still has an arm
			if (armChance && !target.hasCapability(ArmCapability.NONE)) {
				//Check if zombie wields a weapon
				boolean checkWeapon = !(target.getWeapon() instanceof IntrinsicWeapon);
				//Drops the appropriate limb (arm)
				map.locationOf(target).addItem(new ZombieArm());
				//If both arms are still intact
				if (target.hasCapability(ArmCapability.BOTH)) {
					//Modify the zombie's state to having 1 less arm
					target.removeCapability(ArmCapability.BOTH);
					target.addCapability(ArmCapability.HALF);
					//Perform chance calculation for dropping a weapon
					if (checkWeapon && rand.nextInt(2)==1) {
						Actions dropActions = new Actions();
						//Find the weapon the zombie wields
						for (Item item : target.getInventory())
							if (item instanceof WeaponItem)
								dropActions.add(item.getDropAction());
						//Drops the weapon
						for (Action drop : dropActions)	
							drop.execute(target, map);
						dropWeapon = true;
					}
				//If only one arm is left
				} else if (target.hasCapability(ArmCapability.HALF)) {
					//Modify the zombie's state to having no arms
					target.removeCapability(ArmCapability.HALF);
					target.addCapability(ArmCapability.NONE);
					//Guarantees that if a weapon is wielded, the zombie drops it
					if (checkWeapon) {
						Actions dropActions = new Actions();
						//Finds the weapon the zombie wields
						for (Item item : target.getInventory())
							if (item instanceof WeaponItem)
								dropActions.add(item.getDropAction());
						//Drops the weapon
						for (Action drop : dropActions)	
							drop.execute(target, map);
						dropWeapon = true;
					}
				}
				//The text result shown on screen changes based on the aftermath
				if (dropWeapon) {
					result += ", a weapon dropped, ";
				}
				result += target + " loses an arm";
			}
			//If the chance succeeds and the zombie still has a leg
			if (legChance && !target.hasCapability(LegCapability.NONE)) {
				//Drops the appropriate limb leg)
				map.locationOf(target).addItem(new ZombieLeg());
				//If both legs are still intact
				if (target.hasCapability(LegCapability.BOTH)) {
					//Modify the zombie's state to having 1 less leg
					target.removeCapability(LegCapability.BOTH);
					target.addCapability(LegCapability.HALFA);
				}
				//If 1 leg is intact on the HALFA state
				else if (target.hasCapability(LegCapability.HALFA)) {
					//Modify the zombie's state to have no legs
					target.removeCapability(LegCapability.HALFA);
					target.addCapability(LegCapability.NONE);
				}
				//If 1 leg is intact on the HALFB state
				else if (target.hasCapability(LegCapability.HALFB)) {
					//Modify the zombie's state to have no legs
					target.removeCapability(LegCapability.HALFB);
					target.addCapability(LegCapability.NONE);
				}
				//The text result shown on screen changes based on the aftermath
				if (!armChance) {
					result += " and " + target + " loses a leg";
				} else {
					result += "and a leg";
				}
				if (target.hasCapability(LegCapability.NONE)) {
					result += ", target loses its moving capabilities";
				}
			}
		}
		return result + ".";	
	}
	
	@Override
	/**
	 * The option text to be displayed on the menu
	 * 
	 * @param actor		the actor performing the attack
	 * @return The text to be shown on the menu
	 */
	public String menuDescription(Actor actor) {
		return "First option: " +  actor + " snipes " + target;
	}
}