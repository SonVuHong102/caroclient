package caroclient;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.Value;

public class LoginFrm extends JFrame {
	private JTextField usernameText;
	private JPasswordField passwordText;
	private JButton loginButton;
	private JButton signupButton;
	
	public LoginFrm() {
		initFrame();
		initComponents();
		setVisible(true);
	}
	
	private void initFrame() {
		setSize(300,200);
		setTitle("Login");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	private void initComponents() {
		JPanel container = new JPanel();
		
		JLabel connectedMsg = new JLabel("Connected to " + Value.groupAddress + ":" + Value.serverPort);
//		connectedMsg.setForeground(Color.GREEN);
		container.add(connectedMsg);
	
		JPanel usernamePanel = new JPanel();
		JLabel usernameLabel = new JLabel("Username : ");
		usernameText = new JTextField(20);
		usernamePanel.add(usernameLabel);
		usernamePanel.add(usernameText);
		container.add(usernamePanel);
		
		JPanel passwordPanel = new JPanel();
		JLabel passwordLabel = new JLabel("Password :  ");
		passwordText = new JPasswordField(20);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordText);
		container.add(passwordPanel);
		
		JPanel button = new JPanel();
		loginButton = new JButton("Login");
		signupButton = new JButton("Sign up");
		button.add(loginButton);
		button.add(signupButton);
		container.add(button);
		
		this.add(container);
		
	}
	
	public JTextField getUsernameText() {
		return usernameText;
	}

	public JPasswordField getPasswordText() {
		return passwordText;
	}

	public JButton getLoginButton() {
		return loginButton;
	}

	public JButton getSignupButton() {
		return signupButton;
	}

}
