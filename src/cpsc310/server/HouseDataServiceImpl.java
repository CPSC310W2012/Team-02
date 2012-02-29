package cpsc310.server;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {
	
	/**
	 * Get house data for initial drawing of table.
	 * Returning list must be ArrayList because of Google RPC's Serialization policy.
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
		
		// TODO Get HouseDataPoint objects from database for the specified range.
		for (int i = start; i < end; i++){
		}
		
		// TODO Delete this when database is complete.
		List<HouseData> tmpHouse = new ArrayList<HouseData>();
		HouseData test = new HouseData(025530151, "1295 RICHARDS ST", "V6K 1M3", 123, 93821, "John Doe", true, 399928);
		tmpHouse.add(test);
		tmpHouse.add(new HouseData (2000299, "1828 WEST BLVD", "V9N 1L2", 332, 248882, "", false, 0));
		tmpHouse.add(new HouseData (4099223, "1828 EAST BLVD", "V9N 2L2", 331, 244882, "", false, 0));
		tmpHouse.add(new HouseData (188281, "122 CAMBIE ST", "V7K 1H2", 12, 38823, "", false, 0));
		
		
		// Convert HouseDataPoint into HouseData
		for (int i = start; i < end; i++){
			// call convertToHouseData() and add each object to result
		}
		
		// Change below to return grab
		return tmpHouse;
	}

	/**
	 * Get house data within specified criteria.
	 * If user did not specify coordinate range or land value range,
	 * lowerCoord, upperCoord, lowerVal, upperVal will be -1.
	 * In Sprint 2, this function will be modified to include more criteria, and range cases.
	 * @param lowerCoord
	 * @param upperCoord
	 * @param lowerVal
	 * @param upperVal 
	 * @param owner
	 * @return list of House data within specified coordinates
	 */
	@Override
	public List<HouseData> getSearchedHouses(int lowerCoord, int upperCoord, 
			int lowerVal, int upperVal, String owner) {
		List<HouseData> result = null;
		int range = 0;
		
		// If user did not specify coordinate range or land value range,
		// lowerCoord, upperCoord, lowerVal, upperVal will be -1!!
		
		
		// TODO Search database and grab necessary data.
		
		// If there is search result, count the number of found data. 
		// Set the number of data to range, create an ArrayList of 
		// HouseData, where all the results converted into HouseData
		// is stored for return.
		if (false) {
			result = new ArrayList<HouseData>(range);
		}
		
		// Convert the fetched HouseDataPoint data into HouseData and append it to the result list.
		
		return result;
	}
	
	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * @return size of database
	 */
	@Override
	public int getHouseDatabaseLength() {
		// TODO get database length
		int databaseLength = 4;
		
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
	 * For Sprint 2. Add/update user specified information about the specified data
	 * in the database.
	 * @param Owner
	 * @param price
	 * @param isSelling
	 * @param house
	 */
	@Override
	public void updateHouses(String Owner, double price, boolean isSelling, HouseData house) {
		// TODO Update HousePointData data in database
	}
	
	
	/**
	 * Helper to convert HouseDataPoint into HouseDataPoint (data transfer object).
	 * @param house HouseDataPoint to convert into HouseData
	 * @return HouseData object
	 */
	private HouseData convertToHouseData(HouseDataPoint house) {
		HouseData converted = new HouseData();
		
		converted.setPID(Integer.parseInt(house.getPID()));
		converted.setAddress(house.getAddress());
		converted.setPostalCode(house.getPostalCode());
		converted.setCoordinate(house.getCoordinate());
		converted.setLandValue(house.getLandValue());
		converted.setOwner(house.getOwner());
		converted.setIsSelling(house.getIsSelling());
		converted.setPrice(house.getPrice());
		
		return converted; 
	}
	
}
