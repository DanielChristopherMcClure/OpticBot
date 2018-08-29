package methods;

import core.Ibot;
import utilities.Tools;

public class Antiban {

	private Ibot bot = null;

	public Antiban(Ibot bot) {
		this.bot = bot;
	}

	public void antiBan() {
		switch (Tools.random(0, 2000)) {
		case 0:
			bot.methods.moveCameraRandomly();
			break;
		}
	}

}
