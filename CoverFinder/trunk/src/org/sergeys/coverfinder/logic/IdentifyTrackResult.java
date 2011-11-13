package org.sergeys.coverfinder.logic;

/**
 * Corresponds to the recording element from acoustid service
 * 
 * @author sergeys
 *
 */
public class IdentifyTrackResult {
	
	private String artist;
	private String title;
	private String mbid;	// musicbrainz recording id
	private double score;
	
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMbid() {
		return mbid;
	}
	public void setMbid(String mbid) {
		this.mbid = mbid;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	
}
