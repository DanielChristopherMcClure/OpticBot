package methods;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import utilities.Tools;
import core.Ibot;

public class Menu {

	private Ibot bot = null;

	public Menu(Ibot i) {
		bot = i;
	}

	public boolean clickItem(String action) {
		if (this.isMenuUp() && this.menuContains(action)) {
			int x = bot.getClient().getMenuX();
			int y = bot.getClient().getMenuY();
			x += 4;
			y += 4;
			int menuOptionIndex = getMenuIndex(action);
			if (menuOptionIndex == -1) {
				menuOptionIndex = getMenuIndex("Cancel");
			}
			int xOff = Tools.random(4, 4 + Tools.random(0,
					(getMenuItems().get(menuOptionIndex).length() * 4) - 5));
			int yOff = 20 + (15 * menuOptionIndex - 1) + Tools.random(2, 10);
			//Tools.log(x + xOff + ", " + y + yOff);
			bot.mouse.mouseMove(x + xOff, y + yOff, 1, 1);
			bot.mouse.mouseClickLeft();
			return true;
		} else if (this.isMenuUp() && !this.menuContains(action)) {
			Tools.log("Action not in menu: " + action);
			this.moveOffMenu();
		}
		return false;
	}

	public void clickMenuIndex(int index) {
		if (this.isMenuUp()) {
			int x = bot.getClient().getMenuX();
			int y = bot.getClient().getMenuY();
			x += 4;
			y += 4;
			int menuOptionIndex = index;
			if (menuOptionIndex == -1) {
				menuOptionIndex = getMenuIndex("Cancel");
			}
			int xOff = Tools.random(4, 4 + Tools.random(0,
					(getMenuItems().get(menuOptionIndex).length() * 4) - 5));
			int yOff = 20 + (15 * menuOptionIndex - 1) + Tools.random(2, 10);
			bot.mouse.mouseMove(x + xOff, y + yOff, 1, 1);
			bot.mouse.mouseClickLeft();
		}
	}

	public boolean menuContains(String action) {
		if (this.isMenuUp()) {
			for (String s : this.getMenuItems()) {
				if (s != null && s.contains(action))
					return true;
			}
		}
		return false;
	}

	/**
	 * @return The menu items (Options + Actions)
	 */
	public ArrayList<String> getMenuItems() {
		String[] options = bot.getClient().getMenuOptions();
		String[] actions = bot.getClient().getMenuActions();
		int offset = bot.getClient().getMenuOptionsCount();
		ArrayList<String> output = new ArrayList<String>();
		for (int i = offset - 1; i >= 0; i--) {
			String option = options[i];
			String action = actions[i];
			if (option != null && action != null) {
				String text = action + ' ' + option;
				output.add(text.replace("<col=ffff00>", "")
						.replace("<col=ff00>", "").replace("<col=ffff>", "")
						.replace("<col=ff9040>", "")
						.replace("<col=ffffff>", "")
						.replace("<col=40ff00>", "")
						.replace("<col=ff0000>", "").replace("<col=ff3000", ""));
			}
		}
		return output;
	}

	public boolean isMenuUp() {
		return bot.getClient().isMenuUp();
	}

	public Rectangle getMenuBounds() {
		return new Rectangle(bot.getClient().getMenuX(), bot.getClient()
				.getMenuY(), bot.getClient().getMenuWidth(), bot.getClient()
				.getMenuHeight());
	}

	public String getTopText() {
		ArrayList<String> items = this.getMenuItems();
		if (items != null && items.size() > 0)
			return items.get(0);
		return "N/A";
	}

	public void moveOffMenu() {
		Point closest = null;
		while (closest == null) {
			int small = 10000000;
			Point[] points = new Point[30];
			for (int i = 0; i < 30; i++)
				points[i] = new Point(bot.mouse.getMouseX()
						+ Tools.random(-150, 150), bot.mouse.getMouseY()
						+ Tools.random(-150, 150));
			for (Point p : points) {
				if (p != null && Tools.onScreen(p)
						&& !this.getMenuBounds().contains(p)) {
					int dis = (int) p.distance(bot.mouse.getMouseXY());
					if (dis < small) {
						small = dis;
						closest = p;
					}
				}
			}
		}
		if (closest != null) {
			bot.mouse.mouseMove(closest);
		}
	}

	public int getMenuIndex(String optionContains) {
		java.util.List<String> actions = getMenuItems();
		for (int i = 0; i < actions.size(); i++) {
			String action = actions.get(i);
			if (action.contains(optionContains)) {
				return i;
			}
		}
		return -1;
	}

}