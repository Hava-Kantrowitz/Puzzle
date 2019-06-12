import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Creates the board that the puzzle is played on
 * @author havak
 *
 */
public class PuzzleBoard extends JFrame{
	
	private JPanel panel;//the board itself
	private BufferedImage source;//the original image
	private BufferedImage resized; //the resized image
	private Image image;//the input image
	private Button lastButton;//the empty button
	private int width, height;//the width and height of the board
	
	private List<Button> buttons;//list of the current state of the buttons
	private List<Point> solution;//list of the correct state of the buttons
	
	private final int NUMBER_OF_BUTTONS = 12;//the total number of buttons
	private final int DESIRED_WIDTH = 300;//the width of the board
	
	/**
	 * Constructs the puzzle board
	 */
	public PuzzleBoard() {
		initUI();//initializes the UI
	}
	
	/**
	 * Initializes the UI
	 */
	private void initUI() {
		solution = new ArrayList<>();//creates the solution list
		
		//adds the solution points to the solution list
		solution.add(new Point(0,0));
		solution.add(new Point(0,1));
		solution.add(new Point(0,2));
		solution.add(new Point(1,0));
		solution.add(new Point(1,1));
		solution.add(new Point(1,2));
		solution.add(new Point(2,0));
		solution.add(new Point(2,1));
		solution.add(new Point(2,2));
		solution.add(new Point(3,0));
		solution.add(new Point(3,1));
		solution.add(new Point(3,2));
		
		buttons = new ArrayList<>();//creates the in-play buttons list
		
		panel = new JPanel();//creates the overall border
		panel.setBorder(BorderFactory.createLineBorder(Color.black));//sets the border color to black
		panel.setLayout(new GridLayout(4, 3, 0, 0));//sets its layout to a 4x3 grid
		
		try {
			source = loadImage();//get the source image
			int h = getNewHeight(source.getWidth(), source.getHeight());//get the new height
			resized = resizeImage(source, DESIRED_WIDTH, h, BufferedImage.TYPE_INT_ARGB);//the resized image
		} catch (IOException ex) {//if image isn't found, log it and return an error
			Logger.getLogger(PuzzleBoard.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		width = resized.getWidth(null);//get the width of the resized image
		height = resized.getHeight(null);//get the height of the resized image
		
		add(panel, BorderLayout.CENTER);//add the image buttons to the center of the panel
		
		//loop through every row and column
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				image = createImage(new FilteredImageSource(resized.getSource(),
						new CropImageFilter(j*width/3, i*height/4, (width/3),
								height/4)));//set the image to the proper source and size
				
				Button button = new Button(image);//set each piece of the image to a new button
				button.putClientProperty("position", new Point(i,j));//put each new button at the given coordinates
				
				if(i ==3 && j == 2) {//if it's at the end
					lastButton = new Button();//create the empty button
					lastButton.setBorderPainted(false);//don't paint the border
					lastButton.setContentAreaFilled(false);//don't fill the area
					lastButton.setLastButton();//set it to the last button
					lastButton.putClientProperty("position", new Point(i,j));//place it at the given coordinates
				}
				else {//otherwise just add the button to the board
					buttons.add(button);
				}
			}
		}
		
		Collections.shuffle(buttons);//randomly shuffle them
		buttons.add(lastButton);//add the last button to the board
		
		for (int h = 0; h < NUMBER_OF_BUTTONS; h++) {//for each button
			Button button2 = buttons.get(h);//get the button
			panel.add(button2);//add it to the board
			button2.setBorder(BorderFactory.createLineBorder(Color.black));//set border to black
			button2.addActionListener(new ClickAction());//set listener ready for mouse click
		}
		
		pack();//pack the size
		setTitle("My Puzzle");//set title to "My Puzzle"
		setResizable(false);//make frame not resizable
		setLocationRelativeTo(null);//make location relative
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//exit 
	}
	
	/**
	 * Gets the height of the resized image
	 * @param w original width
	 * @param h original height
	 * @return resized height
	 */
	private int getNewHeight(int w, int h) {
		double ratio = DESIRED_WIDTH/(double)w;//determine the ratio
		int newHeight = (int) (h*ratio);//get the new height
		return newHeight;//return it
	}
	
	/**
	 * Loads the given image
	 * @return the image
	 * @throws IOException
	 */
	private BufferedImage loadImage() throws IOException{
		BufferedImage img = ImageIO.read(new File("src/IMG_9792.JPG"));//gets the image from the source file
		return img;//returns it
	}
	
	/**
	 * Resizes the image to fit the board
	 * @param original the original image
	 * @param width original width
	 * @param height original height
	 * @param type original pixel type of the image
	 * @return the resized image
	 * @throws IOException
	 */
	private BufferedImage resizeImage(BufferedImage original, int width, int height, int type)
	throws IOException{
		BufferedImage resizedImage = new BufferedImage(width, height, type);//create the resized image
		Graphics2D g = resizedImage.createGraphics();//draw it 
		g.drawImage(original, 0, 0, width, height, null);
		g.dispose();//throw out drawing tool, no longer needed once drawn
		
		return resizedImage;//return resized
	}
	
	/**
	 * This class records mouse click events and determines what should be done during and after them
	 * @author havak
	 *
	 */
	private class ClickAction extends AbstractAction{
		/**
		 * What to do if a mouse click action is performed
		 */
		public void actionPerformed(ActionEvent e) {
			checkButton(e);//check the buttons
			checkSolution();//check the solution
		}
		
		/**
		 * Checks to see what to do about a button click
		 * @param e the mouse click event
		 */
		private void checkButton(ActionEvent e) {
			int lx = 0;
			for(Button button : buttons) {//for each button in the current puzzle state list
				if(button.getLastButton()) {//if the button is the empty last button
					lx = buttons.indexOf(button);//get its index
				}
			}
			
			JButton button = (JButton) e.getSource();//get next clicked button
			int bx = buttons.indexOf(button);//get its index
			
			if((bx - 1 == lx) || (bx + 1 == lx)
                    || (bx - 3 == lx) || (bx + 3 == lx)) {//if the empty button and clicked button are next to each other
				
				Collections.swap(buttons, bx, lx);//swap them
				updateButtons();//update the board and buttons
				
			}
		}
		
		/**
		 * Updates the panel according to the new button organization
		 */
		private void updateButtons() {
			panel.removeAll();//remove all the buttons
			for(JComponent button : buttons) {//add the buttons back in according to the redone list
				panel.add(button);
			}
			
			panel.validate();//update the panel
		}
		
		/**
		 * Checks if the current puzzle list is equal to the solution puzzle list
		 */
		private void checkSolution() {
			List<Point> current = new ArrayList<>();//get the current list
			for(JComponent button : buttons) {//for each button 
				current.add((Point) button.getClientProperty("position"));//get its position
			}
			
			if(compareList(solution, current)) {//compare the solution and current options, if they are equal
				JOptionPane.showMessageDialog(panel, "Finished", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);//then print finish message
			}
		}
		
		/**
		 * Determines if the two puzzle lists are equal
		 * @param l1 the solution puzzle list
		 * @param l2 the current puzzle list
		 * @return true if they are equal, false otherwise
		 */
		public boolean compareList(List l1, List l2) {
			return l1.toString().contentEquals(l2.toString());//changes both lists to string and performs a content equals check
		}
		
	}

}
