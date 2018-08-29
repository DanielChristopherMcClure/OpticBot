package updater;

import core.Ibot;
import reflection.ReflectedObject;
import utilities.Tools;

public class Updater {

	private Ibot bot = null;

	public Updater(Ibot bot) {
		this.bot = bot;
	}

	public void update() throws ClassNotFoundException, NoSuchFieldException, SecurityException {
		boolean actions = false,options = false;
		loop: for (ReflectedObject o : bot.getReflectedObjects()) {
			if (o.fn.access == 8 && o.fn.desc.equals("[Ljava/lang/String;")) {
				if (o.getValue(null) != null) {
					String[] val = (String[]) o.getArray();
					if (val != null && val.length > 300 && val.length < 550) {
						int count = 0;
						for (String s : val) {
							if (!actions && s != null && s.contains("Walk here")) {
								Tools.log("getMenuActions: " + o.cn.name + "." + o.fn.name);
								actions = true;
								count++;
							}
						}
						if (!options && count == 0) {
							Tools.log("getMenuOptions: " + o.cn.name + "." + o.fn.name);
							options = true;
						}
						
						if(actions && options)
							break loop;

					}
				}
			}
		}



	}

}
