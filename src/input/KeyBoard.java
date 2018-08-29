package input;

import java.awt.Canvas;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import utilities.Tools;
import core.Ibot;

public class KeyBoard implements KeyListener {

	char[] leftSideChars = { '~', '!', '@', '#', '%', '^', '`', '1', '2', '3',
			'4', '5', '6', 'a', 's', 'd', 'f', 'g', 'q', 'w', 'e', 'r', 't',
			'z', 'x', 'c', 'v', 'b' };
	public int minSleepTime;
	public int maxSleepTime;
	public int minReleaseSleepTime;
	public int maxReleaseSleepTime;
	ArrayList<Character> specialChars;

	Ibot client = null;

	public KeyBoard(Ibot client) {
		this.client = client;
		this.minSleepTime = Tools.random(25, 30);
		this.maxSleepTime = Tools.random(62, 70);
		this.minReleaseSleepTime = Tools.random(10, 14);
		this.maxReleaseSleepTime = Tools.random(25, 35);

		char[] spChars = { '~', '!', '@', '#', '%', '^', '&', '*', '(', ')',
				'_', '+', '{', '}', ':', '<', '>', '?', '"', '|' };
		specialChars = new ArrayList<Character>(spChars.length);
		for (int x = 0; x < spChars.length; ++x)
			specialChars.add(spChars[x]);
	}

	public void typeString(String str) {
		// Tools.log("Typing: " + str);
		char[] c = str.toCharArray();
		// try {
		// Thread.sleep(Tools.random(250, 400));
		// } catch (InterruptedException e1) {
		// e1.printStackTrace();
		// }
		for (int i = 0; i < c.length; i++) {
			try {

				KeyEvent[] e = createKeyEvents(client.getCanvas(), c[i]);

				sendKeyEvent(e[0]);
				Thread.sleep(Tools.random(this.minReleaseSleepTime,
						this.maxReleaseSleepTime));

				sendKeyEvent(e[1]);

				sendKeyEvent(e[2]);
				Thread.sleep(Tools.random(this.minReleaseSleepTime,
						this.maxReleaseSleepTime));

				Thread.sleep(Tools.random(this.minSleepTime, this.maxSleepTime));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

	}

	public void typeSend(String str) {
		typeString(str);
		pressEnter();
	}

	public void pressEnter() {
		try {
			Canvas target = client.getCanvas();
			sendKeyEvent(new KeyEvent(target, KeyEvent.KEY_PRESSED,
					System.currentTimeMillis(), 0, 10, (char) 10,
					KeyEvent.KEY_LOCATION_STANDARD));
			Thread.sleep(Tools.random(this.minReleaseSleepTime,
					this.maxReleaseSleepTime));

			sendKeyEvent(new KeyEvent(target, KeyEvent.KEY_TYPED,
					System.currentTimeMillis(), 0, 0, (char) 0,
					KeyEvent.KEY_LOCATION_UNKNOWN));

			sendKeyEvent(new KeyEvent(target, KeyEvent.KEY_RELEASED,
					System.currentTimeMillis(), 0, 10, (char) 10,
					KeyEvent.KEY_LOCATION_STANDARD));
			Thread.sleep(Tools.random(this.minReleaseSleepTime,
					this.maxReleaseSleepTime));

			Thread.sleep(Tools.random(this.minSleepTime, this.maxSleepTime));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public void pressKey(char key) {
		try {
			sendKeyEvent(new KeyEvent(client.getCanvas(), KeyEvent.KEY_PRESSED,
					System.currentTimeMillis(), 0, key, key,
					KeyEvent.KEY_LOCATION_STANDARD));
			Thread.sleep(Tools.random(this.minReleaseSleepTime,
					this.maxReleaseSleepTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void releaseKey(char key) {
		try {
			sendKeyEvent(new KeyEvent(client.getCanvas(),
					KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, key,
					key, KeyEvent.KEY_LOCATION_STANDARD));
			Thread.sleep(Tools.random(this.minReleaseSleepTime,
					this.maxReleaseSleepTime));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public KeyEvent[] createKeyEvents(Component target, char c) {
		KeyEvent pressKey = new KeyEvent(target, KeyEvent.KEY_PRESSED,
				System.currentTimeMillis(), 0, 0, c,
				KeyEvent.KEY_LOCATION_STANDARD);

		KeyEvent typeKey = new KeyEvent(target, KeyEvent.KEY_TYPED,
				System.currentTimeMillis(), 0, 0, c);

		KeyEvent releaseKey = new KeyEvent(target, KeyEvent.KEY_RELEASED,
				System.currentTimeMillis(), 0, 0, c,
				KeyEvent.KEY_LOCATION_STANDARD);

		KeyEvent[] e = { pressKey, typeKey, releaseKey };
		return e;
	}

	public void sendKeyEvent(KeyEvent event) {
		long t = System.currentTimeMillis();
		this.client.getCanvas().processEvent(event);
	}

	public void resetSpeeds() {
		this.minSleepTime = Tools.random(25, 30);
		this.maxSleepTime = Tools.random(62, 70);
		this.minReleaseSleepTime = Tools.random(10, 14);
		this.maxReleaseSleepTime = Tools.random(25, 35);
	}

	@Override
	public void keyPressed(KeyEvent e) {
	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void moveCamera(int degree) {
		switch (Tools.random(0, 1)) {
		case 0:
			this.pressKey((char) KeyEvent.VK_LEFT);
			Tools.sleep(500 * degree);
			this.releaseKey((char) KeyEvent.VK_LEFT);
			break;
		case 1:
			this.pressKey((char) KeyEvent.VK_RIGHT);
			Tools.sleep(500 * degree);
			this.releaseKey((char) KeyEvent.VK_RIGHT);
			break;
		}
	}
}
