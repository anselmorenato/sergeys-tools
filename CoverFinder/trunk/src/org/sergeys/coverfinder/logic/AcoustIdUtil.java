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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sergeys.library.ProcessStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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
	
	
	public Fingerprint getFingerprint(File file) throws Exception{

		Fingerprint fp = new Fingerprint();
		
		try {
			String[] args = { 
					"d:\\workspace\\coverfinder\\lib\\win32\\fpcalc", 
					file.getAbsolutePath() 
			};
			Process process = Runtime.getRuntime().exec(args);
			
			ProcessStreamReader outrdr = new ProcessStreamReader(process.getInputStream());
			ProcessStreamReader errrdr = new ProcessStreamReader(process.getErrorStream());
            outrdr.start();
            errrdr.start();
            process.waitFor();
            outrdr.join();
            errrdr.join();
            
            String line;
            
            String errors = errrdr.getResult();
            BufferedReader rdr = new BufferedReader(new StringReader(errors));
            while((line = rdr.readLine()) != null){
            	if(line.startsWith("ERROR")){
            		throw new Exception(errors);
            	}
            }
            
            String output = outrdr.getResult();            
            rdr = new BufferedReader(new StringReader(output));
            
            while((line = rdr.readLine()) != null){            	
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
		}
		
		return fp;
	}
	
	public String identify(Fingerprint fp){
		
		String result = "";
		
		// http://acoustid.org/webservice
		
		StringBuilder sb = new StringBuilder();
		//sb.append("format=json");
		sb.append("format=xml");
		sb.append("&client="); sb.append(ApiKey);
		sb.append("&duration="); sb.append(fp.duration);
		sb.append("&fingerprint="); sb.append(fp.fingerprint);
		//sb.append("&meta=recordings+releasegroups+compress");
		//sb.append("&meta=recordings+releasegroups");
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
			StringBuilder responseText = new StringBuilder();			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while((line = reader.readLine()) != null) {
				responseText.append(line);
			}
			
			conn.disconnect();
			
			System.out.println(responseText.toString());
			
			result = parseXmlResponse(responseText.toString());
			
//			sb = new StringBuilder();
//			
//			JSONObject json = new JSONObject(responseText.toString());
//			if(json.getString("status").equals("ok")){
//				JSONArray results = json.getJSONArray("results");
//				for(int i = 0; i < results.length(); i++){
//					JSONObject res = results.getJSONObject(i);
//					if(res.has("recordings")){
//						sb.append(
//								res.getJSONArray("recordings").getJSONObject(0).getString("title")
//						);
//						sb.append(" - ");
//						sb.append(
//								res.getJSONArray("recordings").getJSONObject(0).getJSONArray("artists").getJSONObject(0).getString("name")
//						);
//						
//						sb.append(" (");
//						sb.append(res.getDouble("score"));
//						sb.append("); ");
//						sb.append("\r\n");
//					}
//				}
//				
//				result = sb.toString();
//			}

		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * http://www.java-samples.com/showtutorial.php?tutorialid=152
	 * 
	 * @param ele
	 * @param tagName
	 * @return
	 */
	private String getTextValue(Element ele, String tagName) {
		String textVal = null;
		NodeList nl = ele.getElementsByTagName(tagName);
		if(nl != null && nl.getLength() > 0) {
			Element el = (Element)nl.item(0);
			textVal = el.getFirstChild().getNodeValue();
		}

		return textVal;
	}
	
	private String parseXmlResponse(String response){
		StringBuilder sb = new StringBuilder();
		
		try {
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();			
			Document doc = db.parse(new InputSource(new StringReader(response)));
			
			NodeList results = doc.getElementsByTagName("result");
			for(int i = 0; i < results.getLength(); i++){
				Element result = (Element)results.item(0);
				
				// must be present
				NodeList nl = result.getElementsByTagName("score");
				Element scoreEl = (Element)nl.item(0);
				String s = scoreEl.getFirstChild().getNodeValue();
				double score = Double.valueOf(s);
				
				nl = result.getElementsByTagName("recording");
				for(int j = 0; j < nl.getLength(); j++){
					Element e = (Element)nl.item(j);
					String title = getTextValue(e, "title");
					
					sb.append(title);
					
					s = getTextValue(e, "duration");
					int duration = Integer.valueOf(s);
					String mbid = getTextValue(e, "id");
					
					NodeList nlart = e.getElementsByTagName("artist");
					for(int k = 0; k < nlart.getLength(); k++){
						Element eart = (Element)nlart.item(k);
						String name = getTextValue(eart, "name");
						
						sb.append(" - ");
						sb.append(name);
					}
					sb.append("\r\n");
				}
				
				sb.append("Score: ");
				sb.append(score);				
			}
									
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return sb.toString();
	}
	
	private String parseJsonResponse(String response){
		StringBuilder sb = new StringBuilder();
		
		return sb.toString();
	}
}