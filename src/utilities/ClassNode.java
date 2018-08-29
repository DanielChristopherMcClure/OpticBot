package utilities;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ClassNode extends org.objectweb.asm.tree.ClassNode {

	public int percent = 0;
	
	public final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);

	private byte[] bytes = null;

	public static ClassNode createNode(final byte[] bytes) {
		final ClassReader reader = new ClassReader(bytes);
		final ClassNode node = new ClassNode();
		reader.accept(node, ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
		return node;
	}

	public byte[] getBytes(final boolean cached) {
		if (cached && bytes != null) {
			return bytes;
		} else {
			accept(writer);
			return (bytes = writer.toByteArray());
		}
	}

	public byte[] getBytes() {
		return getBytes(false);
	}
}
