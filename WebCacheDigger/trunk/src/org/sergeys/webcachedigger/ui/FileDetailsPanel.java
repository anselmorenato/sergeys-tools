package org.sergeys.webcachedigger.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.CachedFile;

public class FileDetailsPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel1 = null;
	private JLabel jLabelFileName = null;
	private JLabel jLabel2 = null;
	private JLabel jLabelFileSize = null;
	private JButton jButtonPreview = null;

	/**
	 * This is the default constructor
	 */
	public FileDetailsPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridwidth = 3;
		gridBagConstraints4.gridy = 2;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 1;
		jLabelFileSize = new JLabel();
		jLabelFileSize.setText("<unknown>");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 1;
		jLabel2 = new JLabel();
		jLabel2.setText("File size:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		gridBagConstraints1.gridwidth = 3;
		gridBagConstraints1.gridy = 0;
		jLabelFileName = new JLabel();
		jLabelFileName.setText("<unknown>");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		jLabel1 = new JLabel();
		jLabel1.setText("File name:");
		this.setSize(217, 114);
		this.setLayout(new GridBagLayout());
		this.add(jLabel1, gridBagConstraints);
		this.add(jLabelFileName, gridBagConstraints1);
		this.add(jLabel2, gridBagConstraints2);
		this.add(jLabelFileSize, gridBagConstraints3);
		this.add(getJButtonPreview(), gridBagConstraints4);
	}

	/**
	 * This method initializes jButtonPreview	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJButtonPreview() {
		if (jButtonPreview == null) {
			jButtonPreview = new JButton();
			jButtonPreview.setText("Preview");
		}
		return jButtonPreview;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {		
		if(evt.getPropertyName() == "selectedfile"){
			CachedFile file = (CachedFile)evt.getNewValue();
			jLabelFileName.setText(file.getName());
			jLabelFileSize.setText(String.valueOf(file.length()));
		}		
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
