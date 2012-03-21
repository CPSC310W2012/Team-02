package cpsc310.client;

import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.MetaElement;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.control.LargeMapControl3D;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geocode.Geocoder;
import com.google.gwt.maps.client.geocode.LatLngCallback;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.geom.Point;
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.maps.client.overlay.PolyEditingOptions;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.maps.client.streetview.LatLngStreetviewCallback;
import com.google.gwt.maps.client.streetview.Pov;
import com.google.gwt.maps.client.streetview.StreetviewClient;
import com.google.gwt.maps.client.streetview.StreetviewPanoramaOptions;
import com.google.gwt.maps.client.streetview.StreetviewPanoramaWidget;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.reveregroup.gwt.facebook4gwt.Facebook;
import com.reveregroup.gwt.facebook4gwt.ShareButton;

public class PropertyMap {
	private MapWidget map;
	private MapOptions mOptions;
	private StreetviewPanoramaWidget panorama;
	private StreetviewClient svClient;
	// keep a stack of all the markers
	private Stack<Marker> markers = new Stack();
	
	// polygon settings
	private String color = "#FF0000";
	private double opacity = 0.1;
	private int weight = 1;
	private boolean fillFlag = true;
	private Polygon lastPolygon = null;
	private boolean specifyingRegion = false;

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
		
		map.addMapClickHandler(new MapClickHandler() {
			public void onClick(MapClickEvent e) {
		        LatLng point = e.getLatLng();
				if(specifyingRegion)
				{
					drawSquare(point);
					//allow only one square to be drawn at a time
					setSpecifyingRegion(false);
				}
		      }
		    });
		
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
		mOptions = MapOptions.newInstance();
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
	public void findLocation(final HouseData house) {
		LatLngCallback callback = new LatLngCallback() {

			public void onFailure() {
				Window.alert("Location not found");
			}

			public void onSuccess(LatLng point) {
				// add the location onto the map
				// check if it's on sale, true for third param if so.
				addSpecialMarker(point, house, true);
				refreshStreetView(point);
				//isPointInPolygon(point); //for testing
			}
		};
		Geocoder geocoder = new Geocoder();
		geocoder.getLatLng(house.getAddress() + " VANCOUVER, BC", callback);
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
		
		markers.push(marker);
	}

	
	/**
	 * Adds a marker to the map, if the property is on sale, it will be a green marker
	 * 
	 * @param point
	 *            latitude and longitude
	 * @param location
	 *            string representation of the location to be displayed in
	 *            overlay
	 * @param onSale true if the property is on sale
	 *
	 */
	private void addSpecialMarker(final LatLng point, final HouseData house, boolean onSale) {
		Icon icon;
		// icon is green if it's on sale, red otherwise
		if(onSale)
			 icon = Icon.newInstance("http://maps.google.com/mapfiles/ms/micons/green-dot.png");
		else icon = Icon.newInstance();
		
		icon.setShadowURL("http://maps.google.com/mapfiles/ms/micons/msmarker.shadow.png");
	    icon.setIconAnchor(Point.newInstance(6, 20));
	    icon.setInfoWindowAnchor(Point.newInstance(5, 1));

	    MarkerOptions options = MarkerOptions.newInstance();
	    options.setIcon(icon);
	    
		final Marker marker = new Marker(point, options);
		map.addOverlay(marker);
		map.setCenter(point);
		
		// Info window containing house data information
		
		final InfoWindowContent content = new InfoWindowContent(getMarkerPanel(house));
	    
	    map.getInfoWindow().open(marker, content);

		refreshStreetView(point);

		marker.addMarkerClickHandler(new MarkerClickHandler() {
			public void onClick(MarkerClickEvent event) {
				try {
					map.getInfoWindow().open(marker,content);
					refreshStreetView(point);
				} catch (Exception e) {
					Window.alert(e.getMessage());
				}

			}
		});
		
		markers.push(marker);
	}
	
