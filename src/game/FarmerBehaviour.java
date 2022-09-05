package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import java.util.Random;

public class FarmerBehaviour implements Behaviour{

	@Override
	public Action getAction(Actor actor, GameMap map) {
		ArrayList<Action> actions = new ArrayList<Action>();
		Random random = new Random();
		Location currentLocation = map.locationOf(actor);

		// If the Farmer is standing on an unripe crop, the Farmer can fertilize it to decrease the time left to ripen by 10 turns
		// If the Farmer is standing on a ripe crop, the Farmer can harvest it for food and the food will be drop to the ground
		if (currentLocation.getGround() instanceof Crop) {
			Crop crop = (Crop) currentLocation.getGround();
			// If the crop is unripe, fertilize it
			if (!crop.isRipe()) {
				actions.add(new FertilizeAction(crop));
			//If the crop is ripe, harvest it
			} else{
				actions.add(new HarvestAction(currentLocation));
			}
		}

		for (Exit exit : map.locationOf(actor).getExits()) {
			Location destination = exit.getDestination();

			// If the Farmer is standing next to a patch of dirt, the Farmer can sow the patch of dirt and it will become a crop
			if (destination.getDisplayChar() == '.') {
				if (1 + random.nextInt(100) <= 33) {
					actions.add(new SowAction(destination));
				}
			}

			// If the farmer stands next to a ripe crop, the Farmer can harvest it for food and the food will be drop to the ground
			if (destination.getDisplayChar() == 'R') {
				actions.add(new HarvestAction(destination));
			}
		}
		//Returns any one of the actions above
		if (!actions.isEmpty()) {
			return actions.get(random.nextInt(actions.size()));
		}
		else {
			return null;
		}
	}

}
