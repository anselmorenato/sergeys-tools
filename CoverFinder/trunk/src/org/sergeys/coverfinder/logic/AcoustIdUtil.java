package org.sergeys.coverfinder.logic;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.zip.GZIPOutputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sergeys.library.ProcessStreamReader;

/**
 * http://acoustid.org webservice
 * 
 * @author sergeys
 *
 */
public class AcoustIdUtil {
	
	private final String ApiKey = "q3cdey0t";	// app: coverfinder, version: 1
	
	private static AcoustIdUtil instance;
	
	static{
		instance = new AcoustIdUtil();
	}
	
	private AcoustIdUtil(){}
	
	public static AcoustIdUtil getInstance(){
		return instance;
	}
	
	public static class Fingerprint{
		public String fingerprint;
		public String duration;
	}
	
	
	public Fingerprint getFingerprint(File file){
		//String fingerprint = "";
		Fingerprint fp = new Fingerprint();
		
		try {
			String[] args = { 
					"d:\\workspace\\coverfinder\\lib\\win32\\fpcalc", 
					file.getAbsolutePath() 
			};
			Process process = Runtime.getRuntime().exec(args);
			
			ProcessStreamReader reader = new ProcessStreamReader(process);
            reader.start();
            process.waitFor();
            reader.join();
                        
            String output = reader.getResult();
            
            BufferedReader rdr = new BufferedReader(new StringReader(output));
            String line;
            while((line = rdr.readLine()) != null){
//            	if(line.startsWith("ERROR")){
//            		throw new Exception(output);
//            	}
            	
            	String[] tokens = line.split("=");
            	if(tokens.length == 2){
            		if(tokens[0].equals("FINGERPRINT")){
            			fp.fingerprint = tokens[1];
            		}
            		else if(tokens[0].equals("DURATION")){
            			fp.duration = tokens[1];
            		}
            	}
            }
            
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return fp;
	}
	
	public String identify(Fingerprint fp){
		
		String result = "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("format=json");
		sb.append("&client="); sb.append(ApiKey);
		sb.append("&duration="); sb.append(fp.duration);
		sb.append("&fingerprint="); sb.append(fp.fingerprint);
		sb.append("&meta=recordings");
		try {
			URI uri = new URI("http", "api.acoustid.org", "/v2/lookup", sb.toString(), null);
			
			
			// gzipped http post
			// http://www.xyzws.com/Javafaq/how-to-use-httpurlconnection-post-data-to-web-server/139
			
			HttpURLConnection conn = (HttpURLConnection)uri.toURL().openConnection();
			conn.setRequestMethod("POST");
			conn.setUseCaches(false);
		    conn.setDoInput(true);
		    conn.setDoOutput(true);
			
		    ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    GZIPOutputStream gzos = new GZIPOutputStream(bos);
		    gzos.write(sb.toString().getBytes());
		    gzos.flush();
		    gzos.close();
		    
		    // send data
		    bos.writeTo(conn.getOutputStream());
		    conn.getOutputStream().flush();
		    conn.getOutputStream().close();
		    
		    // read response		    		    
			String line;
			StringBuilder builder = new StringBuilder();			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			
			conn.disconnect();
			
			System.out.println(builder.toString());
			
			sb = new StringBuilder();
			
			JSONObject json = new JSONObject(builder.toString());
			if(json.getString("status").equals("ok")){
				JSONArray results = json.getJSONArray("results");
				for(int i = 0; i < results.length(); i++){
					JSONObject res = results.getJSONObject(i);
					if(res.has("recordings")){
						sb.append(
								res.getJSONArray("recordings").getJSONObject(0).getString("title")
						);
						sb.append(" - ");
						sb.append(
								res.getJSONArray("recordings").getJSONObject(0).getJSONArray("artists").getJSONObject(0).getString("name")
						);
						
						sb.append(" (");
						sb.append(res.getDouble("score"));
						sb.append("); ");
						sb.append("\r\n");
					}
				}
				
				result = sb.toString();
			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
}
