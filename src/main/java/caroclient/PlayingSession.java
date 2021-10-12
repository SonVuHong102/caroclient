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
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;

import utils.Value;

/**
 *
 * @author Son Vu
 */
public class PlayingSession {
	private final MulticastSocket client;
	private final String playerName;
	private final String oppName;
	private final int side;
	
	private PlayingFrm board;
	private int boardStatus[][];
	
	public PlayingSession(MulticastSocket client,String playerName,String oppName,int side) {
		this.client = client;
		this.playerName = playerName;
		this.oppName = oppName;
		this.side = side;
	}
	
	public void start() {
		initBoard();
		initCompoments();
		
	}
	
	private void initBoard() {
		boardStatus = new int[Value.blockSize][Value.blockSize];
		board = new PlayingFrm(playerName,oppName,boardStatus,side);
		// TODO initate listener
		
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
	}
	
	private void initCompoments() {
		// Status Label
	}
	
	private void tryMove(int row,int column) {
		if(boardStatus[row][column]==0) {
			boardStatus[row][column] = side;
			// TODO send move command
			
			board.getContainer().repaint();
		}
	}
	
	public class OppListener implements Runnable {
		
		private AtomicBoolean running = new AtomicBoolean(false);
		private Thread worker;
		
		public void start() {
			worker = new Thread(this);
			worker.start();
		}
		
		public void stop() {
			running.set(false);
		}
		
		public void run() {
			running.set(true);
			while(running.get()) {
				// TODO add action
			}
		}
	}
}
