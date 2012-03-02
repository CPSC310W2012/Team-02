package cpsc310.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.DataCatalogueObserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

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
		int timeOut = 60000; //timeout value in ms (60 seconds)
		List<String> fileLines;
		
		try {
			URL fileLocation = new URL(urlLink);
			//open connection to the file
			URLConnection fileConnection = fileLocation.openConnection();
			//TO DO: Implement checking to see if latest version of file is already possessed.		
			//set the reading timeout before retrieving and reading from the input stream
			fileConnection.setReadTimeout(timeOut);
			//retrieve the input stream and store each line from the file into a list
			fileStream = fileConnection.getInputStream();
			fileLines = IOUtils.readLines(fileStream);
			
			return 	fileLines;
		}
		catch (Exception e) {
			return null;
		}
		finally	{
			try	{
				//close the filestream since we don't need it anymore
				if (fileStream != null) {
					fileStream.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}