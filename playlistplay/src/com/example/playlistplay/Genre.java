package com.example.playlistplay;

public class Genre {
	private int id;
	private String name;
	
	public Genre(int id1,String genreTitle) {
		id=id1;
		  name=genreTitle;
		 
		}
	public int getID(){return id;}
	public String getTitle(){return name;}
	
	
}
