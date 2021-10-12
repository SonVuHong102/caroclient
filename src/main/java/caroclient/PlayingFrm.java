/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import javax.swing.JFrame;

import utils.Value;
/**
 *
 * @author Son Vu
 */
public class PlayingFrm extends JFrame {	
	private final int mainFrameSize = Value.playingFrmSize;
	
	private Board container;
	
	public PlayingFrm(String name,String oppName, int boardStatus[][],int side) {
		initMainFrm(name,oppName,side);
		initBoard(boardStatus);
		
		add(container);
		setVisible(true);
	}
	private void initMainFrm(String name,String oppName,int side) {
		if(side == 1)
			setTitle("Player " + name + " : RED  \\\\//\\\\//  Opponent " + oppName + " : BLUE");
		else
			setTitle("Player " + name + " : BLUE  \\\\//\\\\//  Opponent " + oppName + " : RED");
		setSize(mainFrameSize,mainFrameSize);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}
	
	private void initBoard(int boardStatus[][]) {
		container = new Board(boardStatus);
	}
	
	public Board getContainer() {
		return container;
	}
}
