/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.io.IOException;
import java.net.Socket;

import utils.Value;
/**
 *
 * @author Son Vu
 */
public class Client {
	
	public void startClient() {
		try {
			Socket socket = new Socket(Value.serverAddress,Value.serverPort);
			System.out.println("Connected to Server : " + socket.getInetAddress().getHostAddress() + " - " + socket.getPort());
			LoginSession session = new LoginSession(socket);
			session.start();
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
}
