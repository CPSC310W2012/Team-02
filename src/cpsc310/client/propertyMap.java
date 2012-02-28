package cpsc310.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class propertyMap {
	MapWidget map;
	
	// constructor
	public propertyMap() {
		map = new MapWidget();
	}
	
	public void buildUi() {
	    // Open a map centered on vancouver
	    LatLng vancouver = LatLng.newInstance(49.264448, -123.185844);
		map.setCenter(vancouver, 10);
	    map.setSize("100%", "100%");
	    // Add some controls for the zoom level
	    map.addControl(new LargeMapControl());
	  }
	
	  /*
	   * Finds the location and plots it on the map!
	   * 
	   */  
	public void findLocation(final String location) {
		  LatLngCallback callback = new LatLngCallback() {

		 	   public void onFailure() {
		 	   //address was not found. do nothing
		 		   // insert some error handling here
		 		   // popup message?
		 	   }

		 	   public void onSuccess(LatLng point) {
		 	   // add the location onto the map
		 	   Marker marker = new Marker(point);
		 	   map.addOverlay(marker);
		 	   map.setCenter(point);
		 	   VerticalPanel panel = new VerticalPanel();
		 	   InfoWindowContent content = new InfoWindowContent(panel);
		 	   panel.add(new Label(location));
		 	   map.getInfoWindow().open(marker, content);
		 	   }
		 	   };
		 	   Geocoder geocoder = new Geocoder();
		 	   geocoder.getLatLng(location, callback);
	  }
	  
	  // Method that clears all markers from the map
	  public void clearMap() {
		  map.clearOverlays();
	  }
	  
	 // get the mapWidget 
	  public MapWidget getMap() {
		  return this.map;
	  }
	  
	  // TODO: Method that removes one marker given the string location
	  public void removeMarker(final String location) {
		  /*
		  Window.alert("removing: " + location);
		  LatLngCallback callback = new LatLngCallback() {

		 	   public void onFailure() {
		 	   //address was not found. do nothing
		 		   // insert some error handling here
		 		   // popup message?
		 		  Window.alert("marker not found, failed to remove");
		 	   }

		 	   public void onSuccess(LatLng point) {
		 	   // removes the location onto the map
		 	   Window.alert("remove overlay is not working..");
		 	   Marker marker = new Marker(point);
		 	   map.removeOverlay(marker);
		 	   }
		 	   };
		 	   Geocoder geocoder = new Geocoder();
		 	   geocoder.getLatLng(location, callback);
		 	   
		 	   */
		  map.clearOverlays();
	  }
}
