package cpsc310.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.reveregroup.gwt.facebook4gwt.Facebook;
import com.reveregroup.gwt.facebook4gwt.LoginButton;
import com.reveregroup.gwt.facebook4gwt.ShareButton;

/**
 * Main EntryPoint class. UI is built, client-side request is handled.
 */
/**
 * To RICHARD
 * Do not touch the UI code unless you are adding something really necessary. 
 * I'm cleaning up the UI code and fixing the bugs.
 * If you are moving UI elements around, they cause me so many conflicts.
 *
 */
public class Team_02 implements EntryPoint {
	private LayoutPanel mainPanel = new LayoutPanel();
	private DockLayoutPanel submainPanel = new DockLayoutPanel(Unit.PX);
	private FlowPanel sidePanel = new FlowPanel();
	private FlowPanel tableWrapPanel = new FlowPanel();
	private MapContainerPanel data = new MapContainerPanel(new SplitLayoutPanel());
	private boolean isSidePanelHidden = false;
	private boolean isTablePanelHidden = false;
	private HouseTable houseTable = HouseTable.createHouseTable();
	private HouseDataServiceAsync houseDataSvc = GWT
			.create(HouseDataService.class);
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private LoginInfo loginInfo = null;
	private boolean isEditable = false;
	private boolean isLoginServiceAvailable = false;
	private boolean isAdvSearchPanelHidden = true;
	private Set<HouseData> selectedHouses = null;	
	final String[] searchCriteria = 
		{"Street Number", "Address", "Postal Code",
			"Current Land Value", "Current Improvement Value",
			"Assessment Year", "Previous Land Value",
			"Previous Improvement Value", "Year Built", "Big Improvement Year",
			"Price", "Realtor", "For Sale"};
	
	private LatLng vancouver = LatLng.newInstance(49.264448, -123.185844);
	private List<String> addresses = new ArrayList<String>();

	/**
	 * Entry point method. Initializes login service. Upon completion of
	 * asynchronous request to login service, UI is built.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
		if (loginService == null) {
			loginService = GWT.create(LoginService.class);
		}
		
		// TODO: when deploying delete "Team_02.html?gwt.codesvr=127.0.0.1:9997"
		// below.
		loginService.login(GWT.getHostPageBaseURL()
				+ "Team_02.html?gwt.codesvr=127.0.0.1:9997",
				new AsyncCallback<LoginInfo>() {
					public void onFailure(Throwable error) {
						Window.alert("Login service could not be loaded.");
						resetDatabase();
						buildUI();
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						isLoginServiceAvailable = true;
						resetDatabase();
						buildUI();
					}
				});
	}

	/**
	 * Builds application's main UI
	 */
	private void buildUI() {

		// Initialize selection model for map and table
		initSelection();

		// Enable edit function only if login service is available AND
		// the user is logged in.
		if (isLoginServiceAvailable == true && loginInfo.isLoggedIn()) {
			enableEdit();
		}

		// Make main panel fill the browser
		mainPanel.setHeight(Window.getClientHeight() + "px");
		submainPanel.setHeight(Window.getClientHeight() + "px");

		// Make sidePanel
		buildSidePanel(sidePanel);
		submainPanel.addWest(sidePanel, 230);

		// Make tablePanel
		buildTablePanel(tableWrapPanel);
		submainPanel.addSouth(tableWrapPanel, 300);

		// Make mapContainerPanel
		buildMapPanel(data.mapContainerPanel);
		submainPanel.add(data.mapContainerPanel);

		// Add content wrapper to the main panel
		mainPanel.add(submainPanel);
		mainPanel.setWidgetLeftWidth(submainPanel, 0, Unit.PCT, 100, Unit.PCT);

		// Associate Main panel with the HTML host page
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.add(mainPanel);
	}

