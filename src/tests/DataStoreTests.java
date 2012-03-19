package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import cpsc310.server.DataStore;
import cpsc310.server.HouseDataPoint;

public class DataStoreTests {

	// tests to ensure that the DataStore works correctly

	private DataStore datastore;

	@Before
	public void setUp() throws Exception {
		datastore = new DataStore();
	}
	
	// test if initialization of datastore worked
	@Test
	public void testDataStoreStart() {
		ArrayList<String> tempKeys = datastore.getAllKeys();
		assertTrue(tempKeys.size() > 0);
	}

	// test generic search case (where address exactly the same as houseID)
	@Test
	public void testSearchByAddressGeneric() {
		ArrayList<String> tempArray = datastore.searchByAddress("3683 49TH AVE W");
		assertEquals(1, tempArray.size());
	}
}
