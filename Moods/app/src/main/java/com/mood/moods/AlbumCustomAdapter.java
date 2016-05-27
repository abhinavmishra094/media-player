package com.mood.moods;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Adarsh on 3/31/2016.
 */
public class AlbumCustomAdapter extends ArrayAdapter<String> {
    String[] albumArt;
    public AlbumCustomAdapter(Context context, String[] album, String[] albumart) {
        super(context, R.layout.album_custom_layout, album);
        albumArt = albumart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.album_custom_layout, parent, false);
        String singleAlbum = getItem(position);
        ImageView  album = (ImageView) customView.findViewById(R.id.imageView);
        TextView albumName = (TextView) customView.findViewById(R.id.textView5);

        if(singleAlbum.length() > 16){
            albumName.setText(singleAlbum.substring(0, 16)+"...");
        }
        else{
            albumName.setText(singleAlbum);
        }
        if(albumArt[position] == null || albumArt == null) {
            album.setImageResource(R.drawable.music);
        } else {
            album.setImageDrawable(Drawable.createFromPath(albumArt[position]));
        }
        return customView;
    }
}
