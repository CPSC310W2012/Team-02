package cpsc310.client;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;



/**
 * Entry point classes define onModuleLoad().
 */

public class Team_02 implements EntryPoint {

	/**
	 * This is for creating the sample CellTable
	 * Will be removed once HouseDataPoint.java is implemented
	 */
		private static class Address {
			private final int streetNum;
			private final String streetName;
			private Address (int streetNum, String streetName) {
				this.streetNum = streetNum;
				this.streetName = streetName;
			}
			
			@Override
			public String toString() {
				return (String) (streetNum + " " + streetName);
			}
		}
	 // A simple data type that represents a house data point.
	  	private static class TmpHouseDataPoint {
		  private final String pid;
		  private final Address address;
		  private final String postalCode;
		  private final long coordinates;
		  private final long landVal;
		  private final int assmntYr;
		  private final int yrBuilt;
		  private int price;
		  private String isSelling;
		  
		  private TmpHouseDataPoint(String pid, Address address, String postalCode, 
	    		long coordinates, long landVal, int assmntYr, int yrBuilt, int price, String isSelling) {
		      this.pid = pid;
		      this.address = address;
		      this.postalCode = postalCode;
		      this.coordinates = coordinates;
		      this.landVal = landVal;
		      this.assmntYr = assmntYr;
		      this.yrBuilt = yrBuilt;
		      this.price = price;
		      this.isSelling = isSelling;
	    	}
	  	}

	  	// The list of data to display.
	  	private static List<TmpHouseDataPoint> HOUSES = Arrays.asList(
			  new TmpHouseDataPoint("014-900-009", new Address(1185, "12TH AVE E"), "V5T 2J8", 67022255, 634000 , 2012, 1974, 5699100, "For Sale"), 
			  new TmpHouseDataPoint("027-390-624", new Address(1255, "SEYMOUR ST"), "V6B 0H1", 13461255, 404000, 2012, 2008, 0, ""), 
			  new TmpHouseDataPoint("025-479-351", new Address(969,	"RICHARDS ST"), "V6B 1A8", 13860499, 228000, 2012, 2002, 0, ""), 
			  new TmpHouseDataPoint("013-998-70", new Address(5688, "WALLACE ST"), "V6N 2A2", 3474596, 1865000, 2012, 1946, 0, ""), 
			  new TmpHouseDataPoint("028-623-070", new Address(4057, "PRINCE ALBERT ST"), "", 0, 0, 0, 0, 0, "Not for Sale")
	  		  );
	
	  	private static final ProvidesKey<TmpHouseDataPoint> KEY_PROVIDER = new ProvidesKey<TmpHouseDataPoint>() {
	  	    public Object getKey(TmpHouseDataPoint house) {
	  	      return house.pid;
	  	    }
	  	  };
	  	
