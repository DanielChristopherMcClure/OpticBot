package core;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.objectweb.asm.tree.FieldNode;

import accessors.IClient;
import canvas.Debug;
import canvas.RSCanvas;
import data.Hooks;
import data.Mults;
import injection.Injector;
import input.KeyBoard;
import input.Mouse;
import methods.Methods;
import reflection.ReflectedObject;
import script.ScriptHandler;
import script.TronicJarLoader;
import sounds.SoundPlayer;
import ui.IbotAccountUI;
import ui.ImageViewer;
import ui.ReflectionUI;
import ui.TronicScriptSelector;
import ui.TronicSplash;
import ui.TronicUI;
import updater.Updater;
import utilities.ClassNode;
import utilities.JARTools;
import utilities.JarUtil;
import utilities.OpcodeInfo;
import utilities.Rect;
import utilities.Tools;
import wrappers.Client;
import wrappers.UserAccount;

public class Ibot implements AppletStub, FocusListener, Runnable {

	public static double version = 1.00;

	private static final HashMap<String, String> params = new HashMap<String, String>();

	final String versionURL = "http://TronicRS.com/TronicBot/version.txt";
	final String baseLink = "http://oldschool69.runescape.com/";

	public Applet loader;
	public ClassGen appletCG = null;
	public RSCanvas canv = null;
	public Debug debug = null;
	public KeyBoard keys = new KeyBoard(this);
	public Mouse mouse = new Mouse(this);
	public TronicUI ui = new TronicUI(this);
	public JARTools jarUtil = new JARTools();
	public Methods methods = new Methods(this);
	public ScriptHandler script = new ScriptHandler(this);
	public TronicJarLoader jLoader = new TronicJarLoader(this);
	public static ArrayList<UserAccount> accounts = new ArrayList<UserAccount>();
	private static UserAccount active = null;
	public TronicScriptSelector scriptSelector = new TronicScriptSelector(this);
	public IbotAccountUI accountUI = new IbotAccountUI();
	public TronicSplash splash = new TronicSplash();
	public ImageViewer imgViewer = null;
	public URL GamePack;
	public Applet applet;
	public String MClass;
	public ReflectionUI reflectionUI = new ReflectionUI(this);
	public Updater updater = new Updater(this);

	public static ClassGen[] cgPool = null;
	public static HashMap<String, ClassNode> nodes = new HashMap<String, ClassNode>();
	public static JFrame clientFrame = null;
	public static Rectangle cont = new Rect(229, 442, 376, 456).getBounds();
	public static URLClassLoader cLoader = null;
	public static File lastScript = null;
	public static Hooks hooks = new Hooks();
	public static Mults mults = new Mults();
	public static boolean loading = false;
	public static boolean sound = false;
	public static ArrayList<Color> colorList = new ArrayList<Color>();

	public enum Game {
		OSRS, RS3, CLASSIC
	};

	public Game game;

	public static int rsRev = 170;

	private Client client = null;

	public Ibot() throws ClassNotFoundException, IOException {
		game = Game.OSRS;
		new Thread(this).start();
	}

	public boolean versionCheck() {
		return true;
	}

