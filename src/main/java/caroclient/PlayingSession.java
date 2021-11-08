/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;

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
	private boolean isMoved = true;

	public PlayingSession(String name, String opp, Socket server, int side) {
		this.server = server;
		this.opp = opp;
		this.name = name;
		this.side = side;
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
		initBoard();
	}

	private void initBoard() {
		boardStatus = new int[Value.blockSize][Value.blockSize];
		playingFrm = new PlayingFrm(name, boardStatus);
		setMainClosingAction(playingFrm);
		if(side == 1)
			isMoved = false;
		playingFrm.getContainer().addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getX() <= Value.boardMargin || e.getY() <= Value.boardMargin)
					return;
				int column = (e.getX() - Value.boardMargin) / Value.blockSize;
				int row = (e.getY() - Value.boardMargin) / Value.blockSize;
				if (column >= Value.blockNum || row >= Value.blockNum)
					return;
				if (boardStatus[row][column] != 0 || isMoved==true)
					return;
				tryMove(row, column);
			}
		});
	}

	private void tryMove(int row, int column) {
		sendToServer("Move " + opp + " " + row + " " + column);
		boardStatus[row][column] = side;
		isMoved = false;
		playingFrm.boardRepaint(boardStatus);
	}

	private void sendToServer(String msg) {
		try {
			toServer.writeUTF(msg);
			System.out.println("Send to server : " + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

// CLOSE SOCKET
	private void socketStop() {
		try {
			toServer.writeUTF("ClosingSocket " + opp);
			listener.stop();
			fromServer.close();
			toServer.close();
			server.close();
			System.out.println("SocketStopped");
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
					if (t[0].equals("ExitedGame")) {
						if (MessageBox.showYesNo(playingFrm,t[1] + " has exited the game. Do you want to back to room ?", "Game Cancelled")) {
							new RoomSession(server,name).start();
						}
						playingFrm.dispose();
						this.stop();
					} else if(t[0].equals("OppMoved")) {
						int row = Integer.parseInt(t[1]);
						int column = Integer.parseInt(t[2]);
						boardStatus[row][column] = -side;
						isMoved = false;
						playingFrm.boardRepaint(boardStatus);
					}

				} catch (IOException e) {
					e.printStackTrace();
					socketStop();
				}
			}
		}
	}

}
