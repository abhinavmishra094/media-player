package com.mood.moods;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Adarsh on 3/28/2016.
 */
public class VideoCustomAdapter extends ArrayAdapter<String> {
    int[] duration;
    Bitmap[] thumbnails;
    public VideoCustomAdapter(Context context, String[] names, int[] length, Bitmap[] thumb) {
        super(context, R.layout.custom_video_layout, names);
        duration = length;
        thumbnails = thumb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        View customView = inflator.inflate(R.layout.custom_video_layout, parent, false);
        String singleVideo = getItem(position);
        TextView name = (TextView) customView.findViewById(R.id.VideoName);
        TextView length = (TextView) customView.findViewById(R.id.VideoDuration);
        ImageView thumb = (ImageView) customView.findViewById(R.id.VideoImage);

        name.setText(singleVideo);
        thumb.setImageBitmap(thumbnails[position]);
        length.setText(PlayerHelperClass.milliSecondsToTimer(duration[position]));
        return customView;
    }
}
