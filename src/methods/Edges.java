package methods;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import core.Ibot;
import utilities.Tools;

public class Edges {

	private Ibot bot = null;

	public Edges(Ibot bot) {
		this.bot = bot;
	}

	public Point[] getRoughEdges(Rectangle rect) {
		ArrayList<Point> points = new ArrayList<Point>();
		for (Point p : bot.methods.getEdges(rect)) {
			for (int x = p.x - 1; x < p.x + 1; x++) {
				for (int y = p.y - 1; y < p.y + 1; y++) {
					if (Tools.onGameScreen(new Point(x, y))) {
							points.add(new Point(x, y));
					}
				}
			}
		}
		return points.toArray(new Point[] {});
	}

	public int getEdgeId(Rectangle rect) {
		int id = 0;
		for (Point p : bot.methods.getEdges(rect))
			id += (int) (p.getX() + p.getY());
		Tools.copyToClipBoard("" + id);
		return id;
	}

}
