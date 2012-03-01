package tests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc310.server.DataCatalogueObserverImpl;

public class ObserverTests {

	private DataCatalogueObserverImpl observerService;
	private String validURL	= "http://www.archive.org/download/2011-04-vancouver-bc-opendata-site/vancouverdata.2011.04.txt";
	private String invalidURL = "http://www.ugrad.cs.ubc.ca/~y0c7/fakefile.csv";;
	
	//setup all the necessary variables
	@Before
	public void setUp()
	{
		observerService = new DataCatalogueObserverImpl();
	}
	
	@Test
	public void testCorrectURL() {
		List<String> csvFile = observerService.downloadFile(validURL);
		assertTrue(csvFile != null);
	}
	
	@Test
	public void testIncorrectURL() {
		List<String> csvFile = observerService.downloadFile(invalidURL);
		assertTrue(csvFile == null);
	}

}
