package methods;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

import core.Ibot;
import reflection.ReflectedObject;
import utilities.Tools;

public class Game {

	private Ibot bot = null;

	public Game(Ibot bot) {
		this.bot = bot;
	}
	
	public int getGameState() {
		int val = -1;
		try {
			for (ReflectedObject o : bot.getReflectedObjects()) {
				if (o.fn.access == 8 && o.fn.desc.equals("I")) {
					if (o.getValue(null) != null) {
						int v = o.getIntValueWithMult(null);
						if(v == 10 || v == 20 || v == 25 || v== 30)
							return v;
					}
				}
			}
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return val;
	}

	public int getHealth() {
		Point[] points = new Point[] { new Point(550, 62), new Point(548, 62), new Point(545, 61), new Point(543, 59),
				new Point(542, 55), new Point(542, 54), new Point(542, 51), new Point(542, 49), new Point(544, 47),
				new Point(548, 46), new Point(550, 44), new Point(547, 63), new Point(547, 61), new Point(547, 61),
				new Point(544, 58), new Point(542, 58), new Point(540, 56), new Point(540, 55), new Point(540, 54),
				new Point(540, 53), new Point(541, 50), new Point(551, 62), new Point(545, 59), new Point(541, 54),
				new Point(540, 52), new Point(542, 51), new Point(542, 50) };
		double red = 0;
		for (Point p : points) {
			Color c = bot.methods.color.getColorAt(p);
			if (c.getRed() > 80)
				red++;
		}
		return (int) ((red / (double) points.length) * (double) 100);
	}
}
