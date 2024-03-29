package org.sergeys.coverfinder.logic;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

public class ImageSearchResult {
	
	private URL thumbnailUrl;
	private URL imageUrl;
	private int width;
	private int height;
	private long fileSize;
	
	private boolean hasThumbnailImage = false;
	private boolean hasFullImage = false;
	
	private Image thumbnailImage;
	private Image image;
	//private boolean hasImages = false;
	private File imageFile;
	
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


	public long getFileSize() {
		return fileSize;
	}


	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public void downloadThumbnailImage(){
		try {
			System.out.println(String.format("Downloading thumbnail %s", getThumbnailUrl()));
			thumbnailImage = ImageIO.read(getThumbnailUrl());
			hasThumbnailImage = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(String.format("%s not downloaded: %s, %s",
					getThumbnailUrl(),
					e.getLocalizedMessage(), e.getCause() == null ? "(unknown cause)" : e.getCause().getLocalizedMessage()));
		}
	}

	public void downloadFullImage(){
		try {			
			System.out.println(String.format("Downloading thumbnail %s", getImageUrl()));
			
			InputStream is = getImageUrl().openConnection().getInputStream();
			
			imageFile = File.createTempFile("coverfinder-image", ".dat");
			imageFile.deleteOnExit();												
			FileOutputStream fos = new FileOutputStream(imageFile);
			
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buf = new byte[2048];
			int count = 0;
			while((count = bis.read(buf)) > 0){
				fos.write(buf, 0, count);
			}
			
			fos.close();
			bis.close();
						
			image = ImageIO.read(imageFile);
			
			hasFullImage = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(String.format("%s not downloaded: %s, %s",
					getImageUrl(),
					e.getLocalizedMessage(), e.getCause() == null ? "(unknown cause)" : e.getCause().getLocalizedMessage()));
		}
		
	}
	
	public boolean hasThumbnailImage() {
		return hasThumbnailImage;
	}
	
	public Image getThumbnailImage() {
		return thumbnailImage;
	}

	public boolean hasFullImage() {
		return hasFullImage;
	}
	
	public Image getFullImage() {
		return image;
	}

	public File getImageFile() {
		return imageFile;
	}
	
}
