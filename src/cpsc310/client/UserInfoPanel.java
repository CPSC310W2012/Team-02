package cpsc310.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

/**
 * My Account panel that has user information.
 */
public class UserInfoPanel extends FlowPanel {
	private LoginInfo loginInfo = null;
	private Label errorMsg;
	private Label userPhoneNumber;		
	private Label userWebsite;
	private Label userDescription;
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	
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
		userPhoneNumber = new Label("");		
		userWebsite = new Label("");
		userDescription = new Label("");
		
		// Get info from login info
		userName.setText("Hello, " + loginInfo.getNickname());
		userEmail.setText("Email: " + loginInfo.getEmailAddress());
		userPhoneNumber.setText("Phone #: " + loginInfo.getphoneNumber());
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
		changeUserInfoBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EditUserInfoDialog editUserInfoDialog = 
						new EditUserInfoDialog(loginInfo);
				editUserInfoDialog.center();
				editUserInfoDialog.show();
			}
			
		});
		
		this.add(changeUserInfoBtn);
	}

	/**
	 * Builds and adds button that allows user to
	 * see all his/her houses to the panel
	 */
	private void addSeeUserHousesBtn() {
		final Button seeUserHousesBtn = new Button("See My Houses");
		
		seeUserHousesBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getUserHouse();				
			}
		});
		
		this.add(new HTML("<hr>"));
		this.add(seeUserHousesBtn);
	}

	
	/**
	 * Async call to the server to grab user's houses
	 */
	private void getUserHouse() {
		// TODO Make Async call once async call is implemented
		
	}
	
	public void refreshUserInfoPanel()
	{
		AsyncCallback<LoginInfo> userCallback = new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
				Window.alert("exception in refreshUserInfoPanel - call to getUser");
			}
			public void onSuccess(LoginInfo user) {
				//Window.alert("found user: " + user.getEmailAddress());
				userPhoneNumber.setText("Phone #: " + user.getphoneNumber());
				userWebsite.setText("Website: " + user.getWebsite());
				userDescription.setText("Description: " + user.getDescription());
				Window.alert("editing user info panel");
			}
		};
		loginService.getUser(loginInfo.getEmailAddress(), userCallback);
		
	}
	
	
	
}
