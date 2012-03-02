package cpsc310.client;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PropertyMap {
	private MapWidget map;

	/**
	 * Constructor
	 */
	public PropertyMap() {
		map = new MapWidget();
	}

	/**
	 * Sets up the map centered on Vancouver
	 */
	public void buildUi() {
		// Open a map centered on Vancouver
		LatLng vancouver = LatLng.newInstance(49.264448, -123.185844);
		map.setCenter(vancouver, 10);
		map.setSize("100%", "100%");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());
	}

	/**
	 * Finds the location and plots it on the map
	 * @param location - string representation of the address         
	 */
	public void findLocation(final String location) {
		LatLngCallback callback = new LatLngCallback() {

			public void onFailure() {
				Window.alert("Location not found");
			}

			public void onSuccess(LatLng point) {
				// add the location onto the map
				addMarker(point, location);
			}
		};
		Geocoder geocoder = new Geocoder();
		geocoder.getLatLng(location, callback);
	}

	/**
	 * Adds a marker to the map
	 * @param point  latitude and longitude
	 * @param location  string representation of the location to be displayed in overlay
	 */
	private void addMarker(LatLng point, String location)
	{
		Marker marker = new Marker(point);
		map.addOverlay(marker);
		map.setCenter(point);
		VerticalPanel panel = new VerticalPanel();
		InfoWindowContent content = new InfoWindowContent(panel);
		panel.add(new Label(location));
		map.getInfoWindow().open(marker, content);
	}
	
	
	/**
	 * Clears all of the markers from the map
	 */
	public void clearMap() {
		map.clearOverlays();
	}

	/**
	 * Returns reference to the MapWidget
	 */
	public MapWidget getMap() {
		return this.map;
	}
	
	/**
	 * TODO: Method that removes one marker given the string location
	 * @param location - string representation of the address         
	 */
	public void removeMarker(final String location) {
		/*
		 * Window.alert("removing: " + location); LatLngCallback callback = new
		 * LatLngCallback() {
		 * 
		 * public void onFailure() { //address was not found. do nothing //
		 * insert some error handling here // popup message?
		 * Window.alert("marker not found, failed to remove"); }
		 * 
		 * public void onSuccess(LatLng point) { // removes the location onto
		 * the map Window.alert("remove overlay is not working.."); Marker
		 * marker = new Marker(point); map.removeOverlay(marker); } }; Geocoder
		 * geocoder = new Geocoder(); geocoder.getLatLng(location, callback);
		 * map.clearOverlays();
		*/
	}
}
