package cpsc310.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Main EntryPoint class. UI is built, client-side request is handled. 
 */
public class Team_02 implements EntryPoint {
	private LayoutPanel mainPanel = new LayoutPanel();
	private DockLayoutPanel submainPanel = new DockLayoutPanel(Unit.PX);
	private SplitLayoutPanel mapContainerPanel = new SplitLayoutPanel();
	private PropertyMap theMap;	
	private FlowPanel sidePanel = new FlowPanel();
	private FlowPanel sidePanelContentWrap = new FlowPanel();
	private Button hideShowSidePanelButton = new Button("-");
	private boolean isSidePanelHidden = false;	
	private FlowPanel loginPanel = new FlowPanel();	
	private Button loginBtn = new Button("Login");
	private Button logoutBtn = new Button("Log out");	
	private FlowPanel searchPanel = new FlowPanel();	
	private FlowPanel searchSettingPanel = new FlowPanel();
	private TextBox addressTextBox = new TextBox();
	private TextBox postalCodeTextBox = new TextBox();
	private TextBox lowerLandValTextBox = new TextBox();
	private TextBox upperLandValTextBox = new TextBox();
	private TextBox yearBuiltTextBox = new TextBox();
	private TextBox lowerPriceTextBox = new TextBox();
	private TextBox upperPriceTextBox = new TextBox();	
	private TextBox ownerTextBox = new TextBox();
	private RadioButton yesSellingRdBtn = new RadioButton("isSelling", "Yes");
	private RadioButton noSellingRdBtn = new RadioButton("isSelling", "No");
	private RadioButton unknownSellingRdBtn = new RadioButton("isSelling", "All");
	private Button searchBtn = new Button("Search");
	private HorizontalPanel uploadPanel = new HorizontalPanel();
	private Button uploadBtn = new Button("Upload");
	private TextBox uploadFileTextBox = new TextBox();		
	private FlowPanel tableWrapPanel = new FlowPanel();
	private Button hideShowTablePanelButton = new Button("-");
	private boolean isTablePanelHidden = false;		
	private HouseTable houseTable = HouseTable.createHouseTable();
	private SimplePager simplePager = new SimplePager();	
	private HouseDataServiceAsync houseDataSvc = GWT.create(HouseDataService.class);
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private LoginInfo loginInfo = null;
	private boolean isLoginServiceAvailable = false;
	private boolean isEditable = false;
	private boolean isSearching = false;
	private int databaseLength = 0;
	private int pageLength = 0;	
	private Set<HouseData> selectedHouses = null;	
	private List<HouseData> currentHouseList = null;
	
