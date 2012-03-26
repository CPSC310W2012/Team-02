package cpsc310.client;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * My Account panel that has user information.
 */
public class UserInfoPanel extends FlowPanel {
	
	/**
	 * Constructor
	 * @param loginInfo - current user's information instance
	 */
	public UserInfoPanel(LoginInfo loginInfo) {
		if (loginInfo != null) {
			// Set style
			this.setStyleName("userInfoPanel");
		}
	}

}
