package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.sergeys.library.FileUtils;

/**
 * Google Chrome
 *
 * @author sergeys
 *
 */
public class Chrome extends AbstractBrowser {

    private final String SEP = File.separator;

    @Override
    public String getName() {
        return "Google Chrome or Chromium";
    }

    @Override
    protected List<File> collectExistingCachePaths() throws Exception {
        ArrayList<File> existingPaths = new ArrayList<File>();

        String path;
        File f;

        // windows
        if(System.getenv("LOCALAPPDATA") != null){
            // 7
            path = System.getenv("LOCALAPPDATA") + SEP +
                    "Google" + SEP + "Chrome" + SEP + "User Data" + SEP +
                    "Default" + SEP + "Cache" + SEP;

            Settings.getLogger().debug("chrome path to search (win7+): " + path);

            f = new File(path);
            if(f.isDirectory()){
                existingPaths.add(f);
            }
        }
        else if(System.getenv("USERPROFILE") != null){
            // xp
            path = System.getenv("USERPROFILE") + SEP +
                    "Local Settings" + SEP + "Application Data" + SEP +
                    "Google" + SEP + "Chrome" + SEP + "User Data" + SEP +
                    "Default" + SEP + "Cache" + SEP;

            Settings.getLogger().debug("chrome path to search (xp): " + path);

            f = new File(path);
            if(f.isDirectory()){
                existingPaths.add(f);
            }
        }

        // macos
        path = System.getProperty("user.home") + SEP +
                "Library" + SEP + "Caches" + SEP + "Google" + SEP +
                "Chrome" + SEP + "Default" + SEP + "Cache";

        Settings.getLogger().debug("chrome path to search (macosx): " + path);

        f = new File(path);
        if(f.isDirectory()){
            existingPaths.add(f);
        }

        // linux
        path = System.getProperty("user.home") + SEP +
                ".cache" + SEP + "google-chrome" + SEP + "Default" + SEP + "Cache";

        Settings.getLogger().debug("chrome path to search (linux): " + path);

        f = new File(path);
        if(f.isDirectory()){
            existingPaths.add(f);
            Settings.getLogger().info("Actual path to search: " + path);
        }

        path = System.getProperty("user.home") + SEP +
                ".cache" + SEP + "chromium" + SEP + "Default" + SEP + "Cache";
        Settings.getLogger().debug("chromium path to search (linux): " + path);
        f = new File(path);
        if(f.isDirectory()){
            existingPaths.add(f);
            Settings.getLogger().info("Actual path to search: " + path);
        }

        return existingPaths;
    }

    @Override
    public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception {
        ArrayList<CachedFile> files = new ArrayList<CachedFile>();

        // 1. count files
        ArrayList<File> allFiles = new ArrayList<File>();

        for(File cacheDir: getExistingCachePaths()){

            if(!watcher.isAllowedToContinue()){
                return files;
            }

            FileUtils.listFilesRecursive(cacheDir, new FileFilter(){

                @Override
                public boolean accept(File pathname) {
                    return (!pathname.getName().equals("index") && !pathname.getName().startsWith("data_"));
                }}, null, allFiles);
        }

        // 2. filter
        //int minFileSize = settings.getIntProperty(Settings.MIN_FILE_SIZE_BYTES);
        long minFileSize = Settings.getInstance().getMinFileSizeBytes();
        for(File file: allFiles){

            if(!watcher.isAllowedToContinue()){
                return files;
            }

            if(file.length() > minFileSize){
                files.add(new CachedFile(file.getAbsolutePath()));
                watcher.progressStep();
            }
        }

        return files;
    }

    private ImageIcon icon = null;

    @Override
    public ImageIcon getIcon() {
        if(icon == null){
            //icon = new ImageIcon(this.getClass().getResource("/images/chrome.png"));
            icon = new ImageIcon(Chrome.class.getResource("/images/chrome.png"));
        }
        return icon;
    }

    @Override
    public String getScreenName() {
        return "Chrome";
    }

}
