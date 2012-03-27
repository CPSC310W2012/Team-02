package cpsc310.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

/**
 * My Account panel that has user information.
 */
public class UserInfoPanel extends FlowPanel {
	private LoginInfo loginInfo = null;
	
	/**
	 * Constructor
	 * @param loginInfo - current user's information instance
	 */
	public UserInfoPanel(LoginInfo loginInfo) {
		if (loginInfo != null) {

			this.loginInfo = loginInfo;
			
			// Set style
			this.setStyleName("userInfoPanel");
			
			// Add user's current information to the panel
			addUserInfo();

			// Add buttons
			addChangeUserInfoBtn();
			addSeeUserHousesBtn();
		}
	}

	/**
	 * Add user info to the panel
	 */
	private void addUserInfo() {
		Label userName = new Label("");
		Label userEmail = new Label("");
		Label userPhoneNumber = new Label("");		
		Label userWebsite = new Label("");
		Label userDescription = new Label("");
		
		// Get info from login info
		userName.setText("Hello, " + loginInfo.getNickname());
		userEmail.setText("Email: " + loginInfo.getEmailAddress());
		userPhoneNumber.setText("Phone number: " + loginInfo.getphoneNumber());
		userWebsite.setText("Website: " + loginInfo.getWebsite());
		userDescription.setText("Description: " + loginInfo.getDescription());
		
		// Add to panel
		this.add(userName);
		this.add(new HTML("<br>"));
		this.add(userEmail);
		this.add(userPhoneNumber);
		this.add(userWebsite);
		this.add(userDescription);
	}

	/**
	 * Builds and adds Change User Info button to the panel
	 */
	private void addChangeUserInfoBtn() {
		final Button changeUserInfoBtn = new Button("Change My Info");
		
		this.add(changeUserInfoBtn);
	}

	/**
	 * Builds and adds button that allows user to
	 * see all his/her houses to the panel
	 */
	private void addSeeUserHousesBtn() {
		// TODO Auto-generated method stub
		final Button seeUserHousesBtn = new Button("See My Houses");
		
		this.add(new HTML("<hr>"));
		this.add(seeUserHousesBtn);
	}
}
