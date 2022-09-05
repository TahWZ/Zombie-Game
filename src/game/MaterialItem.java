package game;

import edu.monash.fit2099.engine.WeaponItem;

public abstract class MaterialItem extends WeaponItem{
	private WeaponItem craftable;
	
	/** Constructor.
	 *
	 * @param name name of the item
	 * @param displayChar character to use for display when item is on the ground
	 * @param damage amount of damage this weapon does
	 * @param verb verb to use for this weapon, e.g. "hits", "zaps"
	 * @param craftable the weapon that can be crafted from this material
	 */
	public MaterialItem(String name, char displayChar, int damage, String verb, WeaponItem craftable) {
		super(name, displayChar, damage , verb);
		this.craftable = craftable;
	}

	/**
	 * Returns the WeaponItem that can be crafted
	 * 
	 * @return	The WeaponItem craftable
	 */
	public WeaponItem Crafting() {
		return this.craftable;
	}
}
