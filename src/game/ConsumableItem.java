package game;

public abstract class ConsumableItem extends PortableItem{
	
	private int consumableValue;

	/**
	 * Constructor for ConsumableItem
	 * 
	 * @param name - the name of the Item
	 * @param displayChar - the display character of the Item
	 * @param consumableValue - the value of the consumable item
	 */
	public ConsumableItem(String name, char displayChar, int consumableValue) {
		super(name, displayChar);
		this.consumableValue = consumableValue;
	}
	
	/**
	 * Returns the value of the consumable item
	 * 
	 * @return consumableValue - the value of the consumable item
	 */
	public int getConsumableValue() {
		return consumableValue;
	}

}
