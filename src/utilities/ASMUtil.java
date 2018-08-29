package utilities;

import org.objectweb.asm.Opcodes;

import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.FieldInsnNode;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;

import java.lang.reflect.Field;

import java.util.Map;
import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Collections;

import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;

import java.util.regex.Pattern;


public class ASMUtil {

	private static Map<String, Integer> OPCODE_MAP = new HashMap<>();

	static {
		boolean advance = false;
		for (final Field f : Opcodes.class.getFields()) {
			if (!advance) {
				if (!f.getName().equals("NOP")) {
					continue;
				}
				advance = true;
				try {
					OPCODE_MAP.put(f.getName(), (Integer) f.get(null));
				} catch (final IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				try {
					OPCODE_MAP.put(f.getName(), (Integer) f.get(null));
				} catch (final IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean methodContainsParameter(final MethodNode mn, final String parameterDesc) {
		for (final Object parameter : mn.localVariables) {
			if (parameter.toString().equals(parameterDesc)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isDescStandard(final String desc) {
		final String[] keywords = {"I", "Z", "F", "J", "S", "B", "D"};
		return desc.contains("java") && desc.contains("/") || Arrays.binarySearch(keywords, desc) >= 0 || desc.contains("[") && Arrays.binarySearch(keywords, stripDesc(desc)) >= 0;
	}

	public static boolean isFieldStandard(final FieldNode field) {
		return isDescStandard(field.desc);
	}

	public static String stripDesc(String desc) {
		if (desc.startsWith("L")) {
			desc = desc.substring(1);
		}
		return desc.replaceAll("\\[L", "").replaceAll("\\[", "").replaceAll(";", "");
	}

	public static int getOpcode(String opstring) {
		try {
			return OPCODE_MAP.get(opstring.toUpperCase());
		} catch (final Exception e) {
			return -1;
		}
	}

	

	public static int getReturnFor(final String desc) {
		if (desc.startsWith("[")) {
			return Opcodes.ARETURN;
		} else if (desc.equals("D")) {
			return Opcodes.DRETURN;
		} else if (desc.equals("F")) {
			return Opcodes.FRETURN;
		} else if (desc.equals("J")) {
			return Opcodes.LRETURN;
		}
		final String[] i = {"I", "Z"};
		for (final String s : i) {
			if (s.equals(desc)) {
				return Opcodes.IRETURN;
			}
		}
		return Opcodes.ARETURN;
	}

	public static FieldNode getField(final ClassNode cn, final FieldInsnNode fin) {
		for (final FieldNode fn : (List<FieldNode>) cn.fields) {
			if (!fn.name.equals(fin.name)) {
				continue;
			}
			return fn;
		}
		return null;
	}

	public static FieldNode getField(final ClassNode cn, final String field) {
		for (final FieldNode fn : (List<FieldNode>) cn.fields) {
			if (!fn.name.equals(field)) {
				continue;
			}
			return fn;
		}
		return null;
	}

	public static MethodNode getMethod(final ClassNode cn, final String method) {
		for (final MethodNode mn : (List<MethodNode>) cn.methods) {
			if (!mn.name.equals(method)) {
				continue;
			}
			return mn;
		}
		return null;
	}

	public static Map<String, ClassNode> getClassNodes(final JarFile jar) {
		final Map<String, ClassNode> classes = new HashMap<>();
		final Enumeration<?> entries = jar.entries();
		while (entries.hasMoreElements()) {
			final JarEntry entry = (JarEntry) entries.nextElement();
			final String name = entry.getName();
			if (name.endsWith(".class")) {
				InputStream input;
				try {
					input = jar.getInputStream(entry);
				} catch (final IOException e) {
					continue;
				}
				if (input != null) {
					ClassNode node;
					try {
						node = ClassNode.createNode(IOHelper.read(input));
					} catch (IOException e) {
						continue;
					}
					classes.put(node.name, node);
				}
			}
		}
		return classes;
	}

	public static void dump(final String jarName, final Map<String, ClassNode> nodes) {
		try {
			final File jar = new File(jarName + ".jar");
			try (final JarOutputStream output = new JarOutputStream(new FileOutputStream(jar))) {
				for (final Map.Entry<String, ClassNode> entry : nodes.entrySet()) {
					output.putNextEntry(new JarEntry(entry.getKey().replaceAll("\\.", "/") + ".class"));
					output.write(entry.getValue().getBytes());
					output.closeEntry();
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}

