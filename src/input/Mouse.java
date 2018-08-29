package input;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import canvas.Debug;
import canvas.DebugObject;
import core.Ibot;
import utilities.Tools;

public class Mouse {

	Ibot client = null;

	public DebugObject cross = null;
	public DebugObject rect = null;

	public static Point[] current = null;

	public Mouse(Ibot client) {
		this.client = client;
	}

	public void click(Boolean right) {
		if (client.isAuthenticated()) {
			Debug.pressPoint = this.getMouseXY();
			Debug.count = 0;
			if (right) {
				this.pressMouse(this.getMouseX(), this.getMouseY(), false);
				Tools.sleep(50, 150);
				this.releaseMouse(this.getMouseX(), this.getMouseY(), false);
			} else {
				this.pressMouse(this.getMouseX(), this.getMouseY(), true);
				Tools.sleep(50, 150);
				this.releaseMouse(this.getMouseX(), this.getMouseY(), true);
			}
		}
	}

	public void mouseClickRectangle(Rectangle r, boolean right) {
		this.rect = new DebugObject(r);
		Point rp = new Point(
				(int) r.getCenterX() + Tools.random((int) (-1 * (r.getWidth() / 2)), (int) (r.getWidth() / 2)),
				(int) r.getCenterY() + Tools.random((int) (-1 * (r.getHeight() / 2)), (int) (r.getHeight() / 2)));
		this.mouseMove(rp);
		if (right) {
			this.mouseClickRight();
		} else {
			this.mouseClickLeft();
		}
	}

	public void mouseClickLeft(Point here) {
		this.mouseMove(here);
		this.mouseClickLeft();
	}

	public void mouseClickRight(Point here) {
		this.mouseMove(here);
		this.mouseClickRight();
	}

	public void mouseClickRight() {
		this.click(true);
	}

	public void mouseClickLeft() {
		this.click(false);
	}

	public void mouseMove(int x, int y) {
		this.mouseMove(new Point(x, y));
	}

	public void mouseMove(Rectangle r) {
		this.rect = new DebugObject(r);
		Point rp = new Point(
				(int) r.getCenterX() + Tools.random((int) (-1 * (r.getWidth() / 2)), (int) (r.getWidth() / 2)),
				(int) r.getCenterY() + Tools.random((int) (-1 * (r.getHeight() / 2)), (int) (r.getHeight() / 2)));
		this.mouseMove(rp);
	}

	public void mouseMove(int x, int y, int r1, int r2) {
		this.mouseMove(new Point(x + Tools.random(r1, r2), y + Tools.random(r1, r2)));
	}

	public void mouseMove(Point xy) {
		if (xy != null && Tools.onScreen(xy) && !xy.equals(new Point(-1, -1))) {
			this.cross = new DebugObject(xy);
			Spline spline = new Spline(xy.x, xy.y, Tools.random(Tools.random(0, 25), 150),
					Tools.random(Tools.random(0, 25), 150));
			ArrayList<ArrayList<Point>> splines = new ArrayList<ArrayList<Point>>();
			for (int i = 0; i < 10; i++) {
				int count = 0;
				Point[] s = spline.makeMousePath(getMouseXY(), xy);
				ArrayList<Point> current = new ArrayList<Point>();
				for (Point p : s) {
					current.add(p);
					if (!Tools.onScreen(p))
						count++;
				}
				if (count == 0) {
					splines.add(current);
				}
			}
			Point[] points = null;
			if (splines.size() == 0) {
				points = spline.makeMousePath(getMouseXY(), xy);
			} else {
				points = splines.get(Tools.random(0, splines.size() - 1)).toArray(new Point[] {});
			}
			current = points;
			for (Point p : points) {
				if (p != null && Tools.onScreen(p)) {
					this.mouseHop((int) p.getX(), (int) p.getY());
					Tools.sleep(Tools.random(2, 5));
				} else {
					if (p != null) {
						// Tools.log("Next Point in Spline OffScreen, Point: "
						// + p);
					} else {
						// Tools.log("Next Point in Spline is null");
					}
				}
			}
		} else {
			Tools.log("XY is null");
		}
		if (current != null)
			current = null;
	}

	public int getMouseX() {
		return client.getCanvas().mouse.x;
	}

	public int getMouseY() {
		return client.getCanvas().mouse.y;
	}

	public Point getMouseXY() {
		return client.getCanvas().mouse;
	}

	public void mouseHop(int x, int y) {
		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_MOVED,
				System.currentTimeMillis(), 0, x, y, 0, false, 0));
	}

	public void pressMouse(int x, int y, boolean button) {
		Debug.pressPoint = new Point(x, y);
		Debug.count = 0;
		if (x < 0 || y < 0 || x > 756 || y > 503)
			return;

		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_PRESSED,
				System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));
	}

	public void releaseMouse(int x, int y, boolean button) {
		if (x < 0 || y < 0 || x > 756 || y > 503)
			return;

		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_RELEASED,
				System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));

		client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_CLICKED,
				System.currentTimeMillis(), 0, x, y, 1, false, button ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3));
	}

	public void clickMouse(Point point, int button) {
		if (button == 1) {
			this.mouseClickLeft(point);
		} else {
			this.mouseClickRight(point);
		}
	}

	public void dragMouse(Point from, Point to) {
		if (from == null || to == null)
			return;

		try {

			mouseMove(from);

			client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_PRESSED,
					System.currentTimeMillis(), 0, from.x, from.y, 1, false, MouseEvent.BUTTON1));

			Tools.sleep(300, 500);

			mouseMove(to);

			Tools.sleep(300, 500);

			client.getCanvas().dispatchEvent(new MouseEvent(client.getCanvas(), MouseEvent.MOUSE_RELEASED,
					System.currentTimeMillis(), 0, to.x, to.y, 1, false, MouseEvent.BUTTON1));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
