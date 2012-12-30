package org.sergeys.cookbook.logic;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public abstract class Util {

    public static void deleteRecursively(File file){
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f: files){
                deleteRecursively(f);
            }
        }

//		System.out.println("deleting " + file);
        if(!file.delete()){
            System.out.println("not deleted " + file);
            file.deleteOnExit();
        }
    }

    public static void unpackJar(File jar, String targetDir){
        try {
            JarInputStream jis = new JarInputStream(new FileInputStream(jar));
            JarEntry je;
            while((je = jis.getNextJarEntry()) != null){	// manifest not included
//				System.out.println("entry " + je.getName());

                File f = new File(targetDir + File.separator + je.getName().substring(1)); // name starts with slash
                f.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(f);

                byte[] buf = new byte[20480];
                int count;

                while((count = jis.read(buf)) > 0){
//					System.out.println("read bytes " + count);
                    fos.write(buf, 0, count);
                }

                fos.close();
            }

            jis.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
