package com.example.playlistplay;

public class Album {
	private long id;
	private String album;
	private String artist;
	private String artpath;
	public Album(long albumID, String albumTitle, String albumArtist,String albumart) {
		  id=albumID;
		  album=albumTitle;
		  artist=albumArtist;
		  artpath=albumart;
		}
	public long getID(){return id;}
	public String getTitle(){return album;}
	public String getArtist(){return artist;}
	public String getArt(){return artpath;}
}
