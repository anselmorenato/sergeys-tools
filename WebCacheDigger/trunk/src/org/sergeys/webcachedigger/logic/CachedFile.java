package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Hashtable;

import eu.medsea.mimeutil.MimeUtil;

public class CachedFile extends File {

	/**
	 * Property names for PropertyChangeEvent listeners
	 */
	public static final String SELECTED_FILE = "SELECTED_FILE";
	
	private static Hashtable<String, String> extensionByMimetype;

	static {
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");

		// TODO: load from resources, or from webstart
		extensionByMimetype = new Hashtable<String, String>();
		// TODO: any internal audio, video playback?
		extensionByMimetype.put("video/x-flv", 	"flv");
		extensionByMimetype.put("video/mp4", 	"mp4");
		
		extensionByMimetype.put("audio/mpeg", 	"mp3");	// TODO: need more careful detection?
		
		// TODO: internal preview
		extensionByMimetype.put("image/gif", 	"gif");
		extensionByMimetype.put("image/jpeg", 	"jpg");
		extensionByMimetype.put("image/png", 	"png");
		
		//extensionByMimetype.put("text/html", 	"html");
		extensionByMimetype.put("application/pdf", 	"pdf");
	}

	private static final long serialVersionUID = 1L;

	private String hash;
	private String fileType = null;
	
	private boolean selectedToCopy = false;

	public CachedFile(String pathname) {
		super(pathname);
	}

	public String getHash() {
		if (hash == null) {
			hash = "";
		}

		return hash;
	}

	@SuppressWarnings("unchecked")
	public String getFileType() {
		if (fileType == null) {
			// FileDataSource fds = new FileDataSource(this);
			// fileType = fds.getContentType();
			Collection mt = MimeUtil.getMimeTypes(this);
			if (!mt.isEmpty()) {
				fileType = mt.toArray()[0].toString();
			}
		}

		return fileType;
	}

	// http://www.rgagnon.com/javadetails/java-0064.html
	// note limit of 64MB
	public static void copyFile(File in, File out) throws IOException {
		copyFile(in.getAbsolutePath(), out.getAbsolutePath());
	}

	public static void copyFile(String in, String out) throws IOException {
		FileChannel inChannel = new FileInputStream(in).getChannel();
		FileChannel outChannel = new FileOutputStream(out).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} catch (IOException e) {
			throw e;
		} finally {
			if (inChannel != null){
				inChannel.close();
			}
			if (outChannel != null){
				outChannel.close();
			}
		}
	}
	
	public String guessExtension(){
		return extensionByMimetype.get(getFileType());
	}
	
	/**
	 * @param selectedToCopy the selectedToCopy to set
	 */
	public void setSelectedToCopy(boolean selectedToCopy) {
		this.selectedToCopy = selectedToCopy;
	}

	/**
	 * @return the selectedToCopy
	 */
	public boolean isSelectedToCopy() {
		return selectedToCopy;
	}

	public static String junkMessage(){		
		return "test version 1.";
	}
}
