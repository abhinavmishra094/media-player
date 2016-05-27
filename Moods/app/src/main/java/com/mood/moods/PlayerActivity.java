package com.mood.moods;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PlayerActivity extends AppCompatActivity {

    public static ImageButton prev, play, next, replay, shuffle;
    public static TextView currentTime, totalTime;
    public static SeekBar seek;
    public static MediaPlayer myPlayer = new MediaPlayer();
    public static ArrayList<HashMap<String, String>> playlist = new ArrayList<>();
    static Handler mHandler;
    static int index;
    Boolean isShuffle = false;
    static android.support.v7.app.ActionBar ab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        mHandler = new Handler();
        Intent in = getIntent();
        index = in.getIntExtra("position", 0);
        shuffle = (ImageButton) findViewById(R.id.shuffle);
        replay = (ImageButton) findViewById(R.id.replay);
        prev = (ImageButton) findViewById(R.id.prev);
        play = (ImageButton) findViewById(R.id.play);
        next = (ImageButton) findViewById(R.id.nxt);
        currentTime = (TextView) findViewById(R.id.textView9);
        totalTime = (TextView) findViewById(R.id.textView8);
        seek = (SeekBar) findViewById(R.id.seekBar);

        myPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                    nextSong();
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShuffle == true){
                    shuffle.setImageResource(R.drawable.shuffle_off);
                    Toast.makeText(getApplicationContext(), "Shuffle Off", Toast.LENGTH_SHORT).show();
                    isShuffle = false;
                }else{
                    shuffle.setImageResource(R.drawable.shuffle_on);
                    Toast.makeText(getApplicationContext(), "Shuffle On", Toast.LENGTH_SHORT).show();
                    isShuffle = true;
                }
            }
        });

        replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (myPlayer.isLooping()){
//                    myPlayer.setLooping(false);
//                    replay.setImageResource(R.drawable.replay_off);
//                    Toast.makeText(getApplicationContext(), "Repeat Off", Toast.LENGTH_LONG).show();
//                }else{
//                    myPlayer.setLooping(true);
//                    replay.setImageResource(R.drawable.replay_on);
//                    Toast.makeText(getApplicationContext(), "Repeat On", Toast.LENGTH_LONG).show();
//                }
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPlayer.isPlaying()) {
                    play.setImageResource(android.R.drawable.ic_media_play);
                    myPlayer.pause();
                } else {
                    play.setImageResource(android.R.drawable.ic_media_pause);
                    myPlayer.start();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextSong();
                replay.setImageResource(R.drawable.replay_off);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevSong();
                replay.setImageResource(R.drawable.replay_off);
            }
        });

        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTime);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTime);
                myPlayer.seekTo(seekBar.getProgress());
                updateSeekBar();
            }
        });

        playSong(index);
    }

    public static void playSong(int index) {
        try {
            myPlayer.reset();
            myPlayer.setDataSource(playlist.get(index).get("Path"));
            myPlayer.prepare();
            myPlayer.start();
            ab.setTitle(playlist.get(index).get("Name"));

            play.setImageResource(android.R.drawable.ic_media_pause);
            seek.setMax(myPlayer.getDuration());
            totalTime.setText(PlayerHelperClass.milliSecondsToTimer(myPlayer.getDuration()));
            seek.setProgress(0);

        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        updateSeekBar();
    }

    static void updateSeekBar(){
        mHandler.postDelayed(mUpdateTime, 100);
    }

    static Runnable mUpdateTime = new Runnable() {
        @Override
        public void run() {
            Integer currentDuration = myPlayer.getCurrentPosition();
            currentTime.setText(""+PlayerHelperClass.milliSecondsToTimer(currentDuration));
            seek.setProgress(currentDuration);
            mHandler.postDelayed(this, 100);
        }
    };

    public void prevSong(){
            if(isShuffle == true){
                playSong(randomWithRange(0, playlist.size()-1));
            }else{
                if (index > 0){
                    playSong(index - 1);
                    index = index - 1;
                }else{
                    playSong(playlist.size()-1);
                    index = playlist.size()-1;
                }
            }
    }
    public void nextSong(){
            if(isShuffle == true){
                playSong(randomWithRange(0, playlist.size()-1));
            }else{
                if (index < playlist.size()-1){
                    playSong(index + 1);
                    index = index + 1;
                }else{
                    playSong(0);
                    index = 0;
                }
            }
    }

    int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
