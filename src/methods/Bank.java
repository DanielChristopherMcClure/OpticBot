package methods;

import java.awt.Rectangle;

import core.Ibot;

public class Bank {

	private Ibot bot = null;

	public Bank(Ibot bot) {
		this.bot = bot;
	}

	public boolean isOpen() {
		return bot.methods.edges.getEdgeId(new Rectangle(464, 42, 28, 37)) == 17472
				&& bot.methods.edges.getEdgeId(new Rectangle(427, 302, 72, 29)) == 73610;
	}

	public void close() {

	}

}
