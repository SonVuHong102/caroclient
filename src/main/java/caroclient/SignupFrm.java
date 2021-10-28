package caroclient;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SignupFrm extends JFrame{
	private JTextField usernameText;
	private JPasswordField passwordText;
	private JPasswordField repasswordText;
	private JButton signupButton;
	
	public SignupFrm() {
		initFrame();
		initComponents();
		setVisible(true);
	}
	
	private void initFrame() {
		setSize(400,200);
		setTitle("Signup : Enter your information");
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	private void initComponents() {
		JPanel container = new JPanel();
	
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
		
		JPanel repasswordPanel = new JPanel();
		JLabel repasswordLabel = new JLabel("Confirm password :  ");
		repasswordText = new JPasswordField(20);
		repasswordPanel.add(repasswordLabel);
		repasswordPanel.add(repasswordText);
		container.add(repasswordPanel);
		
		JPanel button = new JPanel();
		signupButton = new JButton("Sign up");
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

	public JPasswordField getRepasswordText() {
		return repasswordText;
	}

	public JButton getSignupButton() {
		return signupButton;
	}

	
}
