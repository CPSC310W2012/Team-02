package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client side stub for fetching HouseDataPoint
 */
@RemoteServiceRelativePath("houseData")
public interface HouseDataService extends RemoteService {
	
	public List<HouseData> getHouses(int start, int range);
	public List<HouseData> getHousesByPriceRange(int lowerVal, int upperVal);
	public List<HouseData> getHousesByCoordRange(int lowerCoord, int upperCoord);
	public List<HouseData> getHousesByOwner(String owner);
	public int getHouseDatabaseLength();
	public void sortHouses();
	public void updateHouses(String Owner, double price, boolean isSelling, HouseData house);
}
