package org.sergeys.cookbook;

import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CookBook extends Application {

	private void init(Stage stage){
		//stage.getScene().getRoot().getChildrenUnmodifiable().
	}
		
	
	@Override
	public void start(Stage primaryStage) {
		//Parent root;
		try {
			//root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));						
						
			init(primaryStage);

			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
			
			// http://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html
			
			URL location = getClass().getResource("MainScene.fxml");
			//URL location = getClass().getResource("Main2.fxml");
			//ResourceBundle resources = ResourceBundle.getBundle("com.foo.example");
			FXMLLoader fxmlLoader = new FXMLLoader(location);

			Pane root = (Pane)fxmlLoader.load();
			MainController controller = (MainController)fxmlLoader.getController();
			
			primaryStage.setTitle("CookBook");
			primaryStage.setScene(new Scene(root, 800, 500));
			
			primaryStage.getIcons().add(controller.getAppIcon());	// amor.png BPFolderRecipesGreen.png	
			
			primaryStage.show();
			
			controller.createSampleData(primaryStage);
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	    		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
