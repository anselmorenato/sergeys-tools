package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.sergeys.coverfinder.logic.ImageSearchResult;

public class ImageDetailsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblImage = new JLabel("");

	/**
	 * Create the dialog.
	 */
	public ImageDetailsDialog(Window parent, ImageSearchResult imgResult) {
		super(parent);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImageDetailsDialog.class.getResource("/images/icon.png")));
		setTitle("Image Details");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			lblImage.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblImage, BorderLayout.CENTER);
		}
		{
			JPanel panelDetails = new JPanel();
			contentPanel.add(panelDetails, BorderLayout.SOUTH);
			panelDetails.setLayout(new GridLayout(2, 2, 10, 0));
			{
				JLabel lblDim = new JLabel("Dim");
				lblDim.setHorizontalAlignment(SwingConstants.RIGHT);
				panelDetails.add(lblDim);
			}
			{
				JLabel lblXxy = new JLabel("XxY");
				panelDetails.add(lblXxy);
			}
			{
				JLabel lblDimensions = new JLabel("Dimensions:");
				lblDimensions.setHorizontalAlignment(SwingConstants.RIGHT);
				panelDetails.add(lblDimensions);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Set to selected tracks");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSave();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doCancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		lblImage.setIcon(new ImageIcon(imgResult.getImageUrl()));
		
		//contentPanel.revalidate();
		
		pack();
	}

	protected void doSave() {
		setVisible(false);		
	}

	protected void doCancel() {
		setVisible(false);
	}

}
