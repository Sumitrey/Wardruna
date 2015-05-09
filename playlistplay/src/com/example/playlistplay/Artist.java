package com.example.playlistplay;

public class Artist {
	private int id;
	private String name;
	private int c,c1;
	public Artist(int id1,String artistTitle,int count,int count1) {
		id=id1;
		  name=artistTitle;
		 c=count;
		 c1=count1;
		}
	public int getID(){return id;}
	public String getTitle(){return name;}
	public  int getCountAlbum(){return c;}
	public  int getCountSong(){return c1;}
	
}
