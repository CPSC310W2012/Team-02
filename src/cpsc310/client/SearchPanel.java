package cpsc310.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Search Panel with all text boxes for search fields, Search Button to send
 * search. This class fires asynchronous search call to the server.
 */
public class SearchPanel extends FlowPanel {
	private PropertyMap map;
	private HouseTable table;
	private boolean isAdvSearchPanelHidden = true;
	private PopupPanel errorPopup = new PopupPanel();
	private Label errorMsg = new Label("");
	private final List<TextBox> searchValues = new ArrayList<TextBox>();
	private final List<RadioButton> forSale = new ArrayList<RadioButton>(3);
	private final ListBox addressDropDown = new ListBox(false);
	private List<String> addresses = new ArrayList<String>();
	private HouseDataServiceAsync houseDataSvc = GWT
			.create(HouseDataService.class);
	private LatLng vancouver = LatLng.newInstance(49.264448, -123.185844);
	final String[] searchCriteria = { "Street Number", "Address",
			"Postal Code", "Current Land Value", "Current Improvement Value",
			"Assessment Year", "Previous Land Value",
			"Previous Improvement Value", "Year Built", "Big Improvement Year",
			"Price", "Realtor", "For Sale" };

	/**
	 * Constructor
	 * 
	 * @param map
	 *            - map to specify polygon selection on. This ensures that this
	 *            class modifies the same instance of map as its caller class.
	 * @param table
	 *            - table that appears in caller's class
	 */
	public SearchPanel(PropertyMap map, HouseTable table) {
		final FlowPanel searchSettingPanel = new FlowPanel();
		final PopupPanel advancedSettingPopup = new PopupPanel(false);
		final FlowPanel advancedSettingPanel = new FlowPanel();
		final FlowPanel polygonSettingPanel = new FlowPanel();
		final Button advancedSearchBtn = new Button("Advanced Search >>");
		final Button searchBtn = new Button("Search");
		final Button resetSearchBtn = new Button("Reset");

		// Attach caller's map and table
		this.map = map;
		this.table = table;

		// Set the style name of search panel
		this.setStyleName("searchPanel");

		// Build search panel components
		buildErrorPopup();
		buildBasicSearchPanel(searchSettingPanel);
		buildAdvancedSearchPanel(advancedSettingPopup, advancedSettingPanel);
		buildSearchFields(searchSettingPanel, advancedSettingPanel);

		// Build polygon selection
		buildPolygonSelection(polygonSettingPanel);

		// Build search buttons
		buildAdvancedSearchBtn(advancedSearchBtn, advancedSettingPopup,
				searchSettingPanel);
		buildResetSearchBtn (resetSearchBtn);
		buildSearchBtn(searchBtn);

		// Add searchSettingPanel and searchBtn to the searchPanel
		this.add(polygonSettingPanel);
		this.add(new HTML("<hr>"));
		this.add(resetSearchBtn);
		this.add(searchSettingPanel);
		this.add(new HTML("<br />"));
		this.add(advancedSearchBtn);
		this.add(new HTML("<br />"));
		this.add(searchBtn);

	}

