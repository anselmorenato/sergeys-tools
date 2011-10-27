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

	
	@Override
	public Collection<ImageSearchResult> search(ImageSearchRequest req1) {
		ArrayList<ImageSearchResult> results = new ArrayList<ImageSearchResult>(); 
		
		SearchRequest req = new SearchRequest();
		req.setAppId(APP_ID);
		
		req.setSources(new SourceType[]{ SourceType.Image });
		req.setQuery(req1.getQuery());
		//req.setOptions(new SearchOption[]{ new SearchOption("") });
		
		ImageRequest ir = new ImageRequest();
		//ir.setCount(new UnsignedInt(15));
		ir.setOffset(new UnsignedInt(0));
		ir.setFilters(new String[]{ "Size:Medium", "Aspect:Square" });	// http://msdn.microsoft.com/en-us/library/dd560913.aspx
		req.setImage(ir);
		
		BingServiceLocator loc = new BingServiceLocator();
		try {
			BingPortType portType = loc.getBingPort();
			SearchRequestType1 reqType = new SearchRequestType1(req);			
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
					
					results.add(item);
				}
			}
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 

		return results;
	}

	@Override
	public Collection<ImageSearchResult> searchMore() {
		// TODO Auto-generated method stub
		return null;
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
