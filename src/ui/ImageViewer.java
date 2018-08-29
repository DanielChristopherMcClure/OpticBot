package ui;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import utilities.Tools;

public class ImageViewer extends JFrame implements MouseListener,
		MouseMotionListener {

	public BufferedImage current = null;

	private ScriptImagePanel back = null;

	public ImageViewer(BufferedImage img) {
		this.setSize(765, 503);
		this.setResizable(false);
		back = new ScriptImagePanel(img);
		this.add(back, BorderLayout.CENTER);
		back.addMouseListener(this);
		back.addMouseMotionListener(this);
		this.open(img);
	}

	public void open(BufferedImage img) {
		this.current = img;
		this.setVisible(true);
		back.setImage(img);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Tools.log("new Point(" + e.getX() + "," + e.getY() + "),");

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
