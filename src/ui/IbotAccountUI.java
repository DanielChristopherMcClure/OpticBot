/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import sounds.SoundPlayer;
import utilities.Tools;
import wrappers.UserAccount;
import core.Ibot;

/**
 * 
 * @author Dan
 */
public class IbotAccountUI extends javax.swing.JFrame implements ActionListener {

	public static DefaultListModel accounts = new DefaultListModel();

	/**
	 * Creates new form IbotAccountUI
	 */
	public IbotAccountUI() {
		this.setTitle("Account Manager");
		initComponents();
		this.setResizable(false);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		ACCOUNTLIST = new javax.swing.JList();
		USERNAME = new javax.swing.JTextField();
		PASSWORD = new javax.swing.JPasswordField();
		ADDACCOUNT = new javax.swing.JButton();
		REMOVEACCOUNT = new javax.swing.JButton();

		ADDACCOUNT.addActionListener(this);
		REMOVEACCOUNT.addActionListener(this);

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		ACCOUNTLIST.setModel(accounts);

		jScrollPane1.setViewportView(ACCOUNTLIST);

		USERNAME.setText("Username");

		PASSWORD.setText("jPasswordField1");

		ADDACCOUNT.setText("Add Account");

		REMOVEACCOUNT.setText("Remove Account");

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jScrollPane1)
												.addGroup(
														layout.createSequentialGroup()
																.addGroup(
																		layout.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING,
																				false)
																				.addComponent(
																						PASSWORD)
																				.addComponent(
																						USERNAME))
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		ADDACCOUNT)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		REMOVEACCOUNT)
																.addGap(0,
																		113,
																		Short.MAX_VALUE)))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(USERNAME,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(
														PASSWORD,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(ADDACCOUNT)
												.addComponent(REMOVEACCOUNT))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										8, Short.MAX_VALUE)
								.addComponent(jScrollPane1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	private javax.swing.JList ACCOUNTLIST;
	private javax.swing.JButton ADDACCOUNT;
	private javax.swing.JPasswordField PASSWORD;
	private javax.swing.JButton REMOVEACCOUNT;
	private javax.swing.JTextField USERNAME;
	private javax.swing.JScrollPane jScrollPane1;

	// End of variables declaration

	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (Ibot.sound)
			new Thread(new SoundPlayer(new File("Data/Sounds/drop.wav")))
					.start();
		switch (e.getActionCommand()) {
		case "Add Account":
			if (USERNAME.getText() != null && PASSWORD.getPassword() != null) {
				Tools.log("Adding Account");
				String pass = "";
				for (char c : PASSWORD.getPassword())
					pass += c;
				Ibot.accounts.add(new UserAccount(USERNAME.getText(), pass));
				for (UserAccount u : Ibot.accounts)
					IbotAccountUI.accounts.addElement(u.getUsername());
				ACCOUNTLIST.setModel(accounts);
				saveAccounts();
			}
			break;
		case "Remove Account":
			if (ACCOUNTLIST.getSelectedValue() != null) {
				Tools.log("Removing Account");
				String name = accounts.get(ACCOUNTLIST.getSelectedIndex())
						.toString();
				UserAccount remove = null;
				for (UserAccount u : Ibot.accounts) {
					if (u.getUsername().contains(name)) {
						remove = u;
					}
				}
				if (remove != null) {
					accounts.remove(ACCOUNTLIST.getSelectedIndex());
					Ibot.accounts.remove(remove);
					saveAccounts();
				}
			}
		}
	}

	public void open() {
		this.loadFromIbot();
		this.setVisible(true);
	}

	public void loadFromIbot() {
		accounts.clear();
		for (UserAccount u : Ibot.accounts)
			IbotAccountUI.accounts.addElement(u.getUsername());
		ACCOUNTLIST.setModel(accounts);
	}

	public static void saveAccounts() {
		ArrayList<String> accountData = new ArrayList<String>();
		for (UserAccount u : Ibot.accounts) {
			accountData.add(u.getUsername() + "~" + u.getPassword());
		}
		Tools.writeFile(accountData.toArray(new String[] {}), new File(
				"Data/Settings/acc.txt"));
		Tools.log("Accounts Saved!");
	}
}
