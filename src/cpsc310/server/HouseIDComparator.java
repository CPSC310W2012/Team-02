package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Address Comparator
 * @author Justin
 *
 */
public class HouseIDComparator implements Comparator {
	
	private HashMap<String, HouseDataPoint> tempStore;
	
	/**
	 * Constructor for the Comparator
	 * @param houseHash
	 */
	public HouseIDComparator(HashMap<String, HouseDataPoint> houseHash)
	{
		tempStore = houseHash;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		String houseID0 = tempStore.get(arg0).getHouseID();
		String houseID1 = tempStore.get(arg1).getHouseID();

		return houseID0.compareTo(houseID1);

	}
}
