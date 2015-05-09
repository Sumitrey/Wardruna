package com.example.playlistplay;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable{
	private long id;
	private String title;
	private String artist;
	private String path;

	public Song(long songID, String songTitle, String songArtist,
			String songPath) {
		id = songID;
		title = songTitle;
		artist = songArtist;
		path = songPath;
	}

	public long getID() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getArtist() {
		return artist;
	}

	public String getPath() {
		return path;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
