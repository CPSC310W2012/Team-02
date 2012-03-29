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
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.datepicker.client.CalendarUtil;

/**
 * Class to generate the disclaimer at the start of the application.
 * If the browser has cookies enabled.  A cookie storing the user's
 * decision on how whether or not to display the disclaimer at the
 * application's start up is stored for 1 year.
 */
public class DisclaimerGenerator {

	private DialogBox termsWindow;
	private CheckBox disclaimerCheckbox;
	private String cookieName = "iVanHomesPrices";
	private int aYear = 12; //12 months
	private String width = "500";
	private String height = "200";
	
	/**
	 * Constructor for DisclaimerGenerator class
	 */
	public DisclaimerGenerator() {
		//empty constructor
	}
	
	/**
	 * Method to generate the disclaimer dialog box.
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
			createDisclaimer();
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
	public DialogBox createDisclaimer() {
		//create dialogbox, scroll panel and flow panel to hold content, ok button
		//to close dialog box, and check box
		termsWindow = new DialogBox();
		ScrollPanel scrollPanel = new ScrollPanel();
		FlowPanel dialogBoxHolder = new FlowPanel();
		Button okBtn = new Button();
		disclaimerCheckbox = new CheckBox();

		//add settings to components
		disclaimerCheckbox.setText("Check this box to remove this popup at start.");
		scrollPanel.setSize(width, height);
		termsWindow.setText("Terms of Use");
		termsWindow.setGlassEnabled(true);
		okBtn.setText("OK");
		
		//add click handler to ok button
		okBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
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
		});
		//add components to dialog box and add settings to dialog box
		dialogBoxHolder.add(new DocumentFactory().createTermsDoc());
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
	 * after initial user initially selects the checkbox; i.e. this method
	 * always sets the cookie value to true.
	 * @pre true;
	 * @post Cookies.getCookie("iVanHomesPrices").equals("true");
	 */
	private void refreshCookie() {
		//update the cookie so that it expires in another year
		Date cookieExpireDate = new Date();
		CalendarUtil.addMonthsToDate(cookieExpireDate, aYear);
		Cookies.setCookie(cookieName, "true", cookieExpireDate);
	}
}
