package game;

import java.util.Random;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

/**
 * An item which stores the state of Mambo Marie when she vanishes from the map. Furthermore it will respawn her on the map with
 * a given chance each turn
 * 
 */
public class MarieEssence extends Item{
	
	private int healthAmount;
	private Random rand = new Random();
	private boolean stuck = false;
	
	/**
	 * Constructor
	 * 
	 * @param healthAmount Mambo Marie's current health
	 */
	public MarieEssence(int healthAmount) {
		super("Marie Essence", '.', false);
		this.healthAmount = healthAmount;
		
	}
	
	/**
	 * Mambo Marie's current health
	 * 
	 * @return the health amount of Mambo Marie before vanishing
	 */
	public int healthAmount() {
		return this.healthAmount;
	}
	
	@Override
	/**
	 * If this item is present on the map, it has a 5% chance of spawning Mambo Marie of it's stored state each turn 
	 * 
	 * @param currentLocation the current location of MarrieEssence
	 */
	public void tick(Location currentLocation) {
		if (rand.nextInt(20) == 1 || stuck) {
			if (!currentLocation.containsAnActor()) {
				currentLocation.addActor(new MamboMarie(healthAmount));
				currentLocation.removeItem(this);
				stuck = false;
			} else {
				stuck = true;
			}
		}
	}
}