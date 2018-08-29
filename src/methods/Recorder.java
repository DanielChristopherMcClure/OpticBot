package methods;

import java.awt.Point;
import java.io.File;
import java.util.ArrayList;

import core.Ibot;
import utilities.Tools;
import wrappers.RecordedAction;

public class Recorder implements Runnable {

	private Ibot bot = null;

	public ArrayList<RecordedAction> actions = new ArrayList<RecordedAction>();
	private boolean recording = false;
	public long timer = System.currentTimeMillis();
	public int reps = 1;
	public boolean playing = false;

	public Recorder(Ibot bot) {
		this.bot = bot;
	}

	public void resetTimer() {
		timer = System.currentTimeMillis();
	}

	public void executeActions() {
		new Thread(this).start();
	}

	public void setRecord(boolean on) {
		this.recording = on;
	}

	public boolean isRecording() {
		return this.recording;
	}

	public void loadMacro(String name) {
		Tools.log("Loading Macro: " + name);
		File it = new File("Macros/" + name + ".macro");
		if (it.exists()) {
			String[] lines = Tools.getTextFrom(it);
			if (lines != null && lines.length > 0) {
				actions.clear();
				for (String s : lines) {
					actions.add(new RecordedAction(s.split("~")[0] + "~" + s.split("~")[1],
							Integer.parseInt(s.split("~")[2]), new Point(0, 0)));
				}
			} else {
				Tools.log("Unable to successfully import macro");
			}
		} else {
			Tools.log("File does not exist");
		}
	}

	public void saveCurrentMacro(String name) {
		Tools.log("Saving Current Macro as: " + name + ".macro");
		ArrayList<String> commands = new ArrayList<String>();
		for (RecordedAction a : actions)
			commands.add(a.action + "~" + a.sleep + "~" + a.loc);
		if (!new File("Macros/").exists())
			new File("Macros/").mkdirs();
		Tools.writeFile(commands.toArray(new String[] {}), new File("Macros/" + name + ".macro"));
		Tools.log("Saved!");
	}

	@Override
	public void run() {
		Tools.log("Playing Macro for " + reps + " reps");
		for (int i = 0; i < reps; i++) {
			for (RecordedAction a : actions) {
				String act = a.action;
				Tools.log("Current Action: " + act + " after sleeping for: " + a.sleep + "ms");
				Tools.sleep(a.sleep);
				if (act.contains("mouseclick")) {
					if (act.contains("1")) {
						bot.mouse.mouseClickLeft(a.loc);
					} else {
						bot.mouse.mouseClickRight(a.loc);
					}
				}
			}
			if (!playing) {
				Tools.log("Playback Stopped");
				break;
			}
		}
	}

}
