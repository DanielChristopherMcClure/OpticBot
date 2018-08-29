package wrappers;

import java.awt.Color;
import java.awt.Rectangle;

import methods.ColorUtils;

public class InventoryItem {

	private Color[] colors = null;
	private Rectangle bounds = null;

	public InventoryItem(Rectangle bounds, Color[] colors) {
		this.colors = colors;
		this.bounds = bounds;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public int getColorId() {
		int big = -1;
		Color winner = new Color(0, 0, 0);
		for (Color c : colors) {
			int count = 0;
			for (Color cc : colors)
				if (ColorUtils.getDifference(c, cc) <= 5)
					count++;
			if (count > big) {
				big = count;
				winner = c;
			}
		}
		return big + winner.getRed() + winner.getGreen() + winner.getBlue();
	}
}
