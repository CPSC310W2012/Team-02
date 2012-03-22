package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Postal Code Comparator
 * @author Justin
 *
 */
public class PostalCodeComparator implements Comparator {
	
	private HashMap<String, HouseDataPoint> tempStore;
	
	/**
	 * Constructor for the Comparator
	 * @param houseHash
	 */
	public PostalCodeComparator(HashMap<String, HouseDataPoint> houseHash)
	{
		tempStore = houseHash;
	}
	
	@Override
	public int compare(Object arg0, Object arg1) {
		String postalCode0 = tempStore.get(arg0).getPostalCode();
		String postalCode1 = tempStore.get(arg1).getPostalCode();

		return postalCode0.compareTo(postalCode1);

	}
}
