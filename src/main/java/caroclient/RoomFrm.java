/*
 * CODE,
 * CODE NUA,
 * CODE MAI...
 */
package caroclient;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Son Vu
 */
public class RoomFrm extends JFrame {
	private JTable tblClient;
	private JButton btnInvite;

	public RoomFrm() {
		initFrm();
		initCompoments();
		setVisible(true);
	}

	private void initFrm() {
		setResizable(false);
//		setLayout(new BorderLayout());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void initCompoments() {
		DefaultTableModel model = new DefaultTableModel() {
			@Override
		    public boolean isCellEditable(int row, int column) {
		       return false;
		    }
		};
		tblClient = new JTable(model);
		model.addColumn("Online players : ");
		JScrollPane scrollPane = new JScrollPane(tblClient);
		scrollPane.setPreferredSize(new Dimension(200,200));
		this.add(scrollPane,BorderLayout.CENTER);
		
		JPanel invite = new JPanel();
		btnInvite = new JButton("Invite");
		btnInvite.setPreferredSize(new Dimension(200,50));;
		invite.add(btnInvite,BorderLayout.CENTER);
		invite.setPreferredSize(new Dimension(100,100));
		this.add(invite,BorderLayout.SOUTH);
		
		this.setSize(400,400);
	}


	public JButton getBtnInvite() {
		return btnInvite;
	}
	
	public JTable getTblClient() {
		return tblClient;
	}


	public DefaultTableModel getModel() {
		return (DefaultTableModel) tblClient.getModel();
	}
}
