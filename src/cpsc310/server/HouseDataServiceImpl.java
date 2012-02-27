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
	
	@Override
	public List<HouseData> getHouses(int start, int range) {
		List<HouseDataPoint> houses = new ArrayList<HouseDataPoint>();
		
		// TODO Grab data from database
		List<HouseData> tmpHouse = new ArrayList<HouseData>();
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
		return 0;
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
