package tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import cpsc310.server.DataStore;

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
	public void testSearchByStreet() {
		ArrayList<String> tempArray = datastore.searchByStreet("49TH AVE W");
		assertTrue(tempArray.size() > 1);
	}

	// test retrieval of postal code
	@Test
	public void testSearchByPostalCode() {
		ArrayList<String> tempArray = datastore.searchByPostalCode("V6R 2T7");
		assertTrue(tempArray.size() > 1);
	}

	// test for CurrentLandValue retrieval
	@Test
	public void testSearchByCurrentLandValueGeneric() {
		ArrayList<String> tempArray = datastore.searchByCurrentLandValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test for improvementValue retrieval
	@Test
	public void testSearchByImprovementValuesGeneric() {
		ArrayList<String> tempArray = datastore.searchByImprovementValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test for assessmentYears retrieval
	@Test
	public void testSearchByAssessmentYearGeneric() {
		ArrayList<String> tempArray = datastore.searchByAssessmentYear(2012,
				2012);
		assertTrue(tempArray.size() > 1);
	}

	// test for previousLandValues retrieval
	@Test
	public void testSearchByPreviousLandValueGeneric() {
		ArrayList<String> tempArray = datastore.searchByPreviousLandValue(
				100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test for previousImprovementValues retrieval
	@Test
	public void testSearchByPreviousImprovementValueGeneric() {
		ArrayList<String> tempArray = datastore
				.searchByPreviousImprovementValue(100000, 1000000);
		assertTrue(tempArray.size() > 1);
	}

	// test for yearBuilt retrieval
	@Test
	public void testSearchByYearBuiltGeneric() {
		ArrayList<String> tempArray = datastore.searchByYearBuilt(1980, 1995);
		assertTrue(tempArray.size() > 1);
	}

	// test for bigImprovementYear retrieval
	@Test
	public void testSearchbigImprovementYearGeneric() {
		ArrayList<String> tempArray = datastore.searchByBigImprovementYear(
				1980, 1990);
		assertTrue(tempArray.size() > 1);
	}
}
