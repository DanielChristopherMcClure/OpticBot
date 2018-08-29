package wrappers;

import accessors.IClient;
import core.Ibot;

public class Client {

	private IClient client = null;
	private Ibot bot = null;

	public Client(Ibot bot, IClient client) {
		this.bot = bot;
		this.client = client;
	}

	public String[] getMenuOptions() {
		return client.getMenuOptions();
	}

	public String[] getMenuActions() {
		return client.getMenuActions();
	}

	public int getMenuOptionsCount() {
		return client.getMenuOptionsCount() * Ibot.mults.getMulti("getMenuOptionsCount");
	}

	public int getMenuX() {
		return client.getMenuX() * Ibot.mults.getMulti("getMenuX");
	}

	public int getMenuY() {
		return client.getMenuY() * Ibot.mults.getMulti("getMenuY");
	}

	public int getMenuWidth() {
		return client.getMenuWidth() * Ibot.mults.getMulti("getMenuWidth");
	}

	public int getMenuHeight() {
		return client.getMenuHeight() * Ibot.mults.getMulti("getMenuHeight");
	}

	public int getLoginState() {
		return client.getLoginState() * Ibot.mults.getMulti("getLoginState");
	}

	public boolean isMenuUp() {
		return client.isMenuUp();
	}

}
