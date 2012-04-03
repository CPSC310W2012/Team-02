package cpsc310.client;

import java.util.List;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side stub for fetching HouseDataPoint
 */
@RemoteServiceRelativePath("houseData")
public interface HouseDataService extends RemoteService {

	/**
	 * Get house data for initial drawing of table.
	 * 
	 * @param instanceID
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range
	 */
	public List<HouseData> getHouses(int instanceID, int start, int range);

	/**
	 * Get house data within specified search criteria.
	 * 
	 * @param instanceID
	 * @param userSearchInput
	 *            - list of user's search input into search boxes
	 * @param isSelling
	 *            - integer values, 0 not for sale, 1 for sale, -1 all
	 */
	public void searchHouses(int instanceID, String[] userSearchInput,
			int isSelling);

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @param instanceID
	 * @return size of database
	 */
	public int getHouseDatabaseLength(int instanceID);

	/**
	 * Sort by Address in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByAddress(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Postal Code in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByPostalCode(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Realtor in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByOwner(int instanceID, boolean isSortAscending);

	/**
	 * Sort by For Sale Status in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByForSale(int instanceID, boolean isSortAscending);

	/**
	 * Sort by land value in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByCurrentLandValue(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Improvement Value in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByCurrentImprovementValue(int instanceID,
			boolean isSortAscending);

	/**
	 * Sort by Assessment Year in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByAssessmentYear(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Previous Land Value in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByPreviousLandValue(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Previous Improvement Value in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByPreviousImprovementValue(int instanceID,
			boolean isSortAscending);

	/**
	 * Sort by Year Built in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByYearBuilt(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Big Improvement Year in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByBigImprovementYear(int instanceID, boolean isSortAscending);

	/**
	 * Sort by Price in server side current working id set
	 * 
	 * @param instanceID
	 * @param isSortAscending
	 */
	public void sortByPrice(int instanceID, boolean isSortAscending);

	/**
	 * Add/update user specified information about the specified data in the
	 * database.
	 * 
	 * @param instanceID
	 * @param Owner
	 *            - name of realtor
	 * @param price
	 *            - price of house
	 * @param isSelling
	 *            - for-sale indicator
	 * @param house
	 *            - single house to update
	 * @param longitude
	 *            - the latitude of house
	 * @param longitude
	 *            - the longitude of house
	 * @param postalCode
	 *            - postal code derived from google maps
	 */
	public void updateHouse(String Owner, int price,
			boolean isSelling, String houseID, double latitude,
			double longitude, String postalCode);

	/**
	 * Returns a list of streetNames to address searches
	 * 
	 * @param instanceID
	 * @return list of StreetNames
	 */
	public List<String> getStreetNames();

	/**
	 * Sets ID store to all the keys of the DB
	 * 
	 * @param instanceID
	 */
	public void refreshIDStore(int instanceID);

	/**
	 * resets the house (owner status, for sale) and removes from datastore
	 * 
	 * @param instanceID
	 * @param houseID
	 */
	public void resetHouse(String houseID);

	/**
	 * returns a single house given an ID. This is simply to make some features
	 * cleaner. a combination of getHouses and Search can do the same thing.
	 * 
	 * @param instanceID
	 * @param civicNumber
	 * @param street
	 * @return HouseData - a single House object
	 */
	public HouseData retrieveSingleHouse(int instanceID, int civicNumber,
			String street);

	/**
	 * Searches just by user. Makes user retrieval easier. Retrieve houses by
	 * getHouses.
	 * 
	 * @param instanceID
	 * @param email
	 */
	public void getHomesByUser(int instanceID, String email);

	/**
	 * Searchs within the list of for sale houses within a specified polygon
	 * represented by list of latitude and longitude values
	 * 
	 * @param instanceID
	 * @param userSearchInput
	 * @param latitude
	 * @param longitude
	 */
	public void searchHousesForSalePolygon(int instanceID,
			String[] userSearchInput, double[] latitude, double[] longitude);

	/**
	 * Creates an index on instance for server index of houseIDs for each client
	 * connected
	 * 
	 * @returns ID -an integer representing for server index of houseIDs for
	 *          each client connected
	 */
	public int getInstanceID();
}
