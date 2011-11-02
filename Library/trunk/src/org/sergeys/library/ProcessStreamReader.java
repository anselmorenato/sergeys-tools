package org.sergeys.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;


/**
 * Helper to read Process output stream
 * based on http://www.rgagnon.com/javadetails/java-0480.html
 * 
		StreamReader reader = new StreamReader(process.getInputStream());
        reader.start();
        process.waitFor();
        reader.join();
        String output = reader.getResult();
            
 * @author sergeys
 *
 */
public class ProcessStreamReader 
extends Thread 
{
    private InputStream is;
    private StringWriter sw = new StringWriter();
    
    public ProcessStreamReader(Process process) {
    	this.is = process.getInputStream();
    }

    public ProcessStreamReader(InputStream is) {        
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
