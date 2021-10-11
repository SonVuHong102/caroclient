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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import utils.Value;

/**
 *
 * @author Son Vu
 */
public class LoginSuccessedFrm extends JFrame {
	private JButton btnJoin;
	private JButton btnCreate;
	
	public LoginSuccessedFrm() {
		initFrm();
		initComponents();
		setVisible(true);
	}
	
	private void initFrm() {
		setSize(new Dimension(300,100));
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void initComponents() {
		JPanel container = new JPanel();
		container.setSize(300,60);
		btnCreate = new JButton("Create");
		container.add(btnCreate);
		btnJoin = new JButton("Join");
		container.add(btnJoin);
		this.add(container,BorderLayout.CENTER);
	}

	public JButton getBtnJoin() {
		return btnJoin;
	}

	public JButton getBtnCreate() {
		return btnCreate;
	}

}
