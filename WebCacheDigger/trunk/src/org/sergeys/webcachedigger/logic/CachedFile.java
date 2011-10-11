package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Collection;
import java.util.Properties;

import eu.medsea.mimeutil.MimeUtil;

public class CachedFile extends File {

	/**
	 * Property names for PropertyChangeEvent listeners
	 */
	public static final String SELECTED_FILE = "SELECTED_FILE";
	
	//private static Hashtable<String, String> extensionByMimetype;
	private static Properties extensionByMimetype;

	static {
		MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
		extensionByMimetype = new Properties();
		try {			
			InputStream is = CachedFile.class.getResourceAsStream("/resources/extensionByMime.properties");
			extensionByMimetype.load(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static final long serialVersionUID = 1L;

	private String hash;
	private String mimeType = null;
	
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

	public void detectFileType(){
		@SuppressWarnings("rawtypes")
		Collection mt = MimeUtil.getMimeTypes(this);
		if (!mt.isEmpty()) {
			mimeType = mt.toArray()[0].toString();
		}
		else{
			mimeType = "unknown";
		}
	}
	
	public String getFileType() {
		if (mimeType == null) {
			detectFileType();
		}

		return mimeType;
	}

	// http://www.rgagnon.com/javadetails/java-0064.html
	// TODO: note limit of 64MB
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
		//return extensionByMimetype.get(getFileType());
		return extensionByMimetype.getProperty(getFileType());
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
	
}