	/**
	 * Build reset search button which clears all the text boxes,
	 * resets address drop down box, and returns
	 * radio button to default settings
	 * 
	 * @param resetSearchBtn - button to add reset behavior
	 */
	private void buildResetSearchBtn(Button resetSearchBtn) {
		// Attach a style name
		resetSearchBtn.setStyleName("gwt-Button-textButton");
		resetSearchBtn.addStyleDependentName("resetSearch");
		
		resetSearchBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick (ClickEvent event){
				// Prevent null pointer error
				if (!searchValues.isEmpty()) {
					for (TextBox box : searchValues) {
						box.setText("");
					}
				}
				// Prevent null pointer error
				if (addressDropDown.getItemCount() > 0)
					addressDropDown.setSelectedIndex(0);
				// Prevent null pointer error
				if (!forSale.isEmpty())
					forSale.get(forSale.size() - 1).setValue(true);
			}
		});
	}
	

	/**
	 * Build search panel with basic search settings defined by
	 * basicSerachCriteria.
	 * 
	 * @param searchSettingPanel
	 *            - flow panel to add all the search boxes and their labels
	 * 
	 */
	private void buildBasicSearchPanel(FlowPanel searchSettingPanel) {
		// Set the style name
		searchSettingPanel.setStyleName("searchSettingPanel");
	}

	/**
	 * Build search panel with advanced settings defined by advanced search
	 * criteria. Advanced search panel is a popup panel which appears when
	 * advanced search button is clicked.
	 * 
	 * @param advancedSettingPopup
	 *            - popup that wraps advanced setting panel
	 * @param advancedSettingPanel
	 *            - panel to add all the advanced search settings
	 */
	private void buildAdvancedSearchPanel(PopupPanel advancedSettingPopup,
			FlowPanel advancedSettingPanel) {

		// Set the style name of search panel components
		advancedSettingPopup.setStyleName("advancedSettingPopup");

		// Assemble search panel
		advancedSettingPopup.setAnimationEnabled(true);
		advancedSettingPopup.setWidget(advancedSettingPanel);

	}

	/**
	 * Constructs error popup.
	 */
	private void buildErrorPopup() {
		// Set error message style
		errorMsg.addStyleDependentName("error");
		errorPopup.setStyleName("errorPopup");

		errorPopup.add(errorMsg);
	}

	/**
	 * Builds polygon selection tools.
	 * 
	 * @param polygonSettingPanel
	 *            - panel to hold selection tool
	 */
	private void buildPolygonSelection(FlowPanel polygonSettingPanel) {
		final DrawToolButton specifyRegionBtn = new DrawToolButton();
		final DrawToolButton clearPolygonBtn = new DrawToolButton();

		polygonSettingPanel.setStyleName("polygonSettingPanel");

		// Polygon settings
		specifyRegionBtn.setDrawImage();
		specifyRegionBtn.setWidth("20px");
		clearPolygonBtn.setEraseImage();
		clearPolygonBtn.setWidth("20px");
		clearPolygonBtn.setEnabled(false);

		// Listen for mouse events on specify region Button
		specifyRegionBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				map.setSpecifyingRegion(true);
				clearPolygonBtn.setEnabled(true);
				specifyRegionBtn.setEnabled(false);
				// prompt user to click on a region on the map
				InfoWindowContent content;
				HTML htmlWidget = new HTML(
						"<p> Click on the map to specify region.</br> Drag corners to edit</p>");
				content = new InfoWindowContent(htmlWidget);
				map.getMap().getInfoWindow().open(vancouver, content);

			}
		});

		// Listen for mouse events on clear polygon Button
		clearPolygonBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// map.clearMap();
				map.clearSpecifiedRegion();
				specifyRegionBtn.setEnabled(true);
				clearPolygonBtn.setEnabled(false);
			}
		});

		// Add to setting panel
		polygonSettingPanel
				.add(new Label("Draw and select an area on the map"));
		polygonSettingPanel.add(specifyRegionBtn);
		polygonSettingPanel.add(clearPolygonBtn);
		polygonSettingPanel.add(new InlineHTML("&nbsp;&nbsp;"));
	}

	/**
	 * Attach click handler to the advanced search button
	 * 
	 * @param advancedSearchBtn
	 *            - button to attach advanced search button behavior
	 * @param advancedSettingPopup
	 *            - popup panel to invoke when button is clicked
	 * @param basicSearchPanel
	 *            - basic search panel
	 */
	private void buildAdvancedSearchBtn(final Button advancedSearchBtn,
			final PopupPanel advancedSettingPopup,
			final FlowPanel basicSearchPanel) {
		// Attach style
		advancedSearchBtn.setStyleName("gwt-Button-textButton");

		// Add click handler
		advancedSearchBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (isAdvSearchPanelHidden == true) {
					advancedSearchBtn.setText("Advanced Search <<");
					advancedSettingPopup
							.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

								@Override
								public void setPosition(int offsetWidth,
										int offsetHeight) {
									int left = basicSearchPanel
											.getAbsoluteLeft()
											+ basicSearchPanel.getOffsetWidth();
									int top = basicSearchPanel.getAbsoluteTop();
									advancedSettingPopup.setPopupPosition(left,
											top);
									advancedSettingPopup.show();
								}
							});
					isAdvSearchPanelHidden = false;
				} else {
					advancedSearchBtn.setText("Advanced Search >>");
					advancedSettingPopup.hide();
					if (errorPopup.isVisible()) {
						errorPopup.clear();
						errorPopup.hide();
					}
					isAdvSearchPanelHidden = true;
				}
			}
		});

	}

	/**
	 * Attach click handler to the search button
	 * 
	 * @param searchBtn
	 *            - search button to attach searchHouse method to the click
	 *            event
	 */
	private void buildSearchBtn(Button searchBtn) {
		// Listen for mouse events on Search Button
		searchBtn.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				searchHouse(addressDropDown, searchValues, forSale);
			}
		});
	}

	/**
	 * Builds search fields to given search setting panels.
	 * 
	 * @param basicPanel
	 *            - basic panel to add basic search criteria
	 * @param advancedPanel
	 *            - advanced panel to add advanced search criteria
	 */
	private void buildSearchFields(FlowPanel basicPanel, FlowPanel advancedPanel) {
		final String basicSearchCriteria = "Street Number, Address, Current Land Value, Price, Realtor, For Sale";
		final String advancedSearchCriteria = "Postal Code, Current Improvement Value, Assessment Year, Previous Land Value, Previous Improvement Value,Year Built, Big Improvement Year";
		FlowPanel settingPanel;

		for (String criterion : searchCriteria) {
			if (basicSearchCriteria.contains(criterion))
				settingPanel = basicPanel;
			else if (advancedSearchCriteria.contains(criterion))
				settingPanel = advancedPanel;
			else
				throw new NullPointerException();

			settingPanel.add(new Label(criterion));

			if (criterion.endsWith("Value") || criterion.endsWith("Price")
					|| criterion.endsWith("Year")
					|| criterion.startsWith("Year")) {
				buildRangeBoxes(searchValues, settingPanel, criterion);
			} else if (criterion.equals("For Sale")) {
				buildForSale(settingPanel);
			} else if (criterion.equals("Address")) {
				buildAddressDropMenu(settingPanel);
			} else {
				buildRegularBoxes(searchValues, settingPanel, criterion);
			}
		}
	}

	/**
	 * Adds address drop down menu.
	 * 
	 * @param searchSettingPanel
	 *            - panel to add drop down menu
	 */
	private void buildAddressDropMenu(FlowPanel searchSettingPanel) {
		// If address list is empty, fetch from server
		if (addresses.isEmpty()) {
			if (houseDataSvc == null) {
				houseDataSvc = GWT.create(HouseDataService.class);
			}

			// Fetch address list from server
			AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
				public void onFailure(Throwable caught) {
					addresses.add("");
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
	 * Build and add text boxes that represent a range of numbers. Boxes by
	 * default get predefined labels "min" and "max" in their field.
	 * 
	 * @param searchValues
	 *            list of text boxes representing search field
	 * @param searchSettingPanel
	 *            FlowPanel that holds all the search boxes.
	 * @param type
	 *            typeValue so Validation method know what type of validation to
	 *            do.
	 */
	private void buildRangeBoxes(List<TextBox> searchValues,
			FlowPanel searchSettingPanel, String type) {
		TextBox[] rangeBox = { new TextBox(), new TextBox() };
		String[] labels = { "min", "max" };
		int i = 0;

		for (final TextBox box : rangeBox) {
			// add default style, add to panel and text box list
			box.addStyleDependentName("shorter");
			searchSettingPanel.add(box);
			searchValues.add(box);

			// Add blur handler for type checking
			addBlurHandler(box, type);

			// add predefined text "min" and "max" colored in gray font color
			box.setText(labels[i]);
			box.addStyleDependentName("before");

			// when user clicks the text goes away and gray font color is
			// removed
			box.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					if (box.getStyleName().contains("before")) { 
						box.setText("");
						box.removeStyleDependentName("before");
					}
				}
			});

			// To prevent array out of bounds error
			if (i < labels.length)
				i++;
		}
	}

	/**
	 * Create non-range boxes and adds the box to the list of search boxes, and
	 * the flow panel that holds all the search fields.
	 * 
	 * @param searchValues
	 *            list of text boxes representing search field
	 * @param searchSettingPanel
	 *            FlowPanel that holds all the search boxes.
	 * @param type
	 *            typeValue so Validation method know what type of validation to
	 *            do.
	 */
	private void buildRegularBoxes(List<TextBox> searchValues,
			FlowPanel searchSettingPanel, String type) {
		TextBox tb = new TextBox();
		tb.addStyleDependentName("longer");
		searchValues.add(tb);
		searchSettingPanel.add(tb);

		// Add blur handler for type checking
		addBlurHandler(tb, type);
	}

	/**
	 * Create radio buttons that specify the search criterion "For Sale", and
	 * add the radio buttons to forSale list so that it will be passed to the
	 * search method. "All" criterion is selected by default.
	 * 
	 * @param searchSettingPanel
	 *            - panel to add "For Sale" radio buttons
	 */
	private void buildForSale(FlowPanel searchSettingPanel) {
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
	 * Attaches blur handler to given text box which fires input validation when
	 * text box loses focus. Expected to be called by text box creating methods.
	 * 
	 * @param tb
	 *            - text box to add blur handler
	 * @param type
	 *            -Criterion
	 */
	private void addBlurHandler(final TextBox tb, final String type) {
		tb.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				String input = tb.getValue().trim();
				if (!validateIndivSearchInput(type, input)) {
					errorPopup.showRelativeTo(tb);
					tb.selectAll();
				}
				else {
					if (errorPopup.isVisible()) {
						errorPopup.hide();
					}
				}
			}
		});
	}

	/**
	 * Gets user input from search tab, validates user input, makes asynchronous
	 * call to server-side search, stores search result into local store, and
	 * updates table with the search result.
	 * 
	 * @param addressDropDown
	 *            - address drop-down menu
	 * @param searchValues
	 *            - list of boxes that hold search values
	 * @param forSale
	 *            - for sale radio buttons
	 */
	private void searchHouse(ListBox addressDropDown,
			List<TextBox> searchValues, List<RadioButton> forSale) {
		// Get user input into search boxes
		String[] userSearchInput = getUserSearchInput(addressDropDown,
				searchValues);

		// Validate user input
		if (!validateUserSearchForm(userSearchInput))
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
				table.refreshTableFromBeginning();
			}
		};
		// Make the call to the house data service to search for data in the
		// server
		houseDataSvc.searchHouses(userSearchInput, isSelling, callback);
	}

	/**
	 * Helper to searchHouse(). Grabs the user's input into the search boxes.
	 * 
	 * @param addressDropDown
	 *            - address drop-down list
	 * @param searchValues
	 *            - list of search boxes
	 * @return array of user's search input into text boxes
	 */
	private String[] getUserSearchInput(ListBox addressDropDown,
			List<TextBox> searchValues) {
		// + 1 for adding address
		String[] userInput = new String[searchValues.size() + 2];

		// Because civic number(street number) is already added, begin adding
		// from index 1
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
				if (temp.equals("min") || temp.equals("max"))
					temp = "";
				userInput[i] = temp;
			}
		}
		return userInput;
	}

	/**
	 * Helper to searchHouse(). Validates user's input into search boxes. If
	 * invalid, notifies the user.
	 * 
	 * @param userSearchInput
	 *            list of user's input into search boxes
	 * @return boolean value representing if the inputs were all valid
	 */
	private boolean validateUserSearchForm(String[] userSearchInput) {
		boolean isOK = false;
		String numericAlert = " must be numbers only. No decimal is allowed.\n";
		String postalCodeAlert = " is not a valid postal code.\n";
		String invalidMsg = "";
		int i = 0;

		for (String criterion : searchCriteria) {
			if (criterion.endsWith("Value") || criterion.endsWith("Price")
					|| criterion.endsWith("Number")	|| criterion.endsWith("Year")) {
				isOK = validateIndivSearchInput(criterion, userSearchInput[i]);
				isOK = validateIndivSearchInput(criterion,
						userSearchInput[i + 1]);
				i += 2;
				if (!isOK)
					invalidMsg = invalidMsg + criterion + numericAlert;
			}

			else if (criterion.equals("Postal Code")) {
				isOK = validateIndivSearchInput(criterion, userSearchInput[i]);
				i++;
				if (!isOK)
					invalidMsg = invalidMsg + criterion + postalCodeAlert;
			}

			else {
				isOK = true;
				invalidMsg = "";
				i++;
			}
		}

		if (isOK == false) {
			errorMsg.setText(invalidMsg);
		}

		return isOK;
	}

	/**
	 * For checking individual text boxes
	 * 
	 * @param criterion
	 *            - search criterion of given text box
	 * @param userInput
	 *            - single user input
	 * @return boolean value representing if the inputs were all valid
	 */
	private boolean validateIndivSearchInput(String criterion, String userInput) {
		String numericAlert = " must be non-decimal numbers only.\n";
		String postalCodeAlert = " is not a valid postal code.\n";
		String invalidMsg = "";
		boolean isOK = false;

		if (criterion.endsWith("Value") || criterion.endsWith("Price")
				|| criterion.endsWith("Number") || criterion.endsWith("Year")) {
			if (userInput.matches("^\\d*$")) {
				isOK = true;
			} else {
				invalidMsg = criterion + numericAlert;
			}
		} else if (criterion.equals("Postal Code")) {
			if (userInput.matches("|^[ABCEGHJKLMNPRSTVXY|abceghjklmnprstvxy]{1}\\d{1}[A-z]{1} *\\d{1}[A-z]{1}\\d{1}$")) {
				isOK = true;
			} else {
				invalidMsg = criterion + postalCodeAlert;
			}
		} else {
			isOK = true;
		}
		if (!isOK) {
			errorMsg.setText(invalidMsg);
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
	 * @return integer of response (1 = yes; 0 = no; -1 = all)
	 */
	private int convertRadioBtnSearch(List<RadioButton> forSale) {
		int isSelling = -1;

		if (forSale.get(0).getValue() == true) {
			isSelling = 1;
		} else if (forSale.get(1).getValue() == true) {
			isSelling = 0;
		} else
			isSelling = -1;

		return isSelling;
	}

}
