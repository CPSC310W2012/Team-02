package cpsc310.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>LoginService</code>.
 */
public interface LoginServiceAsync {
  public void login(String requestUri, AsyncCallback<LoginInfo> async);
}