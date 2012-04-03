package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import cpsc310.server.DataStore;
import cpsc310.server.HouseDataServiceImpl;

public class HouseDataServiceTests {

	// tests to ensure that the DataStore works correctly

	public static HouseDataServiceImpl service;

	// Instantiate
	@BeforeClass
	public static void setUp() throws Exception {
		service = new HouseDataServiceImpl();
	}

	// test performance of Search, worst case
	@Test
	public void testSearchPerformWorst() {
		// [0]"civicNumber"
		// [1]"StreetName",
		// Value "Current Land Value" - [2]min, [3]max
		// Value "Price" - [4]min, [5]max
		// [6]"Realtor"
		// [7]"Postal Code"
		// Value "Current Improvement Value" - [8]min, [9]max
		// Year "Assessment Year" - [10]min, [11]max
		// Value "Previous Land Value" - [12]min, [13]max
		// Value "Previous Improvement Value" - [14]min, [15]max
		// Year "Year Built" - [16]min, [17]max
		// Year "Big Improvement Year" - [18]min, [19]max
		String[] userSearchInput = { "", "", "0", "999999999", "0",
				"999999999", "", "", "0", "999999999", "0", "999999999", "0",
				"999999999", "0", "999999999", "0", "999999999", "0",
				"999999999" };
		service.searchHouses(0, userSearchInput, -1);
		assertTrue(service.getHouseDatabaseLength(0) > 0);
	}

	// test performance of Search, best case
	@Test
	public void testSearchPerformBest() {
		// [0]"civicNumber"
		// [1]"StreetName",
		// Value "Current Land Value" - [2]min, [3]max
		// Value "Price" - [4]min, [5]max
		// [6]"Realtor"
		// [7]"Postal Code"
		// Value "Current Improvement Value" - [8]min, [9]max
		// Year "Assessment Year" - [10]min, [11]max
		// Value "Previous Land Value" - [12]min, [13]max
		// Value "Previous Improvement Value" - [14]min, [15]max
		// Year "Year Built" - [16]min, [17]max
		// Year "Big Improvement Year" - [18]min, [19]max
		String[] userSearchInput = { "4203", "13TH AVE W", "", "", "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"" };
		service.searchHouses(0, userSearchInput, -1);
		assertTrue(service.getHouseDatabaseLength(0) > 0);
	}

	// test to resolve issue with owners
	@Test
	public void testSearchOwners() {
		// [0]"civicNumber"
		// [1]"StreetName",
		// Value "Current Land Value" - [2]min, [3]max
		// Value "Price" - [4]min, [5]max
		// [6]"Realtor"
		// [7]"Postal Code"
		// Value "Current Improvement Value" - [8]min, [9]max
		// Year "Assessment Year" - [10]min, [11]max
		// Value "Previous Land Value" - [12]min, [13]max
		// Value "Previous Improvement Value" - [14]min, [15]max
		// Year "Year Built" - [16]min, [17]max
		// Year "Big Improvement Year" - [18]min, [19]max
		String[] userSearchInput = { "", "", "", "", "", "", "The", "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "" };
		service.searchHouses(0, userSearchInput, -1);
		assertTrue(service.getHouseDatabaseLength(0) == 0);
	}

	// test multiple searches performed in succession
	@Test
	public void testSearchMultiple() {
		// [0]"civicNumber"
		// [1]"StreetName",
		// Value "Current Land Value" - [2]min, [3]max
		// Value "Price" - [4]min, [5]max
		// [6]"Realtor"
		// [7]"Postal Code"
		// Value "Current Improvement Value" - [8]min, [9]max
		// Year "Assessment Year" - [10]min, [11]max
		// Value "Previous Land Value" - [12]min, [13]max
		// Value "Previous Improvement Value" - [14]min, [15]max
		// Year "Year Built" - [16]min, [17]max
		// Year "Big Improvement Year" - [18]min, [19]max
		String[] userSearchInput = { "", "", "", "", "", "", "The", "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "" };
		service.searchHouses(0, userSearchInput, -1);
		assertTrue(service.getHouseDatabaseLength(0) == 0);

		String[] userSearchInput1 = { "4203", "13TH AVE W", "", "", "", "", "",
				"", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
				"" };
		service.searchHouses(0, userSearchInput1, -1);
		assertTrue(service.getHouseDatabaseLength(0) > 0);

		String[] userSearchInput2 = { "", "", "", "", "", "", "", "", "10",
				"1000", "", "", "", "", "", "", "", "", "", "", "", "", "", "" };
		service.searchHouses(0, userSearchInput2, -1);
		assertTrue(service.getHouseDatabaseLength(0) > 1);
	}

}
