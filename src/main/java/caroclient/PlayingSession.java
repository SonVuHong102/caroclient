/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import utils.Value;

/**
 *
 * @author Son Vu
 */
public class PlayingSession {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	
	private String name;
	private String opp;
	
	private ServerListener listener;

	private PlayingFrm playingFrm;
	private int boardStatus[][];
	private int side;

	public PlayingSession(String name,String opp,Socket server,int side) {
		this.server = server;
		this.opp = opp;
		this.name = name;
		this.side = side;
	}

	public void start() {
		listener = new ServerListener();
		listener.start();
		initBoard();
		initCompoments();
	}

	private void initBoard() {
//		try {
			boardStatus = new int[Value.blockSize][Value.blockSize];
			playingFrm = new PlayingFrm(name, boardStatus);
			
			playingFrm.getContainer().addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					if (e.getX() <= Value.boardMargin || e.getY() <= Value.boardMargin)
						return;
					int column = (e.getX() - Value.boardMargin) / Value.blockSize;
					int row = (e.getY() - Value.boardMargin) / Value.blockSize;
					if (column >= Value.blockNum || row >= Value.blockNum)
						return;
					tryMove(row, column);
				}
			});
//		} catch (IOException ex) {
//		}
	}

	private void initCompoments() {
		// Status Label
	}

	private void tryMove(int row, int column) {
//		try {
//			
//		} catch (IOException ex) {
//		}
	}
	
// CLOSE SOCKET
	private void socketStop() {
		try {
			toServer.writeUTF("ClosingSocket " + opp);
			listener.stop();
			fromServer.close();
			toServer.close();
			server.close();
			System.out.println("SocketStop");
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
					if(t[0].equals("ExitedGame")) {
						if(MessageBox.showYesNo(playingFrm, t[1] + " has exited the game. Do you want to back to room ?", "Game Cancelled")) {
							// YES
							
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
