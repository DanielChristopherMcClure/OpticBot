package script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FluxClassLoader extends ClassLoader {

	public FluxClassLoader(ClassLoader parent) {
		super(parent);
	}

	public Class loadClass(String loc, String name)
			throws ClassNotFoundException {
		try {
			String url = "file:" + loc;
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();

			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}

			input.close();

			byte[] classData = buffer.toByteArray();
			return defineClass(name, classData, 0, classData.length);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}