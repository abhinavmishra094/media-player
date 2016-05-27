package com.mood.moods;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Adarsh on 3/25/2016.
 */
public class PlaylistCustomAdapter extends ArrayAdapter {

    ArrayList<HashMap<String, String>> songs;
    public PlaylistCustomAdapter(Context context, ArrayList<HashMap<String, String>> sName) {
        super(context, R.layout.playlist_custom_layout, sName);
        songs = sName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.playlist_custom_layout, parent, false);
        TextView name = (TextView) customView.findViewById(R.id.songName);
        TextView albumName = (TextView) customView.findViewById(R.id.songAlbum);
        TextView durationText = (TextView) customView.findViewById(R.id.songDuration);
        ImageView mp3 = (ImageView) customView.findViewById(R.id.songPic);

        name.setText(songs.get(position).get("Name"));
        durationText.setText(PlayerHelperClass.milliSecondsToTimer(Integer.parseInt(songs.get(position).get("Duration"))));
        albumName.setText(songs.get(position).get("Album"));
        return customView;
    }
}
