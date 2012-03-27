package cpsc310.client;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ToggleButton;

public class DrawToolButton extends ToggleButton {
		
	/**
	 * Constructor for drawing buttons for Google Map
	 */
	public DrawToolButton() {
		super();
	}
	
	/**
	 * Method to set the button as a draw button.
	 */
	public void setDrawImage() {
		this.getUpFace().setImage(new Image("/images/ButtonImg/DrawUp.png"));
		this.getUpHoveringFace().setImage(new Image("/images/ButtonImg/DrawUpHov.png"));
		this.getDownFace().setImage(new Image("/images/ButtonImg/DrawDown.png"));
		this.getDownHoveringFace().setImage(new Image("/images/ButtonImg/DrawDownHov.png"));
	}
	
	/**
	 * Method to set the button as an erase button
	 */
	public void setEraseImage() {
		this.getUpFace().setImage(new Image("/images/ButtonImg/EraseUp.png"));
		this.getUpHoveringFace().setImage(new Image("/images/ButtonImg/EraseUpHov.png"));
		this.getDownFace().setImage(new Image("/images/ButtonImg/EraseDown.png"));
		this.getDownHoveringFace().setImage(new Image("/images/ButtonImg/EraseDownHov.png"));
	}
}
