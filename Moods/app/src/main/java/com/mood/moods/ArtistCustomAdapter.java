package com.mood.moods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Adarsh on 4/1/2016.
 */
public class ArtistCustomAdapter extends ArrayAdapter {
    public ArtistCustomAdapter(Context context, String[] artists) {
        super(context, R.layout.artist_custom_layout, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.artist_custom_layout, parent, false);
        String singleArtist = (String) getItem(position);
        TextView artists = (TextView) customView.findViewById(R.id.textView);

        if(singleArtist.length() > 16)
            artists.setText(singleArtist.substring(0, 16)+"...");
        else
        artists.setText(singleArtist);
        return customView;
    }
}
