/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.Value;

/**
 *
 * @author Son Vu
 */
public class PlayingSession {
	private Socket server;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private ObjectOutputStream objToServer;

	private String name;
	private String opp;

	private ServerListener listener;

	private PlayingFrm playingFrm;
	private int boardStatus[][];
	private int boardID;
	private int side;
	private String color;
	private boolean isMoved = true;
	
	private JLabel myTimerArea;
	private JLabel oppTimerArea;
	private JTextArea chatBox;
	private JTextField msgBox;
	private JButton btnSend;
	private JButton btnSurr;

	public PlayingSession(String name, String opp, Socket server, int side, int boardID) {
		this.server = server;
		this.opp = opp;
		this.name = name;
		this.side = side;
		this.boardID = boardID;
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
//		initTimerArea();
		initChatArea();
		initFunctionArea();
	}

	private void initBoard() {
		boardStatus = new int[Value.blockSize][Value.blockSize];
		playingFrm = new PlayingFrm();
		setMainClosingAction(playingFrm);
		color = (side == 1) ? "RED" : "BLUE";
		if(side == 1) {
			isMoved = false;
			playingFrm.setTitle(name + " ("+color+") : Your turn");
		} else {
			playingFrm.setTitle(name + " ("+color+") : Opp turn");
		}
		playingFrm.getBoard().addMouseListener(new MouseAdapter() {
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
	
	private void initTimerArea() {
		myTimerArea = playingFrm.getMyTimerLabel();
		oppTimerArea = playingFrm.getOppTimerLabel();
		// TODO
	}
	
	private void initChatArea() {
		chatBox = playingFrm.getChatBox();
		msgBox = playingFrm.getMsgBox();
		btnSend = playingFrm.getBtnSend();
		playingFrm.setFocusable(true);
		playingFrm.getRootPane().setDefaultButton(btnSend);
		chatBox.setText("----ChatBox----");
		btnSend.addActionListener(e -> {
			String msg = msgBox.getText().trim();
			if(!msg.isEmpty()) {
				chatBox.setText(chatBox.getText() + "\n" + name + " : " + msg);
				msgBox.setText("");
				sendToServer("Chat " + opp);
				sendToServer(msg);
				
			}
		});
		btnSend.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					btnSend.doClick();
				}
			}
		});
	}
	
	private void initFunctionArea() {
		btnSurr = playingFrm.getBtnSurrender();
		btnSurr.addActionListener(e -> {
			if(MessageBox.showYesNo(playingFrm, "Surrender ?", "Alert")) {
				sendToServer("Surrender " + opp);
				if(MessageBox.showYesNo(playingFrm, "Return to room channel ?", "Alert")) {
					new RoomSession(server,name).start();
					listener.stop();
				} else {
					socketStop();
				}
				playingFrm.dispose();
			}
		});
	}

	private void tryMove(int row, int column) {
		boardStatus[row][column] = side;
		isMoved = true;
		playingFrm.boardRepaint(boardStatus);
		playingFrm.setTitle(name + " ("+color+") : Opp turn"); 
		sendToServer("Move " + opp + " " + row + " " + column);
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
//					frame.dispose();
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
					if(t[0].equals("Invitation")) {
						sendToServer("InPlaying " + t[1]);
					} else if (t[0].equals("ExitedGame")) {
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
						playingFrm.setTitle(name + " (" + color + ") : Your turn");
					} else if(t[0].equals("Chat")) {
						msg = fromServer.readUTF();
						chatBox.setText(chatBox.getText() + "\n" + opp + " : " + msg);
					} else if (t[0].equals("OppSurrender")) {
						if(MessageBox.showYesNo(playingFrm, opp + " has surrendered ! Return to room channel ?","You win !")) {
							new RoomSession(server,name).start();
							listener.stop();
						} else {
							socketStop();
						}
						playingFrm.dispose();
					}

				} catch (IOException e) {
					e.printStackTrace();
					socketStop();
				}
			}
		}
	}

}
