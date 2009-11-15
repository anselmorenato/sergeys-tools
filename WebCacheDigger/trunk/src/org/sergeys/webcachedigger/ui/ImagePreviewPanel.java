package org.sergeys.webcachedigger.ui;

import java.awt.GridBagLayout;

import org.sergeys.webcachedigger.logic.CachedFile;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagConstraints;

public class ImagePreviewPanel extends FilePreviewPanel {

	public ImagePreviewPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		jLabelImage = new JLabel();
		jLabelImage.setText("JLabel");
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.add(jLabelImage, BorderLayout.CENTER);
		this.add(getJPanelBottom(), BorderLayout.SOUTH);
	}
	
	/* (non-Javadoc)
	 * @see org.sergeys.webcachedigger.ui.FilePreviewPanel#setCachedFile(org.sergeys.webcachedigger.logic.CachedFile)
	 */
	@Override
	public void setCachedFile(CachedFile cachedFile) {
		
		super.setCachedFile(cachedFile);
		ImageIcon imageIcon = new ImageIcon(cachedFile.getAbsolutePath()); 
		jLabelImage.setIcon(imageIcon);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel jLabelImage = null;
	private JPanel jPanelBottom = null;
	private JLabel jLabel1 = null;
	private JLabel jLabelImageFormat = null;
	private JLabel jLabel2 = null;
	private JLabel jLabelImageSize = null;
	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelBottom() {
		if (jPanelBottom == null) {
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 1;
			gridBagConstraints2.gridy = 1;
			jLabelImageSize = new JLabel();
			jLabelImageSize.setText("<unknown>");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 1;
			jLabel2 = new JLabel();
			jLabel2.setText("Size:");
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.gridx = 1;
			gridBagConstraints.gridy = 0;
			jLabelImageFormat = new JLabel();
			jLabelImageFormat.setText("<unknown>");
			jLabel1 = new JLabel();
			jLabel1.setText("Image format:");
			jPanelBottom = new JPanel();
			jPanelBottom.setLayout(new GridBagLayout());
			jPanelBottom.add(jLabel1, new GridBagConstraints());
			jPanelBottom.add(jLabelImageFormat, gridBagConstraints);
			jPanelBottom.add(jLabel2, gridBagConstraints1);
			jPanelBottom.add(jLabelImageSize, gridBagConstraints2);
		}
		return jPanelBottom;
	}

	
}
