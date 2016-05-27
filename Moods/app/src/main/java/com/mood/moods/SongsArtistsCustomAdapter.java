package com.mood.moods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class SongsArtistsCustomAdapter extends ArrayAdapter {

    ArrayList<HashMap<String, String>> songs;

    public SongsArtistsCustomAdapter(Context context, ArrayList<HashMap<String, String>> name) {
        super(context, R.layout.song_artist_custom_layout, name);
        songs = name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.song_artist_custom_layout, parent, false);
        TextView Name = (TextView) customView.findViewById(R.id.textView6);
        TextView duration = (TextView) customView.findViewById(R.id.textView7);

        Name.setText(songs.get(position).get("Name"));
        duration.setText(PlayerHelperClass.milliSecondsToTimer(Integer.parseInt((songs.get(position).get("Duration")))));
        return customView;
    }
}
