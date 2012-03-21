package cpsc310.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * Provides datastore access and in memory data query access. Emulates databases
 * actions but only store and retrieves houses that are changed or at inital
 * startup
 * 
 * @author Justin
 * 
 */
public class DataStore {
	// Currently we store whole file on module load.
	private HashMap<String, HouseDataPoint> store;

	// To facilitate address look up
	private HashMap<String, ArrayList<Integer>> streetNames;

	// To facilitate postalCode look up
	private HashMap<String, ArrayList<String>> postalCodes;

	// To facilitate owner lookups
	private HashMap<String, ArrayList<String>> owners;

	// To facilitate forSale lookups
	private HashSet<String> forSaleHomes;

	// To facilitate land value search
	private TreeMap<Integer, ArrayList<String>> currentLandValues;

	// To facilitate improvement values search
	private TreeMap<Integer, ArrayList<String>> improvementValues;

	// To facilitate assessment year search
	private TreeMap<Integer, ArrayList<String>> assessmentYears;

	// To facilitate previous land value search
	private TreeMap<Integer, ArrayList<String>> previousLandValues;

	// To facilitate previous improvement year search
	private TreeMap<Integer, ArrayList<String>> previousImprovementValues;

	// To facilitate year build search
	private TreeMap<Integer, ArrayList<String>> yearsBuilt;

	// To facilitate big improvement year search
	private TreeMap<Integer, ArrayList<String>> bigImprovementYears;

	// To facilitate price search
	private TreeMap<Integer, ArrayList<String>> price;

	public DataStore() {
		initilizeDataStorage();
		store = populateMemoryStore();
		initalizeLookups(store);
	}

