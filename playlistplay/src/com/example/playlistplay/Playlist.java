package com.example.playlistplay;

public class Playlist {
	private long id;
	private String title;
	private String artist;
	private String path;

	public Playlist(long playID,String playTitle, String playPath) {
		id = playID;
		title = playTitle;
		
		path = playPath;
	}

	public long getID() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getPath() {
		return path;
	}
}
