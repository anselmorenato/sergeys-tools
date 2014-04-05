package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.sergeys.library.FileUtils;

public class Opera extends AbstractBrowser {

    @Override
    public String getName() {
        return "Opera";
    }

    @Override
    public String getScreenName() {
        return "Opera";
    }

    private ImageIcon icon;

    @Override
    public ImageIcon getIcon() {

        if(icon == null){
            icon = new ImageIcon(Opera.class.getResource("/images/opera.png"));
        }
        return icon;
    }

    @Override
    public List<CachedFile> collectCachedFiles(IProgressWatcher watcher) throws Exception {
        ArrayList<CachedFile> files = new ArrayList<CachedFile>();

        // 1. count files
        ArrayList<File> allFiles = new ArrayList<File>();

        for(File cacheDir: getExistingCachePaths()){
            FileUtils.listFilesRecursive(cacheDir, new FileFilter(){
                @Override
                public boolean accept(File pathname) {
                    return (!pathname.getName().equals("CACHEDIR.TAG") &&
                            !pathname.getName().equals("dcache4.url")
                            );
                }},
                new FileFilter(){
                    @Override
                    public boolean accept(File pathname) {
                        return (!pathname.getName().equals("revocation") &&
                                !pathname.getName().equals("sesn")
                                );
                    }},
                allFiles);
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

    @Override
    protected List<File> collectExistingCachePaths() throws Exception {
        ArrayList<File> existingPaths = new ArrayList<File>();

        String path;
        File f;

        // windows
        if(System.getenv("LOCALAPPDATA") != null){
            // 7
            path = System.getenv("LOCALAPPDATA") + File.separator +
                    "Opera" + File.separator + "Opera" + File.separator +	// yes opera twice
                    "cache";

            Settings.getLogger().debug("opera path to search (win7+): " + path);
            f = new File(path);
            if(f.isDirectory()){
                existingPaths.add(f);
            }
        }
        else if(System.getenv("USERPROFILE") != null){
            // xp
            path = System.getenv("USERPROFILE") + File.separator +
                    "Local Settings" + File.separator + "Application Data" + File.separator +
                    "Opera" + File.separator + "Opera" + File.separator +	// yes opera twice
                    "cache";

            Settings.getLogger().debug("opera path to search (xp): " + path);
            f = new File(path);
            if(f.isDirectory()){
                existingPaths.add(f);
            }
        }

        // macos
        path = System.getProperty("user.home") + File.separator +
                "Library" + File.separator + "Caches" + File.separator + "Opera" + File.separator + "cache";

        Settings.getLogger().debug("opera path to search (macosx): " + path);
        f = new File(path);
        if(f.isDirectory()){
            existingPaths.add(f);
        }

        // linux
        path = System.getProperty("user.home") + File.separator +
                ".opera" + File.separator + "cache";
        Settings.getLogger().debug("opera path to search (linux): " + path);
        f = new File(path);
        if(f.isDirectory()){
            existingPaths.add(f);
        }

        for(File f1: existingPaths){            
            Settings.getLogger().info("Actual path to search: " + f1.getAbsolutePath());            
        }
        
        return existingPaths;
    }

}
