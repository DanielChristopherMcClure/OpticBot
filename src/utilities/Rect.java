package utilities;

import java.awt.Point;
import java.awt.Rectangle;

public class Rect {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	int x, y, x2, y2;

	public Rect(Point one, Point two) {
		this.x = one.x;
		this.y = one.y;
		this.x2 = two.x;
		this.y2 = two.y;
	}

	public Rect(int x, int y, int x2, int y2) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
	}

	public Rectangle getBounds() {
		return new Rectangle(x, y, (Math.abs(x - x2)), (Math.abs(y - y2)))
				.getBounds();
	}

}
