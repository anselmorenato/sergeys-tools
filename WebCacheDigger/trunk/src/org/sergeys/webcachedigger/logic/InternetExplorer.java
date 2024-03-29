package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ImageIcon;

import org.sergeys.library.OsUtils;

/**
 * IE on Windows
 *
 * @author sergeys
 *
 */
public class InternetExplorer extends AbstractBrowser {

    private List<File> listFilesRecursive(File directory){
        ArrayList<File> allFiles = new ArrayList<File>();


        if(directory.isDirectory()){

            // collect regular files
            List<File> files = Arrays.asList(directory
                    .listFiles(new FileFilter() {
                        public boolean accept(File file) {
                            return (!file.isDirectory() &&
                                    !file.getName().toLowerCase().equals("index.dat") &&
                                    !file.getName().toLowerCase().equals("desktop.ini"));
                        }
                    }));

            allFiles.addAll(files);

            // process subdirs
            List<File> subdirs = Arrays.asList(directory
                    .listFiles(new FileFilter() {
                        public boolean accept(File file) {
                            return (file.isDirectory() &&
                                    !file.getName().toLowerCase().equals("antiphishing"));    // ie9 on win7
                        }
                    }));

            for(File subdir: subdirs){
                files = listFilesRecursive(subdir);
                allFiles.addAll(files);
            }

//            SimpleLogger.logMessage("collected files in " + directory);
        }

        return allFiles;
    }

    @Override
    protected List<File> collectExistingCachePaths() throws Exception {
        List<File> paths = new ArrayList<File>();

         // Try default paths.
        if(System.getenv("LOCALAPPDATA") != null){
            // vista/7
            String path = System.getenv("LOCALAPPDATA") + File.separator +
                    "Microsoft" + File.separator + "Windows" + File.separator + "Temporary Internet Files" + File.separator +
                    "Content.IE5";    // win7 ie9

            File f = new File(path);
            if(f.isDirectory()){
                paths.add(f);
                System.out.println("Actual path to search (win): " + path);
            }

        }
        else if(System.getenv("USERPROFILE") != null){
            // 2000/xp
            String path = System.getenv("USERPROFILE") + File.separator +
                    "Local Settings" + File.separator + "Temporary Internet Files" + File.separator +
                    "Content.IE5";    // winxp ie6

            File f = new File(path);
            if(f.isDirectory()){
                paths.add(f);
                System.out.println("Actual path to search (win): " + path);
            }
        }

        if(paths.size() == 0 && OsUtils.isWindows()){
            // not found at default location, get relocated actual path from registry
            // http://www.winxptutor.com/movetif.htm
            // http://stackoverflow.com/questions/62289/read-write-to-windows-registry-using-java
            // http://www.coderanch.com/t/132336/gc/Read-Windows-Registry-java

            String path = OsUtils.readWindowsRegistry("HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
                     + "Explorer\\Shell Folders", "Cache", "REG_SZ");
            System.out.println("Registry path (win): " + path);
            if(path != null){
                path = path + File.separator + "Content.IE5";
                File f = new File(path);
                if(f.isDirectory()){
                    paths.add(f);
                    System.out.println("Actual path to search (win): " + path);
                }
            }
        }

        return paths;
    }

    @Override
    public List<CachedFile> collectCachedFiles(IProgressWatcher watcher)
            throws Exception {
        ArrayList<CachedFile> files = new ArrayList<CachedFile>();

        //int minFileSize = settings.getIntProperty(Settings.MIN_FILE_SIZE_BYTES);
        long minFileSize = Settings.getInstance().getMinFileSizeBytes();

        for (File directory : this.getExistingCachePaths()) {

            List<File> dirFiles = listFilesRecursive(directory);

            for (File file : dirFiles) {

                if(!watcher.isAllowedToContinue()){
                    return files;
                }

                if (file.length() > minFileSize) {
                    files.add(new CachedFile(file.getAbsolutePath()));
                    watcher.progressStep();
                }
            }

        }

        return files;
    }

    @Override
    public String getName() {
        return "Internet Explorer";
    }

    private ImageIcon icon = null;

    @Override
    public ImageIcon getIcon() {
        if(icon == null){
            icon = new ImageIcon(InternetExplorer.class.getResource("/images/ie.png"));
        }
        return icon;
    }

    @Override
    public String getScreenName() {
        return "Internet Explorer";
    }


}
