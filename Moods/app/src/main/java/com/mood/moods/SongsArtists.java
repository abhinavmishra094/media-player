package com.mood.moods;

import android.content.Intent;
import android.database.Cursor;
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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongsArtists extends AppCompatActivity {

    ListView songs;
    static ArrayList<HashMap<String, String>> playlis;
    Intent in;
    PlayerActivity pa;
    String artistName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_artists);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        in = getIntent();
        pa = new PlayerActivity();
        artistName = in.getStringExtra("Artists");
        playlis = new ArrayList<>();
        getPlaylist();
        songs = (ListView) findViewById(R.id.listView);
        ListAdapter adapter = new SongsArtistsCustomAdapter(this, playlis);
        songs.setAdapter(adapter);

        songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent  in = new Intent(getApplicationContext(), PlayerActivity.class);
                in.putExtra("position", position);
                startActivity(in);
                pa.playlist = playlis;
            }
        });
    }

    ArrayList<HashMap<String, String>> getPlaylist(){
        ArrayList<String> songs = new ArrayList<String>();
        Cursor mCursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DURATION}, null, null,
                "LOWER(" + MediaStore.Audio.Media.DISPLAY_NAME + ")ASC");

        int i = 0;
        if(mCursor.moveToFirst()){
            do {
                HashMap<String, String> abcd = new HashMap<>();
                String name = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                if(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)).equals(artistName)){
                    if(name.length()>20){
                        abcd.put("Name", name.substring(0, 20)+"...");
                    }else{
                        abcd.put("Name", name);
                    }
                    abcd.put("Path", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                    abcd.put("Duration", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    playlis.add(abcd);
                }
                i++;
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return playlis;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
