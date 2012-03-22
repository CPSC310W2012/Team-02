package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Owner Comparator
 * 
 * @author Justin
 * 
 */
public class OwnerComparator implements Comparator {

	private HashMap<String, HouseDataPoint> tempStore;

	/**
	 * Constructor for the Comparator
	 * 
	 * @param houseHash
	 */
	public OwnerComparator(HashMap<String, HouseDataPoint> houseHash) {
		tempStore = houseHash;
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		String owner0 = tempStore.get(arg0).getOwner();
		String owner1 = tempStore.get(arg1).getOwner();
		return owner0.compareTo(owner1);
	}
}
