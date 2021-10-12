package caroclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import utils.MessageBox;
import utils.Value;

public class ClientSession {
	private MulticastSocket client;
	private OppListener listener;
	private String clientUsername = "";
	
	private LoginFrm loginFrm;
	
	private LoginSuccessedFrm loginSuccessedFrm;
	private JComboBox box;
	private JButton btnRefresh;
	private JButton btnCreate;
	private JButton btnInvite;
	private JLabel status;
	private ArrayList<Player>  playerList; 
	private String opponentUsername = "";

	private int side = 1;
	
	public ClientSession(MulticastSocket client) {
		this.client = client;
	}
	
	public void start() {
		listener = new OppListener();
		listener.start();
		login();
	}
	
	private void login() {
		loginFrm = new LoginFrm();
		JTextField usernameText = loginFrm.getUsernameText();
		JPasswordField passwordText = loginFrm.getPasswordText();
		JButton login = loginFrm.getLoginButton();
		// TODO add sign up function
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
		btnInvite = loginSuccessedFrm.getBtnInvite();
		status = loginSuccessedFrm.getStatus();
		
		status.setText("Player " + clientUsername + " : Choice a mode");
		
		playerList = new ArrayList<Player>();
		sendRefresh();
		
		btnRefresh.addActionListener( e-> {
			sendRefresh();
		});
		btnCreate.addActionListener(e -> {
			sendCreate();
		});
		
		btnInvite.addActionListener(e -> {
			opponentUsername = (String) box.getSelectedItem();
			sendInvite();
		});
		
	}
	
	private void createdRoom() {
		btnInvite.setEnabled(false);
		btnCreate.setEnabled(false);
		status.setText("Player " + clientUsername + " : Room created. Waiting for invitaion...");
	}
	
	private void refreshPlayerList() {
		box.removeAllItems();
		for(Player i : playerList) {
			box.addItem(i.getUsername());
		}
		box.repaint();
		System.out.println("Player number: " + playerList.size());
	}
	
	private void invitedRoom() {
		btnInvite.setEnabled(false);
		btnCreate.setEnabled(false);
		status.setText("Invited " + opponentUsername + ". Waiting for acception...");
	}
	
	private void showInvitation() {
		boolean isAccepted = MessageBox.showYesNo(loginSuccessedFrm, "Invitaion from " + opponentUsername + ". Accept ?" , "Invitaion");
		if(isAccepted) {
			sendInvitationAccepted();
			playRoom();
		} else {
			sendInvitationRejected();
		}
	}
	
	private void invitationRejected() {
		btnInvite.setEnabled(true);
		btnCreate.setEnabled(true);
		status.setText("Player " + clientUsername + " : Choice a mode");
	}
	
	private void playRoom() {
		loginSuccessedFrm.dispose();
		
		// End this ClientSession
		listener.stop();
		PlayingSession playingSession = new PlayingSession(client,clientUsername,opponentUsername,side);
		playingSession.start();
		
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
	
	private void sendInvite() {
		sendToServer(clientUsername + " invite " + opponentUsername);
	}
	
	private void sendInvitationAccepted() {
		side = -1;
		sendToServer(opponentUsername + " invite " + clientUsername + " accepted");
	}
	
	private void sendInvitationRejected() {
		sendToServer(opponentUsername + " invite " + clientUsername + " rejected");
	}
	
	private class OppListener implements Runnable {
		
		private Thread worker;
	    private final AtomicBoolean running = new AtomicBoolean(false);
		
	    public void start() {
	        worker = new Thread(this);
	        worker.start();
	    }
	    
		public void stop() {
	        running.set(false);
	    }
		
		@Override
		public void run() {
			running.set(true);
			while(running.get()) {
				try {
					byte[] buf = new byte[1024];
					DatagramPacket p = new DatagramPacket(buf, buf.length);
					client.receive(p);
					String msg = new String(buf).trim();
					// Avoid initiate connection creating from others
					if(msg.equalsIgnoreCase("accept connection"))
						continue;
					System.out.println("Msg received : [" + msg + "]");
					String[] t = msg.split(" ");
					// Request : [<username> ??? ] -> Response : [<username> ???] (Authorized responsions)
					if(t[0].equalsIgnoreCase(clientUsername)) {
						if(t[1].equalsIgnoreCase("login")) {
							if(t[2].equalsIgnoreCase("accept")) {
								loginSuccessed();
								System.out.println("Login Success");
							} else {
								MessageBox.showAlert(loginFrm, "Login Failed", "NOTIFICATION");
							}
						} 
						// Request : [<username> refresh] -> Response : [<username> refresh <username1> <username2> .... ] (List of online player)
						else if(t[1].equalsIgnoreCase("refresh")) {
							playerList.clear();
							for(int i=2;i<t.length;i++)
								playerList.add(new Player(t[i]));
							refreshPlayerList();
						} 
						// Request : [<username> create] -> Response : [<username> create accept] (Accept creating request)
						else if(t[1].equalsIgnoreCase("create")) {
							if(t[2].equalsIgnoreCase("accept")) {
								createdRoom();
//								MessageBox.showMessage(loginSuccessedFrm, "Room : [" + clientUsername + "] created. Waiting for invtation...");	
							}
							else {
								MessageBox.showAlert(loginFrm, "Can't create room", "NOTIFICATION");
							}
						}
						// Request : [<username> invite <opponent>] -> Response : [<opponent> invite <username> sent] (send invite from <username> to <opponent>)
						else if(t[1].equalsIgnoreCase("invite")) {
							if(t[3].equalsIgnoreCase("sent")) {
								invitedRoom();
//								MessageBox.showMessage(loginSuccessedFrm, "Invited " + opponentUsername + ". Waiting for acception...");
							}
							// Response : [<username> invite <opponent> accepted] (<opponent> accepted invitation)
							else if(t[3].equalsIgnoreCase("accepted")){
//								MessageBox.showMessage(loginSuccessedFrm, opponentUsername + " accepted invitation !");
								playRoom();
							}
							// Response : [<username> invite <opponent> rejected] (<opponent> rejected invitation)
							else if(t[3].equalsIgnoreCase("rejected")){
								invitationRejected();
								MessageBox.showMessage(loginSuccessedFrm, opponentUsername + " rejected invitation !");
							} else {
								MessageBox.showAlert(loginSuccessedFrm, "Can't invite " + opponentUsername, "NOTIFICATION");
							}
						}
						
						// Response : [ ??? ] (Received server commands)
					} else if(t.length > 2) {
						// Response : [? ? <username> ??? ] (received direct commands)
						if(t[2].equalsIgnoreCase(clientUsername)) {
							// Response : [<opponent> invite <username> sent] (received invite from <opponent>)
							if(t[1].equalsIgnoreCase("invite") && t[3].equalsIgnoreCase("sent")) {
								opponentUsername = t[0];
								showInvitation();
							}
						}
					} else {
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
}
