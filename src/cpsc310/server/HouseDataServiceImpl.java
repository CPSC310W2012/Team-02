package cpsc310.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {

	private DataStore store;

	/**
	 * Constructor
	 */
	public HouseDataServiceImpl() {
		store = new DataStore();
		refreshIDStore();
	}

	/**
	 * Refreshes the working set of IDs
	 */
	public List<String> refreshIDStore() {
		return store.getAllKeys();
	}

	@Override
	public List<String> searchHouses(String[] userSearchInput, int isSelling) {
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

		// TODO: Remove debugging purposes
		System.out.println(0 + " " + userSearchInput[0]);
		System.out.println(1 + " " + userSearchInput[1]);
		System.out.println(2 + " " + userSearchInput[2] + " " + 3 + " "
				+ userSearchInput[3]);
		System.out.println(4 + " " + userSearchInput[4] + " " + 5 + " "
				+ userSearchInput[5]);
		System.out.println(6 + " " + userSearchInput[6]);
		System.out.println(7 + " " + userSearchInput[7]);
		System.out.println(8 + " " + userSearchInput[8] + " " + 9 + " "
				+ userSearchInput[9]);
		System.out.println(10 + " " + userSearchInput[10] + " " + 11 + " "
				+ userSearchInput[11]);
		System.out.println(12 + " " + userSearchInput[12] + " " + 13 + " "
				+ userSearchInput[13]);
		System.out.println(14 + " " + userSearchInput[14] + " " + 15 + " "
				+ userSearchInput[15]);
		System.out.println(16 + " " + userSearchInput[16] + " " + 17 + " "
				+ userSearchInput[17]);
		System.out.println(18 + " " + userSearchInput[18] + " " + 19 + " "
				+ userSearchInput[19]);

		Set<String> results = store.getAllKeysSet();

		// First pass checks in order of reduction factors

		results = firstPassSearch(userSearchInput, results);

		if (isSelling == 1) {
			results.retainAll(store.getForSaleHomes());
		} else if (isSelling == 0) {
			results.removeAll(store.getForSaleHomes());
		}
		// TODO: Start performing single value evaluation increase speed?

		// Second Pass, requires tree traversal
		results = secondPassSearch(userSearchInput, results);

		// convert results
		ArrayList<String> convertedResults = new ArrayList<String>();
		convertedResults.addAll(results);
		return convertedResults;
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
	public List<String> sortByAddress(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByHouseID(houseIDs);
		} else {
			return store.sortByHouseIDDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByPostalCode(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByPostalCode(houseIDs);
		} else {
			return store.sortByPostalCodeDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByOwner(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByOwner(houseIDs);
		} else {
			return store.sortByOwnerDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByForSale(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByForSale(houseIDs);
		} else {
			return store.sortByForSaleDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByCurrentLandValue(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByCurrentLandValue(houseIDs);
		} else {
			return store.sortByCurrentLandValueDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByCurrentImprovementValue(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByCurrentImprovementValue(houseIDs);
		} else {
			return store.sortByCurrentImprovementValueDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByAssessmentYear(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByAssessmentYear(houseIDs);
		} else {
			return store.sortByAssessmentYearDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByPreviousLandValue(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByPreviousLandValue(houseIDs);
		} else {
			return store.sortByPreviousLandValueDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByPreviousImprovementValue(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByPreviousImprovementValue(houseIDs);
		} else {
			return store.sortByPreviousImprovementValueDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByYearBuilt(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByYearBuilt(houseIDs);
		} else {
			return store.sortByYearBuiltDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByBigImprovementYear(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByBigImprovementYear(houseIDs);
		} else {
			return store.sortByBigImprovementYearDes(houseIDs);
		}
	}

	@Override
	public List<String> sortByPrice(boolean isSortAscending,
			List<String> houseIDs) {
		if (isSortAscending) {
			return store.sortByPrice(houseIDs);
		} else {
			return store.sortByPriceDes(houseIDs);
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

	@Override
	public void resetHouse(String houseID) {
		store.resetHouse(houseID);
	}

	@Override
	public HouseData retrieveSingleHouse(int civicNumber, String street) {
		ArrayList<String> workingIDStore = new ArrayList<String>();
		workingIDStore.addAll(store.searchByAddress(civicNumber, street));
		HouseDataPoint currentHDP = store.getHouses(workingIDStore, 0, 1)
				.get(0);
		return convertToHouseData(currentHDP);
	}

	@Override
	public List<String> searchHousesForSalePolygon(String[] userSearchInput,
			double[] latitude, double[] longitude) {
		// TODO: Degbuging - Remove
		System.out.println("Polygon Search");

		Set<String> results = store.searchForSaleInPolygon(latitude, longitude);

		results = firstPassSearch(userSearchInput, results);
		results = secondPassSearch(userSearchInput, results);

		// convert to array
		ArrayList<String> convertedResults = new ArrayList<String>();
		convertedResults.addAll(results);
		return convertedResults;
	}

	/**
	 * Intermediate search using all O(1) search functions
	 * 
	 * @param results
	 * @return results - pruned partial results
	 */
	private Set<String> firstPassSearch(String[] userSearchInput,
			Set<String> results) {
		if (!userSearchInput[0].equals("") && !userSearchInput[1].equals("")) {
			results.retainAll(store.searchByAddress(
					Integer.parseInt(userSearchInput[0]), userSearchInput[1]));
		} else if (!userSearchInput[1].equals("")) {
			results.retainAll(store.searchByStreet(userSearchInput[1]));
		} else if (!userSearchInput[0].equals("")) {
			results.retainAll(store.searchByCivicNumber(Integer
					.parseInt(userSearchInput[0])));
		}
		if (!userSearchInput[7].equals("")) {
			results.retainAll(store.searchByPostalCode(userSearchInput[7]));
		}
		if (!userSearchInput[6].equals("")) {
			results.retainAll(store.searchByOwner(userSearchInput[6]));
		}
		return results;
	}

	/**
	 * Intermediate search using all O(log(n)) search functions
	 * 
	 * @param results
	 * @return results - pruned partial results
	 */
	private Set<String> secondPassSearch(String[] userSearchInput,
			Set<String> results) {
		if (!userSearchInput[4].equals("") && !userSearchInput[5].equals("")) {
			results.retainAll(store.searchByPrice(
					Integer.parseInt(userSearchInput[4]),
					Integer.parseInt(userSearchInput[5])));
		}
		if (!userSearchInput[2].equals("") && !userSearchInput[3].equals("")) {
			results.retainAll(store.searchByCurrentLandValue(
					Integer.parseInt(userSearchInput[2]),
					Integer.parseInt(userSearchInput[3])));
		}
		if (!userSearchInput[8].equals("") && !userSearchInput[9].equals("")) {
			results.retainAll(store.searchByCurrentImprovementValue(
					Integer.parseInt(userSearchInput[8]),
					Integer.parseInt(userSearchInput[9])));
		}
		if (!userSearchInput[10].equals("") && !userSearchInput[11].equals("")) {
			results.retainAll(store.searchByAssessmentYear(
					Integer.parseInt(userSearchInput[10]),
					Integer.parseInt(userSearchInput[11])));
		}
		if (!userSearchInput[12].equals("") && !userSearchInput[13].equals("")) {
			results.retainAll(store.searchByPreviousLandValue(
					Integer.parseInt(userSearchInput[12]),
					Integer.parseInt(userSearchInput[13])));
		}
		if (!userSearchInput[14].equals("") && !userSearchInput[15].equals("")) {
			results.retainAll(store.searchByPreviousImprovementValue(
					Integer.parseInt(userSearchInput[14]),
					Integer.parseInt(userSearchInput[15])));
		}
		if (!userSearchInput[16].equals("") && !userSearchInput[17].equals("")) {
			results.retainAll(store.searchByYearBuilt(
					Integer.parseInt(userSearchInput[16]),
					Integer.parseInt(userSearchInput[17])));
		}
		if (!userSearchInput[18].equals("") && !userSearchInput[19].equals("")) {
			results.retainAll(store.searchByBigImprovementYear(
					Integer.parseInt(userSearchInput[18]),
					Integer.parseInt(userSearchInput[19])));
		}
		return results;
	}

	@Override
	public List<String> getHomesByUser(String email) {
		ArrayList<String> workingIDStore = new ArrayList<String>();
		workingIDStore.addAll(store.searchByOwner(email));
		return workingIDStore;
	}

	@Override
	public int getHouseDatabaseLength() {
		return store.getAllKeys().size();
	}
}
