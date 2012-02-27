package cpsc310.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.HouseDataService;
import cpsc310.client.HouseData;

/**
 * Server side methods to fetch house data
 */
public class HouseDataServiceImpl extends RemoteServiceServlet implements
		HouseDataService {
	
	@Override
	public List<HouseData> getHouses(int start, int range) {
		List<HouseData> houses = new ArrayList<HouseData>();
		
		// TODO Grab data from database
		List<HouseData> tmpHouse = new ArrayList<HouseData>();
		HouseData test = new HouseData(025530151, "1295 RICHARDS ST", "V6K 1M3", 123, 93821, "John Doe", true, 399928);
		tmpHouse.add(test);
		tmpHouse.add(new HouseData (2000299, "1828 WEST BLVD", "V9N 1L2", 332, 248882, "", false, 0));
		tmpHouse.add(new HouseData (4099223, "1828 EAST BLVD", "V9N 2L2", 331, 244882, "", false, 0));
		tmpHouse.add(new HouseData (188281, "122 CAMBIE ST", "V7K 1H2", 12, 38823, "", false, 0));
		return tmpHouse;
	}

	@Override
	public List<HouseData> getHousesByPriceRange (int lowerVal, int upperVal) {
		List<HouseData> houses = new ArrayList<HouseData>();
		// TODO Grab data from database
		return null;
	}

	@Override
	public List<HouseData> getHousesByCoordRange(int lowerCoord, int upperCoord) {
		List<HouseData> houses = new ArrayList<HouseData>();
		// TODO Grab data from database
		return null;
	}

	@Override
	public List<HouseData> getHousesByOwner(String owner) {
		List<HouseData> houses = new ArrayList<HouseData>();
		// TODO Grab data from database
		return null;
	}
	
	@Override
	public int getHouseDatabaseLength() {
		// TODO get database length
		
		// TODO temporary. change when above gets implemented
		return 4;
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
