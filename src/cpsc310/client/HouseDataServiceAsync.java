package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Asynchronous call to server to fetch house data
 */
public interface HouseDataServiceAsync {
	public void getHouses(int start, int range, AsyncCallback<List<HouseData>> callback);
	public void getHousesByPriceRange(int lowerVal, int upperVal, 
			AsyncCallback<List<HouseData>> callback);
	public void getHousesByCoordRange(int lowerCoord, int upperCoord, 
			AsyncCallback<List<HouseData>> callback);
	public void getHousesByOwner(String owner, AsyncCallback<List<HouseData>> callback);
	public void getHouseDatabaseLength(AsyncCallback<Integer> callback);
	public void sortHouses(AsyncCallback<Void> callback);
	public void updateHouses(String Owner, double price, boolean isSelling, HouseData house, AsyncCallback<Void> callback);
}
