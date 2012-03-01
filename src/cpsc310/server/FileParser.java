package cpsc310.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import au.com.bytecode.opencsv.CSVParser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import cpsc310.client.DataCatalogueObserver;
import cpsc310.client.DataCatalogueObserverAsync;

public class FileParser {

	private List<String> CSV;

	public FileParser() {

		DataCatalogueObserverAsync observerService = GWT
				.create(DataCatalogueObserver.class);
		observerService.downloadFile(
				"http://www.ugrad.cs.ubc.ca/~y0c7/property_tax_report2.csv",
				new AsyncCallback<List<String>>() {
					public void onFailure(Throwable caught) {
						// @TODO Error message
					}

					public void onSuccess(List<String> result) {
						CSV = result;
					}
				});
	}

	/*
	 * 
	 */
	public ArrayList<HashMap<String, String>> getHouseList() {
		ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
		CSVParser parser = new CSVParser();

		Iterator<String> itr = CSV.iterator();
		String currentLine = itr.next();
		String[] header;
		try {
			header = parser.parseLine(currentLine);
			while (itr.hasNext()) {
				HashMap<String, String> currentHouse = new HashMap<String, String>();
				String[] currentParsedLine = parser.parseLine(itr.next());
				for (int j = 0; j < currentParsedLine.length; j++) {
					currentHouse.put(header[j], currentParsedLine[j]);
				}
				houseList.add(currentHouse);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return houseList;
	}

}
