package org.sergeys.privoxytool.ui;

import org.sergeys.privoxytool.logic.Settings;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;

public class MainController {

	public Image getAppIcon() {
		// TODO Auto-generated method stub
		return null;
	}

	public void applySettings() {
		// TODO Auto-generated method stub
		
	}

    public void onMenuExit(ActionEvent e){
    	doExit();
    }
    
    private void doExit(){
    	Settings.getLogger().info("application exit");        
        Platform.exit();
    }

}
