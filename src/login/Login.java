/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package login;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import ui.TronicUI;
import utilities.Tools;

/**
 * 
 * @author Dan
 */
public class Login extends javax.swing.JFrame implements ActionListener,
		KeyListener {

	public ImageIcon key = new ImageIcon("Data/Images/account.png");
	public ImageIcon upgrade = new ImageIcon("Data/Images/upgrade.png");

	private String user = null;
	private String pass = null;

	/**
	 * Creates new form LoginUI
	 */
	public Login() {
		this.setTitle("Login to TronicBot");
		initComponents();
		this.setAlwaysOnTop(true);
		this.setIconImage(TronicUI.T);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		USERNAME = new javax.swing.JTextField();
		PASSWORD = new javax.swing.JPasswordField();
		LOGINBUTTON = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

		USERNAME.setText("Username");

		PASSWORD.setText("Password");
		PASSWORD.addKeyListener(this);

		LOGINBUTTON.setText("Login");
		LOGINBUTTON.setIcon(key);
		LOGINBUTTON.addActionListener(this);
		PREMIUM.addActionListener(this);
		PREMIUM.setIcon(upgrade);

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
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(USERNAME)
												.addComponent(
														PASSWORD,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														191, Short.MAX_VALUE)
												.addComponent(
														LOGINBUTTON,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(
														PREMIUM,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(USERNAME,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(PASSWORD,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(LOGINBUTTON)
								.addComponent(PREMIUM)
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		pack();
	}// </editor-fold>

	// Variables declaration - do not modify
	public static javax.swing.JButton LOGINBUTTON;
	public static javax.swing.JPasswordField PASSWORD;
	public static javax.swing.JTextField USERNAME;
	public static JButton PREMIUM = new JButton("Get Premium Membership");

	// End of variables declaration

	public String getTronicBotPassword() {
		return pass;
	}

	public String getTronicBotUsername() {
		return user;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Login":
			login();
			break;
		case "Get Premium Membership":
			try {
				Tools.openWebpage(new URL(
						"http://tronicrs.com/forum/misc.php?action=payments")
						.toURI());
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			break;
		}
	}

	private void login() {
		String p = "";
		for (char c : PASSWORD.getPassword()) {
			p += c;
		}
		p.replace(" ", "%20");
		String n = USERNAME.getText().replace(" ", "%20");
		// Tools.log(n);
		// Tools.log(p);
		try {
			if (LoginCheck.checkLogin(n, p)) {
				Tools.log("Login Successful!");
				this.pass = p;
				this.user = n;
				this.setVisible(false);
			} else {
				JOptionPane
						.showMessageDialog(this,
								"LOGIN FAILED - Incorrect Login or Non-Premium Member!");
			}
		} catch (HeadlessException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case 10:
			login();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
