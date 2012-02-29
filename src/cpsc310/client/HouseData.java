package cpsc310.client;

import java.io.Serializable;
import java.util.Comparator;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Class representing a single house in the table.
 * This is a java data transfer object.
 */
public class HouseData implements Serializable{

	// Class variables representing 
	// may change depending on the server implementation of the database and
	// HouseDataPoint.java
	private int pid;
	private int coordinate;
	private String address;
	private String postalCode;
	private double landValue;
	private String owner;
	private boolean isSelling;
	private double price;

	/**
	 * Constructor for enabling Serializable.
	 */
	public HouseData() {
	}
	
	/**
	 * Constructor of the class 
	 */
	public HouseData(int pid, String address, String postalCode, int coordinate, 
			int landValue, String owner, boolean isSelling, double price) {
		this.pid = pid;
		this.coordinate = coordinate;
		this.address = address;
		this.postalCode = postalCode;
		this.landValue = landValue;

		this.owner = owner;
		this.isSelling = isSelling;
		this.price = price;
	}

	
	// Key provider for table and map selection
	public static final ProvidesKey<HouseData> KEY_PROVIDER = new ProvidesKey<HouseData>() {
		public Object getKey (HouseData house) {
			return house == null ? null : house.getPID(); 
		}
	};
	
	// Getters
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
	
	// Setters
	public void setPID(int PID) {
		this.pid = PID;
	}
	
	public void setCoordinate(int coordinate) {
		this.coordinate = coordinate;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	
	public void setLandValue(double landValue) {
		this.landValue = landValue;
	}
	
	public void setOwner(String newOwner) {
		owner = newOwner;
	}

	public void setIsSelling(boolean sell) {
		isSelling = sell;
	}

	public void setPrice(double salePrice) {
		price = salePrice;
	}	
	
	/**
	 * Comparators for table sorting.
	 */
	
	public static Comparator<HouseData> HousePidComparator =
			new Comparator<HouseData>() {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPID() > house2.getPID() ? 1 : 
				house1.getPID() == house2.getPID() ? 0 : -1;
		}
	};
	
	public static Comparator<HouseData> HouseAddrComparator =
			new Comparator<HouseData>() {
		public int compare (HouseData house1, HouseData house2) {
			String[] addr1 = house1.getAddress().split("[ ]");
			String[] addr2 = house2.getAddress().split("[ ]");
			
			// Compare Street Name
			if (addr1[1].compareTo(addr2[1]) == 0) {
				if (addr1[2].compareTo(addr2[2]) == 0){
					return addr1[0].compareTo(addr2[0]);
				}
				return addr1[2].compareTo(addr2[2]);
			}
			return addr1[1].compareTo(addr2[1]);
		}
	};
	
	public static Comparator<HouseData> HousePostalCodeComparator =
			new Comparator<HouseData>() {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPostalCode().compareTo(house2.getPostalCode());
		}
	};
	
	public static Comparator<HouseData> HouseCoordinateComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getCoordinate() > house2.getCoordinate() ? 1 : 
				house1.getCoordinate() == house2.getCoordinate() ? 0 : -1;
		}
	};
	
	public static Comparator<HouseData> HouseLandValueComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getLandValue() > house2.getLandValue() ? 1 : 
				house1.getLandValue() == house2.getLandValue() ? 0 : -1;
		}
	};
	
	public static Comparator<HouseData> HouseOwnerComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getOwner().compareTo(house2.getOwner());
		}
	};
	
	public static Comparator<HouseData> HousePriceComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPrice() > house2.getPrice() ? 1 : 
				house1.getPrice() == house2.getPrice() ? 0 : -1;
		}
	};
	
	public static Comparator<HouseData> HouseIsSellingComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData o1, HouseData o2) {
			if (o1.getIsSelling() == true && o2.getIsSelling() == false)
				return 1;
			if (o1.getIsSelling() == o2.getIsSelling())
				return 0;
			return -1;
		}
	};
}