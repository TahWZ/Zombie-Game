package game;

import java.util.ArrayList;
import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Special action which performs modifications on all targets in its range
 * 
 */
public class SpreadshotAction extends Action{
	
	protected ArrayList<Actor>targets;
	protected Random rand = new Random();
	protected String direction;
	
	public SpreadshotAction(String direction,ArrayList<Actor> targets) {
		this.targets = targets;
		this.direction = direction;
	}
	
	@Override
	/**
	 * Performs the required modifications on all targets in its shooting range, the shots differ for each
	 * individual target and each can:
	 * 1. Miss
	 * 2. Damage
	 * 3. Have a chance of dropping limbs of zombies along with any weapon it wields
	 * 
	 * @param actor		the actor performing the attack
	 * @param map		the current map being played
	 */
	public String execute(Actor actor, GameMap map) {
		RangedWeapon weapon = (RangedWeapon)actor.getWeapon();
		weapon.removeAmmo(1);
		if (targets.isEmpty()) {
			return actor + " shot " + direction + ", hitting nothing and wasted an ammo";
		}
		boolean armChance = false,legChance = false;
		int damage = weapon.damage();
		//The string values store the appropriate result text for each case
		String result = "", finalResult,shot = "",missed = "";
		//For every target in range
		for (int i = 0; i < targets.size();i ++) {
			Actor target = targets.get(i);
			if (rand.nextInt(4)+1 == 4) {
				if (missed!=""){
					missed += ",";
				}
				missed += (targets.get(i).toString());
				continue;
			}
			
			result += System.lineSeparator();
			//Check if the target is a zombie
			if (target instanceof Zombie) {
				//Chance to drop an arm
				armChance = rand.nextInt(4)+1 == 1;
				//Chance to drop a leg
				legChance = rand.nextInt(4)+1 == 1;
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
					result += target + " loses an arm ";
					if (dropWeapon) {
						result += ", drops a weapon ";
					}
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
						result += target + " loses a leg ";
					} else {
						result += ",loses a leg ";
					}
					if (target.hasCapability(LegCapability.NONE)) {
						result += "and also its moving capabilities ";
					}
				}
				if (shot!=""){
					shot += ",";
				}
				shot += (targets.get(i).toString());
			}
	
			target.hurt(damage);
			if (!target.isConscious()) {
				//Item corpse = new PortableItem("dead " + target, '%');
				
				Actions dropActions = new Actions();
				for (Item item : target.getInventory())
					dropActions.add(item.getDropAction());
				for (Action drop : dropActions)	
					drop.execute(target, map);
				map.removeActor(target);	
				
				//Text to state the corpse being placed
				if (armChance || legChance) {
					result += System.lineSeparator();
				}
				result += target + " is killed.";
			} else {
				if (!armChance && !legChance) {
					result += target + " remains alive.";
				} else {
					result += "but remains alive.";
				}
			}
		}
		if (missed=="") {
			finalResult = actor + " shot " + shot + " for " + damage + " damage" + result;
		} else if (shot =="") {
			finalResult = actor + " missed " + missed;
		} else {
			finalResult = actor + " missed " + missed + " and shot " + shot + " for " + damage + " damage" + result;
		}
		return finalResult;
	}
	
	@Override
	public String menuDescription(Actor actor) {
		String result = actor + " shoot towards " + direction;
		if (targets.isEmpty()) {
			result += ", no targets in range";
		} else {
			result += ", targets include ";
			for (int i=0;i<targets.size();i++) {
				if (targets.size() == 1 || i == targets.size()-2) {
					result += targets.get(i) + " ";
				} else if (targets.size() > 1 && i == targets.size()-1) {
					result += "and " + targets.get(i) +" ";
				} else {
					result += targets.get(i) + ", ";
				}
			}
			result += "(bullet count in inventory: " +  ((RangedWeapon)actor.getWeapon()).getAmmo() + ")";
		}
		return result;
	}
	
}
