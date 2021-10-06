/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.io.IOException;
import java.net.Socket;
/**
 *
 * @author Son Vu
 */
public class Client {
	private int portNum;
	private String hostAdd;

	public Client(int portNum, String hostAdd) {
		this.portNum = portNum;
		this.hostAdd = hostAdd;
	}
	
	public void start() {
		System.out.println("Client created. Connecting to server...");
		try {
			Socket socket = new Socket(hostAdd,portNum);
			System.out.println("Connected to Server : " + socket.getInetAddress().getHostAddress() + " - " + socket.getPort());
			SessionClient session = new SessionClient(socket);
			session.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
}
