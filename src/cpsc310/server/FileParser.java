package cpsc310.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import au.com.bytecode.opencsv.CSVParser;
import java.util.HashMap;

public class FileParser {

	public FileParser() {
	}

	/*
	 * 
	 */
	public ArrayList<HouseDataPoint> parseData(List<String> rawFile) {

		ArrayList<HouseDataPoint> houseOutput = new ArrayList<HouseDataPoint>();
		CSVParser parser = new CSVParser();

		Iterator<String> itr = rawFile.iterator();
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
				houseOutput.add(new HouseDataPoint(currentHouse));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return houseOutput;
	}
}
