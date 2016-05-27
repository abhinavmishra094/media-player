package com.mood.moods;

import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class AlbumsFragment extends Fragment {

    String[] albums;
    String[] path;
    String[] albumArt;
    TextView noAlbums;
    GridView allAlbums;

    public AlbumsFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_albums_fragment, container, false);
        noAlbums = (TextView) rootView.findViewById(R.id.textView4);
        allAlbums = (GridView) rootView.findViewById(R.id.gridView);

        albums = getAlbums();
        if (albums.length > 0 || albums != null) {
            noAlbums.setVisibility(getView().INVISIBLE);
        }
        else{
            noAlbums.setVisibility(getView().VISIBLE);
        }
        ListAdapter adapter = new AlbumCustomAdapter(getActivity(), albums, albumArt);
        allAlbums.setAdapter(adapter);

        allAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(getActivity(), SongAlbums.class);
                in.putExtra("Albums", albums[position]);
                in.putExtra("Images", albumArt[position]);
                startActivity(in);
            }
        });

        return rootView;
    }

    public String[] getAlbums() {

        Cursor mCursor = getActivity().getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[] {MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums._ID}, null, null, "LOWER(" + MediaStore.Audio.Albums.ALBUM + ")ASC");
        int count = mCursor.getCount();
        String[] mAlbum = new String[count];
        albumArt = new String[count];
        int i = 0;
        if(mCursor.moveToFirst()){
            do {
                String s = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                mAlbum[i] = s;
                albumArt[i] = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ART));
                i++;
            }while (mCursor.moveToNext());
        }
        mCursor.close();
        return mAlbum;
    }
}
