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
public class DataCatalogueObserverImpl extends RemoteServiceServlet implements DataCatalogueObserver {

	public List<String> downloadFile() {

		InputStream fileStream = null;
		
		try {
			/*
			SchemeRegistry schemeRegistry = new SchemeRegistry();
			schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			BasicHttpParams params = new BasicHttpParams();
			SingleClientConnManager connmgr = new SingleClientConnManager(params, schemeRegistry);
			
			HttpClient httpClient = new DefaultHttpClient(connmgr, params);*/
/*			HttpGet getFile = new HttpGet("http://199.91.152.42/k7npp5ru9e7g/x5i2y6vwizcfup9/property_tax_report.csv");
			HttpResponse downloadResponse = httpClient.execute(getFile);
			fileStream = downloadResponse.getEntity().getContent();*/
			
			//URL fileLocation = new URL("http://199.91.152.42/k7npp5ru9e7g/x5i2y6vwizcfup9/property_tax_report.csv");
			//URL fileLocation = new URL("http://199.91.152.42/ocmj38xcy7hg/x5i2y6vwizcfup9/property_tax_report.csv");
			//URL fileLocation = new URL("http://205.196.121.157/du5ylgd2eyjg/5t07wlt63a0os7f/property_tax_report.txt");
			URL fileLocation = new URL("http://www.ugrad.cs.ubc.ca/~y0c7/property_tax_report2.csv");
			URLConnection fileConnection = fileLocation.openConnection();
			fileConnection.setReadTimeout(30000);
			fileStream = fileConnection.getInputStream();
			List<String> fileLines = IOUtils.readLines(fileStream);

			String fileName = "asdf";//fileLocation.getFile();
			
			String message;
			if(fileName.endsWith("zip"))
				message = "ZIP";
			else if(fileName.endsWith("csv"))
				message = "CSV";
			else
				message = "what is it?";
			//String.valueOf(fileConnection.getLastModified());
			//fileLocation.getFile();
			//File rawFile = new File("temporary.zip");
			//System.err.println(byteVersion.toString());
			return 	fileLines;
		} catch (IOException e) {
			e.printStackTrace();
			return null;//"I GOT THIS2";
		} finally {
			try {
				if (fileStream != null) {
					fileStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
}