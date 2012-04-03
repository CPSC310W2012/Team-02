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
	public void getHouses(int instanceID, int start, int range,
			AsyncCallback<List<HouseData>> callback);

	public void searchHouses(int instanceID, String[] userSearchInput,
			int isSelling, AsyncCallback<Void> callback);

	public void getHouseDatabaseLength(int instanceID,
			AsyncCallback<Integer> callback);

	public void sortByAddress(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByPostalCode(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByOwner(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByForSale(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByCurrentLandValue(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByCurrentImprovementValue(int instanceID,
			boolean isSortAscending, AsyncCallback<Void> callback);

	public void sortByAssessmentYear(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByPreviousLandValue(int instanceID,
			boolean isSortAscending, AsyncCallback<Void> callback);

	public void sortByPreviousImprovementValue(int instanceID,
			boolean isSortAscending, AsyncCallback<Void> callback);

	public void sortByYearBuilt(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void sortByBigImprovementYear(int instanceID,
			boolean isSortAscending, AsyncCallback<Void> callback);

	public void sortByPrice(int instanceID, boolean isSortAscending,
			AsyncCallback<Void> callback);

	public void updateHouse(String Owner, int price,
			boolean isSelling, String houseID, double latitude,
			double longitude, String postalCode, AsyncCallback<Void> callback);

	public void getStreetNames(AsyncCallback<List<String>> callback);

	public void refreshIDStore(int instanceID, AsyncCallback<Void> callback);

	public void resetHouse(String houseID,
			AsyncCallback<Void> callback);

	public void retrieveSingleHouse(int instanceID, int civicNumber,
			String streetName, AsyncCallback<HouseData> callback);

	public void searchHousesForSalePolygon(int instanceID,
			String[] userSearchInput, double[] latitude, double[] longitude,
			AsyncCallback<Void> callback);

	public void getHomesByUser(int instanceID, String email,
			AsyncCallback<Void> callback);

	public void getInstanceID(AsyncCallback<Integer> callback);
}
