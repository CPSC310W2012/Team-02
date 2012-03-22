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

	public void getSearchedHouses(String[] userSearchInput, int isSelling,
			AsyncCallback<List<HouseData>> callback);

	public void getHouseDatabaseLength(AsyncCallback<Integer> callback);

	public void sortByAddress(AsyncCallback<Void> callback);

	public void sortByPostalCode(AsyncCallback<Void> callback);

	public void sortByOwner(AsyncCallback<Void> callback);

	public void sortByForSale(AsyncCallback<Void> callback);

	public void sortByCurrentLandValue(AsyncCallback<Void> callback);

	public void sortByCurrentImprovementValue(AsyncCallback<Void> callback);

	public void sortByAssessmentYear(AsyncCallback<Void> callback);

	public void sortByPreviousLandValue(AsyncCallback<Void> callback);

	public void sortByPreviousImprovementValue(AsyncCallback<Void> callback);

	public void sortByYearBuilt(AsyncCallback<Void> callback);

	public void sortByBigImprovementYear(AsyncCallback<Void> callback);

	public void sortByPrice(AsyncCallback<Void> callback);

	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude,
			String postalCode, AsyncCallback<Void> callback);

	public void getStreetNames(AsyncCallback<List<String>> callback);
	
	public void refreshIDStore(AsyncCallback<Void> callback);
}
