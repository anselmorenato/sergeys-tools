package org.sergeys.coverfinder.ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;

public class StatusBarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel lblProgressindicator;
	JLabel lblMessage;
	
	/**
	 * Create the panel.
	 */
	public StatusBarPanel() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		lblProgressindicator = new JLabel("");
		lblProgressindicator.setIcon(new ImageIcon(StatusBarPanel.class.getResource("/images/progress.gif")));
		add(lblProgressindicator);
		
		lblMessage = new JLabel("message");
		add(lblMessage);

	}

	public void setMessage(String message){
		lblMessage.setText(message);
	}
	
	public void setWorking(boolean isWorking){
		lblProgressindicator.setVisible(isWorking);
	}
	
	public void setMessage(String message, boolean isWorking){
		lblMessage.setText(message);
		lblProgressindicator.setVisible(isWorking);
	}

}
