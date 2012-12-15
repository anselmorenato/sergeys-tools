package org.sergeys.cookbook;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainController {

    @FXML private TreeView<String> tree;
    @FXML private WebView webview;
    @FXML private AnchorPane pane;

    private Stage stage;
    private FileChooser fc;
    private Image appIcon;
    private Stage dialogStage;

    public void initialize(){
        System.out.println("init");
    }

    public Image getAppIcon(){
        if(appIcon == null){
            appIcon = new Image(getClass().getResourceAsStream("/images/amor.png"));
        }

        return appIcon;
    }

    public void onMenuCloseAction(ActionEvent e){
        System.out.println("exit");
        Platform.exit();
    }

    public void onMenuOpenAction(ActionEvent e){

        if(fc == null){
            fc = new FileChooser();
        }

        // http://java-buddy.blogspot.com/2012/03/javafx-20-disable-ower-window-for.html

        File file = fc.showOpenDialog(stage);

        if (file != null) {
            String path = file.getAbsolutePath();
            System.out.println(path);
            try{
                webview.getEngine().load("file:///" + path);
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }


    public void createSampleData(Stage stage){

        this.stage = stage;

        TreeItem<String> treeRoot = new TreeItem<String>("Root node");

        treeRoot.getChildren().addAll(Arrays.asList(
                new TreeItem<String>("Child Node 1"),
                new TreeItem<String>("Child Node 2"),
                new TreeItem<String>("Child Node 3")));


        treeRoot.getChildren().get(2).getChildren().addAll(Arrays.asList(
                new TreeItem<String>("Child Node 4"),
                new TreeItem<String>("Child Node 5"),
                new TreeItem<String>("Child Node 6"),
                new TreeItem<String>("Child Node 7"),
                new TreeItem<String>("Child Node 8"),
                new TreeItem<String>("Child Node 9"),
                new TreeItem<String>("Child Node 10"),
                new TreeItem<String>("Child Node 11"),
                new TreeItem<String>("Child Node 12")));


        tree.setShowRoot(true);
        tree.setRoot(treeRoot);
        treeRoot.setExpanded(true);

        WebEngine webEngine = webview.getEngine();
        try{
            webEngine.load("file:///D:/workspace/CookBook/samplefiles/2.html");
            //webEngine.load("http://java.oracle.com");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void onMenuHelpAbout(ActionEvent e){
        // http://stackoverflow.com/questions/8309981/how-to-create-and-show-common-dialog-error-warning-confirmation-in-javafx-2
        // http://java-buddy.blogspot.com/2012/02/dialog-with-close-button.html
        if(dialogStage == null){
            try{
                System.out.println("creating dialog");

                URL location = getClass().getResource("About.fxml");
                FXMLLoader loader = new FXMLLoader(location);

                Pane root = (Pane)loader.load();

                dialogStage = new Stage();
                dialogStage.setTitle("CookBook");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.setScene(new Scene(root, 500, 300));
                dialogStage.initOwner(stage);
                dialogStage.getIcons().add(getAppIcon());
                
                // http://stackoverflow.com/questions/13246211/javafx-how-to-get-stage-from-controller-during-initialization
                DialogController controller = loader.getController();
                controller.setStage(dialogStage);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
        if(dialogStage != null){
            dialogStage.show();
            //dialogStage.showAndWait();
        }
    }

}
