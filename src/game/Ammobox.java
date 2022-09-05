package game;

import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;

/**
 * An item which is not portable but will add ammo to a player's gun if the player's at its location
 * 
 */
public class Ammobox extends Item{
	
	protected int amount;
	private RangeCapability type;
	
	/**
	 * Constructor
	 * 
	 * @param amount The amount of ammo this ammobox provides
	 * @param type The weapon type which uses the ammo
	 */
	public Ammobox(int amount,RangeCapability type) {
		super(type.toString().toLowerCase() + " ammo ", 'a', false);
		this.type = type;
		this.amount = amount;
	}
	
	/**
	 * Returns the amount of ammo the ammobox provides
	 *
	 * @return the ammo amount
	 */
	public int ammoAmount() {
		return this.amount;
	}
	
	@Override
	/**
	 * Adds ammo automatically to a player's gun if a player is on the same location as the ammobox
	 * 
	 * @param currentLocation the location of the ammobox
	 */
	public void tick(Location currentLocation) {
		if (currentLocation.containsAnActor() && currentLocation.getActor().getWeapon() instanceof Range) {
			if (((RangedWeapon)currentLocation.getActor().getWeapon()).hasCapability(type)) {
				((RangedWeapon)currentLocation.getActor().getWeapon()).addAmmo(this.amount);
				currentLocation.removeItem(this);
			}
		}
	}
}
