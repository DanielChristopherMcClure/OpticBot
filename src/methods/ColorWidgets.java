package methods;

import java.awt.Rectangle;
import java.util.ArrayList;

import core.Ibot;

public class ColorWidgets {

	private Ibot bot = null;
	private ColorUtils color = null;

	public ColorWidgets(Ibot bot) {
		this.bot = bot;
		this.color = bot.methods.color;
	}

	public Rectangle[] getWidgets() {
		ArrayList<Rectangle> widgets = new ArrayList<Rectangle>();
		return widgets.toArray(new Rectangle[] {});
	}

}
