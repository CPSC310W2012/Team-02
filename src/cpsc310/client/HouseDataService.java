package cpsc310.client;

import java.util.List;
import java.util.Set;

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
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range
	 */
	public List<HouseData> getHouses(int start, int range);
	
	/**
	 * Get house data within specified search criteria.
	 * @param userSearchInput - list of user's search input into search boxes
	 * @param isSelling - boolean value of "for sale" criteria
	 * @return list of House data within specified criteria
	 */
	public List<HouseData> getSearchedHouses(String[] userSearchInput, int isSelling);
		
	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * @return size of database
	 */
	public int getHouseDatabaseLength();
	
	/**
	 * For Sprint 2. server-side sorting
	 */
	public void sortHouses();
	
	/**
	 * Add/update user specified information about the specified data
	 * in the database.
	 * @param Owner - name of realtor
	 * @param price - price of house
	 * @param isSelling - for-sale indicator
	 * @param houses - set of houses to update
	 * @param switchValue - 0 for updating owner; 1 for updating price; 2 for updating isSelling
	 */
	public void updateHouses(String Owner, double price, boolean isSelling, Set<HouseData> houses,
			int switchValue);
}
