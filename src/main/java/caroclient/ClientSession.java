package caroclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.Value;

public class ClientSession {
	private MulticastSocket client;
	
	private LoginFrm loginFrm;
	
	private String clientName = "";
	
	public ClientSession(MulticastSocket client) {
		this.client = client;
	}
	public void start() {
		Thread listener = new Thread(new OppListener());
		listener.start();
		login();
	}
	private void login() {
		loginFrm = new LoginFrm();
		JTextField usernameText = loginFrm.getUsernameText();
		JPasswordField passwordText = loginFrm.getPasswordText();
		JButton login = loginFrm.getLoginButton();
		JButton signup = loginFrm.getSignupButton();
		login.addActionListener(e -> {
			String usernameTxt = usernameText.getText();
			String passwordTxt = new String(passwordText.getPassword());
			System.out.println(usernameTxt + "/" + passwordTxt);
			// Request : [login <username> <password>] - Response : [accept <username>] (Login accepted, initiate client information)
			sendToServer("login " + usernameTxt + " " + passwordTxt);
		});
	}
	
	private void loginSuccessed() {
		loginFrm.dispose();
		// TODO
		
	}
	
	private void sendToServer(String msg) {
		try {
			DatagramPacket p = new DatagramPacket(msg.getBytes(),msg.length(),InetAddress.getByName(Value.serverAddress),Value.serverPort);
			client.send(p);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
	
	private class OppListener implements Runnable {
		@Override
		public void run() {
			byte[] buf = new byte[1024];
			DatagramPacket p = new DatagramPacket(buf, buf.length);
			while(true) {
				try {
					client.receive(p);
					String msg = new String(buf).trim();
					String[] t = msg.split(" ");
					// Request : [login <username> <password>] - Response : [accept <username>] (Login accepted, initiate client information)
					if(t[0].equalsIgnoreCase("accept")) {
						clientName = t[1];
						loginSuccessed();
						System.out.println("Login Success");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
