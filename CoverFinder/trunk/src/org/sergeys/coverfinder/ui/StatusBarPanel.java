package org.sergeys.coverfinder.ui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StatusBarPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	JLabel lblProgressindicator;
	JLabel lblMessage;
	JButton btnCancel;
	
	/**
	 * Create the panel.
	 */
	public StatusBarPanel() {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		lblProgressindicator = new JLabel(""); //$NON-NLS-1$
		lblProgressindicator.setIcon(new ImageIcon(StatusBarPanel.class.getResource("/images/progress.gif"))); //$NON-NLS-1$
		add(lblProgressindicator);
		
		btnCancel = new JButton(Messages.getString("StatusBarPanel.Cancel")); //$NON-NLS-1$
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doCancel();
			}
		});
		add(btnCancel);
		
		lblMessage = new JLabel("message"); //$NON-NLS-1$
		add(lblMessage);

	}

	protected void doCancel() {
		// TODO Auto-generated method stub
		
	}

	public void setMessage(String message){
		lblMessage.setText(message);
	}
	
	public void setWorking(boolean isWorking){
		lblProgressindicator.setVisible(isWorking);
		btnCancel.setVisible(isWorking);
	}
	
	public void setMessage(String message, boolean isWorking){
		lblMessage.setText(message);
		lblProgressindicator.setVisible(isWorking);
		btnCancel.setVisible(isWorking);
	}

}
