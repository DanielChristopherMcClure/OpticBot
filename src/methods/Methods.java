package methods;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import core.Areas;
import core.Ibot;
import input.KeyBoard;
import input.Mouse;
import utilities.Tools;

public class Methods {

	private Ibot bot = null;
	private ArrayList<String> itemData = null;

	public Mouse mouse = null;
	public KeyBoard keys = null;
	public ColorUtils color = null;
	public Cluster clusters = null;
	public Menu menu = null;
	public Tabs tabs = null;
	public Objects objects = null;
	public Inventory inventory = null;
	public Game game = null;
	public Player player = null;
	public NPCS npcs = null;
	public Edges edges = null;
	public Bank bank = null;
	public Recorder recorder = null;

	public Color[] pathColors = { new Color(96, 89, 89), new Color(81, 73, 75), new Color(130, 127, 122) };

	public Methods(Ibot bot) {
		this.bot = bot;
		keys = bot.keys;
		mouse = bot.mouse;
		color = new ColorUtils(bot);
		clusters = new Cluster(color, bot);
		menu = new Menu(bot);
		tabs = new Tabs(bot);
		objects = new Objects(bot);
		inventory = new Inventory(bot);
		game = new Game(bot);
		player = new Player(bot);
		npcs = new NPCS(bot);
		edges = new Edges(bot);
		bank = new Bank(bot);
		recorder = new Recorder(bot);
	}

	public void login() {
		Rectangle existingUser = new Rectangle(393, 273, 138, 34);
		Rectangle tryAgain = new Rectangle(311, 258, 139, 34);
		for (int i = 0; i < Tools.random(5, 65); i++) {
			if (this.getLoginState() == 0) {
				this.mouse.mouseClickRectangle(existingUser, false);
				Tools.sleep(300, 1200);
			} else {
				break;
			}
		}
		for (int i = 0; i < Tools.random(5, 65); i++) {
			if (this.getLoginState() == 2
					&& color.getColorPoints(new Rectangle(3, 457, 109, 43), new Color(58, 60, 68), 5).length > 300) {
				this.keys.typeSend(Ibot.getActive().getUsername());
				Tools.sleep(200, 600);
				this.keys.typeSend(Ibot.getActive().getPassword());
				Tools.sleep(3000, 4000);
				if (this.getLoginState() == 2 && color.getColorPoints(new Rectangle(3, 457, 109, 43),
						new Color(58, 60, 68), 5).length > 300) {
					this.mouse.mouseClickRectangle(tryAgain, false);
					Tools.sleep(500, 1000);
				}
			} else {
				break;
			}
		}
	}

	public Color[] getSelectedColors() {
		return Ibot.colorList.toArray(new Color[] {});
	}

	public int getLoginState() {
		return bot.getClient().getLoginState();
	}

	public Point[] getEdges() {
		ArrayList<Point> edges = new ArrayList<Point>();
		Rectangle rect = Areas.GAMESCREEN;
		BufferedImage image = bot.getCanvas().getImage().getSubimage(rect.x, rect.y, (int) rect.getWidth(),
				(int) rect.getHeight());
		for (int x = 0; x < (int) rect.getWidth(); x++) {
			for (int y = 0; y < (int) rect.getHeight(); y++) {
				if (x - 1 > 0 && y - 1 > 0 && rect.getWidth() > x + 1 && rect.getHeight() > y + 1) {
					Color home = new Color(image.getRGB(x, y), false);
					Point[] check = { new Point(x + 1, y), new Point(x, y + 1), new Point(x, y - 1),
							new Point(x - 1, y) };
					int count = 0;
					for (Point p : check) {
						if (color.getDifference(new Color(image.getRGB(p.x, p.y), false), home) > 35)
							count++;
					}
					if (count > 0)
						edges.add(new Point(x, y));
				}
			}
		}
		return edges.toArray(new Point[] {});
	}

	public Point[] getEdges(Rectangle rect) {
		ArrayList<Point> edges = new ArrayList<Point>();
		BufferedImage image = bot.getCanvas().getImage().getSubimage(rect.x, rect.y, (int) rect.getWidth(),
				(int) rect.getHeight());
		for (int x = 0; x < (int) rect.getWidth(); x++) {
			for (int y = 0; y < (int) rect.getHeight(); y++) {
				if (x - 1 > 0 && y - 1 > 0 && rect.getWidth() > x + 1 && rect.getHeight() > y + 1) {
					Color home = new Color(image.getRGB(x, y), false);
					Point[] check = { new Point(x + 1, y), new Point(x, y + 1), new Point(x, y - 1),
							new Point(x - 1, y) };
					int count = 0;
					for (Point p : check) {
						if (color.getDifference(new Color(image.getRGB(p.x, p.y), false), home) > 35)
							count++;
					}
					if (count > 0)
						edges.add(new Point(x, y));
				}
			}
		}
		return edges.toArray(new Point[] {});
	}

