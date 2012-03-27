package cpsc310.server;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVParser;
import java.util.HashMap;
/**
 * Parses CSV files to houseDataPoints
 * @author Justin
 *
 */
public class FileParser {

	/**
	 * constructor
	 */
	public FileParser() {
	}

	/**
	 * Method to parse the lines of the house data csv file and store the
	 * information into an ArrayList<HouseDataPoint> object.
	 * 
	 * @post true;
	 * @param rawFile
	 *            - a List<String> containing the lines of the .csv file.
	 * @return an HashMap<String, HouseDataPoint> containing the information parsed
	 *         from the .csv file.
	 */
	public HashMap<String, HouseDataPoint> parseData(List<String> rawFile) {

		CSVParser parser = new CSVParser();
		HashMap<String, HouseDataPoint> houseIDs = new HashMap<String, HouseDataPoint>();

		// create iterator to iterate through all lines contained in the .csv
		// file
		Iterator<String> itr = rawFile.iterator();
		String currentLine = itr.next();
		String[] header;
		try {
			// grab the values of the title for each column
			header = parser.parseLine(currentLine);
			// stores PIDs to check for duplicates
			while (itr.hasNext()) {
				// store the values of each house into a HashMap to create
				// HouseDataPoint objects
				HashMap<String, String> currentHouse = new HashMap<String, String>();
				String[] currentParsedLine = parser.parseLine(itr.next());
				for (int j = 0; j < currentParsedLine.length; j++) {
					currentHouse.put(header[j], currentParsedLine[j]);
				}
				// check for duplicates
				String currentHouseID = currentHouse.get("TO_CIVIC_NUMBER").replaceAll("\\..+$", "")
						+ " " + currentHouse.get("STREET_NAME");
				if (validateLine(currentHouse)) {
					int currentLandValue = Integer.parseInt(currentHouse
							.get("CURRENT_LAND_VALUE").replaceAll("\\..+$", ""));
					if (houseIDs.containsKey(currentHouseID)) {
						if (houseIDs.get(currentHouseID).getCurrentLandValue() < currentLandValue) {
							houseIDs.put(currentHouseID, new HouseDataPoint(
									currentHouse));
						}
					} else {
						houseIDs.put(currentHouseID, new HouseDataPoint(
								currentHouse));
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return houseIDs;
	}

	/**
	 * @pre a hashmap parsed from file representing a single line
	 * @post a boolean to whether current line in file is valid
	 * @param currentHouse
	 * @return boolean
	 */
	private boolean validateLine(HashMap<String, String> currentHouse) {
		if ((!currentHouse.get("STREET_NAME").equals(""))
				&& (!currentHouse.get("TO_CIVIC_NUMBER").equals(""))
				&& (!currentHouse.get("CURRENT_LAND_VALUE").equals(""))) {
			return true;
		}
		return false;
	}
}
