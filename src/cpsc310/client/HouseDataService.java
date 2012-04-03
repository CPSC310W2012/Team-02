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
	 * Get house data for drawing of table from search result.
	 * 
	 * @param list
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range
	 */
	public List<HouseData> getHouses(List<String> list, int start, int range);

	/**
	 * Get house data within specified search criteria.
	 * 
	 * @param userSearchInput
	 *            - list of user's search input into search boxes
	 * @param isSelling
	 *            - integer values, 0 not for sale, 1 for sale, -1 all
	 */
	public List<String> searchHouses(String[] userSearchInput, int isSelling);

	/**
	 * Sort by Address in server side current working id set
	 */
	public List<String> sortByAddress(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Postal Code in server side current working id set
	 */
	public List<String> sortByPostalCode(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Realtor in server side current working id set
	 */
	public List<String> sortByOwner(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by For Sale Status in server side current working id set
	 */
	public List<String> sortByForSale(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by land value in server side current working id set
	 */
	public List<String> sortByCurrentLandValue(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Improvement Value in server side current working id set
	 */
	public List<String> sortByCurrentImprovementValue(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Assessment Year in server side current working id set
	 */
	public List<String> sortByAssessmentYear(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Previous Land Value in server side current working id set
	 */
	public List<String> sortByPreviousLandValue(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Previous Improvement Value in server side current working id set
	 */
	public List<String> sortByPreviousImprovementValue(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Year Built in server side current working id set
	 */
	public List<String> sortByYearBuilt(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Big Improvement Year in server side current working id set
	 */
	public List<String> sortByBigImprovementYear(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Sort by Price in server side current working id set
	 */
	public List<String> sortByPrice(boolean isSortAscending, List<String> houseIDs);

	/**
	 * Add/update user specified information about the specified data in the
	 * database.
	 * 
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
	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude, String postalCode);

	/**
	 * Returns a list of streetNames to address searches
	 * 
	 * @return list of StreetNames
	 */
	public List<String> getStreetNames();

	/**
	 * Sets ID store to all the keys of the DB
	 */
	public List<String> refreshIDStore();

	/**
	 * resets the house (owner status, for sale) and removes from datastore
	 * 
	 * @param houseID
	 */
	public void resetHouse(String houseID);

	/**
	 * returns a single house given an ID. This is simply to make some features
	 * cleaner. a combination of getHouses and Search can do the same thing.
	 * 
	 * @param civicNumber
	 * @param street
	 * @return HouseData - a single House object
	 */
	public HouseData retrieveSingleHouse(int civicNumber, String street);

	/**
	 * Searches just by user. Makes user retrieval easier. Retrieve houses by
	 * getHouses.
	 * 
	 * @param email
	 */
	public List<String> getHomesByUser(String email);

	/**
	 * Searchs within the list of for sale houses within a specified polygon
	 * represented by list of latitude and longitude values
	 * 
	 * @param userSearchInput
	 * @param latitude
	 * @param longitude
	 */
	public List<String> searchHousesForSalePolygon(String[] userSearchInput,
			double[] latitude, double[] longitude);
	
	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	public int getHouseDatabaseLength();
}
