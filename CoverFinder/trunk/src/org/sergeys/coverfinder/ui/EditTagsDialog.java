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
		
		setTitle("Edit tags");
		setIconImage(Toolkit.getDefaultToolkit().getImage(EditTagsDialog.class.getResource("/images/icon.png")));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setBounds(100, 100, 488, 147);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		
		JLabel lblArtist = new JLabel("Artist:");
		lblArtist.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPanel.add(lblArtist);
		
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblTitle, 0, SpringLayout.EAST, lblArtist);
		lblTitle.setHorizontalAlignment(SwingConstants.TRAILING);
		contentPanel.add(lblTitle);
		
		txtArtist = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblArtist, 6, SpringLayout.NORTH, txtArtist);
		sl_contentPanel.putConstraint(SpringLayout.EAST, lblArtist, -6, SpringLayout.WEST, txtArtist);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, txtArtist, 4, SpringLayout.NORTH, contentPanel);
		txtArtist.setText("Artist");
		contentPanel.add(txtArtist);
		txtArtist.setColumns(30);
		
		txtTitle = new JTextField();
		sl_contentPanel.putConstraint(SpringLayout.NORTH, lblTitle, 6, SpringLayout.NORTH, txtTitle);
		sl_contentPanel.putConstraint(SpringLayout.NORTH, txtTitle, 3, SpringLayout.SOUTH, txtArtist);
		sl_contentPanel.putConstraint(SpringLayout.WEST, txtTitle, 115, SpringLayout.WEST, contentPanel);
		sl_contentPanel.putConstraint(SpringLayout.WEST, txtArtist, 0, SpringLayout.WEST, txtTitle);
		txtTitle.setText("Title");
		contentPanel.add(txtTitle);
		txtTitle.setColumns(30);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnSave = new JButton("Save");
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						doSave();
					}
				});
				btnSave.setActionCommand("OK");
				buttonPane.add(btnSave);
				getRootPane().setDefaultButton(btnSave);
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
	}
	
	protected void doCancel() {
		setVisible(false);		
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
		
		if(musicItem instanceof Track){
			actionListener.actionPerformed(new EditTagsEvent(this, musicItem, txtArtist.getText(), txtTitle.getText(), null));
		}
		else{
			actionListener.actionPerformed(new EditTagsEvent(this, musicItem, txtArtist.getText(), null, txtTitle.getText()));
		}
						
		setVisible(false);		
	}
	
	MusicItem musicItem;
	private JLabel lblTitle = new JLabel("<MusicItem> title:");
	
	public void setMusicItem(MusicItem musicItem){
		setMusicItem(musicItem, musicItem.getArtist(), musicItem.getTitle());
	}
	
	public void setMusicItem(MusicItem musicItem, String newArtist, String newTitle){
		this.musicItem = musicItem;
		
		if(musicItem instanceof Album){
			lblTitle.setText("Album title:");
		}
		else if(musicItem instanceof Track){
			lblTitle.setText("Track title:");
		}
		
		txtArtist.setText(newArtist);
		txtTitle.setText(newTitle);
	}
}
