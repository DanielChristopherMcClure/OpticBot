package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;

import utilities.Tools;

public class NexusButton extends JButton implements MouseMotionListener,
		MouseListener, Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Point mouse = new Point(-5, -5);

	int opacity = 0;
	int MAX_OPACITY = 175;

	boolean mouseOver = false;

	Image img = null;

	String text = null;

	Color fade = new Color(104, 177, 232);

	public NexusButton(String text) {
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.text = text;
		this.setNexusFont();
		new Thread(this).start();
	}

	public NexusButton(Image img) {
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.img = img;
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.setNexusFont();
		new Thread(this).start();
	}

	public NexusButton(Image img, String text) {
		this.setOpaque(false);
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.img = img;
		this.text = text;
		this.setSize(img.getWidth(null), img.getHeight(null));
		this.setNexusFont();
		new Thread(this).start();
	}

	public void setNexusFont() {
		this.setFont(getCustomFont());
	}

	public void setFadeInColor(Color c) {
		this.fade = c;
	}

	public Font getCustomFont() {
		// try {
		// return Font.createFont(Font.TRUETYPE_FONT,
		// / new FileInputStream("Data/font.ttf")).deriveFont(11f);
		// } catch (FontFormatException | IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return new Font("Times New Roman", Font.PLAIN, 12);
		// return null;
	}

	public void paint(Graphics g) {
		g.setFont(this.getCustomFont());
		Rectangle bounds = new Rectangle(0, 0, this.getWidth(),
				this.getHeight());
		Point center = new Point((int) bounds.getCenterX(),
				(int) bounds.getCenterY());
		g.setColor(new Color(fade.getRed(), fade.getGreen(), fade.getBlue(),
				opacity));
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(new Color(0, 0, 0, 120));
		g.drawRect(0, 0, this.getWidth() - 1, this.getHeight() - 1);

		int textOffset = 0;
		if (text != null)
			textOffset = text.length() * 3;

		if (img != null && text == null) {
			g.drawImage(img, center.x - (img.getWidth(null) / 2), center.y
					- (img.getHeight(null) / 2), null);
		} else if (img != null && text != null) {
			g.drawImage(img, center.x - (img.getWidth(null) / 2) - textOffset,
					(center.y - (img.getHeight(null) / 2)), null);
			Tools.drawSharpText(text,
					center.x - textOffset + img.getWidth(null), center.y + 5,
					Color.BLACK, new Color(0, 0, 0, 0), g);
		} else if (text != null && img == null) {
			Tools.drawSharpText(text, center.x - textOffset, center.y + 5,
					Color.BLACK, new Color(0, 0, 0, 0), g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();
		this.mouseOver = true;
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();
		this.mouseOver = false;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse = new Point(e.getX(), e.getY());
		this.repaint();
	}

	@Override
	public void run() {
		while (true) {
			if (opacity < MAX_OPACITY && mouseOver) {
				opacity++;
			} else if (!mouseOver && opacity != 0) {
				opacity--;
			}
			this.repaint();
			Tools.sleep(5);
		}
	}

}
