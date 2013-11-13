package org.sergeys.cookbook.ui;

import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class DialogController {

    protected Stage stage;

    public void initialize(){
        //System.out.println("DialogController init");
    }

    public void onClose(ActionEvent e){    	
        if(stage != null){
            stage.close();
        }
    }

	public void setStage(Stage stage) {
		this.stage = stage;		
	}
}
