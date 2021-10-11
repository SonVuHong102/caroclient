/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.Value;

/**
 *
 * @author Son Vu
 */
public class LoginSuccessedFrm extends JFrame {
	private JComboBox box;
	private JButton btnRefresh;
	private JButton btnCreate;
	private JButton btnJoin;
	
	
	public LoginSuccessedFrm() {
		initFrm();
		initComponents();
		setVisible(true);
	}
	
	private void initFrm() {
		setSize(new Dimension(300,150));
		setResizable(false);
		setTitle("Create room or select a opponent");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
	}
	
	private void initComponents() {
		JPanel container = new JPanel();
		
		
		JPanel room = new JPanel();
		box = new JComboBox();
		box.setPrototypeDisplayValue("init width for box");
		btnRefresh = new JButton("Refresh");
		room.add(box);
		room.add(btnRefresh);
		container.add(room);
		
		JPanel choices = new JPanel();
		btnCreate = new JButton("Create");
		btnJoin = new JButton("Join");
		choices.add(btnCreate);
		choices.add(btnJoin);
		container.add(choices);
		
		this.add(container,BorderLayout.CENTER);
	}
	
	public JComboBox getBox() {
		return box;
	}


	public JButton getBtnRefresh() {
		return btnRefresh;
	}


	public JButton getBtnCreate() {
		return btnCreate;
	}


	public JButton getBtnJoin() {
		return btnJoin;
	}

}
