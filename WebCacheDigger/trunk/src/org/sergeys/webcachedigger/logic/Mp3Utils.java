package org.sergeys.webcachedigger.logic;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class Mp3Utils {
	
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

	public static String decode(String src){		

		ByteBuffer bb = Mp3Utils.charset8859_1.encode(src);
		String res = Mp3Utils.charsetFixed.decode(bb).toString();	// TODO: see EncodedText, seems trailing zero can occur
		
		return res;
	}

	public static Image getArtwork(File file){
		Mp3File mp3;
		try {
			mp3 = new Mp3File(file.getAbsolutePath());
			if(mp3.hasId3v2Tag()){
				byte[] bytes = mp3.getId3v2Tag().getAlbumImage();
				
				return ImageIO.read(new ByteArrayInputStream(bytes));			 
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
			
		return null;
	}
	
	public static String proposeName(File file){
		String newName = "";
		
		try {
			Mp3File mp3 = new Mp3File(file.getAbsolutePath());
			if(mp3.hasId3v2Tag()){
				ID3v2 id3v2 = mp3.getId3v2Tag();
				
				if(id3v2.getTitle() != null && !id3v2.getTitle().trim().isEmpty()){
					newName = id3v2.getTitle().trim(); 					
				}
				
				if(id3v2.getArtist() != null && !id3v2.getArtist().trim().isEmpty() && !newName.isEmpty()){
					newName = id3v2.getArtist().trim() + " - " + newName; 
				}
																
//				if(id3v2.getTrack() != null && !id3v2.getTrack().trim().isEmpty() && !newName.isEmpty()){
//					newName = id3v2.getTrack().trim() + " - " + newName;
//				}
				
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
			else if(mp3.hasId3v1Tag()){
				ID3v1 id3v1 = mp3.getId3v2Tag();
				
				if(id3v1.getTitle() != null && !id3v1.getTitle().trim().isEmpty()){
					newName = id3v1.getTitle().trim(); 					
				}
				
				if(id3v1.getArtist() != null && !id3v1.getArtist().trim().isEmpty() && !newName.isEmpty()){
					newName = id3v1.getArtist().trim() + " - " + newName; 
				}
																
//				if(id3v1.getTrack() != null && !id3v1.getTrack().trim().isEmpty() && !newName.isEmpty()){
//					newName = id3v1.getTrack().trim() + " - " + newName;
//				}
				
				if(newName.isEmpty()){
					newName = file.getName();
				}

				if(newName.contains(" ")){
					newName = newName.replace("\"", "");
				}
				
				newName = newName.replace(File.separatorChar, '.');
				newName = newName.replace('/', '.');
				newName = decode(newName);
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
		
		
		
		return newName;
	}
}
