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
	private List<ArrayList<String>> instancedHouseLists;

	/**
	 * Constructor
	 */
	public HouseDataServiceImpl() {
		store = new DataStore();
		instancedHouseLists = new ArrayList<ArrayList<String>>();
	}

	public void refreshIDStore(int instanceID) {
		instancedHouseLists.add(instanceID, store.getAllKeys());
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
	public List<HouseData> getHouses(int instanceID, int start, int range) {
		// retrieve house data points
		List<HouseDataPoint> tempList = store.getHouses(
				instancedHouseLists.get(instanceID), start, range);

		// Convert HouseDataPoint into HouseData
		if (tempList == null) {
			tempList = new ArrayList<HouseDataPoint>();
		}
		List<HouseData> grab = convertToListHouseData(tempList);

		// Change below to return grab
		return grab;
	}

	@Override
	public void searchHouses(int instanceID, String[] userSearchInput,
			int isSelling) {
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
		instancedHouseLists.add(instanceID, convertedResults);
	}

	/**
	 * Helper to table drawing to figure out how many rows need to exist.
	 * 
	 * @return size of database
	 */
	@Override
	public int getHouseDatabaseLength(int instanceID) {
		int databaseLength = instancedHouseLists.get(instanceID).size();
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
	public void sortByAddress(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID,
					store.sortByHouseID(instancedHouseLists.get(instanceID)));
		} else {
			instancedHouseLists
					.add(instanceID, store.sortByHouseIDDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByPostalCode(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists
					.add(instanceID, store.sortByPostalCode(instancedHouseLists
							.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByPostalCodeDes(instancedHouseLists.get(instanceID)));
		}
	}

	@Override
	public void sortByOwner(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID,
					store.sortByOwner(instancedHouseLists.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID,
					store.sortByOwnerDes(instancedHouseLists.get(instanceID)));
		}
	}

	@Override
	public void sortByForSale(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID,
					store.sortByForSale(instancedHouseLists.get(instanceID)));
		} else {
			instancedHouseLists
					.add(instanceID, store.sortByForSaleDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByCurrentLandValue(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID,
					store.sortByCurrentLandValue(instancedHouseLists
							.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByCurrentLandValueDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByCurrentImprovementValue(int instanceID,
			boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID, store
					.sortByCurrentImprovementValue(instancedHouseLists
							.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByCurrentImprovementValueDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByAssessmentYear(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID, store
					.sortByAssessmentYear(instancedHouseLists.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByAssessmentYearDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByPreviousLandValue(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID, store
					.sortByPreviousLandValue(instancedHouseLists
							.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByPreviousLandValueDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByPreviousImprovementValue(int instanceID,
			boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID, store
					.sortByPreviousImprovementValue(instancedHouseLists
							.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByPreviousImprovementValueDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByYearBuilt(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID,
					store.sortByYearBuilt(instancedHouseLists.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByYearBuiltDes(instancedHouseLists.get(instanceID)));
		}
	}

	@Override
	public void sortByBigImprovementYear(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID, store
					.sortByBigImprovementYear(instancedHouseLists
							.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID, store
					.sortByBigImprovementYearDes(instancedHouseLists
							.get(instanceID)));
		}
	}

	@Override
	public void sortByPrice(int instanceID, boolean isSortAscending) {
		if (isSortAscending) {
			instancedHouseLists.add(instanceID,
					store.sortByPrice(instancedHouseLists.get(instanceID)));
		} else {
			instancedHouseLists.add(instanceID,
					store.sortByPriceDes(instancedHouseLists.get(instanceID)));
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
	public HouseData retrieveSingleHouse(int instanceID, int civicNumber,
			String street) {
		instancedHouseLists.add(instanceID, new ArrayList<String>());
		instancedHouseLists.get(instanceID).addAll(
				store.searchByAddress(civicNumber, street));
		HouseDataPoint currentHDP = store.getHouses(
				instancedHouseLists.get(instanceID), 0, 1).get(0);
		return convertToHouseData(currentHDP);
	}

	@Override
	public void searchHousesForSalePolygon(int instanceID,
			String[] userSearchInput, double[] latitude, double[] longitude) {
		// TODO: Degbuging - Remove
		System.out.println("Polygon Search");

		Set<String> results = store.searchForSaleInPolygon(latitude, longitude);

		results = firstPassSearch(userSearchInput, results);
		results = secondPassSearch(userSearchInput, results);

		// convert to array
		ArrayList<String> convertedResults = new ArrayList<String>();
		convertedResults.addAll(results);
		instancedHouseLists.add(instanceID, convertedResults);
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
	public void getHomesByUser(int instanceID, String email) {
		instancedHouseLists.add(instanceID, new ArrayList<String>());
		instancedHouseLists.get(instanceID).addAll(store.searchByOwner(email));
	}

	@Override
	public int getInstanceID() {
		int instanceID = instancedHouseLists.size();
		instancedHouseLists.add(instanceID, store.getAllKeys());
		return instanceID;
	}
}
