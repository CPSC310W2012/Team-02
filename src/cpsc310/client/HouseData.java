package cpsc310.client;

import java.io.Serializable;
import java.util.Comparator;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Class representing a single house in the table.
 * This is a java data transfer object.
 * Please see server-side HouseDataPoint.java for javadoc description.
 */
public class HouseData implements Serializable{

	// Class variables representing a house. 
	// Client-side complement to server-side HouseDataPoint.java
	private String houseID;
	private int civicNumber;
	private String streetName;
	private String postalCode;
	private int currentLandValue;
	private int currentImprovementValue;
	private int assessmentYear;
	private int previousLandValue;
	private int previousImprovementValue;
	private int yearBuilt;
	private int bigImprovementYear;
	
	// User specified
	private String owner;
	private int price;
	private boolean isSelling;
	
	
	/**
	 * Constructor for enabling Serializable.
	 */
	public HouseData() {
	}
	

	public HouseData(String houseID, int civicNumber, String streetName, 
			String postalCode,	int currentLandValue, int currentImprovementValue,
			int previousLandValue, int previousImprovementValue, int yearBuilt,
			int bigImprovementYear, String owner, int price, boolean isSelling) {
		this.houseID = houseID;
		this.civicNumber = civicNumber;
		this.streetName = streetName;
		this.postalCode = postalCode;
		this.currentLandValue = currentLandValue;
		this.currentImprovementValue = currentImprovementValue;
		this.previousLandValue = previousLandValue;
		this.previousImprovementValue = previousImprovementValue;
		this.yearBuilt = yearBuilt;
		this.bigImprovementYear = bigImprovementYear;

		this.owner = owner;
		this.price = price;
		this.isSelling = isSelling;
	}

	
	// Key provider for table and map selection
	public static final ProvidesKey<HouseData> KEY_PROVIDER = new ProvidesKey<HouseData>() {
		public Object getKey (HouseData house) {
			return house == null ? null : house.getHouseID(); 
		}
	};
	
	// Getters
	public String getHouseID() {
		return houseID;
	}

	public int getCivicNumber() {
		return civicNumber;
	}

	public String getStreetName() {
		return streetName;
	}
	
	public String getAddress() {
		return civicNumber + " " + streetName;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public int getCurrentLandValue() {
		return currentLandValue;
	}

	public int getCurrentImprovementValue() {
		return currentImprovementValue;
	}

	public int getAssessmentYear() {
		return assessmentYear;
	}

	public int getPreviousLandValue() {
		return previousLandValue;
	}

	public int getPreviousImprovementValue() {
		return previousImprovementValue;
	}

	public int getYearBuilt() {
		return yearBuilt;
	}

	public int getBigImprovementYear() {
		return bigImprovementYear;
	}

	public String getOwner() {
		return owner;
	}

	public boolean getIsSelling() {
		return isSelling;
	}

	public int getPrice() {
		return price;
	}

	// setters
	public void setHouseID(String id) {
		houseID = id;
	}

	public void setCivicNumber(int number) {
		civicNumber = number;
	}

	public void setStreetName(String name) {
		streetName = name;
	}

	public void setPostalCode(String pc) {
		postalCode = pc;
	}

	public void setCurrentLandValue(int value) {
		currentLandValue = value;
	}

	public void setCurrentImprovementValue(int value) {
		currentImprovementValue = value;
	}

	public void setAssessmentYear(int year) {
		assessmentYear = year;
	}

	public void setPreviousLandValue(int value) {
		previousLandValue = value;
	}

	public void setPreviousImporvementValue(int year) {
		previousImprovementValue = year;
	}

	public void setYearBuilt(int year) {
		yearBuilt = year;
	}

	public void setBigImprovementYear(int year) {
		bigImprovementYear = year;
	}
	
	public void setOwner(String newOwner) {
		owner = newOwner;
	}

	public void setIsSelling(boolean sell) {
		isSelling = sell;
	}

	public void setPrice(int salePrice) {
		price = salePrice;
	}
	
	/**
	 * Comparators for table sorting.
	 */
	
	//Comparator for PIDs
	public final static Comparator<HouseData> HouseIDComparator =
			new Comparator<HouseData>() {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getHouseID().compareTo(house2.getHouseID());
		}
	};
	
