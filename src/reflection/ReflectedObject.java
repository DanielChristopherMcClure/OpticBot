package reflection;

import java.lang.reflect.Field;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import core.Ibot;

public class ReflectedObject {

	public Field field;
	public FieldNode fn = null;
	public ClassNode cn = null;
	public Class<?> parent = null;

	public ReflectedObject(Field field, FieldNode fn) {
		this.field = field;
		this.fn = fn;
	}

	public ReflectedObject(Field field, FieldNode fn, Class<?> parent, ClassNode cn) {
		this.field = field;
		this.fn = fn;
		this.parent = parent;
		this.cn = cn;
	}

	public Object getValue() {
		Object parent = new Object();
		try {
			if (field != null) {
				field.setAccessible(true);
				return field.get(null);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new Object(); // Dont give back nulls
	}

	public Object getValue(Object parent) {
		try {
			if (field != null) {
				field.setAccessible(true);
				return field.get(parent);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return new Object();
	}

	public Object[] getArray() {
		try {
			return (Object[]) field.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return new Object[1];
	}

	public Object[] getArray(Object parent) {
		try {
			if (parent != null)
				return (Object[]) field.get(parent);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return new Object[1];
	}

	public Object[][] getArray2Dimensions() {
		try {
			return (Object[][]) field.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return new Object[1][1];
	}

	public Object[][] getArray2Dimensions(Object parent) {
		try {
			return (Object[][]) field.get(parent);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return new Object[1][1];
	}

	public Object[][][] getArray3Dimensions() {
		try {
			return (Object[][][]) field.get(null);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return new Object[1][1][1];
	}

	public Object[][][] getArray3Dimensions(Object parent) {
		try {
			return (Object[][][]) field.get(parent);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
		return new Object[1][1][1];
	}

	public void setObject(Object obj) {
		try {
			field.set(this, obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

	public int getIntValue(Object parent) {
		try {
			field.setAccessible(true);
			int it = field.getInt(parent);
			return it;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int getIntValueWithMult(Object parent) {
		return this.getIntValue(parent) * Ibot.mults.getMulti(cn.name, fn.name);
	}

	public Class getFieldClass() {
		return field.getDeclaringClass();
	}
}
