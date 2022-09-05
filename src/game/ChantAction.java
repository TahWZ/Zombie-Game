package game;

import java.util.ArrayList;
import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

/**
 * An action unique to Mambo Marie which allows her to summon zombies the round after it was performed
 * 
 */
public class ChantAction extends Action {
	private int zombieCount;
	private Random rand = new Random();
	
	/**
	 * Constructor
	 * 
	 * @param zombieCount The number of zombies to spawn
	 */
	public ChantAction(int zombieCount) {
		this.zombieCount = zombieCount;
	}

	@Override
	/**
	 * The actions will be called twice, 
	 * 1. the first action will stop Mambo Marie from doing anything
	 * 2. the second action will cause zombies to spawn randomaly on the map
	 * 
	 * @param actor		the actor performing the chant action
	 * @param map		the current map being played
	 * @return the String result of the action selected
	 */
	public String execute(Actor actor, GameMap map) {
		MamboMarie MM = (MamboMarie)actor;
		//If this is the second turn of the chanting action
		if (MM.isChanting()) {
			ArrayList<Location> invalidLocations = new ArrayList<Location>();
			String result;
			Location currentLocation;
			int totalSpawns = 0;
			int maxRangeX = map.getXRange().max(),maxRangeY = map.getYRange().max();
			int mapSize = (maxRangeX+1)*(maxRangeY+1);
			//spawn zombies randomly on the map as long as there's sufficient space on the map
			while (invalidLocations.size() < mapSize && totalSpawns < zombieCount) {
				currentLocation = map.at(rand.nextInt(maxRangeX), rand.nextInt(maxRangeY));
				if (!invalidLocations.contains(currentLocation)){
					if (!currentLocation.canActorEnter(new Zombie("Marie Zombie"))){
						invalidLocations.add(currentLocation);
					} else {
						currentLocation.addActor(new Zombie("Marie Zombie"));
						totalSpawns += 1;
					}
				}
			}
			result = menuDescription(actor);
			if (totalSpawns != zombieCount) {
				return result + ", " + (zombieCount-totalSpawns) + " were stuck as there's insufficient space to spawn";
			}
			MM.setChanting(false);
			return result;
		} else {
			MM.setChanting(true);
			return actor + " is chanting a summoning spell";
		}
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " summoned " + zombieCount + " zombies";
	}
}