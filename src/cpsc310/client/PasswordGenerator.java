package cpsc310.client;

import java.util.Date;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * Note: This is is just quickly made class based off the
 * Disclaimer Generator to put a password into our application.
 * 
 * Class to generate the disclaimer at the start of the application.
 * If the browser has cookies enabled.  A cookie storing the user's
 * decision on how whether or not to display the disclaimer at the
 * application's start up is stored for 1 year.
 * 
 * Note: This class has been tested and functions correctly; however,
 * the value this feature added to the application wasn't deemed
 * worthy.
 */
public class PasswordGenerator {

	private DialogBox termsWindow;
	private CheckBox disclaimerCheckbox;
	private String cookieName = "iVanHomesPrices";
	private int aYear = 12; //12 months
	private String width = "500";
	private String height = "200";
	private PasswordTextBox password;
	
	/**
	 * Constructor for DisclaimerGenerator class
	 */
	public PasswordGenerator() {
		//empty constructor
	}
	
	/**
	 * Method to generate the disclaimer dialog box or update the cookie that
	 * stores the user's choice.  Calls createDisclaimer() or refreshCookie()
	 * depending on the user's previous history with the application.
	 * 
	 * Note: createDisclaimer() is called when either this is the user's first
	 * time using the application or they didn't select the checkbox from a previous
	 * session.  Otherwise, refreshCookie() is called.
	 * 
	 * @pre true;
	 * @post true;
	 * @return the dialog box containing the disclaimer dialogbox.
	 */
	public void generateDisclaimer() {
		//check to see what the value of the cookie is
		String retrievedValue = Cookies.getCookie(cookieName);
		if(retrievedValue == null || retrievedValue.equals("false")) {
			//generate disclaimer if this is the first time opening the application
			//or the user did not click the checkbox to stop this message
			createPasswordPrompt();
		}
		else {
			//refresh the cookies expire date by 1 year
			refreshCookie();
		}
	}
	
	/**
	 * Method to generate the disclaimer dialog box.
	 * @pre true;
	 * @post dialog window is displayed with terms document;
	 * @return the dialog box containng the disclaimer information and other
	 * 		   components.
	 */
	public DialogBox createPasswordPrompt() {
		//create dialogbox, scroll panel and flow panel to hold content, ok button
		//to close dialog box, and check box
		termsWindow = new DialogBox();
		ScrollPanel scrollPanel = new ScrollPanel();
		FlowPanel dialogBoxHolder = new FlowPanel();
		Button okBtn = new Button();
		disclaimerCheckbox = new CheckBox();
		password = new PasswordTextBox();

		//add settings to components
		disclaimerCheckbox.setText("Remember Password.");
		scrollPanel.setSize(width, height);
		termsWindow.setText("Enter Password");
		termsWindow.setGlassEnabled(true);
		okBtn.setText("OK");
		
		//add click handler to ok button
		okBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String userInput = password.getText();
				if(userInput.equals("RichardRocks@1"))
				{
					//attempt to create cookie
					Date cookieExpireDate = new Date();
					CalendarUtil.addMonthsToDate(cookieExpireDate, aYear);
					boolean userSelection = disclaimerCheckbox.getValue();
		
					//warn user if cookies are disabled
					if(!Cookies.isCookieEnabled()) {
						Window.alert("Your setting won't be saved.  Please enable cookies for" +
							"your browser");
					}
					//create cookie and hide dialog box
					Cookies.setCookie(cookieName, String.valueOf(userSelection), cookieExpireDate);
					termsWindow.hide();
				}
				else
				{
					Window.Location.assign("www.google.com");
				}
			}
		});
		//add components to dialog box and add settings to dialog box
		dialogBoxHolder.add(password);
		dialogBoxHolder.add(disclaimerCheckbox);
		dialogBoxHolder.add(okBtn);
		scrollPanel.add(dialogBoxHolder);
		termsWindow.add(scrollPanel);
		termsWindow.show();
		termsWindow.center();
		return termsWindow;
	}
	
	/**
	 * Method to renew the cookie (for 1 year) for determining if the
	 * checkbox was selected by the user.  Note: should only be called
	 * after user selects the checkbox from initial use of the application;
	 * i.e. this method always sets the cookie value to true.
	 * @pre true;
	 * @post Cookies.getCookie("iVanHomesPrices").equals("true");
	 */
	public void refreshCookie() {
		//update the cookie so that it expires in another year
		Date cookieExpireDate = new Date();
		CalendarUtil.addMonthsToDate(cookieExpireDate, aYear);
		Cookies.setCookie(cookieName, "true", cookieExpireDate);
	}
}
