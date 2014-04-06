package org.sergeys.library;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * Operating System Utilities
 *
 * @author sergeys
 *
 */
public abstract class OsUtils {

    public static boolean isWindows(){
        return System.getenv("OS") != null && System.getenv("OS").equals("Windows_NT");
    }

    public static boolean isMacOSX(){
        //return System.getenv("OSTYPE") != null && System.getenv("OSTYPE").startsWith("darwin");
        return new File("/Applications/System preferences.app").exists();
    }

    /**
     * Works on windows 7, xp
     *
     * based on http://www.rgagnon.com/javadetails/java-0480.html
     *
     * @param location
     * @param key
     * @param expectedType in form like "REG_SZ", as reg.exe returns
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public static final String readWindowsRegistry(String location, String key, String expectedType) throws IOException, InterruptedException {
        try {
            // Run reg query, then read output with StreamReader (internal class)
            Process process;

            process = Runtime.getRuntime().exec("reg query " +
                        '"'+ location + "\" /v " + key);

            StreamReader reader = new StreamReader(process.getInputStream());
            reader.start();
            process.waitFor();
            reader.join();
            String output = reader.getResult();

            // Output has the following format:
            // \n<Version information>\n\n<key>\t<registry type>\t<value>
//            if(!output.contains("\t")){
//                    return null;
//            }

            // note no tabs on win7

            // Parse out the value
            String[] parsed = output.split(expectedType);
            return parsed[parsed.length - 1].trim();
        }
//        catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        finally{}

//        return null;
    }

    static class StreamReader extends Thread {
        private InputStream is;
        private StringWriter sw = new StringWriter();

        public StreamReader(InputStream is) {
            this.is = is;
        }

        public void run() {
            try {
                int c;
                while ((c = is.read()) != -1)
                    sw.write(c);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        public String getResult() {
            return sw.toString();
        }
    }
}
