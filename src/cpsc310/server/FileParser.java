package cpsc310.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import au.com.bytecode.opencsv.CSVParser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

import cpsc310.client.DataCatalogueObserver;
import cpsc310.client.DataCatalogueObserverAsync;

public class FileParser {

	private String CSV;

	public FileParser() {

		DataCatalogueObserverAsync observerService = GWT
				.create(DataCatalogueObserver.class);

		observerService.downloadFile(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				// @TODO Error message
			}

			public void onSuccess(String result) {
				CSV = result;
			}
		});
	}

	public ArrayList<HashMap<String, String>> getHouseList() {
		ArrayList<HashMap<String, String>> houseList = new ArrayList<HashMap<String, String>>();
		CSVParser parser = new CSVParser();

		String[] lines = CSV.split(System.getProperty("line.separator"));
		try {
			String[] header = parser.parseLine(lines[0]);
			for (int i = 1; i < lines.length; i++) {
				HashMap<String, String> currentHouse = new HashMap<String, String>();
				String[] currentString = parser.parseLine(lines[i]);
				for (int j = 0; j < currentString.length; j++) {
					currentHouse.put(header[j], currentString[j]);
				}
				houseList.add(currentHouse);
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return houseList;
	}

}
