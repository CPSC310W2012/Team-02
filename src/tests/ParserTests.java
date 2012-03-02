package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc310.server.FileParser;
import cpsc310.server.HouseDataPoint;

public class ParserTests {

	private FileParser fileParser;
	private ArrayList<HouseDataPoint> houses;
	
	@Before
	public void setUp() throws Exception {
		fileParser = new FileParser();
	}

	@Test
	public void testNullEntry() {
		houses = fileParser.parseData(null);
		assert(houses != null);
	}

}
