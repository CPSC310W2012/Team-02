package cpsc310.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Dialog box that holds edit panel.
 * The assumption is that type checking for selecting only one house
 * is done in the caller's class.
 */
public class EditPanel extends DialogBox {
	private HouseDataServiceAsync houseDataSvc = GWT.create(HouseDataService.class);	
	private HouseData selectedHouse;
	private LoginInfo loginInfo;
	private PropertyMap map;
	private HouseTable table;
	private final TextBox priceBox = new TextBox();
	private final RadioButton yesSell = new RadioButton("editSell", "Yes");
	private final RadioButton noSell = new RadioButton("editSell", "No");
	private Label errorMsg = new Label("");
	private FlowPanel loadingPanel = new FlowPanel();
	private boolean isOkToEdit = false;
	private boolean editDone = false;
	
	/**
	 * Constructor
	 * @param selectedHouse - currently selected house in the caller
	 * @param loginInfo - information about the user making edit request
	 * @param map - map in the caller class
	 * @param table - table in the caller class
	 */
	public EditPanel (HouseData selectedHouse, LoginInfo loginInfo, 
			PropertyMap map, HouseTable table) {
		FlowPanel editPanel = new FlowPanel();	
		
		this.setStyleName("editDialog");
		
		// Initialize class variables
		this.selectedHouse = selectedHouse;
		this.loginInfo = loginInfo;
		this.map = map;
		this.table = table;
		
		// Build dialog contents
		buildEditPanel(editPanel);
		
		// Assemble dialog
		this.setText("Edit a house");
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);		
		this.setWidget(editPanel);
	}

	/**
	 * Builds edit panel that has the form that user needs to fill in
	 * to provide edit information.
	 * 
	 * @param editPanel - panel to add all the edit form components
	 */
	private void buildEditPanel(FlowPanel editPanel) {
		Label houseName = new Label("");
		Button okBtn = new Button("OK");
		Button cancelBtn = new Button("Cancel");
		noSell.setValue(true);
				
		// Set styles of components
		editPanel.setStyleName("editPanel");
		priceBox.addStyleDependentName("shorter");
		errorMsg.addStyleDependentName("error");
		
		// Display the address of the house currently trying to edit
		houseName.setText("House to edit: " + selectedHouse.getAddress());
		
		// Build button behaviors
		buildOKBtn(okBtn);
		buildCancelBtn(cancelBtn);
		
		// Assemble edit panel
		editPanel.add(houseName);
		editPanel.add(new InlineHTML("<br />Price: "));
		editPanel.add(priceBox);
		editPanel.add(new InlineHTML("<br /><br />For Sale: "));
		editPanel.add(yesSell);
		editPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		editPanel.add(noSell);
		editPanel.add(new HTML("<br />"));
		editPanel.add(loadingPanel);
		editPanel.add(errorMsg);
		editPanel.add(new HTML("<br /><br /><br />"));
		editPanel.add(cancelBtn);
		editPanel.add(new InlineHTML("&nbsp;&nbsp;"));
		editPanel.add(okBtn);
	}

	/**
	 * Attach OK Button behavior.
	 * When OK button is clicked, button calls editHouse(), which invokes
	 * async call to the server. While server-side request is being processed,
	 * load image is displayed.
	 * 
	 * @param okBtn - button to attach ok button behavior
	 */
	private void buildOKBtn(Button okBtn) {
		okBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editHouse();
			}
		});
	}
	
	
	/**
	 * Attaches Cancel button behavior.
	 * A click on cancel button clears the dialog and closes.
	 * 
	 * @param cancelBtn - button to attach cancel button behavior
	 */
	private void buildCancelBtn(Button cancelBtn) {
		cancelBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				clear();
				hide();
			}
		});	
	}


	/**
	 * Actual editing function.
	 * Sends editing data to the server by an asynchronous call.
	 * After edit was successful, refreshes table, clears and closes the dialog.
	 * If async call was unsuccessful, it displays a message on the dialog. 
	 */
	private void editHouse() {
		
		// Check user specified price
		if (!checkHousePrice()) 
			return;
		
		// Assemble edit field
		int housePrice = 0;
		String owner = loginInfo.getEmailAddress();
		String houseID =selectedHouse.getHouseID();
		String postalCode = selectedHouse.getPostalCode();
		double latitude = 0;
		double longitude = 0;
		boolean yesSelling = yesSell.getValue();
		
		//This is an array of latitude and longitude. 
		// index 0 = latitude, index 1 = longitude 
		Double[] ll = map.getLL(selectedHouse);
		if (ll != null) {
			latitude = ll[0];
			longitude = ll[1];
		}
		
		// If user did not specify the price, price is 0.
		String price = priceBox.getValue();
		if (!price.isEmpty())
			housePrice = Integer.parseInt(price);
		
		// Draw loading panel
		drawLoading();
		
		// Initialize the service proxy
		if (houseDataSvc == null) {
			houseDataSvc = GWT.create(HouseDataService.class);
		}
	
		// Set up the callback object
		AsyncCallback<Void> callback = new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				loadingPanel.clear();
				errorMsg.setText(caught.getMessage());
			}
	
			public void onSuccess(Void result) {
				table.refreshTableCurrentView();
				clear();
				hide();
			}
		};
		houseDataSvc.updateHouse(owner, housePrice, yesSelling, 
				houseID, latitude, longitude, postalCode, callback);
	}
	
	/**
	 * Check if user specified price is in non-decimal numbers.
	 * If not, prompt the user to change the value.
	 * @return true if price is in non-decimal numbers; false otherwise.
	 */
	private boolean checkHousePrice() {
		String price = priceBox.getValue();
		
		if (!price.matches("\\d*")) {
			errorMsg.setText("Only non-decimal numbers are allowed for price.");
			priceBox.selectAll();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Draw loading panel to indicate async edit call to the 
	 * server is in process
	 */
	private void drawLoading() {
		Image loading = new Image("images/loading.png");
		Label loadingMsg = new Label("Editing...");
		loadingPanel.add(loading);
		loadingPanel.add(loadingMsg);
	}	
}
