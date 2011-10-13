package org.sergeys.webcachedigger.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.sergeys.library.swing.ScaledImage;
import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Messages;
import org.sergeys.webcachedigger.logic.Mp3Utils;
import org.sergeys.webcachedigger.logic.Settings;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class AudioPreviewPanel extends AbstractFilePreviewPanel {
	
	// for property listeners
	public static final String PROPERTY_FILE_TO_PLAY = "AudioPreviewPanel_PROPERTY_FILE_TO_PLAY";  //$NON-NLS-1$
		
	JPanel panelProperties;
	JPanel panelArtwork;
		
	
	public AudioPreviewPanel(Settings settings) {
		
		setSettings(settings);
						
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		panelProperties = new JPanel();
		add(panelProperties, BorderLayout.NORTH);
		
//		JScrollPane scrollPane = new JScrollPane(panelProperties);
//		add(scrollPane, BorderLayout.CENTER);

		
		GridBagLayout gbl_panelProperties = new GridBagLayout();
		//gbl_panelProperties.columnWidths = new int[]{1, 3, 0};
		gbl_panelProperties.rowHeights = new int[]{0, 0};
		gbl_panelProperties.columnWeights = new double[]{1.0, 3.0};
		gbl_panelProperties.rowWeights = new double[]{0.0, Double.MIN_VALUE};
				
		panelProperties.setLayout(gbl_panelProperties);
		
		// eclipse designer
		
		JLabel lblKey = new JLabel("key"); //$NON-NLS-1$
		lblKey.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_lblKey = new GridBagConstraints();
		gbc_lblKey.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblKey.insets = new Insets(0, 0, 0, 5);
		gbc_lblKey.gridx = 0;
		gbc_lblKey.gridy = 0;
				
		
		panelProperties.add(lblKey, gbc_lblKey);
		
		JTextPane txtpnValue = new JTextPane();
		txtpnValue.setText("value long long value value long long value value long long value value long long value value long long value " + //$NON-NLS-1$
				"value long long value value long long value value long long value value long long value value long long value "); //$NON-NLS-1$
		txtpnValue.setEditable(false);
		
		txtpnValue.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$
		// bug here http://stackoverflow.com/questions/613603/java-nimbus-laf-with-transparent-text-fields
		txtpnValue.setOpaque(false);
		txtpnValue.setBorder(BorderFactory.createEmptyBorder());
		txtpnValue.setBackground(new Color(0,0,0,0));
		
		GridBagConstraints gbc_txtpnValue = new GridBagConstraints();
		gbc_txtpnValue.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtpnValue.anchor = GridBagConstraints.NORTH;
		gbc_txtpnValue.gridx = 1;
		gbc_txtpnValue.gridy = 0;
		
		//gbc_txtpnValue.gridwidth = GridBagConstraints.REMAINDER;
		
		
		panelProperties.add(txtpnValue, gbc_txtpnValue);
		
		JPanel panelControl = new JPanel();
		add(panelControl, BorderLayout.SOUTH);
		
		JButton btnPlay = new JButton(Messages.getString("AudioPreviewPanel.Play")); //$NON-NLS-1$
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPlay(e);
			}
		});
		btnPlay.setEnabled(getSettings().isExternalPlayerConfigured());
		panelControl.add(btnPlay);
		
		panelArtwork = new JPanel();
		add(panelArtwork, BorderLayout.CENTER);
		panelArtwork.setLayout(new BorderLayout(0, 0));
		
		
		
	}

	protected void doPlay(ActionEvent e) {		
		firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
	}

	
	/**
	 * 
	 * @param label
	 * @param value
	 * @param row
	 * @return next row number
	 */
	private int addRow(String label, String value, int row){
		
		if(value != null && !value.isEmpty()){
		//if(value != null){

			value = Mp3Utils.decode(value);
			
			JLabel lblKey = new JLabel(label);
			lblKey.setHorizontalAlignment(SwingConstants.TRAILING);
			GridBagConstraints gbc_lblKey = new GridBagConstraints();
			gbc_lblKey.anchor = GridBagConstraints.NORTHEAST;			
			gbc_lblKey.insets = new Insets(0, 0, 0, 5);
			gbc_lblKey.gridx = 0;
			gbc_lblKey.gridy = row;
			
			panelProperties.add(lblKey, gbc_lblKey);
			
			JTextPane txtpnValue = new JTextPane();
			txtpnValue.setText(value);
			txtpnValue.setEditable(false);			
			txtpnValue.setFont(UIManager.getFont("Label.font")); //$NON-NLS-1$
			
			// bug here http://stackoverflow.com/questions/613603/java-nimbus-laf-with-transparent-text-fields
			txtpnValue.setOpaque(false);
			txtpnValue.setBorder(BorderFactory.createEmptyBorder());
			txtpnValue.setBackground(new Color(0,0,0,0));
							
			GridBagConstraints gbc_txtpnValue = new GridBagConstraints();
			gbc_txtpnValue.anchor = GridBagConstraints.NORTH;
			gbc_txtpnValue.fill = GridBagConstraints.HORIZONTAL;
			gbc_txtpnValue.gridx = 1;
			gbc_txtpnValue.gridy = row;
			
			panelProperties.add(txtpnValue, gbc_txtpnValue);
			
			return ++row;
		}
		else{
			return row;
		}
				
	}
	
	
	
	@Override
	public void setCachedFile(CachedFile cachedFile) {
		
		super.setCachedFile(cachedFile);
	
		panelProperties.removeAll();
		panelArtwork.removeAll();
		
		int row = 0;
		
		try {
			Mp3File mp3 = new Mp3File(cachedFile.getAbsolutePath());
			
			if(mp3.hasId3v2Tag()){
				ID3v2 id3v2 = mp3.getId3v2Tag();
			
				row = addRow("", id3v2.getArtist(), row); //$NON-NLS-1$
				row = addRow("", id3v2.getTitle(), row); //$NON-NLS-1$
				
				if(row > 0){
					row = addRow(" ", " ", row); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				row = addRow(Messages.getString("AudioPreviewPanel.album"), id3v2.getAlbum(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.track"), id3v2.getTrack(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.year"), id3v2.getYear(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.genre"), id3v2.getGenreDescription(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.originalArtist"), id3v2.getOriginalArtist(), row); //$NON-NLS-1$
								
				row = addRow(Messages.getString("AudioPreviewPanel.composer"), id3v2.getComposer(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.copyright"), id3v2.getCopyright(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.encoder"), id3v2.getEncoder(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.comment"), id3v2.getComment(), row);				 //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.iTunesComment"), id3v2.getItunesComment(), row);								 //$NON-NLS-1$
				
				row = addRow(Messages.getString("AudioPreviewPanel.url"), id3v2.getUrl(), row); //$NON-NLS-1$
				//row = addRow("Version:", id3v2.getVersion(), row);
				
				byte[] bytes = id3v2.getAlbumImage();
				
				if(bytes != null){
					Image img = ImageIO.read(new ByteArrayInputStream(bytes));
					if(img != null){
						ScaledImage artwork = new ScaledImage(img, false);
						panelArtwork.add(artwork);
					}
				}
							 
												
			}
			else if(mp3.hasId3v1Tag()){
				ID3v1 id3v1 = mp3.getId3v1Tag();				
				
				row = addRow("", id3v1.getArtist(), row); //$NON-NLS-1$
				row = addRow("", id3v1.getTitle(), row); //$NON-NLS-1$
				if(row > 0){
					row = addRow(" ", " ", row); //$NON-NLS-1$ //$NON-NLS-2$
				}
				
				row = addRow(Messages.getString("AudioPreviewPanel.album"), id3v1.getAlbum(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.track"), id3v1.getTrack(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.year"), id3v1.getYear(), row); //$NON-NLS-1$
				row = addRow(Messages.getString("AudioPreviewPanel.genre"), id3v1.getGenreDescription(), row); //$NON-NLS-1$
				
				row = addRow(Messages.getString("AudioPreviewPanel.comment"), id3v1.getComment(), row); //$NON-NLS-1$
												
				//row = addRow(":", id3v1.getVersion(), row);								
			}						
			
		} catch (UnsupportedTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
