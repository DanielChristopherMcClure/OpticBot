package ui;

import javax.swing.ImageIcon;
import javax.swing.JRadioButton;

public class NexusRadioButton extends JRadioButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ImageIcon off = new ImageIcon("Data/Images/offSwitch.png");
	ImageIcon on = new ImageIcon("Data/Images/onSwitch.png");
	
	public NexusRadioButton(String text) {
		super.setText(text);
		this.setIcon(off);
		this.setSelectedIcon(on);
		this.setDisabledIcon(off);
		this.setRolloverIcon(off);
		this.setOpaque(false);
	}
}
