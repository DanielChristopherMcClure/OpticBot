package methods;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import core.Areas;
import core.Ibot;

public class NPCS {

	private Ibot bot = null;

	public NPCS(Ibot bot) {
		this.bot = bot;
	}

	public Point[] getNPCLocationsOnMM() {
		return bot.methods.getNpcLocationsOnMM();
	}	

}
