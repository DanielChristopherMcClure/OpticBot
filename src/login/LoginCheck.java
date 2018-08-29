package login;

import java.io.IOException;

import utilities.Tools;

public class LoginCheck {

	public static boolean checkLogin(String username, String password)
			throws IOException {
		/**
		 * return Tools .getUrlSource( String.format(
		 * "http://tronicrs.com/TronicBot/bot_login.php?username=%s&password=%s"
		 * , username, password)).contains("TRUE") && (Tools .getUrlSource(
		 * String.format(
		 * "http://tronicrs.com/TronicBot/bot_login.php?username=%s&password=%s"
		 * , username, password)).contains("4") || Tools .getUrlSource(
		 * String.format(
		 * "http://tronicrs.com/TronicBot/bot_login.php?username=%s&password=%s"
		 * , username, password)).contains("8"));
		 **/
		return Tools
				.getUrlSource(
						String.format(
								"http://tronicrs.com/TronicBot/bot_login.php?username=%s&password=%s",
								username, password)).contains("TRUE");
	}

}