package game;

/**
 * Interface for Range objects.
 *
 * As well as providing methods needed to determine range of items, this interface is used to
 * determine whether an item are involved in range features.
 */
public interface Range {
	/**
	 * All range items need a range, this method returns this required range amount
	 * 
	 * @return The range of the item
	 */
	public int getRange();
	
	/**
	 * All range items has a variation, this method returns the variation of the range item
	 * 
	 * @return RangeCapability of a given item
	 */
	public RangeCapability RangeType();
}
