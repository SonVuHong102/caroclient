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
	
	public PlayingFrm(String name, int boardStatus[][]) {
		initMainFrm(name);
		initBoard(boardStatus);
		
		add(container);
		setVisible(true);
	}
	private void initMainFrm(String name) {
		setTitle(name);
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
