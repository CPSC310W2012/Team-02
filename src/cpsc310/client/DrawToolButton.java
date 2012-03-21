package cpsc310.client;

import com.google.gwt.user.client.ui.Button;

public class DrawToolButton extends Button {
		
	private BtnType buttonType;
	private Boolean isDrawing;
	
	/**
	 * Constructor for drawing buttons for Google Map
	 */
	public DrawToolButton() {
		super();
		isDrawing = false;
	}
	
	/**
	 * Enum values to restrict input for the setButtonType method.
	 */
	public enum BtnType {
		DRAW, ERASE
	}
	
	/**
	 * Method to set the type of drawing button to display.
	 * @pre type != null;
	 * @post buttonType = type;
	 * @param type - the type of drawing button to display; valid types are: "draw" and "erase"
	 */
	public void setButtonType(BtnType type) {
		buttonType = type;
	}
	
	/**
	 * Method to set if the button is in drawing mode.
	 * @pre (drawEnabled != null) && (buttonType.equals(BtnType.DRAW);
	 * @post isDrawing = drawEnabled;
	 * @param drawEnabled
	 */
	public void setDrawEnabled(Boolean drawEnabled) {
		isDrawing = drawEnabled;
	}
	
	/**
	 * Method to return if the button has been pressed; i.e. user wants to draw on the Google Map
	 * @pre true;
	 * @post true;
	 * @return if the button has been pressed for drawing mode.
	 */
	public boolean drawEnabled() {
		return isDrawing;
	}

}