	public void startClient() {
		loading = true;
		try {

			parseParameters();
			GetParams(new URL(this.baseLink));

			if (!new File("RS" + rsRev + ".jar").exists()) {
				try {
					ui.setTitle("Downloading RS.....");
					Tools.log("Downloading RS.....");
					download();
					Tools.log("Done!");
				} catch (Exception e) {
					Tools.log("Unable to Download RS");
					e.printStackTrace();
				}
			}

			ui.setTitle("Starting Client.....");
			// Tools.log("Starting Client.....");

			// Canvas hack
			Tools.log("Subclassing Canvas.....");
			cgPool = jarUtil.loadClasses(new File("RS" + rsRev + ".jar"));
			for (ClassGen cg : cgPool) {
				if (cg.getSuperclassName().contains("Canvas")) {
					Tools.log("Canvas Class: " + cg.getClassName());
					ConstantPoolGen cpg = cg.getConstantPool();
					cpg.setConstant(cg.getSuperclassNameIndex(), new ConstantClass(cpg.addUtf8("canvas/RSCanvas")));
				}
			}

			this.jarUtil.dumpClasses(new File("client.jar"), cgPool);

			Ibot.nodes = JarUtil.loadClasses("client.jar");

			new Injector(this);

			Ibot.nodes = JarUtil.loadClasses("client.jar");
			// Tools.log("Loaded Nodes: " + nodes.size());

			splash.setPercent(75);

			@SuppressWarnings("deprecation")
			ClassLoader clientClassLoader = new URLClassLoader(new URL[] { new File("client.jar").toURL() });
			Ibot.cLoader = (URLClassLoader) clientClassLoader;
			Class<?> clientClass = clientClassLoader.loadClass("client");

			splash.setPercent(100);

			loader = (Applet) clientClass.newInstance();
			client = new wrappers.Client(this, (IClient) loader);

			loader.setFocusable(false);

			loader.setStub(this);
			loader.setPreferredSize(new Dimension(763, 504));
			loader.init();
			loader.start();

			loader.setBounds(0, 0, 765, 525);

			JPanel panel = new JPanel();
			panel.setLayout(new FlowLayout());

			panel.setFocusable(false);
			panel.add(loader, BorderLayout.CENTER);
			panel.setSize(765, 565);

			for (FocusListener fl : loader.getFocusListeners()) {
				loader.removeFocusListener(fl);
			}

			loader.addFocusListener(this);

			splash.dispose();
			ui.setVisible(true);

			//new Thread(new SoundPlayer(new File("Data/Sounds/intro.wav"))).start();

			// Add to tab
			TronicUI.TABS.addTab("Client", loader);
			ui.toFront();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Tools.sleep(5000);

		if (loader.getComponentCount() > 0 && loader.getComponent(0) == null
				|| !(loader.getComponent(0) instanceof RSCanvas))
			Tools.sleep(5000);
		while (loader.getComponentCount() > 0 && loader.getComponent(0) == null
				|| !(loader.getComponent(0) instanceof RSCanvas)) {
			Tools.sleep(1000, 2000);
			Tools.log("Waiting for canvas");
		}

		for (FocusListener l : loader.getFocusListeners()) {
			loader.removeFocusListener(l);
		}

		canv = (RSCanvas) loader.getComponent(0);
		debug = new Debug(canv);
		canv.debug = debug;
		canv.client = this;
		canv.addKeyListener(keys);
		loading = false;
	}

	public void resetCanvas() {
		while (loader.getComponents().length == 0)
			Tools.sleep(100);
		canv = (RSCanvas) loader.getComponent(0);
		debug = new Debug(canv);
		canv.debug = debug;
		canv.client = this;
		canv.addKeyListener(keys);
	}

	public String nameOpcode(int opcode) {
		return "    " + OpcodeInfo.OPCODES.get(opcode).toLowerCase();
	}

	public RSCanvas getCanvas() {
		if (debug != null)
			return debug.canv;
		return null;
	}

	public void download() throws Exception {
		parseParameters();
		GetParams(new URL(this.baseLink));
		downloadFile(getUrl());
	}

	public void appletResize(int width, int height) {
		Tools.log("Resizing Applet!");
	}

	public final URL getCodeBase() {
		try {
			return new URL(baseLink);
		} catch (Exception e) {
			return null;
		}
	}

	public final URL getDocumentBase() {
		try {
			return new URL(baseLink);
		} catch (Exception e) {
			return null;
		}
	}

	public final String getParameter(String name) {
		return params.get(name);
	}

	public final AppletContext getAppletContext() {
		return null;
	}

	void parseParameters() {
		try {
			URL rsserver = new URL(baseLink);
			BufferedReader in = new BufferedReader(new InputStreamReader(rsserver.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("app") && inputLine.contains("write")) {
					addParam("<app");
					addParam("let ");
				} else if (inputLine.contains("scriptsrc") || inputLine.contains("ie6")) {
				} else if (inputLine.contains("document.write")) {
					inputLine = inputLine.replaceAll("document.write", "").replaceAll("<param name=\"", "")
							.replaceAll("\">'", "\"").replaceAll("'", "").replaceAll("\\(", "").replaceAll("\\)", "")
							.replaceAll("\"", "").replaceAll(" ", "").replaceAll(";", "").replaceAll("value", "");
					String[] splitted = inputLine.split("=");
					// Tools.log(inputLine);
					if (splitted.length == 1) {
						addParam(splitted[0]);
					} else if (splitted.length == 2) {
						addParam(splitted[0], splitted[1]);
					} else if (splitted.length == 3) {
						addParam(splitted[0], splitted[1] + splitted[2]);
					} else if (inputLine.contains("archive")) {
						addParam("archive", splitted[2]);
					}
				}
			}
			in.close();
		} catch (Exception e) {
			return;
		}
	}

	public void GetParams(URL url) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		List<String> params = new ArrayList<String>();
		while ((line = reader.readLine()) != null) {
			if (line.contains("param name"))
				params.add(line);
			if (GamePack == null) // retarted block
				if (line.contains("archive"))
					if (game != Game.CLASSIC)
						GamePack = new URL(
								this.baseLink + line.substring(line.indexOf("archive=") + 8, line.indexOf(" ');")));
					else
						GamePack = new URL("http://classic2.runescape.com/"
								+ line.substring(line.indexOf("archive=") + 8, line.indexOf(" code")));
			if (MClass == null)
				if (line.contains("code="))

					MClass = line.substring(line.indexOf("code=") + 5, line.indexOf(".class"));
		}
		reader.close();

		for (String s : params) {
			addParam(GetParamName(s), GetParamValue(s));
		}
	}

