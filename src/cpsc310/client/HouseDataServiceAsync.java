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

	public void getSearchedHouses(String[] userSearchInput, int isSelling,
			AsyncCallback<List<HouseData>> callback);

	public void getHouseDatabaseLength(AsyncCallback<Integer> callback);

	public void sortHouses(AsyncCallback<Void> callback);

	public void updateHouse(String Owner, int price, boolean isSelling,
			HouseData house, double latitude, double longitude,
			AsyncCallback<Void> callback);
}
