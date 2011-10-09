package org.sergeys.webcachedigger.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;
import javax.swing.BoxLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class FileSearchProgressDialog 
extends JDialog 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SwingWorker<ArrayList<CachedFile>, Integer> worker;
	
	/**
	 * Create the dialog.
	 */
	public FileSearchProgressDialog(SwingWorker<ArrayList<CachedFile>, Integer> worker) {
		setTitle("Search files");
		setModal(true);
		setBounds(100, 100, 350, 129);

		this.worker = worker;
		
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(0, 50, 0, 0));
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JLabel lblFilesFound = new JLabel("Files found:");
		panel.add(lblFilesFound);
		
		JLabel lblCount = new JLabel("0");
		lblCount.setHorizontalAlignment(SwingConstants.LEFT);
		lblCount.setPreferredSize(new Dimension(100, 20));
		panel.add(lblCount);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doCancel();
			}
		});
		panel_1.add(btnCancel);
		
		JPanel panel_2 = new JPanel();
		getContentPane().add(panel_2, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("");
		//lblNewLabel.setIcon(new ImageIcon(FileSearchProgressDialog.class.getResource("/images/search.png")));
		panel_2.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		
		//label.setIcon(new ImageIcon(FileSearchProgressDialog.class.getResource("/images/Zoom.png")));
		//label.setPreferredSize(new Dimension(48, 48));
		panel_2.add(label);
				
		
	}

	protected void doCancel() {
		// TODO Auto-generated method stub
		this.setVisible(false);
	}

}
