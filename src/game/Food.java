package game;

public class Food extends ConsumableItem{

	public Food(String name, char displayChar, int foodValue) {
		super(name,displayChar,foodValue);
	}
	
	public int getFoodValue() {
		return super.getConsumableValue();
	}
}
