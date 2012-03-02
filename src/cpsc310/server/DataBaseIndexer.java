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
	private ArrayList<String> houses;
	
	public DataBaseIndexer(){
		name = "DataBaseIndexer";
		houses = new ArrayList<String>();
	}
	
	public void addToIndex(String house){
		houses.add(house);
	}
	
	public ArrayList<String> getHouses(){
		return houses;
	}
	
	public int size(){
		return houses.size();
	}
}
