package canvas;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.accessibility.Accessible;

import core.Areas;
import core.Ibot;
import interfaces.Paintable;
import ui.TronicUI;
import utilities.Rect;
import utilities.Tools;
import wrappers.RecordedAction;

public class RSCanvas extends Canvas implements MouseMotionListener, MouseListener, Accessible, FocusListener {

	static final long serialVersionUID = 1L;
	static final BufferedImage image = new BufferedImage(765, 503, BufferedImage.TYPE_INT_ARGB);
	public BufferedImage buffer1 = null;
	private static RSCanvas instance = null;
	private static int x = 0;
	private static int y = 0;
	public static boolean inputMouse = true;
	public static boolean inputKeyboard = true;
	public Paintable debug = null;
	public Ibot client = null;
	public Color lastClick = new Color(0, 0, 0);

	public Point mouse = new Point(0, 0);

	public boolean one = true;
	public Point click1 = null;
	public Point click2 = null;
	private int debugRectPoint = 0;

	int count = 0;
	public boolean clickEventSent = false;

	public RSCanvas() {
		instance = this;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		// for (FocusListener fl : this.getFocusListeners()) {
		// this.removeFocusListener(fl);
		// }
		this.addFocusListener(this);
	}

	public Dimension getPreferredSize() {
		return super.getSize();
	}

	public void setSize(Dimension d) {

	}

	public void setLocation(int x, int y) {

	}

	public BufferedImage getImage() {
		return RSCanvas.image;
	}

	public Color getColorAt(int x, int y) {
		return new Color(RSCanvas.image.getRGB(x, y), false);
	}

	public int getRGB(int x, int y) {
		return RSCanvas.image.getRGB(x, y);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
			Debug.pressPoint = mouse;
			Debug.count = 0;
			this.lastClick = client.methods.color.getColorAt(new Point(x, y));
			if (TronicUI.debugRect.isSelected()) {
				if (one) {
					click1 = new Point(x, y);
					one = false;
				} else {
					click2 = new Point(x, y);
					one = true;
				}
			}
			if (TronicUI.logColor.isSelected()) {
				Color c = client.methods.color.getColorAt(new Point(x, y));
				Tools.log("new Color(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + "),");
			}
			if (TronicUI.debugPoly.isSelected()) {
				Debug.poly.add(new Point(x, y));
				ArrayList<Point> poly = Debug.poly;
				int[] xs = new int[poly.size()];
				int[] ys = new int[poly.size()];
				for (int i = 0; i < poly.size(); i++) {
					xs[i] = poly.get(i).x;
					ys[i] = poly.get(i).y;
				}
				System.out.print("new int[] {");
				for (int xx : xs)
					System.out.print(xx + ",");
				System.out.print("}, new int[] {");
				for (int yy : ys)
					System.out.print(yy + ",");
				System.out.print("}, " + poly.size());
				Tools.log("");
			}
			if (TronicUI.drawRect.isSelected() && e.getButton() == 3) {
				if (this.debugRectPoint == 0) {
					Debug.debugRect[0] = mouse;
					this.debugRectPoint = 1;
				} else {
					Debug.debugRect[1] = mouse;
					this.debugRectPoint = 0;
					Tools.log(new Rect(Debug.debugRect[0].x, Debug.debugRect[0].y, Debug.debugRect[1].x,
							Debug.debugRect[1].y).getBounds());
					Tools.log("Edge ID: " + client.methods.edges.getEdgeId(new Rect(Debug.debugRect[0].x,
							Debug.debugRect[0].y, Debug.debugRect[1].x, Debug.debugRect[1].y).getBounds()));
				}
			}
			if (client.methods.recorder.isRecording()) {
				RecordedAction a = new RecordedAction("mouseclick~" + e.getButton(),
						(int) ((System.currentTimeMillis() - client.methods.recorder.timer)), new Point(x, y));
				Tools.log("Action Added: " + a.action + " sleep: " + a.sleep + "  loc: " + a.loc);
				client.methods.recorder.actions.add(a);
				client.methods.recorder.resetTimer();
			}
			if (TronicUI.drawSelectedColors.isSelected()) {
				Point p = client.mouse.getMouseXY();
				if (p != null) {
					if (Areas.GAMESCREEN.contains(p)) {
						Color c = client.canv.getColorAt(p.x, p.y);
						if (c != null) {
							Ibot.colorList.add(c);
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (new Rectangle(0, 0, 765, 503).contains(new Point(e.getX(), e.getY()))) {
			mouse = new Point(e.getX(), e.getY());
			x = mouse.x;
			y = mouse.y;
		}
	}

	@Override
	public Graphics getGraphics() {
		buffer1 = new BufferedImage(this.getWidth(), this.getHeight(), 2);
		Graphics g = buffer1.getGraphics();
		g.drawImage(image, 0, 0, null);
		this.paintDebug(g);
		if (super.getGraphics() != null)
			super.getGraphics().drawImage(buffer1, 0, 0, null);
		return image.getGraphics();
	}

	public void paintDebug(Graphics g) {
		if (this.debug != null) {
			this.debug.paint(g);
		} else {
			// Tools.log("Debug null");
		}
	}

	// sending AWT events
	public void sendEvent(AWTEvent awtevent) {
		if (awtevent instanceof MouseEvent) {
			if (awtevent.getID() == 505) {
				x = -1;
				y = -1;
			} else {
				x = ((MouseEvent) awtevent).getX();
				y = ((MouseEvent) awtevent).getY();
			}
		}
		if (client.isAuthenticated())
			super.processEvent(awtevent);
	}

	@Override
	public void processEvent(AWTEvent awtevent) {
		if (awtevent instanceof MouseEvent) {
			if (((MouseEvent) awtevent).equals(MouseEvent.MOUSE_PRESSED)) {
				this.clickEventSent = true;
			}
			if (awtevent.getID() == 505) {
				x = -1;
				y = -1;
			} else {
				x = ((MouseEvent) awtevent).getX();
				y = ((MouseEvent) awtevent).getY();
			}
			if (!inputMouse) {
				return;
			}
		} else if (awtevent instanceof KeyEvent) {
			if (!inputKeyboard) {
				return;
			}
		}
		if (client != null)
			if (client.isAuthenticated())
				super.processEvent(awtevent);
			else
				super.processEvent(awtevent);
	}

	public static int getMouseX() {
		return x;
	}

	public static int getMouseY() {
		return y;
	}

	@Override
	public void focusGained(FocusEvent e) {
		Tools.log("Gained Focus");
	}

	@Override
	public void focusLost(FocusEvent e) {
		Tools.log("Lost Focus");
		client.resetCanvas();
	}

}