	public String GetParamName(String param) {
		try {
			int StartIndex = param.indexOf("<param name=\"") + 13;
			int EndIndex = param.indexOf("\" value");
			return param.substring(StartIndex, EndIndex);
		} catch (StringIndexOutOfBoundsException e)// classic handles some
													// differently so why not
													// just catch it =P
		{
			int StartIndex = param.indexOf("<param name=") + 12;
			int EndIndex = param.indexOf(" value");
			return param.substring(StartIndex, EndIndex);
		}
	}

	public String GetParamValue(String param) {
		try {
			int StartIndex = param.indexOf("value=\"") + 7;
			int EndIndex = param.indexOf("\">');");
			return param.substring(StartIndex, EndIndex);
		} catch (StringIndexOutOfBoundsException e)// and again :D
		{
			int StartIndex = param.indexOf("value=") + 6;
			int EndIndex = param.indexOf(">');");
			return param.substring(StartIndex, EndIndex);
		}
	}

	void addParam(final String str1) {
		addParam(str1, "");
	}

	void addParam(final String str1, final String str2) {
		params.put(str1, str2);
	}

	String getUrl() throws Exception {
		Tools.log("URL: " + baseLink + params.get("archive"));
		return baseLink + params.get("archive");
	}

	void downloadFile(final String url) {
		try {
			BufferedInputStream in = new java.io.BufferedInputStream(new URL(url).openStream());
			FileOutputStream fos = new java.io.FileOutputStream("RS" + rsRev + ".jar");
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] data = new byte[1024];
			int x = 0;
			while ((x = in.read(data, 0, 1024)) >= 0) {
				bout.write(data, 0, x);
				if (Tools.random(1, 75) == 1)
					splash.setPercent(splash.percent + 1);
			}
			bout.close();
			in.close();
		} catch (Exception e) {
			return;
		}
	}

	public boolean isActive() {
		// TODO Auto-generated method stub
		return false;
	}

	public static URLClassLoader getClassLoader() {
		return cLoader;
	}

	public Class<?> getClass(String name) {
		try {
			return Ibot.getClassLoader().loadClass(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void focusGained(FocusEvent e) {
		// Tools.log("Focus Gained!");
	}

	@Override
	public void focusLost(FocusEvent e) {
		// Tools.log("Focus Lost!");
	}

	public ClassGen getGen(String name) {
		for (ClassGen cg : cgPool)
			if (cg.getClassName().equals(name))
				return cg;
		return null;
	}

	public Client getClient() {
		return this.client;
	}

	public void checkUI() {
		int w = 776, h = 605;
		if (ui.getWidth() != w) {
			ui.setSize(w, ui.getHeight());
		}
		if (ui.getHeight() != h) {
			ui.setSize(ui.getWidth(), h);
		}
	}

	public void checkLogin() throws IOException {

	}

	@Override
	public void run() {
		this.checkUI();
		if (this.versionCheck()) {
			this.startClient();
			// if (methods.edges.getEdgeId(new Rect(153, 23, 605,
			// 172).getBounds()) == EdgeIds.RSLOGO) {
			// Tools.log("RSLOGO is up");
			// }
			while (true) {
				ui.setTitle("OpticBot V" + Ibot.version);
				this.checkUI();
				Tools.sleep(10);
				if (this.getCanvas() == null)
					Tools.log("Canvas null");
				if (methods.getLoginState() == 0 && Ibot.getActive() != null) {
					script.pauseScript();
					methods.login();
					script.resumeScript();
				}
			}
		} else {
			System.exit(0);
		}
	}

	public boolean isAuthenticated() {
		return true;
	}

	public static void main(String[] arg0)
			throws InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		/**
		 * System.setSecurityManager(null);
		 * 
		 * JFrame.setDefaultLookAndFeelDecorated(true);
		 * JDialog.setDefaultLookAndFeelDecorated(true);
		 * 
		 * SwingUtilities.invokeLater(new Runnable() { public void run() { try {
		 * JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		 * ToolTipManager.sharedInstance().setLightWeightPopupEnabled( false);
		 * // Tools.log("Operating System: " // +
		 * System.getProperty("os.name")); if
		 * (System.getProperty("os.name").contains("Windows")) { for
		 * (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
		 * .getInstalledLookAndFeels()) { if ("Nimbus".equals(info.getName())) {
		 * javax.swing.UIManager.setLookAndFeel(info .getClassName()); break; }
		 * } UIManager
		 * .setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		 * 
		 * } else { for (javax.swing.UIManager.LookAndFeelInfo info :
		 * javax.swing.UIManager .getInstalledLookAndFeels()) { if
		 * ("Nimbus".equals(info.getName())) {
		 * javax.swing.UIManager.setLookAndFeel(info .getClassName()); break; }
		 * } Tools.log("Using Default L&F"); }
		 * 
		 * new Ibot();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); } } });
		 **/
		try {
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);
			ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
			new Ibot();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (UnsupportedLookAndFeelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public boolean containsField(Class<?> c, String f) {
		for (Field field : c.getDeclaredFields()) {
			if (field.getName().equals(f)) {
				// Tools.log(field.getName());
				return true;
			}
		}
		return false;
	}

	public ArrayList<ReflectedObject> getReflectedObjects()
			throws ClassNotFoundException, NoSuchFieldException, SecurityException {
		ArrayList<ReflectedObject> objs = new ArrayList<ReflectedObject>();
		for (ClassNode cn : nodes.values()) {
			// if (cn.name.equals("client")) {
			Class<?> c = Ibot.getClassLoader().loadClass(cn.name);
			for (FieldNode fn : Tools.getFieldList(cn)) {
				objs.add(new ReflectedObject(c.getDeclaredField(fn.name), fn, c, cn));
			}
			// }
		}
		return objs;
	}

	public static UserAccount getActive() {
		return active;
	}

	public static void setActive(UserAccount active) {
		Tools.log("Setting Active Account: " + active.getUsername());
		Ibot.active = active;
	}

}
