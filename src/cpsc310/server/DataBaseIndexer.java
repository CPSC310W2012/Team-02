package cpsc310.server;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;

/*
 * Stores a list of the indexes of the house datapoints
 */
@Entity
public class DataBaseIndexer {
	
	@Id
	private String name;
	
	private ArrayList<HouseDataPoint> houses;
	
	public DataBaseIndexer(){
		name = "DataBaseIndexer";
		houses = new ArrayList<HouseDataPoint>();
	}
	
	public void addToIndex(HouseDataPoint house){
		houses.add(house);
	}
	
	public ArrayList<HouseDataPoint> getHouses(){
		return houses;
	}
}
