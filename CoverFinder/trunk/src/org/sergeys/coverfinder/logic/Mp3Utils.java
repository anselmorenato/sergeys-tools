package org.sergeys.coverfinder.logic;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.Set;

import javax.imageio.ImageIO;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.images.Artwork;
import org.jaudiotagger.tag.images.ArtworkFactory;
import org.sergeys.library.FileUtils;

public class Mp3Utils {
	
	// http://download.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
	private Charset charset8859_1 = null;
	private Charset charsetFixed = null;
	private boolean decodeStrings = false;
	
	private Properties charsetByLang;
	
	private static Mp3Utils instance;
	
	// singletion
	private Mp3Utils(){
		try{
			charset8859_1 = Charset.forName("ISO-8859-1");
			charsetFixed = charset8859_1; 
			
			InputStream is = getClass().getResourceAsStream("/resources/charsetByLanguage.properties");
			charsetByLang = new Properties();
			try {
				charsetByLang.load(is);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}				

		}
		catch(Exception ex){
			SimpleLogger.logMessage("Cannot instantiate Charset for ISO-8859-1");			 
		}
		
	}
	
	public static Mp3Utils getInstance(){
		return instance;
	}
	
	static{		
		instance = new Mp3Utils();				
	}

	/**
	 * See http://download.oracle.com/javase/6/docs/technotes/guides/intl/encoding.doc.html
	 * for encoding names
	 * 
	 * @param encoding
	 */
	protected void setDecodeCharset(String encoding){		
		//charsetFixed = Charset.forName("windows-1251"); 			
		charsetFixed = Charset.forName(encoding);
	}
	
	/**
	 * 
	 * @param lang Language key as defined in charsetByLanguage.properties (two-letter name for Locale class) 
	 */
	public void setDecodeLanguage(String lang){
		if(lang == null || lang.isEmpty()){
			setDecodeStrings(false);
			return;
		}
		if(charsetByLang.containsKey(lang)){
			setDecodeCharset(charsetByLang.getProperty(lang));			
		}
	}

	public Set<Object> getDecodingLanguages(){
		return charsetByLang.keySet();
	}
	
	/**
	 * Converts string from 8859-1 to another encoding (see SetDecodeLanguage).
	 * This allows, for example, to correctly display cyrillic chars stored in audio tags
	 * which are in fact read as 8859-1. 
	 * 
	 * @param src
	 * @return
	 */
	public String decode(String src){		
		if(decodeStrings && src != null && !src.isEmpty()){
			ByteBuffer bb = charset8859_1.encode(src);
			String res = charsetFixed.decode(bb).toString();	// TODO: see EncodedText, seems trailing zero can occur
			return res;
		}
		else{
			return src;
		}			
	}

	/**
	 * Converts string from other encoding to 8859-1.
	 * 
	 * @param src
	 * @return
	 */
	public String encode(String src){
		if(decodeStrings && src != null && !src.isEmpty()){
			ByteBuffer bb = charsetFixed.encode(src);
			String res = charset8859_1.decode(bb).toString();
			return res;
		}
		else{
			return src;
		}		
	}
	
