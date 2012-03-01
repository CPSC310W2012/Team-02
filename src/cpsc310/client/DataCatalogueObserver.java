package cpsc310.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the Observer RPC service.
 */
@RemoteServiceRelativePath("observer")
public interface DataCatalogueObserver extends RemoteService {
	List<String> downloadFile(String urlLink) throws IllegalArgumentException;
}