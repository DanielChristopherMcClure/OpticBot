package ui;

import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NexusFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JPanel panel = new JPanel();

	JButton b1;

	public NexusFrame(String title) {
		setTitle("Transparent JFrame Demo");
		setLayout(new GridBagLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setVisible(true);
		setOpacity(1.0f);

		// Try different shapes

		// Normal rounded rectangle
		setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20,
				40));

		// Ellipse shape
		// setShape(new Ellipse2D.Double(0,0,400,400));

		// Quad Curve
		// setShape(new QuadCurve2D.Double(0,0,400,50,400,400));

		// Cubic Curve. Looks funny:)
		// setShape(new CubicCurve2D.Double(0,0,500,5,200,0,500,500));

		// For three-fourth circle
		// setShape(new Arc2D.Double(new
		// Rectangle2D.Double(0,0,500,500),90,270,Arc2D.PIE));

		// For round circle shape
		// setShape(new Arc2D.Double(new Rectangle2D.Double(0, 0, 500, 500), 90,
		// 360, Arc2D.PIE));

		b1 = new NexusButton("X");
		add(b1);

		setSize(500, 500);
		setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	protected void paintComponent(Graphics g) {
		g.drawString("HIIIIIIIII", 15, 25);
	}

	public static void main(String[] arg0) {
		new NexusFrame("sup").setVisible(true);
	}

}
