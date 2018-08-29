package methods;

import java.awt.Color;
import java.awt.Rectangle;

import utilities.Tools;
import core.Ibot;

public class Tabs {

	private Ibot bot = null;

	Color red = new Color(113, 38, 29);

	static int x = 525;
	static int y = 170;

	public static Rectangle[] rects = { new Rectangle(525, 171, 30, 30),
			new Rectangle(562, 171, 30, 30), new Rectangle(595, 170, 30, 30),
			new Rectangle(625, 172, 30, 30), new Rectangle(663, 172, 30, 30),
			new Rectangle(696, 172, 30, 30), new Rectangle(731, 173, 30, 30),
			new Rectangle(527, 470, 30, 30), new Rectangle(564, 471, 30, 30),
			new Rectangle(596, 470, 30, 30), new Rectangle(630, 471, 30, 30),
			new Rectangle(664, 471, 30, 30), new Rectangle(695, 470, 30, 30),
			new Rectangle(729, 470, 30, 30) };

	public static int ATTACK = 0;
	public static int STATS = 1;
	public static int QUESTS = 2;
	public static int INVENTORY = 3;
	public static int EQUIPMENT = 4;
	public static int PRAYER = 5;
	public static int MAGIC = 6;
	public static int CLANCHAT = 7;
	public static int FRIENDS = 8;
	public static int IGNORE = 9;
	public static int LOGOUT = 10;
	public static int SETTINGS = 11;
	public static int EMOTES = 12;
	public static int MUSIC = 13;

	public Tabs(Ibot bot) {
		this.bot = bot;
	}

	public void ensureInventoryIsOpen() {
		if(this.getSelectedTab() != INVENTORY)
			this.openTab(INVENTORY);
	}
	
	public void openTab(int tab) {
		if (this.getSelectedTab() != tab && tab < rects.length) {
			while (this.getSelectedTab() != tab) {
				bot.mouse.mouseClickRectangle(rects[tab], false);
				Tools.sleep(200, 1500);
			}
		}
	}

	public int getSelectedTab() {
		int winner = 0;
		int high = 0;
		for (int i = 0; i < rects.length; i++) {
			int amt = bot.methods.color.getColorPoints(rects[i], red, 25).length;
			if (amt > high) {
				high = amt;
				winner = i;
			}
		}
		return winner;
	}

}
