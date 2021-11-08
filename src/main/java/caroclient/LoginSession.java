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
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

/**
 *
 * @author Son Vu
 */
public class LoginSession {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;

	private ServerListener listener = null;
	private String name;

	private LoginFrm loginFrm;
	private SignupFrm signupFrm;

	public LoginSession(Socket server) {
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

//UI Action
	private void createLoginFrm() {
		loginFrm = new LoginFrm();
		setMainClosingAction(loginFrm);
		loginFrm.getLoginButton().addActionListener(e -> {
			String username = loginFrm.getUsernameText().getText().trim();
			name = username;
			String password = new String(loginFrm.getPasswordText().getPassword()).trim();
			if(username.isEmpty() || password.isEmpty()) {
				MessageBox.showAlert(loginFrm, "Please enter all fields", "Empty field");
				return;
			}
			sendToServer("Login " + username + " " + password);
		});
		loginFrm.getSignupButton().addActionListener(e -> {
			createSignupFrm();
		});
	}

	private void createSignupFrm() {
		loginFrm.setVisible(false);
		signupFrm = new SignupFrm();
		setSubClosingAction(signupFrm, loginFrm);
		signupFrm.getSignupButton().addActionListener(e -> {
			String username = signupFrm.getUsernameText().getText().trim();
			String password = new String(signupFrm.getPasswordText().getPassword()).trim();
			String repassword = new String(signupFrm.getRepasswordText().getPassword()).trim();
			if(username.isEmpty() || password.isEmpty() || repassword.isEmpty()) {
				MessageBox.showAlert(loginFrm, "Please enter all fields", "Empty field");
				return;
			}
			sendToServer("Signup " + username + " " + password + " " + repassword);
		});

	}

	// Close Main Frame -> Close Socket
	private void setMainClosingAction(JFrame frame) {
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (server != null) {
					frame.dispose();
					socketStop();
				}
			}
		});
	}

	// Close Sub Frame -> Open Main Frame
	private void setSubClosingAction(JFrame sub, JFrame main) {
		sub.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				sub.dispose();
				main.setVisible(true);
			}
		});
	}

// SEND TO SERVER
	private void sendToServer(String msg) {
		try {
			toServer.writeUTF(msg);
			System.out.println("Send to server : " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

// CLOSE SOCKET
	private void socketStop() {
		try {
			toServer.writeUTF("ClosingSocket");
			listener.stop();
			fromServer.close();
			toServer.close();
			server.close();
			System.out.println("SocketStop");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//RECEIVER FROM SERVER
	private void loginAccepted() {
		loginFrm.dispose();
		listener.stop();
		//
		new RoomSession(server,name).start();
	}

	private void signupSuccessed() {
		signupFrm.dispose();
		loginFrm.setVisible(true);
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
					// Login
					if (t[0].equals("Login")) {
						if (t[1].equals("Accepted")) {
							loginAccepted();
						} else if(t[1].equals("Rejected")) {
							MessageBox.showAlert(loginFrm, "Login Rejected. Check your username or password!", "Alert");
						} else {
							MessageBox.showAlert(loginFrm, "Login Rejected. Account is in using!", "Alert");
						}

					}
					// Signup
					else if (t[0].equals("Signup")) {
						if (t[1].equals("PasswordNotMatch")) {
							MessageBox.showAlert(signupFrm, "Password NOT match", "Alert");
						} else if (t[1].equals("UsernameIsExisted")) {
							MessageBox.showAlert(signupFrm, "Username is existed", "Alert");
						} else if (t[1].equals("Failed")) {
							MessageBox.showAlert(signupFrm, "Can't Signup. Try again later", "Alert");
						} else {
							MessageBox.showMessage(signupFrm, "Signup successed. Please login !");
							signupSuccessed();
						}

					}
				} catch (IOException e) {
					e.printStackTrace();
					socketStop();
				}
			}
		}
	}
}
