package cpsc310.client;

import com.google.gwt.user.client.ui.Button;

public class DrawToolButton extends Button {
	
	private String buttonType;
	
	/**
	 * Constructor for drawing buttons for Google Map
	 */
	public DrawToolButton() {
		super();
	}
	
	/**
	 * Method to set the type of drawing button to display.
	 * @param type - the type of drawing button to display; valid types are: "draw" and "erase"
	 */
	public void setButtonType(String type) {
		buttonType = type;
	}
	
	

}
