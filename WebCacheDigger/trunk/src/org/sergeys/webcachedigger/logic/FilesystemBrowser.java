package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class FilesystemBrowser
extends AbstractBrowser
{

    @Override
    public String getName() {
        return "filesystem";
    }

    @Override
    public String getScreenName() {
        return "Directories";
    }

    @Override
    public ImageIcon getIcon() {
        return null;
    }

    @Override
    public List<CachedFile> collectCachedFiles(IProgressWatcher watcher)
            throws Exception {

        List<File> files = new ArrayList<File>();
        List<CachedFile> cachedfiles = new ArrayList<CachedFile>();

//        FileUtils.listFilesRecursive(new File("c:\\tmp\\sub dir"), null, null, files);
        for(File f: files){
            cachedfiles.add(new CachedFile(f));
        }

        return cachedfiles;
    }

    @Override
    protected List<File> collectExistingCachePaths() throws Exception {
        ArrayList<File> dirs = new ArrayList<File>();

//        dirs.add(new File("c:\\tmp\\sub dir"));

        return dirs;
    }

}
