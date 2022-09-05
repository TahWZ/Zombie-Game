package game;

import edu.monash.fit2099.engine.Actor;

/**
 * The sniper is the strongest weapon in the game, capable of instakilling enemies
 * 
 */
public class Sniper extends RangedWeapon{
	
	Actor lockedTarget;
	boolean focus;
	int focusTurns;
	
	/**
	 * Constructor
	 * 
	 */
	public Sniper() {
		super("Sniper",'G',50,"BOOM!!!",RangeCapability.SNIPE,10,5);
		this.focus = false;
	}
	
	/**
	 * Aim is a unique features which alters the outcome of snipe actions, calling it stores the focus turns which is 
	 * the number of turns the player aimed a given target
	 * 
	 * @param target		the actor which the aim is targeted at
	 */
	public void aim(Actor target) {
		if (!this.focus) {
			this.focusTurns = 1;
			this.focus = true;
			this.lockedTarget = target;
		} else {
			this.focusTurns += 1;
		}
	}
	
	/**
	 * Returns a boolean which determines whether the player is one a focus state
	 * @return boolean value
	 */
	public boolean isFocus() {
		return this.focus;
	}
	
	/**
	 * Retrieve the number of turns the target has been aimed for
	 * 
	 * @return number of turns the target has been aimed for
	 */
	public int getTurns() {
		return this.focusTurns;
	}
	
	/**
	 * The target which the sniper is targetting due to an aim action performed
	 * 
	 * @return the target
	 */
	public Actor getTarget() {
		return this.lockedTarget;
	}
	
	/**
	 * Causes the focus state to reset
	 * 
	 */
	public void loseFocus() {
		this.focus = false;
		this.focusTurns = 0;
	}
}
