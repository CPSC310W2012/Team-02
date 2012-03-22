package tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Test;
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
		ArrayList<String> tempKeys = datastore.getAllKeys();
		assertTrue(tempKeys.size() > 0);
	}

	// test if initialization of streetNames worked
	@Test
	public void testDataStreetStore() {
		ArrayList<String> tempKeys = datastore.getStreets();
		assertTrue(tempKeys.size() > 0);
	}

	// test generic search case (where address exactly the same as houseID)
	@Test
	public void testSearchByStreetGeneric() {
		ArrayList<String> tempArray = datastore.searchByStreet("49TH AVE W");
		assertTrue(tempArray.size() > 0);
	}

	// test search by street for a street name that should not exist
	@Test
	public void testSearchByStreetEmpty() {
		ArrayList<String> tempArray = datastore
				.searchByStreet("NOT A REAL STREET");
		assertTrue(tempArray.size() == 0);
	}

	// test search to check if streets are equal
	@Test
	public void testSearchByStreetCheckEquals() {
		ArrayList<String> tempArray = datastore.searchByStreet("49TH AVE W");
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getStreetName();
			assertTrue(currentValue.equals("49TH AVE W"));
		}
	}

	// test retrieval of postal code
	@Test
	public void testSearchByPostalCodeGeneric() {
		ArrayList<String> tempArray = datastore.searchByPostalCode("V6R 2T7");
		assertTrue(tempArray.size() > 0);
	}

	// test search by postal code that should not exist
	@Test
	public void testSearchByPostalCodeEmpty() {
		ArrayList<String> tempArray = datastore.searchByPostalCode("V6R 2T71");
		assertTrue(tempArray.size() == 0);
	}

	// test search to check if postal codes are equal
	@Test
	public void testSearchByPostalCodeEquals() {
		ArrayList<String> tempArray = datastore.searchByPostalCode("V6R 2T7");
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
		ArrayList<String> tempArray = datastore.searchByCurrentLandValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 0);
	}

	// test search CurrentLandValue that should not exist
	@Test
	public void testSearchByCurrentLandValueEmpty() {
		ArrayList<String> tempArray = datastore.searchByCurrentLandValue(
				999999999, 999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search CurrentLandValue is exactly specified
	@Test
	public void testSearchByCurrentLandValueExact() {
		ArrayList<String> tempArray = datastore.searchByCurrentLandValue(
				371000, 371000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByCurrentLandValueWithinRange() {
		ArrayList<String> tempArray = datastore.searchByCurrentLandValue(
				100000, 1000000);
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
		ArrayList<String> tempArray = datastore
				.searchByCurrentImprovementValue(100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test search improvementValue that should not exist
	@Test
	public void testSearchByCurrentImprovementValuesEmpty() {
		ArrayList<String> tempArray = datastore
				.searchByCurrentImprovementValue(999999999, 999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search improvementValue is exactly specified
	@Test
	public void testSearchByCurrentImprovementValuesExact() {
		ArrayList<String> tempArray = datastore
				.searchByCurrentImprovementValue(299000, 299000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByCurrentImprovementValuesWithinRange() {
		ArrayList<String> tempArray = datastore
				.searchByCurrentImprovementValue(100000, 1000000);
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
		ArrayList<String> tempArray = datastore.searchByAssessmentYear(2010,
				2012);
		assertTrue(tempArray.size() > 1);
	}

	// test search improvementValue that should not exist
	@Test
	public void testSearchByAssessmentYearEmpty() {
		ArrayList<String> tempArray = datastore.searchByAssessmentYear(2050,
				2070);
		assertTrue(tempArray.size() == 0);
	}

	// test search improvementValue is exactly specified
	@Test
	public void testSearchByAssessmentYearExact() {
		ArrayList<String> tempArray = datastore.searchByAssessmentYear(2012,
				2012);
		assertTrue(tempArray.size() > 1);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByAssessmentYearWithinRange() {
		ArrayList<String> tempArray = datastore.searchByAssessmentYear(2010,
				2012);
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
		ArrayList<String> tempArray = datastore.searchByPreviousLandValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test search PreviousLandValue that should not exist
	@Test
	public void testSearchByPreviousLandValueEmpty() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousLandValue(999999999, 999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search PreviousLandValue is exactly specified
	@Test
	public void testSearchByPreviousLandValueExact() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousLandValue(221000, 221000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByPreviousLandValueWithinRange() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousLandValue(100000, 1000000);
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
		ArrayList<String> tempArray = datastore
				.searchByPreviousImprovementValue(100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test search PreviousImprovementValue that should not exist
	@Test
	public void testSearchByPreviousImprovementValueEmpty() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousImprovementValue(999999999, 999999999);
		assertTrue(tempArray.size() == 0);
	}

	// test search improvementValue is exactly specified
	@Test
	public void testSearchByPreviousImprovementValuesExact() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousImprovementValue(288000, 288000);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByPreviousImprovementValuesWithinRange() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousImprovementValue(100000, 1000000);
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
		ArrayList<String> tempArray = datastore.searchByYearBuilt(1980, 1995);
		assertTrue(tempArray.size() > 1);
	}

	// test search YearBuilt that should not exist
	@Test
	public void testSearchByYearBuiltEmpty() {
		ArrayList<String> tempArray = datastore
				.searchByYearBuilt(2050, 2070);
		assertTrue(tempArray.size() == 0);
	}

	// test search YearBuilt is exactly specified
	@Test
	public void testSearchByYearBuiltExact() {
		ArrayList<String> tempArray = datastore
				.searchByYearBuilt(1980, 1980);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByYearBuiltWithinRange() {
		ArrayList<String> tempArray = datastore
				.searchByYearBuilt(1980, 1995);
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
		ArrayList<String> tempArray = datastore.searchByBigImprovementYear(
				1980, 1990);
		assertTrue(tempArray.size() > 1);
	}

	// test search BigImprovementYear that should not exist
	@Test
	public void testSearchByBigImprovementYearsEmpty() {
		ArrayList<String> tempArray = datastore
				.searchByBigImprovementYear(2050, 2060);
		assertTrue(tempArray.size() == 0);
	}

	// test search BigImprovementYear is exactly specified
	@Test
	public void testSearchByBigImprovementYearExact() {
		ArrayList<String> tempArray = datastore
				.searchByBigImprovementYear(1980, 1980);
		assertTrue(tempArray.size() > 0);
	}

	// test search to check if houses are within range set
	@Test
	public void testSearchByBigImprovementYearWithinRange() {
		ArrayList<String> tempArray = datastore
				.searchByBigImprovementYear(1980, 1990);
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
	// Test Street Sort
	@Test
	public void testSortStreet() {
		ArrayList<String> tempArray = datastore.getAllKeys();
		tempArray = datastore.sortByStreetNames(tempArray);
		List<HouseDataPoint> houses = datastore.getHouses(tempArray, 0,
				tempArray.size());
		Iterator<HouseDataPoint> tempItr = houses.iterator();
		String prevVal = tempItr.next().getStreetName();
		while (tempItr.hasNext()) {
			String currentValue = tempItr.next().getStreetName();
			assertTrue(currentValue.compareTo(prevVal) >= 0);
			prevVal = currentValue;
		}
	}
}
