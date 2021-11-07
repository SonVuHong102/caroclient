package caroclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class RoomSession {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;

	
	private String name;
	private ServerListener listener = null;

	private RoomFrm roomFrm;
	private DefaultTableModel model;

	public RoomSession(Socket server,String name) {
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
		sendToServer("Refresh");
		JTable roomTable = roomFrm.getTblClient();
		roomFrm.getBtnInvite().addActionListener(e -> {
			String opp = (String) roomTable.getValueAt(roomTable.getSelectedRow(), roomTable.getSelectedColumn());
			if(opp == null) 
				return;
			sendToServer("Invite " + opp);
			MessageBox.showMessage(roomFrm, "Invited " + opp + " .Please wait for response or invite another...");
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
					if(t[0].equals("Refresh")) {
						model = roomFrm.getModel();
						model.setRowCount(0);
						for(int i=1;i<t.length;i++) {
							if(!t[i].equals(name))
								model.addRow(new Object[] {t[i]});
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
