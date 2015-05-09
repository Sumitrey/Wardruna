package com.example.playlistplay;



import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class SongsManager {
	private ArrayList<Song> songList = new ArrayList<Song>();
	
	Context context;
	Uri musicUri;
	String select=null;
	String[] selectarg=null;
	String[] proj=new String[]{MediaStore.Audio.Media._ID,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,MediaStore.Audio.Media.DATA};
	
	public SongsManager(Context baseContext) {
		// TODO Auto-generated constructor stub
		this.context = baseContext;
	}

	public ArrayList<Song> getSongList(int type,long id,String title) {
		// retrieve song info
		ContentResolver musicResolver = context.getContentResolver();
		if(type==1)
		musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		else if(type==2)
		musicUri = android.provider.MediaStore.Audio.Playlists.Members.getContentUri("external", id);
		else if(type==3)
		{
			musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			select=MediaStore.Audio.Media.ALBUM + "=?";
			selectarg=new String[]{title};
		}
		else if(type==4)
		musicUri =android.provider.MediaStore.Audio.Genres.Members.getContentUri("external",id);
		else
		{
			musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
			select=MediaStore.Audio.Media.ARTIST + "=?";
			selectarg=new String[]{title};
		}
			
		
		Cursor musicCursor = musicResolver.query(musicUri, null,select,selectarg,
				MediaStore.Audio.Media.TITLE+" ASC");
		if (musicCursor != null && musicCursor.moveToFirst()) {
			// get columns
			int titleColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
			int pathColumn=musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DATA);
			// add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				String thisPath= musicCursor.getString(pathColumn);
				songList.add(new Song(thisId, thisTitle, thisArtist,thisPath));
			} while (musicCursor.moveToNext());
		}
		return songList;
	}
}
