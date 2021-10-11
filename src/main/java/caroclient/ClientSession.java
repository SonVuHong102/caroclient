package caroclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.MessageBox;
import utils.Value;

public class ClientSession {
	private MulticastSocket client;
	private String clientUsername = "";
	
	private LoginFrm loginFrm;
	
	private LoginSuccessedFrm loginSuccessedFrm;
	private JComboBox box;
	private JButton btnRefresh;
	private JButton btnCreate;
	private JButton btnJoin;
	private ArrayList<Player>  playerList; 
	
	
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
			sendLogin(usernameTxt,passwordTxt);
		});
	}
	
	private void loginSuccessed() {
		loginFrm.dispose();
		
		loginSuccessedFrm = new LoginSuccessedFrm();
		box = loginSuccessedFrm.getBox();
		btnRefresh = loginSuccessedFrm.getBtnRefresh();
		btnCreate = loginSuccessedFrm.getBtnCreate();
		btnJoin = loginSuccessedFrm.getBtnJoin();
		playerList = new ArrayList<Player>();
		sendRefresh();
		
		btnRefresh.addActionListener( e-> {
			sendRefresh();
		});
		btnCreate.addActionListener(e -> {
			sendCreate();
		});
		
	}
	
	private void createdRoom() {
		loginSuccessedFrm.dispose();
	}
	
	private void refreshPlayerList() {
		box.removeAllItems();
		for(Player i : playerList) {
			box.addItem(i.getUsername());
		}
		box.repaint();
		System.out.println("Player number: " + playerList.size());
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
	
	private void sendLogin(String usernameTxt,String passwordTxt) {
		clientUsername = usernameTxt;
		sendToServer(usernameTxt + " login " + passwordTxt);
	}
	
	private void sendRefresh() {
		sendToServer(clientUsername + " refresh");
	}
	
	private void sendCreate() {
		sendToServer(clientUsername + " create");
	}
	
	private class OppListener implements Runnable {
		@Override
		public void run() {
			while(true) {
				try {
					byte[] buf = new byte[1024];
					DatagramPacket p = new DatagramPacket(buf, buf.length);
					client.receive(p);
					String msg = new String(buf).trim();
					System.out.println("Msg received : [" + msg + "]");
					String[] t = msg.split(" ");
					// Request : [<username> <T>] - Response : [<username> <T> <...>] (Authorized responsions)
					if(t[0].equalsIgnoreCase(clientUsername)) {
						if(t[1].equalsIgnoreCase("login")) {
							if(t[2].equalsIgnoreCase("accept")) {
								loginSuccessed();
								System.out.println("Login Success");
							} else {
								MessageBox.showAlert(loginFrm, "Login Failed", "NOTIFICATION");
							}
						} 
						// Request : [<username> refresh] - Response : [<username> refresh <...>] (List of online player)
						else if(t[1].equalsIgnoreCase("refresh")) {
							playerList.clear();
							for(int i=2;i<t.length;i++)
								playerList.add(new Player(t[i]));
							refreshPlayerList();
						} 
						// Request : [<username> create] - Response : [<username> create accept] (List of online player)
						else if(t[1].equalsIgnoreCase("create")) {
							if(t[2].equalsIgnoreCase("accept")) {
								MessageBox.showMessage(loginFrm, "Created room : " + clientUsername);
								createdRoom();
							}
							else {
								MessageBox.showAlert(loginFrm, "Can't create room", "NOTIFICATION");
							}
						}
					} else  {
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
