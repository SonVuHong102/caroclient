/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

import utils.Value;

/**
 *
 * @author Son Vu
 */
public class SessionClient {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	
	private ServerListener listener = null;
	
	private LoginFrm loginFrm;
	private SignupFrm signupFrm;

	public SessionClient(Socket server) {
		this.server = server;
		// Create Data Stream
		try {
			fromServer = new DataInputStream(server.getInputStream());
			toServer = new DataOutputStream(server.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
			// Create listener
			listener = new ServerListener();
			listener.start();

			// Initiate UI
			createLoginFrm();

	}

	private void createLoginFrm() {
		loginFrm = new LoginFrm();
		setClosingAction(loginFrm);
		loginFrm.getLoginButton().addActionListener(e -> {
			String username = loginFrm.getUsernameText().getText().trim();
			String password = new String(loginFrm.getPasswordText().getPassword()).trim();
			sendToServer("Login " + username + " " + password);
		});
		loginFrm.getSignupButton().addActionListener(e -> {
			createSignupFrm();
		});
	}

	private void createSignupFrm() {
		signupFrm = new SignupFrm();
		loginFrm.setVisible(false);
		signupFrm.getSignupButton().addActionListener(e -> {
			String username = signupFrm.getUsernameText().getText().trim();
			String password = new String(signupFrm.getPasswordText().getPassword());
			String repassword = new String(signupFrm.getRepasswordText().getPassword());
			String name = signupFrm.getNameText().getText().trim();
			sendToServer("Signup " + username + " " + password + " " + repassword + " " + name);
		});
	}
	
	private void loginAccepted() {
		loginFrm.dispose();
		// TODO
	}
	
	private void loginRejected() {
		MessageBox.showAlert(loginFrm, "Login Rejected. Check your username or password", "Alert");
	}
	
	private void signupSuccessed() {
		signupFrm.dispose();
		loginFrm.setVisible(true);
	}
	
	private void setClosingAction(JFrame frame) {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					if (server != null) {
						toServer.writeUTF("ClosingSocket");
						listener.stop();
						fromServer.close();
						toServer.close();
						server.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void sendToServer(String msg) {
		try {
			toServer.writeUTF(msg);
			System.out.println("Send to server : " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class ServerListener implements Runnable {

		private Thread worker;
		private final AtomicBoolean running = new AtomicBoolean(false);

		public void start() {
			worker = new Thread(this);
			worker.start();
		}

		public void stop() {
			running.set(false);
		}

		public void run() {
			running.set(true);
			while (running.get()) {
				try {
					String msg = fromServer.readUTF();
					String[] t = msg.split(" ");
					//Login
					if(t[0].equals("Login")) {
						if(t[1].equals("Accepted"))
							loginAccepted();
						else
							loginRejected();
						
					} 
					//Signup
					else if(t[0].equals("Signup")) {
						if(t[1].equals("PasswordNotMatch")) {
							MessageBox.showAlert(signupFrm, "Password NOT match","Alert");
						} else if(t[1].equals("UsernameIsExisted")) {
							MessageBox.showAlert(signupFrm, "Username is existed","Alert");
						} else if(t[1].equals("Failed")) {
							MessageBox.showAlert(signupFrm, "Can't Signup. Try again later","Alert");
						} else {
							MessageBox.showMessage(signupFrm, "Signup successed. Please login !");
							signupSuccessed();
						}
						
					}
				} catch (IOException e) {
					e.printStackTrace();
					if(e.getMessage().equals("Connection reset")) {
						stop();
					}
				} 
			}
		}
	}
}
