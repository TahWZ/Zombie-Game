package game;

/**
 * The shotgun is a powerful weapon which shoots at any direction and damages all units in contact of its shot
 * 
 */
public class Shotgun extends RangedWeapon{
	
	/**
	 * Constructor
	 */
	public Shotgun() {
		super("Shotgun",'G',30,"Pow!!!",RangeCapability.SPREAD,3,10);
	}
}
