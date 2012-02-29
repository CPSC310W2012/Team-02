package cpsc310.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.DataCatalogueObserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;

@SuppressWarnings("serial")
public class DataCatalogueObserverImpl extends RemoteServiceServlet implements DataCatalogueObserver
{
	/**
	 * Method to download the .csv file from our server.
	 * @return a List<String> containing the lines of the .csv file; returns null if failed to retrieve file.
	 */
	public List<String> downloadFile()
	{
		InputStream fileStream = null;
		int timeOut = 60000; //timeout value in ms
		List<String> fileLines;
		
		try
		{
			URL fileLocation = new URL("http://www.ugrad.cs.ubc.ca/~y0c7/property_tax_report2.csv");
			//open connection to the file
			URLConnection fileConnection = fileLocation.openConnection();
			//set the reading timeout before retrieving and reading from the input stream
			fileConnection.setReadTimeout(timeOut);
			//retrieve the input stream and store each line from the file into a list
			fileStream = fileConnection.getInputStream();
			fileLines = IOUtils.readLines(fileStream);
			
			return 	fileLines;
		}
		catch (Exception e)
		{
			return null;
		}
		finally
		{
			try
			{
				//close the filestream since we don't need it anymore
				if (fileStream != null)
				{
					fileStream.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}