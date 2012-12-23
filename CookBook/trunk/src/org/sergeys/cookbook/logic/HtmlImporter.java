package org.sergeys.cookbook.logic;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.web.WebEngine;

import org.apache.xml.serialize.HTMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@SuppressWarnings("deprecation")
public class HtmlImporter {
    File originalFile;
    Document doc;
    String destinationDir;
//    SimpleBooleanProperty importComplete = new SimpleBooleanProperty();
//    SimpleStringProperty completedFile = new SimpleStringProperty();
    String hash;

    public enum Status { Unknown, Complete };

    SimpleObjectProperty<Status> status = new SimpleObjectProperty<Status>();

    private void removeElements(Document doc, String tag){
        NodeList nodes = doc.getElementsByTagName(tag);
        while(nodes.getLength() > 0){
            org.w3c.dom.Node n = nodes.item(0);
            n.getParentNode().removeChild(n);
            nodes = doc.getElementsByTagName(tag);
        }
    }

    public void Import(final File htmlFile, ChangeListener<Status> listener){
        originalFile = htmlFile;
        destinationDir = Settings.getSettingsDirPath() + File.separator + Settings.RECIPES_SUBDIR;
        File dir = new File(destinationDir);
        if(!dir.exists()){
            dir.mkdirs();
        }

        //importComplete.set(false);
        //importComplete.addListener(listener);
        //completedFile.addListener(listener);
        status.addListener(listener);

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
                            try {
                                setDocument(doc);
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }});

