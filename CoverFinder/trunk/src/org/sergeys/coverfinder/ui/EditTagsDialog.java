package org.sergeys.coverfinder.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.sergeys.coverfinder.logic.Album;
import org.sergeys.coverfinder.logic.MusicItem;
import org.sergeys.coverfinder.logic.Settings;
import org.sergeys.coverfinder.logic.Track;

public class EditTagsDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtArtist;
	private JTextField txtTitle;
	ActionListener actionListener;

	public class EditTagsEvent
	extends ActionEvent
	{
		MusicItem musicItem;
		String artist;
		String title;
		String albumTitle;
		
		private static final long serialVersionUID = 1L;

		public EditTagsEvent(Object source, int id, String command) {
			super(source, id, command);
		}
		
		public EditTagsEvent(Object source, MusicItem musicItem, String artist, String title, String albumTitle){
			this(source, 0, null);
			
			this.musicItem = musicItem;
			this.artist = artist;
			this.title = title;
			this.albumTitle = albumTitle;
		}
	}
	
	/**
	 * Create the dialog.
	 */
	public EditTagsDialog(Window parent, ActionListener actionListener) {
		super(parent);
		
		this.actionListener = actionListener;
		
		setTitle(Messages.getString("EditTagsDialog.EditTags")); //$NON-NLS-1$
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditTagsDialog.class.getResource("/images/icon.png"))); //$NON-NLS-1$
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 488, 147);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblArtist = new JLabel(Messages.getString("EditTagsDialog.Artist")); //$NON-NLS-1$
		lblArtist.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPanel.add(lblArtist);
		
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblTitle, 0, SpringLayout.EAST, lblArtist);
		lblTitle.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPanel.add(lblTitle);
		
		txtArtist = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblArtist, 6, SpringLayout.NORTH, txtArtist);
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblArtist, -6, SpringLayout.WEST, txtArtist);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, txtArtist, 4, SpringLayout.NORTH, contentPanel);
		txtArtist.setText("Artist"); //$NON-NLS-1$
		contentPanel.add(txtArtist);
		txtArtist.setColumns(30);
		
		txtTitle = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblTitle, 6, SpringLayout.NORTH, txtTitle);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, txtTitle, 3, SpringLayout.SOUTH, txtArtist);
		sl_contentPanel.putConstraint(SpringLayout.WEST, txtTitle, 115, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, txtArtist, 0, SpringLayout.WEST, txtTitle);
		txtTitle.setText("Title"); //$NON-NLS-1$
		contentPanel.add(txtTitle);
		txtTitle.setColumns(30);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton(Messages.getString("EditTagsDialog.Save")); //$NON-NLS-1$
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSave();
					}
				});
				btnSave.setActionCommand("OK"); //$NON-NLS-1$
				buttonPane.add(btnSave);
				getRootPane().setDefaultButton(btnSave);
			}
			{
				JButton cancelButton = new JButton(Messages.getString("EditTagsDialog.Cancel")); //$NON-NLS-1$
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doCancel();
					}
				});
				cancelButton.setActionCommand("Cancel"); //$NON-NLS-1$
				buttonPane.add(cancelButton);
			}
		}
	}
	
	protected void doCancel() {
		setVisible(false);		
	}
		
	protected void doSave() {
		String message = ""; //$NON-NLS-1$
		if(this.musicItem instanceof Album){
			message = String.format(Messages.getString("EditTagsDialog.UpdateFiles"), ((Album)musicItem).getChildCount()); //$NON-NLS-1$
		}
		else if(this.musicItem instanceof Track){
			message = String.format(Messages.getString("EditTagsDialog.UpdateFile"), ((Track)musicItem).getFile().getAbsolutePath()); //$NON-NLS-1$
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
		
		if(musicItem instanceof Track){
			actionListener.actionPerformed(new EditTagsEvent(this, musicItem, txtArtist.getText(), txtTitle.getText(), null));
		}
		else{
			actionListener.actionPerformed(new EditTagsEvent(this, musicItem, txtArtist.getText(), null, txtTitle.getText()));
		}
						
		setVisible(false);		
	}
	
	MusicItem musicItem;
	private JLabel lblTitle = new JLabel("<MusicItem> title:"); //$NON-NLS-1$
	
	public void setMusicItem(MusicItem musicItem){
		setMusicItem(musicItem, musicItem.getArtist(), musicItem.getTitle());
	}
	
	public void setMusicItem(MusicItem musicItem, String newArtist, String newTitle){
		this.musicItem = musicItem;
		
		if(musicItem instanceof Album){
			lblTitle.setText(Messages.getString("EditTagsDialog.AlbumTitle")); //$NON-NLS-1$
		}
		else if(musicItem instanceof Track){
			lblTitle.setText(Messages.getString("EditTagsDialog.TrackTitle")); //$NON-NLS-1$
		}
		
		txtArtist.setText(newArtist);
		txtTitle.setText(newTitle);
	}
}
