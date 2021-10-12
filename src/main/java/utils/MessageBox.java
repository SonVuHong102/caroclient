package utils;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MessageBox {
	
	public static void showMessage(JFrame parent,String msg) {
		JOptionPane.showMessageDialog(parent, msg, "Notification",JOptionPane.INFORMATION_MESSAGE);
	}
	public static void showAlert(JFrame parent,String msg, String title) {
		JOptionPane.showMessageDialog(parent, msg,title,JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean showYesNo(JFrame parent,String msg, String title) {
		int isYes = JOptionPane.showConfirmDialog(parent, msg, title, JOptionPane.YES_NO_OPTION);
		if(isYes == JOptionPane.YES_OPTION)
			return true;
		return false;
	}
}
