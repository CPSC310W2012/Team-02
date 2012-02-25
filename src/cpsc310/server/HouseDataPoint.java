package cpsc310.server;

import com.google.gwt.dev.util.collect.HashMap;

/*
 * A single data point value representing a house
 */
public class HouseDataPoint {
	
	//variables
	//tentinitative stable; may be removing or adding additional ones often early on
	private int pid;
	private int coordinate;
	private double price;
	private String owner;
	private String address;
	private String postalCode;
	private boolean isSelling;
	private int landValue;
	
	/*
	 * Constructor
	 * @Pre: a hashMap Object(keys are table headers)
	 * @Post: a houseDataPoint Object
	 */
	public HouseDataPoint(HashMap CSVRowHashMap)
	{
		
	}
}
