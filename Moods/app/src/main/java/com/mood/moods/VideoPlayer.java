package com.mood.moods;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoPlayer extends AppCompatActivity {

    android.support.v7.app.ActionBar ab;
    VideoView video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_video_player);
        Intent in = getIntent();
        String path = in.getStringExtra("Path");
        Toast.makeText(getApplicationContext(), path, Toast.LENGTH_LONG);
        video = (VideoView) findViewById(R.id.videoView);
        video.setMediaController(new android.widget.MediaController(this));
        video.setVideoPath(path);
        video.requestFocus();
        video.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        System.out.println("Configuration Changed");

    }
}
