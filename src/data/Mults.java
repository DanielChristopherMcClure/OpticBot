package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import utilities.Tools;
import core.Ibot;

public class Mults {

	private HashMap<String, Integer> multis = new HashMap<String, Integer>();

	public Mults() {
		try {
			this.importMultipliers();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Tools.log(multis.size() + " Multipliers Imported!");
	}

	public void importMultipliers() throws NumberFormatException, IOException {
		File data = new File("Data/multis.txt");
		if (data.exists()) {
			BufferedReader reader = new BufferedReader(new FileReader(
					"Data/multis.txt"));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.contains("~")) {
					String[] split = line.split("~");
					multis.put(split[0] + "." + split[1],
							Integer.parseInt(split[2]));
				}
			}
		}
	}

	public HashMap<String, Integer> getMultiMap() {
		return this.multis;
	}
	
	public int getMulti(String c, String f) {
		if (multis.get(c + "." + f) != null) {
			//Tools.log(c+"."+f+" * "+multis.get(c + "." + f));
			return multis.get(c + "." + f);
		}
		return 1;
	}

	public int getMulti(String methodName) {
		for (Hook h : Ibot.hooks.getHooks()) {
			if (h.getName().equals(methodName)) {
				if (multis.get(h.getClazz() + "." + h.getField()) != null)
					return multis.get(h.getClazz() + "." + h.getField());
			}
		}
		return 1;
	}
}
