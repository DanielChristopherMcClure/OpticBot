package methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import core.Ibot;
import utilities.Tools;
import wrappers.InventoryItem;

public class Inventory {

	private Ibot bot = null;
	private ArrayList<Rectangle> slots = new ArrayList<Rectangle>();
	private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();
	private Color invBackground = new Color(72, 61, 46);

	public Inventory(Ibot bot) {
		this.bot = bot;
		for (int y = 0; y < 7; y++) {
			for (int x = 0; x < 4; x++) {
				slots.add(new Rectangle(560 + (x * 43), 213 + (y * 36), 40, 34));
			}
		}
	}

	public InventoryItem[] getItems() {
		items.clear();
		for (Rectangle r : slots) {
			ArrayList<Color> colors = new ArrayList<Color>();
			for (int x = r.x; x < r.x + r.getWidth(); x++) {
				for (int y = r.y; y < r.y + r.getHeight(); y++) {
					Color c = bot.methods.color.getColorAt(new Point(x, y));
					if (bot.methods.color.getDifference(c, invBackground) > 55)
						colors.add(c);
				}
			}
			items.add(new InventoryItem(r, colors.toArray(new Color[] {})));
		}
		return items.toArray(new InventoryItem[] {});
	}

	public InventoryItem getItem(int id) {
		for (InventoryItem it : this.getItems())
			if (it.getColorId() == id)
				return it;
		return null;
	}

	public Rectangle[] getSlots() {
		return slots.toArray(new Rectangle[] {});
	}

	public void dropEverything() {
		for (Rectangle r : slots) {
			Point p = new Point((int) r.getCenterX() + Tools.random(-13, 13),
					(int) r.getCenterY() + Tools.random(-13, 13));
			bot.mouse.mouseClickRight(p);
			Tools.sleep(50, 150);
			bot.methods.menu.clickItem("Drop ");
			Tools.sleep(25, 200);
		}
	}

	public boolean isFull() {
		for (InventoryItem i : getItems())
			if (i.getColorId() == -1)
				return false;
		return true;
	}

}
