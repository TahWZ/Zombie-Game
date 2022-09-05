package game;

public class ZombieArm extends MaterialItem{

	/**
	 * A zombie arm which drops from a zombie after it loses its arm
	 * 
	 */
	public ZombieArm() {
		super("Zombie arm",'A',10,"Swoops",new ZombieClub());
	}
}