	public Image getArtwork(File file){
//		Mp3File mp3;
//		try {
//			mp3 = new Mp3File(file.getAbsolutePath());
//			if(mp3.hasId3v2Tag()){
//				byte[] bytes = mp3.getId3v2Tag().getAlbumImage();
//				
//				return ImageIO.read(new ByteArrayInputStream(bytes));			 
//			}
//		} catch (UnsupportedTagException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidDataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			
//		return null;
		
		AudioFile af;
		try {
			af = AudioFileIO.read(file);
			Tag tag = af.getTag();
			Artwork art = tag.getFirstArtwork();
			if(art != null){
				byte[] bytes = art.getBinaryData();
				return ImageIO.read(new ByteArrayInputStream(bytes));
			}			
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String proposeName(File file){
		String newName = "";
		
//		try {
//			Mp3File mp3 = new Mp3File(file.getAbsolutePath());
//			if(mp3.hasId3v2Tag()){
//				ID3v2 id3v2 = mp3.getId3v2Tag();
//				
//				if(id3v2.getTitle() != null && !id3v2.getTitle().trim().isEmpty()){
//					newName = id3v2.getTitle().trim(); 					
//				}
//				
//				if(id3v2.getArtist() != null && !id3v2.getArtist().trim().isEmpty() && !newName.isEmpty()){
//					newName = id3v2.getArtist().trim() + " - " + newName; 
//				}
//																
////				if(id3v2.getTrack() != null && !id3v2.getTrack().trim().isEmpty() && !newName.isEmpty()){
////					newName = id3v2.getTrack().trim() + " - " + newName;
////				}
//				
//				if(newName.isEmpty()){
//					newName = file.getName();
//				}
//				
//				if(newName.contains(" ")){
//					newName = newName.replace("\"", "");
//				}
//				
//				newName = newName.replace(File.separatorChar, '.');
//				newName = newName.replace('/', '.');	// on windows still path separator
//				newName = decode(newName);				
//			}
//			else if(mp3.hasId3v1Tag()){
//				ID3v1 id3v1 = mp3.getId3v1Tag();
//				
////				if(id3v1.getTitle() != null){
////					SimpleLogger.logMessage("id3v1 title: " + id3v1.getTitle());
////				}
//				
//				if(id3v1.getTitle() != null && !id3v1.getTitle().trim().isEmpty()){
//					newName = id3v1.getTitle().trim(); 					
//				}
//				
//				if(id3v1.getArtist() != null && !id3v1.getArtist().trim().isEmpty() && !newName.isEmpty()){
//					newName = id3v1.getArtist().trim() + " - " + newName; 
//				}
//																
////				if(id3v1.getTrack() != null && !id3v1.getTrack().trim().isEmpty() && !newName.isEmpty()){
////					newName = id3v1.getTrack().trim() + " - " + newName;
////				}
//				
//				if(newName.isEmpty()){
//					newName = file.getName();
//				}
//
//				if(newName.contains(" ")){
//					newName = newName.replace("\"", "");
//				}
//				
//				newName = newName.replace(File.separatorChar, '.');
//				newName = newName.replace('/', '.');
//				newName = decode(newName);
//			}
//			else{
//				newName = file.getName();
//			}
//		} catch (UnsupportedTagException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidDataException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try{
			AudioFile af = AudioFileIO.read(file);
			Tag tag = af.getTag();
			
			if(tag != null){
				String str = tag.getFirst(FieldKey.TITLE).trim(); 
				if(!str.isEmpty()){
					newName = str;
				}
				
				str = tag.getFirst(FieldKey.ARTIST).trim();
				if(!str.isEmpty() && !newName.isEmpty()){
					newName = str + " - " + newName;
				}
				
				if(newName.isEmpty()){
					newName = file.getName();
				}
				
				if(newName.contains(" ")){
					newName = newName.replace("\"", "");
				}
			
				newName = newName.replace(File.separatorChar, '.');
				newName = newName.replace('/', '.');	// on windows still path separator
				newName = decode(newName);				
			}
			else{
				newName = file.getName();
			}
		}
		catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return newName;
	}

	public boolean isDecodeStrings() {
		return decodeStrings;
	}

	public void setDecodeStrings(boolean decodeStrings) {
		this.decodeStrings = decodeStrings;
	}
	
	public void updateTags(Track track, String artist, String title, String albumTitle){
		try {
			
			if(Settings.getInstance().isBackupFileOnSave()){			
				// backup original file
				FileUtils.backupCopy(track.getFile(), Settings.MP3_BACKUP_SUFFIX);
			}
			
			AudioFile af = AudioFileIO.read(track.getFile());
			Tag tag = af.getTagOrCreateAndSetDefault();			
			
			setDecodeLanguage(Settings.getInstance().getAudioTagsLanguage());
			String str;
			
			if(artist != null && !artist.isEmpty()){
				str = encode(artist.trim());
				tag.setField(FieldKey.ARTIST, str);
			}
			if(title != null && !title.isEmpty()){
				str = encode(title.trim());
				tag.setField(FieldKey.TITLE, str);
			}
			if(albumTitle != null && !albumTitle.isEmpty()){
				str = encode(albumTitle.trim());
				tag.setField(FieldKey.ALBUM, str);
			}
			
			//af.commit();
			//af = null;	// fixes jaudiotagger warning when updating album for several tracks?
			AudioFileIO.write(af);
System.out.println("updated " + track.getFile());			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void updateArtwork(Track track, File imageFile){
								
		try {
			if(Settings.getInstance().isBackupFileOnSave()){
				// backup original file
				FileUtils.backupCopy(track.getFile(), Settings.MP3_BACKUP_SUFFIX);
			}
			
			AudioFile af = AudioFileIO.read(track.getFile());
			Tag tag = af.getTagOrCreateAndSetDefault();
			
			Artwork art = ArtworkFactory.createArtworkFromFile(imageFile);
			tag.setField(art);
			
			AudioFileIO.write(af);
			System.out.println("updated artwork in " + track.getFile());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ReadOnlyFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CannotWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
