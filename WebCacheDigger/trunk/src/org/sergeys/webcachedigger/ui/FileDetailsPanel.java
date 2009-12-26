package org.sergeys.webcachedigger.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.sergeys.webcachedigger.logic.CachedFile;
import javax.swing.border.SoftBevelBorder;

public class FileDetailsPanel extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = 1L;
	private JLabel jLabel1 = null;
	private JLabel jLabelFileName = null;
	private JLabel jLabel2 = null;
	private JLabel jLabelFileSize = null;
	private JButton jButtonPreview = null;
	private JPanel jPanelPreview = null;

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
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.gridx = 1;
		gridBagConstraints11.gridy = 0;
		gridBagConstraints11.fill = GridBagConstraints.BOTH; 
		GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
		gridBagConstraints4.gridx = 0;
		gridBagConstraints4.gridwidth = 3;
		gridBagConstraints4.gridy = 3;
		GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
		gridBagConstraints3.gridx = 1;
		gridBagConstraints3.gridy = 2;
		jLabelFileSize = new JLabel();
		jLabelFileSize.setText("<unknown>");
		GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
		gridBagConstraints2.gridx = 0;
		gridBagConstraints2.gridy = 2;
		jLabel2 = new JLabel();
		jLabel2.setText("File size:");
		GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
		gridBagConstraints1.gridx = 1;
		//gridBagConstraints1.gridwidth = 2;
		gridBagConstraints1.gridy = 1;
		jLabelFileName = new JLabel();
		jLabelFileName.setText("<unknown>");
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		jLabel1 = new JLabel();
		jLabel1.setText("File name:");
		this.setSize(217, 114);
		this.setLayout(new GridBagLayout());
		this.setBorder(new SoftBevelBorder(SoftBevelBorder.LOWERED));
		this.add(jLabel1, gridBagConstraints);
		this.add(jLabelFileName, gridBagConstraints1);
		this.add(jLabel2, gridBagConstraints2);
		this.add(jLabelFileSize, gridBagConstraints3);
		this.add(getJButtonPreview(), gridBagConstraints4);
		this.add(getJPanelPreview(), gridBagConstraints11);
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
		if(evt.getPropertyName() == CachedFile.SELECTED_FILE){
			CachedFile file = (CachedFile)evt.getNewValue();
			jLabelFileName.setText(file.getName());
			jLabelFileSize.setText(String.valueOf(file.length()));
			
			FilePreviewPanel preview = FilePreviewPanel.createFilePreviewPanel(file.getFileType());
			if(preview != null){
				preview.setCachedFile(file);							
				//jPanelPreview = preview;								
			}
			else{
				//jPanelPreview = null;
				//jPanelPreview = new JPanel();	// empty panel
			}
			GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
			gridBagConstraints11.gridx = 1;
			gridBagConstraints11.gridy = 0;
			gridBagConstraints11.fill = GridBagConstraints.BOTH;
			this.remove(getJPanelPreview());
			jPanelPreview = (preview == null) ? new JPanel() : preview; 
			this.add(getJPanelPreview(), gridBagConstraints11);
			invalidate();
		}		
	}

	/**
	 * This method initializes jPanelPreview	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelPreview() {
		if (jPanelPreview == null) {
			jPanelPreview = new JPanel();
			jPanelPreview.setLayout(new FlowLayout());
		}
		return jPanelPreview;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"

