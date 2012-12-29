package org.sergeys.cookbook.ui;

import java.text.DateFormat;

import org.sergeys.cookbook.logic.Settings;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AboutController extends DialogController
{
	@FXML Label lblProduct;
	@FXML Label lblVersion;
	@FXML Label lblAuthor;
	@FXML Label lblJava;
	
	public void initialize(){
		super.initialize();
		
		lblProduct.setText("CookBook - the Recipe Book");
		String version = DateFormat.getDateInstance().format(Settings.getInstance().getCurrentVersion()) +
				String.format(" (rev. %s)", Settings.getInstance().getResources().getProperty("svn.revision"));
		lblVersion.setText(version);
		lblAuthor.setText("Sergey Selivanov");
		lblJava.setText(System.getProperties().getProperty("java.runtime.name") + " " + 
				System.getProperties().getProperty("java.runtime.version"));
		
	}
	
}
