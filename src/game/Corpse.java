package game;

import edu.monash.fit2099.engine.Location;
import java.util.Random;

public class Corpse extends PortableItem{
	
	private int counter;
	private Random rand = new Random();
	
	/**
	 * A dead body
	 */
	public Corpse() {
		super("Corpse",'c');
		this.counter = rand.nextInt(11)+5;
	}
	
	@Override
	/**
	 * A zombie should spawn on the corpse's location after 5-15 rounds
	 * 
	 * @param currentLocation	The location of the corpse
	 */
	public void tick(Location currentLocation) {
		//The corpse can only spawn if the current location is empty
		if (counter==0 && !currentLocation.containsAnActor()) {
			//Removes the corpse
			currentLocation.removeItem(this);
			//Adds a zombified human
			currentLocation.addActor(new Zombie("Zombified Human"));
		//Counter which indicate the round which the zombie should spawn
		} else if (counter != 0) {
			counter -= 1;
		}
	}
}
