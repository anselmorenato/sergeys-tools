package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.sergeys.webcachedigger.logic.CachedFile;

public class ImagePreviewPanel extends FilePreviewPanel {

	public ImagePreviewPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		jLabelImage = new JLabel();
		jLabelImage.setText("<image>");
		jLabelImage.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabelImage.setHorizontalAlignment(SwingConstants.CENTER);
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.add(getJPanelBottom(), BorderLayout.SOUTH);
		this.add(getJPanelCenter(), BorderLayout.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see org.sergeys.webcachedigger.ui.FilePreviewPanel#setCachedFile(org.sergeys.webcachedigger.logic.CachedFile)
	 */
	@Override
	public void setCachedFile(CachedFile cachedFile) {
		
		super.setCachedFile(cachedFile);
		ImageIcon imageIcon = new ImageIcon(cachedFile.getAbsolutePath());
		
		// TODO: scale down large image. 
		
//		int w = imageIcon.getIconWidth();
//		int h = imageIcon.getIconHeight();						
//		Image img = imageIcon.getImage();
		//imageIcon.setImage(img.getScaledInstance(100, 100, Image.SCALE_DEFAULT));
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
	private JPanel jPanelCenter = null;
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

	/**
	 * This method initializes jPanelCenter	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCenter() {
		if (jPanelCenter == null) {
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.BOTH;
			jPanelCenter = new JPanel();
			jPanelCenter.setLayout(new GridBagLayout());
			jPanelCenter.add(jLabelImage, gridBagConstraints3);
		}
		return jPanelCenter;
	}

	
}