	/**
	 * Create MultiSelectionModel and attach to map and table. Attachment of
	 * selection model enables display of selected houses in map, and editing of
	 * houses in table.
	 */
	private void initSelection() {

		// Create selection model
		final MultiSelectionModel<HouseData> selectionModel = new MultiSelectionModel<HouseData>(
				HouseData.KEY_PROVIDER);
		
		// Handle selection event. Upon selection selected houses get displayed
		// on map.
		selectionModel
				.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(SelectionChangeEvent event) {
						selectedHouses = selectionModel.getSelectedSet();
						
						if (selectedHouses.isEmpty()) {
							data.theMap.clearMarkers();
							return;
						}
						// clear map markers before proceeding to add new point
						data.theMap.clearMarkers();
						// add marker onto map
						for (HouseData house : selectedHouses)
							data.theMap.findLocation(house, true);
					}
				});

		// Attach selection model to table to enable synchronous selection
		// between map and table.
		houseTable.enableSelection(selectionModel);
	}

	/**
	 * Helper to buildUI(). Assemble map container panel.
	 * 
	 * @param mapContainerPanel
	 *            SplitLayoutPanel to hold the map
	 */
	private void buildMapPanel(SplitLayoutPanel mapContainerPanel) {
		// Open a map centered on Vancouver
		data.theMap = new PropertyMap(vancouver);

		// Assemble map panel
		mapContainerPanel.addWest(data.theMap.getMap(), 600);
		mapContainerPanel.add(data.theMap.getStreetViewMap());
		mapContainerPanel.setWidgetMinSize(data.theMap.getMap(), 600);
		mapContainerPanel.setStyleName("mapContainerPanel");
	}

	/**
	 * Helper to buildUI() Assembles table panel.
	 * 
	 * @param tableWrapPanel
	 *            - flow panel to hold table related elements
	 */
	private void buildTablePanel(FlowPanel tableWrapPanel) {
		Button hideShowTablePanelButton = new Button("-");
		SimplePager simplePager = new SimplePager();

		buildTablePanelButton(hideShowTablePanelButton);

		// Create Cell Table & attach pager to table
		simplePager.setDisplay(houseTable.getHouseTable());
		simplePager.setStylePrimaryName("pager");

		// Assemble table panel
		tableWrapPanel.add(hideShowTablePanelButton);
		tableWrapPanel.add(houseTable.getHouseTable());
		tableWrapPanel.add(simplePager);
		tableWrapPanel.setStyleName("tableWrapPanel");

	}

	/**
	 * Helper to buildTablePanel() Create hide/show behavior to table panel
	 * button
	 * 
	 * @param hideShowTablePanelButton
	 *            - button to behave lie hide/show button
	 */
	private void buildTablePanelButton(final Button hideShowTablePanelButton) {
		hideShowTablePanelButton.setStyleName("hideShowButton");
		hideShowTablePanelButton.addStyleDependentName("horizontal");

		hideShowTablePanelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!isTablePanelHidden) {
					isTablePanelHidden = true;
					hideShowTablePanelButton.setText("+");
					submainPanel.setWidgetSize(tableWrapPanel, 20);
					submainPanel.animate(300);
					data.mapContainerPanel.getWidget(0);
				} else {
					isTablePanelHidden = false;
					hideShowTablePanelButton.setText("-");
					submainPanel.setWidgetSize(tableWrapPanel, 300);
					submainPanel.animate(300);
				}
			}
		});
	}

	/**
	 * Helper to buildUI() Assemble side panel.
	 * 
	 * @param sidePanel
	 *            flow panel to hold sidepanel contents
	 */
	private void buildSidePanel(FlowPanel sidePanel) {
		Button hideShowSidePanelButton = new Button("-");
		TabPanel sidebarTabPanel = new TabPanel();
		FlowPanel menuPanel = new FlowPanel();

		// Create hide/show ability into the button
		buildSidePanelButton(hideShowSidePanelButton);

		// Assemble menu panel
		buildMenuPanel(menuPanel);

		// Assemble GWT widgets to occupy side panel
		buildSideTabPanel(sidebarTabPanel);

		// Assemble side panel
		sidePanel.add(new HTML(
				"<div id ='header'><h1>iVan</br>Homes</br>Prices</h1></div>"));
		sidePanel.add(hideShowSidePanelButton);
		sidePanel.add(menuPanel);
		sidePanel.add(sidebarTabPanel);
		sidePanel.add(new HTML(
				"<div id ='footer'><span>iVanHomesPrices.<br/>Created by Team XD. 2012.</span></div>"));
	}

	/**
	 * Helper to buildSidePanel(). Creates hide/show button behavior.
	 * 
	 * @param hideShowSidePanelButton
	 *            - button to behave like hide/show button
	 */
	private void buildSidePanelButton(final Button hideShowSidePanelButton) {
		hideShowSidePanelButton.setStyleName("hideShowButton");
		hideShowSidePanelButton.addStyleDependentName("vertical");

		hideShowSidePanelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!isSidePanelHidden) {
					isSidePanelHidden = true;
					hideShowSidePanelButton.setText("+");
					submainPanel.setWidgetSize(sidePanel, 20);
					submainPanel.animate(300);
				} else {
					isSidePanelHidden = false;
					hideShowSidePanelButton.setText("-");
					submainPanel.setWidgetSize(sidePanel, 230);
					submainPanel.animate(300);
				}
			}
		});
	}

	/**
	 * Helper to buildSidePanel() Assembles GWT widgets that needs to be
	 * included in the sidePanel.
	 * 
	 * @param sidebarTabPanel
	 *            - flow panel to wrap the widgets
	 */
	private void buildSideTabPanel(TabPanel sidebarTabPanel) {
		FlowPanel searchPanel = new FlowPanel();
						
		// Assemble search panel
		buildSearchPanel(searchPanel);
		
		// Add Widgets to the tab panel
		sidebarTabPanel.add(searchPanel, "Search");
		
		// Set details of tab panel look
		sidebarTabPanel.selectTab(0);
		sidebarTabPanel.setAnimationEnabled(true);
		sidebarTabPanel.addStyleDependentName("sideTabPanel");		
		
		// If user is logged in, assemble user info panel and add it to the tab
		if (isLoginServiceAvailable == true) {
			FlowPanel userInfoPanel = new FlowPanel();
			if (loginInfo.isLoggedIn()) {
				buildUserInfoPanel(userInfoPanel);	
				sidebarTabPanel.add(userInfoPanel, "My Account");
			}
			else {
				userInfoPanel.clear();
				if (sidebarTabPanel.getWidgetCount() > 1)
					sidebarTabPanel.remove(1);
			}
		}
	}

	private void buildMenuPanel(FlowPanel menuPanel) {		
		Button helpBtn = new Button("Help");
		Button termsBtn = new Button("Terms of Use");
		
		//Set the styles of elements
		helpBtn.setStyleName("gwt-Button-textButton");
		termsBtn.setStyleName("gwt-Button-textButton");
		menuPanel.setStyleName("menuPanel");
		
		// Build and add the login anchors to the menu
		buildLoginAnchor(menuPanel);

		// Richard Added
		FlowPanel faceBookTemp = new FlowPanel();
		Facebook.init("257432264338889");
		LoginButton faceBookBtn = new LoginButton(true);
		ShareButton shareBtn = new ShareButton(GWT.getHostPageBaseURL(),"Check out this house!!!");
		faceBookTemp.add(faceBookBtn);
		faceBookTemp.add(shareBtn);
		faceBookTemp.add(new HTML("<iframe src=\"//www.facebook.com/plugins/like.php?href=http%3A%2F%2Frmar3a01.appspot.com%2F&amp;send=false&amp;layout=button_count&amp;width=450&amp;show_faces=false&amp;action=like&amp;colorscheme=light&amp;font&amp;height=21&amp;appId=257432264338889\" scrolling=\"no\" frameborder=\"0\" style=\"border:none; overflow:hidden; width:450px; height:21px;\" allowTransparency=\"true\"></iframe>"));
		
		menuPanel.add(helpBtn);
		menuPanel.add(termsBtn);
		menuPanel.add(faceBookTemp);
	}

	/**
	 * Helper to buildMenuPanel. Adds login/logout links
	 * @param menuPanel - menuPanel to add login/logout links
	 */
	private void buildLoginAnchor(FlowPanel menuPanel) {
		// Enable login/logout only if the login service is available.				
		if (isLoginServiceAvailable == true) {
			Anchor loginLink = new Anchor("Login");
			Anchor logoutLink = new Anchor("Logout");
			
			// Set Login links
			loginLink.setHref(loginInfo.getLoginUrl());
			logoutLink.setHref(loginInfo.getLogoutUrl());
			
			// Add to menu
			menuPanel.add(loginLink);
			menuPanel.add(logoutLink);

			// Enable/disable the login/logout links depending on login/logout status
			if (loginInfo.isLoggedIn()) {
				loginLink.setVisible(false);
				loginLink.setEnabled(false);
				isEditable = true;
			} else {
				logoutLink.setVisible(false);
				logoutLink.setVisible(false);
			}
		}
		
	}

	/**
	 * Helper to buildSidePanelWidgets(). Assembles login panel which holds
	 * login/logout buttons. TODO: add user info
	 */
	private void buildUserInfoPanel(FlowPanel userInfoPanel) {
		// Set style
		userInfoPanel.setStyleName("userInfoPanel");
	}

	/**
	 * Helper to buildSidePanelWidgets(). Build search panel.
	 * searchSettingsPanel contains all the text boxes and labels needed for
	 * search field. searchPanel wraps the searchSettingsPanel and searchBtn.
	 */
	private void buildSearchPanel(FlowPanel searchPanel) {
		final FlowPanel searchSettingPanel = new FlowPanel();
		FlowPanel advancedSettingPanel = new FlowPanel();
		final PopupPanel advancedSettingPopup = new PopupPanel(false);
		Button searchBtn = new Button("Search");
		final Button advancedSearchBtn = new Button ("Advanced Search"); 
		final List<TextBox> searchValues = new ArrayList<TextBox>();
		final List<RadioButton> forSale = new ArrayList<RadioButton>(3);
		final ListBox addressDropDown = new ListBox(false);
		final String[] advancedSearchCriteria = {"Postal Code", "Current Improvement Value",
				"Assessment Year", "Previous Land Value",
				"Previous Improvement Value", "Year Built", "Big Improvement Year"};
		
		final String[] basicSearchCriteria = {"Street Number", "Address", "Current Land Value", 
				"Price", "Realtor", "For Sale"};
		
		// Append style
		searchPanel.setStyleName("searchPanel");
		searchSettingPanel.setStyleName("searchSettingPanel");
		advancedSettingPopup.setStyleName("advancedSettingPopup");

		// Build searchSettingPanel
		searchSettingPanel.add(new HTML("<div class='border'></div>"));
		buildSearchFields(searchSettingPanel, basicSearchCriteria, 
				searchValues, forSale, addressDropDown);
		buildSearchFields(advancedSettingPanel, advancedSearchCriteria, 
				searchValues, forSale, addressDropDown);
		advancedSettingPopup.setAnimationEnabled(true);
		advancedSettingPopup.setWidget(advancedSettingPanel);	
		
		// Add polygon selection
		buildPolygonSelection(searchSettingPanel);

		// Add searchSettingPanel and searchBtn to the searchPanel
		searchPanel.add(searchSettingPanel);
		searchPanel.add(advancedSearchBtn);
		searchPanel.add(new HTML("<br />"));
		searchPanel.add(searchBtn);

		// Listen for mouse events on Search Button
		searchBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				searchHouse(addressDropDown, searchValues, forSale);
			}
		});
		
		//Listen for mouse events on Advanced Search Button
		advancedSearchBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isAdvSearchPanelHidden == true) {
					advancedSettingPopup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
						
						@Override
						public void setPosition(int offsetWidth, int offsetHeight) {
							int left = searchSettingPanel.getAbsoluteLeft()
									+ searchSettingPanel.getOffsetWidth();
							int top = searchSettingPanel.getAbsoluteTop();
							advancedSettingPopup.setPopupPosition(left, top);
						}
					});
					isAdvSearchPanelHidden = false;
				}
				else {
					advancedSettingPopup.hide();
					isAdvSearchPanelHidden = true;
				}
			}
		});
	}

	/**
	 * Helper to buildSearchPanel().
	 * Adds search fields to given search setting panels.
	 * @param searchSettingPanel - panel to add in fields
	 * @param basicSearchCriteria - search criteria to add
	 * @param searchValues - list of search field text box
	 * @param forSale - list of 'for sale' radio buttons
	 * @param addressDropDown - Drop Down list of address
	 */
	private void buildSearchFields(FlowPanel searchSettingPanel,
			String[] basicSearchCriteria, List<TextBox> searchValues, 
			List<RadioButton> forSale, ListBox addressDropDown) {
		
		for (String criterion : basicSearchCriteria) {
			searchSettingPanel.add(new Label(criterion));

			if (criterion.endsWith("Value") || criterion.endsWith("Price")
					|| criterion.endsWith("Year")
					|| criterion.startsWith("Year")) {
				buildRangeBoxes(searchValues, searchSettingPanel);
			} 
			else if (criterion.equals("For Sale")) {
				buildForSale(forSale, searchSettingPanel);
			} 
			else if (criterion.equals("Address")) {
				buildAddressDropMenu(addressDropDown, searchSettingPanel);
			}
			else {
				buildRegularBoxes(searchValues, searchSettingPanel);
			}
		}
	}
	
	/**
	 * Helper to buildSearchField(). Adds address drop down menu.
	 * @param addressDropDown - address drop down to be constructed
	 * @param searchSettingPanel - panel to add address drop down
	 */
	private void buildAddressDropMenu(final ListBox addressDropDown,
			FlowPanel searchSettingPanel) {
		// If address list is empty, fetch from server
		if (addresses.isEmpty()) {
			if (houseDataSvc == null) {
				houseDataSvc = GWT.create(HouseDataService.class);
			}
			
			// Fetch address list from server
			AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}
	
				public void onSuccess(List<String> result) {
					addresses.add("");
					addresses.addAll(1, result);
					for (int i = 0; i < result.size(); i++) {
					      addressDropDown.addItem(addresses.get(i));				
					}
				}
			};
			houseDataSvc.getStreetNames(callback);
		}
		// Otherwise, build list from local store of addresses
		else {
			for (int i = 0; i < addresses.size(); i++) {
			      addressDropDown.addItem(addresses.get(i));				
			}
		}
		searchSettingPanel.add(addressDropDown);
	}

	/**
	 * Helper to buildSearchPanel(). Adds polygon selection tools.
	 * 
	 * @param searchSettingPanel
	 *            - panel to hold selection tool
	 */
	private void buildPolygonSelection(FlowPanel searchSettingPanel) {
		final DrawToolButton specifyRegionBtn = new DrawToolButton();
		final DrawToolButton clearPolygonBtn = new DrawToolButton();
		final Button editPolygonBtn = new Button();

		// Polygon settings
		specifyRegionBtn.setDrawImage();
		clearPolygonBtn.setEraseImage();
		editPolygonBtn.setText("Edit specified region");
		clearPolygonBtn.setEnabled(false);
		editPolygonBtn.setEnabled(false);

		// Listen for mouse events on specify region Button
		specifyRegionBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				data.theMap.setSpecifyingRegion(true);
				clearPolygonBtn.setEnabled(true);
				specifyRegionBtn.setEnabled(false);
				editPolygonBtn.setEnabled(true);
				// prompt user to click on a region on the map
				InfoWindowContent content;
				HTML htmlWidget = new HTML(
						"<p> Click on the map to specify region.</br> Drag corners to edit</p>");
				content = new InfoWindowContent(htmlWidget);
				data.theMap.getMap().getInfoWindow().open(vancouver, content);

			}
		});

		// Listen for mouse events on clear polygon Button
		clearPolygonBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// theMap.clearMap();
				data.theMap.clearSpecifiedRegion();
				specifyRegionBtn.setEnabled(true);
				clearPolygonBtn.setEnabled(false);
				editPolygonBtn.setEnabled(false);
			}
		});

		// Listen for mouse events on editPolygon Button
		editPolygonBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				data.theMap.editPolygon();
			}
		});

		// Add to setting panel
		searchSettingPanel.add(specifyRegionBtn);
		searchSettingPanel.add(clearPolygonBtn);
		searchSettingPanel.add(editPolygonBtn);
	}

	/**
	 * Helper to buildSearchPanel(). Builds and adds text boxes that represent a
	 * range of numbers. boxes by default get predefined labels "min" and "max"
	 * in their field.
	 * 
	 * @param searchValues
	 *            list of text boxes representing search field
	 * @param searchSettingPanel
	 *            FlowPanel that holds all the search boxes.
	 */
	private void buildRangeBoxes(List<TextBox> searchValues,
			FlowPanel searchSettingPanel) {
		TextBox[] rangeBox = { new TextBox(), new TextBox() };
		String[] labels = { "min", "max" };
		int i = 0;

		for (final TextBox box : rangeBox) {
			// add default style, add to panel and text box list
			box.addStyleDependentName("shorter");
			searchSettingPanel.add(box);
			searchValues.add(box);

			// add predefined text "min" and "max" colored in gray font color
			box.setText(labels[i]);
			box.addStyleDependentName("before");

			// when user clicks the text goes away and gray font color is
			// removed
			box.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					box.setText("");
					box.removeStyleDependentName("before");
				}
			});

			// To prevent array out of bounds error
			if (i < labels.length)
				i++;
		}
	}

	/**
	 * Helper to buildSearchPanel(). Creates non-range boxes and adds the box to
	 * the list of search boxes, and the flow panel that holds all the search
	 * fields.
	 * 
	 * @param searchValues
	 *            list of text boxes representing search field
	 * @param searchSettingPanel
	 *            FlowPanel that holds all the search boxes.
	 */
	private void buildRegularBoxes(List<TextBox> searchValues,
			FlowPanel searchSettingPanel) {
		TextBox tb = new TextBox();
		tb.addStyleDependentName("longer");
		searchValues.add(tb);
		searchSettingPanel.add(tb);
	}

	/**
	 * Helper to buildSearchPanel(). Creates radio buttons that specify the
	 * search criterion "For Sale", and adds the radio buttons to forSale list
	 * so that it will be passed to the search method. "All" criterion is
	 * selected by default.
	 * 
	 * @param forSale
	 *            list of Radio Buttons that define "for sale" criterion
	 * @param searchSettingPanel
	 *            FlowPanel that holds all the search boxes.
	 */
	private void buildForSale(List<RadioButton> forSale,
			FlowPanel searchSettingPanel) {
		// Labels that go next to the button
		String[] isSelling = { "Yes", "No", "All" };

		// Build the buttons
		for (String value : isSelling) {
			RadioButton rdBtn = new RadioButton("isSelling", value);
			searchSettingPanel.add(rdBtn);
			searchSettingPanel.add(new InlineHTML("&nbsp;&nbsp;"));
			forSale.add(rdBtn);
		}
		// All is selected by default
		forSale.get(isSelling.length - 1).setValue(true);
	}

	/**
	 * Gets user input from search tab, validates user input, makes asynchronous
	 * call to server-side search, stores search result into local store, and
	 * updates table with the search result.
	 */
	private void searchHouse(ListBox addressDropDown, List<TextBox> searchValues,
			List<RadioButton> forSale) {
		// Get user input into search boxes
		String[] userSearchInput = getUserSearchInput(addressDropDown, searchValues);

		// Validate user input
		if (!validateUserSearchInput(userSearchInput))
			return;

		// Get radio button (For Sale) response
		int isSelling = convertRadioBtnSearch(forSale);

		// Initialize the service proxy
		if (houseDataSvc == null) {
			houseDataSvc = GWT.create(HouseDataService.class);
		}

		// Set up the callback object
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}

			public void onSuccess(Void result) {
				houseTable.refreshTableFromBeginning();
			}
		};
		// Make the call to the house data service to search for data in the
		// server
		houseDataSvc.searchHouses(userSearchInput, isSelling, callback);
	}

	/**
	 * Helper to searchHouse(). Grabs the user's input into the search boxes.
	 * 
	 * @param addressDropDown - address drop-down list
	 * @param searchValues - list of search boxes
	 * @return array of user's search input into text boxes
	 */
	private String[] getUserSearchInput(ListBox addressDropDown, List<TextBox> searchValues) {
		// + 1 for adding address
		String[] userInput = new String[searchValues.size() + 2];			
				
		// Because civic number(street number) is already added, begin adding from index 1
		for (int i = 0; i < searchValues.size(); i++) {
			String temp = searchValues.get(i).getText().trim();			
			
			if (i == 0)
				userInput[i] = temp;
			if (i == 1) {
				int selectedAddrIndex = addressDropDown.getSelectedIndex();
				userInput[i] = addressDropDown.getValue(selectedAddrIndex);
			}
			
			else {
				// if user left min/max labels, then the criterion is empty
				if (temp.equals("min") || temp.equals("max")) 	temp = "";
				userInput[i] = temp;
			}
		}
		return userInput;
	}

	/**
	 * Helper to searchHouse(). Validates user's input into search boxes. If
	 * invalid, notifies the user
	 * 
	 * @param userSearchInput
	 *            list of user's input into search boxes
	 * @return boolean value representing if the inputs were all valid
	 */
	private boolean validateUserSearchInput(String[] userSearchInput) {
		boolean isOK = false;
		String numericAlert = "must be numbers only. No decimal is allowed.\n";
		String postalCodeAlert = "is not a valid postal code.\n";
		String invalidMsg = "";
		int i = 0;

		for (String criterion : searchCriteria) {
			if (criterion.endsWith("Value") || criterion.endsWith("Price")) {
				if (!userSearchInput[i].matches("\\d*")
						|| !userSearchInput[i + 1].matches("\\d*")) {
					invalidMsg = invalidMsg + criterion + numericAlert;
					isOK = false;
					i += 2;
				}
			}

			else if (criterion.equals("Postal Code")) {
				if (!userSearchInput[i]
						.matches("|[A-Z][0-9][A-Z][ ][0-9][A-Z][0-9]")) {
					invalidMsg = invalidMsg + criterion + postalCodeAlert;
					isOK = false;
					i++;
				}
			}

			else {
				isOK = true;
				i++;
			}
		}

		if (isOK == false) {
			Window.alert(invalidMsg);
		}

		return isOK;
	}

	/**
	 * Helper to searchHouse(). Converts user's "For Sale" criterion response
	 * into integer. 1 = yes; 0 = no; -1 = all; Assumption is that the given
	 * list of radio button has always 3 buttons.
	 * 
	 * @param forSale
	 *            list of radio buttons for "For Sale" criteria
	 * @return integer of response
	 */
	private int convertRadioBtnSearch(List<RadioButton> forSale) {
		int isSelling = -1;

		if (forSale.get(0).getValue() == true) {
			isSelling = 1;
		} 
		else if (forSale.get(1).getValue() == true) {
			isSelling = 0;
		} 
		else
			isSelling = -1;

		return isSelling;
	}
	
	
	/**
	 * Enables editing of a house data.
	 * Adds edit button to the table panel,
	 * builds dialog box where user can specify price and for-sale indicator.
	 */
	private void enableEdit() {
		final DialogBox editDialog = new DialogBox();
		Button editBtn = new Button("Edit");
		editDialog.setStyleName("editDialog");
		
		editBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (checkOnlyOneSelected()) {
					buildEditPanel(editDialog);
					editDialog.center();
					editDialog.show();
				}
				else
					Window.alert("Please select one house");
			}
		});
		tableWrapPanel.add(editBtn);
	}
	
	private boolean checkOnlyOneSelected() {
		return (selectedHouses != null && selectedHouses.size() == 1);
	}

	/**
	 * Helper to enableEdit().
	 * Builds contents of edit dialog.
	 * @param editDialog - dialog to add the contents in
	 */
	private void buildEditPanel(final DialogBox editDialog) {	
		FlowPanel editPanel = new FlowPanel();			
		Button okBtn = new Button("OK");
		Button cancelBtn = new Button("Cancel");
		final TextBox priceBox = new TextBox();
		final RadioButton yesSell = new RadioButton("editSell", "Yes");
		RadioButton noSell = new RadioButton("editSell", "No");
		noSell.setValue(true);
		
		editPanel.setStyleName("editPanel");
		priceBox.addStyleDependentName("shorter");
		
		for (HouseData house : selectedHouses) {
			editPanel.add(new Label("House to edit: " + house.getAddress()));
		}
		
		editPanel.add(new InlineHTML("<br /> Price: "));
		editPanel.add(priceBox);
		editPanel.add(new InlineHTML("<br /><br /> For Sale: "));
		editPanel.add(yesSell);
		editPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		editPanel.add(noSell);
		editPanel.add(new HTML("<br /><br /><br />"));
		editPanel.add(cancelBtn);
		editPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		editPanel.add(okBtn);
		
		okBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean isSelling = false;
				isSelling = yesSell.getValue();
				editHouse(priceBox.getValue(), isSelling);
				editDialog.clear();
				editDialog.hide();
			}
		});
		
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editDialog.clear();
				editDialog.hide();
			}
		});
		editDialog.setText("Edit a house");
		editDialog.setGlassEnabled(true);
		editDialog.setAnimationEnabled(true);		
		editDialog.setWidget(editPanel);
	}

	/**
	 * Actual editing function.
	 * Sends editing data to the server by an asynchronous call.
	 * After edit was successful, refreshes table.
	 * @param price - price of house that user specified.
	 * @param yesSelling - for-sale indicator that user specified.
	 */
	private void editHouse(String price, boolean yesSelling) {
		if (selectedHouses.size() == 1) {
			// Assemble edit field
			int housePrice = 0;
			String owner = loginInfo.getEmailAddress();
			String houseID ="";
			String postalCode = "";
			double latitude = 0;
			double longitude = 0;
			
			for (HouseData h : selectedHouses) {
				houseID = h.getHouseID();
				postalCode = h.getPostalCode();
				//This is an array of latitude and longitude. 
				// index 0 = latitude, index 1 = longitude 
				Double[] ll = data.theMap.getLL(h);
				
				if (ll != null) {
					latitude = ll[0];
					longitude = ll[1];
				}
			}			
			
			if (!price.isEmpty()) 
				housePrice = Integer.parseInt(price);
						
			// Initialize the service proxy
			if (houseDataSvc == null) {
				houseDataSvc = GWT.create(HouseDataService.class);
			}
	
			// Set up the callback object
			AsyncCallback<Void> callback = new AsyncCallback<Void>() {
				public void onFailure(Throwable caught) {
					Window.alert(caught.getMessage());
				}
	
				public void onSuccess(Void result) {
					houseTable.refreshTableCurrentView();
				}
			};
			houseDataSvc.updateHouse(owner, housePrice, yesSelling, 
					houseID, latitude, longitude, postalCode, callback);
		}
	}
	
	/**
	 * Resets database to the initial view.
	 */
	private void resetDatabase() {
		// Initialize the service proxy
		if (houseDataSvc == null) {
			houseDataSvc = GWT.create(HouseDataService.class);
		}

		// Set up the callback object
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				Window.alert(caught.getMessage());
			}
			public void onSuccess(Void result) {
			}
		};
		houseDataSvc.refreshIDStore(callback);
	}

}
