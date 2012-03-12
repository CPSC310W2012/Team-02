package cpsc310.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.DataCatalogueObserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;

@SuppressWarnings("serial")
public class DataCatalogueObserverImpl extends RemoteServiceServlet implements DataCatalogueObserver {

	/**
	 * Method to download a file from a server.  Caution: the link provided to the parameter urlLink
	 * must be for the desired file object, otherwise the http request may return other objects on
	 * the website.  For example, the html for a webpage may be returned.
	 * @pre urlLink != null;
	 * @post true;
	 * @param urlLink - the http link to retrieve a file from a server.
	 * @return a List<String> containing the lines of the .csv file; returns null if failed to retrieve file.
	 */
	public List<String> downloadFile(String urlLink) {
		InputStream fileStream = null;
		ZipInputStream zipStream = null;
		int timeOut = 60000; //timeout value in ms (60 seconds)
		List<String> fileLines;
		
		try {
			URL fileLocation = new URL(urlLink);
			//open connection to the file
			URLConnection fileConnection = fileLocation.openConnection();		
			//set the reading timeout before retrieving and reading from the input stream
			fileConnection.setReadTimeout(timeOut);
			//retrieve the input stream and store each line from the file into a list
			fileStream = fileConnection.getInputStream();
			
			String fileName = fileLocation.getFile();
			
			if(fileName.endsWith("zip")) {
				zipStream = new ZipInputStream(fileStream);
				zipStream.getNextEntry();
				fileLines = IOUtils.readLines(zipStream);
			}
			else {
				fileLines = IOUtils.readLines(fileStream);
			}
			
			return 	fileLines;
		} catch (IOException e) {
			return null;
		}
		finally	{
			try	{
				//close the zipstream since we don't need it anymore
				if(zipStream != null) {
					zipStream.close();
				}
				//close the filestream since we don't need it anymore
				if (fileStream != null) {
					fileStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to check if a file on a server has been updated.
	 * @pre (urlLink != null) && (lastModified != null);
	 * @post true;
	 * @param urlLink - the http link to retrieve a file from a server.
	 * @param lastModified - the value returned from a call to .getLastModified() on the
	 * 						 file of interest; i.e. the last modified date of the file
	 * 						 in possession.
	 * @return true is file is up to date; otherwise, false.
	 * @throws IOException - if an error occurred when trying to connect to the file.
	 */
	public boolean haveLatestUpdate(String urlLink, long lastModified) throws IOException   {
		
		URL fileLocation = new URL(urlLink);
		//open connection to the file
		URLConnection fileConnection = fileLocation.openConnection();
		//retrieve file's last modified date
		long serverModified = fileConnection.getLastModified();
			
		return serverModified <= lastModified;
	}
	
	/**
	 * Method to retrieve the last modified date of a file from a server.
	 * @pre urlLink != null;
	 * @post true;
	 * @param urlLink - the http link to retrieve a file from a server.
	 * @return the last modified date of the file from the given URL or 0 if not known. 
	 * @throws IOException - if an error occurred when trying to connect to the file.
	 */
	public long getServerFileLastModifiedDate(String urlLink) throws IOException {
		
		URL fileLocation = new URL(urlLink);
		//open connection to the file
		URLConnection fileConnection = fileLocation.openConnection();
		
		//return file's last modified date
		return fileConnection.getLastModified();
	}
}