package methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utilities.Tools;
import core.Ibot;

public class ColorUtils {

	public Point player = new Point(275, 145);
	private Ibot bot = null;

	public ColorUtils(Ibot bot) {
		this.bot = bot;
	}

	public void clickColor(Rectangle r, Color c, int tol, boolean right) {
		Rectangle cluster = bot.methods.clusters.getWinningCluster(r, c, tol);
		if (cluster != null)
			bot.mouse.mouseClickRectangle(cluster, right);
	}

	public Point findColor(Rectangle rect, Color color, int tol) {
		Point[] matches = this.getColorPoints(rect, color, tol);
		if (matches.length > 0)
			return matches[Tools.random(0, matches.length - 1)];
		return null;
	}

	public Point findColorClosestToPlayer(Rectangle rect, Color color, int tol) {
		int little = 1000000000;
		Point winner = null;
		Point[] matches = this.getColorPoints(rect, color, tol);
		for (Point p : matches) {
			if (p != null && player.distance(p) < little && player.distance(p) > 55) {
				little = (int) player.distance(p);
				winner = p;
			}
		}
		return winner;
	}

	public Color getColorAt(Point here) {
		Color c = null;
		if (here != null)
			c = new Color(bot.getCanvas().getImage().getRGB(here.x, here.y), false);
		return c;
	}

	public Point[] getColorPoints(Rectangle rect, Color color, int tol) {
		int count = 0;
		for (int x = rect.getLocation().x; x < rect.width; x++)
			for (int y = rect.getLocation().y; y < rect.height; y++)
				if (!Tools.onScreen(new Point(x, y)))
					count++;

		if (count == 0 && rect.width > 0 && rect.height > 0) {
			BufferedImage image = bot.getCanvas().getImage().getSubimage(rect.x, rect.y, (int) rect.getWidth(),
					(int) rect.getHeight());
			ArrayList<Point> matches = new ArrayList<Point>();
			for (int x = 0; x < (int) rect.getWidth(); x++) {
				for (int y = 0; y < (int) rect.getHeight(); y++) {
					if (new Point(x, y) != null && Tools.onScreen(new Point(x, y))) {
						if (this.getDifference(new Color(image.getRGB(x, y), false), color) <= tol) {
							matches.add(new Point(x, y));
						}
					}
				}
			}

			return matches.toArray(new Point[] {});
		}
		Tools.log("Rectangle goes offscreen!");
		Tools.log(rect);
		return new Point[] {};
	}

	public Point[] getColorPoints(Polygon poly, Color color, int tol) {
		int count = 0;
		for (int x = 0; x < poly.xpoints.length; x++)
			for (int y = 0; y < poly.ypoints.length; y++)
				if (!Tools.onScreen(new Point(x, y)))
					count++;

		if (count == 0 && poly.xpoints.length > 0 && poly.ypoints.length > 0) {
			BufferedImage image = bot.getCanvas().getImage();
			ArrayList<Point> matches = new ArrayList<Point>();
			for (int x = 0; x < poly.xpoints.length; x++) {
				for (int y = 0; y < poly.ypoints.length; y++) {
					if (new Point(x, y) != null && Tools.onScreen(new Point(x, y))) {
						if (ColorUtils.getDifference(new Color(image.getRGB(x, y), false), color) <= tol) {
							matches.add(new Point(x, y));
						}
					}
				}
			}
			return matches.toArray(new Point[] {});
		}
		return new Point[] {};
	}

	public static int getDifference(Color one, Color two) {
		int[] red = { 0, 0 };
		int[] green = { 0, 0 };
		int[] blue = { 0, 0 };
		if (one != null && two != null) {
			red = new int[] { one.getRed(), two.getRed() };
			green = new int[] { one.getGreen(), two.getGreen() };
			blue = new int[] { one.getBlue(), two.getBlue() };
			return Math.abs(red[0] - red[1]) + Math.abs(green[0] - green[1]) + Math.abs(blue[0] - blue[1]);
		}
		return 1000000000;
	}

	public int getRGB(int x, int y) {
		return bot.getCanvas().getRGB(x, y);
	}
}
