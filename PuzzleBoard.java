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

public class PuzzleBoard extends JFrame{
	
	private JPanel panel;
	private BufferedImage source;
	private BufferedImage resized; 
	//RESEARCH BUFFERED IMAGE
	private Image image;
	private Button lastButton;
	private int width, height;
	
	private List<Button> buttons;
	private List<Point> solution;
	
	private final int NUMBER_OF_BUTTONS = 12;
	private final int DESIRED_WIDTH = 300;
	
	public PuzzleBoard() {
		initUI();
	}
	
	private void initUI() {
		solution = new ArrayList<>();
		
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
		
		buttons = new ArrayList<>();
		
		panel = new JPanel();
		panel.setBorder(BorderFactory.createLineBorder(Color.black));
		panel.setLayout(new GridLayout(4, 3, 0, 0));
		
		try {
			source = loadImage();
			int h = getNewHeight(source.getWidth(), source.getHeight());
			resized = resizeImage(source, DESIRED_WIDTH, h, BufferedImage.TYPE_INT_ARGB);
		} catch (IOException ex) {
			Logger.getLogger(PuzzleBoard.class.getName()).log(Level.SEVERE, null, ex);
		}
		
		width = resized.getWidth(null);
		height = resized.getHeight(null);
		
		add(panel, BorderLayout.CENTER);
		
		for(int i = 0; i < 4; i++) {
			for (int j = 0; j < 3; j++) {
				image = createImage(new FilteredImageSource(resized.getSource(),
						new CropImageFilter(j*width/3, i*height/4, (width/3),
								height/4)));
				
				Button button = new Button(image);
				button.putClientProperty("position", new Point(i,j));
				
				if(i ==3 && j == 2) {
					lastButton = new Button();
					lastButton.setBorderPainted(false);
					lastButton.setContentAreaFilled(false);
					lastButton.setLastButton();
					lastButton.putClientProperty("position", new Point(i,j));
				}
				else {
					buttons.add(button);
				}
			}
		}
		
		Collections.shuffle(buttons);
		buttons.add(lastButton);
		
		for (int h = 0; h < NUMBER_OF_BUTTONS; h++) {
			Button button2 = buttons.get(h);
			panel.add(button2);
			button2.setBorder(BorderFactory.createLineBorder(Color.black));
			button2.addActionListener(new ClickAction());
		}
		
		pack();
		setTitle("My Puzzle");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private int getNewHeight(int w, int h) {
		double ratio = DESIRED_WIDTH/(double)w;
		int newHeight = (int) (h*ratio);
		return newHeight;
	}
	
	private BufferedImage loadImage() throws IOException{
		BufferedImage img = ImageIO.read(new File("src/IMG_9792.JPG"));
		return img;
	}
	
	private BufferedImage resizeImage(BufferedImage original, int width, int height, int type)
	throws IOException{
		BufferedImage resizedImage = new BufferedImage(width, height, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(original, 0, 0, width, height, null);
		g.dispose();
		
		return resizedImage;
	}
	
	private class ClickAction extends AbstractAction{
		public void actionPerformed(ActionEvent e) {
			checkButton(e);
			checkSolution();
		}
		
		private void checkButton(ActionEvent e) {
			int lx = 0;
			for(Button button : buttons) {
				if(button.getLastButton()) {
					lx = buttons.indexOf(button);
				}
			}
			
			JButton button = (JButton) e.getSource();
			int bx = buttons.indexOf(button);
			
			if((bx - 1 == lx) || (bx + 1 == lx)
                    || (bx - 3 == lx) || (bx + 3 == lx)) {
				
				Collections.swap(buttons, bx, lx);
				updateButtons();
				
			}
		}
		
		private void updateButtons() {
			panel.removeAll();
			for(JComponent button : buttons) {
				panel.add(button);
			}
			
			panel.validate();
		}
		
		private void checkSolution() {
			List<Point> current = new ArrayList<>();
			for(JComponent button : buttons) {
				current.add((Point) button.getClientProperty("position"));
			}
			
			if(compareList(solution, current)) {
				JOptionPane.showMessageDialog(panel, "Finished", "Congratulations!", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		
		public boolean compareList(List l1, List l2) {
			return l1.toString().contentEquals(l2.toString());
		}
		
	}

}
