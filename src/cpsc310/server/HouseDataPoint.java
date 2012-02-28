package cpsc310.server;

import java.io.Serializable;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.view.client.ProvidesKey;

/*
 * A single data point value representing a house
 */
public class HouseDataPoint implements Serializable {

	// variables
	// tentatively stable; may be removing or adding additional ones often
	// early on
	// Variables to be set by house data
	private String pid;
	private int coordinate;
	private String address;
	private String postalCode;
	private double landValue;

	// User specified data
	private String owner;
	private boolean isSelling;
	private double price;

	/*
	 * Constructor
	 */
	public HouseDataPoint(String pid, String address, String postalCode,
			int coordinate, int landValue, String owner, boolean isSelling,
			double price) {
		this.pid = pid;
		this.coordinate = coordinate;
		this.address = address;
		this.postalCode = postalCode;
		this.landValue = landValue;

		this.owner = owner;
		this.isSelling = isSelling;
		this.price = price;
	}

	/*
	 * Constructor
	 * 
	 * @pre: a hashMap Object(keys are table headers)
	 * 
	 * @post: a houseDataPoint Object
	 */
	public HouseDataPoint(HashMap<String, String> houseRow) {
		// Variables to be set by house data
		pid = houseRow.get("PID");
		coordinate = Integer.parseInt(houseRow.get("LAND_COORDINATE"));
		address = houseRow.get("TO_CIVIC_NUMBER") + " "
				+ houseRow.get("STREET_NAME");
		postalCode = houseRow.get("PROPERTY_POSTAL_CODE");
		if (!houseRow.get("CURRENT_LAND_VALUE").isEmpty()) {
			landValue = Double.parseDouble(houseRow.get("CURRENT_LAND_VALUE"));
		} else {
			landValue = 0;
		}

		// User specified data
		owner = null;
		isSelling = false;
		price = 0;
	}

	public HouseDataPoint() {
	}

	//
	public static final ProvidesKey<HouseDataPoint> KEY_PROVIDER = new ProvidesKey<HouseDataPoint>() {
		public Object getKey(HouseDataPoint house) {
			return house == null ? null : house.getPID();
		}
	};

	// getters
	public String getPID() {
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

	public double getLandValue() {
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

	// setters
	public void setOwner(String newOwner) {
		owner = newOwner;
	}

	public void setIsSelling(boolean sell) {
		isSelling = sell;
	}

	public void setPrice(double salePrice) {
		price = salePrice;
	}
}
