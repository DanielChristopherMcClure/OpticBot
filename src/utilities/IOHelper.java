package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ByteArrayOutputStream;

import java.net.URL;

public class IOHelper {

	public static String read(final URL url) throws IOException {
		final StringBuilder builder = new StringBuilder();
		try (final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))) {
			String line;
			while ((line = in.readLine()) != null) {
				builder.append(line);
			}
		}
		return builder.toString();
	}

	public static byte[] read(final InputStream input) throws IOException {
		final ByteArrayOutputStream output = new ByteArrayOutputStream();
		final byte[] buffer = new byte[2048];
		int read;
		while (input.available() > 0 && (read = input.read(buffer, 0, buffer.length)) >= 0) {
			output.write(buffer, 0, read);
		}
		return output.toByteArray();
	}
}
