package org.sergeys.coverfinder.logic;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.GZIPOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sergeys.library.NotImplementedException;
import org.sergeys.library.ProcessStreamReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
	private final String FpCalc = "fpcalc";
	
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
	
	public boolean isAvailable(){
		return checkFingerprintUtility();
	}
	
	private boolean checkFingerprintUtility(){
		String fpcalcPath = Settings.getSettingsDirPath() + File.separator + FpCalc;
		String fpcalcPathWin = Settings.getSettingsDirPath() + File.separator + FpCalc + ".exe";
		
		if(new File(fpcalcPath).exists() || new File(fpcalcPathWin).exists()){
			return true;
		}
		
		@SuppressWarnings("rawtypes")
		Class cl = getClass();
		
//		URL u = cl.getResource("/resources/" + FpCalc);
//		System.out.println("url " + u);
//		u = cl.getResource("/resources/" + FpCalc + ".exe");
//		System.out.println("url " + u);
		
		String targetFile;
		InputStream is = cl.getResourceAsStream("/resources/" + FpCalc);
		if(is == null){
System.out.println("no fpcalc, trying .exe ");			
			is = cl.getResourceAsStream("/resources/" + FpCalc + ".exe");
			if(is == null){
System.out.println("no fpcalc.exe ");				
				return false;
			}
			targetFile = fpcalcPathWin;
		}
		else{
			targetFile = fpcalcPath;
		}
		
		Settings.checkDirectory();
		
		try {
System.out.println("writing to " + targetFile);			
			byte[] buffer = new byte[2048];
			FileOutputStream fos = new FileOutputStream(targetFile);
			int count = 0;
			while((count = is.read(buffer)) != -1){
				fos.write(buffer, 0, count);
			}
			fos.close();
			new File(targetFile).setExecutable(true);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public Fingerprint getFingerprint(File file) throws Exception{

		if(!checkFingerprintUtility()){
			throw new NotImplementedException("not supported on this platform");
		}
		
		Fingerprint fp = new Fingerprint();
		
		try {
			String[] args = { 
					//"d:\\workspace\\coverfinder\\lib\\win32\\fpcalc",
					Settings.getSettingsDirPath() + File.separator + FpCalc,	// on windows .exe extension isn't mandatory
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
//					int duration = Integer.valueOf(s);
//					String mbid = getTextValue(e, "id");
					
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
	
//	private String parseJsonResponse(String response){
//		StringBuilder sb = new StringBuilder();
//		
//		return sb.toString();
//	}
}
