package com.mood.moods;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class SongAlbums extends AppCompatActivity {


    ImageView albumArt;
    ListView songs;
    ArrayList<HashMap<String, String>> playlist;
    String album;
    FloatingActionButton playBtn;
    Intent in;
    PlayerActivity pa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_albums);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pa = new PlayerActivity();
        in = getIntent();
        album = in.getStringExtra("Albums");

        playlist = new ArrayList<>();
        getPlaylist();
        albumArt = (ImageView) findViewById(R.id.imageView3);
        songs = (ListView) findViewById(R.id.listView2);
        playBtn = (FloatingActionButton) findViewById(R.id.albumPlaylist);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  in = new Intent(SongAlbums.this, PlayerActivity.class);
                in.putExtra("position", 0);
                startActivity(in);
                pa.playlist = playlist;
            }
        });

        if(in.getStringExtra("Images") == null)
            albumArt.setImageResource(R.drawable.no_cover);
        else
            albumArt.setImageDrawable(Drawable.createFromPath(in.getStringExtra("Images")));
        ListAdapter adapter = new SongsArtistsCustomAdapter(this, playlist);
        songs.setAdapter(adapter);

        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  in = new Intent(SongAlbums.this, PlayerActivity.class);
                in.putExtra("position", position);
                startActivity(in);
                pa.playlist = playlist;
            }
        });
    }

    ArrayList<HashMap<String,String>> getPlaylist(){
        Cursor mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DATA}, null, null,
                "LOWER(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");
        int i = 0;

        if (mCursor.moveToFirst()){
            do{
                String name = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                HashMap<String, String> abcd = new HashMap<>();
                if(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)).equals(album)){
                    if(name.length()>20){
                        abcd.put("Name", name.substring(0, 20)+"...");
                    }else{
                        abcd.put("Name", name);
                    }
                    abcd.put("Duration", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    abcd.put("Path", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));

                    playlist.add(abcd);
                }
                i++;
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return playlist;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
