/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import utils.Value;


/**
 *
 * @author Son Vu
 */
public class SessionClient {
	private Socket socket;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private Socket opp;
	
	private LoginSuccessedFrm loginSuccessedFrm;
	private int side;
	
	private String name;

	public SessionClient(Socket socket) {
		this.socket = socket;
	}

	public void start() {
		try {
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());
			
			name = fromServer.readUTF();
			loginSuccessedFrm = new LoginSuccessedFrm();
			loginSuccessedFrm.setTitle(name);
			
			loginSuccessedFrm.getBtnCreate().addActionListener( e -> {
				try {
					toServer.writeUTF("create");
					listenCreate();
				} catch (IOException ex) {
					ex.printStackTrace();
				} 
			});
			
			loginSuccessedFrm.getBtnJoin().addActionListener(e -> {
				try {
					toServer.writeUTF("join");
					listenJoin();
					
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			});	
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void listenCreate() {
		try {
			loginSuccessedFrm.getBtnCreate().setEnabled(false);
			loginSuccessedFrm.getBtnJoin().setEnabled(false);
			int port = Integer.parseInt(fromServer.readUTF());
			ServerSocket ownServer = new ServerSocket(port);
			loginSuccessedFrm.getStatus().setText("Status : Created. Waiting for connection...");
			opp = ownServer.accept();
			side = 1;
			PlayingSession newPlayingSession = new PlayingSession(opp,side,name);
			newPlayingSession.start();
			loginSuccessedFrm.dispose();
		} catch (IOException ex) {
				ex.printStackTrace();
		}
	}
	
	private void listenJoin() {
		try {
			loginSuccessedFrm.getBtnCreate().setEnabled(false);
			loginSuccessedFrm.getBtnJoin().setEnabled(false);
			int port = Integer.parseInt(fromServer.readUTF());
			String hostAdd = fromServer.readUTF();
			opp = new Socket(hostAdd,port);
			side = -1;
			PlayingSession newPlayingSession = new PlayingSession(opp,side,name);
			newPlayingSession.start();
			loginSuccessedFrm.dispose();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
