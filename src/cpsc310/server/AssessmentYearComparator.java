package cpsc310.server;

import java.util.Comparator;
import java.util.HashMap;

/**
 * AssessmentYear Comparator
 * 
 * @author Justin
 * 
 */
public class AssessmentYearComparator implements Comparator {

	private HashMap<String, HouseDataPoint> tempStore;

	/**
	 * Constructor for the Comparator
	 * 
	 * @param houseHash
	 */
	public AssessmentYearComparator(HashMap<String, HouseDataPoint> houseHash) {
		tempStore = houseHash;
	}

	@Override
	public int compare(Object arg0, Object arg1) {
		int year0 = tempStore.get(arg0).getAssessmentYear();
		int year1 = tempStore.get(arg1).getAssessmentYear();

		if (year0 < year1) {
			return -1;
		}
		else if (year0 == year1) {
			return 0;
		}
		return 1;
	}
}
