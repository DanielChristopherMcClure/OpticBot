package script;

import input.KeyBoard;
import input.Mouse;
import methods.Bank;
import methods.Cluster;
import methods.ColorUtils;
import methods.Inventory;
import methods.Menu;
import methods.Methods;
import methods.NPCS;
import methods.Objects;
import methods.Player;
import methods.Tabs;

public class TronicAPI {

	public Menu menu = null;
	public Objects objects = null;
	public ColorUtils color = null;
	public Cluster clusters = null;
	public Tabs tabs = null;
	public Inventory inventory = null;
	public Mouse mouse = null;
	public ScriptHandler script = null;
	public KeyBoard keyboard = null;
	public Methods methods = null;
	public Player player = null;
	public NPCS npcs = null;
	public Bank bank = null;

	public TronicAPI(Methods methods) {
		this.methods = methods;
		keyboard = methods.keys;
		mouse = methods.mouse;
		menu = methods.menu;
		objects = methods.objects;
		color = methods.color;
		clusters = methods.clusters;
		tabs = methods.tabs;
		inventory = methods.inventory;
		player = methods.player;
		npcs = methods.npcs;
		bank = methods.bank;
	}

}
