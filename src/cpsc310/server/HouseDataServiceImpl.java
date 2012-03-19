package cpsc310.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterable;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {

	// Currently we store whole file on module load.
	private HashMap<String, HouseDataPoint> store = populateMemoryStore();

	/**
	 * Retrives data from data store and datasources parsing into a hashmap of houseDataPoints
	 * returns HashMap of houseDataPoints
	 */
	private HashMap<String, HouseDataPoint> populateMemoryStore() {
		// register objectify objects if not already registered
		initilizeDataStorage();
		
		DataCatalogueObserverImpl observerService = new DataCatalogueObserverImpl();
		List<String> rawData = observerService
				.downloadFile("http://www.ugrad.cs.ubc.ca/~d2t6/property_tax_report_csv.zip");
		// Parse raw data
		FileParser parser = new FileParser();
		HashMap<String, HouseDataPoint> tempStore = parser.parseData(rawData);
		
		//Populate memory store with changed entries
		Objectify ofy = ObjectifyService.begin();
		Iterable<Key<HouseDataPoint>> allKeys = ofy.query(HouseDataPoint.class).fetchKeys();
		Iterator<Key<HouseDataPoint>> houseIDs = allKeys.iterator();
		while(houseIDs.hasNext())
		{
			HouseDataPoint tempHouse = ofy.get(houseIDs.next());
			tempStore.put(tempHouse.getHouseID(), tempHouse);
		}
		
		return tempStore;
	}

	/**
	 * Get house data for initial drawing of table. Returning list must be
	 * ArrayList because of Google RPC's Serialization policy.
	 * 
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range
	 */
	@Override
	public List<HouseData> getHouses(int start, int range) {
		List<HouseData> grab = null;
		int newRange = range;
		int end = start + range;

		// Check for end condition to prevent accessing out-of-array.
		if (end > getHouseDatabaseLength()) {
			end = getHouseDatabaseLength();
			newRange = end - start;
		}

		// Set the size of returning array.
		grab = new ArrayList<HouseData>(newRange);

		// Get homes in a generic non-repeated fashion
		Iterator<String> houserItr = store.keySet().iterator();

		// Convert HouseDataPoint into HouseData
		for (int i = start; (i < end) && (houserItr.hasNext()); i++) {
			grab.add(convertToHouseData(store.get(houserItr.next())));
		}

		// Change below to return grab
		return grab;
	}

	/**
	 * Get house data within specified search criteria.
	 * 
	 * @param userSearchInput
	 *            - list of user's search input into search boxes
	 * @param isSelling
	 *            - boolean value of "for sale" criteria
	 * @return list of House data within specified criteria
	 */
	@Override
	public List<HouseData> getSearchedHouses(String[] userSearchInput,
			int isSelling) {
		// @TODO rework method to work with new houseDataPoints
		// List<HouseData> result = new ArrayList <HouseData> (store.size());
		// boolean searchCoord = false;
		// boolean searchLandVal = false;
		// boolean searchOwner = false;
		//
		// // If user did not specify coordinate range or land value range,
		// // lowerCoord, upperCoord, lowerVal, upperVal will be -1!!
		// if (lowerCoord != -1 || upperCoord != -1) {
		// searchCoord = true;
		// }
		// if (lowerLandVal != -1 || upperLandVal != -1) {
		// searchLandVal = true;
		// }
		// if (owner != null) {
		// searchOwner = true;
		// }
		//
		// // TODO Search database and grab necessary data.
		// Iterator<HouseDataPoint> houserItr = store.iterator();
		// HouseDataPoint check = null;
		//
		// // Convert HouseDataPoint into HouseData
		// for (int i = 0; (i < store.size()) && (houserItr.hasNext()); i++) {
		// check = houserItr.next();
		//
		// if (searchLandVal == true) {
		// if ((check.getLandValue() > lowerLandVal) &&
		// (check.getLandValue() < upperLandVal)) {
		// result.add(convertToHouseData(check));
		// }
		// }
		//
		// if (searchOwner == true) {
		// if (check.getOwner() != null) {
		// if (check.getOwner().equals(owner)) {
		// result.add(convertToHouseData(check));
		// }
		// }
		// }
		// }
		//
		// if (result.isEmpty())
		// return null;
		//
		// return result;
		return null;
	}

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	@Override
	public int getHouseDatabaseLength() {
		// TODO get database length
		int databaseLength = store.size();

		return databaseLength;
	}

	/**
	 * For Sprint 2. server-side sorting
	 */
	@Override
	public void sortHouses() {
		// TODO sort database

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
	 * @return boolean - if successful true, if not false
	 * 
	 */
	@Override
	public void updateHouse(String Owner, int price, boolean isSelling,
			HouseData house, double longitude, double latitude) {
		// create and set object variables
		HouseDataPoint currentHouse = store.get(house.getHouseID());
		currentHouse.setOwner(Owner);
		currentHouse.setPrice(price);
		currentHouse.setIsSelling(isSelling);
		currentHouse.setLatLng(latitude, longitude);

		// Store object
		Objectify ofy = ObjectifyService.begin();
		ofy.put(currentHouse);
	}

	/**
	 * Helper to convert HouseDataPoint into HouseDataPoint (data transfer
	 * object).
	 * 
	 * @param house
	 *            - HouseDataPoint to convert into HouseData
	 * @return the converted HouseDataPoint (returned as a HouseData object)
	 */
	private HouseData convertToHouseData(HouseDataPoint house) {
		HouseData converted = new HouseData();

		converted.setHouseID(house.getHouseID());
		converted.setCivicNumber(house.getCivicNumber());
		converted.setStreetName(house.getStreetName());
		converted.setPostalCode(house.getPostalCode());
		converted.setCurrentLandValue(house.getCurrentLandValue());
		converted
				.setCurrentImprovementValue(house.getCurrentImprovementValue());
		converted.setAssessmentYear(house.getAssessmentYear());
		converted.setPreviousLandValue(house.getPreviousLandValue());
		converted.setPreviousImporvementValue(house
				.getPreviousImprovementValue());
		converted.setYearBuilt(house.getYearBuilt());
		converted.setBigImprovementYear(house.getBigImprovementYear());
		converted.setOwner(house.getOwner());
		converted.setIsSelling(house.getIsSelling());
		converted.setPrice(house.getPrice());

		return converted;
	}

	/*
	 * initilizes DataStore Objects if not already
	 */
	public void initilizeDataStorage() {
		// try to register
		try {
			ObjectifyService.register(HouseDataPoint.class);
		} catch (Exception e) {
			// already registered
		}
	}
}
