package ui;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ScriptImagePanel extends JPanel {

	private Image img = null;

	public ScriptImagePanel(Image img) {
		this.img = img;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
	}

	public void setImage(Image img) {
		this.img = img;
	}

}
