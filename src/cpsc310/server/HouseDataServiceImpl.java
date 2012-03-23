package cpsc310.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.appengine.repackaged.com.google.common.collect.Sets;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {

	DataStore store;
	List<String> workingIDStore;

	/**
	 * Constructor
	 */
	public HouseDataServiceImpl() {
		store = new DataStore();
		refreshIDStore();
	}

	/**
	 * 
	 */
	public void refreshIDStore() {
		workingIDStore = store.getAllKeys();
	}

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
		List<HouseDataPoint> tempList = store.getHouses(workingIDStore, start,
				range);

		// Convert HouseDataPoint into HouseData
		List<HouseData> grab = convertToListHouseData(tempList);

		// Change below to return grab
		return grab;
	}

	@Override
	public void searchHouses(String[] userSearchInput, int isSelling) {
		// [0]"civicNumber"
		// [1]"StreetName",
		// Value "Current Land Value" - [2]min, [3]max
		// Value "Price" - [4]min, [5]max
		// [6]"Realtor"
		// [7]"Postal Code"
		// Value "Current Improvement Value" - [8]min, [9]max
		// Year "Assessment Year" - [10]min, [11]max
		// Value "Previous Land Value" - [12]min, [13]max
		// Value "Previous Improvement Value" - [14]min, [15]max
		// Year "Year Built" - [16]min, [17]max
		// Year "Big Improvement Year" - [18]min, [19]max

		// reduction factors (avg case) = houseID > Street > Civic Number >
		// Postal Code > Realtor > Price > Is Selling > Values > Years > Not
		// Selling
//
//		Set<String> results = null;
//
//		// First pass checks in order of reduction factors
//		if (!userSearchInput[0].equals("") && !userSearchInput[1].equals("")) {
//			if (results == null) {
//				results = store.searchByAddress(
//						Integer.parseInt(userSearchInput[0]),
//						userSearchInput[1]);
//			} else {
//				results = Sets.intersection(results, store.searchByAddress(
//						Integer.parseInt(userSearchInput[0]),
//						userSearchInput[1]));
//			}
//		}
//		if (!userSearchInput[1].equals("")) {
//			if (results == null) {
//				results = store.searchByStreet(userSearchInput[1]);
//			} else {
//				results = Sets.intersection(results,store.searchByStreet(userSearchInput[1]));
//			}
//		}
//		if (!userSearchInput[0].equals("")) {
//			results = store.searchByCivicNumber(Integer
//					.parseInt(userSearchInput[0]));
//			workingIDStore = results;
//			firstPassPruned = true;
//		}
//		if (!userSearchInput[7].equals("")) {
//			results = store.searchByPostalCode(userSearchInput[7]);
//			workingIDStore = results;
//			firstPassPruned = true;
//		}
//		if (!userSearchInput[6].equals("")) {
//			results = store.searchByOwner(userSearchInput[6]);
//		}
//
//		// Second Pass
//		if (!userSearchInput[4].equals("") && !userSearchInput[5].equals("")) {
//			results = store.searchByPrice(Integer.parseInt(userSearchInput[4]),
//					Integer.parseInt(userSearchInput[5]));
//			workingIDStore = results;
//		} else if (isSelling == 1) {
//			results = store.getForSaleHomes();
//			workingIDStore = results;
//		} else if (!userSearchInput[2].equals("")
//				&& !userSearchInput[3].equals("")) {
//			results = store.searchByCurrentLandValue(
//					Integer.parseInt(userSearchInput[2]),
//					Integer.parseInt(userSearchInput[3]));
//			workingIDStore = results;
//		} else if (!userSearchInput[8].equals("")
//				&& !userSearchInput[9].equals("")) {
//			results = store.searchByCurrentImprovementValue(
//					Integer.parseInt(userSearchInput[8]),
//					Integer.parseInt(userSearchInput[9]));
//			workingIDStore = results;
//		} else if (!userSearchInput[12].equals("")
//				&& !userSearchInput[13].equals("")) {
//			results = store.searchByPreviousLandValue(
//					Integer.parseInt(userSearchInput[12]),
//					Integer.parseInt(userSearchInput[13]));
//			workingIDStore = results;
//		} else if (!userSearchInput[14].equals("")
//				&& !userSearchInput[15].equals("")) {
//			results = store.searchByPreviousImprovementValue(
//					Integer.parseInt(userSearchInput[14]),
//					Integer.parseInt(userSearchInput[15]));
//			workingIDStore = results;
//		}
//		workingIDStore = results;
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
	public void sortByAddress(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByHouseID(workingIDStore);
		} else {
			workingIDStore = store.sortByHouseIDDes(workingIDStore);
		}
	}

	@Override
	public void sortByPostalCode(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByPostalCode(workingIDStore);
		} else {
			workingIDStore = store.sortByPostalCodeDes(workingIDStore);
		}
	}

	@Override
	public void sortByOwner(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByOwner(workingIDStore);
		} else {
			workingIDStore = store.sortByOwnerDes(workingIDStore);
		}
	}

	@Override
	public void sortByForSale(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByForSale(workingIDStore);
		} else {
			workingIDStore = store.sortByForSaleDes(workingIDStore);
		}
	}

	@Override
	public void sortByCurrentLandValue(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByCurrentLandValue(workingIDStore);
		} else {
			workingIDStore = store.sortByCurrentLandValueDes(workingIDStore);
		}
	}

	@Override
	public void sortByCurrentImprovementValue(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store
					.sortByCurrentImprovementValue(workingIDStore);
		} else {
			workingIDStore = store
					.sortByCurrentImprovementValueDes(workingIDStore);
		}
	}

	@Override
	public void sortByAssessmentYear(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByAssessmentYear(workingIDStore);
		} else {
			workingIDStore = store.sortByAssessmentYearDes(workingIDStore);
		}
	}

	@Override
	public void sortByPreviousLandValue(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByPreviousLandValue(workingIDStore);
		} else {
			workingIDStore = store.sortByPreviousLandValueDes(workingIDStore);
		}
	}

	@Override
	public void sortByPreviousImprovementValue(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store
					.sortByPreviousImprovementValue(workingIDStore);
		} else {
			workingIDStore = store
					.sortByPreviousImprovementValueDes(workingIDStore);
		}
	}

	@Override
	public void sortByYearBuilt(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByYearBuilt(workingIDStore);
		} else {
			workingIDStore = store.sortByYearBuiltDes(workingIDStore);
		}
	}

	@Override
	public void sortByBigImprovementYear(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByBigImprovementYear(workingIDStore);
		} else {
			workingIDStore = store.sortByBigImprovementYearDes(workingIDStore);
		}
	}

	@Override
	public void sortByPrice(boolean isSortAscending) {
		if (isSortAscending) {
			workingIDStore = store.sortByPrice(workingIDStore);
		} else {
			workingIDStore = store.sortByPriceDes(workingIDStore);
		}
	}

	@Override
	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude, String postalCode) {
		store.updateHouse(Owner, price, isSelling, houseID, longitude,
				latitude, postalCode);
	}

	@Override
	public List<String> getStreetNames() {
		return store.getStreets();
	}
}
