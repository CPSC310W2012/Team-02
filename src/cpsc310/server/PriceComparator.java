package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Price Comparator
 * 
 * @author Justin
 * 
 */
public class PriceComparator implements Comparator {

	private HashMap<String, HouseDataPoint> tempStore;

	/**
	 * Constructor for the Comparator
	 * 
	 * @param houseHash
	 */
	public PriceComparator(HashMap<String, HouseDataPoint> houseHash) {
		tempStore = houseHash;
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		int value0 = tempStore.get(arg0).getPrice();
		int value1 = tempStore.get(arg1).getPrice();

		if (value0 < value1) {
			return -1;
		} else if (value0 == value1) {
			return 0;
		}
		return 1;
	}
}
