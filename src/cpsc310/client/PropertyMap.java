package cpsc310.client;

import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.PolyEditingOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.streetview.LatLngStreetviewCallback;
import com.google.gwt.maps.client.streetview.Pov;
import com.google.gwt.maps.client.streetview.StreetviewClient;
import com.google.gwt.maps.client.streetview.StreetviewPanoramaOptions;
import com.google.gwt.maps.client.streetview.StreetviewPanoramaWidget;
import com.google.gwt.user.client.Window;

public class PropertyMap {
	private MapWidget map;
	private StreetviewPanoramaWidget panorama;
	private StreetviewClient svClient;
	// polygon settings
	private String color = "#FF0000";
	private double opacity = 0.1;
	private int weight = 1;
	private boolean fillFlag = true;
	private Polygon lastPolygon = null;

	/**
	 * Constructor Instantiates two instances of a map - standard and streetview
	 * 
	 * @param location
	 *            - location you want the map centered on
	 * 
	 */

	public PropertyMap(LatLng location) {
		buildStreetViewMap(location);
		buildStandardMap(location);
	}

	/**
	 * Sets up the streetview map centered on provided location
	 * 
	 * @param location
	 *            - the location to center the map on
	 */
	public void buildStreetViewMap(LatLng location) {
		StreetviewPanoramaOptions options = StreetviewPanoramaOptions
				.newInstance();
		options.setLatLng(location);
		svClient = new StreetviewClient();
		panorama = new StreetviewPanoramaWidget(options);
		panorama.setSize("100%", "100%");
	}

	/**
	 * Sets up the standard map centered on provided location
	 * 
	 * @param location
	 *            - the location to center the map on
	 */
	public void buildStandardMap(LatLng location) {
		map = new MapWidget();
		map.setCenter(location, 10);
		map.setSize("100%", "100%");
		// Add some controls for the zoom level
		map.addControl(new LargeMapControl3D());
	}

	/**
	 * Finds the location and plots it on the map changes the street-view too
	 * 
	 * @param location
	 *            - string representation of the address
	 */
	public void findLocation(final String location) {
		LatLngCallback callback = new LatLngCallback() {

			public void onFailure() {
				Window.alert("Location not found");
			}

			public void onSuccess(LatLng point) {
				// add the location onto the map
				addMarker(point, location);
				refreshStreetView(point);
			}
		};
		Geocoder geocoder = new Geocoder();
		geocoder.getLatLng(location, callback);
	}

	/**
	 * Adds a marker to the map
	 * 
	 * @param point
	 *            latitude and longitude
	 * @param location
	 *            string representation of the location to be displayed in
	 *            overlay
	 */
	private void addMarker(final LatLng point, final String location) {
		final Marker marker = new Marker(point);
		map.addOverlay(marker);
		map.setCenter(point);
		map.getInfoWindow().open(marker,
				new InfoWindowContent(location.toLowerCase()));
		refreshStreetView(point);

		marker.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent event) {
				try {
					map.getInfoWindow().open(marker,
							new InfoWindowContent(location.toLowerCase()));
					refreshStreetView(point);
				} catch (Exception e) {
					Window.alert(e.getMessage());
				}

			}
		});
	}

	/**
	 * Changes streetview location given location coordinates
	 * 
	 * @param location
	 *            latitude and longitude
	 */

	private void refreshStreetView(LatLng location) {
		svClient.getNearestPanoramaLatLng(location,
				new LatLngStreetviewCallback() {
					@Override
					public void onFailure() {
						// streetview is not available
					}

					@Override
					public void onSuccess(LatLng point) {
						panorama.setLocationAndPov(point, Pov.newInstance());
					}
				});
	}

	/**
	 * Clears all of the markers from the map
	 */
	public void clearMap() {
		map.clearOverlays();
		if(lastPolygon != null)
			lastPolygon = null;
	}

	/**
	 * Returns reference to the standard map
	 */
	public MapWidget getMap() {
		return this.map;
	}

	/**
	 * Returns reference to the streetView map
	 */
	public StreetviewPanoramaWidget getStreetViewMap() {
		return this.panorama;
	}

	/**
	 * TODO: Method that removes one marker given the string location
	 * 
	 * @param location
	 *            - string representation of the address
	 */
	public void removeMarker(final String location) {

	}

	/**
	 * 
	 * Allows the user to edit the last drawn polygon
	 * 
	 */
	public void editPolygon() {
		if (lastPolygon == null) {
			return;
		}
		lastPolygon.setEditingEnabled(PolyEditingOptions.newInstance(4));
	}

	/**
	 * Allows the user to draw a region on the map
	 * 
	 */
	public void createPolygon() {
		PolyStyleOptions style = PolyStyleOptions.newInstance(color, weight,
				opacity);

		final Polygon poly = new Polygon(new LatLng[0], color, weight, opacity,
				color, fillFlag ? .7 : 0.0);
		lastPolygon = poly;
		map.addOverlay(poly);
		poly.setDrawingEnabled();
		poly.setStrokeStyle(style);
	}
	
	/**
	 * 
	 * TODO: algorithm that calculates whether or not the point is in the polygon
	 * 
	 * @param point  the point to check if it is in the polygon
	 * 
	 */
	public boolean pointInPolygon(LatLng point) {
		if(lastPolygon == null)
			return false;
		return false;
	}

}
