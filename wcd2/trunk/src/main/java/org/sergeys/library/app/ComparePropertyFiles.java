package org.sergeys.library.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.sergeys.library.FileUtils;

public abstract class ComparePropertyFiles {

    public static void main(String[] args) {

        if(args.length < 2){
            System.out.println("Usage: " + ComparePropertyFiles.class.getName() +
                    " [-edit] masterfile targetfile [targetfile2 ...]");
            return;
        }

        boolean bEdit = (args[0].compareTo("-edit") == 0);

        String masterFile = (bEdit) ? args[1] : args[0];
        ArrayList<String> targetFiles = new ArrayList<String>();
        for(int i = (bEdit) ? 2 : 1; i < args.length; i++){
            targetFiles.add(args[i]);
        }

        Properties propMaster = new Properties();
        try {
            propMaster.load(new FileInputStream(masterFile));
        } catch (Exception e) {
            System.out.println(String.format("Cannot open %s: %s", masterFile, e.getMessage()));
            return;
        }

        for(String targetFile: targetFiles) {
            Properties propTarget = new Properties();
            try {
                propTarget.load(new FileInputStream(targetFile));
            } catch (Exception e) {
                System.out.println(String.format("Cannot open %s: %s", targetFile, e.getMessage()));
                return;
            }

            if(bEdit){
                // modify target
                Properties pAdd = compareProps(propMaster, propTarget);
                Properties pRemove = compareProps(propTarget, propMaster);

                // read-write as plain text file to save non-unicode text
                try {
                    BufferedReader rdr = new BufferedReader(new FileReader(targetFile));

                    File tmp = File.createTempFile(ComparePropertyFiles.class.getName(), "txt");
                    FileWriter fw = new FileWriter(tmp);
                    String line = rdr.readLine();
                    while(line != null){

//System.out.println(String.format("line: %s", line));
                        boolean match = false;
                        for(Entry<Object, Object> entry: pRemove.entrySet()) {
                            if(line.startsWith(entry.getKey().toString() + "=")){
                                match = true;
                                System.out.println(String.format("Removed %s=%s", entry.getKey(), entry.getValue()));
                                break;
                            }
                        }
                        if(!match){
                            fw.write(line + "\n");
                        }

                        line = rdr.readLine();
                    }

                    rdr.close();

                    for(Entry<Object, Object> entry: pAdd.entrySet()) {
                        fw.write(String.format("%s=%s%n", entry.getKey(), entry.getValue()));
                        System.out.println(String.format("Added %s=%s", entry.getKey(), entry.getValue()));
                    }

                    fw.close();

                    FileUtils.copyFile(tmp.getAbsolutePath(), targetFile);
                    if(!tmp.delete()){
                        System.out.println(String.format("Cannot delete temp file %s", tmp.getAbsolutePath()));
                    }

                } catch (FileNotFoundException e1) {
                    System.out.println(String.format("Cannot save %s: %s", targetFile, e1.getMessage()));
                    e1.printStackTrace();
                } catch (IOException e) {
                    System.out.println(String.format("Cannot save %s: %s", targetFile, e.getMessage()));
                    e.printStackTrace();
                }


//                for(Object key: pAdd.keySet()){
//                    propTarget.put(key, pAdd.get(key));
//                    System.out.println(String.format("Added %s=%s", key, pAdd.get(key)));
//                }
//                for(Object key: pRemove.keySet()){
//                    propTarget.remove(key);
//                    System.out.println(String.format("Removed %s=%s", key, pRemove.get(key)));
//                }
//
//                try {
//                    propTarget.store(new FileOutputStream(targetFile), "");
//                } catch (FileNotFoundException e) {
//                    System.out.println(String.format("Cannot save %s: %s", targetFile, e.getMessage()));
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    System.out.println(String.format("Cannot save %s: %s", targetFile, e.getMessage()));
//                    e.printStackTrace();
//                }
            }
            else{
                // just print differences
                System.out.println(String.format("Properties only in %s:", masterFile));
                System.out.println();
                Properties p = compareProps(propMaster, propTarget);
                for(Entry<Object, Object> entry: p.entrySet()){
                    System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
                }
                System.out.println();

                System.out.println(String.format("Properties only in %s:", targetFile));
                System.out.println();
                p = compareProps(propTarget, propMaster);
                for(Entry<Object, Object> entry: p.entrySet()){
                    System.out.println(String.format("%s=%s", entry.getKey(), entry.getValue()));
                }
            }
        }
    }

    /**
     * Returns properties that only present in p1.
     *
     * @param p1
     * @param p2
     * @return
     */
    public static Properties compareProps(Properties p1, Properties p2){

        Properties result = new Properties();

        Set<?> keys = ((Hashtable<?, ?>)p1.clone()).keySet();
        keys.removeAll(p2.keySet());

        for(Object o: keys){
            //System.out.println(String.format("%s=%s", o, p1.get(o)));
            result.put(o, p1.get(o));
        }

        return result;
    }

}
