package game;

public class ZombieLeg extends MaterialItem{

	/**
	 * A zombie leg which drops from a zombie after losing a leg
	 * 
	 */
	public ZombieLeg() {
		super("Zombie leg",'L',11,"Smack",new ZombieMace());
	}
}
