package cpsc310.server;

import com.google.gwt.dev.util.collect.HashMap;

/*
 * A single data point value representing a house
 */
public class HouseDataPoint {

	// variables
	// tentatively stable; may be removing or adding additional ones often
	// early on
	// Variables to be set by house data
	private int pid;
	private int coordinate;
	private String address;
	private String postalCode;
	private int landValue;

	// User specified data
	private String owner;
	private boolean isSelling;
	private double price;

	/*
	 * Constructor
	 * 
	 * @pre: a hashMap Object(keys are table headers)
	 * @post: a houseDataPoint Object
	 */
	public HouseDataPoint(HashMap<String, String> houseRow) {
		// Variables to be set by house data
		pid = Integer.parseInt(houseRow.get("pid"));
		coordinate = Integer.parseInt(houseRow.get("coordinate"));
		address = houseRow.get("address");
		postalCode = houseRow.get("postalCode");
		landValue = Integer.parseInt(houseRow.get("landValue"));

		// User specified data
		owner = null;
		isSelling = false;
		price = 0;
	}

	//getters
	public int getPID() {
		return pid;
	}

	public int getCoordinate() {
		return coordinate;
	}

	public String getAddress() {
		return address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public int getLandValue() {
		return landValue;
	}
	
	public String getOwner() {
		return owner;
	}

	public boolean getIsSelling() {
		return isSelling;
	}

	public double getPrice() {
		return price;
	}
	
	//setters
	public void setOwner(String newOwner)
	{
		owner = newOwner;
	}
	public void setIsSelling(boolean sell)
	{
		isSelling = sell;
	}
	public void setPrice(double salePrice)
	{
		price = salePrice;
	}
}
