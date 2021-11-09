/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Son Vu
 */
public class PlayingFrm extends JFrame {
	private Board board;
	private TimerArea timerArea;
	private ChatArea chatArea;
	private FunctionArea funcArea;
	private GridBagLayout gb;
	private GridBagConstraints gbc;

	public PlayingFrm() {
		initMainFrm();
		initComponents();
		pack();
		setVisible(true);
	}

	private void initMainFrm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false); 
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		setLayout(gb);
	}

	private void initComponents() {
		board = new Board();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 9;
		add(board,gbc);
		
//		timerArea = new TimerArea();
//		gbc.gridx = 1;
//		gbc.gridy = 0;
//		gbc.gridheight = 2;
//		add(timerArea,gbc);
		
		chatArea = new ChatArea();
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		add(chatArea,gbc);
		
		funcArea = new FunctionArea();
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridheight = 2;
		add(funcArea,gbc);
		
	}


	public void boardRepaint(int[][] boardStatus) {
		board.boardRepaint(boardStatus);
	}

	public Board getBoard() {
		return board;
	}
	
	public JLabel getMyTimerLabel() {
		return timerArea.getMyTimerLabel();
	}
	
	public JLabel getOppTimerLabel() {
		return timerArea.getOppTimerLabel();
	}
	
	public JTextField getMsgBox() {
		return chatArea.getMsgBox();
	}
	
	public JTextArea getChatBox() {
		return chatArea.getChatBox();
	}
	
	public JButton getBtnSend() {
		return chatArea.getBtnSend();
	}
	
	public JButton getBtnSurrender() {
		return funcArea.getBtnSurrender();
	}

}
