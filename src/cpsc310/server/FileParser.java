package cpsc310.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import au.com.bytecode.opencsv.CSVParser;
import java.util.HashMap;
import java.util.regex.*;

public class FileParser {

	public FileParser() {
	}

	/**
	 * Method to parse the lines of the house data csv file and store the information
	 * into an ArrayList<HouseDataPoint> object.
	 * @pre rawFile != null;
	 * @post true;
	 * @param rawFile - a List<String> containing the lines of the .csv file.
	 * @return an ArrayList<HouseDataPoint> containing the information parsed from the
	 * 		   .csv file.
	 */
	public ArrayList<HouseDataPoint> parseData(List<String> rawFile) {

		ArrayList<HouseDataPoint> houseOutput = new ArrayList<HouseDataPoint>();
		CSVParser parser = new CSVParser();

		//create iterator to iterate through all lines contained in the .csv file
		Iterator<String> itr = rawFile.iterator();
		String currentLine = itr.next();
		String[] header;
		try {
			//grab the values of the title for each column
			header = parser.parseLine(currentLine);
			//stores PIDs to check for duplicates
			HashSet<String> PIDs = new HashSet<String>();
			while (itr.hasNext()) {
				//store the values of each house into a HashMap to create
				//HouseDataPoint objects
				HashMap<String, String> currentHouse = new HashMap<String, String>();
				String[] currentParsedLine = parser.parseLine(itr.next());
				for (int j = 0; j < currentParsedLine.length; j++) {
					currentHouse.put(header[j], currentParsedLine[j]);
				}
				//check for duplicates
				if((currentHouse.get("PID").matches("^[\\d\\-]+$")) && (!PIDs.contains(currentHouse.get("PID"))))
				{
					PIDs.add(currentHouse.get("PID"));
					houseOutput.add(new HouseDataPoint(currentHouse));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return houseOutput;
	}
}
