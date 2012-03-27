package cpsc310.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the Login RPC service.
 */
@RemoteServiceRelativePath("login")
public interface LoginService extends RemoteService {
	public LoginInfo login(String requestUri);

	/**
	 * Stores a user given a LoginInfo Object
	 * @param info
	 */
	public void storeUser(LoginInfo info);

	/**
	 * retrieve a LoginInfo for a specific User given its email. Will return
	 * null if nothing found so you can use it to check if a user is already
	 * added.
	 * 
	 * @param email
	 * @return LoginInfo object
	 */
	public LoginInfo getUser(String email);
	
/**
 * 
 * Edit account information
 * 
 * @param email
 * @param nickname
 * @param phoneNumber
 * @param website
 * @param description
 * 
 */
	public void editUser(String email, String nickname, 
			int phoneNumber, String website, String description);
	
	
}