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

	public void sortByAddress(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByPostalCode(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByOwner(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByForSale(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByCurrentLandValue(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByCurrentImprovementValue(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByAssessmentYear(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByPreviousLandValue(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByPreviousImprovementValue(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByYearBuilt(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByBigImprovementYear(List<String> list,
			AsyncCallback<List<String>> callback);

	public void sortByPrice(List<String> list,
			AsyncCallback<List<String>> callback);

	public void updateHouse(String Owner, int price, boolean isSelling,
			String houseID, double latitude, double longitude,
			String postalCode, AsyncCallback<Void> callback);

	public void getStreetNames(AsyncCallback<List<String>> callback);
}
