/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Son Vu
 */
public class LoginSuccessedFrm extends JFrame {
	private JComboBox box;
	private JButton btnRefresh;
	private JButton btnCreate;
	private JButton btnInvite;
	private JLabel status;
	
	public LoginSuccessedFrm() {
		initFrm();
		initComponents();
		setVisible(true);
	}
	
	private void initFrm() {
		setSize(new Dimension(300,180));
		setResizable(false);
		setTitle("Create or Invite");
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
		btnInvite = new JButton("Invite");
		choices.add(btnCreate);
		choices.add(btnInvite);
		container.add(choices);
		
		JPanel label = new JPanel();
		label.setPreferredSize(new Dimension(290,20));
		status = new JLabel("Status : ");
		label.add(status);
		container.add(label);
		
		this.add(container);
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


	public JButton getBtnInvite() {
		return btnInvite;
	}
	
	public JLabel getStatus() {
		return status;
	}

}
