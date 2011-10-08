package org.sergeys.webcachedigger.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FileSearchProgressDialog 
extends JDialog 
implements PropertyChangeListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	JProgressBar progressBar;
	SwingWorker<ArrayList<CachedFile>, Integer> worker;
	
	/**
	 * Create the dialog.
	 */
	public FileSearchProgressDialog(SwingWorker<ArrayList<CachedFile>, Integer> worker) {
		setBounds(100, 100, 350, 100);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{1.0};
		gridBagLayout.columnWeights = new double[]{1.0};
	
		getContentPane().setLayout(gridBagLayout);
		
		Box verticalBox = Box.createVerticalBox();
		GridBagConstraints gbc_verticalBox = new GridBagConstraints();
		gbc_verticalBox.insets = new Insets(0, 15, 0, 15);
		gbc_verticalBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_verticalBox.gridx = 0;
		gbc_verticalBox.gridy = 0;
		getContentPane().add(verticalBox, gbc_verticalBox);
		
		JLabel lblNotificationString = new JLabel("Notification string");
		verticalBox.add(lblNotificationString);
		
		//JProgressBar progressBar = new JProgressBar();
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setMaximum(1000);
		verticalBox.add(progressBar);

		this.worker = worker;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		int progress = worker.getProgress();
		progressBar.setValue(progress);		
	}


}
