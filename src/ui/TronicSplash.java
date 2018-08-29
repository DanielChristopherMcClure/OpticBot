package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JWindow;

import utilities.Tools;

public class TronicSplash extends JWindow {

	Image img = Toolkit.getDefaultToolkit().getImage("Data/Images/loading.png");
	Image bar = Tools.getLocalImage("Data/Images/bar.png");

	ImageIcon imgicon = new ImageIcon(img);

	public double percent = 1;

	public TronicSplash() {
		try {
			this.setAlwaysOnTop(true);
			setSize(250, 100);
			setLocationRelativeTo(null);
			show();
		} catch (Exception e) {
			Tools.log("Splash Screen failed");
			this.setVisible(false);
		}
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(16, 65, (int) ((double) ((percent / 100) * 214)), 30);
		g.drawImage(img, 0, 0, this);
	}

	public void setPercent(double per) {
		this.percent = per;
		this.repaint();
	}

}
