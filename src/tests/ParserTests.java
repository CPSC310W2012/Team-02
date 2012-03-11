package tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import cpsc310.server.FileParser;
import cpsc310.server.HouseDataPoint;

public class ParserTests {

	//tests to ensure that the file parser works correctly
	
	private FileParser fileParser;
	private ArrayList<HouseDataPoint> houses;
	private List<String> testList;
	private HouseDataPoint house;
	
	@Before
	public void setUp() throws Exception {
		fileParser = new FileParser();
		testList = new ArrayList<String>();
	}

	@Test (expected = NullPointerException.class)
	public void testNullEntry() {
		fileParser.parseData(null);
	}

	@Test (expected = NoSuchElementException.class)
	public void testEmptyList() {
		houses = fileParser.parseData(new ArrayList<String>());
	}
	
	@Test //should be size of 0 since first line is used for titles of columns
	public void testOneEntryList() {
		testList.add("PID,Address,Price");
		houses = fileParser.parseData(testList);
		assertEquals(0, houses.size());
	}
	
	@Test //should be size of 1 since first line is used for titles of columns
	public void testTwoEntryList() {
		testList.add("PID,LAND_COORDINATE,TO_CIVIC_NUMBER,STREET_NAME,PROPERTY_POSTAL_CODE,CURRENT_LAND_VALUE");
		testList.add("1234,5679,4545,Fake Street,A1A 1A1,");
		houses = fileParser.parseData(testList);
		assertEquals(1, houses.size());
		house = houses.get(0);
		assertEquals("1234", house.getPID());
		assertEquals(5679, house.getCoordinate());
		assertEquals("4545 Fake Street", house.getAddress());
		assertEquals("A1A 1A1", house.getPostalCode());
		assertEquals(0, house.getLandValue(), 0); //should be 0 since value is blank
	}
	
	@Test //Should only have 1 instance of the Same PID
	public void testDuplicatePID() {
		testList.add("PID,LAND_COORDINATE,TO_CIVIC_NUMBER,STREET_NAME,PROPERTY_POSTAL_CODE,CURRENT_LAND_VALUE");
		testList.add("1234,5679,4545,Fake Street,A1A 1A1,");
		testList.add("1235,5679,4545,Fake Street,A1A 1A1,");
		testList.add("1234,5679,4545,Fake Street,A1A 1A1,");
		houses = fileParser.parseData(testList);
		assertEquals(2, houses.size());
	}
	
	@Test
	public void testEmptyID() {
		testList.add("PID,LAND_COORDINATE,TO_CIVIC_NUMBER,STREET_NAME,PROPERTY_POSTAL_CODE,CURRENT_LAND_VALUE");
		testList.add(",5679,4545,Fake Street,A1A 1A1,");
		houses = fileParser.parseData(testList);
		assertEquals(0, houses.size());
	}
}
