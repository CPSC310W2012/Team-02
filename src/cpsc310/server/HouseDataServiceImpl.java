package cpsc310.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.DataCatalogueObserver;
import cpsc310.client.DataCatalogueObserverAsync;
import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {
	
	// Currently we store whole file on module load.
	private DataCatalogueObserverImpl observerService = new DataCatalogueObserverImpl();
	private List<String> rawData = observerService.downloadFile("http://www.ugrad.cs.ubc.ca/~y0c7/property_tax_report3.csv");
	private List<HouseDataPoint> store = new ArrayList<HouseDataPoint> (rawData.size());
	
	/**
	 * This method will be refactored into HouseDataBase.
	 * At the moment, this builds HouseDataPoint Store 
	 * by the request module load.
	 */
	public void buildHouseDataPointStore() {
		// Parse raw data
		FileParser parser = new FileParser();
		Iterator<HouseDataPoint> houserItr = parser.parseData(rawData).iterator();
				
		// Convert HouseDataPoint into HouseData
		for (int i = 0; (i < rawData.size()) && (houserItr.hasNext()); i++) {
			store.add(houserItr.next());
		}
	}

	/**
	 * Get house data for initial drawing of table. Returning list must be
	 * ArrayList because of Google RPC's Serialization policy.
	 * 
	 * @param start
	 * @param range
	 * @return list of HouseData within specified range TODO: Implement JDO
	 *         Storage, Current implementation just retrieves data from server
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

		// TODO Get HouseDataPoint objects from database for the specified
		// range. Currently get it from the store.
		Iterator<HouseDataPoint> houserItr = store.iterator();
				
		// Convert HouseDataPoint into HouseData
		for (int i = start; (i < end) && (houserItr.hasNext()); i++) {
			grab.add(convertToHouseData(houserItr.next()));
		}

		// Change below to return grab
		return grab;
	}
	

	/**
	 * Get house data within specified criteria. If user did not specify
	 * coordinate range or land value range, lowerCoord, upperCoord, lowerVal,
	 * upperVal will be -1. In Sprint 2, this function will be modified to
	 * include more criteria, and range cases.
	 * 
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
		List<HouseDataPoint> searchList = new ArrayList <HouseDataPoint> (rawData.size());
		List<HouseData> result = null;
		int range = 0;

		// If user did not specify coordinate range or land value range,
		// lowerCoord, upperCoord, lowerVal, upperVal will be -1!!

		// TODO Search database and grab necessary data.
		Iterator<HouseDataPoint> houserItr = store.iterator();
		
		// Convert HouseDataPoint into HouseData
		for (int i = 0; (i < rawData.size()) && (houserItr.hasNext()); i++) {
			if ((lowerCoord < houserItr.next().getCoordinate() && 
					houserItr.next().getCoordinate() < upperCoord))
				searchList.add(houserItr.next());
		}

		// If there is search result, count the number of found data.
		// Set the number of data to range, create an ArrayList of
		// HouseData, where all the results converted into HouseData
		// is stored for return.
		if (searchList.size() > 0) {
			result = new ArrayList<HouseData>(range);
		}

		// Convert the fetched HouseDataPoint data into HouseData and append it
		// to the result list.
		Iterator<HouseDataPoint> searchItr = store.iterator();
		for (int i = 0; (i < searchList.size()) && (searchItr.hasNext()); i++) {
			result.add(convertToHouseData(searchItr.next()));
		}

		return result;
	}

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	@Override
	public int getHouseDatabaseLength() {
		// TODO get database length
		int databaseLength = rawData.size();

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
	 * For Sprint 2. Add/update user specified information about the specified
	 * data in the database.
	 * 
	 * @param Owner
	 * @param price
	 * @param isSelling
	 * @param house
	 */
	@Override
	public void updateHouses(String Owner, double price, boolean isSelling,
			HouseData house) {
		// TODO Update HousePointData data in database
	}

	/**
	 * Helper to convert HouseDataPoint into HouseDataPoint (data transfer
	 * object).
	 * 
	 * @param house
	 *            HouseDataPoint to convert into HouseData
	 * @return HouseData object
	 */
	private HouseData convertToHouseData(HouseDataPoint house) {
		HouseData converted = new HouseData();

		converted.setPID(house.getPID());
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
