package org.sergeys.mweather.ui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MetarWeather 
extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		URL location = getClass().getResource("MetarWeather.fxml");
		FXMLLoader loader = new FXMLLoader(location);
		Pane root = (Pane)loader.load();
		stage.setTitle("METAR Weather");
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	public static void main(String[] args) {
        launch(args);
    }
}