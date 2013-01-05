package org.sergeys.gpublish.logic;

import java.io.File;
import java.io.FileFilter;

public abstract class FileFilters {

    public static FileFilter OnlyFiles = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.isFile();
        }
    };

    public static FileFilter OnlyDirs = new FileFilter() {
        @Override
        public boolean accept(File file) {
            return file.isDirectory();
        }
    };

}
