package org.sergeys.coverfinder.logic;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.axis.types.UnsignedInt;

import com.microsoft.schemas.LiveSearch._2008._03.Search.BingPortType;
import com.microsoft.schemas.LiveSearch._2008._03.Search.BingServiceLocator;
import com.microsoft.schemas.LiveSearch._2008._03.Search.ImageRequest;
import com.microsoft.schemas.LiveSearch._2008._03.Search.ImageResult;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchRequest;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchRequestType1;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchResponse;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SearchResponseType0;
import com.microsoft.schemas.LiveSearch._2008._03.Search.SourceType;
import com.microsoft.schemas.LiveSearch._2008._03.Search.Thumbnail;

public class BingImageSearch 
implements IImageSearchEngine
{
	
	// https://ssl.bing.com/webmaster/developers/appids.aspx?rfp=7
	
	// get wsdl from http://api.bing.net/search.wsdl?AppID=3835365F7AE679189D6105256B8EFE900B846E6A&Version=2.2 
	
	private static final String APP_ID = "3835365F7AE679189D6105256B8EFE900B846E6A";

	BingServiceLocator locator = new BingServiceLocator();
	private SearchRequest searchRequest;
	private ImageRequest imageRequest;
	private int nextOffset = 0;
	
	private void buildRequest(String query){
		searchRequest = new SearchRequest();
		searchRequest.setAppId(APP_ID);
		
		searchRequest.setSources(new SourceType[]{ SourceType.Image });
		searchRequest.setQuery(query);
		
		imageRequest = new ImageRequest();
		imageRequest.setOffset(new UnsignedInt(0));
		imageRequest.setFilters(new String[]{ "Size:Medium", "Aspect:Square" });	// http://msdn.microsoft.com/en-us/library/dd560913.aspx
		searchRequest.setImage(imageRequest);
		
		nextOffset = 0;
	}
	
	private Collection<ImageSearchResult> doRequest(){
		ArrayList<ImageSearchResult> results = new ArrayList<ImageSearchResult>(); 
				
		try {
			BingPortType portType = locator.getBingPort();
			SearchRequestType1 reqType = new SearchRequestType1(searchRequest);			
			SearchResponseType0 respType = portType.search(reqType);
			SearchResponse resp = respType.getParameters();
									
			ImageResult[] images = resp.getImage().getResults();
						
			if(images == null){
				com.microsoft.schemas.LiveSearch._2008._03.Search.Error[] errors = resp.getErrors();
								
				if(errors != null){
					//JOptionPane.showMessageDialog(null, "Failed");
					for(com.microsoft.schemas.LiveSearch._2008._03.Search.Error err: errors){
						System.out.println("error: " + err.getMessage());
					}
				}
				else{
					//JOptionPane.showMessageDialog(null, "Nothing");
				}
				
				return results;
			}
			
			if(images.length > 0){
				
				System.out.println(images.length + " results");
				
				for(int i = 0; i < images.length; i++){
					ImageResult imgResult = images[i];
															
					String url = imgResult.getMediaUrl();
					Thumbnail th = imgResult.getThumbnail();
					
					ImageSearchResult item = new ImageSearchResult();
					item.setImageUrl(new URL(url));
					item.setThumbnailUrl(new URL(th.getUrl()));
					item.setWidth(imgResult.getWidth().intValue());
					item.setHeight(imgResult.getHeight().intValue());
					item.setFileSize(imgResult.getFileSize().longValue());
					
					results.add(item);
				}
				
				nextOffset += images.length;
				imageRequest.setOffset(new UnsignedInt(nextOffset));
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		return results;		
	}
	
	@Override
	public Collection<ImageSearchResult> search(ImageSearchRequest req) {
		buildRequest(req.getQuery());
		return doRequest();
	}

	@Override
	public Collection<ImageSearchResult> searchMore() {		
		return doRequest();
	}

	@Override
	public String getName() {		
		return "Bing";
	}

	@Override
	public String getDisplayName() {
		return "Bing";
	}

}
