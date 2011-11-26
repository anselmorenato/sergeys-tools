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

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblImage = new JLabel(""); //$NON-NLS-1$

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
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(ImageDetailsDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$
		setTitle(Messages.getString("ImageDetailsDialog.ImageDetails")); //$NON-NLS-1$
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
				JLabel lblTxtDimensions = new JLabel(Messages.getString("ImageDetailsDialog.Dimensions")); //$NON-NLS-1$
				lblTxtDimensions.setHorizontalAlignment(SwingConstants.RIGHT);
				panelDetails.add(lblTxtDimensions);
			}
			{
				lblDimensions = new JLabel(""); //$NON-NLS-1$
				panelDetails.add(lblDimensions);
			}
			{
				lblTxtFileSize = new JLabel(Messages.getString("ImageDetailsDialog.FileSize")); //$NON-NLS-1$
				lblTxtFileSize.setHorizontalAlignment(SwingConstants.RIGHT);
				panelDetails.add(lblTxtFileSize);
			}
			{
				lblFilesize = new JLabel(""); //$NON-NLS-1$
				panelDetails.add(lblFilesize);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton(Messages.getString("ImageDetailsDialog.SetToSelectedTracks")); //$NON-NLS-1$
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSave();
					}
				});
				okButton.setActionCommand("OK"); //$NON-NLS-1$
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(Messages.getString("ImageDetailsDialog.Cancel")); //$NON-NLS-1$
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doCancel();
					}
				});
				cancelButton.setActionCommand("Cancel"); //$NON-NLS-1$
				buttonPane.add(cancelButton);
			}
		}
		
		//lblImage.setIcon(new ImageIcon(imgResult.getImageUrl()));
		if(imgResult.getImage() != null){
			lblImage.setIcon(new ImageIcon(imgResult.getImage()));
		}
		
		lblDimensions.setText(String.format("%d x %d", imgResult.getWidth(), imgResult.getHeight())); //$NON-NLS-1$
		if(imgResult.getFileSize() > 0){
			lblFilesize.setText(String.format(Messages.getString("ImageDetailsDialog.NBytes"), imgResult.getFileSize())); //$NON-NLS-1$
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
		String message = ""; //$NON-NLS-1$
		if(this.musicItem instanceof Album){
			message = String.format(Messages.getString("ImageDetailsDialog.UpdateFilesInAlbum"), ((Album)musicItem).getChildCount()); //$NON-NLS-1$
		}
		else if(this.musicItem instanceof Track){
			message = String.format(Messages.getString("ImageDetailsDialog.UpdateFile"), ((Track)musicItem).getFile().getAbsolutePath()); //$NON-NLS-1$
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