	public int getCompassPosition() {
		Color N = new Color(49, 41, 29);
		Point top = new Point(563, 3);
		Point[] points = color.getColorPoints(Areas.COMPASS, N, 5);
		if (points != null && points.length > 0) {
			if (new Point(Areas.COMPASS.x + points[0].x, Areas.COMPASS.y + points[0].y).x > top.x)
				return (int) top.distance(new Point(Areas.COMPASS.x + points[0].x, Areas.COMPASS.y + points[0].y));
			else
				return (-1)
						* (int) top.distance(new Point(Areas.COMPASS.x + points[0].x, Areas.COMPASS.y + points[0].y));
		}
		return -1;
	}

	public Point[] getPathLocationsOnMM() {
		ArrayList<Point> pathPoints = new ArrayList<Point>();
		for (Color c : pathColors) {
			for (Point p : color.getColorPoints(Areas.MINIMAP, c, 65)) {
				Point[] check = { new Point(p.x - 1, p.y - 1), new Point(p.x - 1, p.y + 1), new Point(p.x + 1, p.y - 1),
						new Point(p.x + 1, p.y + 1) };
				int count = 0;
				for (Point cp : check)
					if (color.getDifference(color.getColorAt(
							new Point(cp.x + Areas.MINIMAP.getLocation().x, cp.y + Areas.MINIMAP.getLocation().y)),
							c) > 40)
						count++;
				if (count == 0 && Areas.ACTUAL_MINIMAP
						.contains(new Point(p.x + Areas.MINIMAP.getLocation().x, p.y + Areas.MINIMAP.getLocation().y)))
					pathPoints.add(new Point(p.x + Areas.MINIMAP.getLocation().x, p.y + Areas.MINIMAP.getLocation().y));
			}
		}
		return pathPoints.toArray(new Point[] {});
	}

	public Point[] getNpcLocationsOnMM() {
		ArrayList<Point> npcs = new ArrayList<Point>();
		Color npc = new Color(245, 245, 0);
		Point[] all = color.getColorPoints(Areas.MINIMAP, npc, 10);
		for (Point p : all) {
			int count = 0;
			for (Point pp : npcs)
				if (pp.distance(p) < 2)
					count++;
			if (count == 0)
				npcs.add(new Point(p.x + Areas.MINIMAP.getLocation().x, p.y + Areas.MINIMAP.getLocation().y));
		}
		return npcs.toArray(new Point[] {});
	}

	public Image getItemImage(int id) {
		try {
			String base = "http://www.2007rshelp.com";
			String url = "http://2007rshelp.com/items.php?id=";
			String source = Tools.getUrlSource(url + id);
			String[] split = source.split("<img src=");
			for (String s : split) {
				if (s.contains("idbimg")) {
					String ext = "";
					boolean on = false;
					for (char c : s.toCharArray()) {
						if (c == '"') {
							if (!on)
								on = true;
							else
								break;
						} else {
							if (on) {
								ext += c;
							}
						}
					}
					Image img = Tools.getImageFromURL((base + ext).replace(" ", "%20"));
					if (img != null)
						return img;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getItemId(String name) {
		if (this.itemData == null) {
			ArrayList<String> itd = new ArrayList<String>();
			String[] lines = Tools.getTextFrom(new File("Data/items.txt"));
			if (lines != null && lines.length > 0) {
				for (String s : lines)
					if (s != null)
						itd.add(s);
			} else {
				Tools.log("Item List not found!");
			}
			this.itemData = itd;
			return getItemId(name);
		} else {
			for (String s : this.itemData) {
				String iname = s.split("~")[0];
				int id = Integer.parseInt(s.split("~")[1]);
				if (iname.equals(name))
					return id;
			}
		}
		return -1;
	}

	public void moveCameraRandomly() {
		switch (Tools.random(0, 1)) {
		case 0:
			bot.keys.pressKey((char) KeyEvent.VK_RIGHT);
			Tools.sleep(10, 2000);
			bot.keys.releaseKey((char) KeyEvent.VK_RIGHT);
			break;
		case 1:
			bot.keys.pressKey((char) KeyEvent.VK_LEFT);
			Tools.sleep(10, 2000);
			bot.keys.releaseKey((char) KeyEvent.VK_LEFT);
			break;
		}
	}

}
