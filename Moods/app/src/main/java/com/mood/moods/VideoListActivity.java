package com.mood.moods;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class VideoListActivity extends Fragment {

    ListView list;
    static String[] names;
    static int[] length;
    static String[] path;
    static Bitmap[] thumbnails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_video_list, container, false);
        names = getVideos();
        list = (ListView) rootView.findViewById(R.id.listView3);
        ListAdapter adapter = new VideoCustomAdapter(getActivity(), names, length, thumbnails);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), path[position], Toast.LENGTH_LONG).show();
                Intent in = new Intent(getActivity(), VideoPlayer.class);
                in.putExtra("Path", path[position]);
                startActivity(in);
            }
        });
        return rootView;
    }

    String[] getVideos(){
        final Cursor mCursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DURATION}, null, null,
                "LOWER(" + MediaStore.Video.Media.DISPLAY_NAME +")ASC");

        int count = mCursor.getCount();
        String[] vid = new String[count];
        length = new int[count];
        path = new String[count];
        thumbnails = new Bitmap[count];

        int i=0;
        if(mCursor.moveToFirst()){
            do{
                vid[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
                length[i] = mCursor.getInt(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION));
                path[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                thumbnails[i] = ThumbnailUtils.createVideoThumbnail(path[i], MediaStore.Video.Thumbnails.MINI_KIND);
                i++;
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return vid;
    }
}
