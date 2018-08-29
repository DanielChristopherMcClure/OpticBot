package script;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import utilities.Tools;
import core.Ibot;

public class TronicJarLoader {

	private Ibot bot = null;

	public TronicJarLoader(Ibot bot) {
		this.bot = bot;
	}

	public void setScript(File jar, String script) {
		try {
			if (jar.exists()) {
				@SuppressWarnings("deprecation")
				ClassLoader clientClassLoader = new URLClassLoader(
						new URL[] { jar.toURL() });
				Class<?> scriptClass = clientClassLoader.loadClass(script);
				Constructor<?> con;

				con = scriptClass.getConstructor(new Class[] { bot.methods
						.getClass() });
				con.setAccessible(true);
				Object it = con.newInstance(bot.methods);
				if (it instanceof OpticScript) {
					bot.script.setScript((OpticScript) it);
				}
			} else {
				Tools.log("Jar Does Not Exist: " + jar.getAbsolutePath());
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setScript(URL url, String script) {
		try {
			@SuppressWarnings("deprecation")
			ClassLoader clientClassLoader = new URLClassLoader(
					new URL[] { url });
			Class<?> scriptClass = clientClassLoader.loadClass(script);
			Constructor<?> con;

			con = scriptClass.getConstructor(new Class[] { bot.methods
					.getClass() });
			con.setAccessible(true);
			Object it = con.newInstance(bot.methods);
			if (it instanceof OpticScript) {
				bot.script.setScript((OpticScript) it);
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
