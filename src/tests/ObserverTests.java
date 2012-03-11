package tests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cpsc310.server.DataCatalogueObserverImpl;
import exceptions.DownloadFailedException;

public class ObserverTests {

	//tests to ensure that the observer works correctly
	
	private DataCatalogueObserverImpl observerService;
	private String ourURL = "http://www.ugrad.cs.ubc.ca/~y0c7/property_tax_report3.csv";
	private String zipURL = "http://www.ugrad.cs.ubc.ca/~y0c7/property_tax_report_csv.zip";
	private String validURL	= "http://www.archive.org/download/2011-04-vancouver-bc-opendata-site/vancouverdata.2011.04.txt";
	private String invalidURL = "http://www.ugrad.cs.ubc.ca/~y0c7/fakefile.csv";;
	private String malformedURL = "this_is_not_a_URL/~y0c7/property_tax_report3.csv";
	private long actualModifiedDate = 1330685942000L; //(as of March 5)
	private long pastModifiedDate = 0;
	
	//setup all the necessary variables
	@Before
	public void setUp()
	{
		observerService = new DataCatalogueObserverImpl();
	}
	
	@Test
	public void testCorrectURL() {
		List<String> textFile = observerService.downloadFile(validURL);
		assertNotNull(textFile);
	}
	
	@Test
	public void testCorrectZipURL() {
		List<String> zipFile = observerService.downloadFile(zipURL);
		assertNotNull(zipFile);
	}
	
	@Test
	public void testIncorrectURL() {
		List<String> csvFile = observerService.downloadFile(invalidURL);
		assertNull(csvFile);
	}

	@Test
	public void testGetLastModifiedGoodURL() throws IOException {
		long modifiedDate = observerService.getServerFileLastModifiedDate(ourURL);
		assertEquals(actualModifiedDate, modifiedDate);
	}
	
	@Test (expected = IOException.class)
	public void testGetLastModifiedBadURL() throws IOException {
		observerService.getServerFileLastModifiedDate(malformedURL);
	}
	
	@Test
	public void testHaveLatestUpdateTrue() throws IOException {
		boolean isUpToDate = observerService.haveLatestUpdate(ourURL, actualModifiedDate);
		assertTrue(isUpToDate);
	}
	
	@Test
	public void testHaveLatestUpdateFalse() throws IOException {
		boolean isUpToDate = observerService.haveLatestUpdate(ourURL, pastModifiedDate);
		assertFalse(isUpToDate);		
	}
	
	@Test (expected = IOException.class)
	public void testHaveLatestUpdateBadURL() throws IOException {
		observerService.haveLatestUpdate(malformedURL, pastModifiedDate);
	}
	
}
