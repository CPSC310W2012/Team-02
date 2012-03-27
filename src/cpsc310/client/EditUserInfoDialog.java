package cpsc310.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Dialog that allows user to edit his/her information
 *
 */
public class EditUserInfoDialog extends DialogBox{
	private int MAXKEYCOUNT = 200;
	private Label errorMsg = new Label("");
	private int keyCount = 0;
	
	/**
	 * Constructor
	 * @param loginInfo - LoginInfo class instance that has user's information
	 */
	public EditUserInfoDialog(LoginInfo loginInfo) {
		FlowPanel contentWrap = new FlowPanel();
		
		this.setStyleName("editDialog");
		
		// Build dialog content
		buildContent(contentWrap);
		
		// Assemble dialog
		this.setText("Change my information");
		this.setGlassEnabled(true);
		this.setAnimationEnabled(true);		
		this.setWidget(contentWrap);		
	}
	
	/**
	 * Build dialog content
	 * @param contentWrap - panel that wraps the contents of dialog
	 */
	private void buildContent(FlowPanel contentWrap) {
		TextBox phoneNumberBox = new TextBox(); 
		TextBox websiteBox = new TextBox();
		TextArea descArea = new TextArea();
		Button okBtn = new Button("OK");
		Button cancelBtn = new Button("Cancel");
		
		// Set style of components
		contentWrap.setStyleName("editPanel");
		phoneNumberBox.addStyleDependentName("longer");
		websiteBox.addStyleDependentName("longer");
		errorMsg.addStyleDependentName("error");
		
		// Build components
		buildDescArea(descArea);
		buildPhoneNumberBox(phoneNumberBox);
		buildOKBtn(okBtn);
		buildCancelBtn(cancelBtn);
		
		// Assemble content
		contentWrap.add(new Label("Phone # (without - or space): "));
		contentWrap.add(phoneNumberBox);
		contentWrap.add(new Label("Website: "));
		contentWrap.add(websiteBox);
		contentWrap.add(new Label("Description about yourself (max 200 char): "));
		contentWrap.add(new InlineHTML("<br>"));
		contentWrap.add(descArea);
		contentWrap.add(new HTML("<br>"));
		contentWrap.add(errorMsg);
		contentWrap.add(cancelBtn);
		contentWrap.add(new InlineHTML("&nbsp;&nbsp;"));
		contentWrap.add(okBtn);
	}
	
	/**
	 * Add blur handler to the phone number box for type checking
	 * @param phoneNumberBox - phone number box to add this behavior
	 */
	private void buildPhoneNumberBox(final TextBox phoneNumberBox) {
		phoneNumberBox.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				String phoneNum = phoneNumberBox.getText().trim();
				if (!phoneNum.matches("|\\d{7}")) {
					errorMsg.setText("Phone number must be a 7-digit number only.");
					phoneNumberBox.selectAll();
				}
				else {
					if (errorMsg.getText().length() > 0)
						errorMsg.setText("");
				}
			}
			
		});
		
	}

	/**
	 * Build details of description text area.
	 * Limit user's input into 200 characters
	 * @param descArea - text area to apply constraints
	 */
	private void buildDescArea(final TextArea descArea) {
		descArea.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (descArea.getText().length() > MAXKEYCOUNT)
					errorMsg.setText("Description can't be more than 200 characters.");
				else {
					if (errorMsg.getText().length() > 0)
						errorMsg.setText("");
				}
			}
		});
	}
	

	/**
	 * Attach OK Button behavior.
	 * When OK button is clicked, button calls editUserInfo(), which invokes
	 * async call to the server.
	 * 
	 * @param okBtn - button to attach ok button behavior
	 */
	private void buildOKBtn(Button okBtn) {
		okBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editUserInfo();
			}
		});
	}
	
	
	/**
	 * Async call to the server to edit user's information
	 */
	private void editUserInfo() {
		// TODO add asycn call when implemented. make sure dialog is closed.
		
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
	

}
