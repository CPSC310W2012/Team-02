package cpsc310.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous call to server to fetch house data. For information about what
 * each method does, please refer to HouseDataService.java or
 * HouseDataServiceImpl.java.
 */
public interface HouseDataServiceAsync {
	public void getHouses(int start, int range,
			AsyncCallback<List<HouseData>> callback);

	public void getHouses(List<String> list, int start, int range,
			AsyncCallback<List<HouseData>> callback);

	public void searchHouses(String[] userSearchInput, int isSelling,
			AsyncCallback<Void> callback);

	public void getHouseDatabaseLength(AsyncCallback<Integer> callback);

	public void sortByAddress(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByPostalCode(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByOwner(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByForSale(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByCurrentLandValue(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByCurrentImprovementValue(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByAssessmentYear(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByPreviousLandValue(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByPreviousImprovementValue(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByYearBuilt(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByBigImprovementYear(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByPrice(boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude,
			String postalCode, AsyncCallback<Void> callback);

	public void getStreetNames(AsyncCallback<List<String>> callback);

	public void refreshIDStore(AsyncCallback<Void> callback);

	public void resetHouse(String houseID, AsyncCallback<Void> callback);
	
	public void retrieveSingleHouse(int civicNumber, String streetName, AsyncCallback<HouseData> callback);
}
