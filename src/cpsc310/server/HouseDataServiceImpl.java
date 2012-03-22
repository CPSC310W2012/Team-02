package cpsc310.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {

	DataStore store = new DataStore();

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
		// retrieve house data points
		List<HouseDataPoint> tempList = store.getHouses(store.getAllKeys(),
				start, range);

		// Convert HouseDataPoint into HouseData
		List<HouseData> grab = convertToListHouseData(tempList);

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
	public List<String> getSearchedHouses(String[] userSearchInput,
			int isSelling) {
				return null;
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
	}

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	@Override
	public int getHouseDatabaseLength() {
		int databaseLength = store.getAllKeys().size();
		return databaseLength;
	}

	/**
	 * Helper to convert HouseDataPoint into HouseDataPoint (data transfer
	 * object).
	 * 
	 * @param houseList
	 *            - HouseDataPoint to convert into HouseData
	 * @return the converted HouseDataPoint (returned as a HouseData object)
	 */
	private List<HouseData> convertToListHouseData(
			List<HouseDataPoint> houseList) {
		List<HouseData> converted = new ArrayList<HouseData>();
		Iterator<HouseDataPoint> tempItr = houseList.iterator();

		// Convert HouseDataPoint into HouseData
		while (tempItr.hasNext()) {
			converted.add(convertToHouseData(tempItr.next()));
		}
		return converted;
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

	@Override
	public List<HouseData> getHouses(List<String> list, int start, int range) {
		// retrieve house data points
		List<HouseDataPoint> tempList = store.getHouses(list, start, range);

		// Convert HouseDataPoint into HouseData
		List<HouseData> grab = convertToListHouseData(tempList);

		// Change below to return grab
		return grab;
	}

	@Override
	public List<String> sortByAddress(List<String> list) {
		return store.sortByHouseID(list);
	}

	@Override
	public List<String> sortByPostalCode(List<String> list) {
		return store.sortByPostalCode(list);
	}

	@Override
	public List<String> sortByOwner(List<String> list) {
		return store.sortByOwner(list);
	}

	@Override
	public List<String> sortByForSale(List<String> list) {
		return store.sortByForSale(list);
	}

	@Override
	public List<String> sortByCurrentLandValue(List<String> list) {
		return store.sortByCurrentLandValue(list);
	}

	@Override
	public List<String> sortByCurrentImprovementValue(List<String> list) {
		return store.sortByCurrentImprovementValue(list);
	}

	@Override
	public List<String> sortByAssessmentYear(List<String> list) {
		return store.sortByAssessmentYear(list);
	}

	@Override
	public List<String> sortByPreviousLandValue(List<String> list) {
		return store.sortByPreviousLandValue(list);
	}

	@Override
	public List<String> sortByPreviousImprovementValue(List<String> list) {
		return store.sortByPreviousImprovementValue(list);
	}

	@Override
	public List<String> sortByYearBuilt(List<String> list) {
		return store.sortByYearBuilt(list);
	}

	@Override
	public List<String> sortByBigImprovementYear(List<String> list) {
		return store.sortByBigImprovementYear(list);
	}

	@Override
	public List<String> sortByPrice(List<String> list) {
		return store.sortByPrice(list);
	}

	@Override
	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude,
			String postalCode) {
		store.updateHouse(Owner, price, isSelling, houseID, longitude, latitude,
				postalCode);
	}

	@Override
	public List<String> getStreetNames() {
		return store.getStreets();
	}

	@Override
	public List<HouseData> getSortedHouses(String sortColumnName,
			boolean isAscending, int currentStartItem, int range) {
		//TODO server-side sort		
		return null;
	}
}
