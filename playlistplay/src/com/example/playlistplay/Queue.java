package com.example.playlistplay;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Queue extends Activity {
	private ArrayList<Song> queueList = new ArrayList<Song>();
	private ListView queueView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playqueue);
		queueView = (ListView) findViewById(R.id.playqueue_list);
		queueList = BridgeClass.instance().cache;
		if(queueList!=null){
		SongAdapter songAdt = new SongAdapter(this, queueList);
		queueView.setAdapter(songAdt);
		}
	}

	public class SongAdapter extends BaseAdapter {
		private ArrayList<Song> songs;
		private LayoutInflater songInf;
		Context context;

		LinearLayout songLay;

		public SongAdapter(Context c, ArrayList<Song> theSongs) {
			songs = theSongs;
			songInf = LayoutInflater.from(c);
			this.context = c;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return songs.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView,
				final ViewGroup parent) {
			// TODO Auto-generated method stub
			// map to song layout
			songLay = (LinearLayout) songInf.inflate(R.layout.queue, parent,
					false);
			// get title and artist views
			TextView songView = (TextView) songLay
					.findViewById(R.id.queue_title);
			TextView artistView = (TextView) songLay
					.findViewById(R.id.queue_artist);
			// get song using position
			final Song currSong = songs.get(position);
			// get title and artist strings
			songView.setText("Song Title: "+currSong.getTitle());
			artistView.setText("Song Artist: " +currSong.getArtist());
			// set position as tag
			songLay.setTag(position);
			return songLay;
		}
	}

}
