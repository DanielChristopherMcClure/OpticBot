package script;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import methods.Methods;
import utilities.Tools;
import core.Ibot;

public class ScriptHandler {

	public OpticScript tronic = null;

	Thread tronicThread = null;

	Ibot client = null;

	public boolean paused = false;

	public ScriptHandler(Ibot client) {
		this.client = client;
	}

	public void resumeScript() {
		if (tronicThread != null) {
			tronicThread.resume();
			paused = false;
		}
	}

	public void pauseScript() {
		if (tronicThread != null) {
			tronicThread.suspend();
			paused = true;
		}
	}

	public void stopScript() {
		if (tronicThread != null) {
			if (tronic != null)
				tronic.onEnd();
			tronicThread.stop();
			tronic = null;
		}
	}

	public void setScript(File script) throws MalformedURLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (script.exists()) {
			ClassLoader parentClassLoader = FluxClassLoader.class
					.getClassLoader();
			FluxClassLoader classLoader = new FluxClassLoader(parentClassLoader);
			Class myObjectClass = classLoader.loadClass(
					script.getAbsolutePath(),
					script.getName().replace(".class", ""));

			try {

				Tools.log("Reflecting Script");

				Methods api = client.methods;

				Constructor<?> con = myObjectClass
						.getConstructor(new Class[] { api.getClass() });
				con.setAccessible(true);

				if (client.isAuthenticated()) {
					tronic = (OpticScript) con
							.newInstance(new Object[] { api });
					tronic.onStart();
					tronicThread = new Thread(tronic);
					tronicThread.start();
				}

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Tools.log("Script Doesnt Exist");
		}
	}

	public void setScript(OpticScript script) throws MalformedURLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		if (client.isAuthenticated()) {
			tronic = script;
			tronic.onStart();
			tronicThread = new Thread(tronic);
			tronicThread.start();
		}
	}

}
