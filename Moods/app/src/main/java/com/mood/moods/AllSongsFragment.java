package com.mood.moods;

import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AllSongsFragment extends Fragment {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    TextView noSongs;
    ListView allSongs;
    static ArrayList<HashMap<String, String>> songsDetails;
    PlayerActivity pa;
    public AllSongsFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_all_songs_fragment, container, false);

        pref = getActivity().getSharedPreferences("MediaPlayer", Context.MODE_PRIVATE);
        editor = pref.edit();
        pa = new PlayerActivity();

        noSongs = (TextView) rootView.findViewById(R.id.textView3);
        allSongs = (ListView) rootView.findViewById(R.id.listView4);
        songsDetails = new ArrayList<>();
        getPlaylist();
        if(songsDetails.size() > 0 || songsDetails != null){
            noSongs.setVisibility(getView().INVISIBLE);
        }
        else{
            noSongs.setVisibility(getView().VISIBLE);
        }

        ListAdapter adapter = new PlaylistCustomAdapter(getActivity(), songsDetails);
        allSongs.setAdapter(adapter);

        allSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getActivity(), PlayerActivity.class);
                in.putExtra("position", position);

                editor.putString("SONG NAME", String.valueOf(songsDetails.get(position).get("Name")));
                editor.putInt("INDEX", position);
                editor.putString("FROM", "All Songs");
                editor.apply();
                startActivity(in);
                pa.playlist = songsDetails;
            }
        });

        return rootView;
    }

    ArrayList<HashMap<String, String>> getPlaylist(){
        final Cursor mCursor = getActivity().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DURATION,
                        MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media._ID}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ")ASC");

        int i=0;
        if(mCursor.moveToFirst()){
            do{
                String name = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                HashMap<String, String> details = new HashMap<>();
                if(name.length()>20){
                    details.put("Name", name.substring(0, 20)+"...");
                }
                else{
                    details.put("Name", name);
                }
                details.put("Path", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                details.put("Duration", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                details.put("Album", mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)));
                songsDetails.add(i, details);
                i++;
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return songsDetails;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            Intent in = new Intent(getActivity(), SearchActivity.class);
            startActivity(in);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setHasOptionsMenu(false);
    }
}





