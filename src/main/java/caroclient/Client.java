/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Random;

import utils.Value;
/**
 *
 * @author Son Vu
 */
public class Client {

	public Client() {
	}
	
	public void start() {
		System.out.println("Client created. Connecting to server...");
		try {
			MulticastSocket client = new MulticastSocket(Value.clientPort);
			client.joinGroup(new InetSocketAddress(InetAddress.getByName(Value.groupAddress),Value.clientPort),NetworkInterface.getByName(Value.hostAddress));
			client.setSoTimeout(20000);
			// Request : [connect] - Response : [accept connection] (Connection accepted, create new ClientSession)
			String msg = "connect";
			DatagramPacket packet = new DatagramPacket(msg.getBytes(),msg.length(),InetAddress.getByName(Value.serverAddress),Value.serverPort);
			client.send(packet);
			System.out.println("Sent connect request to server");
			byte[] buf = new byte[1024];
			packet = new DatagramPacket(buf,buf.length);
			client.receive(packet);
			msg = new String(buf);
			// Request : [connect] - Response : [accept connection] (Connection accepted, create new ClientSession)
			if(msg.trim().equalsIgnoreCase("accept connection")) {
				ClientSession newClient = new ClientSession(client);
				newClient.start();
				return;
			}
			
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
}
