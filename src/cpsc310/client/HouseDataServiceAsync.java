package cpsc310.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous call to server to fetch house data. For information about what
 * each method does, please refer to HouseDataService.java or
 * HouseDataServiceImpl.java.
 */
public interface HouseDataServiceAsync {

	public void getHouses(List<String> list, int start, int range,
			AsyncCallback<List<HouseData>> callback);

	public void searchHouses(String[] userSearchInput, int isSelling,
			AsyncCallback<List<String>> callback);

	public void sortByAddress(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByPostalCode(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByOwner(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByForSale(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByCurrentLandValue(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByCurrentImprovementValue(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByAssessmentYear(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByPreviousLandValue(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByPreviousImprovementValue(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByYearBuilt(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByBigImprovementYear(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void sortByPrice(boolean isSortAscending, List<String> houseIDs,
			AsyncCallback<List<String>> callback);

	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude,
			String postalCode, AsyncCallback<Void> callback);

	public void getStreetNames(AsyncCallback<List<String>> callback);

	public void refreshIDStore(AsyncCallback<List<String>> callback);

	public void resetHouse(String houseID, AsyncCallback<Void> callback);

	public void retrieveSingleHouse(int civicNumber, String streetName,
			AsyncCallback<HouseData> callback);

	public void searchHousesForSalePolygon(String[] userSearchInput,
			double[] latitude, double[] longitude, AsyncCallback<List<String>> callback);

	public void getHomesByUser(String email, AsyncCallback<List<String>> callback);
	
	public void getHouseDatabaseLength(AsyncCallback<Integer> callback);
}
