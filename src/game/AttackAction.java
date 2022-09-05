package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Special Action for attacking other Actors.
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();

	/**
	 * Constructor.
	 * 
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
	}

	@Override
	/**
	 * Performs the required modifications on the target and actor. The effects may vary based on
	 * the weapon the attacker was wielding and whether the attack is fatal. Furthermore,
	 * 1. Attacks can miss
	 * 2. Kills on humans should spawn a corpse
	 * 3. Attacks on zombies should have a chance of dropping limbs along with any weapon it wields
	 * 
	 * @param actor		the actor performing the attack
	 * @param map		the current map being played
	 */
	public String execute(Actor actor, GameMap map) {

		Weapon weapon = actor.getWeapon();

		if (rand.nextBoolean()) {
			return actor + " misses " + target + ".";
		}

		int damage = weapon.damage();
		String result = actor + " " + weapon.verb() + " " + target + " for " + damage + " damage";
		
		//If the attack is a bite, actor plus 5 health
		if(weapon.verb().equals("bites")){
			actor.heal(5);
			result += ". " + actor + " heals 5 health";
		}
		
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
					result += ", a weapon dropped";
				}
				result += " and " + target + " loses an arm";
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
		
		result += ".";

		target.hurt(damage);
		//If a player used aim, an attack should cause the player to lose focus canceling its effects
		if (target instanceof Player && ((Player)target).getWeapon() instanceof Sniper) {
			Sniper sniper = (Sniper)target.getWeapon();
			if (sniper.focus) {
				sniper.loseFocus();
				result += target + " loses focus";
			}
		}
		if (!target.isConscious()) {
			//Item corpse = new PortableItem("dead " + target, '%');
			boolean spawnCorpse = false;
			if (target.hasCapability(ZombieCapability.ALIVE)) {
				map.locationOf(target).addItem(new Corpse());
				spawnCorpse = true;
			}
			
			Actions dropActions = new Actions();
			for (Item item : target.getInventory())
				dropActions.add(item.getDropAction());
			for (Action drop : dropActions)	
				drop.execute(target, map);
			map.removeActor(target);	
			
			//Text to state the corpse being placed
			result += System.lineSeparator() + target + " is killed.";
			if (spawnCorpse) {
				result += " Corpse has been placed";
			}
		}
		return result;
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks " + target;
	}
}
