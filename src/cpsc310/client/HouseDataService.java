package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
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
	 *            - boolean value of "for sale" criteria
	 * @return list of House data within specified criteria
	 */
	public List<String> getSearchedHouses(String[] userSearchInput,
			int isSelling);

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	public int getHouseDatabaseLength();

	/**
	 * Sort by Address
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByAddress(List<String> list);

	/**
	 * Sort by Postal Code
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByPostalCode(List<String> list);

	/**
	 * Sort by Realtor
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByOwner(List<String> list);

	/**
	 * Sort by For Sale Status
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByForSale(List<String> list);

	/**
	 * Sort by land value
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByCurrentLandValue(List<String> list);

	/**
	 * Sort by Improvement Value
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByCurrentImprovementValue(List<String> list);

	/**
	 * Sort by Assessment Year
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByAssessmentYear(List<String> list);

	/**
	 * Sort by Previous Land Value
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByPreviousLandValue(List<String> list);

	/**
	 * Sort by Previous Improvement Value
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByPreviousImprovementValue(List<String> list);

	/**
	 * Sort by Year Built
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByYearBuilt(List<String> list);

	/**
	 * Sort by Big Improvement Year
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByBigImprovementYear(List<String> list);

	/**
	 * Sort by Price
	 * 
	 * @param list
	 *            - list of unsortedHouseIDs
	 * @return houseIDs - sorted list of house IDs
	 */
	public List<String> sortByPrice(List<String> list);

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
	 * Returns a list of houses sorted by specified column. 
	 * 
	 * @param sortColumnName - name of the column to sort
	 * @param isAscending - boolean indicating sort order 
	 * @param currentStartItem - index of current page's start item
	 * @param range - range of page (= page length)
	 * @return list of house data sorted by sortColumnName
	 */
	public List<HouseData> getSortedHouses(String sortColumnName, boolean isAscending, 
			int currentStartItem, int range);	
}
