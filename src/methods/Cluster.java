package methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import core.Ibot;
import utilities.Tools;

public class Cluster {

	private ColorUtils color = null;
	private Ibot bot = null;

	public Cluster(ColorUtils c, Ibot bot) {
		color = c;
		this.bot = bot;
	}

	public Rectangle[] getAllClusters(Rectangle r, Color c, int tol) {

		Point[] points = color.getColorPoints(r, c, tol);

		ArrayList<Rectangle> screen = new ArrayList<Rectangle>();
		ArrayList<Rectangle> remove = new ArrayList<Rectangle>();

		int w = (int) (r.getWidth() * .06);
		int h = (int) (r.getHeight() * .06);

		for (int x = 0; x < (int) r.getWidth(); x += w) {
			for (int y = 0; y < (int) r.getHeight(); y += h) {
				screen.add(new Rectangle(x, y, w, h));
			}
		}

		for (Rectangle rect : screen) {
			int count = 0;
			for (Point p : points) {
				if (rect.contains(p))
					count++;
			}
			if (count == 0) {
				remove.add(rect);
			}
		}

		for (Rectangle rect : remove)
			screen.remove(rect);

		return screen.toArray(new Rectangle[] {});
	}

	public Rectangle[] getAllClusters(Rectangle r, Color c, int tol, double rectSize) {

		Point[] points = color.getColorPoints(r, c, tol);

		ArrayList<Rectangle> screen = new ArrayList<Rectangle>();
		ArrayList<Rectangle> remove = new ArrayList<Rectangle>();

		int w = (int) (r.getWidth() * rectSize);
		int h = (int) (r.getHeight() * rectSize);

		for (int x = 0; x < (int) r.getWidth(); x += w) {
			for (int y = 0; y < (int) r.getHeight(); y += h) {
				screen.add(new Rectangle(x, y, w, h));
			}
		}

		for (Rectangle rect : screen) {
			int count = 0;
			for (Point p : points) {
				if (rect.contains(p))
					count++;
			}
			if (count == 0) {
				remove.add(rect);
			}
		}

		for (Rectangle rect : remove)
			screen.remove(rect);

		return screen.toArray(new Rectangle[] {});
	}

	public Rectangle[] getAllClusters(Rectangle r, Color[] cs, int tol, double rectSize) {

		ArrayList<Rectangle> screen = new ArrayList<Rectangle>();

		for (Color c : cs) {
			Point[] points = color.getColorPoints(r, c, tol);

			ArrayList<Rectangle> remove = new ArrayList<Rectangle>();

			int w = (int) (r.getWidth() * rectSize);
			int h = (int) (r.getHeight() * rectSize);

			for (int x = 0; x < (int) r.getWidth(); x += w) {
				for (int y = 0; y < (int) r.getHeight(); y += h) {
					screen.add(new Rectangle(x, y, w, h));
				}
			}

			for (Rectangle rect : screen) {
				int count = 0;
				for (Point p : points) {
					if (rect.contains(p))
						count++;
				}
				if (count == 0) {
					remove.add(rect);
				}
			}

			for (Rectangle rect : remove)
				screen.remove(rect);
		}

		return screen.toArray(new Rectangle[] {});
	}

	public Rectangle getWinningCluster(Rectangle r, Color c, int tol) {
		Rectangle[] all = this.getAllClusters(r, c, tol);
		Point[] points = color.getColorPoints(r, c, tol);
		int big = 0;
		Rectangle winner = null;
		for (Rectangle rect : all) {
			int count = 0;
			if (Tools.onScreen(rect.getLocation())) {
				for (Point p : points) {
					if (rect.contains(p))
						count++;
				}
				if (count > big) {
					big = count;
					winner = rect;
				}
			}
		}
		return winner;
	}

	public Rectangle getWinningCluster(Rectangle r, Color c, int tol, double rectSize) {
		Rectangle[] all = this.getAllClusters(r, c, tol, rectSize);
		Point[] points = color.getColorPoints(r, c, tol);
		int big = 0;
		Rectangle winner = null;
		for (Rectangle rect : all) {
			int count = 0;
			if (Tools.onScreen(rect.getLocation())) {
				for (Point p : points) {
					if (rect.contains(p))
						count++;
				}
				if (count > big) {
					big = count;
					winner = rect;
				}
			}
		}
		return winner;
	}

	public Rectangle getWinningGameObjectCluster(Rectangle r, Color c, int tol, double rectSize) {
		Rectangle[] all = this.getAllClusters(r, c, tol, rectSize);
		Point[] points = bot.methods.getEdges();
		int big = 0;
		Rectangle winner = null;
		for (Rectangle rect : all) {
			int count = 0;
			if (Tools.onScreen(rect.getLocation())) {
				for (Point p : points) {
					if (bot.methods.color.getDifference(c, bot.methods.color.getColorAt(p)) < tol && rect.contains(p))
						count++;
				}
				if (count > big) {
					big = count;
					winner = rect;
				}
			}
		}
		return winner;
	}

	public Rectangle getWinningGameObjectCluster(Rectangle r, Color[] cs, int tol, double rectSize) {
		Rectangle[] all = this.getAllClusters(r, cs, tol, rectSize);
		Point[] points = bot.methods.getEdges();
		int big = 0;
		Rectangle winner = null;
		for (Rectangle rect : all) {
			int count = 0;
			if (Tools.onScreen(rect.getLocation())) {
				for (Point p : points) {
					for (Color c : cs)
						if (bot.methods.color.getDifference(c, bot.methods.color.getColorAt(p)) < tol
								&& rect.contains(p))
							count++;
				}
				if (count > big) {
					big = count;
					winner = rect;
				}
			}
		}
		return winner;
	}

	public Rectangle[] getBestCluster(Rectangle r, Color[] cs, int tol, double rectSize) {
		ArrayList<Rectangle> clusters = new ArrayList<Rectangle>();
		ArrayList<Rectangle> storage = new ArrayList<Rectangle>();
		Rectangle[] all = this.getAllClusters(r, cs, tol, rectSize);
		for (Rectangle rr : all)
			storage.add(rr);
		Point[] points = bot.methods.edges.getRoughEdges(r);
		while (!storage.isEmpty()) {
			int big = 0;
			Rectangle winner = null;
			for (Rectangle rect : storage) {
				int count = 0;
				if (Tools.onScreen(rect.getLocation())) {
					for (Point p : points) {
						for (Color c : cs)
							if (bot.methods.color.getDifference(c, bot.methods.color.getColorAt(p)) < tol
									&& rect.contains(p))
								count++;
					}
					if (count > big) {
						big = count;
						winner = rect;
					}
				}
			}
			if (winner != null) {
				clusters.add(winner);
				storage.remove(winner);
			} else {
				break;
			}
		}
		return clusters.toArray(new Rectangle[] {});
	}
}
