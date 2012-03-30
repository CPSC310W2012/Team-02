package cpsc310.client;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ToggleButton;

/**
 * Class to create buttons for Google Map.
 */
public class ButtonFactory {
		
	/**
	 * Constructor for drawing buttons for Google Map
	 */
	public ButtonFactory() {
		//emtpy constructor
	}
		
	/**
	 * Method to set the button as a draw button.
	 * @pre true;
	 * @post true;
	 * @return a ToggleButton with faces for a draw button.
	 */
	public ToggleButton createDrawButton() {
		ToggleButton drawBtn = new ToggleButton();
		drawBtn.getUpFace().setImage(new Image("/images/ButtonImg/DrawUp.png"));
		drawBtn.getUpHoveringFace().setImage(new Image("/images/ButtonImg/DrawUpHov.png"));
		drawBtn.getDownFace().setImage(new Image("/images/ButtonImg/DrawDown.png"));
		return drawBtn;
	}
	
	/**
	 * Method to set the button as an erase button
	 * @pre true;
	 * @post true;
	 * @return a PushButton with faces for an erase button.
	 */
	public PushButton createEraseButton() {
		PushButton eraseBtn = new PushButton();
		eraseBtn.getUpFace().setImage(new Image("/images/ButtonImg/EraseUp.png"));
		eraseBtn.getUpHoveringFace().setImage(new Image("/images/ButtonImg/EraseUpHov.png"));
		eraseBtn.getDownFace().setImage(new Image("/images/ButtonImg/EraseDown.png"));		
		return eraseBtn;
	}
}
