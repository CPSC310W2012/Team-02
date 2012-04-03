package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polygon;

import cpsc310.server.DataStore;
import cpsc310.server.HouseDataPoint;

public class DataStoreTests {

	// tests to ensure that the DataStore works correctly

	public static DataStore datastore;

	// Instantiate store
	@BeforeClass
	public static void setUp() throws Exception {
		datastore = new DataStore();
	}

	// test if initialization of datastore worked
	@Test
	public void testDataStoreStart() {
		List<String> tempKeys = datastore.getAllKeys();
		assertTrue(tempKeys.size() > 0);
	}

	// test if initialization of streetNames worked
	@Test
	public void testDataStreetStore() {
		List<String> tempKeys = datastore.getStreets();
		assertTrue(tempKeys.size() > 0);
	}

	// test generic search case
	@Test
	public void testSearchByStreetGeneric() {
		Set<String> tempArray = datastore.searchByStreet("49TH AVE W");
		assertTrue(tempArray.size() > 0);
	}

	// test search by street for a street name that should not exist
	@Test
	public void testSearchByStreetEmpty() {
		Set<String> tempArray = datastore.searchByStreet("NOT A REAL STREET");
		assertTrue(tempArray.size() == 0);
	}

	// test search to check if streets are equal
	@Test
	public void testSearchByStreetCheckEquals() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByStreet("49TH AVE W"));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getStreetName();
			assertTrue(currentValue.equals("49TH AVE W"));
		}
	}

	// test generic search case
	@Test
	public void testSearchByCivicNumberGeneric() {
		Set<String> tempArray = datastore.searchByCivicNumber(4203);
		assertTrue(tempArray.size() > 0);
	}

	// test search by street for a CivicNumber that should not exist
	@Test
	public void testSearchByCivicNumber() {
		Set<String> tempArray = datastore.searchByCivicNumber(999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search to check if CivicNumbers are equal
	@Test
	public void testSearchByCivicNumberEquals() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByCivicNumber(4203));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCivicNumber();
			assertTrue(currentValue == 4203);
		}
	}

	// test retrieval of postal code
	@Test
	public void testSearchByPostalCodeGeneric() {
		Set<String> tempArray = datastore.searchByPostalCode("V6R 2T7");
		assertTrue(tempArray.size() > 0);
	}

	// test search by postal code that should not exist
	@Test
	public void testSearchByPostalCodeEmpty() {
		Set<String> tempArray = datastore.searchByPostalCode("V6R 2T71");
		assertTrue(tempArray.size() == 0);
	}

	// test search to check if postal codes are equal
	@Test
	public void testSearchByPostalCodeEquals() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByPostalCode("V6R 2T7"));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getPostalCode();
			assertTrue(currentValue.equals("V6R 2T7"));
		}
	}

	// test for CurrentLandValue retrieval
	@Test
	public void testSearchByCurrentLandValueGeneric() {
		Set<String> tempArray = datastore.searchByCurrentLandValue(100000,
				1000000);
		assertTrue(tempArray.size() > 0);
	}

	// test search CurrentLandValue that should not exist
	@Test
	public void testSearchByCurrentLandValueEmpty() {
		Set<String> tempArray = datastore.searchByCurrentLandValue(999999999,
				999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search CurrentLandValue is exactly specified
	@Test
	public void testSearchByCurrentLandValueExact() {
		Set<String> tempArray = datastore.searchByCurrentLandValue(371000,
				371000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByCurrentLandValueWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByCurrentLandValue(100000, 1000000));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCurrentLandValue();
			assertTrue(currentValue >= 100000);
			assertTrue(currentValue <= 1000000);
		}
	}

	// test for improvementValue retrieval
	@Test
	public void testSearchByCurrentImprovementValuesGeneric() {
		Set<String> tempArray = datastore.searchByCurrentImprovementValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test search improvementValue that should not exist
	@Test
	public void testSearchByCurrentImprovementValuesEmpty() {
		Set<String> tempArray = datastore.searchByCurrentImprovementValue(
				999999999, 999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search improvementValue is exactly specified
	@Test
	public void testSearchByCurrentImprovementValuesExact() {
		Set<String> tempArray = datastore.searchByCurrentImprovementValue(
				299000, 299000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByCurrentImprovementValuesWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByCurrentImprovementValue(100000,
				1000000));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCurrentImprovementValue();
			assertTrue(currentValue >= 100000);
			assertTrue(currentValue <= 1000000);
		}
	}

	// test for assessmentYears retrieval
	@Test
	public void testSearchByAssessmentYearGeneric() {
		Set<String> tempArray = datastore.searchByAssessmentYear(2010, 2012);
		assertTrue(tempArray.size() > 1);
	}

	// test search improvementValue that should not exist
	@Test
	public void testSearchByAssessmentYearEmpty() {
		Set<String> tempArray = datastore.searchByAssessmentYear(2050, 2070);
		assertTrue(tempArray.size() == 0);
	}

	// test search improvementValue is exactly specified
	@Test
	public void testSearchByAssessmentYearExact() {
		Set<String> tempArray = datastore.searchByAssessmentYear(2012, 2012);
		assertTrue(tempArray.size() > 1);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByAssessmentYearWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByAssessmentYear(2010, 2012));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getAssessmentYear();
			assertTrue(currentValue >= 2010);
			assertTrue(currentValue <= 2012);
		}
	}

	// test for previousLandValues retrieval
	@Test
	public void testSearchByPreviousLandValueGeneric() {
		Set<String> tempArray = datastore.searchByPreviousLandValue(100000,
				1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test search PreviousLandValue that should not exist
	@Test
	public void testSearchByPreviousLandValueEmpty() {
		Set<String> tempArray = datastore.searchByPreviousLandValue(999999999,
				999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search PreviousLandValue is exactly specified
	@Test
	public void testSearchByPreviousLandValueExact() {
		Set<String> tempArray = datastore.searchByPreviousLandValue(221000,
				221000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByPreviousLandValueWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByPreviousLandValue(100000, 1000000));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getPreviousLandValue();
			assertTrue(currentValue >= 100000);
			assertTrue(currentValue <= 1000000);
		}
	}

	// test for previousImprovementValues retrieval
	@Test
	public void testSearchByPreviousImprovementValueGeneric() {
		Set<String> tempArray = datastore.searchByPreviousImprovementValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test search PreviousImprovementValue that should not exist
	@Test
	public void testSearchByPreviousImprovementValueEmpty() {
		Set<String> tempArray = datastore.searchByPreviousImprovementValue(
				999999999, 999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search improvementValue is exactly specified
	@Test
	public void testSearchByPreviousImprovementValuesExact() {
		Set<String> tempArray = datastore.searchByPreviousImprovementValue(
				288000, 288000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByPreviousImprovementValuesWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByPreviousImprovementValue(100000,
				1000000));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getPreviousImprovementValue();
			assertTrue(currentValue >= 100000);
			assertTrue(currentValue <= 1000000);
		}
	}

	// test for yearBuilt retrieval
	@Test
	public void testSearchByYearBuiltGeneric() {
		Set<String> tempArray = datastore.searchByYearBuilt(1980, 1995);
		assertTrue(tempArray.size() > 1);
	}

	// test search YearBuilt that should not exist
	@Test
	public void testSearchByYearBuiltEmpty() {
		Set<String> tempArray = datastore.searchByYearBuilt(2050, 2070);
		assertTrue(tempArray.size() == 0);
	}

	// test search YearBuilt is exactly specified
	@Test
	public void testSearchByYearBuiltExact() {
		Set<String> tempArray = datastore.searchByYearBuilt(1980, 1980);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByYearBuiltWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByYearBuilt(1980, 1995));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getYearBuilt();
			assertTrue(currentValue >= 1980);
			assertTrue(currentValue <= 1995);
		}
	}

	// test for bigImprovementYear retrieval
	@Test
	public void testSearchByBigImprovementYearGeneric() {
		Set<String> tempArray = datastore
				.searchByBigImprovementYear(1980, 1990);
		assertTrue(tempArray.size() > 1);
	}

	// test search BigImprovementYear that should not exist
	@Test
	public void testSearchByBigImprovementYearsEmpty() {
		Set<String> tempArray = datastore
				.searchByBigImprovementYear(2050, 2060);
		assertTrue(tempArray.size() == 0);
	}

	// test search BigImprovementYear is exactly specified
	@Test
	public void testSearchByBigImprovementYearExact() {
		Set<String> tempArray = datastore
				.searchByBigImprovementYear(1980, 1980);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByBigImprovementYearWithinRange() {
		ArrayList<String> tempArray = new ArrayList<String>();
		tempArray.addAll(datastore.searchByBigImprovementYear(1980, 1990));
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getBigImprovementYear();
			assertTrue(currentValue >= 1980);
			assertTrue(currentValue <= 1990);
		}
	}

	// TODO updating houses test function

	// Sorting function tests
	// Test address Sort
	@Test
	public void testSortByAddress() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByHouseID(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getHouseID();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getHouseID();
			assertTrue(currentValue.compareTo(prevVal) >= 0);
			prevVal = currentValue;
		}
	}

	// Test postal code Sort
	@Test
	public void testSortByPostalCode() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByPostalCode(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getPostalCode();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getPostalCode();
			assertTrue(currentValue.compareTo(prevVal) >= 0);
			prevVal = currentValue;
		}
	}

	// Test owners Sort
	@Test
	public void testSortByOwner() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByOwner(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getOwner();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getOwner();
			assertTrue(currentValue.compareTo(prevVal) >= 0);
			prevVal = currentValue;
		}
	}

	// Test by sale sort
	@Test
	public void testSortByForSale() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByForSale(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		boolean prevVal = tempItr.next().getIsSelling();
		while (tempItr.hasNext()) {
			boolean currentValue = tempItr.next().getIsSelling();
			assertTrue(prevVal || (prevVal == currentValue));
			prevVal = currentValue;
		}
	}

	// Test by currentLandValue sort
	@Test
	public void testSortByCurrentLandValue() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByCurrentLandValue(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getCurrentLandValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCurrentLandValue();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by currentImprovementValue sort
	@Test
	public void testSortByCurrentImprovementValue() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByCurrentImprovementValue(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getCurrentImprovementValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCurrentImprovementValue();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by assessmentYear sort
	@Test
	public void testSortByAssessmentYear() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByAssessmentYear(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getAssessmentYear();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getAssessmentYear();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by previousLandValue sort
	@Test
	public void testSortByPreviousLandValue() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByPreviousLandValue(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getPreviousLandValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getPreviousLandValue();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by previousImprovementValue sort
	@Test
	public void testSortByPreviousImprovementValue() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByPreviousImprovementValue(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getPreviousImprovementValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getPreviousImprovementValue();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by YearBuilt sort
	@Test
	public void testSortByYearBuilt() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByYearBuilt(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getYearBuilt();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getYearBuilt();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by BigImprovementYear sort
	@Test
	public void testSortByBigImprovementYear() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByBigImprovementYear(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getBigImprovementYear();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getBigImprovementYear();
			assertTrue(prevVal <= currentValue);
			prevVal = currentValue;
		}
	}

	// Sorting function tests Descending
	// Test address Sort Descending
	@Test
	public void testSortByAddressDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByHouseIDDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getHouseID();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getHouseID();
			assertTrue(currentValue.compareTo(prevVal) <= 0);
			prevVal = currentValue;
		}
	}

	// Test postal code Sort Descending
	@Test
	public void testSortByPostalCodeDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByPostalCodeDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getPostalCode();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getPostalCode();
			assertTrue(currentValue.compareTo(prevVal) <= 0);
			prevVal = currentValue;
		}
	}

	// Test owners Sort Descending
	@Test
	public void testSortByOwnerDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByOwnerDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getOwner();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getOwner();
			assertTrue(currentValue.compareTo(prevVal) <= 0);
			prevVal = currentValue;
		}
	}

	// Test by sale sort Descending
	@Test
	public void testSortByForSaleDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByForSaleDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		boolean prevVal = tempItr.next().getIsSelling();
		while (tempItr.hasNext()) {
			boolean currentValue = tempItr.next().getIsSelling();
			assertTrue(currentValue || (prevVal == currentValue));
			prevVal = currentValue;
		}
	}

	// Test by currentLandValue sort Descending
	@Test
	public void testSortByCurrentLandValueDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByCurrentLandValueDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getCurrentLandValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCurrentLandValue();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by currentImprovementValue sort Descending
	@Test
	public void testSortByCurrentImprovementValueDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByCurrentImprovementValueDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getCurrentImprovementValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getCurrentImprovementValue();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by assessmentYear sort Descending
	@Test
	public void testSortByAssessmentYearDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByAssessmentYearDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getAssessmentYear();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getAssessmentYear();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by previousLandValue sort Descending
	@Test
	public void testSortByPreviousLandValueDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByPreviousLandValueDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getPreviousLandValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getPreviousLandValue();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by previousImprovementValue sort Descending
	@Test
	public void testSortByPreviousImprovementValueDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByPreviousImprovementValueDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getPreviousImprovementValue();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getPreviousImprovementValue();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by YearBuilt sort Descending
	@Test
	public void testSortByYearBuiltDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByYearBuiltDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getYearBuilt();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getYearBuilt();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}

	// Test by BigImprovementYear sort Descending
	@Test
	public void testSortByBigImprovementYearDes() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByBigImprovementYearDes(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		int prevVal = tempItr.next().getBigImprovementYear();
		while (tempItr.hasNext()) {
			int currentValue = tempItr.next().getBigImprovementYear();
			assertTrue(prevVal >= currentValue);
			prevVal = currentValue;
		}
	}
}
