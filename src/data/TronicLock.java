package data;

import java.io.File;
import java.util.ArrayList;

import utilities.Tools;

public class TronicLock {

	public static String[] getFileTextUnlocked(File f) {
		if (f.exists()) {
			String[] locked = Tools.getTextFrom(f);
			ArrayList<String> unlocked = new ArrayList<String>();
			for (String s : locked)
				unlocked.add(TronicLock.unlock(s));
			return unlocked.toArray(new String[] {});
		}
		return null;
	}

	public static void writeFile(String[] lines) {
		ArrayList<String> ls = new ArrayList<String>();
		for (String s : lines)
			ls.add(TronicLock.lock(s));
		Tools.writeFile(ls.toArray(new String[] {}), new File("test.txt"));
	}

	public static String unlock(String input) {
		String result = "";
		for (char c : input.toCharArray())
			result += (char) (c - 5);
		return result;
	}

	public static String lock(String input) {
		String result = "";
		for (char c : input.toCharArray())
			result += (char) (c + 5);
		return result;
	}

}
