package org.sergeys.fmws;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemind.main.FreeMindStarter;

public class FreemindWebstart {

    private static Logger log;

    static{
        log = LoggerFactory.getLogger(FreemindWebstart.class);
    }

    public static void main(String[] args) {

        log.info("app started");

        // TODO print svn revision/version

        try{
            // unpack resources if needed
            String fmHome = System.getProperty("user.home") + File.separator + ".freemind-webstart-home";
            log.info("home dir " + fmHome);
            if(!new File(fmHome + File.separator + "patterns.xml").exists()){

                log.info("extracting files");

                InputStream is = FreemindWebstart.class.getResourceAsStream("/freemind-home.jar");
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
            //System.setProperty("freemind.base.dir", "d:/junkfreemind");
            System.setProperty("freemind.base.dir", fmHome);

            for(String arg: args){
                // TODO: webstart passes 2 args like '-open fullfilename'
                log.debug("Argument passed: " + arg);
            }

            FreeMindStarter.main(args);
        }
        catch(Exception ex){
            log.error("Failed to launch freemind", ex);
        }
    }

}
