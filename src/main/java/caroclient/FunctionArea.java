package caroclient;

import javax.swing.JButton;
import javax.swing.JPanel;

public class FunctionArea extends JPanel {
	private JButton btnSurrender;
	
	public FunctionArea() {
		initArea();
	}

	private void initArea() {
		btnSurrender = new JButton("Surrender");
		add(btnSurrender);
	}

	public JButton getBtnSurrender() {
		return btnSurrender;
	}
	
}
