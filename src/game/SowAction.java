package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;

public class SowAction extends Action {
	private Location location;
	
	/**
	 * Constructor for SowAction
	 * 
	 * @param location - the location for which sowing action will take place
	 */
	public SowAction(Location location) {
		this.location = location;
	}

	@Override
	/**
	 * Performs a Sowing action which sets the ground as a crop
	 * 
	 * @param actor - the actor which is attempting to sow a crop
	 * @param map - the current map being displayed
	 */
	public String execute(Actor actor, GameMap map) {
		//Creates a new Crop and sets the ground as the crop
		location.setGround(new Crop());
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " sows a crop";
	}
}
