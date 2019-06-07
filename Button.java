import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton{
	
	private boolean isLastButton;
	
	public Button() {
		super();
		initUI();
	}
	
	public Button(Image image) {
		super(new ImageIcon(image));
		initUI();
	}
	
	private void initUI() {
		isLastButton = false;
		BorderFactory.createLineBorder(Color.black);
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.yellow));
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.yellow));
			}
		});
	}
	
	public void setLastButton() {
		isLastButton = true;
	}
	
	public boolean getLastButton() {
		return isLastButton;
	}

}
