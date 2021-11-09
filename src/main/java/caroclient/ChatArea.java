package caroclient;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatArea extends JPanel {
	private GridBagLayout gb;
	private GridBagConstraints gbc;
	
	private JTextArea chatBox;
	private JTextField msgBox;
	private JButton btnSend;
	
	public ChatArea() {
		initArea();
	}
	
	private void initArea() {
		gb = new GridBagLayout();
		gbc = new GridBagConstraints();
		setLayout(gb);
		
		chatBox = new JTextArea(20,30);
		chatBox.setEditable(false);
		JScrollPane scroll = new JScrollPane (chatBox,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		gbc.fill = GridBagConstraints.BOTH;
		addComponent(scroll, 0, 0, 5, 5);
		JPanel msg = new JPanel();
		JLabel label = new JLabel("Msg : ");
		msg.add(label);
		msgBox = new JTextField(30);
		msg.add(msgBox);
		btnSend = new JButton("Send");
		msg.add(btnSend);
		gbc.fill = GridBagConstraints.BOTH;
		addComponent(msg, 6, 0, 5, 5);
		
	}
	private void addComponent(Component c, int row, int col, int nrow, int ncol) {
		gbc.gridx = col;
		gbc.gridy = row;

		gbc.gridwidth = ncol;
		gbc.gridheight = nrow;

		gb.setConstraints(c, gbc);
		add(c);
	}

	public JTextArea getChatBox() {
		return chatBox;
	}

	public JTextField getMsgBox() {
		return msgBox;
	}

	public JButton getBtnSend() {
		return btnSend;
	}
	
}
