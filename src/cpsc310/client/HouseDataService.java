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
	 * This method will be refactored into HouseDataBase.
	 * At the moment, this builds HouseDataPoint Store 
	 * by the request module load.
	 */
	public void buildHouseDataPointStore();
	
	/**
	 * Get house data for initial drawing of table.
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range
	 */
	public List<HouseData> getHouses(int start, int range);
	
	/**
	 * Get house data within specified coordinate range.
	 * @param lowerCoord
	 * @param upperCoord
	 * @param lowerVal
	 * @param upperVal
	 * @param owner
	 * @return list of House data within specified coordinates
	 */
	public List<HouseData> getSearchedHouses(int lowerCoord, int upperCoord, 
			int lowerVal, int upperVal, String owner);
		
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
	 * For Sprint 2. Add/update user specified information about the specified data
	 * in the database.
	 * @param Owner
	 * @param price
	 * @param isSelling
	 * @param house
	 */
	public void updateHouses(String Owner, double price, boolean isSelling, HouseData house);
}
