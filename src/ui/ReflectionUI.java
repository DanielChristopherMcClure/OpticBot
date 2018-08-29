package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;

import core.Ibot;
import reflection.ReflectedObject;
import utilities.Tools;

public class ReflectionUI extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Ibot bot = null;

	DefaultListModel<String> fields = new DefaultListModel<String>();
	JButton refresh = new JButton("Refresh");
	JPanel back = new JPanel();
	JList FIELDLIST = new JList();

	public ReflectionUI(Ibot bot) {
		this.bot = bot;
		back.setBackground(Color.DARK_GRAY);
		this.add(back, BorderLayout.CENTER);
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		refresh.addActionListener(this);
		back.setLayout(new BorderLayout());
		back.add(refresh);
		refresh.setSize(100, 45);
		FIELDLIST.setSize(300, 600);
		back.add(new JScrollPane(FIELDLIST));
		FIELDLIST.addMouseListener(new PopClickListener());

		this.setVisible(true);
		this.setAlwaysOnTop(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fields.clear();
		try {
			ArrayList<ReflectedObject> ros = bot.getReflectedObjects();
			for (ReflectedObject ro : ros) {
				if (ro.fn.access == 8) {
					if (ro.fn.desc.equals("I")) {
						int value = ro.getIntValue(null) * Ibot.mults.getMulti(ro.parent.getName(), ro.fn.name);
						fields.addElement(ro.parent.getName() + "." + ro.fn.name + ": " + value);
					} else if (ro.fn.desc.equals("Ljava/lang/String;")) {
						if (ro.getValue(null) != null) {
							String value = ro.getValue().toString();
							fields.addElement(ro.parent.getName() + "." + ro.fn.name + ": " + value);
						}
					} else if (ro.fn.desc.equals("[Ljava/lang/String;") && PopUpDemo.sa.isSelected()) {
						if (ro.getValue(null) != null) {
							String[] value = (String[]) ro.getArray();
							for (int i = 0; i < value.length; i++)
								fields.addElement(
										ro.parent.getName() + "." + ro.fn.name + " string[" + i + "]: " + value[i]);
						}
					} else if (ro.fn.desc.equals("[I") && PopUpDemo.ia.isSelected()) {
						if (ro.getValue(null) != null) {
							int[] value = (int[]) ro.getValue(null);
							for (int i = 0; i < value.length; i++)
								fields.addElement(
										ro.parent.getName() + "." + ro.fn.name + " int[" + i + "]: " + value[i]);
						}
					}
				}
			}
		} catch (ClassNotFoundException | NoSuchFieldException | SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		FIELDLIST.setModel(fields);
	}

}

class PopUpDemo extends JPopupMenu {
	public static JRadioButtonMenuItem ia = new JRadioButtonMenuItem("int[]");
	public static JRadioButtonMenuItem sa = new JRadioButtonMenuItem("string[]");

	public PopUpDemo() {
		add(ia);
		add(sa);
	}
}

class PopClickListener extends MouseAdapter {
	public void mousePressed(MouseEvent e) {
		if (e.isPopupTrigger())
			doPop(e);
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger())
			doPop(e);
	}

	private void doPop(MouseEvent e) {
		PopUpDemo menu = new PopUpDemo();
		menu.show(e.getComponent(), e.getX(), e.getY());
	}
}
