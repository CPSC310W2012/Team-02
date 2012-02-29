package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DataCatalogueObserverAsync {
	void downloadFile(AsyncCallback<List<String>> callback)
			throws IllegalArgumentException;
}
