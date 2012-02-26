package cpsc310.client;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
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
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.HasData;
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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;



/**
 * Entry point classes define onModuleLoad().
 */

public class Team_02 implements EntryPoint {

	private VerticalPanel mainPanel = new VerticalPanel();
	private HorizontalPanel loginPanel = new HorizontalPanel();	
	private SimplePanel mapContainerPanel = new SimplePanel();	
	private TabPanel controlPanel = new TabPanel();	
	private VerticalPanel searchPanel = new VerticalPanel();
	private VerticalPanel editPanel = new VerticalPanel();		
	private CellTable<HouseDataPointClient> homesCellTable = 
			new CellTable<HouseDataPointClient>();
	private SimplePager simplePager = new SimplePager();	
	private	FlexTable searchSettingsFlexTable = new FlexTable();
	private TextBox lowerCoordTextBox = new TextBox();
	private TextBox upperCoordTextBox = new TextBox();
	private TextBox lowerLandValTextBox = new TextBox();
	private TextBox upperLandValTextBox = new TextBox();
	private TextBox ownerTextBox = new TextBox();	
	private Button searchBtn = new Button("Search");
	private FlexTable editFlexTable = new FlexTable();
	private Button editBtn = new Button("Edit");
	private TextBox priceTextBox = new TextBox();
	private Label propAddrLabel = new Label();
	private RadioButton yesSellingRdBtn = new RadioButton("isSelling", "Yes");
	private RadioButton noSellingRdBtn = new RadioButton("isSelling", "No");
	private Button loginBtn = new Button("Login");
	private Button logoutBtn = new Button("Log out");
	private HouseDataPointClient selectedHouse = null;
	private final HorizontalPanel lowerWrapPanel = new HorizontalPanel();
	private final VerticalPanel tableWrapPanel = new VerticalPanel();

  	
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {	

	    // Assemble Login Panel
	    loginPanel.add(loginBtn);
		loginPanel.add(logoutBtn);
		
						
		// Assemble mapContainer panel
		 Maps.loadMapsApi("", "2", false, new Runnable() {
			      public void run() {
			        addMap();
			      }
		});
		 
		// Create Cell Table
		initCellTable();
								
		// Create Search Criteria table. **** Possibly replace with HTML form panel
		searchSettingsFlexTable.setText(0, 0,"Coordinates");
		searchSettingsFlexTable.setWidget(0, 1, lowerCoordTextBox);
		searchSettingsFlexTable.setText(0, 2, "-");	
		searchSettingsFlexTable.setWidget(0, 3, upperCoordTextBox);
		searchSettingsFlexTable.setText(1, 0, "Land Value Range $");
		searchSettingsFlexTable.setWidget(1, 1, lowerLandValTextBox);
		searchSettingsFlexTable.setText(1, 2,"-");
		searchSettingsFlexTable.setWidget(1, 3, upperLandValTextBox);
		searchSettingsFlexTable.setText(2, 0, "Realtor");
		searchSettingsFlexTable.setWidget(2, 1, ownerTextBox);
		
		// Format Search Criteria table
		searchSettingsFlexTable.getFlexCellFormatter().setColSpan(2, 1, 3);
		searchSettingsFlexTable.setCellSpacing(10);
		
		// Assemble Search panel
		searchPanel.add(searchSettingsFlexTable);
		searchPanel.add(searchBtn);
		searchPanel.setCellVerticalAlignment(searchBtn, HasVerticalAlignment.ALIGN_MIDDLE);
		searchPanel.setCellHorizontalAlignment(searchBtn, HasHorizontalAlignment.ALIGN_CENTER);

		
		// Assemble Control Tab panel
		controlPanel.add(searchPanel, "Search", false);
		controlPanel.selectTab(0);
		controlPanel.setAnimationEnabled(true);
		controlPanel.setHeight("300px");
		controlPanel.setWidth("400px");

	  	// Assemble tableWrapPanel
	  	tableWrapPanel.add(homesCellTable);
	  	tableWrapPanel.add(simplePager);
		tableWrapPanel.setSpacing(20);
	  	tableWrapPanel.setCellHorizontalAlignment(simplePager, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Assemble lowerWrapPanel
		lowerWrapPanel.add(controlPanel);	  	
	  	lowerWrapPanel.add(tableWrapPanel);
	  	
		// Assemble Main Panel
		mainPanel.add(loginPanel);		
		mainPanel.add(mapContainerPanel);	
		mainPanel.add(lowerWrapPanel);
	  	
		// Set style
		loginPanel.setStyleName("loginPanel");		 
		mainPanel.setStyleName("mainPanel");
		mapContainerPanel.setStyleName("mapContainerPanel");
		lowerWrapPanel.setStyleName("lowerWrapPanel");
		
		allowEdit();		
		
		// Associate Main panel with the HTML host page
		RootPanel.get("appPanel").add(mainPanel);

		
		// Listen for mouse events on Search
		searchBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				searchHouse();
			}
		});		
		
	}
	
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
	 * Initializes homesCellTable.
	 * Creates cell columns, adds those columns to the table,
	 * populates the table using dataProvider, creates sort handler,
	 * sets sort comparators, then sets the column width of the table. 
	 * Note: sorting and populating will be replaced by server-side methods.
	 */
	private void initCellTable() {
	  	// Create cell columns
	  	TextColumn<HouseDataPointClient> pidColumn = 
	  			new TextColumn<HouseDataPointClient>() {
	  		@Override
	  		public String getValue(HouseDataPointClient house) {
	  			return house.getPID();
	  		}
	  	};
	  	TextColumn<HouseDataPointClient> addrColumn = 
	  			new TextColumn<HouseDataPointClient>() {
	  		@Override
	  		public String getValue(HouseDataPointClient house) {
	  			return house.getAddress();
	  		}
	  	};
	  	TextColumn<HouseDataPointClient> postalColumn = 
	  			new TextColumn<HouseDataPointClient>() {
			@Override
			public String getValue(HouseDataPointClient house) {
				return house.getPostalCode();
			}
		};
	  	Column<HouseDataPointClient, Number> coordColumn = 
	  			new Column<HouseDataPointClient, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseDataPointClient house) {
	  			return house.getCoordinate();
	  		}
	  	};
	  	Column<HouseDataPointClient, Number> landValColumn = 
	  			new Column<HouseDataPointClient, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseDataPointClient house) {
	  			return house.getLandValue();
	  		}
	  	};
	  	TextColumn<HouseDataPointClient> ownerColumn = 
	  			new TextColumn<HouseDataPointClient>() {
	  		@Override
	  		public String getValue(HouseDataPointClient house) {
	  			return house.getOwner();
	  		}
	  	};		
	  	Column<HouseDataPointClient, Number> priceColumn = 
	  			new Column<HouseDataPointClient, Number>(new NumberCell()) {
	  		@Override
	  		public Number getValue(HouseDataPointClient house) {
	  			return house.getPrice();
	  		}
	  	};
	  	TextColumn<HouseDataPointClient> isSellingColumn = 
	  			new TextColumn<HouseDataPointClient>() {
	  		@Override
	  		public String getValue(HouseDataPointClient house) {
	  			if (house.getIsSelling())
	  				return "For Sale";
	  			return "";
	  		}
	  	};
	  	
	  	// Enable sorting by clicking on the column headings
	  	pidColumn.setSortable(true);
	  	addrColumn.setSortable(true);
	  	postalColumn.setSortable(true);
	  	coordColumn.setSortable(true);
	  	landValColumn.setSortable(true);
	  	ownerColumn.setSortable(true);
	  	priceColumn.setSortable(true);
	  	isSellingColumn.setSortable(true);

	  	
	  	// Add columns to the table
	  	homesCellTable.addColumn(pidColumn, "PID");
	  	homesCellTable.addColumn(addrColumn, "Address");		
	  	homesCellTable.addColumn(postalColumn, "Postal Code");		
	  	homesCellTable.addColumn(coordColumn, "Land Coordinate");		
	  	homesCellTable.addColumn(landValColumn, "Current Value");				
	  	homesCellTable.addColumn(ownerColumn, "Owner");
	  	homesCellTable.addColumn(priceColumn, "Price");
	  	homesCellTable.addColumn(isSellingColumn, "Sale");
	  	homesCellTable.setColumnWidth(pidColumn, 80.0, Unit.PX);
	  	homesCellTable.setColumnWidth(addrColumn, 150.0, Unit.PX);
	  	homesCellTable.setColumnWidth(postalColumn, 80.0, Unit.PX);
	  	homesCellTable.setColumnWidth(coordColumn, 80.0, Unit.PX);
	  	homesCellTable.setColumnWidth(landValColumn, 80.0, Unit.PX);
	  	homesCellTable.setColumnWidth(ownerColumn, 80.0, Unit.PX);
	  	homesCellTable.setColumnWidth(priceColumn, 80.0, Unit.PX);
	  	homesCellTable.setColumnWidth(isSellingColumn, 80.0, Unit.PX);
	  	simplePager.setDisplay(homesCellTable);		
		homesCellTable.setPageSize(3);		
	
	/*
		// Create sort handler, associate sort handler to the table
		ListHandler<HouseDataPointClient> sortHandler =  
				new ListHandler<HouseDataPointClient>(dataProvider.getList());
		homesCellTable.addColumnSortHandler(sortHandler);
		
	
		// Set comparators for sorting
		sortHandler.setComparator(pidColumn, new Comparator<HouseDataPointClient>() {
			public int compare(HouseDataPointClient o1, HouseDataPointClient o2) {
				return o1.getPID().compareTo(o2.getPID());
			}
		});
		
		sortHandler.setComparator(addrColumn, new Comparator<HouseDataPointClient>() {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				int o1space = o1.getAddress().indexOf(" ");
				int o2space = o2.getAddress().indexOf(" ");
				String o1StreetName = o1.getAddress().substring(o1space + 1);
				String o2StreetName = o1.getAddress().substring(o2space + 1);
				String o1StreetNum = o1.getAddress().substring(0, o1space - 1);
				String o2StreetNum = o2.getAddress().substring(0, o2space - 1);
				if (o1StreetName.compareTo(o2StreetName) == 0) {
					return o1StreetNum.compareTo(o2StreetNum);
				}
				return o1StreetName.compareTo(o2StreetName);
			}
		});
		
		sortHandler.setComparator(postalColumn, new Comparator<HouseDataPointClient>() {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				return o1.getPostalCode().compareTo(o2.getPostalCode());
			}
		});
		
		sortHandler.setComparator(coordColumn, new Comparator<HouseDataPointClient> () {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				return o1.getCoordinate() > o2.getCoordinate() ? 1 : 
					o1.getCoordinate() == o2.getCoordinate() ? 0 : -1;
			}
		});		
		
		sortHandler.setComparator(landValColumn, new Comparator<HouseDataPointClient> () {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				return o1.getLandValue() > o2.getLandValue() ? 1 : 
					o1.getLandValue() == o2.getLandValue() ? 0 : -1;
			}
		});
			
		sortHandler.setComparator(ownerColumn, new Comparator<HouseDataPointClient> () {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				return o1.getOwner().compareTo(o2.getOwner());
			}
		});
		
		sortHandler.setComparator(priceColumn, new Comparator<HouseDataPointClient> () {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				return o1.getPrice() > o2.getPrice() ? 1 : 
					o1.getPrice() == o2.getPrice() ? 0 : -1;
			}
		});
		
		sortHandler.setComparator(isSellingColumn, new Comparator<HouseDataPointClient> () {
			public int compare (HouseDataPointClient o1, HouseDataPointClient o2) {
				if (o1.getIsSelling() == true && o2.getIsSelling() == false)
					return 1;
				if (o1.getIsSelling() == o2.getIsSelling())
					return 0;
				return -1;
			}
		});
	
	*/
		
		// Set column width
		homesCellTable.setWidth("auto", true);
	}
	
	/**
	 * Gets user input from search tab and passes to server-side search
	 */
	private void searchHouse() {
		final String lowerCoord = lowerCoordTextBox.getText().trim();
		final String upperCoord = upperCoordTextBox.getText().trim();
		final String lowerLandVal = lowerLandValTextBox.getText().trim();
		final String upperLandVal = upperLandValTextBox.getText().trim();
		final String owner = ownerTextBox.getText().trim().toUpperCase();
		
		String[] searchCriteria = {lowerCoord, upperCoord, lowerLandVal, upperLandVal, owner};
		
		String checkDigit = "|[0-9]+[/.]?[0-9]*";
		//String checkPostalCode = "|[A-Z][0-9][A-Z][ ][0-9][A-Z][0-9]";
		String checkOwner = "|[A-Z]+[ ]?+[A-Z]";
	
		// Coordinates and land values must be numeric value
		if (!lowerCoord.matches(checkDigit) | !upperCoord.matches(checkDigit) |
				!lowerLandVal.matches(checkDigit) | !upperLandVal.matches(checkDigit)) {
			Window.alert("Only numeric value is allowed for coordinates and land values.");
			return;
		}		
		
		// Realtor name must be alphabetical
		if (!owner.matches(checkOwner)) {
			Window.alert("Please type in a valid name.");
			ownerTextBox.selectAll();
			return;
		}
		
		// TODO Server-side search
	}

	/**
	 * Enables Edit function in the app. Called upon user login.
	 * To enable edit, initializes selection in the CellTable.
	 * Then creates Edit criteria tab and adds to the main controlTab.
	 */
	private void allowEdit() {
		
		// Initialize table selection
		initCellTableSelection();	
		
		// Create Edit Criteria table.
		editFlexTable.setText(0, 0, "Property Address");
		editFlexTable.setWidget(0, 1, propAddrLabel);
		editFlexTable.setText(1, 0, "Price");
		priceTextBox.setAlignment(TextAlignment.CENTER);
		editFlexTable.setWidget(1, 1, priceTextBox);
		editFlexTable.setText(2, 0, "For Sale");
		editFlexTable.setWidget(2, 1, yesSellingRdBtn);		
		editFlexTable.setWidget(2, 2, noSellingRdBtn);		
		noSellingRdBtn.setValue(true);
		
		// Format Edit Criteria Table
		editFlexTable.getFlexCellFormatter().setColSpan(0, 1, 2);
		editFlexTable.getFlexCellFormatter().setColSpan(1, 1, 2);
		editFlexTable.setCellSpacing(15);
		
		// Assemble Edit panel
		editPanel.add(editFlexTable);
		editPanel.add(editBtn);
		editPanel.setCellVerticalAlignment(editBtn, HasVerticalAlignment.ALIGN_MIDDLE);
		editPanel.setCellHorizontalAlignment(editBtn, HasHorizontalAlignment.ALIGN_CENTER);
		
		// Add Edit tab to the controlPanel
		controlPanel.add(editPanel, "Edit", false);
		
		// Listen for mouse events on Edit
		editBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				editHouse();
			}
		});
	}

	/**
	 * Creates selection column which selects/de-selects a HouseDataPoint 
	 * upon clicking the check box.
	 */
	private void initCellTableSelection() {
		
	/*
		// Create selection model
		final SingleSelectionModel<HouseDataPointClient> selectionModel = 
				new SingleSelectionModel<HouseDataPointClient> (KEY_PROVIDER);
						
		// Associate the selection model with the table
		homesCellTable.setSelectionModel(selectionModel, 
				DefaultSelectionEventManager.<HouseDataPointClient> createCheckboxManager());
		
		// Handle selection event. Upon selection the address label in the edit panel 
		// is updated with the selected house address.
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				HouseDataPointClient selected = selectionModel.getSelectedObject();
				if (selected == null) {
					propAddrLabel.setText(null);
					setSelectedHouse(null);
				}
				propAddrLabel.setText(selected.getAddress());
				setSelectedHouse (selected);
			}
		});
		
		// Create checkBox column		
		final Column<HouseDataPointClient, Boolean> selectRowColumn = 
				new Column<HouseDataPointClient, Boolean>(new CheckboxCell(true, false)) {
			@Override
			public Boolean getValue(HouseDataPointClient house) {
				return selectionModel.isSelected(house);
			}
		};
		
		// Add checkBox column to the table
		homesCellTable.addColumn(selectRowColumn, "Select");
	*/	
	}
	
	/**
	 * Edit handler.
	 * Gets user input in the edit tab, checks for valid input,
	 * then passes on to the server-side edit
	 */
	private void editHouse() {
		final String price = priceTextBox.getText().trim();
		final boolean isSelling = yesSellingRdBtn.getValue();
		final HouseDataPointClient house = selectedHouse;  
		
		// Check for selection
		if (house == null) {
			Window.alert("Please select a house to edit");
			return;
		}
		
		// Price input must be numerical
		if (!price.matches("|[0-9]*")) {
			Window.alert("Only non-decimal numeric value is allowed for price.");
			priceTextBox.selectAll();
			return;
		}
		
		// TODO Server-side edit
		
		// Refresh table
		refreshHouse(house);
	}
	
	/**
	 * Helper to table selection event
	 * 
	 * @param house
	 */
	private void setSelectedHouse(HouseDataPointClient house) {
		selectedHouse = house;
	}
	
	/**
	 * Helper to call server-side refresh upon edit request
	 * @param house
	 */
	private void refreshHouse(HouseDataPointClient house) {
		// TODO Server-side refresh
	}
}
