package org.sergeys.coverfinder.logic;

import java.net.URL;

public class ImageSearchResult {
	
	private URL thumbnailUrl;
	private URL imageUrl;
	private int width;
	private int height;
	
	public ImageSearchResult(){}


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}


	public URL getThumbnailUrl() {
		return thumbnailUrl;
	}


	public void setThumbnailUrl(URL thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}


	public URL getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(URL imageUrl) {
		this.imageUrl = imageUrl;
	}
}
