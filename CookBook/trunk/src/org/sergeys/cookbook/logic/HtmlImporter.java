package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;

import org.apache.xml.serialize.HTMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@SuppressWarnings("deprecation")
public class HtmlImporter {
	File originalFile;
	Document doc;	
	String destinationDir;
	
    private void removeElements(Document doc, String tag){
    	NodeList nodes = doc.getElementsByTagName(tag);
    	while(nodes.getLength() > 0){
    		org.w3c.dom.Node n = nodes.item(0); 
    		n.getParentNode().removeChild(n);
    		nodes = doc.getElementsByTagName(tag);
    	}    	
    }
	
	public void Import(final File htmlFile, String destinationDir){
		originalFile = htmlFile;
		this.destinationDir = destinationDir;
					
		Platform.runLater(new Runnable(){

			@Override
			public void run() {				
				final WebEngine engine = new WebEngine();
				
				engine.documentProperty().addListener(new ChangeListener<Document>(){
					@Override
					public void changed(
							ObservableValue<? extends Document> observable,
							Document oldValue, Document newValue) {
						
						if(newValue != null){
							System.out.println("document set");
							Document doc = engine.getDocument();
                        	setDocument(doc);
						}						
					}});
				
		        engine.getLoadWorker().stateProperty().addListener(
		                new ChangeListener<State>() {
		                    public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
		                        if (newState == State.SUCCEEDED) {      
		                        	System.out.println("worker succeeded");
		                        }
		                        else{
		                            System.out.println("document load failed: " + newState);
		                        }
		                    }							
		                });

		        //engine.load("file:///D:/workspace/CookBook/samplefiles/2.html");
		        System.out.println("loading " + htmlFile.getAbsolutePath());
		        engine.load("file:///" + htmlFile.getAbsolutePath());	// TODO verify url on linux
		        
			}});
	}
	
	private void setDocument(Document document) {
		doc = document;		
		        	
		String hash;
		
		try {
			hash = getFileHash(originalFile);
		} catch (NoSuchAlgorithmException | IOException e2) {			
			e2.printStackTrace();
			return;
		}
        	
		// remove garbage
    	removeElements(doc, "script");
    	removeElements(doc, "noscript");
    	        
//    	NodeList nodes = doc.getElementsByTagName("input");
//    	    	   
//    	while((nodes.getLength() > 0) && found){
//    		
//    		org.w3c.dom.Node n = nodes.item(0); 
//    		org.w3c.dom.Node attr = n.getAttributes().getNamedItem("type");
//    		if(attr != null && attr.getNodeValue().equals("hidden")){
//    			n.getParentNode().removeChild(n);
//    			nodes = doc.getElementsByTagName(tag);
//    		}
//    	}
    	
    	Path path = FileSystems.getDefault().getPath(destinationDir, hash);
    	try {
			Files.createDirectory(path);
		} catch (IOException e2) {			
			e2.printStackTrace();
			return;
		}
    	
    	// collect referenced files
    	NodeList nodes = doc.getElementsByTagName("img");
    	for(int i = 0; i < nodes.getLength(); i++){
    		//nodes.item(i).setTextContent("changed");
    		org.w3c.dom.Node attr = nodes.item(i).getAttributes().getNamedItem("src");
    		if(attr != null){
    			// TODO copy file and modify link
    			
    			attr.setTextContent(hash);
    		}
    	}
    	
    	// extract plaintext for db fulltext search
    	nodes = doc.getElementsByTagName("body");
    	if(nodes.getLength() < 1){
    		System.out.println("body not found");
    		return;
    	}
    	
    	String bodytext = nodes.item(0).getTextContent();
    	
    	Path p = FileSystems.getDefault().getPath(destinationDir, hash + ".txt");
//        	if(!p.toFile().exists()){
//        		System.out.println("nonexistent path " + p);
//        		return;
//        	}
    	
    	try {
			FileWriter wr = new FileWriter(p.toFile());
			wr.write(bodytext);
			wr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        // http://weblogs.java.net/blog/fabriziogiudici/archive/2012/02/12/xslt-xhtml-jdk6-jdk7-madness
        HTMLSerializer sr = new HTMLSerializer(new OutputFormat(doc));
        try {
        	p = FileSystems.getDefault().getPath(destinationDir, hash + ".html");
        	FileOutputStream fos = new FileOutputStream(p.toFile());
			sr.setOutputByteStream(fos);
			sr.serialize(doc);
			fos.close();
			System.out.println("file written");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private String getFileHash(File file) throws IOException, NoSuchAlgorithmException
	{
		// http://www.mkyong.com/java/java-sha-hashing-example/
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
        FileInputStream fis = new FileInputStream(file);
 
        byte[] dataBytes = new byte[1024];
 
        int nread = 0; 
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        }
        
        fis.close();
        byte[] mdbytes = md.digest();
 
//        //convert the byte to hex format method 1
//        StringBuffer sb = new StringBuffer();
//        for (int i = 0; i < mdbytes.length; i++) {
//          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
//        }
// 
//        System.out.println("Hex format : " + sb.toString());
// 
       //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for(int i = 0; i < mdbytes.length; i++) {
    	  hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
    	}
 
    	//System.out.println("Hex format : " + hexString.toString());
    	return hexString.toString();
	}
}
