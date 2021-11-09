/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import utils.Value;

/**
 *
 * @author Son Vu1
 */
public class Board extends JPanel {
	private final int blockNum = Value.blockNum;
	private final int blockSize = Value.blockSize;
	private final int boardSize = Value.boardSize;
	private int boardStatus[][];
	
	public Board() {
		boardStatus = new int[boardSize][boardSize];
		initBoard();
	}
	
	private void initBoard() {
		this.setSize(new Dimension(boardSize,boardSize));
		this.setPreferredSize(new Dimension(boardSize,boardSize));
		this.setBackground(Color.WHITE);
	}
	
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
		doDrawing(g);
    }
	private void doDrawing(Graphics g) {
		int boardMargin = Value.boardMargin;
		for(int i=0;i<blockNum;i++)
			for(int j=0;j<blockNum;j++) {
				Color c = Color.GRAY;
				if(boardStatus[i][j] == 1)
					c = Color.RED;
				else if(boardStatus[i][j] == -1)
					c = Color.BLUE;
				drawSquare(g, boardMargin + j* blockSize, boardMargin + i * blockSize,c);
			}
	}
	
	private void drawSquare(Graphics g, int x, int y, Color c) {
		g.setColor(c);
		g.fillRect(x+1,y+1,blockSize-1,blockSize-1);
	}
	
	public void boardRepaint(int[][] boardStatus) {
		this.boardStatus = boardStatus;
		this.repaint();
	}
	
	public int[][] getBoardStatus() {
		return boardStatus;
	}
}
