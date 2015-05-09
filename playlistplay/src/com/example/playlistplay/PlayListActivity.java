package com.example.playlistplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class PlayListActivity extends TabActivity implements
		OnTabChangeListener {
	private ArrayList<Song> songList = new ArrayList<Song>();
	private ArrayList<Song> queueList = new ArrayList<Song>();
	private ArrayList<Song> songList1 = new ArrayList<Song>();
	private ArrayList<Song> songList2 = new ArrayList<Song>();
	private ArrayList<Song> songList3 = new ArrayList<Song>();
	private ArrayList<Song> songList4 = new ArrayList<Song>();;

	private ListView songView, playlistView, artistView, albumView, genreView;
	private ListView albumView121;
	private ArrayList<Album> albumList = new ArrayList<Album>();
	private ArrayList<Album> albumList2 = new ArrayList<Album>();
	private ArrayList<Genre> genreList = new ArrayList<Genre>();
	private ArrayList<Artist> artistList = new ArrayList<Artist>();
	private ArrayList<Playlist> playList1 = new ArrayList<Playlist>();
	private static final String LIST1_TAB_TAG = "Song";
	private static final String LIST2_TAB_TAG = "Playlist";
	private static final String LIST3_TAB_TAG = "Album";
	private static final String LIST4_TAB_TAG = "Genre";
	private static final String LIST5_TAB_TAG = "Artist";
	private final int SONG_VIEW = 0;
	private final int PLAYLIST_VIEW = 1;
	private final int SONG = 1;
	private final int PLAYLIST = 2;
	private final int ALBUM = 3;
	private final int GENRE = 4;

	public int c = 0;
	Uri musicUri1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	TabHost th;
	TabSpec ts;
	String select = null;
	String[] selectarg = new String[] { null };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		// convert to simple array
		th = getTabHost();
		th.setOnTabChangedListener(this);

		songView = (ListView) findViewById(R.id.song_list);
		playlistView = (ListView) findViewById(R.id.playlist_list);
		songList = new ArrayList<Song>();
		SongsManager plm = new SongsManager(getApplicationContext());
		// get all songs from sdcard
		this.songList = plm.getSongList(SONG, 0, "");

		SongAdapter songAdt = new SongAdapter(PlayListActivity.this, songList,
				SONG_VIEW);
		songView.setAdapter(songAdt);
		// listening to single listitem click

		songView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting listitem index
				final int songIndex = position;
				Log.v("Nicche na", Integer.toString(position));
				// Starting new intent
				queueList = songList;
				BridgeClass.instance().cache = queueList;

				Intent in = new Intent(PlayListActivity.this,
						AndroidBuildingMusicPlayerActivity.class);
				// Sending songIndex to PlayerActivity
				in.putExtra("songIndex", songIndex);

				setResult(100, in);
				// Closing PlayListView
				finish();
			}
		});

		th.addTab(th.newTabSpec(LIST1_TAB_TAG).setIndicator(LIST1_TAB_TAG).setContent(R.id.tab1));
		// PlayLIst

		final ContentResolver resolver = PlayListActivity.this
				.getContentResolver();
		final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

		final Cursor musicCursor = resolver.query(uri, null, null, null,
				MediaStore.Audio.Playlists.NAME + " ASC");
		if (musicCursor != null && musicCursor.moveToFirst()
				&& musicCursor.getCount() > 0) {
			// get columns
			int titleColumn = musicCursor
					.getColumnIndex(MediaStore.Audio.Playlists.NAME);
			int idColumn = musicCursor
					.getColumnIndex(MediaStore.Audio.Playlists._ID);

			int pathColumn = musicCursor
					.getColumnIndex(android.provider.MediaStore.Audio.Playlists.DATA);
			// add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);

				String thisPath = musicCursor.getString(pathColumn);

				Playlist play = new Playlist(thisId, thisTitle, thisPath);
				if (play != null)
					Log.d("A.TAG", "null");
				playList1.add(play);
				Log.d("balas", "playlist: " + thisTitle + "   " + thisPath
						+ "   " + Long.toString(thisId));
				Log.v("LOGGING_TAG", Integer.toString(playList1.size()));
			} while (musicCursor.moveToNext());
		}
		musicCursor.close();
		PlaylistAdapter playAdt = new PlaylistAdapter(PlayListActivity.this,
				playList1);
		playlistView.setAdapter(playAdt);
		playlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				long retId = playList1.get(position).getID();
				Log.d("balas", Long.toString(retId));
				Log.d("balas", playList1.get(position).getTitle());
				SongsManager plm = new SongsManager(getApplicationContext());
				PlayListActivity.this.songList1 = plm.getSongList(PLAYLIST,
						retId, "");

				setContentView(R.layout.main_layout);
				songView = (ListView) findViewById(R.id.song_list);

				SongAdapter songAdt = new SongAdapter(PlayListActivity.this,
						songList1, PLAYLIST_VIEW, retId);

				songView.setAdapter(songAdt);
				songView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// getting listitem index
						final int songIndex = position;
						Log.v("Nicche na", Integer.toString(position));
						// Starting new intent
						queueList = songList1;
						BridgeClass.instance().cache = queueList;
						Intent in = new Intent(getApplicationContext(),
								AndroidBuildingMusicPlayerActivity.class);
						// Sending songIndex to PlayerActivity
						in.putExtra("songIndex", songIndex);

						setResult(100, in);

						// Closing PlayListView
						finish();

					}
				});

			}

		});

		th.addTab(th.newTabSpec(LIST2_TAB_TAG).setIndicator(LIST2_TAB_TAG).setContent(R.id.tab2));

		// AlBUM//

		albumView = (ListView) findViewById(R.id.album_list);

		ContentResolver musicResolver = getContentResolver();
		Uri musicUri = android.provider.MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
		// String[]
		String[] proj = { android.provider.MediaStore.Audio.Albums.ALBUM,
				BaseColumns._ID,
				android.provider.MediaStore.Audio.Albums.ARTIST,
				MediaStore.Audio.Albums.ALBUM_ART };
		Cursor musicCursor1 = musicResolver.query(musicUri, null, null, null,
				MediaStore.Audio.Albums.ALBUM + " ASC");
		if (musicCursor1 != null && musicCursor1.moveToFirst()) {
			// get columns

			int titleColumn = musicCursor1
					.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM);
			int idColumn = musicCursor1.getColumnIndex(BaseColumns._ID);
			int artistColumn = musicCursor1
					.getColumnIndex(android.provider.MediaStore.Audio.Albums.ARTIST);
			// add albums to list
			int artpath = musicCursor1
					.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

			do {
				long thisId = musicCursor1.getLong(idColumn);
				String thisTitle = musicCursor1.getString(titleColumn);
				String thisArtist = musicCursor1.getString(artistColumn);
				String path = musicCursor1.getString(artpath);
				Log.d("tag", "Albums : " + thisId + "     " + thisTitle
						+ "    " + thisArtist + "     " + path);
				albumList.add(new Album(thisId, thisTitle, thisArtist, path));
			} while (musicCursor1.moveToNext());
		}

		AlbumAdapter albumAdt = new AlbumAdapter(PlayListActivity.this,
				albumList);
		albumView.setAdapter(albumAdt);
		albumView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				SongsManager plm = new SongsManager(getApplicationContext());
				PlayListActivity.this.songList2 = plm.getSongList(ALBUM, 0,
						albumList.get(position).getTitle());

				setContentView(R.layout.main_layout);
				songView = (ListView) findViewById(R.id.song_list);

				SongAdapter songAdt = new SongAdapter(PlayListActivity.this,
						songList2, 0);

				songView.setAdapter(songAdt);
				songView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// getting listitem index
						final int songIndex = position;
						Log.v("Nicche na", Integer.toString(position));
						// Starting new intent
						queueList = songList2;
						BridgeClass.instance().cache = queueList;
						Intent in = new Intent(PlayListActivity.this,
								AndroidBuildingMusicPlayerActivity.class);
						// Sending songIndex to PlayerActivity
						in.putExtra("songIndex", songIndex);

						setResult(100, in);

						// Closing PlayListView
						finish();
					}
				});

			}
		});

		th.addTab(th.newTabSpec(LIST3_TAB_TAG).setIndicator(LIST3_TAB_TAG).setContent(R.id.tab3));
		// Genre

		genreView = (ListView) findViewById(R.id.genre_list);
		final ContentResolver musicResolver1 = getContentResolver();
		Uri musicUri1 = android.provider.MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI;
		Cursor musicCursor11 = musicResolver1.query(musicUri1, null, null,
				null, MediaStore.Audio.Genres.NAME + " ASC");
		if (musicCursor11 != null && musicCursor11.moveToFirst()) {
			// get columns

			int titleColumn = musicCursor11
					.getColumnIndex(android.provider.MediaStore.Audio.Genres.NAME);
			int idColumn = musicCursor11.getColumnIndex(BaseColumns._ID);

			do {

				String thisTitle = musicCursor11.getString(titleColumn);
				final int thisId = musicCursor11.getInt(idColumn);

				Uri musicUri11 = MediaStore.Audio.Genres.Members.getContentUri(
						"external", thisId);

				Log.d("tag",
						"Genre: " + thisTitle + "  " + Integer.toString(thisId));
				genreList.add(new Genre(thisId, thisTitle));
			} while (musicCursor11.moveToNext());
		}
		musicCursor11.close();

		genreAdapter genreAdt = new genreAdapter(PlayListActivity.this,
				genreList);
		genreView.setAdapter(genreAdt);
		genreView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				long retId = genreList.get(position).getID();

				SongsManager plm = new SongsManager(getApplicationContext());
				PlayListActivity.this.songList3 = plm.getSongList(GENRE, retId,
						"");

				setContentView(R.layout.main_layout);
				songView = (ListView) findViewById(R.id.song_list);

				SongAdapter songAdt = new SongAdapter(PlayListActivity.this,
						songList3, 0);

				songView.setAdapter(songAdt);
				songView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// getting listitem index
						final int songIndex = position;
						Log.v("Nicche na", Integer.toString(position));
						// Starting new intent
						queueList = songList3;
						BridgeClass.instance().cache = queueList;
						Intent in = new Intent(PlayListActivity.this,
								AndroidBuildingMusicPlayerActivity.class);
						// Sending songIndex to PlayerActivity
						in.putExtra("songIndex", songIndex);

						setResult(100, in);

						// Closing PlayListView
						finish();
					}
				});

			}
		});

		th.addTab(th.newTabSpec(LIST4_TAB_TAG).setIndicator(LIST4_TAB_TAG).setContent(R.id.tab4));
		// Artist
		artistView = (ListView) findViewById(R.id.artist_list);
		final ContentResolver musicResolver11 = getContentResolver();
		Uri musicUri11 = android.provider.MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
		Cursor musicCursor111 = musicResolver11.query(musicUri11, null, null,
				null, MediaStore.Audio.Artists.ARTIST + " ASC");
		if (musicCursor111 != null && musicCursor111.moveToFirst()) {
			// get columns

			int titleColumn = musicCursor111
					.getColumnIndex(android.provider.MediaStore.Audio.Artists.ARTIST);
			int idColumn = musicCursor111.getColumnIndex(BaseColumns._ID);
			int countalbumColumn = musicCursor111
					.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
			int countsongColumn = musicCursor111
					.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS);

			do {

				String thisTitle = musicCursor111.getString(titleColumn);
				final int thisId = musicCursor111.getInt(idColumn);

				int count = musicCursor111.getInt(countalbumColumn);
				int count1 = musicCursor111.getInt(countsongColumn);
				
				artistList.add(new Artist(thisId, thisTitle, count, count1));
			} while (musicCursor111.moveToNext());
		}
		musicCursor111.close();

		ArtistAdapter artistAdt = new ArtistAdapter(this, artistList);
		artistView.setAdapter(artistAdt);
		artistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int retId = artistList.get(position).getID();
				BridgeClass.instance().cache1 = artistList.get(position)
						.getTitle();
				ContentResolver musicResolver = getContentResolver();
				final Uri musicUri = android.provider.MediaStore.Audio.Artists.Albums
						.getContentUri("external", retId);

				Cursor musicCursor = musicResolver.query(musicUri, null, null,
						null, MediaStore.Audio.Albums.ALBUM + " ASC");
				if (musicCursor != null && musicCursor.moveToFirst()) {
					// get columns

					int titleColumn = musicCursor
							.getColumnIndex(android.provider.MediaStore.Audio.Albums.ALBUM);
					int idColumn = musicCursor.getColumnIndex(BaseColumns._ID);
					int artistColumn = musicCursor
							.getColumnIndex(android.provider.MediaStore.Audio.Albums.ARTIST);
					int artpath = musicCursor
							.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
					// add albums to list

					do {
						long thisId = musicCursor.getLong(idColumn);
						String thisTitle = musicCursor.getString(titleColumn);
						String thisArtist = musicCursor.getString(artistColumn);
						String thisPath = musicCursor.getString(artpath);

						albumList2.add(new Album(thisId, thisTitle, thisArtist,
								thisPath));
					} while (musicCursor.moveToNext());
				}

				setContentView(R.layout.main_layout);
				albumView = (ListView) findViewById(R.id.album_list);

				AlbumAdapter albumAdt = new AlbumAdapter(PlayListActivity.this,
						albumList2);
				albumView.setAdapter(albumAdt);

				albumView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Album currAlbum = albumList2.get(position);
						SongsManager pl = new SongsManager(
								getApplicationContext());
						songList4 = pl.getSongList(3, 0, currAlbum.getTitle());

						setContentView(R.layout.main_layout);
						songView = (ListView) findViewById(R.id.song_list);

						SongAdapter songAdt = new SongAdapter(
								PlayListActivity.this, songList4, 0);

						songView.setAdapter(songAdt);
						songView.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent,
									View view, int position, long id) {
								// getting listitem index
								final int songIndex = position;
								Log.v("Nicche na", Integer.toString(position));
								// Starting new intent
								queueList = songList4;
								BridgeClass.instance().cache = queueList;
								Intent in = new Intent(
										PlayListActivity.this,
										AndroidBuildingMusicPlayerActivity.class);
								// Sending songIndex to PlayerActivity
								in.putExtra("songIndex", songIndex);

								setResult(100, in);

								// Closing PlayListView
								finish();
							}
						});
					}
				});

			}
		});
		th.addTab(th.newTabSpec(LIST5_TAB_TAG).setIndicator(LIST5_TAB_TAG).setContent(R.id.tab5));
		 TextView x1 = (TextView) th.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
		 TextView x2 = (TextView) th.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
		 TextView x3 = (TextView) th.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
		 TextView x4 = (TextView) th.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
		 TextView x5 = (TextView) th.getTabWidget().getChildAt(4).findViewById(android.R.id.title);
		 float f=(float) 10.58;
		 x1.setTextSize(f);
		 x2.setTextSize(f);
		 x3.setTextSize(f);
		 x4.setTextSize(f);
		 x5.setTextSize(f);
		 x1.setSingleLine();
		 x2.setSingleLine();
		 x3.setSingleLine();
		 x4.setSingleLine();
		 x5.setSingleLine();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();

	}

	public class SongAdapter extends BaseAdapter {
		private ArrayList<Song> songs;
		private LayoutInflater songInf;
		Context context;
		Button button1;
		LinearLayout songLay;
		int type;
		long bigplaylistId;

		public SongAdapter(Context c, ArrayList<Song> theSongs, int typ) {
			songs = theSongs;
			songInf = LayoutInflater.from(c);
			this.context = c;
			type = typ;

		}

		public SongAdapter(Context c, ArrayList<Song> theSongs, int typ,
				long retId) {
			// TODO Auto-generated constructor stub
			songs = theSongs;
			songInf = LayoutInflater.from(c);
			this.context = c;
			type = typ;
			bigplaylistId = retId;
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
			songLay = (LinearLayout) songInf.inflate(R.layout.song, parent,
					false);
			// get title and artist views
			TextView songView = (TextView) songLay
					.findViewById(R.id.song_title);
			TextView artistView = (TextView) songLay
					.findViewById(R.id.song_artist);
			// get song using position
			final Song currSong = songs.get(position);
			// get title and artist strings
			songView.setText(currSong.getTitle());
			artistView.setText(currSong.getArtist());
			// set position as tag
			songLay.setTag(position);

			button1 = (Button) songLay.findViewById(R.id.button1);

			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					// Creating the instance of PopupMenu
					final PopupMenu popup = new PopupMenu(context, button1);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.popup_menu,
							popup.getMenu());
					if (type == 1)
						popup.getMenu().add(Menu.NONE, 6, Menu.NONE,
								"Delete From Playlist");
					// registering popup with OnMenuItemClickListener
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						int i;

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
							case R.id.addplay:
								i = 1;
								PopupMenu popup1 = new PopupMenu(context, v,
										Gravity.CENTER);
								popup1.getMenu().add(Menu.NONE, i++, Menu.NONE,
										"New PlayList");

								final ContentResolver resolver = context
										.getContentResolver();
								final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

								final Cursor musicCursor = resolver.query(uri,
										null, null, null, null);
								if (musicCursor != null
										&& musicCursor.moveToFirst()) {
									// get columns
									int titleColumn = musicCursor
											.getColumnIndex(MediaStore.Audio.Playlists.NAME);

									do {

										String thisTitle = musicCursor
												.getString(titleColumn);
										popup1.getMenu().add(Menu.NONE, i++,
												Menu.NONE, thisTitle);

									} while (musicCursor.moveToNext());
								}
								musicCursor.close();
								popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

									@Override
									public boolean onMenuItemClick(MenuItem item) {
										// TODO Auto-generated method stub
										Intent intent = item.getIntent();
										switch (item.getItemId()) {
										case 1:
											final Dialog dialog = new Dialog(
													context);

											// setting custom layout to dialog
											dialog.setContentView(R.layout.new_playlist);
											dialog.setTitle("New Playlist");
											dialog.show();
											// adding text dynamically
											final EditText txt = (EditText) dialog
													.findViewById(R.id.playlist_name);

											// adding button click event
											Button dismissButton = (Button) dialog
													.findViewById(R.id.cancel);
											dismissButton
													.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															dialog.dismiss();
														}
													});
											Button create = (Button) dialog
													.findViewById(R.id.createplaylist);
											create.setText("Create");
											create.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													ContentResolver resolver = context
															.getContentResolver();

													Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
													String[] proj = {
															MediaStore.Audio.Playlists._ID,
															MediaStore.Audio.Playlists.NAME };
													Log.d("A.TAG",
															"Checking for existing playlist for "
																	+ txt.getText());
													Cursor c = resolver.query(
															playlists, proj,
															null, null, null);
													long playlistId = 0;
													if (c != null
															&& c.getCount() > 0) {
														// Log.d("tag",Integer.toString(c.getCount()));
														c.moveToFirst();
														do {
															String plname = c
																	.getString(c
																			.getColumnIndex(MediaStore.Audio.Playlists.NAME));
															if (plname.equals(txt
																	.getText())) {
																playlistId = c
																		.getLong(c
																				.getColumnIndex(MediaStore.Audio.Playlists._ID));
																break;
															}
														} while (c.moveToNext());
														c.close();
													}
													if (playlistId != 0) {
														Uri deleteUri = ContentUris
																.withAppendedId(
																		playlists,
																		playlistId);
														Log.d("A.TAG",
																"REMOVING Existing Playlist: "
																		+ playlistId);

														// delete the playlist
														resolver.delete(
																deleteUri,
																null, null);
													}

													Log.d("A.TAG",
															"CREATING PLAYLIST: "
																	+ txt.getText());
													ContentValues v1 = new ContentValues();

													v1.put(MediaStore.Audio.Playlists.NAME,
															txt.getText()
																	.toString());

													v1.put(MediaStore.Audio.Playlists.DATE_MODIFIED,
															System.currentTimeMillis());
													Uri newpl = resolver
															.insert(playlists,
																	v1);
													Log.d("A.TAG",
															"Added PlayLIst: "
																	+ newpl);
													Uri insUri = Uri
															.withAppendedPath(
																	newpl,
																	MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);

													int order = 1;
													String[] selectarg = new String[] { Long
															.toString(currSong
																	.getID()) };
													Log.d("A.TAG",
															"Playlist Members Url: "
																	+ insUri);
													c = resolver
															.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
																	null,
																	MediaStore.Audio.Media._ID
																			+ "=?",
																	selectarg,
																	MediaStore.Audio.Media.TITLE
																			+ " ASC");
													if (c.moveToFirst()) {
														Log.d("A.TAG",
																"Adding Songs to PlayList **");
														do {
															long id = c.getLong(c
																	.getColumnIndex(MediaStore.Audio.Media._ID));
															ContentValues cv = new ContentValues();
															cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
																	order++);
															cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
																	id);
															Uri u = resolver
																	.insert(insUri,
																			cv);
															Log.d("A.TAG",
																	"Added Playlist Item: "
																			+ u
																			+ " for AUDIO_ID "
																			+ id);
														} while (c.moveToNext());
													}
													c.close();

													dialog.dismiss();

												}
											});

											break;

										default:
											Toast.makeText(
													context,
													"You Clicked : "
															+ item.getTitle(),
													Toast.LENGTH_SHORT).show();
											Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
											String[] proj = {
													MediaStore.Audio.Playlists._ID,
													MediaStore.Audio.Playlists.NAME };
											Cursor c = resolver.query(
													playlists, proj, null,
													null, null);
											long playlistId = 0;
											if (c != null && c.getCount() > 0) {
												Log.d("tag", Integer.toString(c
														.getCount()));
												c.moveToFirst();
												do {
													String plname = c.getString(c
															.getColumnIndex(MediaStore.Audio.Playlists.NAME));
													if (plname.equals(item
															.getTitle())) {
														playlistId = c.getLong(c
																.getColumnIndex(MediaStore.Audio.Playlists._ID));
														break;
													}
												} while (c.moveToNext());
												c.close();
											}
											Log.d("tag",
													Long.toString(playlistId));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// Projection to get high water mark
											// of PLAY_ORDER in a particular
											// playlist
											final String[] PROJECTION_PLAYLISTMEMBERS_PLAYORDER = new String[] {
													MediaStore.Audio.Playlists.Members._ID,
													MediaStore.Audio.Playlists.Members.PLAY_ORDER };
											// Projection to get the list of
											// song IDs to be added to a
											// playlist
											final String[] PROJECTION_SONGS_ADDTOPLAYLIST = new String[] { MediaStore.Audio.Media._ID, };
											Cursor c2 = resolver
													.query(playlists,
															PROJECTION_PLAYLISTMEMBERS_PLAYORDER,
															null,
															null,
															MediaStore.Audio.Playlists.Members.PLAY_ORDER
																	+ " DESC ");
											int mPlayOrder = 1;
											if (c2 != null) {
												if (c2.moveToFirst()) {
													mPlayOrder = (c2.getInt(c2
															.getColumnIndex(MediaStore.Audio.Playlists.Members.PLAY_ORDER))) + 1;
												}
												c2.close();
											}
											Log.d("tag", Integer
													.toString(mPlayOrder));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											if (type == 0) {
												String[] selectarg = new String[] { Long
														.toString(currSong
																.getID()) };
												c = resolver
														.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
																null,
																MediaStore.Audio.Media._ID
																		+ "=?",
																selectarg,
																MediaStore.Audio.Media.TITLE
																		+ " ASC");

											} else {
												String[] selectarg = new String[] { currSong
														.getTitle() };
												c = resolver
														.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
																null,
																MediaStore.Audio.Media.TITLE
																		+ "=?",
																selectarg,
																MediaStore.Audio.Media.TITLE
																		+ " ASC");

											}
											Log.d("tag", Integer.toString(c
													.getCount()));
											if (c.moveToFirst()) {
												Log.d("A.TAG",
														"Adding Songs to PlayList **");
												do {
													long id = c.getLong(c
															.getColumnIndex(MediaStore.Audio.Media._ID));
													ContentValues cv = new ContentValues();
													cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
															mPlayOrder++);
													cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
															id);
													Uri u = resolver.insert(
															playlists, cv);
													Log.d("A.TAG",
															"Added Playlist Item: "
																	+ u
																	+ " for AUDIO_ID "
																	+ id);
												} while (c.moveToNext());
											}
											c.close();

										}
										return true;
									}
								});
								popup1.show();
								break;
							case 6:
								ContentResolver resolver1 = context
										.getContentResolver();

								String[] proj1 = { MediaStore.Audio.Media._ID };
								String[] selectarg = new String[] { currSong
										.getTitle() };
								Cursor c = resolver1
										.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
												proj1,
												MediaStore.Audio.Media.TITLE
														+ "=?", selectarg,
												MediaStore.Audio.Media.TITLE
														+ " ASC");
								long songId = 0;
								if (c != null && c.getCount() > 0) {
									Log.d("tag", Integer.toString(c.getCount()));
									c.moveToFirst();
									do {
										songId = c.getLong(0);
									} while (c.moveToNext());
									c.close();
								}
								Log.d("tag", Long.toString(songId));
								Log.d("tag", Long.toString(bigplaylistId));
								int countDel = 0;

								Uri uri1 = MediaStore.Audio.Playlists.Members
										.getContentUri("external",
												bigplaylistId);
								String where = MediaStore.Audio.Playlists.Members.AUDIO_ID
										+ "=?";

								String audioId1 = Long.toString(songId);
								String[] whereVal = { audioId1 };
								Log.d("tag", "Uri : " + uri1.toString() + " "
										+ where + " " + whereVal[0]);
								countDel = resolver1.delete(uri1, where,
										whereVal);
								Log.d("TAG", "tracks deleted=" + countDel);
								break;
							case R.id.two:
								queueList = BridgeClass.instance().cache;
								queueList.add(new Song(currSong.getID(),
										currSong.getTitle(), currSong
												.getArtist(), currSong
												.getPath()));
								BridgeClass.instance().cache = queueList;
								Intent in1 = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);

								setResult(50, in1);
								break;
							case R.id.five:
								String path = currSong.getPath();
								int delcount = context
										.getContentResolver()
										.delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
												MediaStore.Audio.Media.DATA
														+ "='" + path + "'",
												null);
								Toast.makeText(context,
										delcount + " row deleted",
										Toast.LENGTH_SHORT).show();
								break;
							default:
								return false;
							}
							// Toast.makeText(context,"You Clicked : " +
							// item.getTitle(),Toast.LENGTH_SHORT).show();
							return true;
						}
					});
					popup.show();

				}
			});

			return songLay;
		}

	}

	public class AlbumAdapter extends BaseAdapter {

		private ArrayList<Album> albums;
		private LayoutInflater albumInf;
		Context context;

		public AlbumAdapter(Context c, ArrayList<Album> theAlbums) {
			albums = theAlbums;
			albumInf = LayoutInflater.from(c);
			context = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return albums.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			// map to album layout
			RelativeLayout albumLay = (RelativeLayout) albumInf.inflate(
					R.layout.album, parent, false);
			// get title and artist views
			TextView albumView = (TextView) albumLay
					.findViewById(R.id.album_title);
			TextView artistView = (TextView) albumLay
					.findViewById(R.id.album_artist);
			ImageView albumart = (ImageView) albumLay
					.findViewById(R.id.imageView1);

			// get album using position
			final Album currSong = albums.get(position);
			String path = currSong.getArt();
			Bitmap mbit;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 8;
			if (path != null) {
				mbit = BitmapFactory.decodeFile(currSong.getArt(), options);
				albumart.setImageBitmap(mbit);
			} else {
				mbit = BitmapFactory.decodeResource(getResources(),
						R.drawable.fallback_cover, options);
				albumart.setImageBitmap(mbit);
			}

			// get title and artist strings
			// Log.d("tag",Long.toString(currSong.getID())+" "+currSong.getTitle());
			albumView.setText(currSong.getTitle());
			artistView.setText(currSong.getArtist());
			// set position as tag
			albumLay.setTag(position);
			final Button button1 = (Button) albumLay
					.findViewById(R.id.button12);

			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					// Creating the instance of PopupMenu
					final PopupMenu popup = new PopupMenu(context, button1);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.popup_menualbum,
							popup.getMenu());
					popup.show();
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						int i;

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
							case R.id.addplay:
								i = 1;
								PopupMenu popup1 = new PopupMenu(context, v,
										Gravity.CENTER);
								popup1.getMenu().add(Menu.NONE, i++, Menu.NONE,
										"New PlayList");

								final ContentResolver resolver = context
										.getContentResolver();
								final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

								final Cursor musicCursor = resolver.query(uri,
										null, null, null, null);
								if (musicCursor != null
										&& musicCursor.moveToFirst()) {
									// get columns
									int titleColumn = musicCursor
											.getColumnIndex(MediaStore.Audio.Playlists.NAME);

									do {

										String thisTitle = musicCursor
												.getString(titleColumn);
										popup1.getMenu().add(Menu.NONE, i++,
												Menu.NONE, thisTitle);

									} while (musicCursor.moveToNext());
								}
								musicCursor.close();
								popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

									@Override
									public boolean onMenuItemClick(MenuItem item) {
										// TODO Auto-generated method stub
										Intent intent = item.getIntent();
										switch (item.getItemId()) {
										case 1:
											final Dialog dialog = new Dialog(
													context);

											// setting custom layout to dialog
											dialog.setContentView(R.layout.new_playlist);
											dialog.setTitle("New Playlist");
											dialog.show();
											// adding text dynamically
											final EditText txt = (EditText) dialog
													.findViewById(R.id.playlist_name);

											// adding button click event
											Button dismissButton = (Button) dialog
													.findViewById(R.id.cancel);
											dismissButton
													.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															dialog.dismiss();
														}
													});
											Button create = (Button) dialog
													.findViewById(R.id.createplaylist);
											create.setText("Create");
											create.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													ContentResolver resolver = context
															.getContentResolver();

													Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
													String[] proj = {
															MediaStore.Audio.Playlists._ID,
															MediaStore.Audio.Playlists.NAME };
													Log.d("A.TAG",
															"Checking for existing playlist for "
																	+ txt.getText());
													Cursor c = resolver.query(
															playlists, proj,
															null, null, null);
													long playlistId = 0;
													if (c != null
															&& c.getCount() > 0) {
														// Log.d("tag",Integer.toString(c.getCount()));
														c.moveToFirst();
														do {
															String plname = c
																	.getString(c
																			.getColumnIndex(MediaStore.Audio.Playlists.NAME));
															if (plname.equals(txt
																	.getText())) {
																playlistId = c
																		.getLong(c
																				.getColumnIndex(MediaStore.Audio.Playlists._ID));
																break;
															}
														} while (c.moveToNext());
														c.close();
													}
													if (playlistId != 0) {
														Uri deleteUri = ContentUris
																.withAppendedId(
																		playlists,
																		playlistId);
														Log.d("A.TAG",
																"REMOVING Existing Playlist: "
																		+ playlistId);

														// delete the playlist
														resolver.delete(
																deleteUri,
																null, null);
													}

													Log.d("A.TAG",
															"CREATING PLAYLIST: "
																	+ txt.getText());
													ContentValues v1 = new ContentValues();

													v1.put(MediaStore.Audio.Playlists.NAME,
															txt.getText()
																	.toString());

													v1.put(MediaStore.Audio.Playlists.DATE_MODIFIED,
															System.currentTimeMillis());
													Uri newpl = resolver
															.insert(playlists,
																	v1);
													Log.d("A.TAG",
															"Added PlayLIst: "
																	+ newpl);
													Uri insUri = Uri
															.withAppendedPath(
																	newpl,
																	MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);

													int order = 1;
													String[] selectarg = new String[] { currSong
															.getTitle() };
													Log.d("A.TAG",
															"Playlist Members Url: "
																	+ insUri);
													c = resolver
															.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
																	null,
																	MediaStore.Audio.Albums.ALBUM
																			+ "=?",
																	selectarg,
																	MediaStore.Audio.Albums.ALBUM
																			+ " ASC");
													if (c.moveToFirst()) {
														Log.d("A.TAG",
																"Adding Songs to PlayList **");
														do {
															long id = c.getLong(c
																	.getColumnIndex(MediaStore.Audio.Media._ID));
															ContentValues cv = new ContentValues();
															cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
																	order++);
															cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
																	id);
															Uri u = resolver
																	.insert(insUri,
																			cv);
															Log.d("A.TAG",
																	"Added Playlist Item: "
																			+ u
																			+ " for AUDIO_ID "
																			+ id);
														} while (c.moveToNext());
													}
													c.close();

													dialog.dismiss();

												}
											});

											break;

										default:
											Toast.makeText(
													context,
													"You Clicked : "
															+ item.getTitle(),
													Toast.LENGTH_SHORT).show();
											Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
											String[] proj = {
													MediaStore.Audio.Playlists._ID,
													MediaStore.Audio.Playlists.NAME };
											Cursor c = resolver.query(
													playlists, proj, null,
													null, null);
											long playlistId = 0;
											if (c != null && c.getCount() > 0) {
												Log.d("tag", Integer.toString(c
														.getCount()));
												c.moveToFirst();
												do {
													String plname = c.getString(c
															.getColumnIndex(MediaStore.Audio.Playlists.NAME));
													if (plname.equals(item
															.getTitle())) {
														playlistId = c.getLong(c
																.getColumnIndex(MediaStore.Audio.Playlists._ID));
														break;
													}
												} while (c.moveToNext());
												c.close();
											}
											Log.d("tag",
													Long.toString(playlistId));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// Projection to get high water mark
											// of PLAY_ORDER in a particular
											// playlist
											final String[] PROJECTION_PLAYLISTMEMBERS_PLAYORDER = new String[] {
													MediaStore.Audio.Playlists.Members._ID,
													MediaStore.Audio.Playlists.Members.PLAY_ORDER };
											// Projection to get the list of
											// song IDs to be added to a
											// playlist
											final String[] PROJECTION_SONGS_ADDTOPLAYLIST = new String[] { MediaStore.Audio.Media._ID, };
											Cursor c2 = resolver
													.query(playlists,
															PROJECTION_PLAYLISTMEMBERS_PLAYORDER,
															null,
															null,
															MediaStore.Audio.Playlists.Members.PLAY_ORDER
																	+ " DESC ");
											int mPlayOrder = 1;
											if (c2 != null) {
												if (c2.moveToFirst()) {
													mPlayOrder = (c2.getInt(c2
															.getColumnIndex(MediaStore.Audio.Playlists.Members.PLAY_ORDER))) + 1;
												}
												c2.close();
											}
											Log.d("tag", Integer
													.toString(mPlayOrder));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// if(type==0){
											String[] selectarg = new String[] { currSong
													.getTitle() };
											Log.d("tag", currSong.getTitle());
											c = resolver
													.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
															null,
															MediaStore.Audio.Albums.ALBUM
																	+ "=?",
															selectarg,
															MediaStore.Audio.Albums.ALBUM
																	+ " ASC");

											Log.d("tag", Integer.toString(c
													.getCount()));
											if (c.moveToFirst()) {
												Log.d("A.TAG",
														"Adding Songs to PlayList **");
												do {
													long id = c.getLong(c
															.getColumnIndex(MediaStore.Audio.Media._ID));
													ContentValues cv = new ContentValues();
													cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
															mPlayOrder++);
													cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
															id);
													Uri u = resolver.insert(
															playlists, cv);
													Log.d("A.TAG",
															"Added Playlist Item: "
																	+ u
																	+ " for AUDIO_ID "
																	+ id);
												} while (c.moveToNext());
											}
											c.close();

										}
										return true;
									}
								});
								popup1.show();
								break;

							case R.id.play:

								Log.d("tag", currSong.getTitle());
								String select = MediaStore.Audio.Media.ALBUM
										+ "=?";
								SongsManager s = new SongsManager(
										getApplicationContext());
								ArrayList<Song> songList = s.getSongList(3, 0,
										currSong.getTitle());
								final int songIndex = 0;

								// Starting new intent

								queueList = songList;
								BridgeClass.instance().cache = queueList;
								Intent in = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);
								// Sending songIndex to PlayerActivity
								in.putExtra("songIndex", songIndex);

								setResult(100, in);
								finish();
								break;
							case R.id.addqueue:
								String slct = MediaStore.Audio.Albums.ALBUM
										+ "=?";
								SongsManager s1 = new SongsManager(
										getApplicationContext());
								queueList = BridgeClass.instance().cache;

								ArrayList<Song> temp = s1.getSongList(3, 0,
										currSong.getTitle());
								queueList.addAll(temp);
								BridgeClass.instance().cache = queueList;
								Intent in1 = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);

								setResult(50, in1);
							}
							return true;
						}
					});

				}
			});

			return albumLay;
		}

	}

	public class PlaylistAdapter extends BaseAdapter {
		private ArrayList<Playlist> plays = new ArrayList<Playlist>();;
		private ArrayList<Song> songList12 = new ArrayList<Song>();;
		private ArrayList<Song> queueList12 = new ArrayList<Song>();;
		private LayoutInflater playInf;
		LinearLayout playLay;
		Button button1;
		Context context;
		private Activity activity;
		String popUpContents[];
		PopupWindow poof;
		String name = null;

		public PlaylistAdapter(Context c, ArrayList<Playlist> theplays) {

			plays = theplays;
			playInf = LayoutInflater.from(c);
			context = c;
			Log.v("LOGGING_TAG123", Integer.toString(plays.size()));

		}

		public PlaylistAdapter(Activity imagebinding) {
			// TODO Auto-generated constructor stub

			activity = imagebinding;

			context = imagebinding;
			playInf = LayoutInflater.from(imagebinding);
			context = imagebinding;
			playInf = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return plays.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// playLay = (LinearLayout) playInf.inflate(R.layout.playlist,
			// parent, false);
			List<String> popupList = new ArrayList<String>();
			popupList.add("Play");
			popupList.add("Add to Play Queue");
			popupList.add("Rename");
			popupList.add("Delete");

			// convert to simple array
			popUpContents = new String[popupList.size()];
			popupList.toArray(popUpContents);

			/*
			 * initialize pop up window
			 */

			convertView = playInf.inflate(R.layout.playlist, null);
			// get title and artist views
			TextView playView = (TextView) convertView
					.findViewById(R.id.textView123);
			final Playlist currplay = plays.get(position);
			// get title and artist strings
			// currplay.getTitle()
			Log.v("LOGGING_TAG123", "iN HERE");
			playView.setText(currplay.getTitle());
			Button button1 = (Button) convertView.findViewById(R.id.playbutton);
			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// poof = poof(name);
					final PopupWindow popupWindow = new PopupWindow(context);

					// the drop down list is a list view
					ListView templist = new ListView(context);

					// set our adapter and pass our pop up window contents
					templist.setAdapter(PlayAdapter(popUpContents));

					// set the item click listener
					templist.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							// get the context and main activity to access
							// variables

							// Log.v("order","in here bhetor"+currplay.getTitle());
							// add some animation when a list item was clicked
							Animation fadeInAnimation = AnimationUtils
									.loadAnimation(context,
											android.R.anim.fade_in);
							fadeInAnimation.setDuration(10);
							view.startAnimation(fadeInAnimation);

							// dismiss the pop up
							popupWindow.dismiss();

							// get the text and set it as the button text

							// Toast.makeText(context, "Selected Positon is: " +
							// position, 100).show();
							switch (position) {
							case 0:

								Log.d("tagpod", Long.toString(currplay.getID()));
								SongsManager sm = new SongsManager(
										getApplicationContext());
								songList12 = sm.getSongList(2,
										currplay.getID(), "");

								queueList12 = songList12;
								BridgeClass.instance().cache = queueList12;
								Intent in1 = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);
								in1.putExtra("songIndex", 0);
								setResult(100, in1);
								finish();
								break;
							case 1:
								queueList = BridgeClass.instance().cache;
								SongsManager sm1 = new SongsManager(
										getApplicationContext());
								songList12 = sm1.getSongList(2,
										currplay.getID(), "");

								queueList.addAll(songList12);
								BridgeClass.instance().cache = queueList;
								Intent in = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);

								setResult(50, in);
								break;
							case 2:
								final ContentResolver resolver = context
										.getContentResolver();
								final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
								String[] proj = {
										MediaStore.Audio.Playlists._ID,
										MediaStore.Audio.Playlists.NAME };
								Cursor c = resolver.query(uri, proj, null,
										null, null);
								long playlistId = 0;
								if (c != null && c.getCount() > 0) {
									// Log.d("tag",Integer.toString(c.getCount()));
									c.moveToFirst();
									do {
										String plname = c.getString(c
												.getColumnIndex(MediaStore.Audio.Playlists.NAME));
										if (plname.equals(currplay.getTitle())) {
											playlistId = c.getLong(c
													.getColumnIndex(MediaStore.Audio.Playlists._ID));
											break;
										}
									} while (c.moveToNext());
									c.close();
								}
								final Dialog dialog = new Dialog(context);
								// setting custom layout to dialog
								dialog.setContentView(R.layout.new_playlist);
								dialog.setTitle("Rename Playlist");
								dialog.show();
								final EditText txt = (EditText) dialog
										.findViewById(R.id.playlist_name);
								txt.setText(currplay.getTitle());
								final long playlist_Id = playlistId;
								// adding button click event
								Button dismissButton = (Button) dialog
										.findViewById(R.id.cancel);
								dismissButton
										.setOnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												dialog.dismiss();
											}
										});
								Button create = (Button) dialog
										.findViewById(R.id.createplaylist);
								create.setText("Rename");
								create.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {

										ContentValues values = new ContentValues();
										String where = MediaStore.Audio.Playlists._ID
												+ " =? ";
										String[] whereVal = { Long
												.toString(playlist_Id) };
										values.put(
												MediaStore.Audio.Playlists.NAME,
												txt.getText().toString());
										resolver.update(uri, values, where,
												whereVal);
										dialog.dismiss();
									}
								});

								break;
							case 3:
								ContentResolver resolver1 = context
										.getContentResolver();
								final Uri uri1 = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
								String[] proj1 = {
										MediaStore.Audio.Playlists._ID,
										MediaStore.Audio.Playlists.NAME };
								Cursor c1 = resolver1.query(uri1, proj1, null,
										null, null);
								long playlistId1 = 0;
								if (c1 != null && c1.getCount() > 0) {
									// Log.d("tag",Integer.toString(c.getCount()));
									c1.moveToFirst();
									do {
										String plname = c1.getString(c1
												.getColumnIndex(MediaStore.Audio.Playlists.NAME));
										if (plname.equals(currplay.getTitle())) {
											playlistId1 = c1.getLong(c1
													.getColumnIndex(MediaStore.Audio.Playlists._ID));
											break;
										}
									} while (c1.moveToNext());
									c1.close();
								}
								Log.v("testing", Long.toString(playlistId1));
								String where = MediaStore.Audio.Playlists._ID
										+ "=?";
								String[] whereVal = { Long
										.toString(playlistId1) };
								int no = resolver1
										.delete(MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI,
												where, whereVal);
								Toast toast = Toast.makeText(context, no
										+ " Deleted", Toast.LENGTH_SHORT);
								toast.show();
							}

						}

					});

					// some other visual settings
					popupWindow.setFocusable(true);
					popupWindow.setWidth(350);
					popupWindow
							.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

					// set the list view as pop up window content
					popupWindow.setContentView(templist);

					popupWindow.showAsDropDown(v, -5, 0);

				}
			});
			convertView.setTag(position);
			return convertView;
		}

		private ArrayAdapter<String> PlayAdapter(String playArray[]) {

			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					android.R.layout.simple_list_item_1, playArray) {

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {

					// setting the ID and text for every items in the list

					String text = getItem(position);

					// visual settings for the list item
					TextView listItem = new TextView(context);

					listItem.setText(text);
					listItem.setTag(position);
					listItem.setTextSize(22);
					listItem.setPadding(10, 10, 10, 10);
					listItem.setTextColor(Color.WHITE);
					listItem.setLines(1);
					return listItem;
				}
			};

			return adapter;
		}

	}

	public class genreAdapter extends BaseAdapter {

		private ArrayList<Genre> genres;
		private LayoutInflater genreInf;
		Context context;

		public genreAdapter(Context c, ArrayList<Genre> thegenres) {
			genres = thegenres;
			genreInf = LayoutInflater.from(c);
			context = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return genres.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			// map to genre layout
			LinearLayout genreLay = (LinearLayout) genreInf.inflate(
					R.layout.genre, parent, false);
			// get title and artist views
			TextView genreView = (TextView) genreLay
					.findViewById(R.id.genre_title);
			TextView artistView = (TextView) genreLay
					.findViewById(R.id.genre_noofsongs);
			// get genre using position
			final Genre currSong = genres.get(position);
			// get title and artist strings
			// Log.d("tag",Long.toString(currSong.getID())+" "+currSong.getTitle());
			genreView.setText(currSong.getTitle());
			/*
			 * if (currSong.getCount() == 1)
			 * artistView.setText(String.valueOf(currSong.getCount()) +
			 * " song"); else
			 * artistView.setText(String.valueOf(currSong.getCount()) +
			 * " songs");
			 */
			// set position as tag
			genreLay.setTag(position);
			final Button button1 = (Button) genreLay
					.findViewById(R.id.button12);

			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					// Creating the instance of PopupMenu
					final PopupMenu popup = new PopupMenu(context, button1);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.popup_menualbum,
							popup.getMenu());
					popup.show();
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						int i;

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
							case R.id.addplay:
								i = 1;
								PopupMenu popup1 = new PopupMenu(context, v,
										Gravity.CENTER);
								popup1.getMenu().add(Menu.NONE, i++, Menu.NONE,
										"New PlayList");

								final ContentResolver resolver = context
										.getContentResolver();
								final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

								final Cursor musicCursor = resolver.query(uri,
										null, null, null, null);
								if (musicCursor != null
										&& musicCursor.moveToFirst()) {
									// get columns
									int titleColumn = musicCursor
											.getColumnIndex(MediaStore.Audio.Playlists.NAME);

									do {

										String thisTitle = musicCursor
												.getString(titleColumn);
										popup1.getMenu().add(Menu.NONE, i++,
												Menu.NONE, thisTitle);

									} while (musicCursor.moveToNext());
								}
								musicCursor.close();
								popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

									@Override
									public boolean onMenuItemClick(MenuItem item) {
										// TODO Auto-generated method stub
										Intent intent = item.getIntent();
										switch (item.getItemId()) {
										case 1:
											final Dialog dialog = new Dialog(
													context);

											// setting custom layout to dialog
											dialog.setContentView(R.layout.new_playlist);
											dialog.setTitle("New Playlist");
											dialog.show();
											// adding text dynamically
											final EditText txt = (EditText) dialog
													.findViewById(R.id.playlist_name);

											// adding button click event
											Button dismissButton = (Button) dialog
													.findViewById(R.id.cancel);
											dismissButton
													.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															dialog.dismiss();
														}
													});
											Button create = (Button) dialog
													.findViewById(R.id.createplaylist);
											create.setText("Create");
											create.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													ContentResolver resolver = context
															.getContentResolver();

													Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
													String[] proj = {
															MediaStore.Audio.Playlists._ID,
															MediaStore.Audio.Playlists.NAME };
													Log.d("A.TAG",
															"Checking for existing playlist for "
																	+ txt.getText());
													Cursor c = resolver.query(
															playlists, proj,
															null, null, null);
													long playlistId = 0;
													if (c != null
															&& c.getCount() > 0) {
														// Log.d("tag",Integer.toString(c.getCount()));
														c.moveToFirst();
														do {
															String plname = c
																	.getString(c
																			.getColumnIndex(MediaStore.Audio.Playlists.NAME));
															if (plname.equals(txt
																	.getText())) {
																playlistId = c
																		.getLong(c
																				.getColumnIndex(MediaStore.Audio.Playlists._ID));
																break;
															}
														} while (c.moveToNext());
														c.close();
													}
													if (playlistId != 0) {
														Uri deleteUri = ContentUris
																.withAppendedId(
																		playlists,
																		playlistId);
														Log.d("A.TAG",
																"REMOVING Existing Playlist: "
																		+ playlistId);

														// delete the playlist
														resolver.delete(
																deleteUri,
																null, null);
													}

													Log.d("A.TAG",
															"CREATING PLAYLIST: "
																	+ txt.getText());
													ContentValues v1 = new ContentValues();

													v1.put(MediaStore.Audio.Playlists.NAME,
															txt.getText()
																	.toString());

													v1.put(MediaStore.Audio.Playlists.DATE_MODIFIED,
															System.currentTimeMillis());
													Uri newpl = resolver
															.insert(playlists,
																	v1);
													Log.d("A.TAG",
															"Added PlayLIst: "
																	+ newpl);
													Uri insUri = Uri
															.withAppendedPath(
																	newpl,
																	MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);

													int order = 1;

													Log.d("A.TAG",
															"Playlist Members Url: "
																	+ insUri);
													c = resolver
															.query(android.provider.MediaStore.Audio.Genres.Members
																	.getContentUri(
																			"external",
																			currSong.getID()),
																	null, null,
																	null, null);
													if (c.moveToFirst()) {
														Log.d("A.TAG",
																"Adding Songs to PlayList **"
																		+ c.getCount());
														do {
															long id = c.getLong(c
																	.getColumnIndex(MediaStore.Audio.Media._ID));
															ContentValues cv = new ContentValues();
															cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
																	order++);
															cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
																	id);
															Uri u = resolver
																	.insert(insUri,
																			cv);
															Log.d("A.TAG",
																	"Added Playlist Item: "
																			+ u
																			+ " for AUDIO_ID "
																			+ id);
														} while (c.moveToNext());
													}
													c.close();

													dialog.dismiss();

												}
											});

											break;

										default:
											Toast.makeText(
													context,
													"You Clicked : "
															+ item.getTitle(),
													Toast.LENGTH_SHORT).show();
											Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
											String[] proj = {
													MediaStore.Audio.Playlists._ID,
													MediaStore.Audio.Playlists.NAME };
											Cursor c = resolver.query(
													playlists, proj, null,
													null, null);
											long playlistId = 0;
											if (c != null && c.getCount() > 0) {
												Log.d("tag", Integer.toString(c
														.getCount()));
												c.moveToFirst();
												do {
													String plname = c.getString(c
															.getColumnIndex(MediaStore.Audio.Playlists.NAME));
													if (plname.equals(item
															.getTitle())) {
														playlistId = c.getLong(c
																.getColumnIndex(MediaStore.Audio.Playlists._ID));
														break;
													}
												} while (c.moveToNext());
												c.close();
											}
											Log.d("tag",
													Long.toString(playlistId));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// Projection to get high water mark
											// of PLAY_ORDER in a particular
											// playlist
											final String[] PROJECTION_PLAYLISTMEMBERS_PLAYORDER = new String[] {
													MediaStore.Audio.Playlists.Members._ID,
													MediaStore.Audio.Playlists.Members.PLAY_ORDER };
											// Projection to get the list of
											// song IDs to be added to a
											// playlist
											final String[] PROJECTION_SONGS_ADDTOPLAYLIST = new String[] { MediaStore.Audio.Media._ID, };
											Cursor c2 = resolver
													.query(playlists,
															PROJECTION_PLAYLISTMEMBERS_PLAYORDER,
															null,
															null,
															MediaStore.Audio.Playlists.Members.PLAY_ORDER
																	+ " DESC ");
											int mPlayOrder = 1;
											if (c2 != null) {
												if (c2.moveToFirst()) {
													mPlayOrder = (c2.getInt(c2
															.getColumnIndex(MediaStore.Audio.Playlists.Members.PLAY_ORDER))) + 1;
												}
												c2.close();
											}
											Log.d("tag", Integer
													.toString(mPlayOrder));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// if(type==0){
											c = resolver
													.query(android.provider.MediaStore.Audio.Genres.Members
															.getContentUri(
																	"external",
																	currSong.getID()),
															null, null, null,
															null);

											Log.d("tag", Integer.toString(c
													.getCount()));
											if (c.moveToFirst()) {
												Log.d("A.TAG",
														"Adding Songs to PlayList **");
												do {
													long id = c.getLong(c
															.getColumnIndex(MediaStore.Audio.Media._ID));
													ContentValues cv = new ContentValues();
													cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
															mPlayOrder++);
													cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
															id);
													Uri u = resolver.insert(
															playlists, cv);
													Log.d("A.TAG",
															"Added Playlist Item: "
																	+ u
																	+ " for AUDIO_ID "
																	+ id);
												} while (c.moveToNext());
											}
											c.close();

										}
										return true;

									}
								});
								popup1.show();
								break;

							case R.id.play:
								SongsManager a = new SongsManager(
										getApplicationContext());
								songList = a.getSongList(4, currSong.getID(),
										"");

								final int songIndex = 0;

								// Starting new intent

								queueList = songList;
								BridgeClass.instance().cache = queueList;
								Intent in = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);
								// Sending songIndex to PlayerActivity
								in.putExtra("songIndex", songIndex);

								setResult(100, in);
								finish();
								break;
							case R.id.addqueue:

								queueList = BridgeClass.instance().cache;
								SongsManager a1 = new SongsManager(
										getApplicationContext());
								songList = a1.getSongList(4, currSong.getID(),
										"");
								queueList.addAll(songList);
								BridgeClass.instance().cache = queueList;
								Intent in1 = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);

								setResult(50, in1);
							}
							return true;
						}
					});

				}
			});
			return genreLay;

		}

	}

	public class ArtistAdapter extends BaseAdapter {

		private ArrayList<Artist> artists;
		private LayoutInflater artistInf;
		private ArrayList<Song> song;
		Context context;

		public ArtistAdapter(Context c, ArrayList<Artist> theartists) {
			artists = theartists;
			artistInf = LayoutInflater.from(c);
			context = c;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return artists.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return artists.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			// map to artist layout

			LinearLayout artistLay = (LinearLayout) artistInf.inflate(
					R.layout.artist, parent, false);
			// get title and artist views
			TextView artistView = (TextView) artistLay
					.findViewById(R.id.artist_title);
			TextView artistView1 = (TextView) artistLay
					.findViewById(R.id.artist_noofsongs);
			// get artist using position
			final Artist currSong = artists.get(position);
			// get title and artist strings
			// Log.d("tag",Long.toString(currSong.getID())+" "+currSong.getTitle());
			artistView.setText(currSong.getTitle());
			if (currSong.getCountAlbum() == 1)
				artistView1.setText(String.valueOf(currSong.getCountAlbum())
						+ " album");
			else
				artistView1.setText(String.valueOf(currSong.getCountAlbum())
						+ " albums");
			// set position as tag
			// BridgeClass.instance().cache1=currSong.getTitle();
			artistLay.setTag(position);
			final Button button1 = (Button) artistLay
					.findViewById(R.id.button12);

			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(final View v) {
					// TODO Auto-generated method stub
					// Creating the instance of PopupMenu
					final PopupMenu popup = new PopupMenu(context, button1);
					// Inflating the Popup using xml file
					popup.getMenuInflater().inflate(R.menu.popup_menualbum,
							popup.getMenu());
					popup.show();
					popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						int i;

						@Override
						public boolean onMenuItemClick(MenuItem item) {
							// TODO Auto-generated method stub
							switch (item.getItemId()) {
							case R.id.addplay:
								i = 1;
								PopupMenu popup1 = new PopupMenu(context, v,
										Gravity.CENTER);
								popup1.getMenu().add(Menu.NONE, i++, Menu.NONE,
										"New PlayList");

								final ContentResolver resolver = context
										.getContentResolver();
								final Uri uri = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;

								final Cursor musicCursor = resolver.query(uri,
										null, null, null, null);
								if (musicCursor != null
										&& musicCursor.moveToFirst()) {
									// get columns
									int titleColumn = musicCursor
											.getColumnIndex(MediaStore.Audio.Playlists.NAME);

									do {

										String thisTitle = musicCursor
												.getString(titleColumn);
										popup1.getMenu().add(Menu.NONE, i++,
												Menu.NONE, thisTitle);

									} while (musicCursor.moveToNext());
								}
								musicCursor.close();
								popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

									@Override
									public boolean onMenuItemClick(MenuItem item) {
										// TODO Auto-generated method stub

										switch (item.getItemId()) {
										case 1:
											final Dialog dialog = new Dialog(
													context);

											// setting custom layout to dialog
											dialog.setContentView(R.layout.new_playlist);
											dialog.setTitle("New Playlist");
											dialog.show();
											// adding text dynamically
											final EditText txt = (EditText) dialog
													.findViewById(R.id.playlist_name);

											// adding button click event
											Button dismissButton = (Button) dialog
													.findViewById(R.id.cancel);
											dismissButton
													.setOnClickListener(new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															dialog.dismiss();
														}
													});
											Button create = (Button) dialog
													.findViewById(R.id.createplaylist);
											create.setText("Create");
											create.setOnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													ContentResolver resolver = context
															.getContentResolver();

													Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
													String[] proj = {
															MediaStore.Audio.Playlists._ID,
															MediaStore.Audio.Playlists.NAME };
													Log.d("A.TAG",
															"Checking for existing playlist for "
																	+ txt.getText());
													Cursor c = resolver.query(
															playlists, proj,
															null, null, null);
													long playlistId = 0;
													if (c != null
															&& c.getCount() > 0) {
														// Log.d("tag",Integer.toString(c.getCount()));
														c.moveToFirst();
														do {
															String plname = c
																	.getString(c
																			.getColumnIndex(MediaStore.Audio.Playlists.NAME));
															if (plname.equals(txt
																	.getText())) {
																playlistId = c
																		.getLong(c
																				.getColumnIndex(MediaStore.Audio.Playlists._ID));
																break;
															}
														} while (c.moveToNext());
														c.close();
													}
													if (playlistId != 0) {
														Uri deleteUri = ContentUris
																.withAppendedId(
																		playlists,
																		playlistId);
														Log.d("A.TAG",
																"REMOVING Existing Playlist: "
																		+ playlistId);

														// delete the playlist
														resolver.delete(
																deleteUri,
																null, null);
													}

													Log.d("A.TAG",
															"CREATING PLAYLIST: "
																	+ txt.getText());
													ContentValues v1 = new ContentValues();

													v1.put(MediaStore.Audio.Playlists.NAME,
															txt.getText()
																	.toString());

													v1.put(MediaStore.Audio.Playlists.DATE_MODIFIED,
															System.currentTimeMillis());
													Uri newpl = resolver
															.insert(playlists,
																	v1);
													Log.d("A.TAG",
															"Added PlayLIst: "
																	+ newpl);
													Uri insUri = Uri
															.withAppendedPath(
																	newpl,
																	MediaStore.Audio.Playlists.Members.CONTENT_DIRECTORY);

													int order = 1;

													Log.d("A.TAG",
															"Playlist Members Url: "
																	+ insUri);
													c = resolver
															.query(android.provider.MediaStore.Audio.Artists.Albums
																	.getContentUri(
																			"external",
																			currSong.getID()),
																	null, null,
																	null, null);
													if (c.moveToFirst()) {
														Log.d("A.TAG",
																"Adding Songs to PlayList **"
																		+ c.getCount());
														do {
															long id = c.getLong(c
																	.getColumnIndex(MediaStore.Audio.Media._ID));
															ContentValues cv = new ContentValues();
															cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
																	order++);
															cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
																	id);
															Uri u = resolver
																	.insert(insUri,
																			cv);
															Log.d("A.TAG",
																	"Added Playlist Item: "
																			+ u
																			+ " for AUDIO_ID "
																			+ id);
														} while (c.moveToNext());
													}
													c.close();

													dialog.dismiss();

												}
											});

											break;

										default:
											Toast.makeText(
													context,
													"You Clicked : "
															+ item.getTitle(),
													Toast.LENGTH_SHORT).show();
											Uri playlists = MediaStore.Audio.Playlists.EXTERNAL_CONTENT_URI;
											String[] proj = {
													MediaStore.Audio.Playlists._ID,
													MediaStore.Audio.Playlists.NAME };
											Cursor c = resolver.query(
													playlists, proj, null,
													null, null);
											long playlistId = 0;
											if (c != null && c.getCount() > 0) {
												Log.d("tag", Integer.toString(c
														.getCount()));
												c.moveToFirst();
												do {
													String plname = c.getString(c
															.getColumnIndex(MediaStore.Audio.Playlists.NAME));
													if (plname.equals(item
															.getTitle())) {
														playlistId = c.getLong(c
																.getColumnIndex(MediaStore.Audio.Playlists._ID));
														break;
													}
												} while (c.moveToNext());
												c.close();
											}
											Log.d("tag",
													Long.toString(playlistId));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// Projection to get high water mark
											// of PLAY_ORDER in a particular
											// playlist
											final String[] PROJECTION_PLAYLISTMEMBERS_PLAYORDER = new String[] {
													MediaStore.Audio.Playlists.Members._ID,
													MediaStore.Audio.Playlists.Members.PLAY_ORDER };
											// Projection to get the list of
											// song IDs to be added to a
											// playlist
											final String[] PROJECTION_SONGS_ADDTOPLAYLIST = new String[] { MediaStore.Audio.Media._ID, };
											Cursor c2 = resolver
													.query(playlists,
															PROJECTION_PLAYLISTMEMBERS_PLAYORDER,
															null,
															null,
															MediaStore.Audio.Playlists.Members.PLAY_ORDER
																	+ " DESC ");
											int mPlayOrder = 1;
											if (c2 != null) {
												if (c2.moveToFirst()) {
													mPlayOrder = (c2.getInt(c2
															.getColumnIndex(MediaStore.Audio.Playlists.Members.PLAY_ORDER))) + 1;
												}
												c2.close();
											}
											Log.d("tag", Integer
													.toString(mPlayOrder));
											playlists = MediaStore.Audio.Playlists.Members
													.getContentUri("external",
															playlistId);
											// if(type==0){

											c = resolver
													.query(android.provider.MediaStore.Audio.Artists.Albums
															.getContentUri(
																	"external",
																	currSong.getID()),
															null, null, null,
															null);

											Log.d("tag", Integer.toString(c
													.getCount()));
											if (c.moveToFirst()) {
												Log.d("A.TAG",
														"Adding Songs to PlayList **");
												do {
													long id = c.getLong(c
															.getColumnIndex(MediaStore.Audio.Media._ID));
													ContentValues cv = new ContentValues();
													cv.put(MediaStore.Audio.Playlists.Members.PLAY_ORDER,
															mPlayOrder++);
													cv.put(MediaStore.Audio.Playlists.Members.AUDIO_ID,
															id);
													Uri u = resolver.insert(
															playlists, cv);
													Log.d("A.TAG",
															"Added Playlist Item: "
																	+ u
																	+ " for AUDIO_ID "
																	+ id);
												} while (c.moveToNext());
											}
											c.close();

										}
										return true;

									}
								});
								popup1.show();
								break;

							case R.id.play:

								SongsManager p = new SongsManager(
										getApplicationContext());
								song = p.getSongList(5, 0, currSong.getTitle());

								final int songIndex = 0;

								// Starting new intent

								queueList = song;
								BridgeClass.instance().cache = queueList;
								Intent in = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);
								// Sending songIndex to PlayerActivity
								in.putExtra("songIndex", songIndex);

								setResult(100, in);
								finish();
								break;
							case R.id.addqueue:

								queueList = BridgeClass.instance().cache;

								SongsManager p1 = new SongsManager(
										getApplicationContext());
								song = p1.getSongList(5, 0, currSong.getTitle());
								queueList.addAll(song);
								BridgeClass.instance().cache = queueList;
								Intent in1 = new Intent(
										context,
										AndroidBuildingMusicPlayerActivity.class);

								setResult(50, in1);
							}
							return true;
						}
					});

				}
			});
			return artistLay;

		}

	}
	 @Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.about_us) {
				Intent i=new Intent("com.example.playlistplay.ABOUTUS");
				startActivity(i);
			}
			if(id == R.id.exit){
				finish();
			}
			if (id == R.id.queue) {
				Intent i=new Intent("com.example.playlistplay.QUEUE");
				startActivity(i);
			}
			return super.onOptionsItemSelected(item);
		}

}
