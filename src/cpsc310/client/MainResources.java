package cpsc310.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * This interface enables injection of design external sources
 * into GWT. 
 * This enables css injection in standard mode.
 */
public interface MainResources extends ClientBundle {
	public static final MainResources INSTANCE =  GWT.create(MainResources.class);
	
	/**
	 * Connect external css to the file that makes a call
	 * @return main css stylesheet
	 */
	@Source("Team_02.css")
	@CssResource.NotStrict
	public CssResource css();
	
}