package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.sergeys.coverfinder.logic.Album;
import org.sergeys.coverfinder.logic.ImageSearchResult;
import org.sergeys.coverfinder.logic.MusicItem;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.coverfinder.logic.Track;

public class ImageDetailsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblImage = new JLabel("");

	public class EditImageEvent
	extends ActionEvent
	{
		private static final long serialVersionUID = 1L;

		ImageSearchResult imgResult;
		MusicItem musicItem;
		
		public EditImageEvent(Object source, int id, String command) {
			super(source, id, command);
		}
		
		public EditImageEvent(Object source, ImageSearchResult imgResult, MusicItem musicItem){
			this(source, 0, null);
			
			this.imgResult = imgResult;
			this.musicItem = musicItem;
		}
	}
	
	ImageSearchResult imgResult;
	MusicItem musicItem;
	HashSet<ActionListener> actionListeners = new HashSet<ActionListener>();
	private JLabel lblDimensions;
	private JLabel lblFilesize;
	private JLabel lblTxtFileSize;
	
	/**
	 * Create the dialog.
	 */
	public ImageDetailsDialog(Window parent, ImageSearchResult imgResult, MusicItem musicItem) {
		super(parent);
		
		this.imgResult = imgResult;
		this.musicItem = musicItem;
		//this.actionListener = actionListener;
		
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
				JLabel lblTxtDimensions = new JLabel("Dimensions:");
				lblTxtDimensions.setHorizontalAlignment(SwingConstants.RIGHT);
				panelDetails.add(lblTxtDimensions);
			}
			{
				lblDimensions = new JLabel("");
				panelDetails.add(lblDimensions);
			}
			{
				lblTxtFileSize = new JLabel("File size:");
				lblTxtFileSize.setHorizontalAlignment(SwingConstants.RIGHT);
				panelDetails.add(lblTxtFileSize);
			}
			{
				lblFilesize = new JLabel("");
				panelDetails.add(lblFilesize);
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
		
		//lblImage.setIcon(new ImageIcon(imgResult.getImageUrl()));
		if(imgResult.getImage() != null){
			lblImage.setIcon(new ImageIcon(imgResult.getImage()));
		}
		
		lblDimensions.setText(String.format("%d x %d", imgResult.getWidth(), imgResult.getHeight()));
		if(imgResult.getFileSize() > 0){
			lblFilesize.setText(String.format("%d bytes", imgResult.getFileSize()));
		}
		else{
			lblTxtFileSize.setVisible(false);
			lblFilesize.setVisible(false);
		}
		
		//contentPanel.revalidate();		
		pack();
	}

	public void addActionListener(ActionListener actionListener){
		actionListeners.add(actionListener);
	}
	
	protected void doSave() {
		String message = "";
		if(this.musicItem instanceof Album){
			message = String.format("Update %d file(s) in selected album?", ((Album)musicItem).getChildCount());
		}
		else if(this.musicItem instanceof Track){
			message = String.format("Update file %s ?", ((Track)musicItem).getFile().getAbsolutePath());
		}		
		
		if(Settings.getInstance().isConfirmFileEdit()){
			int answer = JOptionPane.showConfirmDialog(this, message);
			
			if(answer == JOptionPane.CANCEL_OPTION){
				return;
			}
			else if(answer == JOptionPane.NO_OPTION){
				setVisible(false);
				return;
			} 
		}

		//actionListener.actionPerformed(new EditImageEvent(this, imgResult, musicItem));
		for(ActionListener listener: actionListeners){
			listener.actionPerformed(new EditImageEvent(this, imgResult, musicItem));
		}
		
		setVisible(false);		
	}

	protected void doCancel() {
		setVisible(false);
	}

}
