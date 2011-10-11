package org.sergeys.webcachedigger.ui;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.sergeys.webcachedigger.logic.CachedFile;
import org.sergeys.webcachedigger.logic.SimpleLogger;

import com.mpatric.mp3agic.EncodedText;
import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

public class AudioPreviewPanel extends AbstractFilePreviewPanel {
	
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
	
	public AudioPreviewPanel() {
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
						
		// eclipse designer
		
//		GridBagLayout gridBagLayout = new GridBagLayout();
//		gridBagLayout.columnWidths = new int[]{0, 0, 0};
//		gridBagLayout.rowHeights = new int[]{0, 0};
//		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
//		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
//		setLayout(gridBagLayout);
//		
//		JLabel lblProperty = new JLabel("property:");
//		GridBagConstraints gbc_lblProperty = new GridBagConstraints();
//		gbc_lblProperty.anchor = GridBagConstraints.NORTHEAST;
//		gbc_lblProperty.insets = new Insets(0, 0, 0, 5);
//		gbc_lblProperty.gridx = 0;
//		gbc_lblProperty.gridy = 0;
//		add(lblProperty, gbc_lblProperty);
//		
//		JLabel lblValue = new JLabel("value");
//		GridBagConstraints gbc_lblValue = new GridBagConstraints();
//		gbc_lblValue.anchor = GridBagConstraints.NORTHWEST;
//		gbc_lblValue.gridx = 1;
//		gbc_lblValue.gridy = 0;
//		add(lblValue, gbc_lblValue);
		
		// runtime
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};		
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};				
		setLayout(gridBagLayout);
	}

	private static String decode(String src){		

		ByteBuffer bb = AudioPreviewPanel.charset8859_1.encode(src);
		String res = AudioPreviewPanel.charsetFixed.decode(bb).toString();	// TODO: see EncodedText, seems trailing zero can occur
		
		return res;
	}
	
	private void addRow(String label, String value, int row){
		JLabel lblProperty = new JLabel(label);
		GridBagConstraints gbc_lblProperty = new GridBagConstraints();
		gbc_lblProperty.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblProperty.insets = new Insets(0, 0, 0, 5);
		gbc_lblProperty.gridx = 0;
		gbc_lblProperty.gridy = row;
		add(lblProperty, gbc_lblProperty);
		
		JLabel lblValue = new JLabel(value);
		GridBagConstraints gbc_lblValue = new GridBagConstraints();
		gbc_lblValue.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblValue.gridx = 1;
		gbc_lblValue.gridy = row;
		add(lblValue, gbc_lblValue);
		
	}
	
	@Override
	public void setCachedFile(CachedFile cachedFile) {
		
		super.setCachedFile(cachedFile);
	
		removeAll();
		
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		int row = 0;
		
		try {
			Mp3File mp3 = new Mp3File(cachedFile.getAbsolutePath());
			
			if(mp3.hasId3v2Tag()){
				ID3v2 id3v2 = mp3.getId3v2Tag();
				
				String str = id3v2.getAlbum();
				
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Album:", str, row);					
					row++;
				}
				
				str = id3v2.getArtist(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Artist:", str, row);					
					row++;
				}
				
				str = id3v2.getComment(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Comment:", str, row);					
					row++;
				}
				
				str = id3v2.getComposer(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Composer:", str, row);					
					row++;
				}
				
				str = id3v2.getCopyright(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Copyright:", str, row);					
					row++;
				}
				
				str = id3v2.getEncoder(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Encoder:", str, row);					
					row++;
				}
				
				str = id3v2.getGenreDescription(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Genre:", str, row);					
					row++;
				}
				
				str = id3v2.getItunesComment(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("iTunes comment:", str, row);					
					row++;
				}
				
				str = id3v2.getOriginalArtist(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Original artist:", str, row);					
					row++;
				}
				
				str = id3v2.getTitle(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Title:", str, row);					
					row++;
				}
				
				str = id3v2.getTrack(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Track:", str, row);					
					row++;
				}
				
				str = id3v2.getUrl(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Url:", str, row);					
					row++;
				}
				
				str = id3v2.getVersion(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Version:", str, row);					
					row++;
				}
				
				str = id3v2.getYear(); 
				if(str != null && !str.isEmpty()){
					str = decode(str);
					addRow("Year:", str, row);					
					row++;
				}				
			}
			else if(mp3.hasId3v1Tag()){
				ID3v1 id3v1 = mp3.getId3v1Tag();
				String str = id3v1.getAlbum(); 
				if(str != null && !str.isEmpty()){
					addRow("Album:", str, row);					
					row++;
				}
				
				str = id3v1.getArtist(); 
				if(str != null && !str.isEmpty()){
					addRow("Artist:", str, row);					
					row++;
				}
				
				str = id3v1.getComment(); 
				if(str != null && !str.isEmpty()){
					addRow("Comment:", str, row);					
					row++;
				}
				
				// ...
				
				str = id3v1.getTitle(); 
				if(str != null && !str.isEmpty()){
					addRow("Title:", str, row);					
					row++;
				}								

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
