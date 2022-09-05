package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * An abstract object for all weapons which are ranged. Any actions involving the targets will not be limited to a one
 * block range but instead a provided range from the constructor. Furthermore each type of range weapon has its own
 * special traits but share some similar methods
 *
 */
public abstract class RangedWeapon extends WeaponItem implements Range{
	
	protected int ammoAmount = 0;
	protected int rangeLen;
	/** Constructor.
	 *
	 * @param name name of the item
	 * @param displayChar character to use for display when item is on the ground
	 * @param damage amount of damage this weapon does
	 * @param verb verb to use for this weapon, e.g. "hits", "zaps"
	 * @param range The type of weapon
	 * @param rangeLen The range of the weapon
	 * @param ammoAmount The amount of ammo the weapon should have upon creation
	 */
	public RangedWeapon(String name, char displayChar, int damage, String verb,RangeCapability range, int rangeLen, int ammoAmount) {
		super(name, displayChar, damage , verb);
		addCapability(range);
		this.rangeLen = rangeLen;
		this.ammoAmount = ammoAmount;
	}
	
	@Override
	public int getRange() {
		return this.rangeLen;
	}
	@Override
	public RangeCapability RangeType() {
		if (hasCapability(RangeCapability.SPREAD))
			return RangeCapability.SPREAD;
		else
			return RangeCapability.SNIPE;
	}

	/**
	 * Adds a given number of ammo to the weapon
	 * 
	 * @param amount The amount of ammo to add
	 */
	public void addAmmo(int amount) {
		this.ammoAmount += amount;
	}
	
	/**
	 * Removes a given number of ammo from the weapon
	 * 
	 * @param amount The amount of ammo to remove
	 */
	public void removeAmmo(int amount) {
		this.ammoAmount -= amount;
	}
	
	/**
	 * Retrieves the ammo amount of the weapon
	 * 
	 * @return the number of ammo in the weapon
	 */
	public int getAmmo() {
		return this.ammoAmount;
	}
	
	/**
	 * This is used to check if the target has any ground or objects in its range which prevents range actions from occuring.
	 * 
	 * @param map The map being played
	 * @param blockedLocation The locations which are in blocked range
	 * @param x The x position where the range action user is at
	 * @param y The y position where the range action user is at
	 * @param directionX The direction of x which the actions head towards
	 * @param directionY The direction of y which the actions head towards
	 * @param currentRange The distance between the player and the current position of the action taking place
	 * @return the String result of the action selected by the player
	 */
	private boolean checkBlock(GameMap map,ArrayList<Location> blockedLocations,int x,int y, int directionX, int directionY,int currentRange) {
		int maxX = map.getXRange().max(), minX = map.getXRange().min(), maxY = map.getYRange().max(), minY = map.getYRange().min();
		boolean checkX = x <= maxX && x >= minX,checkY = y <= maxY && y >= minY;
		//If the location is a blocked
		if (!checkX || !checkY || blockedLocations.contains(map.at(x,y))) {
			return false;
		//If the location contains a blocking ground/object
		} else if (map.at(x,y).getGround().blocksThrownObjects()) {
			//Stores the range which the ground blocks range actions
			while (currentRange<=this.rangeLen && checkX && checkY) {
				blockedLocations.add(map.at(x, y));
				x += directionX;
				y += directionY;
				checkX = x <= maxX && x >= minX;
				checkY = y <= maxY && y >= minY;
				currentRange++;
			}
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if all given actors is acceptable to be a target, and if so added to the targets arraylist
	 * 
	 * @param targets The targets Arraylist
	 * @param location The locations of the targets
	 * @param attackableTeam The types of actors acceptable to be a target
	 */
	private void addTarget(ArrayList<Actor> targets,Location location,ZombieCapability attackableTeam) {
		if (location.containsAnActor() && location.getActor().hasCapability(attackableTeam)) {
			targets.add(location.getActor());
		} 
	}
	
	/**
	 * Searches all targets in a given direction
	 * 
	 * @param map				the current map being played
	 * @param start				the current location of the actor with the ranged weapon
	 * @param direction			the direction to check for targets
	 * @param attackableTeam	the type of acceptable targets
	 * @return The actors that are valid targets
	 */
	public ArrayList<Actor> searchTargets(GameMap map,Location start,String direction, ZombieCapability attackableTeam) {
		ArrayList<Actor> targets = new ArrayList<Actor>();
		ArrayList<Location> blockedLocations = new ArrayList<Location>();
		int currentX,currentY;
		int x = start.x();
		int y = start.y();
		//If ammo amount is valid
		if (this.ammoAmount >= 1) {
			//Checks the direction provided for enemies through the addtarget method and whether its blocked through the checkblock method
			if (direction == "North") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=0; j <= i*2;j++) {
						currentX = x+j-i;
						currentY = y-i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,0,-1,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "North-East") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=1; j <= this.rangeLen - i + 1;j++) {
						currentX = x+j;
						currentY = y-i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,1,0,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "East") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=0; j <= i*2;j++) {
						currentX = x+i;
						currentY = y+j-i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,1,0,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "South-East") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=1; j <= this.rangeLen - i + 1;j++) {
						currentX = x+j;
						currentY = y+i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,1,1,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "South") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=0; j <= i*2;j++) {
						currentX = x+j-i;
						currentY = y+i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,0,1,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "South-West") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=1; j <= this.rangeLen - i + 1;j++) {
						currentX = x-j;
						currentY = y+i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,-1,1,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "West") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=0; j <= i*2;j++) {
						currentX = x-i;
						currentY = y+j-i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,-1,0,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			} else if (direction == "North-West") {
				for (int i=1;i <= this.rangeLen;i++) {
					for (int j=1; j <= this.rangeLen - i + 1;j++) {
						currentX = x-j;
						currentY = y-i;
						if (!checkBlock(map,blockedLocations,currentX,currentY,-1,-1,i))
							continue;
						addTarget(targets,map.at(currentX,currentY),attackableTeam);
					}
				}
			}
			return targets;
		}
		return null;
	}
}
