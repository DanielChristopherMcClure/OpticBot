package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import utilities.Tools;

public class Hooks {

	private ArrayList<Hook> hooks = new ArrayList<Hook>();

	public Hooks() {
		try {
			this.importHooks();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tools.log(hooks.size() + " Hooks imported!");
	}

	public void importHooks() throws IOException {
		File loc = new File("Data/hooks.txt");
		if (loc.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(
					"Data/hooks.txt"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.split("-").length > 2) {
					String[] split = line.split("-");
					hooks.add(new Hook(split[0], split[1], split[2]));
				}
			}
		} else {
			Tools.log("Hooks File Not Found!");
		}
	}

	public Hook getHook(String method) {
		for (Hook h : hooks)
			if (h.getName().equals(method))
				return h;
		return null;
	}

	public ArrayList<Hook> getHooks() {
		return hooks;
	}

}
