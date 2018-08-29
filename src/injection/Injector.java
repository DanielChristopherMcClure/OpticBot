package injection;

import java.io.File;
import java.util.ArrayList;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.generic.ALOAD;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;

import utilities.ASMUtil;
import utilities.ClassNode;
import utilities.Tools;
import core.Ibot;
import data.Hook;

public class Injector {

	Ibot bot = null;

	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> classes = new ArrayList<String>();
	ArrayList<String> hooks = new ArrayList<String>();

	public Injector(Ibot bot) {
		this.bot = bot;
		this.modifyInterfaces();
		this.modifyClasses();
	}

	public void modifyClasses() {
		Ibot.cgPool = bot.jarUtil.loadClasses(new File("client.jar"));
		for (Hook h : Ibot.hooks.getHooks()) {
			for (ClassGen cg : Ibot.cgPool) {
				if (cg.getClassName().equals(h.getClazz())
						&& !h.getName().equals("getItemDef")
						&& this.getField(h.getField(), cg) != null) {
					Field f = this.getField(h.getField(), cg);
					String returnType = this.getReturnType(f.getSignature());
					// Tools.log(h.getName() + "   ReturnType: " + returnType);
					if (!f.isStatic()) {
						this.putGetter(h.getClazz(), h.getName(), returnType,
								h.getClazz(), h.getField(), f.getSignature());
					} else {
						this.putGetter("client", h.getName(), returnType,
								h.getClazz(), h.getField(), f.getSignature(),
								true);
					}
				}
			}
		}
		bot.jarUtil.dumpClasses(new File("client.jar"), Ibot.cgPool);
	}

	public String getReturnType(String sig) {
		for (int i = 0; i < classes.size(); i++) {
			String c = classes.get(i);
			String current = "L" + c + ";";
			if (current.equals(sig)) {
				return "Laccessors/" + names.get(i) + ";";
			}
			current = "[L" + c + ";";
			if (current.equals(sig)) {
				return "[Laccessors/" + names.get(i) + ";";
			}
			current = "[[L" + c + ";";
			if (current.equals(sig)) {
				return "[[Laccessors/" + names.get(i) + ";";
			}
			current = "[[[L" + c + ";";
			if (current.equals(sig)) {
				return "[[[Laccessors/" + names.get(i) + ";";
			}
		}
		return sig;
	}

	public Field getField(String name, ClassGen cg) {
		for (Field f : cg.getFields())
			if (f.getName().equals(name))
				return f;
		Tools.log("Field null: " + cg.getClassName() + "." + name);
		return null;
	}

	@SuppressWarnings("unchecked")
	public void modifyInterfaces() {
		this.loadOwnersFromHooks();
		for (ClassNode cn : Ibot.nodes.values()) {
			for (int i = 0; i < classes.size(); i++) {
				String it = classes.get(i);
				if (cn.name.equals(it)) {
					cn.interfaces.add("accessors/" + names.get(i));
				}
			}
		}
		ASMUtil.dump("client", Ibot.nodes);
	}

	public void loadOwnersFromHooks() {
		names.add("IClient");
		classes.add("client");
	}

	public void loadOwners() {
		String[] lines = Tools.getTextFrom(new File("Data/inserts.txt"));
		for (String s : lines) {
			if (s != null && s.contains("~")) {
				String[] split = s.split("~");
				names.add(split[0]);
				classes.add(split[1]);
			}
		}
	}

	public void putGetter(String c, String mName, String returnType,
			String clazz, String field, String desc) {
		for (ClassGen cg : Ibot.cgPool) {
			if (cg.getClassName().equals(c)) {
				this.injectGetter(cg, mName, returnType, clazz, field, desc,
						false);
				break;
			}
		}
	}

	public void putGetter(String c, String mName, String returnType,
			String clazz, String field, String desc, boolean isStatic) {
		for (ClassGen cg : Ibot.cgPool) {
			if (cg.getClassName().equals(c)) {
				this.injectGetter(cg, mName, returnType, clazz, field, desc,
						isStatic);
				break;
			}
		}
	}

	public void injectGetter(ClassGen cG, String methodName,
			String methodReturnType, String fieldClass, String fieldName,
			String fieldType, boolean fieldIsStatic) {
		ConstantPoolGen cp = cG.getConstantPool();
		InstructionList iList = new InstructionList();
		MethodGen method = new MethodGen(Constants.ACC_PUBLIC,
				Type.getType(methodReturnType), Type.NO_ARGS, new String[] {},
				methodName, cG.getClassName(), iList, cp);
		InstructionFactory iFact = new InstructionFactory(cG, cp);
		Instruction pushThis = new ALOAD(0);
		Instruction get;
		if (fieldIsStatic)
			get = iFact.createFieldAccess(fieldClass, fieldName,
					Type.getType(fieldType), Constants.GETSTATIC);
		else
			get = iFact.createFieldAccess(fieldClass, fieldName,
					Type.getType(fieldType), Constants.GETFIELD);
		Instruction returner = InstructionFactory.createReturn(Type
				.getType(methodReturnType));
		iList.append(pushThis);
		iList.append(get);
		iList.append(returner);
		method.setMaxStack();
		method.setMaxLocals();
		cG.addMethod(method.getMethod());
	}

}
