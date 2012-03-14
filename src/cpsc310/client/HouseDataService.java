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
	 * Get house data within specified coordinate range.
	 * @param lowerCoord - lower range for coordinate of house
	 * @param upperCoord - upper range for coordinate of house
	 * @param lowerVal - lower asking price of house
	 * @param upperVal - upper asking price of house
	 * @param owner - realtor of the house
	 * @return list of House data within specified coordinates
	 */
	public List<HouseData> getSearchedHouses(int lowerCoord, int upperCoord, 
			double lowerLandVal, double upperLandVal, String owner);
		
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