	//Comparator for house addresses
	public final static Comparator<HouseData> HouseAddrComparator =
			new Comparator<HouseData>() {
		public int compare (HouseData house1, HouseData house2) {
			String[] addr1 = house1.getStreetName().split("[ ]");
			String[] addr2 = house2.getStreetName().split("[ ]");
			int streetNum1 = house1.getCivicNumber();
			int streetNum2 = house2.getCivicNumber();
			
			// Compare Street Name
			if (addr1[0].compareTo(addr2[0]) == 0) {
				if (addr1[1].compareTo(addr2[1]) == 0){
					return streetNum1 > streetNum2 ? 1 :
						streetNum1 == streetNum2 ? 0 : -1;
				}
				return addr1[1].compareTo(addr2[1]);
			}
			return addr1[0].compareTo(addr2[0]);
		}
	};
	
	//Comparator for postal codes
	public final static Comparator<HouseData> HousePostalCodeComparator =
			new Comparator<HouseData>() {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPostalCode().compareTo(house2.getPostalCode());
		}
	};
		
	//Comparator for the value of the land the house resides on
	public final static Comparator<HouseData> HouseCurrentLandValueComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getCurrentLandValue() > house2.getCurrentLandValue() ? 1 : 
				house1.getCurrentLandValue() == house2.getCurrentLandValue() ? 0 : -1;
		}
	};
	
	//Comparator for current improvement value
	public final static Comparator<HouseData> HouseCurrentImprovementValueComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getCurrentImprovementValue() > house2.getCurrentImprovementValue() ? 1 : 
				house1.getCurrentImprovementValue() == house2.getCurrentImprovementValue() ? 0 : -1;
		}
	};
	
	//Comparator for assessment year
	public final static Comparator<HouseData> HouseAssYearComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getAssessmentYear() > house2.getAssessmentYear() ? 1 : 
				house1.getAssessmentYear() == house2.getAssessmentYear() ? 0 : -1;
		}
	};	
	
	//Comparator for the previous value of the land the house resides on
	public final static Comparator<HouseData> HousePrevLandValueComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPreviousLandValue() > house2.getPreviousLandValue() ? 1 : 
				house1.getPreviousLandValue() == house2.getPreviousLandValue() ? 0 : -1;
		}
	};
	
	//Comparator for previous improvement value
	public final static Comparator<HouseData> HousePrevImprovementValueComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPreviousImprovementValue() > house2.getPreviousImprovementValue() ? 1 : 
				house1.getPreviousImprovementValue() == house2.getPreviousImprovementValue() ? 0 : -1;
		}
	};
	
	//Comparator for built year
	public final static Comparator<HouseData> HouseYearBuiltComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getYearBuilt() > house2.getYearBuilt() ? 1 : 
				house1.getYearBuilt() == house2.getYearBuilt() ? 0 : -1;
		}
	};		
	
	//Comparator for built year
	public final static Comparator<HouseData> HouseBigImprYearComparator = 
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getBigImprovementYear() > house2.getBigImprovementYear()  ? 1 : 
				house1.getBigImprovementYear()  == house2.getBigImprovementYear()  ? 0 : -1;
		}
	};		
		
	
	//Comparator for the realtor of the house
	public final static Comparator<HouseData> HouseOwnerComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getOwner().compareTo(house2.getOwner());
		}
	};
	
	//Comparator for house prices
	public final static Comparator<HouseData> HousePriceComparator =
			new Comparator<HouseData> () {
		public int compare (HouseData house1, HouseData house2) {
			return house1.getPrice() > house2.getPrice() ? 1 : 
				house1.getPrice() == house2.getPrice() ? 0 : -1;
		}
	};
	
	//Comparator for whether or not a house is for sale
	public final static Comparator<HouseData> HouseIsSellingComparator = 
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