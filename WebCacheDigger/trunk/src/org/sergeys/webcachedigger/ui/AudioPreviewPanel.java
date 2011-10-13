package org.sergeys.webcachedigger.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.Settings;
import org.sergeys.webcachedigger.logic.SimpleLogger;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class AudioPreviewPanel extends AbstractFilePreviewPanel {
	
	// for property listeners
	public static final String PROPERTY_FILE_TO_PLAY = "AudioPreviewPanel_PROPERTY_FILE_TO_PLAY"; 
	
	// http://download.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
	private static Charset charset8859_1 = null;
	private static Charset charsetFixed = null;
	
	static{
		try{
			charset8859_1 = Charset.forName("ISO-8859-1");
		}
		catch(Exception ex){
			SimpleLogger.logMessage("Cannot instantiate Charset for ISO-8859-1");			
		}
		
		try{
			charsetFixed = Charset.forName("windows-1251");
		}
		catch(Exception ex){
			SimpleLogger.logMessage("Cannot instantiate Charset for windows-1251");			
		}
	}
	
	JPanel panelProperties;
	
	
	
	public AudioPreviewPanel(Settings settings) {
		
		setSettings(settings);
						
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setLayout(new BorderLayout(0, 0));
		
		panelProperties = new JPanel();
		add(panelProperties, BorderLayout.NORTH);
		GridBagLayout gbl_panelProperties = new GridBagLayout();
		//gbl_panelProperties.columnWidths = new int[]{1, 3, 0};
		gbl_panelProperties.rowHeights = new int[]{0, 0};
		gbl_panelProperties.columnWeights = new double[]{1.0, 3.0};
		gbl_panelProperties.rowWeights = new double[]{0.0, Double.MIN_VALUE};
				
		panelProperties.setLayout(gbl_panelProperties);
		
		// eclipse designer
		
		JLabel lblKey = new JLabel("key");
		lblKey.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_lblKey = new GridBagConstraints();
		gbc_lblKey.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblKey.insets = new Insets(0, 0, 0, 5);
		gbc_lblKey.gridx = 0;
		gbc_lblKey.gridy = 0;
				
		
		panelProperties.add(lblKey, gbc_lblKey);
		
		JTextPane txtpnValue = new JTextPane();
		txtpnValue.setText("value long long value value long long value value long long value value long long value value long long value " +
				"value long long value value long long value value long long value value long long value value long long value ");
		txtpnValue.setEditable(false);
		
		txtpnValue.setFont(UIManager.getFont("Label.font"));
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
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doPlay(e);
			}
		});
		btnPlay.setEnabled(getSettings().isExternalPlayerConfigured());
		panelControl.add(btnPlay);
		
		
	}

	protected void doPlay(ActionEvent e) {		
		firePropertyChange(PROPERTY_FILE_TO_PLAY, null, getCachedFile());
	}

	private static String decode(String src){		

		ByteBuffer bb = AudioPreviewPanel.charset8859_1.encode(src);
		String res = AudioPreviewPanel.charsetFixed.decode(bb).toString();	// TODO: see EncodedText, seems trailing zero can occur
		
		return res;
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

			value = decode(value);
			
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
			txtpnValue.setFont(UIManager.getFont("Label.font"));
			
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
		
		int row = 0;
		
		try {
			Mp3File mp3 = new Mp3File(cachedFile.getAbsolutePath());
			
			if(mp3.hasId3v2Tag()){
				ID3v2 id3v2 = mp3.getId3v2Tag();
			
				row = addRow("", id3v2.getArtist(), row);
				row = addRow("", id3v2.getTitle(), row);
				
				if(row > 0){
					row = addRow(" ", " ", row);
				}
				
				row = addRow("Album:", id3v2.getAlbum(), row);
				row = addRow("Track:", id3v2.getTrack(), row);
				row = addRow("Year:", id3v2.getYear(), row);
				row = addRow("Genre:", id3v2.getGenreDescription(), row);
				row = addRow("Original Artist:", id3v2.getOriginalArtist(), row);
								
				row = addRow("Composer:", id3v2.getComposer(), row);
				row = addRow("Copyright:", id3v2.getCopyright(), row);
				row = addRow("Encoder:", id3v2.getEncoder(), row);
				row = addRow("Comment:", id3v2.getComment(), row);				
				row = addRow("iTunes comment:", id3v2.getItunesComment(), row);								
				
				row = addRow("URL:", id3v2.getUrl(), row);
				//row = addRow("Version:", id3v2.getVersion(), row);
												
			}
			else if(mp3.hasId3v1Tag()){
				ID3v1 id3v1 = mp3.getId3v1Tag();

				row = addRow("", id3v1.getArtist(), row);
				row = addRow("", id3v1.getTitle(), row);
				if(row > 0){
					row = addRow(" ", " ", row);
				}
				
				row = addRow("Album:", id3v1.getAlbum(), row);
				row = addRow("Track:", id3v1.getTrack(), row);
				row = addRow("Year:", id3v1.getYear(), row);
				row = addRow("Genre:", id3v1.getGenreDescription(), row);
				
				row = addRow("Comment:", id3v1.getComment(), row);
												
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
