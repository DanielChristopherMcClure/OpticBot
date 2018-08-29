package canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public class DebugObject {

	public int count = 0;
	public Point p = null;
	public Rectangle r = null;

	public DebugObject(Rectangle r) {
		this.r = r;
	}

	public DebugObject(Point p) {
		this.p = p;
	}

	public void drawRectangle(Graphics g, Color c) {
		g.setColor(c);
		g.drawRect(r.x, r.y, r.width, r.height);
		count++;
	}

	public void drawCross(int size, Graphics g, Color c) {
		g.setColor(c);
		g.drawLine(p.x - size / 2, p.y, p.x + size / 2, p.y);
		g.drawLine(p.x, p.y - size / 2, p.x, p.y + size / 2);
		count++;
	}

}
