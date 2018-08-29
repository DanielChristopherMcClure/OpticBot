package methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import core.Areas;
import core.Ibot;
import utilities.Tools;

public class Player {

	private Ibot bot = null;

	public Player(Ibot bot) {
		this.bot = bot;
	}

	public int getPixelChange(int sleep) {
		int amt = 0;
		ArrayList<Color> colors = new ArrayList<Color>();
		for (int x = Areas.PLAYER_AREA.x; x < Areas.PLAYER_AREA.x + Areas.PLAYER_AREA.width; x++) {
			for (int y = Areas.PLAYER_AREA.y; y < Areas.PLAYER_AREA.y + Areas.PLAYER_AREA.height; y++) {
				colors.add(bot.methods.color.getColorAt(new Point(x, y)));
			}
		}
		Tools.sleep(sleep);
		ArrayList<Color> colors2 = new ArrayList<Color>();
		for (int x = Areas.PLAYER_AREA.x; x < Areas.PLAYER_AREA.x + Areas.PLAYER_AREA.width; x++) {
			for (int y = Areas.PLAYER_AREA.y; y < Areas.PLAYER_AREA.y + Areas.PLAYER_AREA.height; y++) {
				colors2.add(bot.methods.color.getColorAt(new Point(x, y)));
			}
		}
		for (int i = 0; i < colors.size(); i++) {
			amt += ColorUtils.getDifference(colors.get(i), colors2.get(i));
		}
		return amt;
	}

}
