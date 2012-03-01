package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Asynchronous call to server to fetch house data.
 * For information about what each method does, please refer to HouseDataService.java
 * or HouseDataServiceImpl.java.
 */
public interface HouseDataServiceAsync {
	public void buildHouseDataPointStore(AsyncCallback<Void> callback);
	public void getHouses(int start, int range, AsyncCallback<List<HouseData>> callback);
	public void getSearchedHouses(int lowerCoord, int upperCoord, 
			int lowerVal, int upperVal, String owner, AsyncCallback<List<HouseData>> callback);
	public void getHouseDatabaseLength(AsyncCallback<Integer> callback);
	public void sortHouses(AsyncCallback<Void> callback);
	public void updateHouses(String Owner, double price, boolean isSelling, HouseData house, AsyncCallback<Void> callback);
}
