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
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range
	 */
	public List<HouseData> getHouses(int start, int range);

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
	public void searchHouses(String[] userSearchInput,
			int isSelling);

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	public int getHouseDatabaseLength();

	/**
	 * Sort by Address in server side current working id set
	 */
	public void sortByAddress(boolean isSortAscending);

	/**
	 * Sort by Postal Code in server side current working id set
	 */
	public void sortByPostalCode(boolean isSortAscending);

	/**
	 * Sort by Realtor in server side current working id set
	 */
	public void sortByOwner(boolean isSortAscending);

	/**
	 * Sort by For Sale Status in server side current working id set
	 */
	public void sortByForSale(boolean isSortAscending);

	/**
	 * Sort by land value in server side current working id set
	 */
	public void sortByCurrentLandValue(boolean isSortAscending);

	/**
	 * Sort by Improvement Value in server side current working id set
	 */
	public void sortByCurrentImprovementValue(boolean isSortAscending);

	/**
	 * Sort by Assessment Year in server side current working id set
	 */
	public void sortByAssessmentYear(boolean isSortAscending);

	/**
	 * Sort by Previous Land Value in server side current working id set
	 */
	public void sortByPreviousLandValue(boolean isSortAscending);

	/**
	 * Sort by Previous Improvement Value in server side current working id set
	 */
	public void sortByPreviousImprovementValue(boolean isSortAscending);

	/**
	 * Sort by Year Built in server side current working id set
	 */
	public void sortByYearBuilt(boolean isSortAscending);

	/**
	 * Sort by Big Improvement Year in server side current working id set
	 */
	public void sortByBigImprovementYear(boolean isSortAscending);

	/**
	 * Sort by Price in server side current working id set
	 */
	public void sortByPrice(boolean isSortAscending);

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
	public void refreshIDStore();
	
	/**
	 * resets the house (owner status, for sale) and removes from datastore
	 * 
	 * @param houseID
	 */
	public void resetHouse(String houseID);
}
