package cpsc310.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DataCatalogueObserverAsync {
	void downloadFile(AsyncCallback<String> callback)
			throws IllegalArgumentException;
}