//                engine.getLoadWorker().stateProperty().addListener(
//                        new ChangeListener<State>() {
//                            public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
//                                if (newState == State.SUCCEEDED) {
//                                    System.out.println("worker succeeded");
//                                }
//                                else{
//                                    System.out.println("document load failed: " + newState);
//                                }
//                            }
//                        });

                //engine.load("file:///D:/workspace/CookBook/samplefiles/2.html");
                System.out.println("loading " + htmlFile.getAbsolutePath());

                //engine.load("file:///" + htmlFile.getAbsolutePath());	// TODO verify url on linux
                System.out.println("uri " + htmlFile.toURI().toString());
                engine.load(htmlFile.toURI().toString());
            }});
    }

    /**
     * assume to work in temp dir
     *
     * @param document
     * @param tag
     * @param attribute
     * @param relativeSubdir
     * @param absTargetDir
     */
    private void fixReferences(Document document, String tag, String attribute, String relativeSubdir, String absTargetDir){
        // copy referenced files and fix references

        // collect referenced files
        NodeList nodes = document.getElementsByTagName(tag);
        for(int i = 0; i < nodes.getLength(); i++){
            org.w3c.dom.Node attr = nodes.item(i).getAttributes().getNamedItem(attribute);
            if(attr != null){
                // copy file and modify link
                if(attr.getNodeValue().startsWith("http:") ||
                    attr.getNodeValue().startsWith("https:") ||
                    attr.getNodeValue().startsWith("//") ||
                    attr.getNodeValue().isEmpty()){
                    // TODO: fetch remote files
                    System.out.println(tag + ": skip url '" + attr.getNodeValue() + "'");
                    continue;
                }

                String srcName;
                try {
                    srcName = URLDecoder.decode(attr.getNodeValue(), "UTF-8");
                } catch (UnsupportedEncodingException | DOMException e1) {
                    e1.printStackTrace();
                    continue;
                }

                // assume saved files keep other files in their relative subdir
                Path src = FileSystems.getDefault().getPath(
                        originalFile.getParentFile().getAbsolutePath(), srcName);
                Path dest = FileSystems.getDefault().getPath(absTargetDir, relativeSubdir, src.toFile().getName());
                if(src.toFile().exists()){
                    try {
                        if(!dest.getParent().toFile().exists()){
                            dest.getParent().toFile().mkdirs();
                            //dest.getParent().toFile().deleteOnExit();
                        }

                        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                        attr.setTextContent(relativeSubdir + "/" + src.getFileName());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        System.out.println("failed to copy on '" + attr.getNodeValue() + "'");
                        e.printStackTrace();
                    }
                    //attr.setTextContent(hash);
                }
                else{
                    System.out.println("nonexistent path " + src);
                }
            }
        }
    }

    private void addJarEntry(String baseDir, File source, JarOutputStream target) throws IOException
    {
      BufferedInputStream in = null;
      try
      {
        if (source.isDirectory())
        {
          String name = source.getPath().replace("\\", "/");
          name = name.substring(baseDir.length());
          if (!name.isEmpty())
          {
            if (!name.endsWith("/")){
              name += "/";
            }
            JarEntry entry = new JarEntry(name);
            entry.setTime(source.lastModified());
            target.putNextEntry(entry);
            target.closeEntry();
          }
          for (File nestedFile: source.listFiles()){
            addJarEntry(baseDir, nestedFile, target);
          }
          return;
        }

        String name = source.getPath().replace("\\", "/");
        name = name.substring(baseDir.length());
        JarEntry entry = new JarEntry(name);
        entry.setTime(source.lastModified());
        target.putNextEntry(entry);
        in = new BufferedInputStream(new FileInputStream(source));

        byte[] buffer = new byte[1024];
        while (true)
        {
          int count = in.read(buffer);
          if (count == -1)
            break;
          target.write(buffer, 0, count);
        }
        target.closeEntry();
      }
      finally
      {
        if (in != null)
          in.close();
      }
    }

    private void packJar(String dir, String subdir){
        // http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
        Manifest manifest = new Manifest();
        manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
        JarOutputStream target;
        try {
            String jarname = FileSystems.getDefault().getPath(dir, subdir + ".jar").toString();
            target = new JarOutputStream(new FileOutputStream(jarname), manifest);

            Path path = FileSystems.getDefault().getPath(dir, subdir + ".html");
            addJarEntry(dir, path.toFile(), target);

            path = FileSystems.getDefault().getPath(dir, subdir + ".txt");
            addJarEntry(dir, path.toFile(), target);

            DirectoryStream<Path> subfiles = Files.newDirectoryStream(FileSystems.getDefault().getPath(dir, subdir));
            for(Path entry: subfiles) {
                addJarEntry(dir, entry.toFile(), target);
            }
            subfiles.close();
            target.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDocument(Document document) throws IOException {

        // TODO: do this in background

        doc = document;

        //String hash;

        try {
            hash = getFileHash(originalFile);
        } catch (NoSuchAlgorithmException | IOException e2) {
            e2.printStackTrace();
            return;
        }

        try {
            if(Database.getInstance().isRecipeExists(hash)){
                System.out.println("already exist in database");
                return;
            }
        } catch (SQLException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
            return;
        }

        // remove garbage
        removeElements(doc, "script");
        removeElements(doc, "noscript");

        Path tempDir;

        tempDir = Files.createTempDirectory("cookbook");
        tempDir.toFile().deleteOnExit();

        fixReferences(doc, "img", "src", hash, tempDir.toString());
        fixReferences(doc, "link", "href", hash, tempDir.toString());

        // extract plaintext for db fulltext search
        NodeList nodes = doc.getElementsByTagName("body");
        if(nodes.getLength() < 1){
            System.out.println("body not found");
            return;
        }

        String bodytext = nodes.item(0).getTextContent();
        Path p = FileSystems.getDefault().getPath(tempDir.toString(), hash + ".txt");
        try {
            FileWriter wr = new FileWriter(p.toFile());
            wr.write(bodytext);
            wr.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        // TODO broken encoding for 1251 file
        // http://weblogs.java.net/blog/fabriziogiudici/archive/2012/02/12/xslt-xhtml-jdk6-jdk7-madness
        HTMLSerializer sr = new HTMLSerializer(new OutputFormat(doc));
        try {
            p = FileSystems.getDefault().getPath(tempDir.toString(), hash + ".html");
            FileOutputStream fos = new FileOutputStream(p.toFile());
            sr.setOutputByteStream(fos);
            sr.serialize(doc);
            fos.close();
            System.out.println("file written");
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // pack all to a single file
        // http://stackoverflow.com/questions/1281229/how-to-use-jaroutputstream-to-create-a-jar-file
        packJar(tempDir.toString(), hash);

        // TODO: put to database
        File jarfile = new File(tempDir.toString() + File.separator + hash + ".jar");
        try {
            Database.getInstance().addRecipe(hash, jarfile, "recipe");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // delete dir recursively
        //Files.deleteIfExists(tempDir);
        Util.deleteRecursively(tempDir.toFile());

//        importComplete.set(true);
        //completedFile.set(p.toFile().getAbsolutePath());
        status.set(Status.Complete);
    }

    public String getHash(){
    	return hash;
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

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
//        	String s = Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1);

            //sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            sb.append(String.format("%02x", mdbytes[i]));
        }

        System.out.println("Hex format : " + sb.toString());
        return sb.toString();

//       //convert the byte to hex format method 2
//        StringBuffer hexString = new StringBuffer();
//        for(int i = 0; i < mdbytes.length; i++) {
//          hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
//        }
//
//        System.out.println("Hex format : " + hexString.toString());
//        return hexString.toString();
    }
}
