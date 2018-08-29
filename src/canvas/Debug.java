package canvas;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import core.Areas;
import core.Ibot;
import input.Mouse;
import interfaces.Paintable;
import methods.Tabs;
import ui.TronicUI;
import utilities.Rect;
import utilities.Tools;
import wrappers.InventoryItem;

public class Debug implements Paintable {

	public RSCanvas canv = null;

	public Image mousePaint = Tools.getLocalImage("Data/Images/mouse.png");
	public Image mousePressed = Tools.getLocalImage("Data/Images/mouseClick.png");
	public Image mousePaintRandom = Tools.getLocalImage("Data/Images/mouseRandom.png");
	public Image mousePressedRandom = Tools.getLocalImage("Data/Images/mouseClickRandom.png");
	public Image splash = Tools.getLocalImage("Data/Images/mouse.png");

	public static int count = 0;
	public static Point pressPoint = null;
	public static String mouseColor = "red";
	public static ArrayList<Point> poly = new ArrayList<Point>();
	public static Point[] debugRect = new Point[] { new Point(0, 0), new Point(0, 0) };

	public Debug(RSCanvas canv) {
		this.canv = canv;
	}

	public void drawMousePaint(Graphics g) {
		// if (Mouse.current != null && Mouse.current.length > 0) {
		// for (int i = 0; i + 1 < Mouse.current.length; i++) {
		// Point one = Mouse.current[i];
		// Point two = Mouse.current[i + 1];
		// g.setColor(Color.BLACK);
		// g.drawLine(one.x, one.y, two.x, two.y);
		// }
		// }
		g.drawImage(this.getMouseImage(), canv.client.mouse.getMouseX() - 12, canv.client.mouse.getMouseY() - 12, null);
		if (Debug.pressPoint != null) {
			if (count < 80) {
				Graphics2D gg = (Graphics2D) g;
				gg.setComposite(
						AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ((float) (81 - count / 2) / 100) * 1));
				gg.drawImage(this.getMousePressedImage(), pressPoint.x - 12, pressPoint.y - 12, null);
				count++;
			} else {
				count = 0;
				pressPoint = null;
			}
		}
	}

	public Image getMouseImage() {
		if (Debug.mouseColor.contains("white"))
			return this.mousePaintRandom;
		return this.mousePaint;
	}

	public Image getMousePressedImage() {
		if (Debug.mouseColor.contains("white"))
			return this.mousePressedRandom;
		return this.mousePressed;
	}

	public void paintColorClustersDebug(Graphics g) {
		Rectangle[] rects = canv.client.methods.clusters.getAllClusters(Areas.ENTIRE,
				canv.client.methods.color.getColorAt(canv.client.mouse.getMouseXY()), 15, .11);
		for (Rectangle r : rects) {
			if (r != null && Tools.onScreen(r.getLocation())) {
				g.setColor(new Color(0, 255, 0, 100));
				g.drawRect(r.x, r.y, r.width, r.height);
			}
		}
		Rectangle winner = canv.client.methods.clusters.getWinningCluster(Areas.ENTIRE,
				canv.client.methods.color.getColorAt(canv.client.mouse.getMouseXY()), 15, .05);
		if (winner != null && Tools.onScreen(winner.getLocation())) {
			g.setColor(Color.RED);
			g.drawRect(winner.x, winner.y, winner.width, winner.height);
		}

	}

	public void drawLastClick(Graphics g) {
		if (canv.lastClick != null) {
			g.setColor(canv.lastClick);
			g.fillRect(250, 15, 255, 50);
			g.setColor(Color.WHITE);
			Tools.drawSharpText("" + canv.lastClick, 255, 30, Color.WHITE, Color.BLACK, g);
		}
	}

	public void drawColorRect(Graphics g) {
		g.setColor(new Color(0, 255, 0, 200));
		if (canv.click1 != null) {
			Point p = canv.click1;
			g.fillRect(p.x - 2, p.y - 2, 5, 5);
			Tools.drawSharpText("Top Left", p.x, p.y, Color.WHITE, Color.black, g);
		}
		if (canv.click2 != null) {
			Point p = canv.click2;
			g.fillRect(p.x - 2, p.y - 2, 5, 5);
			Tools.drawSharpText("Bottom Right", p.x, p.y, Color.WHITE, Color.black, g);
		}
		if (canv.click1 != null && canv.click2 != null) {
			g.setColor(new Color(255, 0, 0, 100));
			Rectangle r = new Rect(canv.click1, canv.click2).getBounds();
			g.fillRect(r.x, r.y, r.width, r.height);
			Tools.drawSharpText(canv.client.methods.color.getColorAt(canv.client.methods.mouse.getMouseXY()).toString()
					.replace("java.awt.Color", "")
					+ " = "
					+ canv.client.methods.color.getColorPoints(r,
							canv.client.methods.color.getColorAt(canv.client.methods.mouse.getMouseXY()), 15).length,
					r.x + 5, r.y + 5, Color.WHITE, Color.BLACK, g);
			Tools.drawSharpText("   Rect: " + r.x + "," + r.y + "," + r.width + "," + r.height, r.x + 5, r.y + 20,
					Color.WHITE, Color.BLACK, g);
		}
	}

	public void drawMMNpcs(Graphics g) {
		Point[] ps = canv.client.methods.getNpcLocationsOnMM();
		if (ps != null && ps.length > 0) {
			g.setColor(Color.RED);
			for (Point p : ps) {
				g.fillOval(p.x - 2, p.y - 2, 5, 5);
			}
		}
	}

	public void drawMMPaths(Graphics g) {
		Point[] ps = canv.client.methods.getPathLocationsOnMM();
		if (ps != null && ps.length > 0) {
			g.setColor(new Color(0, 0, 255, 100));
			for (Point p : ps) {
				g.fillOval(p.x - 2, p.y - 2, 5, 5);
			}
		} else {
			Tools.log("MM path points = 0 or null");
		}
		Color N = new Color(49, 41, 29);
		Point top = new Point(563, 3);
		Point[] points = canv.client.methods.color.getColorPoints(Areas.COMPASS, N, 5);
		if (points != null && points.length > 0) {
			g.setColor(Color.CYAN);
			g.drawLine(top.x, top.y, Areas.COMPASS.x + points[0].x, Areas.COMPASS.y + points[0].y);
		}
		Tools.drawSharpText("" + canv.client.methods.getCompassPosition(), Areas.COMPASS.x, Areas.COMPASS.y + 15,
				Color.RED, Color.BLACK, g);
	}

	public void drawEdges(Graphics g) {
		Point[] edges = canv.client.methods.edges.getRoughEdges(Areas.GAMESCREEN);
		if (edges != null) {
			for (Point p : edges) {
				if (Tools.onScreen(p)) {
					g.setColor(Color.CYAN);
					g.drawOval(p.x, p.y, 1, 1);
				}
			}
		}
	}

	public void drawInventoryItems(Graphics g) {
		for (InventoryItem it : canv.client.methods.inventory.getItems()) {
			Rectangle s = it.getBounds();
			g.setColor(Color.GREEN);
			g.drawRect(s.x, s.y, s.width, s.height);
			Tools.drawSharpText("" + it.getColorId(), s.x + 5, s.y + 15, Color.WHITE, Color.BLACK, g);
		}
	}

	public void drawSpline(Graphics g) {
		if (Mouse.current != null && Mouse.current.length > 0) {
			g.setColor(Color.BLACK);
			for (Point p : Mouse.current)
				g.fillOval(p.x, p.y, 1, 1);
		}
	}

	public void debugGame(Graphics g) {
		Tools.drawSharpText("Approx Health: " + canv.client.methods.game.getHealth() + "%", 605, 15, Color.WHITE,
				Color.BLACK, g);
		Tools.drawSharpText("Game State: " + canv.client.methods.game.getGameState(), 605, 30, Color.WHITE,
				Color.BLACK, g);
		Tools.drawSharpText("Selected Tab: " + canv.client.methods.tabs.getSelectedTab(), 605, 45, Color.WHITE,
				Color.BLACK, g);
		Tools.drawSharpText("Bank Open: " + canv.client.methods.bank.isOpen(), 605, 60, Color.WHITE, Color.BLACK, g);
	}

	public void debugSelectedColors(Graphics g) {
		if (TronicUI.drawSelectedColors.isSelected()) {
			if (Ibot.colorList != null && Ibot.colorList.size() > 0) {
				Color[] cs = Ibot.colorList.toArray(new Color[] {});
				int y = 155;
				for (Color c : cs) {
					Tools.drawSharpText("SELECTED COLOR", 15, y, c, Color.BLACK, g);
					y += 15;
					g.setColor(Color.MAGENTA);
					Point[] ps = canv.client.methods.color.getColorPoints(Areas.GAMESCREEN, c, 15);
					if (ps != null && ps.length > 0) {
						for (Point p : ps) {
							if (p != null) {
								g.fillRect(p.x, p.y, 2, 2);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		if (!canv.client.isAuthenticated()) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 800, 800);
			g.drawImage(splash, 0, 0, null);
		}
		if (TronicUI.debugClusters.isSelected())
			this.paintColorClustersDebug(g);
		if (TronicUI.debugMouseColor.isSelected()) {
			Tools.drawSharpText("Mouse Color: " + canv.client.methods.color.getColorAt(canv.client.mouse.getMouseXY())
					+ "  At: " + canv.client.mouse.getMouseXY(), 15, 45, Color.RED, Color.BLACK, g);
			g.setColor(canv.client.methods.color.getColorAt(canv.client.mouse.getMouseXY()));
			g.fillRect(15, 55, 15, 15);
		}
		if (TronicUI.debugLastClick.isSelected())
			this.drawLastClick(g);
		if (TronicUI.debugRect.isSelected())
			this.drawColorRect(g);
		if (TronicUI.debugLoginState.isSelected())
			Tools.drawSharpText("Login State: " + canv.client.methods.getLoginState(), 15, 75, Color.RED, Color.BLACK,
					g);
		if (TronicUI.debugTopText.isSelected())
			Tools.drawSharpText("TopText: " + canv.client.methods.menu.getTopText(), 15, 100, Color.RED, Color.BLACK,
					g);

		if (TronicUI.debugEdges.isSelected()) {
			this.drawEdges(g);
		}
		if (TronicUI.debugMM.isSelected()) {
			this.drawMMPaths(g);
			this.drawMMNpcs(g);
		}

		if (TronicUI.debugInv.isSelected() && canv.client.methods.tabs.getSelectedTab() == Tabs.INVENTORY) {
			this.drawInventoryItems(g);
		}

		if (TronicUI.debugPoly.isSelected()) {
			int[] xs = new int[poly.size()];
			int[] ys = new int[poly.size()];
			for (int i = 0; i < poly.size(); i++) {
				xs[i] = poly.get(i).x;
				ys[i] = poly.get(i).y;
			}
			g.setColor(Color.CYAN);
			g.drawPolygon(xs, ys, poly.size());
			g.setColor(Color.RED);
			for (Point p : poly)
				g.fillOval(p.x - 2, p.y - 2, 5, 5);
		} else if (!poly.isEmpty()) {
			poly.clear();
		}

		if (TronicUI.debugGame.isSelected()) {
			this.debugGame(g);
		}

		if (TronicUI.drawRect.isSelected()) {
			Tools.drawSharpText("Right Click To Change Rectangle", 300, 15, Color.CYAN, Color.BLACK, g);
			Rectangle r = new Rect(debugRect[0].x, debugRect[0].y, debugRect[1].x, debugRect[1].y).getBounds();
			g.setColor(Color.CYAN);
			g.drawRect(r.x, r.y, r.width, r.height);
			Tools.drawSharpText("" + r, r.x, r.y, Color.RED, Color.BLACK, g);
		}

		this.debugSelectedColors(g);

		if (canv.client.script.tronic != null) {
			canv.client.script.tronic.paint(g);
		}

		// this.drawSpline(g);
		this.drawMousePaint(g);
	}
}
