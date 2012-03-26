package cpsc310.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.FlowPanel;
import com.reveregroup.gwt.facebook4gwt.Facebook;
import com.reveregroup.gwt.facebook4gwt.LoginButton;
import com.reveregroup.gwt.facebook4gwt.LoginButton.Background;
import com.reveregroup.gwt.facebook4gwt.LoginButton.Length;
import com.reveregroup.gwt.facebook4gwt.LoginButton.Size;
import com.reveregroup.gwt.facebook4gwt.ShareButton;

/**
 * Main EntryPoint class. UI is built, client-side request is handled.
 */
public class Team_02 implements EntryPoint {
	private HouseTable houseTable = HouseTable.createHouseTable();	
	private LatLng vancouver = LatLng.newInstance(49.264448, -123.185844);
	private PropertyMap theMap = new PropertyMap(vancouver);
	private boolean isSidePanelHidden = false;
	private boolean isTablePanelHidden = false;
	private HouseDataServiceAsync houseDataSvc = GWT
			.create(HouseDataService.class);
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private LoginInfo loginInfo = null;
	private boolean isLoginServiceAvailable = false;
	private Set<HouseData> selectedHouses = null;
    
	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);	
	private MapContainerPanel mapPanel = new MapContainerPanel(theMap);
	private FlowPanel sidePanel = new FlowPanel();
	private SearchPanel searchPanel = new SearchPanel(theMap, houseTable);
	private DockLayoutPanel tableWrapPanel = new DockLayoutPanel(Unit.PX);
	
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
						loadURLSearch();
					}

					public void onSuccess(LoginInfo result) {
						loginInfo = result;
						isLoginServiceAvailable = true;
						resetDatabase();
						buildUI();
						loadURLSearch();
					}
				});
	}

	/**
	 * Builds application's main UI
	 */
	private void buildUI() {

		// Initialize selection model for map and table
		initSelection();

		// Make main panel fill the browser
		mainPanel.setHeight(Window.getClientHeight() + "px");

		// Make sidePanel
		buildSidePanel(sidePanel);
		mainPanel.addWest(sidePanel, 230);

		// Make tablePanel
		buildTablePanel(tableWrapPanel);
		mainPanel.addSouth(tableWrapPanel, 300);

		// Make mapContainerPanel
		mainPanel.add(mapPanel);

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
							theMap.clearMarkers();
							return;
						}
						// clear map markers before proceeding to add new point
						theMap.clearMarkers();
						// add marker onto map
						for (HouseData house : selectedHouses)
							theMap.findLocation(house, true);
					}
				});

		// Attach selection model to table to enable synchronous selection
		// between map and table.
		houseTable.enableSelection(selectionModel);
	}

	/**
	 * Helper to buildUI(). Assembles table panel.
	 * 
	 * @param tableWrapPanel
	 *            - flow panel to hold table related elements
	 */
	private void buildTablePanel(DockLayoutPanel tableWrapPanel) {
		FlowPanel buttonPanel = new FlowPanel();
		ScrollPanel tablePanel = new ScrollPanel(houseTable.getHouseTable());
		FlowPanel pagerPanel = new FlowPanel();
		Button hideShowTablePanelButton = new Button("-");
		Button expandShrinkTableBtn = new Button("Expand table");
		SimplePager simplePager = new SimplePager();

		// Set styles of edit panel & edit panel's components
		expandShrinkTableBtn.setStyleName("gwt-Button-textButton");
		simplePager.setStylePrimaryName("pager");
		pagerPanel.setStylePrimaryName("pagerPanel");		
		tablePanel.setStyleName("tablePanel");
		buttonPanel.setStyleName("buttonPanel");
		tableWrapPanel.setStyleName("tableWrapPanel");
		
		// Create hide/show button for table panel
		buildTablePanelButton(hideShowTablePanelButton);
		
		// Assemble button panel 
		buttonPanel.add(hideShowTablePanelButton);
		buttonPanel.add(expandShrinkTableBtn);
				
		// Enable edit function only if login service is available AND
		// the user is logged in.
		if (isLoginServiceAvailable == true && loginInfo.isLoggedIn()) {
			enableEdit(buttonPanel);
		}		
		
		// Attach pager to table
		simplePager.setDisplay(houseTable.getHouseTable());
		pagerPanel.add(simplePager);
				
		// Assemble table panel
		tableWrapPanel.addNorth(buttonPanel, 20);
		tableWrapPanel.addSouth(pagerPanel, 30);
		tableWrapPanel.add(tablePanel);
	}

	/**
	 * Helper to buildTablePanel(). Create hide/show behavior to table panel
	 * button.
	 * 
	 * @param hideShowTablePanelButton
	 *            - button to behave lie hide/show button
	 */
	private void buildTablePanelButton(final Button hideShowTablePanelButton) {
		hideShowTablePanelButton.setStyleName("hideShowButton");
		hideShowTablePanelButton.addStyleDependentName("horizontal");
		hideShowTablePanelButton.setTitle("Minimize");

		hideShowTablePanelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!isTablePanelHidden) {
					isTablePanelHidden = true;
					hideShowTablePanelButton.setText("+");
					hideShowTablePanelButton.setTitle("Unminimize");
					tableWrapPanel.addStyleDependentName("collapsed");
					mainPanel.setWidgetSize(tableWrapPanel, 20);
					mainPanel.animate(300);							
				} else {
					isTablePanelHidden = false;
					hideShowTablePanelButton.setText("-");
					hideShowTablePanelButton.setTitle("Minimize");
					mainPanel.setWidgetSize(tableWrapPanel, 300);			
					mainPanel.animate(300);
					tableWrapPanel.removeStyleDependentName("collapsed");					
				}
			}
		});
	}

	/**
	 * Helper to buildUI(). 
	 * Assemble side panel which holds header, menu, 
	 * facebook panel, search panel, footer.
	 * 
	 * @param sidePanel
	 *            flow panel to hold side panel contents
	 */
	private void buildSidePanel(FlowPanel sidePanel) {
		Button hideShowSidePanelButton = new Button("-");
		TabLayoutPanel sidebarTabPanel = new TabLayoutPanel(25, Unit.PX);
		FlowPanel menuPanel = new FlowPanel();
		
		sidePanel.setStyleName("sidePanel");

		// Create hide/show ability into the button
		buildSidePanelButton(hideShowSidePanelButton);

		// Assemble menu panel
		buildMenuPanel(menuPanel);

		// Assemble GWT widgets to occupy side panel
		buildSideTabPanel(sidebarTabPanel);

		// Assemble side panel
		sidePanel.add(new HTML(
				"<div id ='header'><h1>iVanHomesPrices</h1></div>"));
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
		hideShowSidePanelButton.setTitle("Minimize");
		
		hideShowSidePanelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (!isSidePanelHidden) {
					isSidePanelHidden = true;
					hideShowSidePanelButton.setText("+");
					hideShowSidePanelButton.setTitle("Unminimize");
					mainPanel.setWidgetSize(sidePanel, 20);
					mainPanel.animate(300);
					sidePanel.addStyleDependentName("collapsed");
					
				} else {
					isSidePanelHidden = false;
					hideShowSidePanelButton.setText("-");
					hideShowSidePanelButton.setTitle("Minimize");
					sidePanel.removeStyleDependentName("collapsed");
					mainPanel.setWidgetSize(sidePanel, 230);		
					mainPanel.animate(300);
				}
			}
		});
	}

	/**
	 * Helper to buildSidePanel().
	 * Builds menu which holds login, help, terms of use,
	 * and facebook.
	 * 
	 * @param menuPanel - menu panel to add login, help, terms of use,
	 * and facebook.
	 */
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
		faceBookTemp.setStyleName("facebookPanel");
		Facebook.init("257432264338889");
		LoginButton faceBookBtn = new LoginButton(true, Size.SMALL, Background.LIGHT, Length.SHORT);
		ShareButton shareBtn = new ShareButton(GWT.getHostPageBaseURL(),"Check out this house!!!");
		faceBookTemp.add(faceBookBtn);
		faceBookTemp.add(shareBtn);
		faceBookTemp.add(new InlineHTML("<iframe src=\"//www.facebook.com/plugins/like.php?href=http%3A%2F%2Frmar3a01.appspot.com%2F&amp;send=false&amp;layout=button_count&amp;width=90&amp;show_faces=false&amp;action=like&amp;colorscheme=light&amp;font&amp;height=21&amp;appId=257432264338889\" scrolling=\"no\" frameborder=\"0\" style=\"border:none; overflow:hidden; width:90px; height:21px;\" allowTransparency=\"true\"></iframe>"));
		
		menuPanel.add(new InlineHTML("&nbsp;&nbsp;|&nbsp;&nbsp;"));
		menuPanel.add(helpBtn);
		menuPanel.add(new InlineHTML("&nbsp;&nbsp;|&nbsp;&nbsp;"));
		menuPanel.add(termsBtn);
		menuPanel.add(new HTML("<hr>"));
		menuPanel.add(faceBookTemp);
	}

	/**
	 * Helper to buildMenuPanel(). Adds login/logout links
	 * 
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
			} else {
				logoutLink.setVisible(false);
				logoutLink.setVisible(false);
			}
		}
		
	}

	/**
	 * Helper to buildSidePanel() Assembles GWT widgets that needs to be
	 * included in the sidePanel.
	 * 
	 * @param sidebarTabPanel
	 *            - tab panel to wrap the widgets
	 */
	private void buildSideTabPanel(TabLayoutPanel sidebarTabPanel) {
		
		// Add Widgets to the tab panel
		sidebarTabPanel.add(searchPanel, "Search");
		
		// Set details of tab panel look
		sidebarTabPanel.setAnimationDuration(100);
		sidebarTabPanel.addStyleDependentName("sideTabPanel");
		
		// If user is logged in, assemble user info panel and add it to the tab
		if (isLoginServiceAvailable == true) {
			UserInfoPanel userInfoPanel = new UserInfoPanel(loginInfo);
			
			if (loginInfo.isLoggedIn()) {
				sidebarTabPanel.add(userInfoPanel, "My Account");
			}
			else {
				if (sidebarTabPanel.getWidgetCount() > 1) {
					userInfoPanel.clear();
					sidebarTabPanel.remove(1);
				}
			}
		}
	}
	
	/**
	 * Enables editing of a house data.
	 * Adds edit button to the table panel,
	 * builds dialog box where user can specify price and for-sale indicator.
	 * @param buttonPanel - panel that holds edit button
	 */
	private void enableEdit(FlowPanel buttonPanel) {
		Button editBtn = new Button("Edit");
		Button removeBtn = new Button("Remove");
		
		// Set buttton's tooltip contents
		editBtn.setTitle("Edit house information");
		removeBtn.setTitle("Remove information from selected house");
		
		// Set button styles
		editBtn.setStyleName("gwt-Button-textButton");
		removeBtn.setStyleName("gwt-Button-textButton");

		// Add buttons to the button panel
		buttonPanel.add(editBtn);
		buttonPanel.add(removeBtn);
		
		// Add edit button handler
		editBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				HouseData selectedHouse = checkAndGetSelectedHouse();
				EditPanel editDialog = 
						new EditPanel(selectedHouse, loginInfo, theMap, houseTable);
				editDialog.center();
				editDialog.show();					
			}
		});
	}
	
	/**
	 * Helper to enableEdit().
	 * Checks if the number of currently selected houses is one.
	 * If it is one, return that house. If not, warn the user and return null.
	 *  
	 * @return HouseData object that is currently selected by the user
	 */
	private HouseData checkAndGetSelectedHouse() {
		if (selectedHouses != null && 
				selectedHouses.size() == 1) {
			for (HouseData house : selectedHouses)
				return house;
		}
		Window.alert("Please select one house");
		return null;
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

	/**
	 * Method to search for a house based on the URL parameters.  Searches for
	 * the parameters cn (civic number) and sn (street name).
	 * @pre (Window.Location.getParameter("cn") != null) &&
	 * 		(Window.Location.getParameter("sn") != null)
	 * @post if house exists, house is displayed on the application
	 * 
	 * Note: Spaces in the URL must be "+" or "%20"
	 */
	private void loadURLSearch() {
		//acquire parameters from the URL
		String civicNumber = Window.Location.getParameter("cn");
		String streetName = Window.Location.getParameter("sn");

		//only search for house if the street number and address are given
		if(civicNumber != null && streetName != null) {	

			//create String[] to pass to the searchHouses function
			String[] urlParameters = new String[20];
			urlParameters[0] = civicNumber;
			urlParameters[1] = streetName;
			//fill rest of array with blanks i.e. ""
			for(int i = 2; i < 20; i++) {
				urlParameters[i] = "";
			}
			
			//set isSelling to -1 (all) since using URL means that user is looking
			//for the house regardless of the selling status of the house
			int isSelling = -1;
				
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
			// Make the call to the house data service to search for the house in the server
			houseDataSvc.searchHouses(urlParameters, isSelling, callback);
		}
	}
}