	private VerticalPanel getMarkerPanel(HouseData house)
	{
		VerticalPanel markerInfoWindow = new VerticalPanel();
		HTML htmlWidget;
		// If the house is on sale, provide extra field for sale price and realtor information
		if(house.getIsSelling()){
			//TODO: isSelling field for a house is not set when realtor edits on table
			// need to add event handler and modify houseData point
			htmlWidget = new HTML("<p><b><u>Property Information</u></b></br> " +
	    		"<b>Address: </b>" + house.getAddress().toLowerCase()+ "</br>" +
	    		"<b>Current Land Value: </b>" + house.getCurrentLandValue()+ "</br>" + 
	    		"<b>Year built: </b>" + house.getYearBuilt() + "</br>" +
	    		"<b>Selling Price: </b>" +house.getPrice()+"</br>" +
	    		"<b>Owner: </b>" +house.getOwner()+ "</p>");
		}
		else
		{
			htmlWidget = new HTML("<p><b><u>Property Information</u></b></br> " +
		    		"<b>Address: </b>" + house.getAddress().toLowerCase()+ "</br>" +
		    		"<b>Current Land Value: </b>" + house.getCurrentLandValue()+ "</br>" + 
		    		"<b>Year built: </b>" + house.getYearBuilt() + "</p>");
		}
		
		markerInfoWindow.add(htmlWidget);
		// TODO: Add Facebook share buttoN
		
		//Richard Added
		/* Other idea that I didn't try
		MetaElement houseMeta = Document.get().createMetaElement();
		houseMeta.setAttribute("og:description", "This house");
		*/
		NodeList<Element> metaTags = Document.get().getElementsByTagName("meta");
		for(int i = 0; i < metaTags.getLength(); i++)
		{
			MetaElement tagRetrieved = (MetaElement) metaTags.getItem(i);
			if(tagRetrieved.getAttribute("property").equals("og:description"))
			{
				tagRetrieved.setContent("This house");
			}
		}
		
		String shareText = "hello\nhello\n";
		ShareButton shareBtn = new ShareButton(GWT.getHostPageBaseURL(), shareText);
		markerInfoWindow.add(shareBtn);
		
		return markerInfoWindow;
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
	 * Clears all overlays from the map
	 */
	public void clearMap() {
		map.clearOverlays();
		if(lastPolygon != null)
			lastPolygon = null;
	}
	
	/**
	 * Clears all of the markers from the map
	 */
	public void clearMarkers() {

		while(!markers.empty())
		{
			map.removeOverlay(markers.pop());
		}
	}
	
	
	/**
	 * deletes the specified region on the map
	 */
	public void clearSpecifiedRegion() {
		if(lastPolygon != null)
		{
			map.removeOverlay(lastPolygon);
			lastPolygon = null;
		}
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
	 * Algorithm that calculates whether or not the point is in the polygon
	 * 
	 * @param point  the point to check if it is in the polygon
	 * 
	 */
	boolean isPointInPolygon(LatLng point) {

	if(lastPolygon == null)
	{
	 Window.alert("No region specified");
	 return false;
	}

	int j = 0;
	boolean oddNodes = false;
	double y = point.getLatitude();
	double x = point.getLongitude();
	int numVertexes = lastPolygon.getVertexCount();

	for(int i = 0; i < numVertexes; i++)
	{
		j++; 
	    if (j == numVertexes) {
			j = 0;
		}
	    if (((lastPolygon.getVertex(i).getLatitude() < y) && (lastPolygon.getVertex(j).getLatitude() >= y)) 
	      || ((lastPolygon.getVertex(j).getLatitude() < y) && (lastPolygon.getVertex(i).getLatitude() >= y))) 
		{ 
	        if ( lastPolygon.getVertex(i).getLongitude() + (y - lastPolygon.getVertex(i).getLatitude()) 
	        /  (lastPolygon.getVertex(j).getLatitude()-lastPolygon.getVertex(i).getLatitude()) 
	        *  (lastPolygon.getVertex(j).getLongitude() - lastPolygon.getVertex(i).getLongitude())<x ) { 
	          oddNodes = !oddNodes; 
	        } 
	    } 
	} 
	
	if(oddNodes)
		Window.alert("point is in the polygon");
	else 
		Window.alert("point is not in the polygon");
	    return oddNodes; 
	}
	
	
	/**
	 * 
	 * Draws a rectangle on the map given a corner point
	 * 
	 * @param point  location of the corner of the rectangle
	 * 
	 */
	public void drawSquare(LatLng point)
	{
		PolyStyleOptions style = PolyStyleOptions.newInstance(color, weight,
				opacity);
		//the other four points of the triangle
		LatLng point1 = LatLng.newInstance(point.getLatitude(), point.getLongitude()+0.1);
		LatLng point2 = LatLng.newInstance(point.getLatitude()-0.02, point.getLongitude()+0.1);
		LatLng point3 = LatLng.newInstance(point.getLatitude()-0.02, point.getLongitude());
		
		LatLng[] polygonPoints = new LatLng[5];
		polygonPoints[0] = point;
		polygonPoints[1] = point1;
		polygonPoints[2] = point2;
		polygonPoints[3] = point3;
		polygonPoints[4] = point;
		
		
		final Polygon poly = new Polygon(polygonPoints, color, weight, opacity,
				color, fillFlag ? .7 : 0.0);
		lastPolygon = poly;
		map.addOverlay(poly);
		poly.setStrokeStyle(style);
		lastPolygon.setEditingEnabled(PolyEditingOptions.newInstance(5));
		
		
	
	}
	
	/**
	 * 
	 * setter method for map click handler when drawing rectangle
	 * 
	 * @param specifyingRegion  if the user is specifying a region, it's true
	 * 
	 */
	public void setSpecifyingRegion(boolean specifyingRegion) {
		this.specifyingRegion = specifyingRegion;
	}

	
	/**
	 * 
	 * access to the private boolean value specifyingRegion
	 * 
	 * @param specifyingRegion  if the user is specifying a region, it's true
	 * 
	 */
	public boolean isSpecifyingRegion() {
		return specifyingRegion;
	}
	
}