	/**
	 * Retrives data from data store and datasources parsing into a hashmap of
	 * houseDataPoints returns HashMap of houseDataPoints
	 */
	private HashMap<String, HouseDataPoint> populateMemoryStore() {
		DataCatalogueObserverImpl observerService = new DataCatalogueObserverImpl();
		List<String> rawData = observerService
				.downloadFile("http://www.ugrad.cs.ubc.ca/~d2t6/property_tax_report_csv.zip");
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

	/**
	 * initalizes lookup for search functions
	 * 
	 * @param houses
	 */
	private void initalizeLookups(HashMap<String, HouseDataPoint> houses) {
		// initialize data-lookup structures
		streetNames = new HashMap<String, ArrayList<Integer>>();
		postalCodes = new HashMap<String, ArrayList<String>>();
		owners = new HashMap<String, ArrayList<String>>();
		forSaleHomes = new HashSet<String>();
		currentLandValues = new TreeMap<Integer, ArrayList<String>>();
		improvementValues = new TreeMap<Integer, ArrayList<String>>();
		assessmentYears = new TreeMap<Integer, ArrayList<String>>();
		previousLandValues = new TreeMap<Integer, ArrayList<String>>();
		previousImprovementValues = new TreeMap<Integer, ArrayList<String>>();
		yearsBuilt = new TreeMap<Integer, ArrayList<String>>();
		bigImprovementYears = new TreeMap<Integer, ArrayList<String>>();
		price = new TreeMap<Integer, ArrayList<String>>();

		Iterator<String> tempItr = houses.keySet().iterator();
		while (tempItr.hasNext()) {
			HouseDataPoint currentHouse = houses.get(tempItr.next());

			// Populate Street Name Hash
			if (streetNames.containsKey(currentHouse.getStreetName())) {
				streetNames.get(currentHouse.getStreetName()).add(
						currentHouse.getCivicNumber());
			} else {
				ArrayList<Integer> tempCivicList = new ArrayList<Integer>();
				tempCivicList.add(currentHouse.getCivicNumber());
				streetNames.put(currentHouse.getStreetName(), tempCivicList);
			}

			// Populate current land value tree
			if (currentLandValues.containsKey(currentHouse
					.getCurrentLandValue())) {
				currentLandValues.get(currentHouse.getCurrentLandValue()).add(
						currentHouse.getHouseID());
			} else {
				ArrayList<String> tempHouseIDs = new ArrayList<String>();
				tempHouseIDs.add(currentHouse.getHouseID());
				currentLandValues.put(currentHouse.getCurrentLandValue(),
						tempHouseIDs);
			}

			// Populate improvementValues
			if (currentHouse.getCurrentImprovementValue() != -1) {
				if (improvementValues.containsKey(currentHouse
						.getCurrentImprovementValue())) {
					improvementValues.get(
							currentHouse.getCurrentImprovementValue()).add(
							currentHouse.getHouseID());
				} else {
					ArrayList<String> tempHouseIDs = new ArrayList<String>();
					tempHouseIDs.add(currentHouse.getHouseID());
					improvementValues.put(
							currentHouse.getCurrentImprovementValue(),
							tempHouseIDs);
				}
			}

			// Populate assessmentYears
			if (currentHouse.getAssessmentYear() != -1) {
				if (assessmentYears.containsKey(currentHouse
						.getAssessmentYear())) {
					assessmentYears.get(currentHouse.getAssessmentYear()).add(
							currentHouse.getHouseID());
				} else {
					ArrayList<String> tempHouseIDs = new ArrayList<String>();
					tempHouseIDs.add(currentHouse.getHouseID());
					assessmentYears.put(currentHouse.getAssessmentYear(),
							tempHouseIDs);
				}
			}

			// Populate previousLandValues
			if (currentHouse.getPreviousLandValue() != -1) {
				if (previousLandValues.containsKey(currentHouse
						.getPreviousLandValue())) {
					previousLandValues.get(currentHouse.getPreviousLandValue())
							.add(currentHouse.getHouseID());
				} else {
					ArrayList<String> tempHouseIDs = new ArrayList<String>();
					tempHouseIDs.add(currentHouse.getHouseID());
					previousLandValues.put(currentHouse.getPreviousLandValue(),
							tempHouseIDs);
				}
			}

			// Populate previousImprovementValues
			if (currentHouse.getPreviousImprovementValue() != -1) {
				if (previousImprovementValues.containsKey(currentHouse
						.getPreviousImprovementValue())) {
					previousImprovementValues.get(
							currentHouse.getPreviousImprovementValue()).add(
							currentHouse.getHouseID());
				} else {
					ArrayList<String> tempHouseIDs = new ArrayList<String>();
					tempHouseIDs.add(currentHouse.getHouseID());
					previousImprovementValues.put(
							currentHouse.getPreviousImprovementValue(),
							tempHouseIDs);
				}
			}

			// Populate yearsBuilt
			if (currentHouse.getYearBuilt() != -1) {
				if (yearsBuilt.containsKey(currentHouse.getYearBuilt())) {
					yearsBuilt.get(currentHouse.getYearBuilt()).add(
							currentHouse.getHouseID());
				} else {
					ArrayList<String> tempHouseIDs = new ArrayList<String>();
					tempHouseIDs.add(currentHouse.getHouseID());
					yearsBuilt.put(currentHouse.getYearBuilt(), tempHouseIDs);
				}
			}

			// Populate bigImprovementYears
			if (currentHouse.getBigImprovementYear() != -1) {
				if (bigImprovementYears.containsKey(currentHouse
						.getBigImprovementYear())) {
					bigImprovementYears.get(
							currentHouse.getBigImprovementYear()).add(
							currentHouse.getHouseID());
				} else {
					ArrayList<String> tempHouseIDs = new ArrayList<String>();
					tempHouseIDs.add(currentHouse.getHouseID());
					bigImprovementYears.put(
							currentHouse.getBigImprovementYear(), tempHouseIDs);
				}
			}

			updateIndexes(currentHouse);
		}
	}

	/**
	 * given a house updates the house indexes that can change due to house
	 * updates
	 * 
	 * @param currentHouse
	 */
	private void updateIndexes(HouseDataPoint currentHouse) {

		// Populate postal code Hash
		if (!currentHouse.getPostalCode().equals("")) {
			if (postalCodes.containsKey(currentHouse.getPostalCode())) {
				postalCodes.get(currentHouse.getPostalCode()).add(
						currentHouse.getHouseID());
			} else {
				ArrayList<String> tempHouseIDs = new ArrayList<String>();
				tempHouseIDs.add(currentHouse.getHouseID());
				postalCodes.put(currentHouse.getPostalCode(), tempHouseIDs);
			}
		}

		// Populate Owners Hash
		if (!currentHouse.getOwner().equals("")) {
			if (owners.containsKey(currentHouse.getOwner())) {
				owners.get(currentHouse.getOwner()).add(
						currentHouse.getHouseID());
			} else {
				ArrayList<String> tempHouseIDs = new ArrayList<String>();
				tempHouseIDs.add(currentHouse.getHouseID());
				owners.put(currentHouse.getOwner(), tempHouseIDs);
			}
		}

		// Populate edited Entries Hash
		if (currentHouse.getIsSelling()) {
			forSaleHomes.add(currentHouse.getHouseID());
		} else {
			forSaleHomes.remove(currentHouse.getHouseID());
		}

		// Populate price
		if (currentHouse.getPrice() != -1) {
			if (price.containsKey(currentHouse.getPrice())) {
				price.get(currentHouse.getPrice()).add(
						currentHouse.getHouseID());
			} else {
				ArrayList<String> tempHouseIDs = new ArrayList<String>();
				tempHouseIDs.add(currentHouse.getHouseID());
				price.put(currentHouse.getPrice(), tempHouseIDs);
			}
		}
	}

	/**
	 * Remove house from indexes, needs to be invoked before an existing house
	 * is updated
	 * 
	 * @param house
	 */
	private void removeFromIndexes(HouseDataPoint currentHouse) {
		// remove from postal code Hash
		if (!currentHouse.getPostalCode().equals("")) {
			if (postalCodes.containsKey(currentHouse.getPostalCode())) {
				postalCodes.get(currentHouse.getPostalCode()).remove(
						currentHouse.getHouseID());
				// if position has any empty array remove the whole thing
				if (postalCodes.get(currentHouse.getPostalCode()).size() <= 0) {
					postalCodes.remove(currentHouse.getPostalCode());
				}
			}
		}

		// remove from owners hash
		if (!currentHouse.getOwner().equals("")) {
			if (owners.containsKey(currentHouse.getOwner())) {
				owners.get(currentHouse.getOwner()).remove(
						currentHouse.getHouseID());
				// if position has any empty array remove the whole thing
				if (postalCodes.get(currentHouse.getOwner()).size() <= 0) {
					postalCodes.remove(currentHouse.getOwner());
				}
			}
		}

		// remove price tree
		if (currentHouse.getPrice() != -1) {
			if (price.containsKey(currentHouse.getPrice())) {
				price.get(currentHouse.getPrice()).remove(
						currentHouse.getHouseID());
				// if position has any empty array remove the whole thing
				if (postalCodes.get(currentHouse.getPrice()).size() <= 0) {
					postalCodes.remove(currentHouse.getPrice());
				}
			}
		}
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

	// HouseID retrieval Methods

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
	 * Gets the streets names of entire datastore
	 * 
	 * @return keys (list of street names)
	 */
	public ArrayList<String> getStreets() {
		ArrayList<String> keys = new ArrayList<String>();
		keys.addAll(streetNames.keySet());
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
	 * Searches for a user inputed address.
	 * 
	 * @param civicNumber
	 * @param street
	 * @return housesFound
	 */
	public ArrayList<String> searchByAddress(int civicNumber, String street) {
		ArrayList<String> keys = new ArrayList<String>();
		if (store.containsKey(civicNumber + " " + street)) {
			keys.add(civicNumber + " " + street);
		}
		return keys;
	}

	/**
	 * Searches for user inputed address returns all address within that street.
	 * 
	 * @param street
	 * @return housesFound
	 */
	public ArrayList<String> searchByStreet(String street) {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<Integer> currentCivicNumbers = streetNames.get(street);
		Iterator<Integer> tempItr = currentCivicNumbers.iterator();
		while (tempItr.hasNext()) {
			keys.add(tempItr.next() + " " + street);
		}
		return keys;
	}

	/**
	 * Searches for houses via postal code
	 * 
	 * @param postalCode
	 * @return houseFound
	 */
	public ArrayList<String> searchByPostalCode(String postalCode) {
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> currentPostalCodes = postalCodes.get(postalCode);
		Iterator<String> tempItr = currentPostalCodes.iterator();
		while (tempItr.hasNext()) {
			keys.add(tempItr.next());
		}
		return keys;
	}

	/**
	 * return all houses that are for sale
	 * 
	 * @return house - houses that are for sale
	 */
	public ArrayList<String> getForSaleHomes() {
		ArrayList<String> tempList = new ArrayList<String>();
		tempList.addAll(forSaleHomes);
		return tempList;
	}

	/**
	 * Searches for houses current land value
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByCurrentLandValue(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = currentLandValues
				.subMap(min, true, max, true);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of improvementValues
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByImprovementValue(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = improvementValues
				.subMap(min, true, max, true);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of assessmentYear
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByAssessmentYear(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = assessmentYears.subMap(
				min, max);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of previousLandValue
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByPreviousLandValue(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = previousLandValues
				.subMap(min, true, max, true);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of previousImprovementValue
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByPreviousImprovementValue(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = previousImprovementValues
				.subMap(min, true, max, true);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of year Built
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByYearBuilt(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = yearsBuilt.subMap(min,
				max);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of bigImprovementYear
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByBigImprovementYear(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = bigImprovementYears
				.subMap(min, true, max, true);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Searches for houses in range of price
	 * 
	 * @precondition min <= max
	 * @param min
	 *            - upper bound
	 * @param max
	 *            -lower bound
	 * @return houseFound
	 */
	public ArrayList<String> searchByPrice(int min, int max) {
		SortedMap<Integer, ArrayList<String>> tempMap = price.subMap(min, true,
				max, true);
		return convertRangedSearchResult(tempMap);
	}

	/**
	 * Helper method to convert SortedMaps to arraylists created when search in
	 * a range of numbers
	 * 
	 * @param result
	 * @return convertedList - ArrayList of houseIDs
	 */
	private ArrayList<String> convertRangedSearchResult(
			SortedMap<Integer, ArrayList<String>> result) {
		ArrayList<String> keys = new ArrayList<String>();
		Iterator<Integer> tempItr = result.keySet().iterator();
		while (tempItr.hasNext()) {
			// retrieve from list of houses
			ArrayList<String> currentList = result.get(tempItr.next());
			Iterator<String> tempKeyItr = currentList.iterator();
			while (tempKeyItr.hasNext()) {
				keys.add(tempKeyItr.next());
			}
		}
		return keys;
	}

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
	 * @param postalCode
	 *            - postal Code for house
	 * @return boolean - if successful true, if not false
	 * 
	 */
	public void updateHouse(String Owner, int price, boolean isSelling,
			String house, double longitude, double latitude, String postalCode) {
		// create and set object variables
		HouseDataPoint currentHouse = store.get(house);

		// remove from indexes
		removeFromIndexes(currentHouse);

		// Update house values
		currentHouse.setOwner(Owner);
		currentHouse.setPrice(price);
		currentHouse.setIsSelling(isSelling);
		currentHouse.setLatLng(latitude, longitude);
		currentHouse.setPostalCode(postalCode);

		// re-add to indexes
		updateIndexes(currentHouse);

		// Store object
		Objectify ofy = ObjectifyService.begin();
		ofy.put(currentHouse);
	}
}
