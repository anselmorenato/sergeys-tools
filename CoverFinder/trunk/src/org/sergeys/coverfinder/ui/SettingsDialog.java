package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SettingsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SettingsDialog dialog = new SettingsDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SettingsDialog() {
		setTitle("Settings");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblLibraryFolders = new JLabel("Library folders:");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblLibraryFolders, 10, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, lblLibraryFolders, 10, SpringLayout.WEST, contentPanel);
		contentPanel.add(lblLibraryFolders);
		
		JButton button = new JButton("+");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doAddDirectory();
			}
		});
		sl_contentPanel.putConstraint(SpringLayout.NORTH, button, -4, SpringLayout.NORTH, lblLibraryFolders);
		sl_contentPanel.putConstraint(SpringLayout.WEST, button, -51, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, button, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(button);
		
		JButton button_1 = new JButton("-");
		sl_contentPanel.putConstraint(SpringLayout.NORTH, button_1, 4, SpringLayout.SOUTH, button);
		sl_contentPanel.putConstraint(SpringLayout.WEST, button_1, -51, SpringLayout.EAST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, button_1, -10, SpringLayout.EAST, contentPanel);
		contentPanel.add(button_1);
		
		JScrollPane scrollPane = new JScrollPane();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, scrollPane, -5, SpringLayout.NORTH, lblLibraryFolders);
		sl_contentPanel.putConstraint(SpringLayout.WEST, scrollPane, 6, SpringLayout.EAST, lblLibraryFolders);
		sl_contentPanel.putConstraint(SpringLayout.SOUTH, scrollPane, 90, SpringLayout.NORTH, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.EAST, scrollPane, -11, SpringLayout.WEST, button);
		contentPanel.add(scrollPane);
		
		JList list = new JList();
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {"one", "two", "three", "one", "two", "three", "one", "two", "three"};
			public int getSize() {
				return values.length;
			}
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		scrollPane.setViewportView(list);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton saveButton = new JButton("Save");
				saveButton.setActionCommand("OK");
				buttonPane.add(saveButton);
				getRootPane().setDefaultButton(saveButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	protected void doAddDirectory() {
		DirSelectorDialog dlg = new DirSelectorDialog();
		dlg.setLocationRelativeTo(this);
		dlg.setVisible(true);
		
	}
}
