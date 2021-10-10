package caroclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ClientSession {
	public ClientSession() {
		
	}
	public void login() {
		LoginFrm loginFrm = new LoginFrm();
		JTextField usernameTxt = loginFrm.getUsernameText();
		JPasswordField passwordTxt = loginFrm.getPasswordText();
		JButton login = loginFrm.getLoginButton();
		JButton signup = loginFrm.getSignupButton();
		login.addActionListener(e -> {
			String username = usernameTxt.getText();
			String password = new String(passwordTxt.getPassword());
			System.out.println(username + password);
		});
	}
}
