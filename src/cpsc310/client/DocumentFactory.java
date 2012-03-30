package cpsc310.client;

import com.google.gwt.user.client.ui.HTML;

public class DocumentFactory {

	/**
	 * Constructor for creating the help and terms of use documents
	 */
	public DocumentFactory() {
		//empty constructor
	}
	
	/**
	 * Method to create the Help document for the application. 
	 * @pre true;
	 * @post true;
	 * @return an HTML object containing the contents for the Help document.
	 */
	public HTML createHelpDocument() {
		HTML helpDoc = new HTML(
			"<span>" +
			"1) How to register as a Realtor?<br><br>" +
			"Login with your Google Account credentials.  Click on the \"Register Me\" button.  Fill out the required information and click Submit.<br><br>" +
			"2) How do I search for houses?<br><br>" +
			"Type in the desired type of house (e.g. house price range, location, etc.) in our search panel and click the search button.  All houses matching your input will be displayed in the table.  If you are looking for a more detailed information search, click on the advanced search option for more search options.<br><br>" +
			"3) How can I see more results per page?<br><br>" +
			"Click on Expand Table at the top right of the table and you will be able to view at most 50 houses per table page.<br><br>" +
			"4) How do I view houses?<br><br>" +
			"From the houses displayed in the table, click on the house that you're interested in and the Google Map and Street View will update, displaying the house you clicked.<br><br>" +
			"5) How do I sell a house?<br><br>" +
			"After successfully logging into our application, select the house you want to sell, change the radio button from no sale to sale and then press \"Submit\".  The house you selected will automatically update and display that it is for sale.<br><br>" +
			"6) What's the best way to share this application with people I know?<br><br>" +
			"If you and your friends have Facebook accounts, you're in luck, each house displayed on our Google Map contains a Facebook Share button which can post the link to a given house on various locations including your wall and your friend's wall.<br><br>" +
			"7) Is it possible to sort my search results?<br><br>" +
			"It certainly is, in fact, we have implemented our sort so that you can sort the entire database by the value of any of the columns of our table.  All you have to do is click the value you want to sort by on the table, and you can sort in ascending or descending order.<br><br>" +
			"8) Can I search for houses only within a particular region of the Google Map?<br><br>" +
			"Yes.  Either before or after entering the search criteria for the houses you want to find.  Click on the Draw button, select the region on the map that you're interested in by clicking on the Google Map and you will see a red box appear.  You can adjust the shape and size of the box via the editable vertices.  When you're done editing the search region, click on the Search button and all houses fulfilling your search criteria and are within the region you drew will be displayed in the table.  If you don't like the region you drew or don't region it anymore, just click on the Erase button.<br><br>" +
			"9) If I'm a realtor, how can I edit my contact information?<br><br>" +
			"Go to the Information tab.  Click on the edit button and enter the new information.  And now you're done!<br><br>" +
			"10)  I don't like having so many panels open at a time, is there any way to collapse some of them?<br><br>" +
			"Both the search panel and the table in our application can be collapsed.  A minimize button can be found at the top right of the search panel and another one can be found at the top left of the table.  In addition, the amount of space that the Google Map and Street View take can be adjusted by sliding the grey vertical bar that separates them.<br><br>" +
			"11) Are there any policies or rules that I should be aware of?<br><br>" +
			"This important information can be found in the link to our Terms of Use page." +
			"</span>");
		helpDoc.setWordWrap(true);
		return helpDoc;
	}
	
	/**
	 * Method to create the Terms document for the application.
	 * @pre true;
	 * @post true;
	 * @return an HTML object containing the contents for the Terms document.
	 */
	public HTML createTermsDoc() {
		HTML termsDoc = new HTML(
			"<span>" +
				"Disclaimer: This application was solely produced for educational purposes.  We are not responsible for how the data is used.  In addition, there is no moderator for this application in any aspect, including the purchase of houses; in other words, we are not responsible for how users interact with the application as well as between each other.<br><br>" +
				"By using the information in this application, you also agree to the terms in the following document online:<br><br>" +
				"<center><a href=http://data.vancouver.ca/termsOfUse.htm>Data Vancouver Terms of Use</a></center><br><br>" +
				"This document can be viewed at any time by clicking the \"Terms of Use\" link found on the application." +
			"</span>");
		termsDoc.setWordWrap(true);
		return termsDoc;
	}
}
