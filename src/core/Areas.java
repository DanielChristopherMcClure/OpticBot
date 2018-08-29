package core;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import utilities.Rect;

public class Areas {
	public static Rectangle PLAYER_AREA = new Rectangle(236, 136, 49, 67);
	public static Point PLAYER_ON_MINIMAP = new Point(643, 85);
	public static Point PLAYER_ON_GAMESCREEN = new Point(643, 85);
	public static Rectangle ENTIRE = new Rectangle(4, 4, 755, 490);
	public static Rectangle GAMESCREEN = new Rectangle(5, 5, 509, 330);
	public static Rectangle MINIMAP = new Rectangle(540, 8, 210, 152);
	public static Rectangle COMPASS = new Rect(543, 5, 578, 38).getBounds();
	public static Polygon ACTUAL_MINIMAP = new Polygon(
			new int[] { 628, 624, 616, 608, 599, 586, 577, 572, 573, 577, 585, 598, 613, 627, 642, 657, 675, 688, 701,
					711, 713, 711, 701, 682, 667, 658, 655, 642, },
			new int[] { 154, 145, 137, 131, 126, 117, 104, 82, 63, 50, 37, 26, 18, 12, 11, 11, 18, 28, 43, 60, 77, 97,
					116, 128, 138, 148, 154, 156, },
			28);
}
