package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * For Sale Comparator
 * @author Justin
 *
 */
public class ForSaleComparator implements Comparator {
	
	private HashMap<String, HouseDataPoint> tempStore;
	
	/**
	 * Constructor for the Comparator, will order for sale objects first
	 * @param houseHash
	 */
	public ForSaleComparator(HashMap<String, HouseDataPoint> houseHash)
	{
		tempStore = houseHash;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		boolean value0 = tempStore.get(arg0).getIsSelling();
		boolean value1 = tempStore.get(arg1).getIsSelling();

		if (value0 == true && value1 == false) {
			return -1;
		}
		else if (value0 == value1) {
			return 0;
		}
		return 1;
	}
}
