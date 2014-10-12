package org.sergeys.sqws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SquirrelWebstart {

    private static Logger log;

    static{
        log = LoggerFactory.getLogger(SquirrelWebstart.class);
    }

    public static void main(String[] args) {

        log.info("app started");

        try{
            // unpack resources if needed
            String fmHome = System.getProperty("user.home") + File.separator + ".squirrelsql-webstart-home";
            log.info("home dir " + fmHome);
            if(!new File(fmHome + File.separator + "update-log4j.properties").exists()){

                log.info("extracting files");

                InputStream is = SquirrelWebstart.class.getResourceAsStream("/squirrel-home.jar");
                JarInputStream jis = new JarInputStream(is);
                JarEntry je;

                byte[] buffer = new byte[10240];

                while((je = jis.getNextJarEntry()) != null){

                    if(je.isDirectory()){
                        continue;
                    }

                    String fileName = je.getName();

                    File newFile = new File(fmHome + File.separator + fileName);
                    new File(newFile.getParent()).mkdirs();

                    log.info(newFile.getAbsolutePath());

                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while ((len = jis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }

                jis.close();
            }

            // set home dir
            // launch main class

            //System.setProperty("freemind.base.dir", fmHome);
            //FreeMindStarter.main(args);

            net.sourceforge.squirrel_sql.client.Main.main(args);
        }
        catch(Exception ex){
            log.error("Failed to launch SQuirrel SQL", ex);
        }
    }

}
