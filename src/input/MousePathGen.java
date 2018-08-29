package input;
import java.awt.Point;

abstract class MousePathGen {

	/**
	 * This constructs a path from x1,y1 to x2,y2 using whatever algorithim is
	 * supplied by the child class.
	 * 
	 * @param x1
	 *            First x coord.
	 * @param y1
	 *            First y coord.
	 * @param x2
	 *            Second x coord.
	 * @param y2
	 *            Second y coord.
	 * @return The path to follow in the form of a point array.
	 */
	public abstract Point[] makeMousePath(int x1, int y1, int x2, int y2);

	/**
	 * This constructs a path from x1,y1 to x2,y2 using whatever algorithim is
	 * supplied by the child class.
	 * 
	 * @param from
	 *            First coord.
	 * @param to
	 *            Second coord.
	 * @return The path to follow in the form of a point array.
	 */
	public final Point[] makeMousePath(Point from, Point to) {
		return makeMousePath(from.x, from.y, to.x, to.y);
	}
}
