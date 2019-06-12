import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * This class models a button within the puzzle. Each puzzle piece is a button
 * @author havak
 *
 */
public class Button extends JButton{
	
	private boolean isLastButton;//whether or not the piece is the final piece
	
	/**
	 * Constructs the button class with the blank square
	 */
	public Button() {
		super();//uses the constructor of the JButton class
		initUI();//initializes the UI
	}
	
	/**
	 * Constructs the button class with the image
	 * @param image the image within the button square
	 */
	public Button(Image image) {
		super(new ImageIcon(image));//uses the constructor of the JButton class
		initUI();//initializes the UI
	}
	
	/**
	 * Initializes the UI 
	 */
	private void initUI() {
		isLastButton = false;//sets last button to false
		BorderFactory.createLineBorder(Color.black);//creates a black border around each puzzle piece
		
		/**
		 * Listens for mouse input and determines what to do if mouse is over button
		 */
		addMouseListener(new MouseAdapter() {
			
			/**
			 * When mouse enters the puzzle piece, set the border to yellow
			 * @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.yellow));
			}
			
			/**
			 * When mouse leaves the puzzle piece, reset the border to black
			 * @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent)
			 */
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.black));
			}
		});
	}
	
	/**
	 * Sets whether the given button is the final button to true
	 */
	public void setLastButton() {
		isLastButton = true;
	}
	
	/**
	 * Gets whether or not the given button is the final button
	 */
	public boolean getLastButton() {
		return isLastButton;
	}

}
