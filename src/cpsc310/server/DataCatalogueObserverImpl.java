package cpsc310.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import cpsc310.client.DataCatalogueObserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("serial")
public class DataCatalogueObserverImpl extends RemoteServiceServlet implements DataCatalogueObserver {

	public String downloadFile() {

		InputStream fileStream = null;
		
		try {
			URL fileLocation = new URL("http://www.archive.org/download/2011-04-vancouver-bc-opendata-site/vancouverdata.2011.04.txt");
			URLConnection fileConnection = fileLocation.openConnection();
			fileStream = fileConnection.getInputStream();
			
			byte[] buffer = new byte[1024];
			ByteArrayOutputStream byteVersion = new ByteArrayOutputStream();		
			while(fileStream.read(buffer) != -1)
			{
				byteVersion.write(buffer);
				buffer = new byte[1024];
			}
			String fileName = fileLocation.getFile();
			String message;
			if(fileName.endsWith("zip"))
				message = "ZIP";
			else if(fileName.endsWith("csv"))
				message = "CSV";
			else
				message = "what is it?";
			String.valueOf(fileConnection.getLastModified());
			//fileLocation.getFile();
			//File rawFile = new File("temporary.zip");
			
			return 	byteVersion.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();//"I GOT THIS2";
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