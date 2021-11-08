package caroclient;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RoomSession {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;

	private String name;
	private String opp;
	private ServerListener listener = null;

	private RoomFrm roomFrm;
	private DefaultTableModel model;
	
	private int side;

	public RoomSession(Socket server, String name) {
		this.server = server;
		this.name = name;
		try {
			fromServer = new DataInputStream(server.getInputStream());
			toServer = new DataOutputStream(server.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		listener = new ServerListener();
		listener.start();

		createRoomFrm();
	}

//UI Action

	private void createRoomFrm() {
		roomFrm = new RoomFrm();
		roomFrm.setTitle("Your name : " + name);
		setMainClosingAction(roomFrm);
		sendToServer("RefreshPlayer");
		JTable roomTable = roomFrm.getTblClient();
		roomFrm.getBtnInvite().addActionListener(e -> {
			String player = (String) roomTable.getValueAt(roomTable.getSelectedRow(), roomTable.getSelectedColumn());
			if (player == null)
				return;
			sendToServer("Invite " + player);
		});

	}
	
	private void createPlayRoom() {
		roomFrm.dispose();
		listener.stop();
		new PlayingSession(name,opp,server,side).start();
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

// SEND TO SERVER
	private void sendToServer(String msg) {
		try {
			toServer.writeUTF(msg);
			System.out.println("Send to server : " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

// SOCKET CLOSE
	private void socketStop() {
		try {
			toServer.writeUTF("ClosingSocket");
			listener.stop();
			fromServer.close();
			toServer.close();
			server.close();
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
					System.out.println("Receive from server : " + msg);
					String[] t = msg.split(" ");
					// Refresh
					if (t[0].equals("RefreshPlayer")) {
						model = roomFrm.getModel();
						model.setRowCount(0);
						for (int i = 1; i < t.length; i++) {
							if (!t[i].equals(name))
								model.addRow(new Object[] { t[i] });
						}
					} else if (t[0].equals("InvitedPlayer")) {
						MessageBox.showMessage(roomFrm,
								"Invited " + t[1] + " .Please wait for response or invite another...");
					} else if (t[0].equals("Invitation")) {
						// YES
						if (MessageBox.showYesNo(roomFrm, t[1] + " want to play. Accept ?", "Invitation")) {
							sendToServer("AcceptedInvitation " + t[1]);
							opp = t[1];
							side = 2;
							createPlayRoom();
						} else { // NO
							sendToServer("RejectedInvitation " + t[1]);
						}

					} else if (t[0].equals("RejectedInvitation")) {
						MessageBox.showMessage(roomFrm, t[1] + " rejected your invitation.");
					} else if (t[0].equals("AcceptedInvitation")) {
						MessageBox.showMessage(roomFrm, t[1] + " accepted your invitation.");
						opp = t[1];
						side = 1;
						createPlayRoom();
					}

				} catch (IOException e) {
					e.printStackTrace();
					socketStop();
				}
			}
		}
	}
}
