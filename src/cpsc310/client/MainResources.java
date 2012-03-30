package cpsc310.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface MainResources extends ClientBundle {
	public static final MainResources INSTANCE =  GWT.create(MainResources.class);
	
	@Source("Team_02.css")
	@CssResource.NotStrict
	public CssResource css();
	
}
