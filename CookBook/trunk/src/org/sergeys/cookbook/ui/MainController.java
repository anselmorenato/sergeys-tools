package org.sergeys.cookbook.ui;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.TimelineBuilder;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.sergeys.cookbook.logic.Database;
import org.sergeys.cookbook.logic.HtmlImporter;
import org.sergeys.cookbook.logic.HtmlImporter.Status;
import org.sergeys.cookbook.logic.RecipeLibrary;
import org.sergeys.cookbook.logic.Settings;
import org.sergeys.cookbook.logic.Tag;

public class MainController {

    @FXML private TreeView<String> tree;
    @FXML private WebView webview;
    @FXML private AnchorPane pane;
    @FXML private StackPane modalDimmer;
    @FXML private SplitPane splitter;
    
    private Stage stage;
    private FileChooser fc;
    private Image appIcon;
    private Stage dialogStage;
        
    public void initialize(){
    	// called by convention
    	// http://docs.oracle.com/javafx/2/api/javafx/fxml/doc-files/introduction_to_fxml.html
        System.out.println("init");
        
        // TODO call in background
        RecipeLibrary.getInstance().validate();
        
        TreeItem<String> treeRoot = new TreeItem<String>("All recipes");
        tree.setShowRoot(true);
        tree.setRoot(treeRoot);
        treeRoot.setExpanded(true);
        
        buildTree();
        
        // http://docs.oracle.com/javafx/2/ui_controls/tree-view.htm#BABDEADA
//        tree.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {			
//			@Override
//			public TreeCell<String> call(TreeView<String> arg0) {
//				
//				return new TextFieldTreeCellImpl();
//			}
//		});
        
        double pos = Settings.getInstance().getWinDividerPosition();
        System.out.println("set " + pos);
        
//        splitter.setDividerPosition(0, pos);        
        System.out.println("actual " + splitter.getDividerPositions()[0]);
        
//        splitter.layout();
        
        splitter.getDividers().get(0).positionProperty().addListener(new ChangeListener<Number>(){

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				// TODO Auto-generated method stub
				System.out.println("changed to " + newValue);				
			}});
        
        splitter.getDividers().get(0).positionProperty().addListener(new InvalidationListener(){

			@Override
			public void invalidated(Observable arg0) {
				// TODO Auto-generated method stub
				System.out.println("invalidated");
			}});
                
        
        pane.visibleProperty().addListener(new ChangeListener<Boolean>(){

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0,
					Boolean arg1, Boolean arg2) {
				// TODO Auto-generated method stub
				if(arg2){
					System.out.println("visible");
					double pos = Settings.getInstance().getWinDividerPosition();
			        System.out.println("set " + pos);			        
//			        splitter.setDividerPosition(0, pos);        
				}
				
			}});
    }

    public void myInit(Stage stage){
        this.stage = stage;
    }
    
    public void applySettings(){
//    	double pos = Settings.getInstance().getWinDividerPosition();
//        splitter.setDividerPositions(pos, 1.0 - pos);
    }
    
    public void collectSettings(){
    	double pos[] = splitter.getDividerPositions();
    	System.out.println("collect " + pos[0]);
    	Settings.getInstance().setWinDividerPosition(pos[0]);
    }
    
    public Image getAppIcon(){
        if(appIcon == null){
            appIcon = new Image(getClass().getResourceAsStream("/images/amor.png"));	// amor.png BPFolderRecipesGreen.png
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
        
        File prev = new File(Settings.getInstance().getLastFilechooserLocation());
        if(prev.exists()){
        	fc.setInitialDirectory(prev);
        }

        // http://java-buddy.blogspot.com/2012/03/javafx-20-disable-ower-window-for.html

        File file = fc.showOpenDialog(stage);

        if (file != null) {
        	Settings.getInstance().setLastFilechooserLocation(file.getParent());
            String path = file.getAbsolutePath();
            System.out.println(path);
            try{
                //webview.getEngine().load("file:///" + path);
            	webview.getEngine().load(file.toURI().toString());
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    public void onMenuImport(ActionEvent e){
    	doImport();
//    	
//    	double pos = Settings.getInstance().getWinDividerPosition();
//        System.out.println("set " + pos);        
//        splitter.setDividerPosition(0, pos);
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
            //showModalMessage(dialogStage.sceneProperty().get().getRoot());
        }
    }


    /**
     * Show the given node as a floating dialog over the whole application, with
     * the rest of the application dimmed out and blocked from mouse events.
     *
     * @param message
     */
    public void showModalMessage(Node message) {
        modalDimmer.getChildren().add(message);
        modalDimmer.setOpacity(0);
        modalDimmer.setVisible(true);
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
            new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(),1, Interpolator.EASE_BOTH)
        )).build().play();
    }

    /**
     * Hide any modal message that is shown
     */
    public void hideModalMessage() {
        modalDimmer.setCache(true);
        TimelineBuilder.create().keyFrames(
            new KeyFrame(Duration.seconds(1),
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent t) {
                        modalDimmer.setCache(false);
                        modalDimmer.setVisible(false);
                        modalDimmer.getChildren().clear();
                    }
                },
                new KeyValue(modalDimmer.opacityProperty(),0, Interpolator.EASE_BOTH)
        )).build().play();
    }

    private void buildTree(){
    	
    	tree.getRoot().getChildren().clear();
    	
    	try {
			ArrayList<Tag> tags = Database.getInstance().getRootTags();
			for(Tag t: tags){
				tree.getRoot().getChildren().add(new TreeItem<String>(t.getVal()));
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void doImport(){
        if(fc == null){
            fc = new FileChooser();            
        }
        
        File prev = new File(Settings.getInstance().getLastFilechooserLocation());
        if(prev.exists()){
        	fc.setInitialDirectory(prev);
        }

        File file = fc.showOpenDialog(stage);
        if(file != null){
        	Settings.getInstance().setLastFilechooserLocation(file.getParent());
        	final HtmlImporter imp = new HtmlImporter();
        	imp.Import(file, new ChangeListener<HtmlImporter.Status>() {

				@Override
				public void changed(
						ObservableValue<? extends Status> observable,
						Status oldValue, Status newValue) {
					// TODO Auto-generated method stub
					
					if(newValue == Status.Complete){
						System.out.println("completed import of " + imp.getHash());
						
						RecipeLibrary.getInstance().validate();
						
						buildTree();
					}
					else{
						System.out.println("importer status " + newValue);
					}
				}
			});
        }    	
    }
    
    
//    private class TextFieldTreeCellImpl
//    extends TreeCell<String>
//    {
//
//		@Override
//		public void cancelEdit() {
//			// TODO Auto-generated method stub
//			super.cancelEdit();
//		}
//
//		@Override
//		public void commitEdit(String newValue) {
//			// TODO Auto-generated method stub
//			super.commitEdit(newValue);
//		}
//
//		@Override
//		public void startEdit() {
//			// TODO Auto-generated method stub
//			super.startEdit();
//		}
//    	
//    }

// ===============
    
    public void createSampleData(){

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

        // TODO open last file
//        WebEngine webEngine = webview.getEngine();

        try{
            //webEngine.load("file:///D:/workspace/CookBook/samplefiles/2.html");
            //webEngine.load("http://java.oracle.com");
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        double pos = Settings.getInstance().getWinDividerPosition();
        System.out.println("createsample set " + pos);			        
//        splitter.setDividerPosition(0, pos);        
        
//        test();
    }

}
