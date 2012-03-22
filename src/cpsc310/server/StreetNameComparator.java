package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Street Name Comparator
 * @author Justin
 *
 */
public class StreetNameComparator implements Comparator {
	
	private HashMap<String, HouseDataPoint> tempStore;
	
	/**
	 * Constructor for the Comparator
	 * @param houseHash
	 */
	public StreetNameComparator(HashMap<String, HouseDataPoint> houseHash)
	{
		tempStore = houseHash;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		String street0 = tempStore.get(arg0).getStreetName();
		String street1 = tempStore.get(arg1).getStreetName();

		return street0.compareTo(street1);

	}
}
