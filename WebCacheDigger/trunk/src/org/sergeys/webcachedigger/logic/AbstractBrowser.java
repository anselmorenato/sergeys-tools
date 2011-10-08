package org.sergeys.webcachedigger.logic;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBrowser implements IBrowser {

	protected Settings settings;
	protected List<String> cachePaths;
	
	/*
	public AbstractBrowser() {		
	}
	*/
	
	public void setSettings(Settings settings) {
		this.settings = settings;
	}
	
	protected abstract List<String> collectCachePaths() throws Exception;
	
	protected List<String> getCachePaths() throws Exception{
		if(cachePaths == null){
			cachePaths = collectCachePaths();
		}
		
		return cachePaths;
	}	
/*	
	protected ArrayList<IBrowserProgressListener> listeners = new ArrayList<IBrowserProgressListener>();
	
	
	public void addBrowserProgressListener(IBrowserProgressListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	protected void notifyListenersOnFileFound(FileFoundEvent event){
		for(IBrowserProgressListener listener : listeners){
			listener.fileFound(event);
		}
	}
	
	protected void notifyListenersOnFileCount(int count){
		for(IBrowserProgressListener listener : listeners){
			listener.fileSearchCount(count);
		}
	}
	
	protected void notifyListenersOnSearchComplete(){
		for(IBrowserProgressListener listener : listeners){
			listener.fileSearchComplete();
		}
	}
*/	
	@Override	
	public void run() {		
		try {
			collectCachedFiles();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
