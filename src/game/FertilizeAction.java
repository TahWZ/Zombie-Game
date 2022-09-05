package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;

public class FertilizeAction extends Action {
	private Crop crop;

	public FertilizeAction(Crop crop) {
		this.crop = crop;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		//Sets the age of the to be plus 10
		try { 
			crop.setAge(crop.getAge() + 10);
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}
		//If the age of the crop is >= 20, change to Ripe
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " fertilize a crop";
	}
}
