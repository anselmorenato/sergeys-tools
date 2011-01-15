package org.sergeys.desktopreminder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AboutDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		try {
//			AboutDialog dialog = new AboutDialog();
//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//			dialog.setVisible(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * Create the dialog.
	 */
	public AboutDialog(Frame owner) {
		super(owner);
		setTitle("About Reminder");
		
		setModal(true);
				
		
		setBounds(100, 100, 346, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JPanel panel = new JPanel();
			panel.setBorder(new EmptyBorder(50, 0, 50, 0));
			contentPanel.add(panel);
			panel.setLayout(new GridLayout(3, 1, 0, 0));
			{
				JLabel lblReminderThe = new JLabel("Reminder - the timer application");
				panel.add(lblReminderThe);
				lblReminderThe.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				JLabel label = new JLabel("2011");
				panel.add(label);
				label.setHorizontalAlignment(SwingConstants.CENTER);
			}
			{
				JLabel lblSergeySelivanov = new JLabel("Sergey Selivanov");
				panel.add(lblSergeySelivanov);
				lblSergeySelivanov.setHorizontalAlignment(SwingConstants.CENTER);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						setVisible(false);
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
