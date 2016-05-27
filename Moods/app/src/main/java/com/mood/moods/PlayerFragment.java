package com.mood.moods;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {

    ArrayList<HashMap<String, String>> playlist;
    SharedPreferences songs;

    public PlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        songs = getActivity().getSharedPreferences("MediaPlayer", Context.MODE_PRIVATE);
        Log.d("Hello", "1");
        Toast.makeText(getActivity(), songs.getString("SONG NAME", "Song"), Toast.LENGTH_LONG).show();
        Log.d("Hello", "2");
        Toast.makeText(getActivity(), String.valueOf(songs.getInt("INDEX", 0)), Toast.LENGTH_LONG).show();
        Log.d("Hello", "3");
        Toast.makeText(getActivity(), songs.getString("FROM", "From"), Toast.LENGTH_LONG).show();
        Log.d("Hello", "4");

        return rootView;
    }

}
