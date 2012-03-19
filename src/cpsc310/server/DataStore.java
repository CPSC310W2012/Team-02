package cpsc310.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class DataStore {
	// Currently we store whole file on module load.
	private HashMap<String, HouseDataPoint> store;

	public DataStore() {
		initilizeDataStorage();
		store = populateMemoryStore();
	}

	/**
	 * Retrives data from data store and datasources parsing into a hashmap of
	 * houseDataPoints returns HashMap of houseDataPoints
	 */
	private HashMap<String, HouseDataPoint> populateMemoryStore() {
		DataCatalogueObserverImpl observerService = new DataCatalogueObserverImpl();
		List<String> rawData = observerService
				.downloadFile("ftp://webftp.vancouver.ca/OpenData/csv/property_tax_report_csv.zip");
		// Parse raw data
		FileParser parser = new FileParser();
		HashMap<String, HouseDataPoint> tempStore = parser.parseData(rawData);

		// register objectify objects if not already registered
		initilizeDataStorage();

		try {
			// Populate memory store with changed entries
			Objectify ofy = ObjectifyService.begin();
			Iterable<Key<HouseDataPoint>> allKeys = ofy.query(
					HouseDataPoint.class).fetchKeys();
			Iterator<Key<HouseDataPoint>> houseIDs = allKeys.iterator();
			while (houseIDs.hasNext()) {
				HouseDataPoint tempHouse = ofy.get(houseIDs.next());
				tempStore.put(tempHouse.getHouseID(), tempHouse);
			}
		} catch (NullPointerException e) {
			// need to handle the fact there is no objectify data store in junit
			// tests
			System.err.println("This should only print when using junit tests");
		}

		return tempStore;
	}

	/*
	 * Initializes DataStore Objects (houseDataPoints) if not already
	 */
	private void initilizeDataStorage() {
		// try to register
		try {
			ObjectifyService.register(HouseDataPoint.class);
		} catch (Exception e) {
			// already registered
		}
	}

	// House retrieval Methods

	/**
	 * Gets keys of all of entire datastore
	 * 
	 * @return keys
	 */
	public ArrayList<String> getAllKeys() {
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(store.keySet());
		return keys;
	}

	/**
	 * Get house data for table display
	 * 
	 * @param keys
	 * @param start
	 * @param range
	 * @return list of HouseDataPoint
	 */
	public List<HouseDataPoint> getHouses(ArrayList<String> keys, int start,
			int range) {
		List<HouseDataPoint> grab = new ArrayList<HouseDataPoint>();
		int end = start + range;
		// Check for end condition to prevent accessing out-of-array.
		if (end > keys.size()) {
			end = keys.size();
		}
		for (int i = start; i < end; i++) {
			grab.add(store.get(keys.get(i)));
		}
		// Change below to return grab
		return grab;
	}

	// Search methods (subset of retrival Methods, retrives only arrays of keys)
	/**
	 * Searches for a user inputed address. TODO add support for common versions
	 * of writting address and partial matches
	 * 
	 * @param address
	 * @return housesFound
	 */
	public ArrayList<String> searchByAddress(String address) {
		ArrayList<String> keys = new ArrayList();
		String convertedAddress = convertAddress(address);
		// check if address exists
		if (store.containsKey(address)) {
			keys.add(convertedAddress);
		}
		return keys;
	}

	// helper methods to search
	/**
	 * Converts common address values to HouseIDs
	 */
	private String convertAddress(String rawAddress) {
		// TODO need to parse common ways of writing address
		String convertedString = rawAddress;

		return convertedString;
	}

	// Sort Methods

	// updataMethods

	/**
	 * Add/update user specified information about the specified data in the
	 * database.
	 * 
	 * @pre Owner, price, isSelling, house, longitude, latitude != null
	 * @post the current memory set is refreshed to reflect changes and when
	 *       datastore is updated a true value is returned
	 * 
	 * @param Owner
	 *            - name of realtor
	 * @param price
	 *            - price of house
	 * @param isSelling
	 *            - for-sale indicator
	 * @param houses
	 *            - set of houses to update
	 * @param latitude
	 *            - latitude geolocation for house
	 * @param longitude
	 *            - longitude geolocation for house
	 * @return boolean - if successful true, if not false
	 * 
	 */
	public void updateHouse(String Owner, int price, boolean isSelling,
			String house, double longitude, double latitude) {
		// create and set object variables
		HouseDataPoint currentHouse = store.get(house);
		currentHouse.setOwner(Owner);
		currentHouse.setPrice(price);
		currentHouse.setIsSelling(isSelling);
		currentHouse.setLatLng(latitude, longitude);

		// Store object
		Objectify ofy = ObjectifyService.begin();
		ofy.put(currentHouse);
	}
}
