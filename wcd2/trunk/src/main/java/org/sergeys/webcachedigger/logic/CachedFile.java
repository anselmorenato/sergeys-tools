package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import org.sergeys.library.FileUtils;
import org.sergeys.webcachedigger.logic.Settings.FileType;

import eu.medsea.mimeutil.MimeUtil;

public class CachedFile extends File {

    /**
     * Property names for PropertyChangeEvent listeners
     */
    public static final String SELECTED_FILE = "SELECTED_FILE";

    private static Properties extensionByMimetype;

    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        extensionByMimetype = new Properties();
        InputStream is = null;
        try {
            is = CachedFile.class.getResourceAsStream("/resources/extensionByMime.properties");
            extensionByMimetype.load(is);
        } catch (IOException e) {
            Settings.getLogger().error("", e);
        }
        finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    Settings.getLogger().error("", e);
                }
            }
        }
    }

    private static final long serialVersionUID = 1L;

    private String hash;
    private String mimeType = null;
    private String proposedName = null;

    private boolean selectedToCopy = false;

    public CachedFile(String pathname) {
        super(pathname);
    }

    public CachedFile(File file) {
        super(file.getAbsolutePath());
    }

    public void detectHash() throws NoSuchAlgorithmException, IOException{
//		SimpleLogger.logMessage("calculating md5 for " + this.getName());
        //hash = FileUtils.md5hash(this, 1024 * 1024); // not too smooth

        switch(Settings.getInstance().getCompareFilesMethod()){
        case Fast:
            hash = FileUtils.md5hashPartial(this, 1024);
            break;
        case Full:
            hash = FileUtils.md5hash(this, 512 * 512);
        }

    }

    public String getHash() {
//		if (hash == null) {
//
//		}

        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void detectMimeType(){
        @SuppressWarnings("rawtypes")
        Collection mt = MimeUtil.getMimeTypes(this);
        if (!mt.isEmpty()) {
            mimeType = mt.toArray()[0].toString();
        }
        else{
            mimeType = "unknown";
        }
    }

    public String getMimeType() {
//		if (mimeType == null) {
//			detectFileType();
//		}

        return mimeType;
    }

    public void setMimeType(String mimeType){
        this.mimeType = mimeType;
    }

    public String guessExtension(){
        return extensionByMimetype.getProperty(getMimeType());
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

    public String getProposedName() {
        if(proposedName == null){
            return getName();
        }

        return proposedName;
    }

    public void setProposedName(String proposedName) {
        this.proposedName = proposedName;
    }

    public static void detectMimeTypes(ArrayList<CachedFile> files, IProgressWatcher watcher){
        for(CachedFile file: files){
            if(!watcher.isAllowedToContinue()){
                return;
            }
            file.detectMimeType();
            watcher.progressStep();
        }
    }

    public static void detectHashes(ArrayList<CachedFile> files, IProgressWatcher watcher)
            throws NoSuchAlgorithmException, IOException{
        for(CachedFile file: files){

            if(!watcher.isAllowedToContinue()){
                return;
            }

            file.detectHash();
            watcher.progressStep();
        }
    }

    /**
     * Filters files by mime type
     *
     * @param files
     * @param settings
     * @param skipped
     */
    public static ArrayList<CachedFile> filter(ArrayList<CachedFile> files, ArrayList<CachedFile> skipped){

        ArrayList<CachedFile> matched = new ArrayList<CachedFile>();

        for(CachedFile file: files){
            if(file.getMimeType().startsWith("audio/"))
            {
                if(Settings.getInstance().getActiveFileTypes().contains(FileType.Audio)){
                    matched.add(file);
                }
            }
            else if(file.getMimeType().startsWith("video/"))
            {
                if(Settings.getInstance().getActiveFileTypes().contains(FileType.Video)){
                    matched.add(file);
                }
            }
            else if(file.getMimeType().startsWith("image/"))
            {
                if(Settings.getInstance().getActiveFileTypes().contains(FileType.Image)){
                    matched.add(file);
                }
            }
            else if(Settings.getInstance().getActiveFileTypes().contains(FileType.Other))
            {
                matched.add(file);
            }
            else{
                if(skipped != null){
                    skipped.add(file);
                }
            }
        }

        return matched;
    }

}
