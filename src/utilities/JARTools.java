package utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.generic.ClassGen;


public class JARTools {


	public ClassGen[] loadClasses(File fileJar) {
		try {
			LinkedList<ClassGen> classes = new LinkedList<ClassGen>();
			JarFile file = new JarFile(fileJar);
			Enumeration<JarEntry> entries = file.entries();
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				if (entry.getName().endsWith(".class")) {
					ClassGen cG = new ClassGen(new ClassParser(
							file.getInputStream(entry), entry.getName()
									.replaceAll(".class", "")).parse());
					classes.add(cG);
				}
			}
			return classes.toArray(new ClassGen[classes.size()]);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ClassGen getClass(String name, ClassGen[] pool) {
		for (ClassGen cg : pool) {
			if (cg.getClassName().equals(name)) {
				return cg;
			}
		}
		return null;
	}

	public void dumpClasses(File file, ClassGen[] classes) {
		try {
			JarOutputStream out = new JarOutputStream(
					new FileOutputStream(file));
			for (ClassGen cG : classes) {
				JarEntry entry = new JarEntry(cG.getClassName() + ".class");
				out.putNextEntry(entry);
				byte[] b = cG.getJavaClass().getBytes();
				out.write(b);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ClassGen[] replaceClasses(ClassGen[] clientClasses,
			ClassGen[] hackedClasses) {
		List<String> names = new ArrayList<String>();
		for (ClassGen cg : hackedClasses) {
			names.add(cg.getClassName());
		}
		Map<String, ClassGen> loaderClassMap = new HashMap<String, ClassGen>();
		Map<String, ClassGen> out = new HashMap<String, ClassGen>();
		for (ClassGen cG : hackedClasses) {
			loaderClassMap.put(cG.getClassName(), cG);
		}
		int count = 0;
		for (ClassGen cG : clientClasses) {
			for (String name : names) {
				if (cG.getClassName().equals(name)) {
					ClassGen loaderCG = loaderClassMap.get(name);
					cG = loaderCG;
					count++;
					break;
				}
			}
			out.put(cG.getClassName(), cG);
		}
		clientClasses = out.values().toArray(new ClassGen[0]);
		return clientClasses;
	}

}