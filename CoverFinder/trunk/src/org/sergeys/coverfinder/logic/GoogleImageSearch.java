package org.sergeys.coverfinder.logic;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleImageSearch implements IImageSearchEngine {

	private String referer = "http://svs.bugz.org.ua";
	private int resultCount = 8;	// This argument supplies an integer from 1–8 indicating the number of results to return per page
	private String currentBaseQuery;
	private int currentOffset = 0; 
	
	private Collection<ImageSearchResult> doRequest(String query) throws ImageSearchException{
		ArrayList<ImageSearchResult> res = new ArrayList<ImageSearchResult>();
		
		try {
									
			// escape parameters
			URI uri = new URI("https", "ajax.googleapis.com", "/ajax/services/search/images", query, null);
						
//System.out.println("request uri: " + uri);			
//System.out.println("request uri ascii: " + uri.toASCIIString());

			String line;
			StringBuilder builder = new StringBuilder();
			
			// request google
			//URLConnection connection = uri.toURL().openConnection();
			URLConnection connection = new URL(uri.toASCIIString()).openConnection();
			connection.addRequestProperty("Referer", referer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}

			reader.close();
			
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
			//e.printStackTrace();			
			throw new ImageSearchException(e);
		}
				
		return res;		
	}
	
	@Override
	public Collection<ImageSearchResult> search(ImageSearchRequest req) throws ImageSearchException {
//		try{
			//		URL url = new URL("https://ajax.googleapis.com/ajax/services/search/images?" +
			//        "v=1.0&q=barack%20obama&key=INSERT-YOUR-KEY&userip=INSERT-USER-IP");
			
			// http://code.google.com/apis/imagesearch/v1/jsondevguide.html#json_snippets_java			
			StringBuilder sb = new StringBuilder();
			
			sb.append("v=1.0");
			sb.append("&q=");
			sb.append(req.getQuery());
			//sb.append(URLEncoder.encode(req.getQuery(), "UTF-8"));	// encode manually instead of using URI		
			
			String userip = null;
			try {
				userip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(userip != null){
				sb.append("&userip=");
				sb.append(userip);
			}						
			
			sb.append("&rsz=" + resultCount);
			
			this.currentBaseQuery = sb.toString();
			this.currentOffset = 0;
			
			return doRequest(this.currentBaseQuery);
			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	@Override
	public Collection<ImageSearchResult> searchMore() throws ImageSearchException {
		if(currentBaseQuery == null){
			return null;
		}
		currentOffset += resultCount;
		String moreQuery = currentBaseQuery + "&start=" + currentOffset;
		return doRequest(moreQuery);
	}

	@Override
	public String getName() {		
		return "Google";
	}

	@Override
	public String getDisplayName() {
		//return "Google Image Search";
		return "Google";
	}

	@Override
	public String getBranding() {
		return "Powered by Google";
	}

}
