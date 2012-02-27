package cpsc310.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.HouseData;
import cpsc310.client.HouseDataService;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {
	
	private List<HouseData> HOUSES = new ArrayList<HouseData>();
	private HouseData test = new HouseData(29992, "123 12th AVE W", "V6K 1M3", 123, 93821, "John Doe", true, 399928);
	
	@Override
	public List<HouseData> getHouses(int start, int range) {
		List<HouseDataPoint> houses = new ArrayList<HouseDataPoint>();
		
		// TODO Grab data from database
		List<HouseData> tmpHouse = new ArrayList<HouseData>();
		HOUSES.set(0, test);
		tmpHouse.addAll(HOUSES);
		return tmpHouse;
	}

	@Override
	public List<HouseData> getHousesByPriceRange (int lowerVal, int upperVal) {
		List<HouseDataPoint> houses = new ArrayList<HouseDataPoint>();
		// TODO Grab data from database
		return null;
	}

	@Override
	public List<HouseData> getHousesByCoordRange(int lowerCoord, int upperCoord) {
		List<HouseDataPoint> houses = new ArrayList<HouseDataPoint>();
		// TODO Grab data from database
		return null;
	}

	@Override
	public List<HouseData> getHousesByOwner(String owner) {
		List<HouseDataPoint> houses = new ArrayList<HouseDataPoint>();
		// TODO Grab data from database
		return null;
	}
	
	@Override
	public int getHouseDatabaseLength() {
		// TODO get database length
		
		// TODO temporary. change when above gets implemented
		return HOUSES.size();
	}	

	@Override
	public void sortHouses() {
		// TODO sort database
		
	}	
	
	@Override
	public void updateHouses(String Owner, double price, boolean isSelling, HouseData house) {
		// TODO Update data in database
		int pid = house.getPID();
	}

}
