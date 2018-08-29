package utilities;

import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil {
	// Store our classes here
	public static HashMap<String, ClassNode> parsedClasses = new HashMap<String, ClassNode>();

	public static HashMap<String, ClassNode> loadClasses(String file)
			throws IOException {
		// Make sure you decrypt the JAR with the class I [Swipe] uploaded
		File theFile = new File(file);
		try {
			// If we are using a folder like a noob
			if (theFile.isDirectory()) {
				for (File f : theFile.listFiles()) { // May cause a
														// NullPointerException
					// We're only concerned with class files!
					if (f.getName().contains(".class")
							&& !f.getName().contains("META")) {
						// Create a ClassReader object
						ClassReader cr = new ClassReader(f.getCanonicalPath());
						// Put the ClassReader's bytes that it gained into a
						// ClassNode
						ClassNode cn = new ClassNode();
						cr.accept(cn, ClassReader.SKIP_DEBUG
								| ClassReader.SKIP_FRAMES);
						// Place the ClassNode with its name to the HashMap for
						// future use.
						parsedClasses.put(cn.name, cn);
					}
				}
			} else {
				// If we are using a JAR like a pro
				JarFile theJar = new JarFile(theFile);
				// Self-explanatory
				Enumeration<JarEntry> en = theJar.entries();
				// Loop through the elements of the JarFile
				while (en.hasMoreElements()) {
					// Reference the entry to work with
					JarEntry entry = en.nextElement();
					// We're only concerned with class files!
					if (entry.getName().contains(".class")
							&& !entry.getName().contains("META")) {
						// Create a ClassReader object
						ClassReader cr = new ClassReader(
								theJar.getInputStream(entry));
						// Put the ClassReader's bytes that it gained into a
						// ClassNode
						ClassNode cn = new ClassNode();
						cr.accept(cn, ClassReader.SKIP_DEBUG
								| ClassReader.SKIP_FRAMES);
						// Place the ClassNode with its name to the HashMap for
						// future use.
						parsedClasses.put(cn.name, cn);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return parsedClasses;
	}

	public static ClassNode getNode(String name) {
		for (ClassNode cn : parsedClasses.values()) {
			if (cn.name.equals(name))
				return cn;
		}
		return null;
	}

}
