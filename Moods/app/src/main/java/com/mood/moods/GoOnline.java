package com.mood.moods;



import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoOnline extends Fragment {

    private List<Track> mListItems;
    private SCTrackAdapter mAdapter;
    private TextView mSelectedTrackTitle;
    private ImageView mSelectedTrackImage;
    private MediaPlayer mMediaPlayer;
    private ImageView mPlayerControl;

    public GoOnline() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_go_online, container, false);
        mListItems = new ArrayList<Track>();
        ListView listView = (ListView)rootView.findViewById(R.id.track_list_view);
        mSelectedTrackTitle = (TextView)rootView.findViewById(R.id.selected_track_title);
        mSelectedTrackImage = (ImageView)rootView.findViewById(R.id.selected_track_image);
        mPlayerControl = (ImageView)rootView.findViewById(R.id.player_control);
        mAdapter = new SCTrackAdapter(getActivity(), mListItems);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Track track = mListItems.get(position);

                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    mMediaPlayer.reset();
                }

                try {
                    mMediaPlayer.setDataSource(track.getStreamURL() + "?client_id=" + Config.CLIENT_ID);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mSelectedTrackTitle.setText(track.getTitle());
                Picasso.with(getActivity()).load(track.getArtworkURL()).into(mSelectedTrackImage);

            }
        });

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                togglePlayPause();
            }
        });

        mPlayerControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePlayPause();
            }
        });

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayerControl.setImageResource(android.R.drawable.ic_media_play);
            }
        });

        SCService scService = SoundCloud.getService();
        scService.getRecentTracks(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                if(tracks.size() <=0){
                    Toast.makeText(getActivity(), "No Songs", Toast.LENGTH_SHORT).show();
                }else{
                    loadTracks(tracks);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("Hey", "Error: " + error);
            }
        });
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void loadTracks(List<Track> tracks) {
        mListItems.clear();
        mListItems.addAll(tracks);
        mAdapter.notifyDataSetChanged();
    }
    private void togglePlayPause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mPlayerControl.setImageResource(android.R.drawable.ic_media_play);
        } else {
            mMediaPlayer.start();
            mPlayerControl.setImageResource(android.R.drawable.ic_media_pause);
        }
    }

}
