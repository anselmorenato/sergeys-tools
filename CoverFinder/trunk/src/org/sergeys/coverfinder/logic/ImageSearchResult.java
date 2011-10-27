package org.sergeys.coverfinder.logic;

import java.net.URL;

public class ImageSearchResult {
	
	private URL thumbnail;
	private URL fullImage;
	private int width;
	private int height;
	
	public ImageSearchResult(){}

	public URL getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(URL thumbnail) {
		this.thumbnail = thumbnail;
	}

	public URL getFullImage() {
		return fullImage;
	}

	public void setFullImage(URL fullImage) {
		this.fullImage = fullImage;
	}

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
}