	  	// Create cell columns
	  	private TextColumn<TmpHouseDataPoint> pidColumn = new TextColumn<TmpHouseDataPoint>() {
			@Override
			public String getValue(TmpHouseDataPoint house) {
				return house.pid;
			}
		};
		private TextColumn<TmpHouseDataPoint> addrColumn = new TextColumn<TmpHouseDataPoint>() {
			@Override
			public String getValue(TmpHouseDataPoint house) {
				return house.address.toString();
			}
		};
	    private TextColumn<TmpHouseDataPoint> postalColumn = new TextColumn<TmpHouseDataPoint>() {
			@Override
			public String getValue(TmpHouseDataPoint house) {
				return house.postalCode;
			}
		};
		private Column<TmpHouseDataPoint, Number> coordColumn = new Column<TmpHouseDataPoint, Number>(new NumberCell()) {
			@Override
			public Number getValue(TmpHouseDataPoint house) {
				return house.coordinates;
			}
		};
		private Column<TmpHouseDataPoint, Number> landValColumn = new Column<TmpHouseDataPoint, Number>(new NumberCell()) {
			@Override
			public Number getValue(TmpHouseDataPoint house) {
				return house.landVal;
			}
		};
		NumberFormat fmt = NumberFormat.getFormat("####");
		private Column<TmpHouseDataPoint, Number> assmntYrColumn = new Column<TmpHouseDataPoint, Number>(new NumberCell(fmt)) {
			@Override
			public Number getValue(TmpHouseDataPoint house) {
				return house.assmntYr;
			}
		};
		private Column<TmpHouseDataPoint, Number> yrBuiltColumn = new Column<TmpHouseDataPoint, Number>(new NumberCell(fmt)) {
			@Override
			public Number getValue(TmpHouseDataPoint house) {
				return house.yrBuilt;
			}
		};
		private Column<TmpHouseDataPoint, Number> priceColumn = new Column<TmpHouseDataPoint, Number>(new NumberCell()) {
			@Override
			public Number getValue(TmpHouseDataPoint house) {
				return house.price;
			}
		};
		private TextColumn<TmpHouseDataPoint> isSellingColumn = new TextColumn<TmpHouseDataPoint>() {
			@Override
			public String getValue(TmpHouseDataPoint house) {
				return house.isSelling;
			}
		};
		private final Column<TmpHouseDataPoint, Boolean> selectRowColumn = new Column<TmpHouseDataPoint, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(TmpHouseDataPoint house) {
				return selectionModel.isSelected(house);
			}
		};
		
		// Create sorting handler
		private ListDataProvider<TmpHouseDataPoint> dataProvider = new ListDataProvider<TmpHouseDataPoint>();
		private ListHandler<TmpHouseDataPoint> sortHandler = new ListHandler<TmpHouseDataPoint>(dataProvider.getList());
		private final SingleSelectionModel<TmpHouseDataPoint> selectionModel = new SingleSelectionModel<TmpHouseDataPoint> (KEY_PROVIDER);
	  /**
	  * End of sample CellTable
	  */	
	
	
	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel loginPanel = new HorizontalPanel();	
	private SimplePanel mapContainerPanel = new SimplePanel();	
	private FlexTable lowerWrapFlexTable = new FlexTable();	
	private TabPanel controlPanel = new TabPanel();	
	private VerticalPanel searchPanel = new VerticalPanel();
	private VerticalPanel editPanel = new VerticalPanel();		
	private CellTable<TmpHouseDataPoint> homesCellTable = new CellTable<TmpHouseDataPoint>(KEY_PROVIDER);
	private SimplePager simplePager = new SimplePager();	
	private	FlexTable searchSettingsFlexTable = new FlexTable();
	private TextBox lowerCoordTextBox = new TextBox();
	private TextBox upperCoordTextBox = new TextBox();
	private TextBox lowerLandValTextBox = new TextBox();
	private TextBox upperLandValTextBox = new TextBox();
	private TextBox postalCodeTextBox = new TextBox();	
	private Button searchBtn = new Button("Search");
	private FlexTable editFlexTable = new FlexTable();
	private Button editBtn = new Button("Edit");
	private TextBox priceTextBox = new TextBox();
	private Label propAddrLabel = new Label();
	private RadioButton yesSellingRdBtn = new RadioButton("isSelling", "Yes");
	private RadioButton noSellingRdBtn = new RadioButton("isSelling", "No");
	private RadioButton unknownSellingRdBtn = new RadioButton("isSelling", "Unknown");
	private Button loginBtn = new Button("Login");
	private Button logoutBtn = new Button("Log out");
	private TmpHouseDataPoint selectedHouse = null;
	
	private LoginInfo loginInfo = null;
	

  	
  	
	  /**
	  * Adds the map
	  */
	private void addMap() {
	    // Open a map centered on UBC
	    LatLng ubc = LatLng.newInstance(49.261084, -123.252965);

	    final MapWidget map = new MapWidget(ubc, 12);
	    map.setSize("100%", "100%");
	    map.setStyleName("map");
	    // Add some controls for the zoom level
	    map.addControl(new LargeMapControl());

	    // Add a test marker
	    map.addOverlay(new Marker(ubc));

	    // Add an info window to highlight a point of interest
	    map.getInfoWindow().open(map.getCenter(),
	        new InfoWindowContent("Vancouver - UBC"));

	    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	    dock.addNorth(map, 500);

	    // Add the map to the HTML host page
	    mapContainerPanel.add(dock);
	  }
	
	
	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {	
		// Check login status using login service
		LoginServiceAsync loginService = GWT.create(LoginService.class);
	    loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
	      public void onFailure(Throwable error) {
	      }

	      public void onSuccess(LoginInfo result) {
	    	  loginInfo = result;
	    	// Allow Edit if logged in.
	  		if (loginInfo.isLoggedIn() == true) {
	  			allowEdit();
	  			loginBtn.setVisible(false);
	  			loginBtn.setEnabled(false);
	  		}
	  		else {
	  			logoutBtn.setVisible(false);
	  			logoutBtn.setEnabled(false);
	  		}    	  
	      }
	    });
	    
	    // Assemble Login Panel
	    loginPanel.add(loginBtn);
		loginPanel.add(logoutBtn);
						
		// Assemble mapContainer panel
		 Maps.loadMapsApi("", "2", false, new Runnable() {
			      public void run() {
			        addMap();
			      }
		});
		 
		// Create Cell Table ******* Will be updated once HouseDataPoint.java is implemented
		initCellTableSorting(sortHandler);		
		homesCellTable.addColumnSortHandler(sortHandler);
		homesCellTable.addColumn(pidColumn, "PID");
		homesCellTable.addColumn(addrColumn, "Address");		
		homesCellTable.addColumn(postalColumn, "Postal Code");		
		homesCellTable.addColumn(coordColumn, "Land Coordinate");		
		homesCellTable.addColumn(landValColumn, "Current Value");		
		homesCellTable.addColumn(assmntYrColumn, "Assessment Year");		
		homesCellTable.addColumn(yrBuiltColumn, "Year Built");
		homesCellTable.addColumn(priceColumn, "Price");
		homesCellTable.addColumn(isSellingColumn, "Sale");
		homesCellTable.setPageSize(3);
		dataProvider.addDataDisplay(homesCellTable);
		simplePager.setDisplay(homesCellTable);		 

		// Connect the table to the data provider. ******* Will be updated once HouseDataPoint.java is implemented
		List<TmpHouseDataPoint> list = dataProvider.getList();
		
		// Add the data to the data provider, which automatically pushes it to the
	    // widget. ******* Will be updated once HouseDataPoint.java is implemented
		for (TmpHouseDataPoint house : HOUSES) {
			list.add(house);
		}	
			
		// Create Search Criteria table. **** Possibly replace with HTML form panel
		searchSettingsFlexTable.setText(0, 0,"Coordinates");
		searchSettingsFlexTable.setWidget(0, 1, lowerCoordTextBox);
		searchSettingsFlexTable.setText(0, 2, "-");	
		searchSettingsFlexTable.setWidget(0, 3, upperCoordTextBox);
		searchSettingsFlexTable.setText(1, 0, "Land Value Range $");
		searchSettingsFlexTable.setWidget(1, 1, lowerLandValTextBox);
		searchSettingsFlexTable.setText(1, 2,"-");
		searchSettingsFlexTable.setWidget(1, 3, upperLandValTextBox);
		searchSettingsFlexTable.setText(2, 0, "Postal Code");
		searchSettingsFlexTable.setWidget(2, 1, postalCodeTextBox);
		
		// Assemble Search panel
		searchPanel.add(searchSettingsFlexTable);	
		searchPanel.add(searchBtn);
		
		// Assemble Control Tab panel
		controlPanel.add(searchPanel, "Search", false);
		controlPanel.selectTab(0);
			
		// Assemble lowerWrapFlexTable which wraps controlPanel, homesCellTable, and simplePager
		lowerWrapFlexTable.setWidget(0, 0, controlPanel);
		lowerWrapFlexTable.setWidget(0, 1, homesCellTable);
		lowerWrapFlexTable.setWidget(1, 1, simplePager);		
		
		// Assemble Main Panel
		mainPanel.add(loginPanel);		
		mainPanel.add(mapContainerPanel);	
		mainPanel.add(lowerWrapFlexTable);
		
		// Set style
		loginPanel.setStyleName("loginPanel");		 
		mainPanel.setStyleName("mainPanel");
		mapContainerPanel.setStyleName("mapContainerPanel");
		lowerWrapFlexTable.setStyleName("lowerWrapFlexTable");		
				
		// Associate Main panel with the HTML host page
		RootPanel.get("appPanel").add(mainPanel);

		// Listen for mouse events on Login
		loginBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				Window.Location.replace(loginInfo.getLoginUrl());
			}
		});
		
		// Listen for mouse events on Logout
		logoutBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				Window.Location.replace(loginInfo.getLogoutUrl());
			}
		});
		
		// Listen for mouse events on Search
		searchBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				searchHouse();
			}
		});		
		
		// Listen for mouse events on Edit
		editBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				editHouse();
			}
		});
	}
	
	// For enabling CellTable sort **** Will be updated once HouseDataPoint.java is implemented
	private void initCellTableSorting(ListHandler<TmpHouseDataPoint> sortHandler) {
		
		sortHandler.setComparator(pidColumn, new Comparator<TmpHouseDataPoint>() {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.pid.compareTo(o2.pid);
			}
		});
		
		sortHandler.setComparator(addrColumn, new Comparator<TmpHouseDataPoint>() {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				if (o1.address.streetName.compareTo(o2.address.streetName) == 0) {
					return o1.address.streetNum > o2.address.streetNum ? 1 : o1.address.streetNum == o2.address.streetNum ? 0 : -1;
				}
				return o1.address.streetName.compareTo(o2.address.streetName);
			}
		});
		
		sortHandler.setComparator(postalColumn, new Comparator<TmpHouseDataPoint>() {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.postalCode.compareTo(o2.postalCode);
			}
		});
		
		sortHandler.setComparator(coordColumn, new Comparator<TmpHouseDataPoint> () {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.coordinates > o2.coordinates ? 1 : o1.coordinates == o2.coordinates ? 0 : -1;
			}
		});		
		
		sortHandler.setComparator(landValColumn, new Comparator<TmpHouseDataPoint> () {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.landVal > o2.landVal ? 1 : o1.landVal == o2.landVal ? 0 : -1;
			}
		});
		
		sortHandler.setComparator(assmntYrColumn, new Comparator<TmpHouseDataPoint> () {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.assmntYr > o2.assmntYr ? 1 : o1.assmntYr == o2.assmntYr ? 0 : -1;
			}
		});
		
		sortHandler.setComparator(yrBuiltColumn, new Comparator<TmpHouseDataPoint> () {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.yrBuilt > o2.yrBuilt ? 1 : o1.yrBuilt == o2.yrBuilt ? 0 : -1;
			}
		});
		
		sortHandler.setComparator(priceColumn, new Comparator<TmpHouseDataPoint> () {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.price > o2.price ? 1 : o1.price == o2.price ? 0 : -1;
			}
		});
		
		sortHandler.setComparator(isSellingColumn, new Comparator<TmpHouseDataPoint> () {
			public int compare(TmpHouseDataPoint o1, TmpHouseDataPoint o2) {
				return o1.isSelling.compareTo(o2.isSelling);
			}
		});
		
		pidColumn.setSortable(true);
		addrColumn.setSortable(true);
		postalColumn.setSortable(true);
		coordColumn.setSortable(true);
		landValColumn.setSortable(true);
		assmntYrColumn.setSortable(true);
		yrBuiltColumn.setSortable(true);
		priceColumn.setSortable(true);
		isSellingColumn.setSortable(true);
	}
	
	private void initCellTableSelection(final SingleSelectionModel<TmpHouseDataPoint> selectionModel) {
		homesCellTable.setSelectionModel(selectionModel, DefaultSelectionEventManager.<TmpHouseDataPoint> createCheckboxManager());
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				TmpHouseDataPoint selected = selectionModel.getSelectedObject();
				if (selected == null) {
					propAddrLabel.setText(null);
					setSelectedHouse(null);
				}
				propAddrLabel.setText(selected.address.toString() + " " + selected.postalCode);
				setSelectedHouse (selected);
			}
		});
	}
	
	private void searchHouse() {
		final String lowerCoord = lowerCoordTextBox.getText().trim();
		final String upperCoord = upperCoordTextBox.getText().trim();
		final String lowerLandVal = lowerLandValTextBox.getText().trim();
		final String upperLandVal = upperLandValTextBox.getText().trim();
		final String postalCode = postalCodeTextBox.getText().trim().toUpperCase();
		
		String[] searchCriteria = {lowerCoord, upperCoord, lowerLandVal, upperLandVal, postalCode};
		
		String checkDigit = "|[0-9]+[/.]?[0-9]*";
		String checkPostalCode = "|[A-Z][0-9][A-Z][ ][0-9][A-Z][0-9]";

		// Coordinate must be numeric value
		if (!lowerCoord.matches(checkDigit) | !upperCoord.matches(checkDigit) |
				!lowerLandVal.matches(checkDigit) | !upperLandVal.matches(checkDigit)) {
			Window.alert("Only numeric value is allowed for coordinates and land values.");
			return;
		}		
		
		if (!postalCode.matches(checkPostalCode)) {
			Window.alert("Please type in a valid postal code");
			postalCodeTextBox.selectAll();
			return;
		}
		
		// TODO Search function
	}
	
	private void editHouse() {
		final String price = priceTextBox.getText().trim();
		final boolean unknownSelling = unknownSellingRdBtn.getValue();
		final TmpHouseDataPoint house = selectedHouse;  
		
		
		if (house == null) {
			Window.alert("Please select a house to edit");
			return;
		}
		
		if (!price.matches("|[0-9]*")) {
			Window.alert("Only non-decimal numeric value is allowed for price.");
			priceTextBox.selectAll();
			return;
		}
		
		
		if (price.isEmpty())
			house.price = 0;
		else
			house.price = Integer.parseInt(price);
		
		if (unknownSelling == true)
			house.isSelling = "";
		else {			
			boolean isSelling = yesSellingRdBtn.getValue();			
			if (isSelling == true)
				house.isSelling = "For Sale";
			else
				house.isSelling = "Not For Sale";
		}
		
		refreshHouse(house);
	}
	
	private void setSelectedHouse(TmpHouseDataPoint house) {
		selectedHouse = house;
	}
	
	private void refreshHouse(TmpHouseDataPoint house) {
		homesCellTable.redraw();
	}
	
	private void allowEdit() {
		
		initCellTableSelection(selectionModel);
		homesCellTable.addColumn(selectRowColumn, "Select");
		
		// Create Edit Criteria table.
		editFlexTable.setText(0, 0, "Property Address:");
		editFlexTable.setWidget(0, 1, propAddrLabel);
		editFlexTable.setText(1, 0, "Price");
		editFlexTable.setWidget(1, 1, priceTextBox);
		editFlexTable.setText(2, 0, "For Sale");
		editFlexTable.setWidget(2, 1, yesSellingRdBtn);		
		editFlexTable.setWidget(2, 2, noSellingRdBtn);		
		editFlexTable.setWidget(2, 3, unknownSellingRdBtn);
		unknownSellingRdBtn.setValue(true);
		
		// Assemble Edit panel
		editPanel.add(editFlexTable);		
		editPanel.add(editBtn);
		
		// Add Edit tab to the controlPanel
		controlPanel.add(editPanel, "Edit", false);
		
	}
}
