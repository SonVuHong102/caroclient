package caroclient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimerArea extends JPanel {
	
	private JLabel timerLabel;
	private JLabel myTimerLabel;
	private JLabel oppTimerLabel;
	
	public TimerArea() {
		initArea();
	}
	
	public void initArea() {
		setLayout(new GridLayout(3,1));
		setBackground(Color.white);
		timerLabel = new JLabel("Remaining time");
		myTimerLabel = new JLabel("Yours : "); 
		oppTimerLabel = new JLabel("Opp's : ");
		this.add(timerLabel);
		this.add(myTimerLabel);
		this.add(oppTimerLabel);
	}

	public JLabel getMyTimerLabel() {
		return myTimerLabel;
	}

	public JLabel getOppTimerLabel() {
		return oppTimerLabel;
	}
	
}
