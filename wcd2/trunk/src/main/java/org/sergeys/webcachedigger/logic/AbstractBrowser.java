package org.sergeys.webcachedigger.logic;

import java.io.File;
import java.util.List;

public abstract class AbstractBrowser implements IBrowser {

    //protected Settings settings;
    protected List<File> cachePaths;

//	public void setSettings(Settings settings) {
//		this.settings = settings;
//	}

    public boolean isPresent() {
        boolean present = false;

        try {
            getExistingCachePaths();
            present = (cachePaths != null) ? (getExistingCachePaths().size() > 0) : false;
        } catch (Exception e) {
            Settings.getLogger().error("", e);
        }

        return present;
    }

    protected abstract List<File> collectExistingCachePaths() throws Exception;

    protected List<File> getExistingCachePaths() throws Exception{
        if(cachePaths == null){
            cachePaths = collectExistingCachePaths();
        }

        return cachePaths;
    }
}
