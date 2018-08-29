package methods;

import java.awt.Color;
import java.awt.Rectangle;

import core.Areas;
import core.Ibot;
import utilities.Tools;

public class Objects {

	private Ibot bot = null;

	public Objects(Ibot bot) {
		this.bot = bot;
	}

	public boolean clickGameObject(Color color, int tol, String topText) {
		boolean clicked = false;
		int count = 0;
		while (!clicked && count < Tools.random(8, 13)) {
			Rectangle[] clusters = bot.methods.clusters.getBestCluster(Areas.GAMESCREEN, new Color[] { color }, tol,
					.08);
			if (clusters != null && clusters.length > count) {
				int out = 0;
				while (!clicked && out < Tools.random(0, 3)) {
					clicked = click(clusters[count], topText);
					out++;
				}
			}
			count++;
		}
		return clicked;
	}

	private boolean click(Rectangle click, String topText) {
		if (click != null) {
			bot.methods.mouse.mouseMove(click);
			if (bot.methods.menu.getTopText().contains(topText)) {
				bot.methods.mouse.mouseClickLeft();
				return true;
			} else {
				for (String s : bot.methods.menu.getMenuItems()) {
					if (s.contains(topText)) {
						bot.methods.mouse.mouseClickRight();
						bot.methods.menu.clickItem(topText);
						return true;
					}
				}
			}
		}
		return false;
	}

}
