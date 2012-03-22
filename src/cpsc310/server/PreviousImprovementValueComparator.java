package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Previous Improvement Value Comparator
 * 
 * @author Justin
 * 
 */
public class PreviousImprovementValueComparator implements Comparator {

	private HashMap<String, HouseDataPoint> tempStore;

	/**
	 * Constructor for the Comparator
	 * 
	 * @param houseHash
	 */
	public PreviousImprovementValueComparator(
			HashMap<String, HouseDataPoint> houseHash) {
		tempStore = houseHash;
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		int value0 = tempStore.get(arg0).getPreviousImprovementValue();
		int value1 = tempStore.get(arg1).getPreviousImprovementValue();

		if (value0 < value1) {
			return -1;
		} else if (value0 == value1) {
			return 0;
		}
		return 1;
	}
}
