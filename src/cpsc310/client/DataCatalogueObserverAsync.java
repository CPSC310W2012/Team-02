package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DataCatalogueObserver</code>.
 */
public interface DataCatalogueObserverAsync {
	void downloadFile(String urlLink, AsyncCallback<List<String>> callback)
			throws IllegalArgumentException;
}
