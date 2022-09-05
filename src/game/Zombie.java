package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import edu.monash.fit2099.engine.MoveActorAction;
import edu.monash.fit2099.engine.PickUpItemAction;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * A Zombie.
 * 
 * This Zombie is pretty boring.  It needs to be made more interesting.
 * 
 * @author ram
 *
 */
public class Zombie extends ZombieActor {
	private Behaviour[] behaviours = {
			new AttackBehaviour(ZombieCapability.ALIVE),
			new HuntBehaviour(Human.class, 10),
			new WanderBehaviour()
	};

	public Zombie(String name) {
		super(name, 'Z', 100, ZombieCapability.UNDEAD);
		//Added the arm and leg capability
		addCapability(ArmCapability.BOTH);
		addCapability(LegCapability.BOTH);
	}
	

	@Override
	public IntrinsicWeapon getIntrinsicWeapon() {
		Random random = new Random();
		boolean chance;
		//The chance of biting alters based on the number of arms it has
		if (this.hasCapability(ArmCapability.BOTH)) {
			chance = (1 + random.nextInt(100) <= 50);
		} else if(this.hasCapability(ArmCapability.HALF)) {
			chance = (1 + random.nextInt(100) <= 75);
		//Having no arms guarantees a bite
		} else {
			chance = true;
		}
		//Zombies has a 50% chance of using bite rather than punch
		if(chance)
			return new IntrinsicWeapon(20,"bites");
		return new IntrinsicWeapon(10, "punches");
	}

	/**
	 * If a Zombie can attack, it will.  If not, it will chase any human within 10 spaces.  
	 * If no humans are close enough it will wander randomly.
	 * 
	 * @param actions list of possible Actions
	 * @param lastAction previous Action, if it was a multiturn action
	 * @param map the map where the current Zombie is
	 * @param display the Display where the Zombie's utterances will be displayed
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		Random random = new Random();
		
		//Zombies have a 10% of saying Braaaaains
		if (1 + random.nextInt(100) <= 10) {
			display.println(this + " says 'Braaaaains'");
		}
		
		//Zombies now have the ability to pick up weapons
		Location currentLocation = map.locationOf(this);
		for(Item item : currentLocation.getItems()) {
			if(item instanceof WeaponItem && !(item instanceof Range)) {
				return new PickUpItemAction(item);
			}
		}
		
		for (Behaviour behaviour : behaviours) {
			Action action = behaviour.getAction(this, map);
			if (action instanceof MoveActorAction) {
				//The following code executes only when the zombie has 1 leg which decides whether the zombie can move this turn
				if (this.hasCapability(LegCapability.HALFA)) {
					this.removeCapability(LegCapability.HALFA);
					this.addCapability(LegCapability.HALFB);
					return new DoNothingAction();
				} else if (this.hasCapability(LegCapability.HALFB)) {
					this.removeCapability(LegCapability.HALFB);
					this.addCapability(LegCapability.HALFA);
				//If the zombie has no leg, the zombie can never move
				} else if (this.hasCapability(LegCapability.NONE)){
					return new DoNothingAction();
				}
			}
			if (action != null)
				return action;
		}
		return new DoNothingAction();	
	}
}
