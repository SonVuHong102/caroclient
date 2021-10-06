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
import utils.Value;

/**
 *
 * @author Son Vu
 */
public class PlayingSession {
	private final Socket opp;
	private final String name;
	private DataInputStream fromOpp;
	private DataOutputStream toOpp;
	
	
	private PlayingFrm board;
	private int boardStatus[][];
	private final int side;
	
	public PlayingSession(Socket socket,int side,String name) {
		this.opp = socket;
		this.side = side;
		this.name = name;
	}
	
	public void start() {
		initBoard();
		initCompoments();
		
	}
	
	private void initBoard() {
		try {
			boardStatus = new int[Value.blockSize][Value.blockSize];
			board = new PlayingFrm(name,boardStatus);
			fromOpp = new DataInputStream(opp.getInputStream());
			toOpp = new DataOutputStream(opp.getOutputStream());
			Thread listener = new Thread(new OppListener());
			listener.start();
			String color = (side == 1) ? "RED" : "BLUE";
			board.setTitle(name + "  -  " + color);
			board.getContainer().addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e) {
					if(e.getX() <= Value.boardMargin || e.getY() <= Value.boardMargin)
						return;
					int column = (e.getX()-Value.boardMargin)/Value.blockSize;
					int row = (e.getY()-Value.boardMargin)/Value.blockSize;
					if(column >= Value.blockNum || row >= Value.blockNum)
						return;
					tryMove(row,column);
				}
			});
		} catch (IOException ex) {
		}
	}
	
	private void initCompoments() {
		// Status Label
	}
	
	private void tryMove(int row,int column) {
		try {
			if(boardStatus[row][column]==0) {
				boardStatus[row][column] = side;
				toOpp.writeUTF("move " + row + " " + column);
				board.getContainer().repaint();
			}
		} catch (IOException ex) {
		}
	}
	
	public class OppListener implements Runnable {
		public void run() {
			while(true) {
				try {
					String msg = fromOpp.readUTF();
					String[] command = msg.split(" ");
					if(command[0].equalsIgnoreCase("move")) {
						int row = Integer.parseInt(command[1]);
						int column = Integer.parseInt(command[2]);
						boardStatus[row][column] = -side;
						board.getContainer().repaint();
					}
				} catch (IOException ex) {
				} 
			}
		}
	}
}
