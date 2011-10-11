package org.sergeys.webcachedigger.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.sergeys.webcachedigger.logic.CachedFile;

public class ImagePreviewPanel extends FilePreviewPanel {

	public ImagePreviewPanel() {
		super();
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setSize(300, 200);
		this.add(getJPanelTop(), BorderLayout.NORTH);
		this.add(getJPanelCenter(), BorderLayout.CENTER);
	}
	
	ScaledImage scaledImage = new ScaledImage(null);
	
	@Override
	public void setCachedFile(CachedFile cachedFile) {
		
		super.setCachedFile(cachedFile);
		
		ImageIcon imageIcon = new ImageIcon(cachedFile.getAbsolutePath());
		
		lblImageSize.setText(imageIcon.getIconWidth() + " x " + imageIcon.getIconHeight());
		
		getJPanelCenter().remove(scaledImage);
		scaledImage = new ScaledImage(imageIcon.getImage());
		getJPanelCenter().add(scaledImage);
		invalidate();		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel jPanelTop = null;
	
	private JLabel jLabel2 = null;
	
	private JLabel lblImageSize;
	private JPanel jPanelCenter = null;
	
	/**
	 * This method initializes jPanelBottom	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTop() {
		if (jPanelTop == null) {
			GridBagConstraints gbc_lblImageSize = new GridBagConstraints();
			gbc_lblImageSize.anchor = GridBagConstraints.WEST;
			gbc_lblImageSize.gridx = 1;
			gbc_lblImageSize.gridy = 0;
			lblImageSize = new JLabel();
			lblImageSize.setText("<unknown>");
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.ipady = 5;
			gridBagConstraints1.ipadx = 5;
			gridBagConstraints1.anchor = GridBagConstraints.EAST;
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jLabel2 = new JLabel();
			jLabel2.setText("Dimensions:");
			jPanelTop = new JPanel();
			jPanelTop.setLayout(new GridBagLayout());
			jPanelTop.add(jLabel2, gridBagConstraints1);
			jPanelTop.add(lblImageSize, gbc_lblImageSize);
		}
		return jPanelTop;
	}

	/**
	 * This method initializes jPanelCenter	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCenter() {
		if (jPanelCenter == null) {
			jPanelCenter = new JPanel();
			jPanelCenter.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			jPanelCenter.setLayout(new BorderLayout(0, 0));
		}
		return jPanelCenter;
	}

	
}