	/**
	 * Entry point method. Initializes login service.
	 * Upon completion of asynchronous request to login service,
	 * UI is built. 
	 */
	public void onModuleLoad() {
		// Check login status using login service.
		if (loginService == null) {
			loginService = GWT.create(LoginService.class);
		}
		// TODO: when deploying delete "Team_02.html?gwt.codesvr=127.0.0.1:9997" below.
	    loginService.login(GWT.getHostPageBaseURL()+ "Team_02.html?gwt.codesvr=127.0.0.1:9997", 
	    		new AsyncCallback<LoginInfo>() {
		      public void onFailure(Throwable error) {
		    	  Window.alert("Login service could not be loaded.");
		    	  buildUI();
		      }
		      public void onSuccess(LoginInfo result) {
		        loginInfo = result;
		        isLoginServiceAvailable = true;
		        buildUI();
		      }
		    });
	
	}
	
	
	private void buildUI() {
	    
		// Add login panel
		addLoginPanel();
		
		// Open a map centered on Vancouver
		LatLng vancouver = LatLng.newInstance(49.264448, -123.185844);
		theMap = new PropertyMap(vancouver);
		
		// Initialize selection model for map and table
		initSelection();		
				
		// Enable edit function only if login service is available AND
		// the user is logged in.
		if (isLoginServiceAvailable == true && loginInfo.isLoggedIn()) {
			houseTable.enableEdit();
		}
		mainPanel.setHeight(Window.getClientHeight() + "px");
		submainPanel.setHeight(Window.getClientHeight() + "px");
	  	sidePanel.add(new HTML ("<div id ='header'><h1>iVan</br>Homes</br>Prices</h1></div>"));		
		hideShowSidePanelButton.setStyleName("hideShowButton");
		hideShowSidePanelButton.addStyleDependentName("vertical");
	  	sidePanel.add(hideShowSidePanelButton);
		sidePanelContentWrap.setStyleName("sidePanelContentWrap");
		sidePanel.add(sidePanelContentWrap);
		submainPanel.addWest(sidePanel, 230);
		
		tableWrapPanel.setStyleName("tableWrapPanel");
	  	
	  	submainPanel.addSouth(tableWrapPanel, 300);
	  	hideShowTablePanelButton.setStyleName("hideShowButton");
	  	hideShowTablePanelButton.addStyleDependentName("horizontal");
	  	tableWrapPanel.add(hideShowTablePanelButton);
	  	tableWrapPanel.add(houseTable.getHouseTable());
	  	tableWrapPanel.add(simplePager);
	  	
		// Create Cell Table & attach pager to table
		simplePager.setDisplay(houseTable.getHouseTable());
		simplePager.setStylePrimaryName("pager");		
		
		// Assemble map panel
		mapContainerPanel.addWest(theMap.getStreetViewMap(), 500);
		mapContainerPanel.add(theMap.getMap());
		mapContainerPanel.setStyleName("mapContainerPanel");
		submainPanel.add(mapContainerPanel);		
		
	  	
		
	  	sidePanelContentWrap.add(loginPanel);
	  	sidePanelContentWrap.add(new HTML("<br />"));
	  	// Assemble search panel
	 	addSearchPanel();
	 	sidePanelContentWrap.add(new HTML("<br />"));
	  	sidePanelContentWrap.add(uploadPanel);
	  	sidePanelContentWrap.add(new HTML("<br />"));
	  	sidePanel.add(new HTML ("<div id ='footer'><span>iVanHomesPrices.<br/>Created by Team XD. 2012.</span></div>"));
	  	mainPanel.add(submainPanel);
	  	mainPanel.setWidgetLeftWidth(submainPanel, 0, Unit.PCT, 100, Unit.PCT);
	  	
	  	// Set style
	  	loginPanel.setStyleName("loginPanel");
	  	
	  	// Assemble uploadPanel
	  	uploadPanel.add(uploadFileTextBox);
	  	uploadPanel.add(uploadBtn);
	  	
		// Listen for mouse events on Upload Button
		// for testing purposes, given URL 
		uploadBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				String URL = uploadFileTextBox.getText();
				if(URL.length()>1){
					
					AsyncCallback<Void> callback = new AsyncCallback<Void> () {
						@Override
						public void onFailure (Throwable caught) {
							Window.alert(caught.getMessage());	
						}
						@Override
						public void onSuccess (Void result) {
						}
					};
					houseDataSvc.initilizeDataStorage(URL, callback);
				}
				else Window.alert("URL invalid");
			}
		});
		
		
		hideShowSidePanelButton.addClickHandler(new ClickHandler () {
			public void onClick (ClickEvent event) {
				if (!isSidePanelHidden) {
					isSidePanelHidden = true;
					hideShowSidePanelButton.setText("+");
					submainPanel.setWidgetSize(sidePanel, 20);
					submainPanel.animate(300);	  
				}
				else {
					isSidePanelHidden = false;
					hideShowSidePanelButton.setText("-");
					submainPanel.setWidgetSize(sidePanel, 230);
					submainPanel.animate(300);
				}
			}
		});
	  	
		hideShowTablePanelButton.addClickHandler(new ClickHandler () {
			public void onClick (ClickEvent event) {
				if (!isTablePanelHidden) {
					isTablePanelHidden = true;
					hideShowTablePanelButton.setText("+");
					submainPanel.setWidgetSize(tableWrapPanel, 20);
					submainPanel.animate(300);	  
				}
				else {
					isTablePanelHidden = false;
					hideShowTablePanelButton.setText("-");
					submainPanel.setWidgetSize(tableWrapPanel, 300);
					submainPanel.animate(300);
				}
			}
		});
	  	
	  	// Associate Main panel with the HTML host page		
	 	RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
	 	rootLayoutPanel.add(mainPanel);
		
	}
	
	
	/**
	 * Helper to buildUI(). Assembles login panel which holds login/logout buttons.
	 * TODO: add user info
	 */
	private void addLoginPanel() {
		// Enable login/logout only if the login service is available.
		if (isLoginServiceAvailable == true) {
			// Set Login Panel
			loginPanel.add(loginBtn);
			loginPanel.add(logoutBtn);
			
			// Load the login/logout button depending on login/logout status
	        if(loginInfo.isLoggedIn()){
				loginBtn.setVisible(false);
				loginBtn.setEnabled(false);
				isEditable = true;
			}
			else{
				logoutBtn.setVisible(false);
				logoutBtn.setVisible(false);
			}
					
			 // Listen for mouse events on Login
			loginBtn.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					Window.Location.assign(loginInfo.getLoginUrl());
				}
			});
			
			// Listen for mouse events on Logout
			logoutBtn.addClickHandler(new ClickHandler() {
				public void onClick (ClickEvent event) {
					Window.Location.assign(loginInfo.getLogoutUrl());
				}
			});
		}		
	}
	
	/**
	 * Helper to buildUI(). Build search panel.
	 */
	private void addSearchPanel() {
		
		
		searchPanel.setStyleName("searchPanel");
	  	searchSettingPanel.setStyleName("searchSettingPanel");
	  	
	  	searchSettingPanel.add(new HTML("<div class='border'></div>"));
	  	searchSettingPanel.add(new Label("Address"));	  	
	  	searchSettingPanel.add(addressTextBox);
	  	addressTextBox.addStyleDependentName("longer");
	  	searchSettingPanel.add(new Label("Postal Code"));
	  	searchSettingPanel.add(postalCodeTextBox);
	  	searchSettingPanel.add(new Label("Land Value"));
	  	searchSettingPanel.add(lowerLandValTextBox);
	  	lowerLandValTextBox.setText("min");
	  	lowerLandValTextBox.addStyleDependentName("before");
	  	lowerLandValTextBox.addClickHandler(new ClickHandler() {
	  		public void onClick (ClickEvent event) {
	  			lowerLandValTextBox.setText("");
	  			lowerLandValTextBox.removeStyleDependentName("before");
	  		}
	  	});
	  	searchSettingPanel.add(upperLandValTextBox);
	  	searchSettingPanel.add(new Label("Year Built"));
	  	searchSettingPanel.add(yearBuiltTextBox);
	  	searchSettingPanel.add(new Label("Price"));
	  	searchSettingPanel.add(lowerPriceTextBox);
	  	searchSettingPanel.add(upperPriceTextBox);
	  	searchSettingPanel.add(new Label("Realtor"));
	  	searchSettingPanel.add(ownerTextBox);
	  	searchSettingPanel.add(new Label("For Sale"));
	  	searchSettingPanel.add(yesSellingRdBtn);
	  	searchSettingPanel.add(new InlineHTML("&nbsp;&nbsp;"));
	  	searchSettingPanel.add(noSellingRdBtn);
	  	searchSettingPanel.add(new InlineHTML("&nbsp;&nbsp;"));
	  	searchSettingPanel.add(unknownSellingRdBtn);
	  	unknownSellingRdBtn.setValue(true);
	  	
	  	searchPanel.add(searchSettingPanel);
	  	searchPanel.add(new HTML("<br />"));
	  	searchPanel.add(searchBtn);
	  	sidePanelContentWrap.add(searchPanel);
		
		// Listen for mouse events on Search Button
		searchBtn.addClickHandler(new ClickHandler() {
			public void onClick (ClickEvent event) {
				searchHouse();
			}
		});
	}
	
	
	/**
	 * Create MultiSelectionModel and attach to map and table.
	 * Attachment of selection model enables display of selected houses in map, and
	 * editing of houses in table.
	 */
	private void initSelection() {
		
		// Create selection model
		final MultiSelectionModel<HouseData> selectionModel = 
				new MultiSelectionModel<HouseData> (HouseData.KEY_PROVIDER);
		
		// Handle selection event. Upon selection selected houses get displayed on map.
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				selectedHouses = selectionModel.getSelectedSet();
				if (selectedHouses.isEmpty()) {
					if (isEditable == true) {
						
					}
					theMap.clearMap();	
					return;
				}
				if (isEditable == true) {
					
				}
				// clear map before proceeding to add new point
				theMap.clearMap();
				// add marker onto map
				for (HouseData house : selectedHouses)
					theMap.findLocation(house.getAddress() + " VANCOUVER");
			}
		});
		
		// Attach selection model to table to enable synchronous selection between map and table.
		houseTable.enableSelection(selectionModel);
	}
	
	
	/**
	 * Gets user input from search tab and passes to server-side search
	 * TODO: refactor into input validation, criteria assembly, and asynch search request
	 * TODO: add additional search criteria to reflect everything on searchsettingspanel
	 */
	private void searchHouse() {
		boolean isCoordRangeValid = true;
		boolean isLandValRangeValid = true;
		double lowerLandVal = -1;
		double upperLandVal = -1;
		int lowerCoord = -1;
		int upperCoord = -1;
		String owner = null;
		final String lowerCoordInput = lowerPriceTextBox.getText().trim();
		final String upperCoordInput = upperPriceTextBox.getText().trim();
		final String lowerLandValInput = lowerLandValTextBox.getText().trim();
		final String upperLandValInput = upperLandValTextBox.getText().trim();
		final String ownerInput = ownerTextBox.getText().trim().toUpperCase();
		
		String checkDigit = "\\d*";
		//String checkPostalCode = "|[A-Z][0-9][A-Z][ ][0-9][A-Z][0-9]";
		String checkOwner = "[A-Za-z\\s]*";
		
		String numericAlert = "Only numeric values are allowed for coordinates and land values. \n";
		String ownerAlert = "Only alphabet characters are allowed for realtor name";
		String inputErrorMsg = "";
		boolean checkNumeric = true;
		boolean checkAlpha = true;
		
		String coordRangeAlert = "I need to have both values of coordinate range specified. \n";
		String landValRangeAlert = "I need to have both values of land value range specified.";
		String rangeErrorMsg ="";
	
		// Coordinates and land values must be numeric value
		if (!lowerCoordInput.matches(checkDigit) || !upperCoordInput.matches(checkDigit) ||
				!lowerLandValInput.matches(checkDigit) || !upperLandValInput.matches(checkDigit)) {
			checkNumeric = false;
			inputErrorMsg = inputErrorMsg.concat(numericAlert);
		}		
		
		// Realtor name must be alphabetical
		if (!ownerInput.matches(checkOwner)) {
			checkAlpha = false;
			inputErrorMsg = inputErrorMsg.concat(ownerAlert);
			ownerTextBox.selectAll();
		}
		
		// Throw error message to user if inputs are invalid. 
		if (checkNumeric == false || checkAlpha == false) {
			Window.alert(inputErrorMsg);
			return;
		}
		
		// Warn upperCoord/upperLandVal must not be less than lowerCoord/lowerLandVal
		if (upperCoordInput.compareTo(lowerCoordInput) == -1 ||
				upperLandValInput.compareTo(lowerLandValInput) == -1) {
			Window.alert("Upper values cannot be greater than lower values");
			return;
		}
		
		// Warn user if only one of the coordinate/land value range is given. 
		// **Will be modified to accommodate one-input range cases in Sprint 2
		if (lowerCoordInput.isEmpty() ^ upperCoordInput.isEmpty()) {
			isCoordRangeValid = false;
			rangeErrorMsg = rangeErrorMsg.concat(coordRangeAlert);
		}
		else if (lowerLandValInput.isEmpty() ^ upperLandValInput.isEmpty()) {
			isLandValRangeValid = false;
			rangeErrorMsg = rangeErrorMsg.concat(landValRangeAlert);
		}
		
		// Warn user if user gave only one value of the coordinate/land range.
		if ((isCoordRangeValid == false) || (isLandValRangeValid == false)) {
			Window.alert(rangeErrorMsg);
			return;
		}
		
		// If user sent empty search, return default (all) data
		if (lowerCoordInput.isEmpty() && upperCoordInput.isEmpty() &&
				lowerLandValInput.isEmpty() && upperLandValInput.isEmpty() &&
				ownerInput.isEmpty()) {
			
			// Initialize the service proxy
			if (houseDataSvc == null) {
				houseDataSvc = GWT.create(HouseDataService.class);
			}
			// Setup Callback
			AsyncCallback<List<HouseData>> callback = new AsyncCallback<List<HouseData>> () {
				@Override
				public void onFailure (Throwable caught) {
					Window.alert(caught.getMessage());
				}
				@Override
				public void onSuccess (List<HouseData> result) {
					isSearching = false;
					houseTable.setSearch(isSearching, result);
					houseTable.updateTable(result, databaseLength, 0, true);
				}
			};
			houseDataSvc.getHouses(0, pageLength, callback);
			return;
		}
	
		// Assemble search criteria
		if (!lowerCoordInput.isEmpty())
			lowerCoord = Integer.parseInt(lowerCoordInput);
		if (!upperCoordInput.isEmpty())
			upperCoord = Integer.parseInt(upperCoordInput);		
		if (!lowerLandValInput.isEmpty())
			lowerLandVal = Double.parseDouble(lowerLandValInput);
		if (!upperLandValInput.isEmpty())
			upperLandVal = Double.parseDouble(upperLandValInput);
		if (!ownerInput.isEmpty())
			owner = ownerInput;		
		
		// Initialize the service proxy
		if (houseDataSvc == null) {
			houseDataSvc = GWT.create(HouseDataService.class);
		}
		
		// Set up the callback object
		AsyncCallback<List<HouseData>> callback = new AsyncCallback<List<HouseData>>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			public void onSuccess(List<HouseData> result) {
				if (result != null) {
					currentHouseList = result;
					isSearching = true;					
					houseTable.setSearch(isSearching, result);
					houseTable.updateTable(result, result.size(), 0, false);
				}
				else {
					result = new ArrayList<HouseData> ();
					houseTable.updateTable(result, 0, 0, false);
					Window.alert("No result found");
				}
			}
		};
		// Make the call to the house data service to search for data in the server
			houseDataSvc.getSearchedHouses(lowerCoord, upperCoord, 
					lowerLandVal, upperLandVal, owner, callback);
	}
	
}
