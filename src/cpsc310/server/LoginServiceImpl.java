package cpsc310.server;

import java.util.HashMap;
import java.util.Iterator;
 
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import cpsc310.client.LoginInfo;
import cpsc310.client.LoginService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class LoginServiceImpl extends RemoteServiceServlet implements
LoginService {

	/**
	 * Constructor
	 */
	public LoginServiceImpl() {
		// try to register
		try {
			ObjectifyService.register(LoginInfo.class);
		} catch (IllegalArgumentException e) {
			// already registered
			System.out.println("LoginInfo already registered in data store");
		}
	}

	public LoginInfo login(String requestUri) {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		LoginInfo loginInfo = new LoginInfo();

		if (user != null) {
			loginInfo.setLoggedIn(true);
			loginInfo.setEmailAddress(user.getEmail());
			loginInfo.setNickname(user.getNickname());
			loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
		} else {
			loginInfo.setLoggedIn(false);
			loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
		}
		return loginInfo;
	}

	@Override
	public LoginInfo getUser(String email) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.get(LoginInfo.class, email);
	}
	
	@Override
	public void storeUser(LoginInfo info) {
		// Populate memory store with changed entries
		Objectify ofy = ObjectifyService.begin();
		ofy.put(info);
	}
}