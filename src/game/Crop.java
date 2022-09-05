package game;

import edu.monash.fit2099.engine.Ground;
import edu.monash.fit2099.engine.Location;

public class Crop extends Ground {

	private int age;
	
	/**
	 * Constructor for Crop
	 */
	public Crop() {
		super('C');
	}
	
	/**
	 * Returns the age of the crop
	 * 
	 * @return age - the age of the crop
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * Sets the age of the crop and calls changeToRipe method()
	 * 
	 * @param age - the age of the crop
	 */
	public void setAge(int age) {
		if (age < 0) {
			throw new IllegalArgumentException("The crop should never have a negative age");
		}
		this.age = age;
		changeToRipe();
	}
	
	/**
	 * Checks if the crop is ripe
	 * 
	 * @return True if age more than or equals to 20, False if otherwise
	 */
	public boolean isRipe() {
		if (age >= 20) {
			return true;
		}
		return false;
	}
	
	/**
	 * Changes the displayChar of the crop to "R" to signify the crop is ripe
	 */
	public void changeToRipe() {
		if (isRipe()) {
			displayChar = 'R';
		}
	}

	@Override
	/**
	 * Increments the crop's age by 1 everytime the Player's playturn method is called
	 * Will change to ripe when it reaches 20
	 * 
	 * @param location - the location of the crop
	 */
	public void tick(Location location) {
		age++;
		changeToRipe();
	}
}
