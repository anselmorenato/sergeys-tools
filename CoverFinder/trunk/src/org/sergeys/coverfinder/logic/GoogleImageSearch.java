package org.sergeys.coverfinder.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleImageSearch implements IImageSearchEngine {

	@Override
	public Collection<ImageSearchResult> search(ImageSearchRequest req) {
		ArrayList<ImageSearchResult> res = new ArrayList<ImageSearchResult>();
		
		String referer = "http://svs.bugz.org.ua";
		
		
		
		try {
//			URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
//			        "v=1.0&q=barack%20obama&key=INSERT-YOUR-KEY&userip=INSERT-USER-IP");

			// http://code.google.com/apis/imagesearch/v1/jsondevguide.html#json_snippets_java			
			StringBuilder sb = new StringBuilder();
			//sb.append("/ajax/services/search/images");
			sb.append("v=1.0");
			sb.append("&q=");
			sb.append(req.getQuery());
			sb.append("&userip=");
			sb.append(InetAddress.getLocalHost().getHostAddress());
			sb.append("&rsz=8");
									
			// escape parameters
			URI uri = new URI("https", "ajax.googleapis.com", "/ajax/services/search/images", sb.toString(), null);
			//URL url = uri.toURL();
			
System.out.println("request: " + uri);			

			

			String line;
			StringBuilder builder = new StringBuilder();
			
			// request google
			URLConnection connection = uri.toURL().openConnection();
			connection.addRequestProperty("Referer", referer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}

			// test response
//			BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/jsonresponse.txt")));
//			while((line = reader.readLine()) != null) {
//				builder.append(line);
//			}
			
			JSONObject json = new JSONObject(builder.toString());
			
			
			int retcode = json.getInt("responseStatus");			
			String retDetails = json.isNull("responseDetails") ? "" : json.getString("responseDetails");
			
			System.out.println("result: " + retcode + " " + retDetails);
			if(retcode != 200){
				return res;
			}
			
			JSONArray items = json.getJSONObject("responseData").getJSONArray("results");
			
			for(int i = 0; i < items.length(); i++){
				JSONObject item = items.getJSONObject(i);
//System.out.println("request: " + uri);
				ImageSearchResult r = new ImageSearchResult();
				r.setThumbnailUrl(new URL(item.getString("tbUrl")));
				r.setImageUrl(new URL(item.getString("url")));
				r.setWidth(item.getInt("width"));
				r.setHeight(item.getInt("height"));
				
				res.add(r);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return res;
	}

	@Override
	public Collection<ImageSearchResult> searchMore() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {		
		return "Google";
	}

	@Override
	public String getDisplayName() {
		return "Google Image Search";
	}

}
