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
	

	/**
	 * Constructor
	 * 
	 * @param houseID - unique ID of house which identifies the object in the database 
	 * @param civicNumber - street number
	 * @param streetName - name of the street 
	 * @param postalCode - postal code of the house
	 * @param currentLandValue - current land value of the house
	 * @param currentImprovementValue - current improvement of the house
	 * @param previousLandValue - previous property tax value of the house
	 * @param previousImprovementValue - previous improvement of the house
	 * @param yearBuilt - year when this house was built
	 * @param bigImprovementYear - year when big improvement happened
	 * @param owner - realtor of the house
	 * @param price - price of the house
	 * @param isSelling - indicator whether this house is for sale
	 */
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

	
	/**
	 * Key provider for table and map selection
	 */
	public static final ProvidesKey<HouseData> KEY_PROVIDER = new ProvidesKey<HouseData>() {
		public Object getKey (HouseData house) {
			return house == null ? null : house.getHouseID(); 
		}
	};
	

	/**
	 * Getter for house id
	 * @return house id of the house
	 */
	public String getHouseID() {
		return houseID;
	}

	/**
	 * Getter for civic number
	 * @return street number of the house
	 */
	public int getCivicNumber() {
		return civicNumber;
	}

	/**
	 * Getter for street name
	 * @return the street name of the house address
	 */
	public String getStreetName() {
		return streetName;
	}
	
	/**
	 * Getter for address
	 * @return a string assembled into a full address
	 */
	public String getAddress() {
		return civicNumber + " " + streetName;
	}

	/**
	 * Getter for postal code
	 * @return postal code of the house
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * Getter for Current Land Value
	 * @return current land value of the house
	 */
	public int getCurrentLandValue() {
		return currentLandValue;
	}

	/**
	 * Getter for current improvement value
	 * @return current improvement value of the house
	 */
	public int getCurrentImprovementValue() {
		return currentImprovementValue;
	}

	/**
	 * Getter for assessment year
	 * @return year that current land value was assessed
	 */
	public int getAssessmentYear() {
		return assessmentYear;
	}

	/**
	 * Getter for previous land value
	 * @return value of the land from the previous assessment 
	 */
	public int getPreviousLandValue() {
		return previousLandValue;
	}

	/**
	 * Getter for previous improvement value
	 * @return previous improvement value of the house
	 */
	public int getPreviousImprovementValue() {
		return previousImprovementValue;
	}

	/**
	 * Getter for year built
	 * @return year that the house was built
	 */
	public int getYearBuilt() {
		return yearBuilt;
	}

	/**
	 * Getter for big improvement year
	 * @return year when big improvement happened
	 */
	public int getBigImprovementYear() {
		return bigImprovementYear;
	}

	/**
	 * Getter for owner
	 * @return name of the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * Getter for isSelling
	 * @return boolean value indicating whether the house is for sale
	 */
	public boolean getIsSelling() {
		return isSelling;
	}

	/**
	 * Getter for price
	 * @return price of house
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Setter for houseID
	 * @param id - unique id of the house
	 */
	public void setHouseID(String id) {
		houseID = id;
	}

	/**
	 * Setter for civic number
	 * @param number - street number of the house address
	 */
	public void setCivicNumber(int number) {
		civicNumber = number;
	}

	/**
	 * Setter for street name
	 * @param name - street name of the house address
	 */
	public void setStreetName(String name) {
		streetName = name;
	}

	/**
	 * Setter for postal code 
	 * @param pc - postal code of the house
	 */
	public void setPostalCode(String pc) {
		postalCode = pc;
	}

	/**
	 * Setter for current land value
	 * @param value - current value of the house
	 */
	public void setCurrentLandValue(int value) {
		currentLandValue = value;
	}

	/**
	 * Setter for current improvement value
	 * @param value - value of the current improvement
	 */
	public void setCurrentImprovementValue(int value) {
		currentImprovementValue = value;
	}

	/**
	 * Setter for assessment year
	 * @param year - assessment year of current property value
	 */
	public void setAssessmentYear(int year) {
		assessmentYear = year;
	}

	/**
	 * Setter for previous land value
	 * @param value - value of the land from the last assessment
	 */
	public void setPreviousLandValue(int value) {
		previousLandValue = value;
	}

	/**
	 * Setter for previous improvement value
	 * @param value - value of the previous improvement
	 */
	public void setPreviousImporvementValue(int value) {
		previousImprovementValue = value;
	}

	/**
	 * Setter for year built
	 * @param year - year when the house/strata was built
	 */
	public void setYearBuilt(int year) {
		yearBuilt = year;
	}

	/**
	 * Setter for Big Improvement Year
	 * @param year - year when big improvement occured
	 */
	public void setBigImprovementYear(int year) {
		bigImprovementYear = year;
	}
	
	/**
	 * Setter for realtor 
	 * @param newOwner - name of the realtor
	 */
	public void setOwner(String newOwner) {
		owner = newOwner;
	}

	/**
	 * Setter for isSelling
	 * @param sell - boolean value whether the house is for sale or not
	 */
	public void setIsSelling(boolean sell) {
		isSelling = sell;
	}

	/**
	 * Setter for price
	 * @param salePrice - price of the house
	 */
	public void setPrice(int salePrice) {
		price = salePrice;
	}
	
	
	/**
	 * Comparators for table sorting.
	 * Commented out since we do not do local sorting of the table
	 * 
	 * When re-enabling local sorting of the table,
	 * make sure you uncomment the sotr handler in HouseTable.java 
	 */
	/*
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
	*/
}